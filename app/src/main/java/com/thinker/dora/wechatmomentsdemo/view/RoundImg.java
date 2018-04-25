package com.thinker.dora.wechatmomentsdemo.view;


import android.content.Context;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.thinker.dora.wechatmomentsdemo.R;


public class RoundImg extends LinearLayout {

	ImageView img;
	Matrix matrix;
	Matrix currentMatrix;
	RoundProgress progress;
	Thread thread;
	int lock = 0;
	int lock_l = 0;

	/*
	 * private Handler mHandler = new Handler(){ public void
	 * handleMessage(Message msg) { switch (msg.what) { case 1: if(pro <= 100){
	 * 
	 * pro += 1;
	 * 
	 * System.out.println("==="+pro); progress.setProgress(pro);
	 * mHandler.sendEmptyMessageDelayed(1, 50); }else{
	 * 
	 * pro = 0; //setProgress(); } break;
	 * 
	 * } }; };
	 */

	public RoundImg(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		init();
	}

	public RoundImg(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	public RoundImg(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}

	public void init() {
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.user_view_move, this);
		img = (ImageView) findViewById(R.id.img);
		progress = (RoundProgress) findViewById(R.id.roundProgressBar);

		currentMatrix = new Matrix();
		currentMatrix.set(img.getImageMatrix());
		matrix = new Matrix();
		img.setImageMatrix(matrix);
	}

	public void move() {
		if (lock == 0) {
			setAnim();
			setProgress();
			setProgressRound();
		}
	}
	
	public void remove() {
		progress.setProgress(0);
		lock_l = 1;
		img.clearAnimation();
		progress.clearAnimation();
	}

	private void setAnim() {
		img.setImageMatrix(currentMatrix);
		Animation animation = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, -0.25f, Animation.RELATIVE_TO_SELF,
				0.25f, Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f);
		animation.setDuration(2500);
		animation.setFillBefore(true);
		animation.setRepeatCount(Integer.MAX_VALUE);
		animation.setRepeatMode(Animation.RESTART);
		animation.setInterpolator(new LinearInterpolator());
		img.startAnimation(animation);
	}

	public void UpAnim() {

		Animation animation = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, -0.26f, Animation.RELATIVE_TO_SELF,
				0.26f, Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f);
		animation.setDuration(1500);
		animation.setFillAfter(true);
		img.startAnimation(animation);
	}

	public void DownAnim() {

		Animation animation = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0.26f, Animation.RELATIVE_TO_SELF,
				-0.26f, Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f);
		animation.setDuration(1500);
		animation.setFillAfter(true);
		img.startAnimation(animation);
	}

	public void MatrixAnim(float y) {
		matrix.setTranslate(y / 5, 0);
		img.setImageMatrix(matrix);
	}

	public void clearAnim() {
		img.clearAnimation();
	}

	public void setProgressRound() {
		Animation animation = new RotateAnimation(0f, 360f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		animation.setDuration(2500);
		animation.setRepeatCount(Integer.MAX_VALUE);
		animation.setRepeatMode(Animation.RESTART);
		animation.setInterpolator(new LinearInterpolator());
		progress.startAnimation(animation);
	}

	/*
	 * public void setProgress(){ mHandler.sendEmptyMessage(1); }
	 */

	public void setProgress() {
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				lock = 1;
				lock_l = 0;
				for (int i = 0; i < 100; i++) {
					if(lock_l==0){
					progress.setProgress(i);
					try {
						Thread.sleep(80);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					}else{
						break;
					}

				}

				if(lock_l==0){
					setProgress();
				}
			}
		});

		thread.start();
	}
	/*
	 * class PageTask extends AsyncTask<Integer, Integer, Integer> { //
	 * 可变长的输入参数，与AsyncTask.exucute()对应
	 * 
	 * @Override protected Integer doInBackground(Integer... params) {
	 * while(params[0] <= 100){ params[0] += 1;
	 * 
	 * publishProgress(params[0]); System.out.println("==="+params[0]);
	 * 
	 * try { Thread.sleep(80); } catch (InterruptedException e) {
	 * e.printStackTrace(); } }
	 * 
	 * return params[0];
	 * 
	 * }
	 * 
	 * @Override protected void onCancelled() { super.onCancelled(); }
	 * 
	 * // 第二参数
	 * 
	 * @Override protected void onProgressUpdate(Integer... values) {
	 * 
	 * progress.setProgress(values[0]); }
	 * 
	 * // 返回参数
	 * 
	 * @Override protected void onPostExecute(Integer result) {
	 * 
	 * task = new PageTask(); task.execute(0); }
	 * 
	 * // 启动
	 * 
	 * @Override protected void onPreExecute() {
	 * 
	 * } }
	 */
}
