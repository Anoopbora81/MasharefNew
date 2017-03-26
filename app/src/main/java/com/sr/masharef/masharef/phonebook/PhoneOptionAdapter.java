package com.sr.masharef.masharef.phonebook;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.sr.masharef.masharef.R;
import com.sr.masharef.masharef.constants.Constants;
import com.sr.masharef.masharef.model.phonebook.AContact;
import com.sr.masharef.masharef.utility.Utils;

import java.util.ArrayList;

/**
 * Created by lenovo on 12/01/2017.
 */
public class PhoneOptionAdapter extends RecyclerView.Adapter<PhoneOptionAdapter.MyViewHolder> {
    private  ArrayList<AContact> phoneadapterOptionList;
    FragmentManager apapterFragmentManager;
    Context context;
    final int FLIP_VERTICAL = 1;
    final int FLIP_HORIZONTAL = 2;
    //    int postionValue;
    public PhoneOptionAdapter(Context mContext, FragmentManager fm,  ArrayList<AContact> phoneOptionList) {
        this.phoneadapterOptionList = phoneOptionList;
        this.apapterFragmentManager = fm;
        this.context = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.phone_cat_opt_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
//        postionValue=position;
        AContact phoneOption = phoneadapterOptionList.get(position);

        holder.nametext.setText(""+phoneOption.getContactName()+" "+phoneOption.lastName);
        // holder.occupationtext.setText("" + phoneOption.getContactOccupation());
        holder.workplacetext.setText("" +phoneOption.getContactWorkplace());
        String contactNumber = phoneOption.getContactNo();
        holder.mobiletext.setText("" + contactNumber);
        if( Utils.getSelectedLanguage(context).equalsIgnoreCase(Constants.LAN_ARABIC)) {
            String contactNumber_ar = contactNumber.substring(1)+"+";
            holder.mobiletext.setText("" + contactNumber_ar);

         //   holder.imViewAndroid.setImageBitmap(flipImage(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_phone_c),2));

        }


//        holder.nametext.setText("Name: " + phoneOption.getContactName());
//        holder.occupationtext.setText("Occupation: " + phoneOption.getContactOccupation());
//        holder.workplacetext.setText("Workplace: " +phoneOption.getContactWorkplace());
//        holder.mobiletext.setText("Mobile No: " + phoneOption.getContactNo());
        String ratingString=phoneOption.getContactRating();
        float rating =0.0f;
        if(ratingString.isEmpty()){
            rating= 0.0f;  //0
        }
        else
        {
            rating = Float.parseFloat(ratingString);
        }

        updateRatingBar(rating, holder);
    }

   /* public Bitmap flipImage(Bitmap src, int type) {
        // create new matrix for transformation
        Matrix matrix = new Matrix();
        // if vertical
        if(type == FLIP_VERTICAL) {
            // y = y * -1
            matrix.preScale(1.0f, -1.0f);
        }
        // if horizonal
        else if(type == FLIP_HORIZONTAL) {
            // x = x * -1
            matrix.preScale(-1.0f, 1.0f);
            // unknown type
        } else {
            return null;
        }

        // return transformed image
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }*/

    @Override
    public int getItemCount() {
        return phoneadapterOptionList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,ActivityCompat.OnRequestPermissionsResultCallback {
        TextView nametext, occupationtext, workplacetext, mobiletext;
        ImageView image, phone, star1, star2, star3, star4, star5, imViewAndroid;
        RatingBar ratingbar;
        CardView cardView;
        View view;
        private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;

        public MyViewHolder(View itemViewOption) {
            super(itemViewOption);
            view = itemViewOption;
            cardView = (CardView) itemViewOption.findViewById(R.id.card_view);
            nametext = (TextView) itemViewOption.findViewById(R.id.textViewCategeoryOptName);
            //occupationtext = (TextView) itemViewOption.findViewById(R.id.textViewCategeoryOptDesignation);
            workplacetext = (TextView) itemViewOption.findViewById(R.id.textViewCategeoryOptWorkspace);
            mobiletext = (TextView) itemViewOption.findViewById(R.id.textViewCategeoryOptPhNo);
            image = (ImageView) itemViewOption.findViewById(R.id.imageViewCategeoryOptCard);
            //ratingbar = (RatingBar) itemViewOption.findViewById(R.id.ratingBarCatOpt);
            star1 = (ImageView) itemViewOption.findViewById(R.id.star_image1);
            star2 = (ImageView) itemViewOption.findViewById(R.id.star_image2);
            star3 = (ImageView) itemViewOption.findViewById(R.id.star_image3);
            star4 = (ImageView) itemViewOption.findViewById(R.id.star_image4);
            star5 = (ImageView) itemViewOption.findViewById(R.id.star_image5);

            phone = (ImageView) itemViewOption.findViewById(R.id.phoneCatOpt);
            System.out.print("is enter");
            imViewAndroid = (ImageView) itemViewOption.findViewById(R.id.phoneCatOpt);
//            LayerDrawable stars = (LayerDrawable) ratingbar.getProgressDrawable();
//            stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);

            phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.print("is enter1");
                    /*Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:123456789"));
                    *//*if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }*//*
                    try {
                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                            context.startActivity(callIntent);
                        }
//                        context.startActivity(callIntent);
                    }catch (Exception e){
                        System.out.println("call exception"+e);
                    }*/
                    makePhoneCall(view);
                }
            });
            cardView.setOnClickListener(this);
        }

        public void makePhoneCall(View view) {

            int permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
            } else {
                callPhone();
            }

        }

        @Override
        public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
            switch (requestCode) {
                case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        callPhone();
                    }
                }
            }
        }

        private void callPhone() {
            int idPhone = getAdapterPosition();
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:"+ phoneadapterOptionList.get(idPhone).getContactNo()));
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                context.startActivity(intent);
            }
        }
        @Override
        public void onClick(View view) {
            int id = getAdapterPosition();
            Fragment phoneDetail;
            FragmentTransaction ft = apapterFragmentManager.beginTransaction();
            phoneDetail = new PhonePesonDetails();
            Bundle personInfoBundle = new Bundle();
            personInfoBundle.putString("phoneContactId", phoneadapterOptionList.get(id).getContactId());
            personInfoBundle.putString("phonename", phoneadapterOptionList.get(id).getContactName());
            personInfoBundle.putString("phoneoccupation", phoneadapterOptionList.get(id).getContactOccupation());
            personInfoBundle.putString("phoneworkplace", phoneadapterOptionList.get(id).getContactWorkplace());
            personInfoBundle.putString("phonemobile", phoneadapterOptionList.get(id).getContactNo());
            personInfoBundle.putString("phoneRating", phoneadapterOptionList.get(id).getContactRating());
            phoneDetail.setArguments(personInfoBundle);
            ft.replace(R.id.content_frame, phoneDetail, "phonetab");
            ft.addToBackStack("del_phone");
            ft.commit();
        }
    }

    private void updateRatingBar(float ratingFromServer, MyViewHolder holder) {

        if (ratingFromServer > 0 && ratingFromServer < 0.3) {
            holder.star1.setBackgroundResource(R.drawable.small_star_1_filled);
            if( Utils.getSelectedLanguage(context).equalsIgnoreCase(Constants.LAN_ARABIC)) {
                holder.star1.setImageBitmap(Utils.flipImage(BitmapFactory.decodeResource(context.getResources(), R.drawable.small_star_1_filled),2));
            }

        } else if (ratingFromServer >= 0.3 && ratingFromServer < 0.45) {
            holder.star1.setBackgroundResource(R.drawable.small_star_2_filled);
            if( Utils.getSelectedLanguage(context).equalsIgnoreCase(Constants.LAN_ARABIC)) {
                holder.star1.setImageBitmap(Utils.flipImage(BitmapFactory.decodeResource(context.getResources(), R.drawable.small_star_2_filled),2));
            }
        } else if (ratingFromServer >= 0.45 && ratingFromServer < 0.55) {
            holder.star1.setBackgroundResource(R.drawable.small_star_half_filled);
            if( Utils.getSelectedLanguage(context).equalsIgnoreCase(Constants.LAN_ARABIC)) {
                holder.star1.setImageBitmap(Utils.flipImage(BitmapFactory.decodeResource(context.getResources(), R.drawable.small_star_half_filled),2));
            }
        } else if (ratingFromServer >= 0.55 && ratingFromServer < 0.7) {
            holder.star1.setBackgroundResource(R.drawable.small_star_3_filled);
            if( Utils.getSelectedLanguage(context).equalsIgnoreCase(Constants.LAN_ARABIC)) {
                holder.star1.setImageBitmap(Utils.flipImage(BitmapFactory.decodeResource(context.getResources(), R.drawable.small_star_3_filled),2));
            }
        } else if (ratingFromServer >= 0.7 && ratingFromServer < 0.9) {
            holder.star1.setBackgroundResource(R.drawable.small_star_4_filled);
            if( Utils.getSelectedLanguage(context).equalsIgnoreCase(Constants.LAN_ARABIC)) {
                holder.star1.setImageBitmap(Utils.flipImage(BitmapFactory.decodeResource(context.getResources(), R.drawable.small_star_4_filled),2));
            }
        } else if (ratingFromServer >= 0.9 && ratingFromServer <= 1) {
            holder.star1.setBackgroundResource(R.drawable.star_filled);
        } else if (ratingFromServer >= 1 && ratingFromServer < 1.3) {
            holder.star1.setBackgroundResource(R.drawable.star_filled);
            holder.star2.setBackgroundResource(R.drawable.small_star_1_filled);
            if( Utils.getSelectedLanguage(context).equalsIgnoreCase(Constants.LAN_ARABIC)) {
                holder.star2.setImageBitmap(Utils.flipImage(BitmapFactory.decodeResource(context.getResources(), R.drawable.small_star_1_filled),2));
            }
        } else if (ratingFromServer >= 1.3 && ratingFromServer < 1.45) {
            holder.star1.setBackgroundResource(R.drawable.star_filled);
            holder.star2.setBackgroundResource(R.drawable.small_star_2_filled);
            if( Utils.getSelectedLanguage(context).equalsIgnoreCase(Constants.LAN_ARABIC)) {
                holder.star2.setImageBitmap(Utils.flipImage(BitmapFactory.decodeResource(context.getResources(), R.drawable.small_star_2_filled),2));
            }
        } else if (ratingFromServer >= 1.45 && ratingFromServer < 1.55) {
            holder.star1.setBackgroundResource(R.drawable.star_filled);
            holder.star2.setBackgroundResource(R.drawable.small_star_half_filled);
            if( Utils.getSelectedLanguage(context).equalsIgnoreCase(Constants.LAN_ARABIC)) {
                holder.star2.setImageBitmap(Utils.flipImage(BitmapFactory.decodeResource(context.getResources(), R.drawable.small_star_half_filled),2));
            }
        } else if (ratingFromServer >= 1.55 && ratingFromServer < 1.7) {
            holder.star1.setBackgroundResource(R.drawable.star_filled);
            holder.star2.setBackgroundResource(R.drawable.small_star_3_filled);
            if( Utils.getSelectedLanguage(context).equalsIgnoreCase(Constants.LAN_ARABIC)) {
                holder.star2.setImageBitmap(Utils.flipImage(BitmapFactory.decodeResource(context.getResources(), R.drawable.small_star_3_filled),2));
            }
        } else if (ratingFromServer >= 1.7 && ratingFromServer < 1.9) {
            holder.star1.setBackgroundResource(R.drawable.star_filled);
            holder.star2.setBackgroundResource(R.drawable.small_star_4_filled);
            if( Utils.getSelectedLanguage(context).equalsIgnoreCase(Constants.LAN_ARABIC)) {
                holder.star2.setImageBitmap(Utils.flipImage(BitmapFactory.decodeResource(context.getResources(), R.drawable.small_star_4_filled),2));
            }
        } else if (ratingFromServer >= 1.9 && ratingFromServer <= 2) {
            holder.star1.setBackgroundResource(R.drawable.star_filled);
            holder.star2.setBackgroundResource(R.drawable.star_filled);
        } else if (ratingFromServer > 2 && ratingFromServer < 2.3) {
            holder.star1.setBackgroundResource(R.drawable.star_filled);
            holder.star2.setBackgroundResource(R.drawable.star_filled);
            holder.star3.setBackgroundResource(R.drawable.small_star_1_filled);
            if( Utils.getSelectedLanguage(context).equalsIgnoreCase(Constants.LAN_ARABIC)) {
                holder.star3.setImageBitmap(Utils.flipImage(BitmapFactory.decodeResource(context.getResources(), R.drawable.small_star_1_filled),2));
            }
        } else if (ratingFromServer >= 2.3 && ratingFromServer < 2.45) {
            holder.star1.setBackgroundResource(R.drawable.star_filled);
            holder.star2.setBackgroundResource(R.drawable.star_filled);
            holder.star3.setBackgroundResource(R.drawable.small_star_2_filled);
            if( Utils.getSelectedLanguage(context).equalsIgnoreCase(Constants.LAN_ARABIC)) {
                holder.star3.setImageBitmap(Utils.flipImage(BitmapFactory.decodeResource(context.getResources(), R.drawable.small_star_2_filled),2));
            }
        } else if (ratingFromServer >= 2.45 && ratingFromServer < 2.55) {
            holder.star1.setBackgroundResource(R.drawable.star_filled);
            holder.star2.setBackgroundResource(R.drawable.star_filled);
            holder.star3.setBackgroundResource(R.drawable.small_star_half_filled);
            if( Utils.getSelectedLanguage(context).equalsIgnoreCase(Constants.LAN_ARABIC)) {
                holder.star3.setImageBitmap(Utils.flipImage(BitmapFactory.decodeResource(context.getResources(), R.drawable.small_star_half_filled),2));
            }
        } else if (ratingFromServer >= 2.55 && ratingFromServer < 2.7) {
            holder.star1.setBackgroundResource(R.drawable.star_filled);
            holder.star2.setBackgroundResource(R.drawable.star_filled);
            holder.star3.setBackgroundResource(R.drawable.small_star_3_filled);
            if( Utils.getSelectedLanguage(context).equalsIgnoreCase(Constants.LAN_ARABIC)) {
                holder.star3.setImageBitmap(Utils.flipImage(BitmapFactory.decodeResource(context.getResources(), R.drawable.small_star_3_filled),2));
            }
        } else if (ratingFromServer >= 2.7 && ratingFromServer < 2.9) {
            holder.star1.setBackgroundResource(R.drawable.star_filled);
            holder.star2.setBackgroundResource(R.drawable.star_filled);
            holder.star3.setBackgroundResource(R.drawable.small_star_4_filled);
            if( Utils.getSelectedLanguage(context).equalsIgnoreCase(Constants.LAN_ARABIC)) {
                holder.star3.setImageBitmap(Utils.flipImage(BitmapFactory.decodeResource(context.getResources(), R.drawable.small_star_4_filled),2));
            }
        } else if (ratingFromServer >= 2.9 && ratingFromServer <= 3) {
            holder.star1.setBackgroundResource(R.drawable.star_filled);
            holder.star2.setBackgroundResource(R.drawable.star_filled);
            holder.star3.setBackgroundResource(R.drawable.star_filled);
        } else if (ratingFromServer > 3 && ratingFromServer < 3.3) {
            holder.star1.setBackgroundResource(R.drawable.star_filled);
            holder.star2.setBackgroundResource(R.drawable.star_filled);
            holder.star3.setBackgroundResource(R.drawable.star_filled);
            holder.star4.setBackgroundResource(R.drawable.small_star_1_filled);
            if( Utils.getSelectedLanguage(context).equalsIgnoreCase(Constants.LAN_ARABIC)) {
                holder.star4.setImageBitmap(Utils.flipImage(BitmapFactory.decodeResource(context.getResources(), R.drawable.small_star_1_filled),2));
            }

        } else if (ratingFromServer >= 3.3 && ratingFromServer < 3.45) {
            holder.star1.setBackgroundResource(R.drawable.star_filled);
            holder.star2.setBackgroundResource(R.drawable.star_filled);
            holder.star3.setBackgroundResource(R.drawable.star_filled);
            holder.star4.setBackgroundResource(R.drawable.small_star_2_filled);
            if( Utils.getSelectedLanguage(context).equalsIgnoreCase(Constants.LAN_ARABIC)) {
                holder.star4.setImageBitmap(Utils.flipImage(BitmapFactory.decodeResource(context.getResources(), R.drawable.small_star_2_filled),2));
            }
        } else if (ratingFromServer >= 3.45 && ratingFromServer < 3.55) {
            holder.star1.setBackgroundResource(R.drawable.star_filled);
            holder.star2.setBackgroundResource(R.drawable.star_filled);
            holder.star3.setBackgroundResource(R.drawable.star_filled);
            holder.star4.setBackgroundResource(R.drawable.small_star_half_filled);
            if( Utils.getSelectedLanguage(context).equalsIgnoreCase(Constants.LAN_ARABIC)) {
                holder.star4.setImageBitmap(Utils.flipImage(BitmapFactory.decodeResource(context.getResources(), R.drawable.small_star_half_filled),2));
            }
        } else if (ratingFromServer >= 3.55 && ratingFromServer < 3.7) {
            holder.star1.setBackgroundResource(R.drawable.star_filled);
            holder.star2.setBackgroundResource(R.drawable.star_filled);
            holder.star3.setBackgroundResource(R.drawable.star_filled);
            holder.star4.setBackgroundResource(R.drawable.small_star_3_filled);
            if( Utils.getSelectedLanguage(context).equalsIgnoreCase(Constants.LAN_ARABIC)) {
                holder.star4.setImageBitmap(Utils.flipImage(BitmapFactory.decodeResource(context.getResources(), R.drawable.small_star_3_filled),2));
            }
        } else if (ratingFromServer >= 3.7 && ratingFromServer < 3.9) {
            holder.star1.setBackgroundResource(R.drawable.star_filled);
            holder.star2.setBackgroundResource(R.drawable.star_filled);
            holder.star3.setBackgroundResource(R.drawable.star_filled);
            holder.star4.setBackgroundResource(R.drawable.small_star_4_filled);
            if( Utils.getSelectedLanguage(context).equalsIgnoreCase(Constants.LAN_ARABIC)) {
                holder.star4.setImageBitmap(Utils.flipImage(BitmapFactory.decodeResource(context.getResources(), R.drawable.small_star_4_filled),2));
            }
        } else if (ratingFromServer >= 3.9 && ratingFromServer <= 4) {
            holder.star1.setBackgroundResource(R.drawable.star_filled);
            holder.star2.setBackgroundResource(R.drawable.star_filled);
            holder.star3.setBackgroundResource(R.drawable.star_filled);
            holder.star4.setBackgroundResource(R.drawable.star_filled);
        } else if (ratingFromServer > 4 && ratingFromServer < 4.3) {
            holder.star1.setBackgroundResource(R.drawable.star_filled);
            holder.star2.setBackgroundResource(R.drawable.star_filled);
            holder.star3.setBackgroundResource(R.drawable.star_filled);
            holder.star4.setBackgroundResource(R.drawable.star_filled);
            holder.star5.setBackgroundResource(R.drawable.small_star_1_filled);
            if( Utils.getSelectedLanguage(context).equalsIgnoreCase(Constants.LAN_ARABIC)) {
                holder.star5.setImageBitmap(Utils.flipImage(BitmapFactory.decodeResource(context.getResources(), R.drawable.small_star_1_filled),2));
            }

        } else if (ratingFromServer >= 4.3 && ratingFromServer < 4.45) {
            holder.star1.setBackgroundResource(R.drawable.star_filled);
            holder.star2.setBackgroundResource(R.drawable.star_filled);
            holder.star3.setBackgroundResource(R.drawable.star_filled);
            holder.star4.setBackgroundResource(R.drawable.star_filled);
            holder.star5.setBackgroundResource(R.drawable.small_star_2_filled);
            if( Utils.getSelectedLanguage(context).equalsIgnoreCase(Constants.LAN_ARABIC)) {
                holder.star5.setImageBitmap(Utils.flipImage(BitmapFactory.decodeResource(context.getResources(), R.drawable.small_star_2_filled),2));
            }
        } else if (ratingFromServer >= 4.45 && ratingFromServer < 4.55) {
            holder.star1.setBackgroundResource(R.drawable.star_filled);
            holder.star2.setBackgroundResource(R.drawable.star_filled);
            holder.star3.setBackgroundResource(R.drawable.star_filled);
            holder.star4.setBackgroundResource(R.drawable.star_filled);
            holder.star5.setBackgroundResource(R.drawable.small_star_half_filled);
            if( Utils.getSelectedLanguage(context).equalsIgnoreCase(Constants.LAN_ARABIC)) {
                holder.star5.setImageBitmap(Utils.flipImage(BitmapFactory.decodeResource(context.getResources(), R.drawable.small_star_half_filled),2));
            }
        } else if (ratingFromServer >= 4.55 && ratingFromServer < 4.7) {
            holder.star1.setBackgroundResource(R.drawable.star_filled);
            holder.star2.setBackgroundResource(R.drawable.star_filled);
            holder.star3.setBackgroundResource(R.drawable.star_filled);
            holder.star4.setBackgroundResource(R.drawable.star_filled);
            holder.star5.setBackgroundResource(R.drawable.small_star_3_filled);
            if( Utils.getSelectedLanguage(context).equalsIgnoreCase(Constants.LAN_ARABIC)) {
                holder.star5.setImageBitmap(Utils.flipImage(BitmapFactory.decodeResource(context.getResources(), R.drawable.small_star_3_filled),2));
            }
        } else if (ratingFromServer >= 4.7 && ratingFromServer < 4.9) {
            holder.star1.setBackgroundResource(R.drawable.star_filled);
            holder.star2.setBackgroundResource(R.drawable.star_filled);
            holder.star3.setBackgroundResource(R.drawable.star_filled);
            holder.star4.setBackgroundResource(R.drawable.star_filled);
            holder.star5.setBackgroundResource(R.drawable.small_star_4_filled);
            if( Utils.getSelectedLanguage(context).equalsIgnoreCase(Constants.LAN_ARABIC)) {
                holder.star5.setImageBitmap(Utils.flipImage(BitmapFactory.decodeResource(context.getResources(), R.drawable.small_star_4_filled),2));
            }
        } else if (ratingFromServer >= 4.9 && ratingFromServer <= 5) {
            holder.star1.setBackgroundResource(R.drawable.star_filled);
            holder.star2.setBackgroundResource(R.drawable.star_filled);
            holder.star3.setBackgroundResource(R.drawable.star_filled);
            holder.star4.setBackgroundResource(R.drawable.star_filled);
            holder.star5.setBackgroundResource(R.drawable.star_filled);
        }
    }

}
