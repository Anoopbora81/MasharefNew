package com.sr.masharef.masharef.gallary;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;
import com.sr.masharef.masharef.R;
import com.sr.masharef.masharef.model.ABasicMessage;
import com.sr.masharef.masharef.model.JsonObj;
import com.sr.masharef.masharef.utility.Log;
import com.sr.masharef.masharef.utility.Utils;
import com.sr.masharef.masharef.webservice.api.UserServices;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLConnection;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GalleryLibraryFlie.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GalleryLibraryFlie#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GalleryLibraryFlie extends DialogFragment  implements MediaPlayer.OnCompletionListener, SeekBar.OnSeekBarChangeListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private int PICK_REQUEST_LIBRARY = 2;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    ImageView imageLibrary,play_button,pause_button;
    Intent libraryIntent;
    private OnFragmentInteractionListener mListener;
    Button uploadLibraryDone,uploadLibraryCancel;
    Bitmap bitmap = null;
    private VideoView galleryVideoView;
    RelativeLayout library_image_layout,library_video_layout,library_audio_layout;
    SeekBar audio_seekbar;
    MediaPlayer mp;
    public static  boolean cancelValue;
    String mimeType,fileString;
    File uploadFile;
    View viewController;
    Uri uri;
    String is_private;
    RadioButton galleryPravite,galleryPublic;
    private Handler mHandler = new Handler();
    private ProgressDialog progressDialog;
    float compressed_fileSize;
    MediaController mediaController;

    public GalleryLibraryFlie() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GalleryLibraryFlie.
     */
    // TODO: Rename and change types and number of parameters
    public static GalleryLibraryFlie newInstance(String param1, String param2) {
        GalleryLibraryFlie fragment = new GalleryLibraryFlie();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCanceledOnTouchOutside(false);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setStyle(STYLE_NO_FRAME, R.style.AppTheme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View libraryView= inflater.inflate(R.layout.gallery_library_flie, container, false);
        final ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(false);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("Gallery");
        String value = getArguments().getString("filePath");
        final Toolbar toolbar = (Toolbar)((AppCompatActivity)getActivity()).findViewById(R.id.toolbar);
        // toolbar.setNavigationIcon(R.drawable.cal_left_arrow_on);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionBar.setDisplayHomeAsUpEnabled(false);
                actionBar.setDisplayShowCustomEnabled(false);
                actionBar.setDisplayShowTitleEnabled(true);
                if(getActivity() != null)
                    getActivity().onBackPressed();

            }
        });
        imageLibrary = (ImageView)libraryView.findViewById(R.id.library_image);
        galleryVideoView = (VideoView)libraryView. findViewById(R.id.library_video);
        library_image_layout = (RelativeLayout)libraryView.findViewById(R.id.library_image_layout);
        library_video_layout = (RelativeLayout)libraryView.findViewById(R.id.library_video_layout);
        library_audio_layout = (RelativeLayout)libraryView.findViewById(R.id.library_audio_layout);
        audio_seekbar = (SeekBar)libraryView.findViewById(R.id.library_seekBar);
        play_button = (ImageView)libraryView.findViewById(R.id.play_button);
        pause_button = (ImageView)libraryView.findViewById(R.id.pause_button);
        viewController = (View)libraryView.findViewById(R.id.controller_view);
        galleryPravite = (RadioButton)libraryView.findViewById(R.id.private_rdo);
        galleryPublic = (RadioButton)libraryView.findViewById(R.id.public_rdo);
        galleryPravite.setChecked(true);
        mediaController = new MediaController(getActivity());
        intializeUi();
        mp=new MediaPlayer();
        audio_seekbar.setProgress(0);
        audio_seekbar.setMax(100);
        audio_seekbar.setOnSeekBarChangeListener(this); // Important
        mp.setOnCompletionListener(this);

//            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), libraryIntent.getData());
        if(libraryIntent==null){
            fileString=/*GetFilePathFromDevice.getPath(getActivity(),uri);*/value;
        }else {
            fileString = value;/*GetFilePathFromDevice.getPath(getActivity(), libraryIntent.getData());*/
        }
            uploadFile= new File(fileString);
             uri = Uri.fromFile(uploadFile);
            mimeType = URLConnection.guessContentTypeFromName(fileString);

            if (mimeType != null && mimeType.startsWith("image")) {
                //handle image
                library_image_layout.setVisibility(View.VISIBLE);
                library_video_layout.setVisibility(View.GONE);
                library_audio_layout.setVisibility(View.GONE);
//                Uri uri = Uri.fromFile(uploadFile);
                imageLibrary.setImageURI(uri);
            } else  if (mimeType != null && mimeType.startsWith("video")) {
                //handle video
                library_image_layout.setVisibility(View.GONE);
                library_video_layout.setVisibility(View.VISIBLE);
                library_audio_layout.setVisibility(View.GONE);
                mediaController.setAnchorView(viewController);
                galleryVideoView.setMediaController(mediaController);
                galleryVideoView.setVideoURI(uri);
                galleryVideoView.start();

            }  else if(mimeType != null && mimeType.startsWith("audio")){
                try{
                    library_image_layout.setVisibility(View.GONE);
                    library_video_layout.setVisibility(View.GONE);
                    library_audio_layout.setVisibility(View.VISIBLE);
                    mp.setDataSource(getActivity(),uri);
                    mp.prepare();
//                    play_button.setBackgroundResource(R.drawable.del_play_button);

                }catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        galleryVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                                          /*
                                           *  add media controller
                                           */
                        mediaController = new MediaController(getActivity());;
                                          /*
                                           * and set its position on screen
                                           */
                        mediaController.setAnchorView(galleryVideoView);
                    }
                });
            }
        });

            play_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    play_button.setVisibility(View.GONE);
                    pause_button.setVisibility(View.VISIBLE);
                    mp = new MediaPlayer();
                    mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    try {
                        mp.setDataSource(getActivity(), uri);
                    } catch (IllegalArgumentException e) {
                        Toast.makeText(getActivity(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
                    } catch (SecurityException e) {
                        Toast.makeText(getActivity(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
                    } catch (IllegalStateException e) {
                        Toast.makeText(getActivity(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        mp.prepare();
                    } catch (IllegalStateException e) {
                        Toast.makeText(getActivity(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        Toast.makeText(getActivity(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
                    }
                    mp.start();
                    mHandler.postDelayed(mUpdateTimeTask, 10);
                }
            });

            pause_button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                play_button.setVisibility(View.VISIBLE);
                pause_button.setVisibility(View.GONE);
                if(mp!=null && mp.isPlaying()){
                    mp.stop();
                }
            }
        });

        uploadLibraryDone = (Button)libraryView.findViewById(R.id.buttondoneGalleryLib);
        uploadLibraryCancel = (Button)libraryView.findViewById(R.id.buttonCancelGalleryLib);
        uploadLibraryCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelValue=true;
                if(mp.isPlaying()){
                    mp.stop();
                    play_button.setVisibility(View.VISIBLE);
                    pause_button.setVisibility(View.GONE);
                }
                if(galleryVideoView.isPlaying())
                    galleryVideoView.pause();
                getActivity().onBackPressed();
            }
        });

        uploadLibraryDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(galleryPravite.isChecked()){
                    is_private="1";
                }
                else if(galleryPublic.isChecked()){
                    is_private="0";
                }
                String mediaType="";
                File imageFile =new File(fileString);
                if(galleryVideoView.isPlaying())
                galleryVideoView.pause();

                if(mp.isPlaying()) {
                    mp.stop();
                    audio_seekbar.setProgress(0);
                    pause_button.setVisibility(View.GONE);
                    play_button.setVisibility(View.VISIBLE);
                }
                String cmd = null;
                String[] command = null;
                String newFilePath= null;
               /* if(bitmap!=null){
                    tempUri = getImageUri(getActivity(), bitmap);
                    imageFile = new File(getRealPathFromURI(tempUri));
                }else{
                    imageFile = new File(getRealPathFromURI(tempUri));
                }*/

                boolean isValidMedia = true;
                long fileSizeInBytes = imageFile.length();
                long fileSizeInKB = fileSizeInBytes / 1024;
                float fileSizeInMB = (float)fileSizeInKB / 1024;

                if (mimeType != null && mimeType.startsWith("image")) {
                    mediaType="1";
                    if(fileSizeInMB >=2)
                    compressImage();
                }else  if (mimeType != null && mimeType.startsWith("video")) {
                    mediaType="2";
                    /*if(fileString.contains("WhatsApp") && fileSizeInMB >=2) {
                        isValidMedia = false;
                        errorDialog("The File size exceeding, so not allowed for upload", "video");
                    }
                    if(!fileString.contains("WhatsApp")) {
                        progressDialog.setTitle("Compressing Video File...");
                        compressed_fileSize = (fileSizeInMB / 14.0f) * 1024;
                        newFilePath = fileString.substring(0, fileString.indexOf(".mp4")) + "_new.mp4";
                        cmd = "-y -i "; //+fileString+" -s 320x240 -r 20 -c:v libx264 -preset ultrafast -c:a copy -me_method zero -tune fastdecode -tune zerolatency -strict -2 -b:v 1000k -pix_fmt yuv420p "+newFilePath;
                        String cmd2 = "-s 320x240 -r 20 -c:v libx264 -preset ultrafast -c:a copy -me_method zero -tune fastdecode -tune zerolatency -strict -2 -b:v 1000k -pix_fmt yuv420p ";
                        String[] command1 = cmd.split(" ");
                        String[] cmd_withOldFile = concatTwoStringArrays(command1, new String[]{fileString});
                        String[] command2 = cmd2.split(" ");
                        String[] cmd_withNewFile = concatTwoStringArrays(command2, new String[]{newFilePath});
                        command = concatTwoStringArrays(cmd_withOldFile, cmd_withNewFile);
                    }*/
                }
                else if(mimeType != null && mimeType.startsWith("audio")){
                    mediaType="3";
                    if(fileString.contains("WhatsApp") && fileSizeInMB >=2) {
                        isValidMedia = false;
                        errorDialog("The File size exceeding, so not allowed for upload", "audio");
                    }
                    if(!fileString.contains("WhatsApp") && fileSizeInMB >=2) {
                        String cmd2 = null;
                        progressDialog.setTitle("Compressing Audio File...");
                        if(fileString.contains(".mp3")) {
                            newFilePath = fileString.substring(0, fileString.indexOf(".mp3")) + "_new.mp3";
                            cmd2 = "-ar 44100 -ac 2 -ab 64k -f mp3";
                        }
                        else if(fileString.contains(".m4a")) {
                            newFilePath = fileString.substring(0, fileString.indexOf(".m4a")) + "_new.m4a";
                            cmd2 = "-ar 44100 -ac 2 -ab 64k -f m4a";
                        }
                        //"-y -i /storage/emulated/0/Download/google.mp3 -ar 44100 -ac 2 -ab 64k -f mp3 /storage/emulated/0/Download/outaudio.mp3"
                        // String cmd_audio = "ffmpeg -i "+fileString+" -codec:a libmp3lame -qscale:a 2 "+newFilePath;

                       if(cmd2 != null) {
                           cmd = "-y -i ";
                           String[] command1 = cmd.split(" ");
                           String[] cmd_withOldFile = concatTwoStringArrays(command1, new String[]{fileString});
                           String[] command2 = cmd2.split(" ");
                           String[] cmd_withNewFile = concatTwoStringArrays(command2, new String[]{newFilePath});
                           command = concatTwoStringArrays(cmd_withOldFile, cmd_withNewFile);
                       }
                    }

                }
                if(command != null && newFilePath != null) {
                    loadFFMpegBinary();
                    execFFmpegBinary(command, mediaType, is_private, newFilePath);
                }
                 else if(isValidMedia)
                {
                    uploadMediaLibrary(getActivity(),mediaType, is_private, fileString);
                }
            }
        });
        actionBar.setDisplayHomeAsUpEnabled(true);
        return libraryView;
    }
    public static String[] concatTwoStringArrays(String[] s1, String[] s2){
        String[] result = new String[s1.length+s2.length];
        int i;
        for (i=0; i<s1.length; i++)
            result[i] = s1[i];
        int tempIndex =s1.length;
        for (i=0; i<s2.length; i++)
            result[tempIndex+i] = s2[i];
        return result;
    }//concatTwoStringArrays().
    private void compressImage() {
        FileOutputStream outStream = null;
        String outputFile = null;
        try {
            Bitmap mBitmap = BitmapFactory.decodeFile(fileString);// getBitmap(fileString);
            if(fileString.contains(".jpg")) {
                outputFile = fileString.substring(0, fileString.indexOf(".jpg")) + "_new.jpg";
                outStream = new FileOutputStream(new File(outputFile));
                mBitmap.compress(Bitmap.CompressFormat.JPEG, 70, outStream);
            }
            else if(fileString.contains(".png")) {
                outputFile = fileString.substring(0, fileString.indexOf(".png")) + "_new.png";
                outStream = new FileOutputStream(new File(outputFile));
                mBitmap.compress(Bitmap.CompressFormat.PNG, 70, outStream);
            }
            outStream.flush();
        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (null != outStream) {
                try {
                    File newFile = new File(outputFile);
                    android.util.Log.d(TAG, "outputFile Size===================== : "+newFile.length());
                    fileString = outputFile;
                    outStream.close();
                } catch (IOException io) {
                    io.printStackTrace();
                }
            }
        }
    }

    private void intentType(String intentType){
        Intent intentLibrary = new Intent();
        intentLibrary.setType(intentType+"/*");
        intentLibrary.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intentLibrary, ""), PICK_REQUEST_LIBRARY);
    }


    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {

            if(mp.isPlaying()) {

                int mediaPos_new = mp.getCurrentPosition();
                int mediaMax_new = mp.getDuration();
                audio_seekbar.setMax(mediaMax_new);
                audio_seekbar.setProgress(mediaPos_new);
            }
            else
            {
                audio_seekbar.setProgress(0);
                play_button.setVisibility(View.VISIBLE);
                pause_button.setVisibility(View.GONE);
//                play_button.setBackgroundResource(R.drawable.del_play_button);

            }

            mHandler.postDelayed(this, 10);
        }
    };

    private void intializeUi() {
        library_image_layout.setVisibility(View.GONE);
        library_video_layout.setVisibility(View.GONE);
        library_audio_layout.setVisibility(View.GONE);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public Uri getImageUri(final Context inContext,final Bitmap inImage) {

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        //inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);\
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);

        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public void uploadMediaLibrary(final Context context, final String mediaType,  final String is_private,  final String newFilePath){

       /* AccessToken accessToken = null;
        try {
            accessToken = KeychainManager.storedAccessToken(context);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String accTokenValue = accessToken.getToken();
        final HashMap<String, String> mHeaders = new HashMap<String, String>();
        mHeaders.put("Access-Token", accTokenValue.toString());

        final File newImageFile = new File(newFilePath);

        HashMap params = new HashMap();
        params.put("media_type",mediaType);
        params.put("is_private", is_private);
        params.put("media", newImageFile);

        String url = ApiServiceUtils.getUrl(getActivity(), R.string.uploadmedia, null);
        JsonObjectRequest stringRequest = new JsonObjectRequest (*//*"http://mashareftest.systemrapid.systems/jsonapi/registerDeviceNotification"*//* url, new JSONObject(params),

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        try {
                        Log.i("LoginActivity",response.toString());
                        //   Toast.makeText(LoginActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // progressDialog.dismiss();
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {


            public Map<String, String> getHeaders() {
                return mHeaders;
            }
        };
        MyVolley.getInstance(getActivity()).addToRequestQueue(stringRequest);
*/


        final ProgressDialog progress = Utils.getProgress(context, context.getString(R.string.processing_upload));
        final File newImageFile = new File(newFilePath);
        new Thread(new Runnable() {

            public void run() {

                JSONObject param = new JSONObject();
                try {
                    param.putOpt("media_type",mediaType);
                    param.putOpt("is_private", is_private);
                    param.putOpt("media", newImageFile);

                    Log.i("newFilePath", newFilePath);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                UserServices userServ = new UserServices();
                boolean callStatus 	= userServ.uploadMedia(context, param, new UserServices.UserServiceListeners() {

                    public void onSuccess(JsonObj model) {

                        //AUserDetail user = (AUserDetail)model;
                        final ABasicMessage msg =  (ABasicMessage)model;

                        if(msg == null)
                            Log.e("GalleryLibraryFile"+msg.getMessage());
                            getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progress.dismiss();
                                showThanksDialog( msg.getMessage());
                               // newImageFile.delete();
//                                Utils.alert(context, R.drawable.del_success_alert,R.string.gallery_upload_success, msg.getMessage());
                            }
                        });
                        progress.dismiss();

                    }

                    public void onForceLogout(Exception error) {
                        Log.e(error.getMessage()+"  GalleryLibraryFile Class");
                        progress.dismiss();
                    }

                    public void onFailure(Exception error) {
                        Log.e(error.getMessage()+" GalleryLibraryFile Class");
                        Utils.showAlertOnMainThread(context, error.getMessage());
                        progress.dismiss();
                    }

                });

                if(!callStatus)
                    progress.dismiss();

            }

        }).start();
    }


    public  void showThanksDialog(String successMsg) {

        final Dialog dialog = new Dialog((Activity)getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.thanks_dialog_layout);
        TextView succesText = (TextView)dialog.findViewById(R.id.dialog_text);
        succesText.setText(successMsg);
        dialog.setCancelable(false);

        Button buttonDone = (Button) dialog.findViewById(R.id.buttonDone);

        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelValue=false;
                getActivity().onBackPressed();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
       /* if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
       /* if(mp != null && b){
            mp.seekTo(i * 1000);
        }*/
        if(b){
            mp.seekTo(i);
            audio_seekbar.setProgress(i);}

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
//        mHandler.removeCallbacks(mUpdateTimeTask);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
//        mHandler.removeCallbacks(mUpdateTimeTask);
        int totalDuration = mp.getDuration();
        int currentPosition = mp.getCurrentPosition() / 100;


        // forward or backward to certain seconds
        mp.seekTo(currentPosition);

        // update timer progress again
//        updateProgressBar();
    }

    private void errorDialog(String errorMsg, final String type){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        android.app.AlertDialog d = builder.create();
        d.setCanceledOnTouchOutside(false);
        // d.setMessage(""+R.string.exit_msg);
        d.setIcon(R.mipmap.ic_launcher);
        d.setMessage(errorMsg);
        d.setButton(android.app.AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if(type.equalsIgnoreCase("audio")){
                            intentType("audio");
                        }
                        if(type.equalsIgnoreCase("video")){
                            intentType("video");
                        }    }
                });
        d.show();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    String TAG ="execFFmpegBinary";
    private void execFFmpegBinary(final String[] command, final String mediaType, final String is_private, final String newFilePath) {

        FFmpeg ffmpeg = FFmpeg.getInstance(getActivity());
        try {
            ffmpeg.execute(command, new ExecuteBinaryResponseHandler() {
                @Override
                public void onFailure(String s) {
                    // addTextViewToLayout("FAILED with output : "+s);
                    android.util.Log.d(TAG, "FAILED with output : "+s);
                }

                @Override
                public void onSuccess(String s) {
                    //addTextViewToLayout("SUCCESS with output : "+s);
                    android.util.Log.d(TAG, "SUCCESS with output : "+s);
                    File newFile = new File(newFilePath);
                    android.util.Log.d(TAG, "newFilePath====================== : "+newFilePath);
                    android.util.Log.d(TAG, "newFileSize===================== : "+newFile.length());
                    uploadMediaLibrary(getActivity(),mediaType, is_private, newFilePath);
                }

                @Override
                public void onProgress(String s) {

//                    addTextViewToLayout("progress : "+s);

                    if(s.contains("frame"))
                    {
                        android.util.Log.d(TAG, "Started command : ffmpeg "+"progress : "+s.trim());

                        if(s.contains("size=")) {
                            String s2 = s.substring(s.indexOf("size=") + 5, s.indexOf("kB"));
                            float chunks = Float.parseFloat(s2);
                            int percentageCompressed = (int) ((chunks / compressed_fileSize) * 100);
                            if(percentageCompressed >= 98)
                            {
                                percentageCompressed = 98;
                            }
                            progressDialog.setMessage("Processed "+percentageCompressed+"%"+" of file");
                            android.util.Log.d(TAG, "Processing ==== "+percentageCompressed+"%");
                        }
                        //  android.util.Log.d(TAG, "FAILED with output : "+s);
                        // progressDialog.setMessage("Processing\n"+percentageCompressed+"%");
                    }

                }

                @Override
                public void onStart() {
//                    outputLayout.removeAllViews();
                    android.util.Log.d(TAG, "Started command : ffmpeg " + command);
                    progressDialog.setMessage("Processing...");
                    progressDialog.show();
                }

                @Override
                public void onFinish() {
                    android.util.Log.d(TAG, "Finished command : ffmpeg "+command);
                    progressDialog.dismiss();
                 //   uploadMediaLibrary(getActivity(),mediaType, is_private, fileString);
                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            // do nothing for now
        }
    }

    private void loadFFMpegBinary() {
        FFmpeg ffmpeg = FFmpeg.getInstance(getActivity());
        try {
            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {
                @Override
                public void onFailure() {
//                    showUnsupportedExceptionDialog();
                    Utils.alert(getActivity(), "device not supported");
                }
            });
        } catch (FFmpegNotSupportedException e) {
            //showUnsupportedExceptionDialog();
            Utils.alert(getActivity(), "device not supported");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_REQUEST_LIBRARY)
        {
            if(resultCode == RESULT_OK) {
                //  uploadFile= new File(data.getData());
                fileString = GetFilePathFromDevice.getPath(getActivity(), data.getData());
                uri = data.getData();
                mimeType = URLConnection.guessContentTypeFromName(fileString);

                /*if (mimeType != null && mimeType.startsWith("image")) {
                    //handle image
                    library_image_layout.setVisibility(View.VISIBLE);
                    library_video_layout.setVisibility(View.GONE);
                    library_audio_layout.setVisibility(View.GONE);
//                Uri uri = Uri.fromFile(uploadFile);
                    imageLibrary.setImageURI(uri);
                } else*/ if (mimeType != null && mimeType.startsWith("video")) {
                    //handle video
                    library_image_layout.setVisibility(View.GONE);
                    library_video_layout.setVisibility(View.VISIBLE);
                    library_audio_layout.setVisibility(View.GONE);
                    mediaController.setAnchorView(viewController);
                    galleryVideoView.setMediaController(mediaController);
                    galleryVideoView.setVideoURI(uri);
                    galleryVideoView.start();

                } else if (mimeType != null && mimeType.startsWith("audio")) {
                    try {
                        library_image_layout.setVisibility(View.GONE);
                        library_video_layout.setVisibility(View.GONE);
                        library_audio_layout.setVisibility(View.VISIBLE);
                        mp.setDataSource(getActivity(), uri);
                        mp.prepare();
//                    play_button.setBackgroundResource(R.drawable.del_play_button);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    /**
     * Compress file for Uploading the file to server
     * */
  /*  private class CompressFile extends AsyncTask<String[], Integer, String> {
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            progressBar.setProgress(0);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Making progress bar visible
            progressBar.setVisibility(View.VISIBLE);

            // updating progress bar value
            progressBar.setProgress(progress[0]);

            // updating percentage value
            //  txtPercentage.setText(String.valueOf(progress[0]) + "%");
        }

        @Override
        protected String doInBackground(String[]... params) {
            // execFFmpegBinary(params[0]);
            uploadFile(params[0]);
            return "";
        }

        @SuppressWarnings("deprecation")
        private String uploadFile(String[] command1) {
            String responseString = null;
            execFFmpegBinary(command1);
            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            Log.e(TAG, "Response from server: " + result);
            super.onPostExecute(result);
        }


        private void execFFmpegBinary(final String[] command) {
            FFmpeg ffmpeg = FFmpeg.getInstance(getActivity());
            try {
                ffmpeg.execute(command, new ExecuteBinaryResponseHandler() {
                    @Override
                    public void onFailure(String s) {
                        // addTextViewToLayout("FAILED with output : "+s);
                        android.util.Log.d(TAG, "FAILED with output : "+s);
                    }

                    @Override
                    public void onSuccess(String s) {
                        //addTextViewToLayout("SUCCESS with output : "+s);
                        android.util.Log.d(TAG, "SUCCESS with output : "+s);
                    }

                    @Override
                    public void onProgress(String s) {

//                    addTextViewToLayout("progress : "+s);

                        if(s.contains("frame"))
                        {
                            android.util.Log.d(TAG, "Started command : ffmpeg "+"progress : "+s.trim());

                            if(s.contains("size=")) {
                                String s2 = s.substring(s.indexOf("size=") + 5, s.indexOf("kB"));
                                float chunks = Float.parseFloat(s2);
                                int percentageCompressed = (int) ((chunks / compressed_fileSize) * 100);
                                if(percentageCompressed >= 98)
                                {
                                    percentageCompressed = 98;
                                }
                                // progressDialog.setMessage("Processing\n"+percentageCompressed+"%");
                                android.util.Log.d(TAG, "Processing ==== "+percentageCompressed+"%");
                                publishProgress(percentageCompressed);
                            }
                            //  android.util.Log.d(TAG, "FAILED with output : "+s);
                            // progressDialog.setMessage("Processing\n"+percentageCompressed+"%");
                        }

                    }

                    @Override
                    public void onStart() {
//                    outputLayout.removeAllViews();
                        android.util.Log.d(TAG, "Started command : ffmpeg " + command);
//                        progressDialog.setMessage("Processing...");
//                        progressDialog.show();
                    }

                    @Override
                    public void onFinish() {
                        android.util.Log.d(TAG, "Finished command : ffmpeg "+command);
//                        progressDialog.dismiss();
                    }
                });
            } catch (FFmpegCommandAlreadyRunningException e) {
                // do nothing for now
            }
        }
    }*/
}
