package com.sr.masharef.masharef.gallary;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class SquareImageView extends ImageView {
    public SquareImageView(Context context) {
        super(context);
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        //force a 4:3 aspect ratio
        int height = Math.round(width * .75f);
        setMeasuredDimension(width, height);
       // setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth()); //Snap to width
    }


}
