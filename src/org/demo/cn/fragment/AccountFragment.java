package org.demo.cn.fragment;

import org.demo.cn.LoginActivity;
import org.demo.cn.MediaActivity;
import org.demo.cn.R;
import org.demo.cn.app.App;
import org.demo.cn.dao.UserDao;
import org.demo.cn.vo.UserVo;
import org.demo.cn.widget.CircleImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

public class AccountFragment extends RootFragment {
	
	private String TAG = "AccountFragment";
	
	public static final int	REQUEST_CODE_SUCCESS	= 200;
	public static final int	REQUEST_CODE_MY			= 1000;
	
	private CircleImageView mPersonImg;
	private LayoutInflater  mInflater;
	
	private ImageLoader     imageLoader;
	private DisplayImageOptions options;
	
	private boolean islogin = false;
	
	public AccountFragment(){}
	
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub 
		View oView = inflater.inflate(R.layout.fragment_account_layout, container, false);
		mPersonImg = (CircleImageView) oView.findViewById(R.id.person_img);
		return oView;
	}
	
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		mInflater = LayoutInflater.from(getActivity());
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
		options = new DisplayImageOptions.Builder().showStubImage(R.drawable.images)
				.showImageForEmptyUri(R.drawable.images).showImageOnFail(R.drawable.images).cacheInMemory()
				.cacheOnDisc().displayer(new RoundedBitmapDisplayer(20)).displayer(new FadeInBitmapDisplayer(300))
				.build();
		
		
		
		mPersonImg.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!islogin){
				    Intent intent = new Intent(getActivity(), LoginActivity.class);
				    startActivityForResult(intent,REQUEST_CODE_MY);
				}
			}
		});
		
	}
	
	

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Log.v("CNetHelper","-------:"+resultCode+"");
		if(requestCode==1000 && resultCode == REQUEST_CODE_SUCCESS){
			islogin = true;
			UserVo vo = (UserVo) data.getSerializableExtra("uservo");
			Log.v("CNetHelper","-------:"+vo.getImg());
			if(vo!=null){
				imageLoader.displayImage(vo.getImg(), mPersonImg, options);
				
			}
		}
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	protected void initData(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
	}
	
	

}
