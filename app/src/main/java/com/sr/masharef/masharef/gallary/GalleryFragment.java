package com.sr.masharef.masharef.gallary;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sr.masharef.masharef.MAppManager;
import com.sr.masharef.masharef.R;
import com.sr.masharef.masharef.common.DialogToolbarInterface;
import com.sr.masharef.masharef.model.gallery.AGallery;
import com.sr.masharef.masharef.utility.Log;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GalleryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GalleryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GalleryFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int REQUEST_PERMISSION_READ_EXTERNAL_STORAGE = 1;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ArrayList<AGallery> galleriesList;

    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    ImageView uploadButton,galleryuser,galleryPublic;
    public static int int_items = 3 ;
    public static int PICK_REQUEST = 1;
    String typeCategory,privatOrNot;
    Toolbar toolbar;
    boolean isVisible;
    int postion, lastPosition;
    PagerAdapter adapter;
    boolean value;
    static GalleryFragment GalleryFragment;

    private LinearLayout container;

//    private MyAdapter mViewAdapter;
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO,Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 203;

    private OnFragmentInteractionListener mListener;
    Uri file;

    public GalleryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GalleryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GalleryFragment newInstance(String param1, String param2) {
        GalleryFragment fragment = new GalleryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        if(checkPermission()){

        }else{
            ActivityCompat.requestPermissions(getActivity(), permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View GalleryContainerView=inflater.inflate(R.layout.fragment_gallery, container, false);
        tabLayout = (TabLayout)GalleryContainerView.findViewById(R.id.tabs);
        container = (LinearLayout) GalleryContainerView.findViewById(R.id.fragment_container);
//        viewPager = (ViewPager)GalleryContainerView.findViewById(R.id.viewpager);
        uploadButton=(ImageView)GalleryContainerView.findViewById(R.id.buttonUpload_inact);
        galleryuser=(ImageView)GalleryContainerView.findViewById(R.id.g_public_inact);
        galleryPublic=(ImageView) GalleryContainerView.findViewById(R.id.g_home_inact);
        privatOrNot="0";
        final ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(false);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("Gallery");
        lastPosition = postion;
        System.out.println("lastPosition "+lastPosition);
        tabLayout.addTab(tabLayout.newTab().setText("IMAGES"));
        tabLayout.addTab(tabLayout.newTab().setText("VIDEOS"));
        tabLayout.addTab(tabLayout.newTab().setText("AUDIOS"));
        if(lastPosition==0){
            TabLayout.Tab tab = tabLayout.getTabAt(0);
            tab.select();
            if(value){
                privatOrNot = "1";
            }
            replaceFragment(new ImageFragment());
        }
        if(lastPosition==1){
            TabLayout.Tab tab = tabLayout.getTabAt(1);
            tab.select();
            if(value){
                privatOrNot = "1";
            }
            replaceFragment(new VideoFragment());
        }
        else if(lastPosition==2){
            TabLayout.Tab tab = tabLayout.getTabAt(2);
            tab.select();
            if(value){
                privatOrNot = "1";
            }
            replaceFragment(new AudioFragment());
        }

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    postion=tab.getPosition();
                    replaceFragment(new ImageFragment());
                } else if (tab.getPosition() == 1) {
                    postion=tab.getPosition();
                    replaceFragment(new VideoFragment());
                } else {
                    postion= tab.getPosition();
                    replaceFragment(new AudioFragment());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        if(value){
            galleryPublic.setBackgroundResource(R.drawable.gallery_home);
            uploadButton.setBackgroundResource(R.drawable.gallery_add);
            galleryuser.setBackgroundResource(R.drawable.gallery_user_selected);
        }else{
            galleryPublic.setBackgroundResource(R.drawable.gallery_home_selected);
            uploadButton.setBackgroundResource(R.drawable.gallery_add);
            galleryuser.setBackgroundResource(R.drawable.gallery_user);
        }


        galleryPublic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                value = false;
                privatOrNot="0";
                galleryPublic.setBackgroundResource(R.drawable.gallery_home_selected);
                uploadButton.setBackgroundResource(R.drawable.gallery_add);
                galleryuser.setBackgroundResource(R.drawable.gallery_user);
                TabLayout.Tab tab = tabLayout.getTabAt(postion);
                tab.select();
                if(postion==0){
                    replaceFragment(new ImageFragment());
                }else if(postion==1){
                    replaceFragment(new VideoFragment());
                }else if(postion==2){
                    replaceFragment(new AudioFragment());
                }

            }
        });
        galleryuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                value = true;
                privatOrNot="1";
                galleryPublic.setBackgroundResource(R.drawable.gallery_home);
                uploadButton.setBackgroundResource(R.drawable.gallery_add);
                galleryuser.setBackgroundResource(R.drawable.gallery_user_selected);
                TabLayout.Tab tab = tabLayout.getTabAt(postion);
                tab.select();
                if(postion==0){
                    replaceFragment(new ImageFragment());
                }else if(postion==1){
                    replaceFragment(new VideoFragment());
                }else if(postion==2){
                    replaceFragment(new AudioFragment());
                }
            }
        });
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                galleryPublic.setBackgroundResource(R.drawable.gallery_home);
                uploadButton.setBackgroundResource(R.drawable.gallery_add_selected);
                galleryuser.setBackgroundResource(R.drawable.gallery_user);
                popUpChooseUploadGallery();

            }
        });

        toolbar = (Toolbar)((AppCompatActivity)getActivity()).findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionBar.setDisplayHomeAsUpEnabled(false);
                actionBar.setDisplayShowCustomEnabled(false);
                actionBar.setDisplayShowTitleEnabled(true);
                if(getActivity() != null) {
                    System.out.println("postion"+lastPosition);
                    GalleryLibraryFlie.cancelValue=false;
                        getActivity().onBackPressed();
                }


            }
        });
        if(GalleryLibraryFlie.cancelValue){
            toolbar.setNavigationIcon(R.mipmap.ic_back);
            dialogShowType();
        }
        actionBar.setDisplayHomeAsUpEnabled(true);
        return GalleryContainerView;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        Bundle infoBundel = new Bundle();
        infoBundel.putString("privateId",privatOrNot);
       // infoBundel.putInt("postion",postion);
        fragment.setArguments(infoBundel);
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
    

    private JSONObject getParams(String privatOrNot,String type){
        JSONObject params=new JSONObject();
        try {
            params.putOpt("is_private",privatOrNot);
            params.putOpt("media_type", type);
            params.putOpt("time_stamp", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            params.putOpt("index", "0");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return params;
    }


    public class PagerAdapter extends FragmentStatePagerAdapter {
        int mNumOfTabs;

        public PagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }

        @Override
        public Fragment getItem(int position) {
//            position=viewPager.getCurrentItem();
            switch (position) {
                case 0:
                    ImageFragment tab1 = new ImageFragment();
                    Bundle bundle1=new Bundle();
                    bundle1.putString("privateId", privatOrNot);
                    tab1.setArguments(bundle1);
                    return tab1;
                case 1:
                    VideoFragment tab2 = new VideoFragment();
                    Bundle bundle2=new Bundle();
                    bundle2.putString("privateId", privatOrNot);
                    tab2.setArguments(bundle2);
                    return tab2;
                case 2:
                    AudioFragment tab3 = new AudioFragment();
                    Bundle bundle3=new Bundle();
                    bundle3.putString("privateId", privatOrNot);
                    tab3.setArguments(bundle3);
                    return tab3;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }




    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.RECORD_AUDIO);
        int result1 = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA);
        int result2 = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE);
        int result3 = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED&&
                result2 == PackageManager.PERMISSION_GRANTED&&
                result3 == PackageManager.PERMISSION_GRANTED;
    }

    private void popUpChooseUploadGallery() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.gallery_choose_dialog);
        dialog.setCancelable(false);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        final ListView choosePart = (ListView) dialog.findViewById(R.id.gallery_listview);
        Button closeDialog = (Button)dialog.findViewById(R.id.gallery_cancel);
        final String[] chooseArrayList={"Camera","Photo & Video Library"};
        int [] Images={R.drawable.camera_type, R.drawable.photo_video_lib};

        choosePart.setAdapter(new DialogChooseListViewAdapter(getActivity(),chooseArrayList,Images));
        choosePart.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String sClickedItem =(String) (chooseArrayList[i]);
                typeCategory=sClickedItem;
                if(sClickedItem.equalsIgnoreCase("Camera")){
                    dialogShowType();
                }
                if(sClickedItem.equalsIgnoreCase("Photo & Video Library")){
                    checkPermissionsStorage();
                }
                dialog.dismiss();
            }
        });
        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                galleryPublic.setBackgroundResource(R.drawable.gallery_home_selected);
                uploadButton.setBackgroundResource(R.drawable.gallery_add);
                galleryuser.setBackgroundResource(R.drawable.gallery_user);
                dialog.dismiss();
            }
        });

        dialog.show();
    }



    private void dialogShowType(){
        final ArrayList<String> libraryType=new ArrayList<>();
        libraryType.add("Photos");
        libraryType.add("Videos");
        libraryType.add("Audios");
         int [] categoryImages={R.drawable.photo, R.drawable.video, R.drawable.audio};

        final Dialog dialogType = new Dialog(getActivity());
        dialogType.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogType.setContentView(R.layout.gallery_choose_dialog);
        dialogType.setCancelable(false);
        dialogType.setCancelable(true);
        dialogType.setCanceledOnTouchOutside(true);
        final ListView chooseType = (ListView) dialogType.findViewById(R.id.gallery_listview);
        Button closeDialog = (Button)dialogType.findViewById(R.id.gallery_cancel);
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, libraryType);

        chooseType.setAdapter(itemsAdapter);
        chooseType.setAdapter(new DialogChooseListTypeViewAdapter(getActivity(),libraryType,categoryImages));
        chooseType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String sClickedItem =(String) (libraryType.get(i));
                if(typeCategory!=null && typeCategory.equalsIgnoreCase("Photo & Video Library")) {
                    if (sClickedItem.equalsIgnoreCase("Photos")) {
                        intentType("image");
                    }
                    if (sClickedItem.equalsIgnoreCase("Videos")) {
                        intentType("video");
                    }
                    if (sClickedItem.equalsIgnoreCase("Audios")) {
                        intentType("audio");
                    }
                }else{
                    if (sClickedItem.equalsIgnoreCase("Photos")) {
                        intentCameraType("image");
                    }
                    if (sClickedItem.equalsIgnoreCase("Videos")) {
                        intentCameraType("video");
                    }
                    if (sClickedItem.equalsIgnoreCase("Audios")) {
                        intentCameraType("audio");
                    }
                }
                dialogType.dismiss();
            }
        });
        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogType.dismiss();
            }
        });

        dialogType.show();

    }

    private void intentType(String intentType){
        Intent intentLibrary = new Intent();
        intentLibrary.setType(intentType+"/*");
        intentLibrary.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intentLibrary, ""), PICK_REQUEST);
    }
    private void intentCameraType(String intentType){
        if(intentType.equalsIgnoreCase("image")){
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
             file = Uri.fromFile(getOutputMediaPictureFile());
            intent.putExtra(MediaStore.EXTRA_OUTPUT, file);
            startActivityForResult(intent, PICK_REQUEST);
        }
        if(intentType.equalsIgnoreCase("video")){
            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            file = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, file);
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            startActivityForResult(intent, PICK_REQUEST);
        }
        if(intentType.equalsIgnoreCase("audio")){
            Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
            if (isAvailable(getActivity(), intent)) {
                file = Uri.fromFile(getOutputMediaAudioFile());
                intent.putExtra(MediaStore.EXTRA_OUTPUT, file);
                startActivityForResult(intent, PICK_REQUEST);
            }else{
                 Toast.makeText(getActivity(),"Recorder is not avilable in your device",Toast.LENGTH_LONG).show();
            }
        }
    }

    public static boolean isAvailable(Context ctx, Intent intent) {
        final PackageManager mgr = ctx.getPackageManager();
        List<ResolveInfo> list =
                mgr.queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    private static File getOutputMediaPictureFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "/Masharef/MasharefPictures");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
    }

    private static File getOutputMediaAudioFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_MUSIC), "/Masharef/MasharefAudio");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".mp3");
    }

    private Uri getOutputMediaFileUri(int type){

        return Uri.fromFile(getOutputMediaVideoFile(type));
    }

    /** Create a File for saving an image or video */
    private File getOutputMediaVideoFile(int type){

        // Check that the SDCard is mounted
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "/Masharef/MasharefVideo");
        if (! mediaStorageDir.exists()){

            if (! mediaStorageDir.mkdirs()){
                Toast.makeText(getActivity(), "Failed to create directory MyCameraVideo.",
                        Toast.LENGTH_LONG).show();

                Log.d("MyCameraVideo", "Failed to create directory MyCameraVideo.");
                return null;
            }
        }
        java.util.Date date= new java.util.Date();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(date.getTime());

        File mediaFile;

        if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");

        } else {
            return null;
        }

        return mediaFile;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Fragment uploadLibraryFrag;
        FragmentManager uploadFragMan = getFragmentManager();
        FragmentTransaction ft = uploadFragMan.beginTransaction();
        if (resultCode == RESULT_OK) {
            String fileString;
            if (requestCode == PICK_REQUEST) {
                if (data == null) {
                    fileString = GetFilePathFromDevice.getPath(getActivity(), file);
                } else {
                    fileString = GetFilePathFromDevice.getPath(getActivity(), data.getData());
                }
                if(fileString!=null && !fileString.isEmpty()){
                    Bundle args = new Bundle();
                    args.putString("filePath", fileString);
                    File libraryFile = new File(fileString);
                    long fileSizeInBytes = libraryFile.length();
                    long fileSizeInKB = fileSizeInBytes / 1024;
                    long fileSizeInMB = fileSizeInKB / 1024;
                    if (fileString.contains(".mp3")) {
                        if (fileSizeInMB < 2) {
                            uploadLibraryFrag = new GalleryLibraryFlie();
                            uploadLibraryFrag.setArguments(args);
                            ft.replace(R.id.content_frame, uploadLibraryFrag, "gallery").addToBackStack("del_gallery").commit();
                        } else {
                            errorDialog("The selected file size is not allowed for upload", "audio");
                        }
                    } else if (fileString.contains(".mp4")) {

                        if (fileSizeInMB > 25) {
                            errorDialog("The File size exceeding, so not allowed for upload", "video");
                        }
                        else {

                            uploadLibraryFrag = new GalleryLibraryFlie();
                            uploadLibraryFrag.setArguments(args);
                            ft.replace(R.id.content_frame, uploadLibraryFrag, "gallery").addToBackStack("del_gallery").commit();
                        }
                    } else {
                        uploadLibraryFrag = new GalleryLibraryFlie();
                        uploadLibraryFrag.setArguments(args);
                        ft.replace(R.id.content_frame, uploadLibraryFrag, "gallery").addToBackStack("del_gallery").commit();
                    }
                }else{
                    Toast.makeText(getActivity(),"File path is not found & Please try again",Toast.LENGTH_LONG).show();
                }

            }
        }
        if(resultCode == Activity.RESULT_CANCELED) {
            getFragmentManager().popBackStackImmediate();
            toolbar.setNavigationIcon(R.mipmap.ic_back);
            uploadLibraryFrag = new GalleryFragment();
            ft.replace(R.id.content_frame, uploadLibraryFrag, "gallery").addToBackStack("del_gallery").commit();
        }
        }

    private void errorDialog(String errorMsg, final String type){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        AlertDialog d = builder.create();
        d.setCanceledOnTouchOutside(false);
        // d.setMessage(""+R.string.exit_msg);
        d.setIcon(R.mipmap.ic_launcher);
        d.setMessage(errorMsg);
        d.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
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


    class DialogChooseListViewAdapter extends BaseAdapter {
        String[] chooseList;
        Context context;
        TextView textView;
        ImageView image;
        int [] imageId;

        DialogChooseListViewAdapter(Context context, String[] chooseList, int[] images){
            this.chooseList=chooseList;
            this.context=context;
            imageId=images;
        }

        @Override
        public int getCount() {
            return chooseList.length;
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.gallery_row, viewGroup, false);
            textView = (TextView) rowView.findViewById(R.id.gallery_name);
            image = (ImageView) rowView.findViewById(R.id.gallery_icon);
            image.setImageResource(imageId[i]);
            textView.setText(chooseList[i]);
            return rowView;
        }
    }

    class DialogChooseListTypeViewAdapter extends BaseAdapter {
        ArrayList<String> chooseListType;
        Context context;
        TextView textViewType;
        ImageView imageType;
        int [] imageIdType;

        DialogChooseListTypeViewAdapter(Context context, ArrayList<String> chooseList, int[] images){
            this.chooseListType=chooseList;
            this.context=context;
            imageIdType=images;
        }

        @Override
        public int getCount() {
            return chooseListType.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.gallery_row, viewGroup, false);
            textViewType = (TextView) rowView.findViewById(R.id.gallery_name);
            imageType = (ImageView) rowView.findViewById(R.id.gallery_icon);
            imageType.setImageResource(imageIdType[i]);
            textViewType.setText(chooseListType.get(i));
            return rowView;
        }
    }

   /* class MyAdapter extends FragmentStatePagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            //Fragment fragment = null;
            Log.e("getItem position "+position);
            Log.e("viewPager.getCurrentItem() "+viewPager.getCurrentItem());

            Bundle bundle = new Bundle();
            if(viewPager.getCurrentItem()==0){
                //viewPager.setCurrentItem(0);
                Toast.makeText(getActivity(),"pos "+viewPager.getCurrentItem(),Toast.LENGTH_SHORT).show();
                Fragment fragment = new ImageFragment();
                bundle.putString("privateId", privatOrNot);
                fragment.setArguments(bundle);
                return fragment;
            }else if(viewPager.getCurrentItem()==1){
                //viewPager.setCurrentItem(1);
//                initializeAdapter();
                Toast.makeText(getActivity(),"pos "+viewPager.getCurrentItem(),Toast.LENGTH_SHORT).show();
                Fragment fragment = new VideoFragment();
                bundle.putString("privateId", privatOrNot);
                fragment.setArguments(bundle);
                return fragment;
            }else if(viewPager.getCurrentItem()==2){
                //viewPager.setCurrentItem(2);
                Fragment fragment = new AudioFragment();
                bundle.putString("privateId", privatOrNot);
                Toast.makeText(getActivity(),"pos "+viewPager.getCurrentItem(),Toast.LENGTH_SHORT).show();
                fragment.setArguments(bundle);
                return fragment;
            }

        return null;
        }

        @Override
        public int getCount() {
            return int_items;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0 :
                    return "IMAGES";
                case 1 :
                    return "VEDIOS";
                case 2:
                    return "AUDIOS";
            }
            return null;
        }
    }*/



    public void checkPermissionsStorage(){
        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        if (PackageManager.PERMISSION_GRANTED == permissionCheck) {
//            displayLibraryGalley();
            dialogShowType();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                //we should show some explanation for user here
                showExplanationDialog("Allow External Storage","Android requires external storage device permissions");
            } else {
                //request permission
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_READ_EXTERNAL_STORAGE);
            }
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
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_READ_EXTERNAL_STORAGE);
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
                        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_READ_EXTERNAL_STORAGE);

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
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                permissionToRecordAccepted  = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                permissionToRecordAccepted  = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                permissionToRecordAccepted  = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) getActivity().finish();
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (REQUEST_PERMISSION_READ_EXTERNAL_STORAGE == requestCode) {
                dialogShowType();
            }

        } else {
            showDisableDialog("No Access Permissions","External storage device permission not allowing. This app will NOT show your galley.");
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
    public interface YourFragmentInterface {
        void fragmentBecameVisible();
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
}
