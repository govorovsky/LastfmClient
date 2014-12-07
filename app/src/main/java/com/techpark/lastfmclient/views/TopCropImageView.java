package com.techpark.lastfmclient.views;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.ImageView;



public class TopCropImageView extends ImageView {
    private Matrix mMatrix;
    private boolean mHasFrame;

    @SuppressWarnings("UnusedDeclaration")
    public TopCropImageView(Context context) {
        this(context, null, 0);
    }

    @SuppressWarnings("UnusedDeclaration")
    public TopCropImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressWarnings("UnusedDeclaration")
    public TopCropImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mHasFrame = false;
        mMatrix = new Matrix();
        // we have to use own matrix because:
        // ImageView.setImageMatrix(Matrix matrix) will not call
        // configureBounds(); invalidate(); because we will operate on ImageView object
    }

    @Override
    protected boolean setFrame(int l, int t, int r, int b)
    {
        boolean changed = super.setFrame(l, t, r, b);
        if (changed) {
            mHasFrame = true;
            // we do not want to call this method if nothing changed
            setupScaleMatrix(r-l, b-t);
        }
        return changed;
    }

    private void setupScaleMatrix(int width, int height) {
        if (!mHasFrame) {
            // we have to ensure that we already have frame
            // called and have width and height
            return;
        }
        final Drawable drawable = getDrawable();
        if (drawable == null) {
            // we have to check if drawable is null because
            // when not initialized at startup drawable we can
            // rise NullPointerException
            return;
        }
        Matrix matrix = mMatrix;
        final int intrinsicWidth = drawable.getIntrinsicWidth();
        final int intrinsicHeight = drawable.getIntrinsicHeight();

        float factorWidth = width/(float) intrinsicWidth;
        float factorHeight = height/(float) intrinsicHeight;
        float factor = Math.max(factorHeight, factorWidth);

        // there magic happen and can be adjusted to current
        // needs
        matrix.setTranslate(-intrinsicWidth/2.0f, 0);
        matrix.postScale(factor, factor, 0, 0);
        matrix.postTranslate(width/2.0f, 0);
        setImageMatrix(matrix);
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        // We have to recalculate image after chaning image
        setupScaleMatrix(getWidth(), getHeight());
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        // We have to recalculate image after chaning image
        setupScaleMatrix(getWidth(), getHeight());
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        // We have to recalculate image after chaning image
        setupScaleMatrix(getWidth(), getHeight());
    }

    // We do not have to overide setImageBitmap because it calls
    // setImageDrawable method

}

//public class TopCropImageView extends ImageView {
//
//
//    public TopCropImageView(Context context) {
//        super(context);
//        setScaleType(ScaleType.MATRIX);
//    }
//
//    public TopCropImageView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        setScaleType(ScaleType.MATRIX);
//    }
//
//    public TopCropImageView(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        setScaleType(ScaleType.MATRIX);
//    }
//
//    float mWidthPercent = 0, mHeightPercent = 0;
//
//    public void setOffset(float widthPercent, float heightPercent) {
//        mWidthPercent = widthPercent;
//        mHeightPercent = heightPercent;
//    }
//
//    private void setup() {
//        setScaleType(ScaleType.MATRIX);
//    }
//
//    @Override
//    protected boolean setFrame(int l, int t, int r, int b) {
//        Matrix matrix = getImageMatrix();
//
//        float scale;
//        int viewWidth = getWidth() - getPaddingLeft() - getPaddingRight();
//        int viewHeight = getHeight() - getPaddingTop() - getPaddingBottom();
//        int drawableWidth, drawableHeight;
//        // Allow for setting the drawable later in code by guarding ourselves here.
//        if (getDrawable() != null) {
//            drawableWidth = getDrawable().getIntrinsicWidth();
//            drawableHeight = getDrawable().getIntrinsicHeight();
//        } else return super.setFrame(l, t, r, b);
//
//        // Get the scale.
//        if (drawableWidth * viewHeight > drawableHeight * viewWidth) {
//            // Drawable is flatter than view. Scale it to fill the view height.
//            // A Top/Bottom crop here should be identical in this case.
//            scale = (float) viewHeight / (float) drawableHeight;
//        } else {
//            // Drawable is taller than view. Scale it to fill the view width.
//            // Left/Right crop here should be identical in this case.
//            scale = (float) viewWidth / (float) drawableWidth;
//        }
//
//        float viewToDrawableWidth = viewWidth / scale;
//        float viewToDrawableHeight = viewHeight / scale;
//        float xOffset = mWidthPercent * (drawableWidth - viewToDrawableWidth);
//        float yOffset = mHeightPercent * (drawableHeight - viewToDrawableHeight);
//
//        // Define the rect from which to take the image portion.
//        RectF drawableRect = new RectF(xOffset, yOffset, xOffset + viewToDrawableWidth,
//                yOffset + viewToDrawableHeight);
//        RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
//        matrix.setRectToRect(drawableRect, viewRect, Matrix.ScaleToFit.FILL);
//
//        setImageMatrix(matrix);
//
//        return super.setFrame(l, t, r, b);
//    }
//

