package org.demo.cn.widget;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.demo.cn.R;
import org.demo.cn.utils.Constant;
import org.demo.cn.utils.DataTypeUtils;
import org.demo.cn.utils.DensityUtil;
import org.demo.cn.vo.VideoVo;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnInfoListener;
import io.vov.vitamio.utils.StringUtils;
import io.vov.vitamio.widget.VideoView;
import io.vov.vitamio.widget.MediaController.OnHiddenListener;
import io.vov.vitamio.widget.MediaController.OnShownListener;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.FrameLayout.LayoutParams;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.Slider;

public class MediaControllerView extends FrameLayout implements
		Slider.OnValueChangedListener, MediaPlayer.OnBufferingUpdateListener,
		MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener,
		OnInfoListener, OnSeekBarChangeListener {

	private static final int delayMillis = 3000;
	private static final int FADE_OUT = 1;
	private static final int SHOW_PROGRESS = 2;
	private VideoView mVideoView;
	private View mLoadingView;
	private View mMediaPhoto;
	private RelativeLayout mMediaCtrl01;
	private RelativeLayout mMediaCtrl02;
	
	
	private Context context;
	private int mLayout = VideoView.VIDEO_LAYOUT_ZOOM;
	private ImageButton mPauseButton;

	private SeekBar mProgress;
	private TextView mEndTime, mCurrentTime;
	private TextView mFileName;
	private long mDuration;
	private boolean mShowing;
	private boolean mDragging;
	private boolean mInstantSeeking = false;

	private View mVolumeBrightnessLayout;
	private ImageView mOperationBg;
	private ImageView mOperationPercent;
	private AudioManager mAudioManager;
	private GestureDetector mGestureDetector;
	/** 最大声音 */
	private int mMaxVolume;
	/** 当前声音 */
	private int mVolume = -1;
	/** 当前亮度 */
	private float mBrightness = -1f;
	private int mDistance = 10;

	private ImageButton mFullScreen;
	private boolean isFullScreen = false;
	private RelativeLayout mMediaComment;
	private RelativeLayout mMediaRelative;
	private ProgressBar mProgressBar;
	private TextView mPercent;
	
	private boolean isFirst = false;

	
	private VideoVo mVideoVo;

	public VideoVo getmVideoVo() {
		return mVideoVo;
	}

	public void setmVideoVo(VideoVo mVideoVo) {
		this.mVideoVo = mVideoVo;
	}

	public RelativeLayout getmMediaRelative() {
		return mMediaRelative;
	}

	public void setmMediaRelative(RelativeLayout mMediaRelative) {
		this.mMediaRelative = mMediaRelative;
	}

	public boolean isFullScreen() {
		return isFullScreen;
	}

	public MediaControllerView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public MediaControllerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public MediaControllerView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView(context);
	}

	private void initView(Context context) {
		this.context = context;
		View v = LayoutInflater.from(context).inflate(
				R.layout.media_controller_view, null);
		addView(v);
		
		mMediaCtrl01 = findView(R.id.media_ctrl01);
		mMediaCtrl02 = findView(R.id.media_ctrl02);
		
		mVideoView = findView(R.id.media_view);

		// mVideoView.setOnTouchListener(this);
		mVideoView.setOnPreparedListener(this);
		mVideoView.setOnBufferingUpdateListener(this);
		mVideoView.setOnCompletionListener(this);
		mVideoView.setOnInfoListener(this);

		mVideoView.requestFocus();

		mPauseButton = findView(R.id.mediacontroller_play_pause);
		mPauseButton.setImageResource(getResources().getIdentifier(
				"mediacontroller_play_button", "drawable",
				this.context.getPackageName()));
		mPauseButton.setVisibility(View.VISIBLE);

		mLoadingView = findView(R.id.video_loading);
		mLoadingView.setVisibility(View.GONE);
		//mProgressBar = findView(R.id.mediacontroller_progressbar);
		mMediaPhoto = findView(R.id.media_photo);
		//mPercent = findView(R.id.mediacontroller_percent);

		mProgress = findView(R.id.mediacontroller_seekbar);
		mProgress.setMax(1000);
		SeekBar seeker = (SeekBar) mProgress;
		seeker.setOnSeekBarChangeListener(this);

		mEndTime = findView(R.id.mediacontroller_time_total);
		mCurrentTime = findView(R.id.mediacontroller_time_current);
		mFileName = findView(R.id.mediacontroller_file_name);

		mVolumeBrightnessLayout = findViewById(R.id.operation_volume_brightness);
		mOperationBg = (ImageView) findViewById(R.id.operation_bg);
		mOperationPercent = (ImageView) findViewById(R.id.operation_percent);

		mAudioManager = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		mMaxVolume = mAudioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

		mGestureDetector = new GestureDetector(mVideoView.getContext(),
				new MyGestureListener());

		mFullScreen = findView(R.id.mediacontroller_fullscreen);
		
	

		Log.v("CNetHelper",
				mVideoView.getWidth() + "," + mVideoView.getHeight());

		setMediaPlayButton();

		setMediaFullScreem();

	}

	public void initVideoView() {
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, DensityUtil.dip2px(context, Constant.MEDIA_HEIGHT));
		mVideoView.setLayoutParams(lp);
	}
	
	private void setMediaCtrlDisplay(boolean isshow){
		if(isshow){
			mMediaCtrl01.setVisibility(View.VISIBLE);
			mMediaCtrl02.setVisibility(View.VISIBLE);
		}else{
			mMediaCtrl01.setVisibility(View.GONE);
			mMediaCtrl02.setVisibility(View.GONE);
		}
	}

	public RelativeLayout getmMediaComment() {
		return mMediaComment;
	}

	public void setmMediaComment(RelativeLayout mMediaComment) {
		this.mMediaComment = mMediaComment;
	}

	private void setMediaFullScreem() {
		mFullScreen.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				fullscreenChanger();
			}

		});
	}

	private void fullscreenChanger() {
		if (isFullScreen()) {
			mFullScreen.setImageResource(getResources().getIdentifier(
					"mediacontroller_fullscreen_button", "drawable",
					this.context.getPackageName()));
			mFullScreen.setVisibility(View.VISIBLE);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, DensityUtil.dip2px(context, Constant.MEDIA_HEIGHT));
			if (mMediaRelative != null) {
				mMediaRelative.setLayoutParams(lp);
			}
			if (mMediaComment != null) {
				mMediaComment.setVisibility(View.VISIBLE);
			}

			RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, DensityUtil.dip2px(context, Constant.MEDIA_HEIGHT));
			mVideoView.setLayoutParams(lp2);

			((Activity) this.context)
					.setRequestedOrientation(ActivityInfo.CONFIG_SMALLEST_SCREEN_SIZE);
			mVideoView.setVideoLayout(VideoView.VIDEO_LAYOUT_STRETCH, 0);
			Log.v("CNetHelper",
					mVideoView.getWidth() + "," + mVideoView.getHeight());
			isFullScreen = false;
		} else {
			mFullScreen.setImageResource(getResources().getIdentifier(
					"mediacontroller_fullscreen_off_button", "drawable",
					this.context.getPackageName()));
			mFullScreen.setVisibility(View.VISIBLE);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			if (mMediaRelative != null) {
				mMediaRelative.setLayoutParams(lp);
			}
			if (mMediaComment != null) {
				mMediaComment.setVisibility(View.GONE);
			}

			mVideoView.setVideoLayout(VideoView.VIDEO_LAYOUT_STRETCH, 0);
			RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			mVideoView.setLayoutParams(lp2);
			((Activity) this.context)
					.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

			Log.v("CNetHelper",
					mVideoView.getWidth() + "," + mVideoView.getHeight());
			isFullScreen = true;
		}

	}

	private void setMediaPlayButton() {

		mPauseButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				playStateChange();
			}
		});
	}

	private void playStateChange() {
		if (isPlaying()) {
			mPauseButton.setImageResource(getResources().getIdentifier(
					"mediacontroller_play_button", "drawable",
					this.context.getPackageName()));
			mPauseButton.setVisibility(View.VISIBLE);
			mVideoView.pause();
		} else {
			mPauseButton.setImageResource(getResources().getIdentifier(
					"mediacontroller_pause_button", "drawable",
					this.context.getPackageName()));
			mPauseButton.setVisibility(View.VISIBLE);
			mMediaPhoto.setVisibility(View.GONE);
			if(mVideoVo!=null && !isFirst){
                Log.v("CNetHelper", "this click url here :"+mVideoVo.getUrl());
				setVideoPath(mVideoVo.getUrl());
				isFirst = true;
				mLoadingView.setVisibility(View.VISIBLE);
				
			}
			mVideoView.start();

		}
		show(delayMillis);
	}

	/**
	 * 开始播放
	 */
	public void start() {
		mPauseButton.setImageResource(getResources().getIdentifier(
				"mediacontroller_pause_button", "drawable",
				this.context.getPackageName()));
		mPauseButton.setVisibility(View.VISIBLE);
		mVideoView.start();
	}

	/**
	 * 暂停播放
	 */

	public void pause() {
		mPauseButton.setImageResource(getResources().getIdentifier(
				"mediacontroller_play_button", "drawable",
				this.context.getPackageName()));
		mPauseButton.setVisibility(View.VISIBLE);
		mVideoView.pause();
	}

	/**
	 * 释放资源
	 */

	public void release() {
		mVideoView.stopPlayback();
	}

	@TargetApi(21)
	@Override
	public void onCompletion(MediaPlayer player) {
		mPauseButton.setImageResource(getResources().getIdentifier(
				"mediacontroller_play_button", "drawable",
				this.context.getPackageName()));
		mPauseButton.setVisibility(View.VISIBLE);
		mPauseButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mVideoView.seekTo(0);
				setMediaPlayButton();
				mVideoView.start();
			}
		});
	}

	private void stopPlayer() {

		if (mVideoView != null) {
			mPauseButton.setImageResource(getResources().getIdentifier(
					"mediacontroller_play_button", "drawable",
					this.context.getPackageName()));
			mPauseButton.setVisibility(View.VISIBLE);

			mVideoView.pause();
			Log.v("org.demo.cn", "****************mVideoView.pause()");
		}

	}

	private void startPlayer() {
		if (mVideoView != null) {
			mPauseButton.setImageResource(getResources().getIdentifier(
					"mediacontroller_pause_button", "drawable",
					this.context.getPackageName()));
			mPauseButton.setVisibility(View.VISIBLE);
			mVideoView.start();
		}
	}

	private void stopPlayback() {

	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		// TODO Auto-generated method stub
		mp.setPlaybackSpeed(1.0f);

	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		// TODO Auto-generated method stub
		Log.v("CNetHelper", ""+percent+"%");
//		if(!isPlaying()){
//			return ;
//			
//		}
//		
//		if (percent >= 99) {
//			mPercent.setVisibility(View.INVISIBLE);
//		}else{
//			if(mLoadingView.getVisibility()==View.VISIBLE){
//				mPercent.setVisibility(View.VISIBLE);
//				mPercent.setText(percent+"%");
//			}
//		}
	}

	@Override
	public void onValueChanged(int value) {
		// TODO Auto-generated method stub
		mVideoView.seekTo(value);
		mVideoView.start();
	}

	private <T extends View> T findView(int id) {
		return (T) super.findViewById(id);
	}

	public void setVideoPath(String sPath) {
		if (sPath.startsWith("http:")) {
			mVideoView.setVideoURI(Uri.parse(sPath));
		} else {
			mVideoView.setVideoPath(sPath);
		}
	}

	public void setVideoFileName(String filename) {
		if (mFileName != null) {
			mFileName.setText(filename);
		}
	}

	public VideoView getVideoView() {
		return mVideoView;
	}

	private boolean isPlaying() {
		return mVideoView != null && mVideoView.isPlaying();
	}
	
	

	private boolean needResume;

	@Override
	public boolean onInfo(MediaPlayer mp, int what, int extra) {

		Log.v("org.demo.cn", "****************oninfo  start");
		switch (what) {
		case MediaPlayer.MEDIA_INFO_BUFFERING_START:
			// 开始缓存，暂停播放
			Log.v("org.demo.cn", "****************oninfo buffering start");
			if (isPlaying()) {
				stopPlayer();
				needResume = true;
			}
			mLoadingView.setVisibility(View.VISIBLE);

			break;
		case MediaPlayer.MEDIA_INFO_BUFFERING_END:
			// 缓存完成，继续播放
			Log.v("org.demo.cn", "****************oninfo buffering end");
			if (needResume)
				startPlayer();
			mLoadingView.setVisibility(View.GONE);
			// initVideoView();
			break;
		case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
			// 显示 下载速度
			 //Log.v("CNetHelper","download rate:" + extra);
			 //mListener.onDownloadRateChanged(extra);
			break;
		}

		return true;
	}

	public VideoView getmVideoView() {
		return mVideoView;
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		long newposition = (mDuration * progress) / 1000;
		String time = StringUtils.generateTime(newposition);
		if (mInstantSeeking)
			mVideoView.seekTo(newposition);
		if (mCurrentTime != null)
			mCurrentTime.setText(time);
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		mDragging = true;
		show(3600000);
		mHandler.removeMessages(SHOW_PROGRESS);
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		if (!mInstantSeeking)
			mVideoView.seekTo((mDuration * seekBar.getProgress()) / 1000);
		mHandler.removeMessages(SHOW_PROGRESS);
		mDragging = false;
		mHandler.sendEmptyMessageDelayed(SHOW_PROGRESS, 1000);
	}

	private long setProgress() {
		if (mVideoView == null || mDragging)
			return 0;
		long position = mVideoView.getCurrentPosition();
		long duration = mVideoView.getDuration();
		if (mProgress != null) {
			if (duration > 0) {
				long pos = 1000L * position / duration;
				mProgress.setProgress((int) pos);
			}
			int percent = mVideoView.getBufferPercentage();
			mProgress.setSecondaryProgress(percent * 10);
		}
		mDuration = duration;
		if (mEndTime != null)
			mEndTime.setText(StringUtils.generateTime(mDuration));
		if (mCurrentTime != null)
			mCurrentTime.setText(StringUtils.generateTime(position));

		return position;
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			Log.v("org.demo.cn", msg.what + "**********");
			long pos;
			switch (msg.what) {
			case FADE_OUT:
				//hide();
				break;
			case SHOW_PROGRESS:
				pos = setProgress();
				if (!mDragging && mShowing) {
					msg = obtainMessage(SHOW_PROGRESS);
					sendMessageDelayed(msg, 1000 - (pos % 1000));
					// updatePausePlay();
				}
				break;
			}
		}
	};

	public void show(int timeout) {
		if (!mShowing){
			if (mPauseButton != null)
				mPauseButton.requestFocus();
			mShowing = true;
			setMediaCtrlDisplay(mShowing);
			
			
		}
		Log.v("org.demo.cn", timeout + "*************time");
		mHandler.sendEmptyMessage(SHOW_PROGRESS);
		if (timeout != 0) {
			mHandler.removeMessages(FADE_OUT);
			mHandler.sendMessageDelayed(mHandler.obtainMessage(FADE_OUT),
					timeout);
		}
	}

	public void hide() {
		if (mShowing) {
			try {
				mHandler.removeMessages(SHOW_PROGRESS);
			} catch (IllegalArgumentException ex) {
			}
			mShowing = false;
			setMediaCtrlDisplay(mShowing);
		}
	}

	/**
	 * 滑动改变声音大小
	 * 
	 * @param percent
	 */
	private void onVolumeSlide(float percent) {
		if (mVolume == -1) {
			mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
			if (mVolume < 0)
				mVolume = 0;

			// 显示
			mOperationBg.setImageResource(R.drawable.video_volumn_bg);
			mVolumeBrightnessLayout.setVisibility(View.VISIBLE);
		}

		int index = (int) (percent * mMaxVolume) + mVolume;
		// int index = (int) (percent) + mVolume;
		if (index > mMaxVolume)
			index = mMaxVolume;
		else if (index < 0)
			index = 0;

		// 变更声音
		mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);

		// 变更进度条
		ViewGroup.LayoutParams lp = mOperationPercent.getLayoutParams();
		lp.width = findViewById(R.id.operation_full).getLayoutParams().width
				* index / mMaxVolume;
		mOperationPercent.setLayoutParams(lp);
	}

	/**
	 * 滑动改变亮度
	 * 
	 * @param percent
	 */
	private void onBrightnessSlide(float percent) {
		if (mBrightness < 0) {
			mBrightness = ((Activity) this.context).getWindow().getAttributes().screenBrightness;
			if (mBrightness <= 0.00f)
				mBrightness = 0.50f;
			if (mBrightness < 0.01f)
				mBrightness = 0.01f;

			// 显示
			mOperationBg.setImageResource(R.drawable.video_brightness_bg);
			mVolumeBrightnessLayout.setVisibility(View.VISIBLE);
		}
		WindowManager.LayoutParams lpa = ((Activity) this.context).getWindow()
				.getAttributes();
		lpa.screenBrightness = mBrightness + percent;
		if (lpa.screenBrightness > 1.0f)
			lpa.screenBrightness = 1.0f;
		else if (lpa.screenBrightness < 0.01f)
			lpa.screenBrightness = 0.01f;
		((Activity) this.context).getWindow().setAttributes(lpa);

		ViewGroup.LayoutParams lp = mOperationPercent.getLayoutParams();
		lp.width = (int) (findViewById(R.id.operation_full).getLayoutParams().width * lpa.screenBrightness);
		mOperationPercent.setLayoutParams(lp);
	}

	/** 定时隐藏 */
	private Handler mDismissHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			mVolumeBrightnessLayout.setVisibility(View.GONE);
		}
	};

	/** 手势结束 */
	private void endGesture() {
		mVolume = -1;
		mBrightness = -1f;

		// 隐藏
		mDismissHandler.removeMessages(0);
		mDismissHandler.sendEmptyMessageDelayed(0, 500);
	}

	private class MyGestureListener extends SimpleOnGestureListener {

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			// TODO Auto-generated method stub
			Log.v("org.demo.cn", "**on fling**");
			// float mOldX = e1.getX(), mOldY = e1.getY();
			// int y = (int) e2.getRawY();
			Display disp = ((Activity) context).getWindowManager()
					.getDefaultDisplay();
			int windowWidth = disp.getWidth();
			int windowHeight = disp.getHeight();

			float x = e2.getX() - e1.getX();
			float y = e2.getY() - e1.getY();

			float x_limit = windowWidth / 3;
			float y_limit = windowHeight / 3;

			float x_0 = windowWidth / 2;

			float x_abs = Math.abs(x);
			float y_abs = Math.abs(y);
			if (isFullScreen) {
				if (x_abs < y_abs) {
					if (y > y_limit || y < -y_limit) {
						float val = (float) (-y * 0.5);
						if (Math.abs(e1.getX()) < x_0
								&& Math.abs(e2.getX()) < x_0) {

							onBrightnessSlide(val / windowHeight);
						} else {
							onVolumeSlide(val / windowHeight);
						}
					}
				}
			}
			return true;

		}

		private void show(String value) {
			Toast.makeText(context, value, Toast.LENGTH_SHORT).show();
		}

	}

	// @Override
	// public boolean onTouch(View v, MotionEvent event) {
	// // TODO Auto-generated method stub
	// return false;
	// }

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		mShowing = true;
		setMediaCtrlDisplay(mShowing);
		if (mGestureDetector.onTouchEvent(event)) {
			return false;
		} else {
		}

		// 处理手势结束
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_UP:
			endGesture();
			break;
		}
		return super.onTouchEvent(event);
	}
	
	public interface OnShownListener {
	    public void onShown();
	  }

	  public interface OnHiddenListener {
	    public void onHidden();
	  }

}
