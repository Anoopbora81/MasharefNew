package com.sr.masharef.masharef.gallary;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.Toast;

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
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AudioFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AudioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AudioFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView galleryAudioRecyclerView;
    RecyclerView.LayoutManager recyclerViewAudioLayoutManager;
    String private_id,media_type;
    ArrayList<AGallery> galleryAudioList=new ArrayList<>();
    GalleryAudioAdapter galleryAudioAdapter;
    ImageLoader loader;
    Toolbar toolbar;
    public DialogFragment newFragment;

    private OnFragmentInteractionListener mListener;

    public AudioFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VideoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AudioFragment newInstance(String param1, String param2) {
        AudioFragment fragment = new AudioFragment();
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

                this.private_id = getArguments().getString("privateId");
//                this.media_type = getArguments().getString("mediaType");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View videoView = inflater.inflate(R.layout.gallery_audio, container, false);
        galleryAudioRecyclerView = (RecyclerView) videoView.findViewById(R.id.recycler_view_videoGallary);
        recyclerViewAudioLayoutManager = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        galleryAudioRecyclerView.setLayoutManager(recyclerViewAudioLayoutManager);
        galleryAudioRecyclerView.setHasFixedSize(true);
        toolbar = (Toolbar)((AppCompatActivity)getActivity()).findViewById(R.id.toolbar);
//            getGallery(getParams(private_id,"3"));
        loader	= new ImageLoader(getActivity(), 120, 120);
        if (private_id.equalsIgnoreCase("0") && MAppManager.galleryPublicAudioList == null) {
            getGallery(getParams(private_id, "3")); //MAppManager.galleryPrivateImagesList = galleryTemplate.galleryArrayList;
        } else if (private_id.equalsIgnoreCase("1") && MAppManager.galleryPrivateAudioList == null) {
            getGallery(getParams(private_id, "3"));//MAppManager.galleryPublicImagesList = galleryTemplate.galleryArrayList;
        } else if (private_id.equalsIgnoreCase("0") && MAppManager.galleryPublicAudioList != null) {
            initializeAdapterOnMainGalleryAudioThread(MAppManager.galleryPublicAudioList);
        } else if (private_id.equalsIgnoreCase("1") && MAppManager.galleryPrivateAudioList != null) {
            initializeAdapterOnMainGalleryAudioThread(MAppManager.galleryPrivateAudioList);
        }
        return videoView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    void getGallery(final JSONObject param) {
        final ProgressDialog progress = Utils.getProgress(getActivity(), getActivity().getString(R.string.getting_gallery));

        new Thread(new Runnable() {

            public void run() {
                GalleryListServices userServ = new GalleryListServices(getActivity());

                boolean callStatus 	= userServ.getGalleryList(param,new GalleryListServices.GalleryListServiceListeners(){


                    @Override
                    public void onSuccess(AGalleryTemplate galleryTemplate) {
                        if (private_id.equalsIgnoreCase("1")) {
                            MAppManager.galleryPrivateAudioList = galleryTemplate.galleryArrayList;
                        } else {
                            MAppManager.galleryPublicAudioList = galleryTemplate.galleryArrayList;
                        }
                        galleryAudioList = galleryTemplate.galleryArrayList;
                            initializeAdapterOnMainGalleryAudioThread(galleryTemplate.galleryArrayList);

                        progress.dismiss();
                    }

                    @Override
                    public void onFailure(Exception error) {
                        Log.e(error.getMessage()+" At getaudiogalleriesList() of Gallery Class");
                        Utils.showAlertOnMainThread(getActivity(), error.getMessage());
                        progress.dismiss();
                    }

                    @Override
                    public void onForceLogout(Exception error) {
                        Log.e(error.getMessage()+" At getaudiogalleriesList() of Gallery Class");
                        progress.dismiss();
                    }

                });

                if(!callStatus)
                    progress.dismiss();

            }

        }).start();

    }

    private void initializeAdapterOnMainGalleryAudioThread(final ArrayList<AGallery> galleryAudioList) {
        if(getActivity()!=null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    galleryAudioAdapter = new GalleryAudioAdapter(galleryAudioList);
                    galleryAudioRecyclerView.setAdapter(galleryAudioAdapter);
                    galleryAudioAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    class GalleryAudioAdapter extends RecyclerView.Adapter<GalleryAudioAdapter.MyGalleryViewHolder> {
        int imageInt;
        Cursor adapterCursor;
        ArrayList<AGallery> videos = new ArrayList<>();

        @Override
        public MyGalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemViewGallery = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.gallery_grid_view, parent, false);

            return new MyGalleryViewHolder(itemViewGallery);
        }

        @Override
        public void onBindViewHolder(MyGalleryViewHolder holder, int position) {
            AGallery imageList = videos.get(position);
//            File imgFile = new File(imageList.media_url+"/"+imageList.gallery_image_icon);
            final String gg=  imageList.media_url+"/audio/"+imageList.gallery_image_icon;

            loader.loadImage(gg, R.drawable.defaultaudio, holder.gallery_audio);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Toast.makeText(getActivity(),"Media not available ",Toast.LENGTH_SHORT).show();
                    FragmentManager fragmentManager = getFragmentManager();
                    newFragment =GalleryDetailsFragment.newInstance(gg);
                    newFragment.show(fragmentManager, "TAG");

                   /* FragmentManager fragmentManager = getFragmentManager();
                    android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
                    Fragment fragment =new GalleryDetailsFragment();
                    Bundle infoBundel = new Bundle();
                    infoBundel.putString("path",gg);
                    fragment.setArguments(infoBundel);
                    transaction.replace(R.id.content_frame, fragment).addToBackStack("audio");
                    transaction.commit();*/
                }
            });

        }

        @Override
        public int getItemCount() {
            return videos.size();
        }

        public class MyGalleryViewHolder extends RecyclerView.ViewHolder {
            ImageView gallery_audio;

            public MyGalleryViewHolder(View itemView) {
                super(itemView);
                gallery_audio = (ImageView) itemView.findViewById(R.id.gallery_photo);
            }
        }

        public GalleryAudioAdapter(ArrayList<AGallery> videolist) {
            this.videos = videolist;
        }

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
        toolbar.setNavigationIcon(R.mipmap.ic_back);
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
