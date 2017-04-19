package org.demo.cn.widget;

import android.content.Context;
import android.util.AttributeSet;
import io.vov.vitamio.widget.VideoView;

public class MediaView extends VideoView {
	
	private int mWidth;
	private int mHeight;

	public int getmWidth() {
		return mWidth;
	}

	public void setmWidth(int mWidth) {
		this.mWidth = mWidth;
	}

	public int getmHeight() {
		return mHeight;
	}

	public void setmHeight(int mHeight) {
		this.mHeight = mHeight;
	}

	public MediaView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public MediaView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MediaView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	@Override  
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {  
        // TODO Auto-generated method stub  
  
        int width = getDefaultSize(mWidth, widthMeasureSpec);  
        int height = getDefaultSize(mHeight, heightMeasureSpec);  
        setMeasuredDimension(width, height);  
    }  

}
