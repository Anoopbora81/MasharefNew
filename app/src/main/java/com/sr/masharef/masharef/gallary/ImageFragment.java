package com.sr.masharef.masharef.gallary;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sr.masharef.masharef.MAppManager;
import com.sr.masharef.masharef.R;
import com.sr.masharef.masharef.common.ImageLoader;
import com.sr.masharef.masharef.model.gallery.AGallery;
import com.sr.masharef.masharef.model.gallery.AGalleryTemplate;
import com.sr.masharef.masharef.utility.Log;
import com.sr.masharef.masharef.utility.Utils;
import com.sr.masharef.masharef.webservice.api.GalleryListServices;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by systemrapid on 13/01/17.
 */

public class ImageFragment extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback {

    RecyclerView galleryRecyclerView;
    GalleryAdapter galleryAdapter;
    private static final int REQUEST_PERMISSION = 1;
    Uri uri;
    Cursor newcursor;
    String[] projectionnew;
    String PathOfImage = null;
    ArrayList<String> listOfAllImages = new ArrayList<String>();
    Toolbar toolbar;
    //    public ArrayList<AGallery> MAppManager.galleryPrivateImagesList;
    String private_id, media_type;
    String current_private_id = "0";
    RecyclerView.LayoutManager recyclerViewLayoutManager;
    ImageLoader loader;
    public DialogFragment newFragment;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.private_id = getArguments().getString("privateId");
//            this.media_type = getArguments().getString("mediaType");
        }
     /*   if(savedInstanceState==null) {
            MAppManager.galleryPrivateImagesList = MDatabaseManager.getInstance().getGallelryList(private_id);
            if (MAppManager.galleryPrivateImagesList == null) {
                getImageGallery(getImageParams(private_id, "1"));
            }
            else
            {
                initializeAdapterOnMainGalleryVideoThread(MAppManager.galleryPrivateImagesList);
            }
        }*/
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View dashboard = inflater.inflate(R.layout.fragment_images, container, false);
        if (savedInstanceState == null) {
            try {
                galleryRecyclerView = (RecyclerView) dashboard.findViewById(R.id.recycler_view_gallary);
                recyclerViewLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
                galleryRecyclerView.setLayoutManager(recyclerViewLayoutManager);
                galleryRecyclerView.setHasFixedSize(true);
                loader = new ImageLoader(getActivity(), 120, 120);
                toolbar = (Toolbar)((AppCompatActivity)getActivity()).findViewById(R.id.toolbar);
                if (private_id.equalsIgnoreCase("0") && MAppManager.galleryPublicImagesList == null) {
                    getImageGallery(getImageParams(private_id, "1")); //MAppManager.galleryPrivateImagesList = galleryTemplate.galleryArrayList;
                } else if (private_id.equalsIgnoreCase("1") && MAppManager.galleryPrivateImagesList == null) {
                    getImageGallery(getImageParams(private_id, "1"));//MAppManager.galleryPublicImagesList = galleryTemplate.galleryArrayList;
                } else if (private_id.equalsIgnoreCase("0") && MAppManager.galleryPublicImagesList != null) {
                    initializeAdapterOnMainGalleryVideoThread(MAppManager.galleryPublicImagesList);
                } else if (private_id.equalsIgnoreCase("1") && MAppManager.galleryPrivateImagesList != null) {
                    initializeAdapterOnMainGalleryVideoThread(MAppManager.galleryPrivateImagesList);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return dashboard;
    }

    private JSONObject getImageParams(String privatOrNot, String type) {
        JSONObject params = new JSONObject();
        try {
            params.putOpt("is_private", privatOrNot);
            params.putOpt("media_type", type);
            params.putOpt("time_stamp", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            params.putOpt("index", "0");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return params;
    }

    void getImageGallery(final JSONObject param) {
        final ProgressDialog progress = Utils.getProgress(getActivity(), getActivity().getString(R.string.getting_gallery));

        new Thread(new Runnable() {

            public void run() {
                GalleryListServices userServ = new GalleryListServices(getActivity());

                boolean callStatus = userServ.getGalleryList(param, new GalleryListServices.GalleryListServiceListeners() {


                    @Override
                    public void onSuccess(AGalleryTemplate galleryTemplate) {
                        if (private_id.equalsIgnoreCase("1")) {
                            MAppManager.galleryPrivateImagesList = galleryTemplate.galleryArrayList;
                        } else {
                            MAppManager.galleryPublicImagesList = galleryTemplate.galleryArrayList;
                        }
//                        Log.e("galleriesList.size()galleriesList.size()categoryList.size() = " + MAppManager.galleryPrivateImagesList.size() + " XX");
                        //  MAppManager.galleryPrivateImagesList = MDatabaseManager.getInstance().getGallelryList(private_id);
                        initializeAdapterOnMainGalleryVideoThread(galleryTemplate.galleryArrayList);
                        progress.dismiss();
                    }

                    @Override
                    public void onFailure(Exception error) {
                        Log.e(error.getMessage() + " At getgalleriesList() of Gallery Class");
                        Utils.showAlertOnMainThread(getActivity(), error.getMessage());
                        progress.dismiss();
                    }

                    @Override
                    public void onForceLogout(Exception error) {
                        Log.e(error.getMessage() + " At getgalleriesList() of Gallery Class");
                        progress.dismiss();
                    }

                });

                if (!callStatus)
                    progress.dismiss();

            }

        }).start();

    }

    private void initializeAdapterOnMainGalleryVideoThread(final ArrayList<AGallery> galleryImagesList) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    galleryAdapter = new GalleryAdapter(galleryImagesList);
                    galleryRecyclerView.setAdapter(galleryAdapter);
                    galleryAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        /*int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION);
            getAllImages();
        }*/

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (REQUEST_PERMISSION == requestCode) {
                //same request code as was in request permission
//                startScanning();
                getAllImages();

               /* int column_index_data;
                uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

                projectionnew = new String[]{MediaStore.MediaColumns.DATA,
                        MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

                newcursor = getActivity().getContentResolver().query(uri, projectionnew, null,
                        null, null);

                column_index_data = newcursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                while (newcursor.moveToNext()) {
                    PathOfImage = newcursor.getString(column_index_data);

                    listOfAllImages.add(PathOfImage);
                }*/
            }

        } else {
            //not granted permission
            //show some explanation dialog1 that some features will not work
            requestWritePermission(getActivity());
        }

        /*switch (requestCode) {
            case REQUEST_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
            }
        }*/
    }

    ArrayList<String> getAllImages() {

        // pull request test

        int column_index_data;
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        projectionnew = new String[]{MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        newcursor = getActivity().getContentResolver().query(uri, projectionnew, null,
                null, null);

        column_index_data = newcursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        while (newcursor.moveToNext()) {
            PathOfImage = newcursor.getString(column_index_data);

            listOfAllImages.add(PathOfImage);
        }
        return listOfAllImages;
    }

    private static void requestWritePermission(final Context context) {
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(context)
                    .setMessage("This app needs permission to write data to the phone so that the Screenshot can be saved")
                    .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
                        }
                    }).show();

        } else {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
        }
    }

    class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.MyGalleryViewHolder> {
        ArrayList<AGallery> images = new ArrayList<>();

        public GalleryAdapter(ArrayList<AGallery> imagelist) {
            this.images = imagelist;
        }

        @Override
        public MyGalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemViewGallery = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.gallery_grid_view, parent, false);

            return new MyGalleryViewHolder(itemViewGallery);
        }

        @Override
        public void onBindViewHolder(MyGalleryViewHolder holder, final int position) {
            AGallery imageList = images.get(position);
            final String gg = imageList.media_url + "/" + imageList.gallery_image_icon;
            Calendar calendar = Calendar.getInstance();
            Date currentTime = calendar.getTime();
            String duration = Utils.calculateDuration(currentTime, currentTime);

            loader.loadImage(gg, R.drawable.defaultimage, holder.galleryImageView);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    FragmentManager fragmentManager = getFragmentManager();
                    android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
                    Fragment fragment = new ImageDisplayFragment(images, position);
                    transaction.replace(R.id.content_frame, fragment).addToBackStack("image");
                    transaction.commit();
                }
            });
            /*if (imgFile!=null) {

                Glide.with(getActivity())
                        .load(imgFile.getAbsolutePath())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into( holder.galleryImageView);



                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                holder.galleryImageView.setImageBitmap(decodeSampledBitmapFromFile(imgFile.getAbsolutePath(), 500, 250));

            }*/

        }

        @Override
        public int getItemCount() {
            return images.size();
        }

        public class MyGalleryViewHolder extends RecyclerView.ViewHolder {
            ImageView galleryImageView;
            TextView textViewTimeStamp;

            public MyGalleryViewHolder(View itemView) {
                super(itemView);
                galleryImageView = (ImageView) itemView.findViewById(R.id.gallery_photo);
                textViewTimeStamp = (TextView) itemView.findViewById(R.id.text_view_time_stamp);
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        toolbar.setNavigationIcon(R.mipmap.ic_back);
    }
}
