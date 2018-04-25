package com.thinker.dora.wechatmomentsdemo.view;


import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thinker.dora.wechatmomentsdemo.R;


public class XHeaderView extends LinearLayout {
    public final static int STATE_NORMAL = 0;
    public final static int STATE_READY = 1;
    public final static int STATE_REFRESHING = 2;

    private final int ROTATE_ANIM_DURATION = 180;

    private LinearLayout mContainer;


    private RoundImg mProgressBar;

    private TextView mHintTextView,mHeaderTime;

    private int mState = STATE_NORMAL;


    private boolean mIsFirst;

    public XHeaderView(Context context) {
        super(context);
        initView(context);
    }

    public XHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        // Initial set header view height 0
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, 0);
        mContainer = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.user_vw_header, null);
        addView(mContainer, lp);
        setGravity(Gravity.BOTTOM);

        //mArrowImageView = (ImageView) findViewById(R.id.header_arrow);
        mHintTextView = (TextView) findViewById(R.id.header_hint_text);
        mProgressBar = (RoundImg) findViewById(R.id.header_progressbar);
        mHeaderTime = (TextView) findViewById(R.id.header_hint_time);
        //mProgressBar.move();

		/*mRotateUpAnim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -0.26f, Animation.RELATIVE_TO_SELF, 0.26f,
											Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateUpAnim.setFillAfter(true);

        mRotateDownAnim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.26f, Animation.RELATIVE_TO_SELF, -0.26f,
											Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateDownAnim.setFillAfter(true);*/
    }

    public void setState(int state,float Y) {
        if (state == mState && mIsFirst) {
            mIsFirst = true;
            return;
        }

        switch (state) {
            case STATE_NORMAL:
                /*if (mState == STATE_READY) {
                	mProgressBar.clearAnim();
                	mProgressBar.DownAnim(Y);
                }

                if (mState == STATE_REFRESHING) {
                	mProgressBar.clearAnim();
                	mProgressBar.move();
                	mProgressBar.clearAnim();
                	mProgressBar.DownAnim(Y);
                }*/

                mHintTextView.setText(R.string.header_hint_refresh_normal);
            	mProgressBar.MatrixAnim(Y);
            	long time = System.currentTimeMillis();
//            	mHeaderTime.setText(Tool.getDateTime(Tool.loadlong(getContext(), "now_time", time)));  
                break;

            case STATE_READY:
                if (mState != STATE_READY) {/*
                	mProgressBar.clearAnim();
                	mProgressBar.UpAnim(Y);
                	*/
                    mHintTextView.setText(R.string.header_hint_refresh_ready);
                	mProgressBar.MatrixAnim(Y);
                }
                break;

            case STATE_REFRESHING:
                mHintTextView.setText(R.string.header_hint_refresh_loading);
            	mProgressBar.move();
            	long time1 = System.currentTimeMillis();
//            	Tool.savelong(getContext(), "now_time", time1);
//            	mHeaderTime.setText(Tool.getDateTime(System.currentTimeMillis()));  
                break;

            default:
                break;
        }

        mState = state;
    }

    /**
     * Set the header view visible height.
     *
     * @param height
     */
    public void setVisibleHeight(int height) {
        if (height < 0) height = 0;
        LayoutParams lp = (LayoutParams) mContainer.getLayoutParams();
        lp.height = height;
        mContainer.setLayoutParams(lp);
    }

    /**
     * Get the header view visible height.
     *
     * @return
     */
    public int getVisibleHeight() {
        return mContainer.getHeight();
    }

    public void setLock(){
    	mProgressBar.lock = 0;
    	mProgressBar.remove();
    }
}
