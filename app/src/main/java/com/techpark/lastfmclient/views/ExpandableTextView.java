package com.techpark.lastfmclient.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.TextView;

/**
 * Created by Andrew Govorovsky on 16.12.14.
 */
public class ExpandableTextView extends TextView {

    private int maxLine = 2;
    private boolean mNeedTextMeasure;
    private int mMaxHeight;
    private int mCollapsedHeight;
    private TextView mStateTextView;


    enum State {IDLE, COLLAPSED, EXPANDED}

    private State mState = State.IDLE;

    public ExpandableTextView(Context context) {
        this(context, null);
    }

    public ExpandableTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpandableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if (!mNeedTextMeasure) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }

        mNeedTextMeasure = false;

        setMaxLines(Integer.MAX_VALUE);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        Log.v("Max height(measured)", "-" + getMeasuredHeight());

        if (getLineCount() <= maxLine) { // no need to expand
            mStateTextView.setVisibility(INVISIBLE);
            return;
        }

        mMaxHeight = getRealTextViewHeight(this);

        Log.v("Max height(calculated)", "-" + getMeasuredHeight());

        setMaxLines(maxLine);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mCollapsedHeight = getMeasuredHeight();
        Log.v("Collapsed height", "-" + mCollapsedHeight);
        mStateTextView.setVisibility(VISIBLE);

        if (mState == State.IDLE) {
            mState = State.COLLAPSED;
        }
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        mNeedTextMeasure = true;
        super.setText(text, type);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    private static int getRealTextViewHeight(TextView textView) {
        int textHeight = textView.getLayout().getLineTop(textView.getLineCount());
        int padding = textView.getCompoundPaddingTop() + textView.getCompoundPaddingBottom();
        return textHeight + padding;
    }

    public void setExpandHandler(View mExpandHandler) {
        mExpandHandler.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCollapsedHeight == 0) {
                    return; // we don't collapsed, so no need to do anything
                }
                Animation animation;
                clearAnimation();

                if (mState == State.EXPANDED) {
                    animation = new ExpandCollapseAnimation(ExpandableTextView.this, getHeight(), mCollapsedHeight);
                    mState = State.COLLAPSED;
                    mStateTextView.setText("EXPAND"); // todo resources
                } else {
                    animation = new ExpandCollapseAnimation(ExpandableTextView.this, getHeight(), mMaxHeight);
                    mState = State.EXPANDED;
                    mStateTextView.setText("HIDE");
                }
                startAnimation(animation);
            }
        });
    }

    protected class ExpandCollapseAnimation extends Animation {
        private final View mTargetView;
        private final int mStartHeight;
        private final int mEndHeight;
        private final int mDeltaY;

        public ExpandCollapseAnimation(View view, int startHeight, int endHeight) {
            mTargetView = view;
            mStartHeight = startHeight;
            mEndHeight = endHeight;
            mDeltaY = mEndHeight - mStartHeight;
            setDuration((int) (Math.abs(mDeltaY) / mTargetView.getContext().getResources().getDisplayMetrics().density)); // 1dp / ms
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            int newHeight = (int) ((mDeltaY) * interpolatedTime + mStartHeight);
            setMaxHeight(newHeight);
            mTargetView.getLayoutParams().height = newHeight;
            mTargetView.requestLayout();
        }

        @Override
        public boolean willChangeBounds() {
            return true;
        }
    }

    public void setStateTextView(TextView mStateTextView) {
        this.mStateTextView = mStateTextView;
    }
}
