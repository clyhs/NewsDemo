package org.demo.cn;

import me.maxwin.view.IXListViewLoadMore;
import me.maxwin.view.XListView;

import org.demo.cn.base.BaseActivity;
import org.demo.cn.vo.ArticleVo;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class NewsActivity extends BaseActivity{
	
	private ArticleVo mArticleVo;
	private ProgressBar mProgressBar;
	
	private TextView mTitle;
	private TextView mContent;
	private ImageView mImg;
	private TextView mCreateTime;
	private TextView mSource;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_content);
		this.mArticleVo = (ArticleVo) getIntent().getSerializableExtra("articlevo");
		initView();
		initData();
	}
	
	private void initView(){
		mProgressBar = (ProgressBar) findViewById(R.id.news_progressbar);
		mTitle = (TextView) findViewById(R.id.news_title);
		mContent = (TextView)findViewById(R.id.news_content);
		mImg = (ImageView) findViewById(R.id.news_img);
		mCreateTime = (TextView) findViewById(R.id.news_createTime);
		mSource = (TextView) findViewById(R.id.news_source);
	}
	
	private void initData(){
		if(this.mArticleVo!=null){
			mTitle.setText(this.mArticleVo.getTitle());
			mContent.setText(this.mArticleVo.getContent());
			imageLoader.displayImage(this.mArticleVo.getImages(), mImg, options);
			mCreateTime.setText(this.mArticleVo.getCreateTime());
			mSource.setText("capitalfamily.cn");
		}
		mProgressBar.setVisibility(View.GONE);
	}

	
	
	/**
	 * µã»÷·µ»Ø°´Å¥
	 * @param view
	 */
	public void back(View view)
	{
		finish();
	}


}
