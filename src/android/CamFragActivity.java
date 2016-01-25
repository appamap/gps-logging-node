package com.geteventro.plugin;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ListView;
import com.geteventro.app.R;


public class CamFragActivity extends FragmentActivity implements Constants {

	private CameraFrag cam;
	private String category;
	private Handler mHandler = new Handler();
	private FrameLayout frame;
	private Context mContext;
	private String JRawString;


	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {


		} else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){

			finish(); //kill view
		}
	}

	@Override
	public void onAttachFragment(Fragment fragment) {

		super.onAttachFragment(fragment);
	}

	@Override
	protected void onPause() {

		super.onPause();
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

		try
		{
			if(cam!=null)
			{
				cam.cleanMemory();
				getSupportFragmentManager()
				.beginTransaction()
				.remove(cam)
				.commitAllowingStateLoss();		
				cam=null;
			}
		} catch (Exception e ) {
			Log.e("log_tag", "Error : " + e.toString());
		}	
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
	}

	@Override
	protected void onResume() {

		DisplayMetrics metrics = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(metrics);

		int realWidth = metrics.widthPixels;
		int realheight = metrics.heightPixels;

        ListView lv = (ListView) findViewById(R.id.left_drawer_camera);
        frame = (FrameLayout) findViewById(R.id.content_frame_camera);
        cam = new CameraFrag();
        cam.setJSONFrag(JRawString,realWidth, realheight);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(frame.getId(),cam, CAM_FRAG)
                .commitAllowingStateLoss();


		super.onResume();
	}


	@Override public void finish()
	{
		super.finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {

            JRawString = getIntent().getExtras().getString("JData");

			overridePendingTransition(R.layout.mainfadein, R.layout.splashfadeout );
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
			setContentView(R.layout.act_main_camera);
			mContext = this;

		} catch (Exception e ) {
			Log.e("log_tag", "Error : " + e.toString());
		}
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {

		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		return super.onCreateOptionsMenu(menu);
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		return super.onOptionsItemSelected(item);
	}


	public final static boolean isValidEmail(CharSequence target) {

		if ((target == null)||(target.length()==ZERO)) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
		}
	}
}