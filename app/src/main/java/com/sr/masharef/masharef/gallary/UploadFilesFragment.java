package com.sr.masharef.masharef.gallary;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sr.masharef.masharef.R;
import com.sr.masharef.masharef.model.ABasicMessage;
import com.sr.masharef.masharef.model.JsonObj;
import com.sr.masharef.masharef.utility.Log;
import com.sr.masharef.masharef.utility.Utils;
import com.sr.masharef.masharef.webservice.api.UserServices;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public /*abstract*/ class UploadFilesFragment extends Fragment  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ImageView circleImage;
    ImageView display_image;
    String chooseTypeCamera;
    TextView uploadDone;
    File imageFile;

    Preview preview;


    private LinearLayout cameraPreview;

    private boolean cameraFront = false;

    private int cameraId = 0;
    private static final int REQUEST_CAMERA = 1;
    private int PICK_IMAGE_REQUEST = 2;
    private int REQUEST_PERMISSION_CAMERA = 12;
    private int CAMERA_PERMISSIONS = 13;
    private String imgPath;
    Uri imageUri;

    private RelativeLayout cameraLinearPreview,flash_layout;
    private Context myContext;
    private CameraPreview mPreview;
    private Camera mCamera;
    private Camera.PictureCallback mPicture;
    ImageView turnImage,flashImage;
    private boolean isFlashOn,hasFlash;
    Camera.Parameters params;
    TextView photoText,videoText,audioText;
    View photoView,videoView,audioView;
    private OnFragmentInteractionListener mListener;

    public UploadFilesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
     * @return A new instance of fragment UploadFilesFragment.
     */
    // TODO: Rename and change types and number of parameters
//    public static UploadFilesFragment newInstance(String param1, String param2) {
//        UploadFilesFragment fragment = new UploadFilesFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        checkPermissionsCamera();
        /*int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA);
        }else{
            requestWritePermission(getActivity());
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View uploadView=inflater.inflate(R.layout.upload_files, container, false);

//        chooseTypeCamera = getArguments().getString("camera");

        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        myContext = getActivity();
        cameraLinearPreview = (RelativeLayout)uploadView.findViewById(R.id.surfaceview);
        mPreview = new CameraPreview(myContext, mCamera);
        cameraLinearPreview.addView(mPreview);


        circleImage = (ImageView)uploadView. findViewById(R.id.circleImage);
        turnImage = (ImageView)uploadView. findViewById(R.id.switchImage);
        flashImage = (ImageView)uploadView. findViewById(R.id.flashImage);
        flash_layout = (RelativeLayout) uploadView. findViewById(R.id.flash_layout);
        photoText = (TextView) uploadView.findViewById(R.id.photoText);
        videoText = (TextView) uploadView.findViewById(R.id.videoText);
        audioText = (TextView) uploadView.findViewById(R.id.audioText);
        photoView = (View) uploadView.findViewById(R.id.photoView);
        videoView = (View) uploadView.findViewById(R.id.videoView);
        audioView = (View) uploadView.findViewById(R.id.audioView);
        photoText.setTextColor(Color.parseColor("#000000"));
        photoView.setBackgroundResource(R.color.button_active);
        circleImage.setOnClickListener(captrureListener);
        turnImage.setOnClickListener(switchCameraListener);
        flashImage.setOnClickListener(flashCameraListener);

        photoText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photoText.setTextColor(Color.parseColor("#000000"));
                photoView.setBackgroundResource(R.color.button_active);

//                videoText.setTextColor(Color.parseColor("#000000"));
                videoView.setBackgroundResource(R.color.view_inactive);

//                photoText.setTextColor(Color.parseColor("#000000"));
                audioView.setBackgroundResource(R.color.view_inactive);

            }
        });

        videoText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoText.setTextColor(Color.parseColor("#000000"));
                videoView.setBackgroundResource(R.color.button_active);

//                videoText.setTextColor(Color.parseColor("#000000"));
                audioView.setBackgroundResource(R.color.view_inactive);

//                photoText.setTextColor(Color.parseColor("#000000"));
                photoView.setBackgroundResource(R.color.view_inactive);

            }
        });

        audioText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                audioText.setTextColor(Color.parseColor("#000000"));
                audioView.setBackgroundResource(R.color.button_active);

//                videoText.setTextColor(Color.parseColor("#000000"));
                videoView.setBackgroundResource(R.color.view_inactive);

//                photoText.setTextColor(Color.parseColor("#000000"));
                photoView.setBackgroundResource(R.color.view_inactive);

            }
        });

//        display_image = (ImageView)uploadView.findViewById(R.id.display_image);
        uploadDone = (TextView)uploadView.findViewById(R.id.uploadDone);
       /*  preview = new Preview(getActivity(), (SurfaceView)uploadView.findViewById(R.id.surfaceView));
        preview.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ((FrameLayout)uploadView.findViewById(R.id.upload_frame_layout)).addView(preview);
        preview.setKeepScreenOn(true);*/


       /* circleImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                checkPermissionsCamera();
                if( chooseTypeCamera.equalsIgnoreCase("Camera")){

                    final Intent intentCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intentCapture.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
                    startActivityForResult(intentCapture, REQUEST_CAMERA);
                }
                if(chooseTypeCamera.equalsIgnoreCase("Photo and Video Library")){
                    Intent intentLibrary = new Intent();
                    intentLibrary.setType("image*//*");
                    intentLibrary.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intentLibrary, ""), PICK_IMAGE_REQUEST);
                }

            }
        });*/

        uploadDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject param = new JSONObject();
                try {
                    param.putOpt("media_type", 1);
                    param.putOpt("type", 1);
                    param.putOpt("media", imageFile);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                uploadMedia(getActivity(),param);


            }
        });

       /* if (!getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Toast.makeText(getActivity(), "No camera on this device", Toast.LENGTH_LONG)
                    .show();
        } else {
            cameraId = findFrontFacingCamera();
            if (cameraId < 0) {
                Toast.makeText(getActivity(), "No front facing camera found.",
                        Toast.LENGTH_LONG).show();
            } else {
                mCamera = Camera.open(cameraId);
            }
        }*/
//        getActivity().findViewById(R.id.fab).setVisibility(View.GONE);
       /* cameraPreview = (LinearLayout)uploadView.findViewById(R.id.camera_preview);
        mPreview = new CameraPreview(getActivity(), mCamera);
        cameraPreview.addView(mPreview);*/
        return uploadView;
    }

    View.OnClickListener captrureListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCamera.takePicture(null, null, mPicture);
            if(isFlashOn){
                turnOffFlash();
            }
        }
    };

    public static void setCameraDisplayOrientation(Activity activity,
                                                   int cameraId, android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }


    public void checkPermissionsCamera(){

        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA);
        }else {
            /*if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
                //we should show some explanation for user here
                showExplanationDialog("Allow Camera","Android requires camera storage permissions");
            } else {*/
                //request permission
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISSION_CAMERA);
//            }
        }
    }

    private void showExplanationDialog(String title,String reqMsg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        AlertDialog d = builder.create();
        d.setCanceledOnTouchOutside(false);
        // d.setMessage(""+R.string.exit_msg);
        d.setIcon(R.mipmap.ic_launcher);
        d.setTitle(title);
        d.setMessage(reqMsg);
        d.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISSION_CAMERA);
                    }
                });
        d.show();
    }

    private void showDisableDialog(String title,String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        AlertDialog d = builder.create();
        // d.setMessage(""+R.string.exit_msg);
        d.setIcon(R.mipmap.ic_launcher);
        d.setCanceledOnTouchOutside(false);
        d.setTitle(title/*"No Access Permission"*/);
        d.setMessage(msg/*"External storage device permission not allowing. This app will NOT show your galley."*/);
        d.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISSION_CAMERA);

                    }
                });

        d.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        d.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (REQUEST_PERMISSION_CAMERA == requestCode) {
                //same request code as was in request permission
               /* final Intent intentCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intentCapture.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
                startActivityForResult(intentCapture, REQUEST_CAMERA);*/
            }

        } else {
            //not granted permission
            //show some explanation dialog1 that some features will not work
            showDisableDialog("No Access Permissions","Camera permission not allowing. This app will NOT open camera.");
        }
    }

    View.OnClickListener switchCameraListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //get the number of cameras
            int camerasNumber = Camera.getNumberOfCameras();
            if (camerasNumber > 1) {
                //release the old camera instance
                //switch camera, from the front and the back and vice versa

                releaseCamera();
                chooseCamera();
            } else {
                Toast toast = Toast.makeText(myContext, "Sorry, your phone has only one camera!", Toast.LENGTH_LONG);
                toast.show();
            }
        }
    };

    View.OnClickListener flashCameraListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isFlashOn) {
                // turn off flash
                turnOffFlash();

            } else {
                // turn on flash
                turnOnFlash();

            }
        }
    };

    // Turning On flash
    private void turnOnFlash() {
        if (!isFlashOn) {
            if (mCamera == null || params == null) {
                return;
            }
            // play sound
//            playSound();

            params = mCamera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            mCamera.setParameters(params);
//            mCamera.startPreview();
            isFlashOn = true;

            // changing button/switch image
//            toggleButtonImage();
            flashImage.setBackgroundResource(R.drawable.flash_on);
        }

    }


    // Turning Off flash
    private void turnOffFlash() {
        if (isFlashOn) {
            if (mCamera == null || params == null) {
                return;
            }
            // play sound
//            playSound();

            params = mCamera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            mCamera.setParameters(params);
//            mCamera.stopPreview();
            isFlashOn = false;

            // changing button/switch image
//            toggleButtonImage();
            flashImage.setBackgroundResource(R.drawable.flash_off);
        }
    }


    public void chooseCamera() {
        //if the camera preview is the front
        if (cameraFront) {
            int cameraId = findBackFacingCamera();
            if (cameraId >= 0) {
                //open the backFacingCamera
                //set a picture callback
                //refresh the preview

                mCamera = Camera.open(cameraId);
                params = mCamera.getParameters();
                flash_layout.setVisibility(View.VISIBLE);
//                turnOffFlash();
                setCameraDisplayOrientation(getActivity(),cameraId,mCamera);
                mPicture = getPictureCallback();
                mPreview.refreshCamera(mCamera);
            }
        } else {
            int cameraId = findFrontFacingCamera();
            if (cameraId >= 0) {
                //open the backFacingCamera
                //set a picture callback
                //refresh the preview
                flash_layout.setVisibility(View.GONE);
                mCamera = Camera.open(cameraId);
                setCameraDisplayOrientation(getActivity(),cameraId,mCamera);
                mPicture = getPictureCallback();
                mPreview.refreshCamera(mCamera);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!hasCamera(myContext)) {
            Toast toast = Toast.makeText(myContext, "Sorry, your phone does not have a camera!", Toast.LENGTH_LONG);
            toast.show();
            getActivity().onBackPressed();
        }

        if (mCamera == null) {
            //if the front facing camera does not exist
            if (findFrontFacingCamera() < 0) {
                Toast.makeText(getActivity(), "No front facing camera found.", Toast.LENGTH_LONG).show();
//                switchCamera.setVisibility(View.GONE);
            }
            try {
                mCamera = Camera.open(findBackFacingCamera());
                params = mCamera.getParameters();
                isFlashOn=true;
                turnOffFlash();
                mPicture = getPictureCallback();
                mPreview.refreshCamera(mCamera);
            }catch (Exception e){
//                checkPermissionsCamera();
            }
        }

        /*int numCams = Camera.getNumberOfCameras();
        if(numCams > 0){
            try{
                camera = Camera.open(0);
                camera.startPreview();
                preview.setCamera(camera);
            } catch (RuntimeException ex){
                Toast.makeText(getActivity(),"getting error", Toast.LENGTH_LONG).show();
            }
        }*/
    }


    public boolean isCameraUsebyApp() {
        Camera camera = null;
        try {
            camera = Camera.open();
        } catch (RuntimeException e) {
            return true;
        } finally {
            if (camera != null) camera.release();
        }
        return false;
    }

    private int findFrontFacingCamera() {
        int cameraId = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                cameraId = i;
                cameraFront = true;
                break;
            }
        }
        return cameraId;
    }

    private int findBackFacingCamera() {
        int cameraId = -1;
        //Search for the back facing camera
        //get the number of cameras
        int numberOfCameras = Camera.getNumberOfCameras();
        //for every camera check
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                cameraId = i;
                cameraFront = false;
                break;
            }
        }
        return cameraId;
    }

    private Camera.PictureCallback getPictureCallback() {
        Camera.PictureCallback picture = new Camera.PictureCallback() {

            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                //make a new picture file
                File pictureFile = getOutputMediaFile();

                if (pictureFile == null) {
                    return;
                }
                try {
                    //write the file
                    FileOutputStream fos = new FileOutputStream(pictureFile);
                    fos.write(data);
                    fos.close();
                    Toast toast = Toast.makeText(myContext, "Picture saved: " + pictureFile.getName(), Toast.LENGTH_LONG);
                    toast.show();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //refresh camera to continue preview
                mPreview.refreshCamera(mCamera);
            }
        };
        return picture;
    }



    //make picture and save to a folder
    private static File getOutputMediaFile() {
        //make a new file directory inside the "sdcard" folder
        int count=1;
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/", "Masharef Images"+new SimpleDateFormat("dd").format(new Date()));

        //if this "JCGCamera folder does not exist
        if (!mediaStorageDir.exists()) {
            //if you cannot make this folder return
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        //take the current timeStamp
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        //and make a media file:
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");

        return mediaFile;
    }



    public Uri setImageUri() {
        // Store image in dcim
        imageFile = new File(Environment.getExternalStorageDirectory() + "/DCIM/", "image" + new Date().getTime() + ".png");
        Uri imgUri = Uri.fromFile(imageFile);
        this.imageUri=imgUri;
        this.imgPath = imageFile.getAbsolutePath();
        return imgUri;
    }

    public void uploadMedia(final Context context, final JSONObject param){

        final ProgressDialog progress = Utils.getProgress(context, context.getString(R.string.processing_number));

        new Thread(new Runnable() {

            public void run() {

                UserServices userServ = new UserServices();
                boolean callStatus 	= userServ.uploadMedia(context, param, new UserServices.UserServiceListeners() {

                    public void onSuccess(JsonObj model) {

                        //AUserDetail user = (AUserDetail)model;
                       final ABasicMessage msg =  (ABasicMessage)model;

                        if(msg == null)
                            Log.e(""+msg.getMessage());
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progress.dismiss();
                                Utils.alert(context, R.drawable.del_success_alert,R.string.gallery_upload_success, msg.getMessage());
                            }
                        });
                        progress.dismiss();

                    }

                    public void onForceLogout(Exception error) {
                        Log.e(error.getMessage()+" At doRequestMobileVerifyCodeProcess() of MobileVerificationFragment Class");
                        progress.dismiss();
                    }

                    public void onFailure(Exception error) {
                        Log.e(error.getMessage()+" At doRequestMobileVerifyCodeProcess() of MobileVerificationFragment Class");
                        Utils.showAlertOnMainThread(context, error.getMessage());
                        progress.dismiss();
                    }

                });

                if(!callStatus)
                    progress.dismiss();

            }

        }).start();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_CANCELED) {
            if (requestCode == PICK_IMAGE_REQUEST) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
//                    imageFile=new File(String.valueOf(data.getData()));
//                    display_image.setImageBitmap(bitmap);
                    // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                    Uri tempUri = getImageUri(getActivity(), bitmap);

                    // CALL THIS METHOD TO GET THE ACTUAL PATH
                     imageFile = new File(getRealPathFromURI(tempUri));

                    System.out.println(tempUri);
                    System.out.println(imageFile.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == REQUEST_CAMERA) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
//                    display_image.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }

    }


    public Uri getImageUri(final Context inContext,final Bitmap inImage) {

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        //inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);

        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public Bitmap decodeFile(String path) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, o);
            // The new size we want to scale to
            final int REQUIRED_SIZE = 70;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeFile(path, o2);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;

    }




    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    private void releaseCameraAndPreview() {
        mPreview.setCamera(null);
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    private static void requestWritePermission(final Context context){
        if(ActivityCompat.shouldShowRequestPermissionRationale((Activity)context, Manifest.permission.CAMERA)) {
            new AlertDialog.Builder(context)
                    .setMessage("This app needs permission to write data to the phone so that the Screenshot can be saved")
                    .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA},REQUEST_CAMERA);
                        }
                    }).show();

        }else {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
        }
    }


   /* @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (REQUEST_CAMERA == requestCode) {
                //same request code as was in request permission
                if (!getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                    Toast.makeText(getActivity(), "No camera on this device", Toast.LENGTH_LONG)
                            .show();
                } else {
//                    cameraId = findFrontFacingCamera();
                    if (cameraId < 0) {
                        Toast.makeText(getActivity(), "No front facing camera found.",
                                Toast.LENGTH_LONG).show();
                    } else {
                        mCamera = Camera.open(cameraId);
                    }
                }
            }

        } else {
            //not granted permission
            //show some explanation dialog1 that some features will not work
            requestWritePermission(getActivity());
        }
    }*/

    @Override
    public void onPause() {
        releaseCamera();
        /*if(camera != null) {
            camera.stopPreview();
            preview.setCamera(null);
            camera.release();
            camera = null;
        }*/
        super.onPause();
    }

    private void releaseCamera() {
        // stop and release camera
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }



    private boolean hasCamera(Context context) {
        //check if the device has camera
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnFragmentInteractionListener) {
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


//    public  abstract void onImageUpload();
}
