package org.demo.cn;

import java.util.ArrayList;
import java.util.List;

import org.demo.cn.adapter.FragmentTabAdapter;
import org.demo.cn.base.BaseFragmentActivity;
import org.demo.cn.fragment.AccountFragment;
import org.demo.cn.fragment.FoundFragment;
import org.demo.cn.fragment.MediaFragment;
import org.demo.cn.fragment.NewsFragment;
import org.demo.cn.fragment.RootFragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.RadioGroup;

public class MainActivity extends BaseFragmentActivity {

	private FragmentManager fragmentManager;

	private RadioGroup rgs;
	public List<RootFragment> fragments = new ArrayList<RootFragment>();

	public NewsFragment newsFragment;
	public MediaFragment mediaFragment;
	public FoundFragment foundFragment;
	public AccountFragment accountFragment;

	private static final int news = 1000;
	private static final int media = 1001;
	private static final int found = 1002;
	private static final int account = 1003;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initView();
	}

	private void initView() {
		newsFragment = new NewsFragment();
		// newsFragment.setRootId(news);
		mediaFragment = new MediaFragment();
		// mediaFragment.setRootId(media);
		foundFragment = new FoundFragment();
		// foundFragment.setRootId(found);
		accountFragment = new AccountFragment();
		// accountFragment.setRootId(account);
		fragments.add(newsFragment);
		// putFragment(newsFragment);
		fragments.add(mediaFragment);
		// putFragment(mediaFragment);
		fragments.add(foundFragment);
		// putFragment(foundFragment);
		fragments.add(accountFragment);
		// putFragment(accountFragment);
		rgs = (RadioGroup) findViewById(R.id.bottom_tabrg);

		fragmentManager = getSupportFragmentManager();

		tabAdapter = new FragmentTabAdapter(this, fragments, R.id.tab_content,
				rgs, fragmentManager);

		tabAdapter
				.setOnRgsExtraCheckedChangedListener(new FragmentTabAdapter.OnRgsExtraCheckedChangedListener() {
					@Override
					public void OnRgsExtraCheckedChanged(RadioGroup radioGroup,
							int checkedId, int index) {

					}
				});

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			new AlertDialog.Builder(MainActivity.this)
					.setTitle(R.string.set_dialog_title)
					.setMessage(R.string.exit_message)
					.setPositiveButton(getString(R.string.exit_confirm),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									finish();
								}

							})
					.setNegativeButton(getString(R.string.exit_cancel), null)
					.show();

		}
		return super.onKeyDown(keyCode, event);
	}

}
