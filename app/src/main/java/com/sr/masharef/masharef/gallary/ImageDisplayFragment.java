package com.sr.masharef.masharef.gallary;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.sr.masharef.masharef.R;
import com.sr.masharef.masharef.common.ImageLoader;
import com.sr.masharef.masharef.model.gallery.AGallery;

import java.util.ArrayList;

import static android.view.View.VISIBLE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ImageDisplayFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ImageDisplayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImageDisplayFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ViewPager imageViewPager;
    PagerAdapter imageAdapter;

    ArrayList<AGallery> imagesList;
    ImageLoader loader;
    int mPostion=0;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ImageDisplayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ImageDisplayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ImageDisplayFragment newInstance(String param1, String param2) {
        ImageDisplayFragment fragment = new ImageDisplayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ImageDisplayFragment(ArrayList<AGallery> imagesList,int postion){
        this.imagesList = imagesList;
        this.mPostion = postion;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View DisplayImageView =  inflater.inflate(R.layout.image_display, container, false);
        intializeControls(DisplayImageView);
        return DisplayImageView;
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
    }

    private void intializeControls(View viewObject){
        imageViewPager = (ViewPager)viewObject.findViewById(R.id.image_pager);
//        loader = new ImageLoader(getActivity(), ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        imageAdapter = new ViewImagePagerAdapter(getActivity(),imagesList);
        imageViewPager.setAdapter(imageAdapter);
        imageViewPager.setCurrentItem(mPostion);

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

    public class ViewImagePagerAdapter extends PagerAdapter{
        LayoutInflater inflater;
        Context context;
        ArrayList<AGallery> imageViewList;

        public ViewImagePagerAdapter(Context context,ArrayList<AGallery> adapterImageList){
            this.imageViewList = adapterImageList;
            this.context = context;
        }

        @Override
        public int getCount() {
            return imageViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((RelativeLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView image;
            TextView imageText;

            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View itemView = inflater.inflate(R.layout.viewpager_item, container,
                    false);
            image = (ImageView)itemView.findViewById(R.id.item_image);
            imageText = (TextView)itemView.findViewById(R.id.image_text);
            final ProgressBar progressBar = (ProgressBar)itemView.findViewById(R.id.image_prograss);
            AGallery imageList = imageViewList.get(position);
            final String stringImage = imageList.media_url + "/" + imageList.gallery_image_icon;
            imageText.setText(imageList.gallery_image_icon);
            Glide.with(getActivity())
                    .load(stringImage)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into( image);

//            loader.loadImage(stringImage, R.drawable.defaultimage, image);
            container.addView(itemView);
            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // Remove viewpager_item.xml from ViewPager
            container.removeView((RelativeLayout) object);

        }
    }

}
