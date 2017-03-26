package com.sr.masharef.masharef.phonebook;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sr.masharef.masharef.R;
import com.sr.masharef.masharef.common.crypto.AJSONObject;
import com.sr.masharef.masharef.constants.Constants;
import com.sr.masharef.masharef.utility.Log;
import com.sr.masharef.masharef.utility.Utils;
import com.sr.masharef.masharef.webservice.api.RatingServices;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PhonePesonDetails.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PhonePesonDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhonePesonDetails extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView detailsName,detailsOccupation,detilsWorkplace,detailsMobile,rateUser;
    ImageView detailsImage;
    Button buttonRating;
    int rating =0;
    ImageView star1, star2, star3, star4, star5, star_detail1, star_detail2, star_detail3, star_detail4, star_detail5;
    String infoName,infoOcupation,infoWorkplace,infoMobile, contactId, phoneRating;
    int infoImage;
    EditText editTextReview;
    RatingBar rb;
    LinearLayout rating_barLayout_detail;

    private OnFragmentInteractionListener mListener;

    public PhonePesonDetails() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PhonePesonDetails.
     */
    // TODO: Rename and change types and number of parameters
    public static PhonePesonDetails newInstance(String param1, String param2) {
        PhonePesonDetails fragment = new PhonePesonDetails();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View personViewDetails=inflater.inflate(R.layout.phone_person_details, container, false);


        contactId = getArguments().getString("phoneContactId");
        infoName=getArguments().getString("phonename");
        infoOcupation=getArguments().getString("phoneoccupation");
        infoWorkplace=getArguments().getString("phoneworkplace");
        infoMobile=getArguments().getString("phonemobile");
        infoImage=getArguments().getInt("phonrimage");
        phoneRating = getArguments().getString("phoneRating");

        detailsName=(TextView)personViewDetails.findViewById(R.id.personName);
        detailsOccupation=(TextView)personViewDetails.findViewById(R.id.personOccupation);
        detilsWorkplace=(TextView)personViewDetails.findViewById(R.id.personWorkplace);
        detailsMobile=(TextView)personViewDetails.findViewById(R.id.personMobile);
        detailsImage=(ImageView)personViewDetails.findViewById(R.id.personImage);

       // buttonRating=(Button)personViewDetails.findViewById(R.id.buttonRating);
        rating_barLayout_detail=(LinearLayout)personViewDetails.findViewById(R.id.rating_barLayout_detail);
        star_detail1 = (ImageView) personViewDetails.findViewById(R.id.star_detail1);
        star_detail2 = (ImageView) personViewDetails.findViewById(R.id.star_detail2);
        star_detail3 = (ImageView) personViewDetails.findViewById(R.id.star_detail3);
        star_detail4 = (ImageView) personViewDetails.findViewById(R.id.star_detail4);
        star_detail5 = (ImageView) personViewDetails.findViewById(R.id.star_detail5);

        rateUser = (TextView)personViewDetails.findViewById(R.id.rateUser);

       /* star_detail20 = (ImageView) personViewDetails.findViewById(R.id.star_detail20);
        star_detail40 = (ImageView) personViewDetails.findViewById(R.id.star_detail40);
        star_detail50 = (ImageView) personViewDetails.findViewById(R.id.star_detail50);
        star_detail60 = (ImageView) personViewDetails.findViewById(R.id.star_detail60);
        star_detail80 = (ImageView) personViewDetails.findViewById(R.id.star_detail80);*/

        rateUser.setText(getString(R.string.rate)+" "+infoName);
        detilsWorkplace.setSelected(true);
        detailsName.setText(""+infoName);
        detailsOccupation.setText(""+infoOcupation);
        detilsWorkplace.setText(""+infoWorkplace);

        detailsImage.setBackgroundResource(infoImage);

         detailsMobile.setText(""+infoMobile);
         if( Utils.getSelectedLanguage(getActivity()).equalsIgnoreCase(Constants.LAN_ARABIC)) {
            String infoMobile_ar = infoMobile.substring(1)+"+";
            detailsMobile.setText(""+infoMobile_ar);
        }

       /* final ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        final Toolbar toolbar = (Toolbar)((AppCompatActivity)getActivity()).findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionBar.setDisplayHomeAsUpEnabled(false);
//                    actionBar.setDisplayShowCustomEnabled(false);
                actionBar.setDisplayShowTitleEnabled(true);
                if(getActivity() != null)
                    getActivity().onBackPressed();

            }
        });*/
        final ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        final Toolbar toolbar = (Toolbar)((AppCompatActivity)getActivity()).findViewById(R.id.toolbar);
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

       /* buttonRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUpRatingDialog();
            }
        });*/

        rating_barLayout_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUpRatingDialog();
            }
        });
        Float phoneRatingF = Float.parseFloat(phoneRating);
        updateRatingBar(phoneRatingF);

      /*  rb=(RatingBar)personViewDetails.findViewById(R.id.myRatingBar);
        rb.setRating(3.5f);
        rb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener(){

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                // TODO Auto-generated method stub

                Toast.makeText(getActivity(),Float.toString(rating),Toast.LENGTH_LONG).show();
            }
        });*/



        return personViewDetails;
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
    public void onResume() {
        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {

                    getFragmentManager().popBackStack();
                    return true;
                }
                return false;
            }
        });
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


    private void popUpRatingDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.ratingbar_dialog_layout);
        dialog.setCancelable(false);

        star1 = (ImageView) dialog.findViewById(R.id.star_image1);
        star2 = (ImageView) dialog.findViewById(R.id.star_image2);
        star3 = (ImageView) dialog.findViewById(R.id.star_image3);
        star4 = (ImageView) dialog.findViewById(R.id.star_image4);
        star5 = (ImageView) dialog.findViewById(R.id.star_image5);

        ((TextView)dialog.findViewById(R.id.textViewRatingName)).setText(getString(R.string.rate_to)+" "+infoName);
        editTextReview  = (EditText) dialog.findViewById(R.id.editTextReview);
        //editTextReview.setText("");

        star1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rating == 1)
                {
                    rating = 0;
                    star1.setBackgroundResource(R.drawable.empty_star2);
                }
                else
                {
                    rating =1;
                    star1.setBackgroundResource(R.drawable.filled_star2);
                }

                star2.setBackgroundResource(R.drawable.empty_star2);
                star3.setBackgroundResource(R.drawable.empty_star2);
                star4.setBackgroundResource(R.drawable.empty_star2);
                star5.setBackgroundResource(R.drawable.empty_star2);
            }
        });
        star2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rating =2;
                star1.setBackgroundResource(R.drawable.filled_star2);
                star2.setBackgroundResource(R.drawable.filled_star2);
                star3.setBackgroundResource(R.drawable.empty_star2);
                star4.setBackgroundResource(R.drawable.empty_star2);
                star5.setBackgroundResource(R.drawable.empty_star2);
            }
        });
        star3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rating =3;
                star1.setBackgroundResource(R.drawable.filled_star2);
                star2.setBackgroundResource(R.drawable.filled_star2);
                star3.setBackgroundResource(R.drawable.filled_star2);
                star4.setBackgroundResource(R.drawable.empty_star2);
                star5.setBackgroundResource(R.drawable.empty_star2);
            }
        });
        star4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rating =4;
                star1.setBackgroundResource(R.drawable.filled_star2);
                star2.setBackgroundResource(R.drawable.filled_star2);
                star3.setBackgroundResource(R.drawable.filled_star2);
                star4.setBackgroundResource(R.drawable.filled_star2);
                star5.setBackgroundResource(R.drawable.empty_star2);
            }
        });
        star5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rating =5;
                star1.setBackgroundResource(R.drawable.filled_star2);
                star2.setBackgroundResource(R.drawable.filled_star2);
                star3.setBackgroundResource(R.drawable.filled_star2);
                star4.setBackgroundResource(R.drawable.filled_star2);
                star5.setBackgroundResource(R.drawable.filled_star2);
            }
        });


        // set the custom dialog components - text, image and button

//                final EditText edittext = (EditText) dialog
//                        .findViewById(R.id.editText1);
        Button buttonDone = (Button) dialog.findViewById(R.id.buttonDone);
        Button buttonCancelF = (Button) dialog.findViewById(R.id.buttonCancelF);


        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String  textReview = editTextReview.getText().toString();
                sendRatingInfo(contactId, rating, textReview);
                dialog.dismiss();

            }
        });

        buttonCancelF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog.dismiss();

            }
        });

        dialog.show();

    }

    private void sendRatingInfo(String contactId, int rating, String textReview) {

        JSONObject ratingDetail =  new JSONObject();
        try {
            ratingDetail.put("contact_id", contactId);
            ratingDetail.put("rating", rating);
            ratingDetail.put("review", textReview);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(e.getMessage()+" at verifyCodeForMobileNumber Module!!");
        }

        sendRatingtoServer(ratingDetail);
    }

    private void sendRatingtoServer(final JSONObject ratingDetails) {
        final ProgressDialog progress = Utils.getProgress(getActivity(), getActivity().getString(R.string.ratingstatus));

        new Thread(new Runnable() {

            public void run() {

                RatingServices ratingServices = new RatingServices(getActivity());

                boolean callStatus 	= ratingServices.submitContactRating(ratingDetails, new RatingServices.RatingServicesListeners(){
                    @Override
                    public void onSuccess(JSONObject jsonJoiningEvents) {
                        final float ratingfromServer = Float.parseFloat(AJSONObject.optString(jsonJoiningEvents, "current").toString());

                        ((Activity)getActivity()).runOnUiThread(new Runnable() {
                            public void run() {
                                // TODO Auto-generated method stub
                                //    rb.setRating(ratingfromServer);

                                Toast.makeText(getActivity(), ""+infoName+" "+getString(R.string.rating_submit), Toast.LENGTH_SHORT).show();
                                updateRatingBar(ratingfromServer);

//                                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                                PhoneBook phoneBook = new PhoneBook();
//                                ft.replace(R.id.content_frame, phoneBook, "phonetab");
//                                ft.commit();

                                getFragmentManager().popBackStack();
                                getFragmentManager().popBackStack();
                                progress.dismiss();
                            }
                        });
                    }

                    @Override
                    public void onFailure(Exception error) {

                    }

                    @Override
                    public void onForceLogout(Exception error) {

                    }

                });
                if(!callStatus)
                    progress.dismiss();

            }

        }).start();

    }

    private void updateRatingBar(float ratingFromServer) {

        if(ratingFromServer >0 && ratingFromServer <0.3)
        {
            star_detail1.setBackgroundResource(R.drawable.star_1_filled);

           if( Utils.getSelectedLanguage(getActivity()).equalsIgnoreCase(Constants.LAN_ARABIC)) {
                 star_detail1.setImageBitmap(Utils.flipImage(BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.star_1_filled),2));
           }
            star_detail2.setBackgroundResource(R.drawable.empty_star2);
            star_detail3.setBackgroundResource(R.drawable.empty_star2);
            star_detail4.setBackgroundResource(R.drawable.empty_star2);
            star_detail5.setBackgroundResource(R.drawable.empty_star2);

        }
        else if(ratingFromServer >=0.3 && ratingFromServer <0.45)
        {

            star_detail1.setBackgroundResource(R.drawable.star_2_filled);
            if( Utils.getSelectedLanguage(getActivity()).equalsIgnoreCase(Constants.LAN_ARABIC)) {
                star_detail1.setImageBitmap(Utils.flipImage(BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.star_2_filled),2));
            }
//            star_detail2.setBackgroundResource(R.drawable.empty_star2);
//            star_detail3.setBackgroundResource(R.drawable.empty_star2);
//            star_detail4.setBackgroundResource(R.drawable.empty_star2);
//            star_detail5.setBackgroundResource(R.drawable.empty_star2);
        }
        else if(ratingFromServer >=0.45 && ratingFromServer <0.55)
        {
            star_detail1.setBackgroundResource(R.drawable.star_half_filled);
            if( Utils.getSelectedLanguage(getActivity()).equalsIgnoreCase(Constants.LAN_ARABIC)) {
                star_detail1.setImageBitmap(Utils.flipImage(BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.star_half_filled),2));
            }
//            star_detail2.setBackgroundResource(R.drawable.empty_star2);
//            star_detail3.setBackgroundResource(R.drawable.empty_star2);
//            star_detail4.setBackgroundResource(R.drawable.empty_star2);
//            star_detail5.setBackgroundResource(R.drawable.empty_star2);
        }
        else if(ratingFromServer >=0.55 && ratingFromServer <0.7)
        {
            star_detail1.setBackgroundResource(R.drawable.star_3_filled);
            if( Utils.getSelectedLanguage(getActivity()).equalsIgnoreCase(Constants.LAN_ARABIC)) {
                star_detail1.setImageBitmap(Utils.flipImage(BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.star_3_filled),2));
            }
//            star_detail2.setBackgroundResource(R.drawable.empty_star2);
//            star_detail3.setBackgroundResource(R.drawable.empty_star2);
//            star_detail4.setBackgroundResource(R.drawable.empty_star2);
//            star_detail5.setBackgroundResource(R.drawable.empty_star2);
        }
        else if(ratingFromServer >=0.7 && ratingFromServer <0.9)
        {
            star_detail1.setBackgroundResource(R.drawable.star_4_filled);
            if( Utils.getSelectedLanguage(getActivity()).equalsIgnoreCase(Constants.LAN_ARABIC)) {
                star_detail1.setImageBitmap(Utils.flipImage(BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.star_4_filled),2));
            }
//            star_detail2.setBackgroundResource(R.drawable.empty_star2);
//            star_detail3.setBackgroundResource(R.drawable.empty_star2);
//            star_detail4.setBackgroundResource(R.drawable.empty_star2);
//            star_detail5.setBackgroundResource(R.drawable.empty_star2);
        }
        else if(ratingFromServer >=0.9 && ratingFromServer <=1)
        {
            star_detail1.setBackgroundResource(R.drawable.filled_star2);
//            star_detail2.setBackgroundResource(R.drawable.empty_star2);
//            star_detail3.setBackgroundResource(R.drawable.empty_star2);
//            star_detail4.setBackgroundResource(R.drawable.empty_star2);
//            star_detail5.setBackgroundResource(R.drawable.empty_star2);
        }

        else if(ratingFromServer >=1 && ratingFromServer <1.3)
        {
            star_detail1.setBackgroundResource(R.drawable.filled_star2);
            star_detail2.setBackgroundResource(R.drawable.star_1_filled);
            if( Utils.getSelectedLanguage(getActivity()).equalsIgnoreCase(Constants.LAN_ARABIC)) {
                star_detail2.setImageBitmap(Utils.flipImage(BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.star_1_filled),2));
            }
//            star_detail3.setBackgroundResource(R.drawable.empty_star2);
//            star_detail4.setBackgroundResource(R.drawable.empty_star2);
//            star_detail5.setBackgroundResource(R.drawable.empty_star2);

        }
        else if(ratingFromServer >=1.3 && ratingFromServer <1.45)
        {
            star_detail1.setBackgroundResource(R.drawable.filled_star2);
            star_detail2.setBackgroundResource(R.drawable.star_2_filled);
            if( Utils.getSelectedLanguage(getActivity()).equalsIgnoreCase(Constants.LAN_ARABIC)) {
                star_detail2.setImageBitmap(Utils.flipImage(BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.star_2_filled),2));
            }
//            star_detail3.setBackgroundResource(R.drawable.empty_star2);
//            star_detail4.setBackgroundResource(R.drawable.empty_star2);
//            star_detail5.setBackgroundResource(R.drawable.empty_star2);
        }
        else if(ratingFromServer >=1.45 && ratingFromServer <1.55)
        {
            star_detail1.setBackgroundResource(R.drawable.filled_star2);
            star_detail2.setBackgroundResource(R.drawable.star_half_filled);
            if( Utils.getSelectedLanguage(getActivity()).equalsIgnoreCase(Constants.LAN_ARABIC)) {
                star_detail2.setImageBitmap(Utils.flipImage(BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.star_half_filled),2));
            }
//            star_detail3.setBackgroundResource(R.drawable.empty_star2);
//            star_detail4.setBackgroundResource(R.drawable.empty_star2);
//            star_detail5.setBackgroundResource(R.drawable.empty_star2);
        }
        else if(ratingFromServer >=1.55 && ratingFromServer <1.7)
        {
            star_detail1.setBackgroundResource(R.drawable.filled_star2);
            star_detail2.setBackgroundResource(R.drawable.star_3_filled);
            if( Utils.getSelectedLanguage(getActivity()).equalsIgnoreCase(Constants.LAN_ARABIC)) {
                star_detail2.setImageBitmap(Utils.flipImage(BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.star_3_filled),2));
            }
//            star_detail3.setBackgroundResource(R.drawable.empty_star2);
//            star_detail4.setBackgroundResource(R.drawable.empty_star2);
//            star_detail5.setBackgroundResource(R.drawable.empty_star2);
        }
        else if(ratingFromServer >=1.7 && ratingFromServer <1.9)
        {
            star_detail1.setBackgroundResource(R.drawable.filled_star2);
            star_detail2.setBackgroundResource(R.drawable.star_4_filled);
            if( Utils.getSelectedLanguage(getActivity()).equalsIgnoreCase(Constants.LAN_ARABIC)) {
                star_detail2.setImageBitmap(Utils.flipImage(BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.star_4_filled),2));
            }
//            star_detail3.setBackgroundResource(R.drawable.empty_star2);
//            star_detail4.setBackgroundResource(R.drawable.empty_star2);
//            star_detail5.setBackgroundResource(R.drawable.empty_star2);
        }
        else if(ratingFromServer >=1.9 && ratingFromServer <=2)
        {
            star_detail1.setBackgroundResource(R.drawable.filled_star2);
            star_detail2.setBackgroundResource(R.drawable.filled_star2);
//            star_detail3.setBackgroundResource(R.drawable.empty_star2);
//            star_detail4.setBackgroundResource(R.drawable.empty_star2);
//            star_detail5.setBackgroundResource(R.drawable.empty_star2);
        }

        else if(ratingFromServer >2 && ratingFromServer <2.3)
        {
            star_detail1.setBackgroundResource(R.drawable.filled_star2);
            star_detail2.setBackgroundResource(R.drawable.filled_star2);
            star_detail3.setBackgroundResource(R.drawable.star_1_filled);
            if( Utils.getSelectedLanguage(getActivity()).equalsIgnoreCase(Constants.LAN_ARABIC)) {
                star_detail3.setImageBitmap(Utils.flipImage(BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.star_1_filled),2));
            }
//            star_detail4.setBackgroundResource(R.drawable.empty_star2);
//            star_detail5.setBackgroundResource(R.drawable.empty_star2);

        }
        else if(ratingFromServer >=2.3 && ratingFromServer <2.45)
        {
            star_detail1.setBackgroundResource(R.drawable.filled_star2);
            star_detail2.setBackgroundResource(R.drawable.filled_star2);
            star_detail3.setBackgroundResource(R.drawable.star_2_filled);
            if( Utils.getSelectedLanguage(getActivity()).equalsIgnoreCase(Constants.LAN_ARABIC)) {
                star_detail3.setImageBitmap(Utils.flipImage(BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.star_2_filled),2));
            }
//            star_detail4.setBackgroundResource(R.drawable.empty_star2);
//            star_detail5.setBackgroundResource(R.drawable.empty_star2);
        }
        else if(ratingFromServer >=2.45 && ratingFromServer <2.55)
        {
            star_detail1.setBackgroundResource(R.drawable.filled_star2);
            star_detail2.setBackgroundResource(R.drawable.filled_star2);
            star_detail3.setBackgroundResource(R.drawable.star_half_filled);
            if( Utils.getSelectedLanguage(getActivity()).equalsIgnoreCase(Constants.LAN_ARABIC)) {
                star_detail3.setImageBitmap(Utils.flipImage(BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.star_half_filled),2));
            }
//            star_detail4.setBackgroundResource(R.drawable.empty_star2);
//            star_detail5.setBackgroundResource(R.drawable.empty_star2);
        }
        else if(ratingFromServer >=2.55 && ratingFromServer <2.7)
        {
            star_detail1.setBackgroundResource(R.drawable.filled_star2);
            star_detail2.setBackgroundResource(R.drawable.filled_star2);
            star_detail3.setBackgroundResource(R.drawable.star_3_filled);
            if( Utils.getSelectedLanguage(getActivity()).equalsIgnoreCase(Constants.LAN_ARABIC)) {
                star_detail3.setImageBitmap(Utils.flipImage(BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.star_3_filled),2));
            }
//            star_detail4.setBackgroundResource(R.drawable.empty_star2);
//            star_detail5.setBackgroundResource(R.drawable.empty_star2);
        }
        else if(ratingFromServer >=2.7 && ratingFromServer <2.9)
        {
            star_detail1.setBackgroundResource(R.drawable.filled_star2);
            star_detail2.setBackgroundResource(R.drawable.filled_star2);
            star_detail3.setBackgroundResource(R.drawable.star_4_filled);
            if( Utils.getSelectedLanguage(getActivity()).equalsIgnoreCase(Constants.LAN_ARABIC)) {
                star_detail3.setImageBitmap(Utils.flipImage(BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.star_4_filled),2));
            }
//            star_detail4.setBackgroundResource(R.drawable.empty_star2);
//            star_detail5.setBackgroundResource(R.drawable.empty_star2);
        }
        else if(ratingFromServer >=2.9 && ratingFromServer <=3)
        {
            star_detail1.setBackgroundResource(R.drawable.filled_star2);
            star_detail2.setBackgroundResource(R.drawable.filled_star2);
            star_detail3.setBackgroundResource(R.drawable.filled_star2);
            star_detail4.setBackgroundResource(R.drawable.empty_star2);
            star_detail5.setBackgroundResource(R.drawable.empty_star2);
        }

        else if(ratingFromServer >3.0 && ratingFromServer <3.3)
        {
            star_detail1.setBackgroundResource(R.drawable.filled_star2);
            star_detail2.setBackgroundResource(R.drawable.filled_star2);
            star_detail3.setBackgroundResource(R.drawable.filled_star2);
            star_detail4.setBackgroundResource(R.drawable.star_1_filled);
            if( Utils.getSelectedLanguage(getActivity()).equalsIgnoreCase(Constants.LAN_ARABIC)) {
                star_detail4.setImageBitmap(Utils.flipImage(BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.star_1_filled),2));
            }
//            star_detail5.setBackgroundResource(R.drawable.empty_star2);

        }
        else if(ratingFromServer >=3.3 && ratingFromServer <3.45)
        {
            star_detail1.setBackgroundResource(R.drawable.filled_star2);
            star_detail2.setBackgroundResource(R.drawable.filled_star2);
            star_detail3.setBackgroundResource(R.drawable.filled_star2);
            star_detail4.setBackgroundResource(R.drawable.star_2_filled);
            if( Utils.getSelectedLanguage(getActivity()).equalsIgnoreCase(Constants.LAN_ARABIC)) {
                star_detail4.setImageBitmap(Utils.flipImage(BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.star_2_filled),2));
            }
//            star_detail5.setBackgroundResource(R.drawable.empty_star2);
        }
        else if(ratingFromServer >=3.45 && ratingFromServer <3.55)
        {
            star_detail1.setBackgroundResource(R.drawable.filled_star2);
            star_detail2.setBackgroundResource(R.drawable.filled_star2);
            star_detail3.setBackgroundResource(R.drawable.filled_star2);
            star_detail4.setBackgroundResource(R.drawable.star_half_filled);
            if( Utils.getSelectedLanguage(getActivity()).equalsIgnoreCase(Constants.LAN_ARABIC)) {
                star_detail4.setImageBitmap(Utils.flipImage(BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.star_half_filled),2));
            }
//            star_detail5.setBackgroundResource(R.drawable.empty_star2);
        }
        else if(ratingFromServer >=3.55 && ratingFromServer <3.7)
        {
            star_detail1.setBackgroundResource(R.drawable.filled_star2);
            star_detail2.setBackgroundResource(R.drawable.filled_star2);
            star_detail3.setBackgroundResource(R.drawable.filled_star2);
            star_detail4.setBackgroundResource(R.drawable.star_3_filled);
            if( Utils.getSelectedLanguage(getActivity()).equalsIgnoreCase(Constants.LAN_ARABIC)) {
                star_detail4.setImageBitmap(Utils.flipImage(BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.star_3_filled),2));
            }
//            star_detail5.setBackgroundResource(R.drawable.empty_star2);
        }
        else if(ratingFromServer >=3.7 && ratingFromServer <3.9)
        {
            star_detail1.setBackgroundResource(R.drawable.filled_star2);
            star_detail2.setBackgroundResource(R.drawable.filled_star2);
            star_detail3.setBackgroundResource(R.drawable.filled_star2);
            star_detail4.setBackgroundResource(R.drawable.star_4_filled);
            if( Utils.getSelectedLanguage(getActivity()).equalsIgnoreCase(Constants.LAN_ARABIC)) {
                star_detail4.setImageBitmap(Utils.flipImage(BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.star_4_filled),2));
            }
//            star_detail5.setBackgroundResource(R.drawable.empty_star2);
        }
        else if(ratingFromServer >=3.9 && ratingFromServer <=4)
        {
            star_detail1.setBackgroundResource(R.drawable.filled_star2);
            star_detail2.setBackgroundResource(R.drawable.filled_star2);
            star_detail3.setBackgroundResource(R.drawable.filled_star2);
            star_detail4.setBackgroundResource(R.drawable.filled_star2);
//            star_detail5.setBackgroundResource(R.drawable.empty_star2);
        }

        else if(ratingFromServer >4 && ratingFromServer <4.3)
        {
            star_detail1.setBackgroundResource(R.drawable.filled_star2);
            star_detail2.setBackgroundResource(R.drawable.filled_star2);
            star_detail3.setBackgroundResource(R.drawable.filled_star2);
            star_detail4.setBackgroundResource(R.drawable.filled_star2);
            star_detail5.setBackgroundResource(R.drawable.star_1_filled);
            if( Utils.getSelectedLanguage(getActivity()).equalsIgnoreCase(Constants.LAN_ARABIC)) {
                star_detail5.setImageBitmap(Utils.flipImage(BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.star_1_filled),2));
            }

        }
        else if(ratingFromServer >=4.3 && ratingFromServer <4.45)
        {
            star_detail1.setBackgroundResource(R.drawable.filled_star2);
            star_detail2.setBackgroundResource(R.drawable.filled_star2);
            star_detail3.setBackgroundResource(R.drawable.filled_star2);
            star_detail4.setBackgroundResource(R.drawable.filled_star2);
            star_detail5.setBackgroundResource(R.drawable.star_2_filled);
            if( Utils.getSelectedLanguage(getActivity()).equalsIgnoreCase(Constants.LAN_ARABIC)) {
                star_detail5.setImageBitmap(Utils.flipImage(BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.star_2_filled),2));
            }

        }
        else if(ratingFromServer >=4.45 && ratingFromServer <4.55)
        {
            star_detail1.setBackgroundResource(R.drawable.filled_star2);
            star_detail2.setBackgroundResource(R.drawable.filled_star2);
            star_detail3.setBackgroundResource(R.drawable.filled_star2);
            star_detail4.setBackgroundResource(R.drawable.filled_star2);
            star_detail5.setBackgroundResource(R.drawable.star_half_filled);
            if( Utils.getSelectedLanguage(getActivity()).equalsIgnoreCase(Constants.LAN_ARABIC)) {
                star_detail5.setImageBitmap(Utils.flipImage(BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.star_half_filled),2));
            }

        }
        else if(ratingFromServer >=4.55 && ratingFromServer <4.7)
        {
            star_detail1.setBackgroundResource(R.drawable.filled_star2);
            star_detail2.setBackgroundResource(R.drawable.filled_star2);
            star_detail3.setBackgroundResource(R.drawable.filled_star2);
            star_detail4.setBackgroundResource(R.drawable.filled_star2);
            star_detail5.setBackgroundResource(R.drawable.star_3_filled);
            if( Utils.getSelectedLanguage(getActivity()).equalsIgnoreCase(Constants.LAN_ARABIC)) {
                star_detail5.setImageBitmap(Utils.flipImage(BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.star_3_filled),2));
            }

        }
        else if(ratingFromServer >=4.7 && ratingFromServer <4.9)
        {
            star_detail1.setBackgroundResource(R.drawable.filled_star2);
            star_detail2.setBackgroundResource(R.drawable.filled_star2);
            star_detail3.setBackgroundResource(R.drawable.filled_star2);
            star_detail4.setBackgroundResource(R.drawable.filled_star2);
            star_detail5.setBackgroundResource(R.drawable.star_4_filled);
            if( Utils.getSelectedLanguage(getActivity()).equalsIgnoreCase(Constants.LAN_ARABIC)) {
                star_detail5.setImageBitmap(Utils.flipImage(BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.star_4_filled),2));
            }

        }
        else if(ratingFromServer >=4.9 && ratingFromServer <=5)
        {
            star_detail1.setBackgroundResource(R.drawable.filled_star2);
            star_detail2.setBackgroundResource(R.drawable.filled_star2);
            star_detail3.setBackgroundResource(R.drawable.filled_star2);
            star_detail4.setBackgroundResource(R.drawable.filled_star2);
            star_detail5.setBackgroundResource(R.drawable.filled_star2);
        }


    }

}
