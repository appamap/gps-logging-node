package com.geteventro.plugin;


import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.geteventro.app.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;


public class CameraFrag extends Fragment implements Constants {

	private float compassHeading=0;
	private float accelVal=0;
	private RelativeLayout layout;
	private SurfaceView sv;
	private CameraSurfaceHolder surfaceHolder;
	private Handler handler = new Handler();
	private Activity thisActivity;
	private boolean hideAR = false;
	private float[] inR = new float[16];
	private float[] I = new float[16];
	private float[] gravity = new float[3];
	private float[] geomag = new float[3];
	private float[] orientVals = new float[3];
	private double azimuth = 0;
	private double pitch = 0;
	private double roll = 0;
	private float adjustedHeading=0;
	private SensorManager sensorManager;
	private MapCord mapCord = new MapCord();
	private LocationListenerCopy locationListenerCopy = null;
	private LocationManager locationManager =null;
	private boolean runit=true;
	private boolean isGPSFix=false;
	private ProgressDialog progressDialog;
	private ArrayAdapter<String> arrayAdapter;
	private int shuntBoostLocal=20;
    private ObjMapData singleARObject;
	private String JSONFrag;
	private ArrayList<ObjMapData> JSONList;
	private int realWidth;
	private int realHeight;
    private Activity mActivity;

	public String getJSONFrag() {
		return JSONFrag;
	}

	public void setJSONFrag(String JSONFrag, int widthParm, int heightParm) {

        this.realWidth=widthParm;
        this.realHeight = heightParm;
		this.JSONFrag = JSONFrag;

		JSONArray mArray;
		JSONList = new ArrayList<ObjMapData>();
		try {
			mArray = new JSONArray(this.JSONFrag);

			for (int i = 0; i < mArray.length(); i++)
			{
				JSONObject mJsonObject = mArray.getJSONObject(i);
				ObjMapData tempObj = new ObjMapData();
				tempObj.setPlaceName(mJsonObject.getString("placename"));
				tempObj.setExtIconfile(mJsonObject.getString("imgurl"));
				String lat = mJsonObject.getString("lat");
				String lng = mJsonObject.getString("lng");
				tempObj.setPlaceCord(lat + "," + lng);
				tempObj.setPlaceName(mJsonObject.getString("placename"));

				JSONList.add(tempObj);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	final android.os.CountDownTimer arDelay = new android.os.CountDownTimer(1000, 1000) {
		public void onTick(long millisUntilFinished) {
			//do nothing
		}
		public void onFinish() {
			runit=true;
		}
	}; 


	public void cleanMemory()
	{
		try
		{
			handler.removeCallbacks(timedTask);
			handler= null;
			timedTask=null;
			layout= null;
			mapCord= null;
			sv= null;
			surfaceHolder.killAll();
			surfaceHolder=null;
			arrayAdapter= null;

		} catch (Exception e ) {

			Log.e("log_tag", "Error : " + e.toString());
		}
	}

	@Override
	public void onAttach(Activity activity) {

		super.onAttach(activity);

        mActivity = activity;
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.camerafrag, container, false);
		return view;
	}


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		progressDialog = ProgressDialog.show(getActivity(), "", "Waiting for location..");
		progressDialog.setCanceledOnTouchOutside(true);
		arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.select_dialog_singlechoice);
	}

	@Override
	public void onStart() {

        super.onStart();
	}

	public void resetViews()
	{
		try {

			final ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
			final android.net.NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity().getApplicationContext());

			if (resultCode == ConnectionResult.SUCCESS) {
				sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
				sensorManager.registerListener((SensorEventListener)  listener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
				sensorManager.registerListener((SensorEventListener) listener, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_UI);
				locationListenerCopy = new LocationListenerCopy();
				locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
				locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListenerCopy);
				locationManager.addGpsStatusListener(locationListenerCopy);

				boolean isGPS = locationManager.isProviderEnabled (LocationManager.GPS_PROVIDER);

				if(!isGPS)
				{
					showCloseAlert(getString(R.string.GPS_IS_OFF),getString(R.string.NO_GPS_SERVICE),true);
				}

			} else if (resultCode == ConnectionResult.SERVICE_MISSING ||
					resultCode == ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED ||
					resultCode == ConnectionResult.SERVICE_DISABLED) {

				showCloseAlert(getString(R.string.GOOGLE_SERVICES_UNAVAILABLE_TITLE),getString(R.string.GOOGLE_SERVICES_UNAVAILABLE),true);
			}

			sv = (SurfaceView)getActivity().findViewById(R.id.camerasurface);
			surfaceHolder = new CameraSurfaceHolder(this.getActivity(),thisActivity,sv);
			layout=(RelativeLayout) getActivity().findViewById(R.id.frame);

			handler.post(timedTask); 
			buildViews();
			layout.addView(new View(getActivity()));
			setLocation();

		} catch (Exception e ) {
			Log.e("log_tag", "Error : " + e.toString());
		}
	}

	public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {

		int width = bm.getWidth();
		int height = bm.getHeight();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
		return resizedBitmap;
	}

	public void addARImage()
	{
		Bitmap bit = null;
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		int height = displaymetrics.heightPixels;
		int width = displaymetrics.widthPixels;

		try {
			InputStream bitmap=getActivity().getAssets().open("iStock.jpg");
			bit=getResizedBitmap(BitmapFactory.decodeStream(bitmap),height,width);

		} catch (IOException e) {
			e.printStackTrace();
		}

		ImageView ar = (ImageView) getActivity().findViewById(R.id.ar);
		ar.setImageBitmap(bit);	
	}

	@Override
	public void onResume() {
		super.onResume();

		try {
			if((surfaceHolder==null)&&(locationListenerCopy==null))
			{
				resetViews();
			}
			else //register
			{
				try {

					sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
					sensorManager.registerListener((SensorEventListener)  listener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
					sensorManager.registerListener((SensorEventListener) listener, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_UI);
					locationListenerCopy = new LocationListenerCopy();
					locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
					locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListenerCopy);
					locationManager.addGpsStatusListener(locationListenerCopy);

				} catch (Exception e ) {
					Log.e("log_tag", "Error : " + e.toString());
				}
			}


			arDelay.start();			


		} catch (Exception e ) {
			Log.e("log_tag", "Error : " + e.toString());
		}
	}

	@Override
	public void onPause() {
		super.onPause();

		try {
			sensorManager.unregisterListener(listener);
			locationManager.removeGpsStatusListener(locationListenerCopy);
			locationManager.removeUpdates(locationListenerCopy);

		} catch (Exception e ) {
			Log.e("log_tag", "Error : " + e.toString());
		}
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroy() {

		super.onDestroy();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

		try {
			if(progressDialog!=null)
			{
				progressDialog.cancel();
			}

			sensorManager.unregisterListener(listener);
			locationManager.removeGpsStatusListener(locationListenerCopy);
			locationManager.removeUpdates(locationListenerCopy);
			locationListenerCopy=null;

		} catch (Exception e) {

			e.printStackTrace();
		}

		surfaceHolder=null;
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	private Runnable timedTask = new Runnable(){

		@Override
		public void run() 
		{
			runAR();
			handler.postDelayed(timedTask, ThreadARDuration);
		}
	};

	private void buildViews() 
	{
		int fontSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 18F, Resources.getSystem().getDisplayMetrics());
		final int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics());
		final int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getResources().getDisplayMetrics());
		final int widthImg = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 64, getResources().getDisplayMetrics());
		final int heightImg = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 64, getResources().getDisplayMetrics());
		final int rightSpacer = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics());
		final int midSpacer = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
		final int imgBorderSpacer = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics());

		switch(Resources.getSystem().getDisplayMetrics().densityDpi){
		case DisplayMetrics.DENSITY_LOW:
			shuntBoostLocal=15;
			break;
		case DisplayMetrics.DENSITY_MEDIUM:
			shuntBoostLocal=15;
			break;
		case DisplayMetrics.DENSITY_HIGH:
			shuntBoostLocal=15;
			break;
		case DisplayMetrics.DENSITY_XHIGH:
			shuntBoostLocal=35;
			break;
		case DisplayMetrics.DENSITY_XXHIGH:
			shuntBoostLocal=35;
			break;
        case DisplayMetrics.DENSITY_XXXHIGH:  //super high
             shuntBoostLocal=38;
             break;
		}


		for (ObjMapData obj : JSONList)
		{
			try {

				try {
					final ARViewSkin arViewSkin = new ARViewSkin(getActivity(), obj.getPlaceName(), obj.getExtIconfile(),
							fontSize, rightSpacer, midSpacer, imgBorderSpacer, widthImg, heightImg);
					arViewSkin.setBackgroundColor(getResources().getColor(android.R.color.transparent));
					arViewSkin.setLayoutParams(new LayoutParams(width, height));
					arViewSkin.setPlaceName(obj.getPlaceName());

					String[] r = obj.getPlaceCord().toString().split(",");
					arViewSkin.setLatitude(Double.parseDouble(r[1].toString()));
					arViewSkin.setLongitude(Double.parseDouble(r[0].toString()));

					new ImageDownloaderTask(arViewSkin).execute(obj.getExtIconfile());


				} catch (Exception e) {
					e.printStackTrace();
				}

			} catch (Exception e) {
				Log.e("log_tag", "Error : " + e.toString());
			}
		}
	}

	private Bitmap downloadBitmap(String url) {
		HttpURLConnection urlConnection = null;
		try {
			URL uri = new URL(url);
			urlConnection = (HttpURLConnection) uri.openConnection();
			int statusCode = urlConnection.getResponseCode();

			InputStream inputStream = urlConnection.getInputStream();
			if (inputStream != null) {
				Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
				return bitmap;
			}
		} catch (Exception e) {
			urlConnection.disconnect();
			Log.w("ImageDownloader", "Error downloading image from " + url);
		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
		}
		return null;
	}


	private double DegreesToRadians(double degrees)
	{
		return degrees * Math.PI / 180.0;
	}

	private double RadiansToDegrees(double radians)
	{
		return radians * 180.0/ Math.PI;
	}

	private double checkBearing(double lon, double lat)
	{
		double lat1 = DegreesToRadians(mapCord.getLatitude());
		double lon1 = DegreesToRadians(mapCord.getLongitude());
		double lat2 = DegreesToRadians(lat);
		double lon2 = DegreesToRadians(lon);
		double dLon = lon2 - lon1;
		double y = Math.sin(dLon) * Math.cos(lat2);
		double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) * Math.cos(dLon);
		double radiansBearing = Math.atan2(y, x);

		if(radiansBearing < 0.0)
			radiansBearing += 2* Math.PI;

		return  RadiansToDegrees(radiansBearing);
	}

	private SensorEventListener listener = new SensorEventListener() {

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {

		}

		@Override
		public void onSensorChanged(SensorEvent sensorEvent)
		{
			if (sensorEvent.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE)
				return;

			switch (sensorEvent.sensor.getType()) {
			case Sensor.TYPE_ACCELEROMETER:
				gravity = sensorEvent.values.clone();
				break;
			case Sensor.TYPE_MAGNETIC_FIELD:
				geomag = sensorEvent.values.clone();
				break;
			}

			if (gravity != null && geomag != null)
			{
				boolean success = SensorManager.getRotationMatrix(inR, I, gravity, geomag);

				if (success)
				{
					SensorManager.getOrientation(inR, orientVals);
					azimuth = Math.toDegrees(orientVals[0]);
					pitch = Math.toDegrees(orientVals[1]);
					roll = Math.toDegrees(orientVals[2]);
					azimuth = (azimuth + 360.0) % 360.0;
					pitch = (pitch + 360.0) % 360.0;
					roll = (roll + 360.0) % 360.0;
					adjustedHeading = (float) (azimuth);
					adjustedHeading+=90; //need to change

					if(runit)
					{
						runit=false;
						arDelay.start();
						if(sensorEvent.values[2]>0)
						{
							accelVal=sensorEvent.values[2];
						}
						compassHeading=adjustedHeading;
					}
				}
				else
				{

				}
			}
		}
	};

	private void runAR()
	{
		try {

			int spacer1=0;
			int spacer2=spacer1;
			int spacer3=spacer1;
			int lowerAdjustedOne=0;
			int lowerAdjustedTwo=0;
			int addFactor=0;
			float leftAdjustment=0;
			float rightAdjustment=0;
			float adjustedHeading=0;
			boolean runSplit = false;

			adjustedHeading=compassHeading;

			if(adjustedHeading>360)
			{
				adjustedHeading-=360;
			}

			for (int i = 0; i < layout.getChildCount(); i++)
			{
				final View v = layout.getChildAt(i);

				if(v instanceof ARViewSkin)
				{
					if((!hideAR)
							&&(isGPSFix))
						//&&(((ARViewSkin) v).getPlaceNameLong().contains("Black Mountain")))
					{


						double destBearing = checkBearing(((ARViewSkin) v).getLatitude(),((ARViewSkin) v).getLongitude());
						double upper = destBearing + DEGREE_RANGE;
						double lower = destBearing - DEGREE_RANGE;
						final Point positionSplit = new Point();
						double diff=adjustedHeading-destBearing;

						for (ObjMapData s : JSONList)
						{
							try
							{
								if (((ARViewSkin) v).getPlaceNameLong().equalsIgnoreCase(s.getPlaceName()))
								{
									((ARViewSkin) v).setDistance(s.getDistanceTo());
								}
							} catch (Exception e) {
								Log.e("log_tag", "Error : " + e.toString());
							}
						}

						if(lower < 0)
						{
							lowerAdjustedOne=(int) (lower+360);
							lowerAdjustedTwo=(int) upper;
							runSplit=true;
						}
						else if (upper > 360)
						{
							lowerAdjustedTwo=(int) (upper-360);
							lowerAdjustedOne=(int) lower;
							runSplit=true;
						}
						else
						{
							runSplit=false;
						}

						if(runSplit)
						{
							if (((adjustedHeading>=lowerAdjustedOne)&&(adjustedHeading<=northSplitOne)) ||
									((adjustedHeading>=northSplitTwo)&&(adjustedHeading<=lowerAdjustedTwo)) )
							{
								if((adjustedHeading>=lowerAdjustedOne)&&(adjustedHeading<=northSplitOne))
								{
									addFactor=(int) (360-(adjustedHeading-destBearing));
									if(addFactor>=360-DEGREE_RANGE)
										addFactor=addFactor-360;
									if(addFactor<=-360-DEGREE_RANGE)
										addFactor=addFactor+360;
									leftAdjustment = ((realWidth/2)+(addFactor*shuntBoostLocal));
									positionSplit.x=(int) leftAdjustment;
									positionSplit.y=(int) (spacer1- (this.accelVal*-accelXvalMult));
									spacer1+=STACK_SPACE;
								}
								else
								{
									addFactor=(int) (360+(adjustedHeading-destBearing));
									if(addFactor>=360-DEGREE_RANGE)
										addFactor=addFactor-360;
									if(addFactor<=-360-DEGREE_RANGE)
										addFactor=addFactor+360;
									rightAdjustment = ((realWidth/2)-(addFactor*shuntBoostLocal));
									positionSplit.x=(int) rightAdjustment;
									positionSplit.y=(int) (spacer2- (this.accelVal*-accelXvalMult));
									spacer2+=STACK_SPACE;
								}

								if(((ARViewSkin) v).isFinished())
								{
									((ARViewSkin) v).setFinished(false); //lock it down
									v.setVisibility(View.VISIBLE);
									final Point position = new Point();
									Random r = new Random();
									final int rand = r.nextInt(maxRand - minRand + 1) + minRand;

									position.x=positionSplit.x;
									position.y=positionSplit.y;

									ObjectAnimator animX = ObjectAnimator.ofFloat(((ARViewSkin) v), "x", position.x + rand - (((ARViewSkin) v).getImgHeightParm() / 2));
									ObjectAnimator animY = ObjectAnimator.ofFloat(((ARViewSkin) v), "y", position.y + rand);
									ObjectAnimator rotation;

									if(diff>-DEGREE_RANGE && diff<DEGREE_RANGE)
									{
										rotation = ObjectAnimator.ofFloat(((ARViewSkin) v), "rotationY", (float) diff);
									}
									else
									{
										rotation = ObjectAnimator.ofFloat(((ARViewSkin) v), "rotationY", 0);
									}

									AnimatorSet animSetXY = new AnimatorSet();
									animSetXY.playTogether(animX, animY, rotation);
									animSetXY.start();
									animSetXY.setDuration(ANIMATION_DURATION);
									animSetXY.addListener(new AnimatorListener() {

										@Override
										public void onAnimationCancel(
												Animator animation) {
											// TODO Auto-generated method stub
										}

										@Override
										public void onAnimationEnd(
												Animator animation) {

											((ARViewSkin) v).setFinished(true);
										}

										@Override
										public void onAnimationRepeat(
												Animator animation) {
										}

										@Override
										public void onAnimationStart(
												Animator animation) {
										}
									});
								}
								else
								{

								}
							}
							else
							{
								v.clearAnimation();
								((ARViewSkin) v).setFinished(true);
								v.setVisibility(View.GONE);
							}
						}
						else //dont run split
						{
							if ((adjustedHeading>=lower)&&(adjustedHeading<=upper))//&&(((ARViewSkin) v).getPlaceName().equalsIgnoreCase("Titanic")))  //main check..outside zero fold
							{
								if(((ARViewSkin) v).isFinished()) //is it finished
								{
									((ARViewSkin) v).setFinished(false); //lock it down
									v.setVisibility(View.VISIBLE);
									final Point position = new Point();
									Random r = new Random();
									final int rand = r.nextInt(maxRand - minRand + 1) + minRand;

									position.x=(int) ((realWidth/2)+(destBearing-adjustedHeading)*shuntBoostLocal);
									position.y=(int) (spacer3- (this.accelVal*-accelXvalMult));
									spacer3+=STACK_SPACE;

									ObjectAnimator animX = ObjectAnimator.ofFloat(((ARViewSkin) v), "x", position.x + rand - (((ARViewSkin) v).getImgHeightParm() / 2));
									ObjectAnimator animY = ObjectAnimator.ofFloat(((ARViewSkin) v), "y", position.y + rand);
									ObjectAnimator rotation;

									if(diff>-DEGREE_RANGE && diff<DEGREE_RANGE)
									{
										rotation = ObjectAnimator.ofFloat(((ARViewSkin) v), "rotationY", (float) diff);
									}
									else
									{
										rotation = ObjectAnimator.ofFloat(((ARViewSkin) v), "rotationY", 0);
									}

									AnimatorSet animSetXY = new AnimatorSet();
									animSetXY.playTogether(animX, animY, rotation);
									animSetXY.start();
									animSetXY.setDuration(ANIMATION_DURATION);
									animSetXY.addListener(new AnimatorListener() {

										@Override
										public void onAnimationCancel(
												Animator animation) {
											// TODO Auto-generated method stub

										}

										@Override
										public void onAnimationEnd(
												Animator animation) {

											((ARViewSkin) v).setFinished(true);
										}

										@Override
										public void onAnimationRepeat(
												Animator animation) {
										}

										@Override
										public void onAnimationStart(
												Animator animation) {
										}
									});
								}
							}
							else
							{
								v.clearAnimation();
								((ARViewSkin) v).setFinished(true);
								v.setVisibility(View.GONE);
							}
						}
					}
					else
					{
						v.clearAnimation();
						((ARViewSkin) v).setFinished(true);
						v.setVisibility(View.GONE);
					}
				}
				else
				{
					//catid remover
				}
			}
		} catch (Exception e ) {
			Log.e("log_tag", "Error : " + e.toString());
		}
	}


	private void runToast(String message)
	{
 //toast crashing UI
	}


	private void checkPointDistances()
	{
		try
		{
			try {
				String[] r;
				MapCord markerLoc = new MapCord();

				for (ObjMapData obj : JSONList) {

					r = obj.getPlaceCord().toString().split(",");
					markerLoc.setLatitude(Double.parseDouble(r[0].toString())); //reversed coordinates
					markerLoc.setLongitude(Double.parseDouble(r[1].toString()));

					Location currentLoc = new Location("");
					currentLoc.setLatitude(mapCord.getLatitude());
					currentLoc.setLongitude(mapCord.getLongitude());

					Location destLoc = new Location("");
					destLoc.setLatitude(markerLoc.getLatitude());
					destLoc.setLongitude(markerLoc.getLongitude());

					float distance = currentLoc.distanceTo(destLoc);

					DecimalFormat df = new DecimalFormat(DECIMAL_FORMAT); //km
					updateARSkinDistances(obj.getPlaceName(), Float.parseFloat(df.format(distance / MAP_METERS_IN_KM))); //update views
				}
			} catch (Exception e) {
				Log.e("log_tag", "Error :8 " + e.toString());
			}

		} catch (Exception e ) {
			Log.e("log_tag", "Error :9 " + e.toString());
		}

	}

	private void updateARSkinDistances(String dis, float dist)
	{
		for (int i = 0; i < layout.getChildCount(); i++)
		{
			final View v = layout.getChildAt(i);

			if(v instanceof ARViewSkin)
			{
				if(((ARViewSkin) v).getPlaceNameLong().equalsIgnoreCase(dis))
				{
					((ARViewSkin) v).setDistance(dist);
					v.invalidate();
				}
			}
		}
	}

	private void showCloseAlert(String title,String message, final boolean closeDown)
	{
		try {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setMessage(message)
			.setTitle(title)
			.setCancelable(false)
			.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {

					if(closeDown)
						System.exit(0);
				}
			});
			AlertDialog alert = builder.create();
			alert.show();

		} catch (Exception e ) {
			Log.e("log_tag", "Error : " + e.toString());
		}
	}


	private void setLocation()
	{
		// Get the location manager
		LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		String bestProvider = locationManager.getBestProvider(criteria, false);
		Location location = locationManager.getLastKnownLocation(bestProvider);

		try {
			mapCord.setLatitude(location.getLatitude ());
			mapCord.setLongitude(location.getLongitude ());
		}
		catch (NullPointerException e){

			mapCord.setLatitude(-1.0);
			mapCord.setLongitude(-1.0);
		}
	}

	private class LocationListenerCopy implements LocationListener, android.location.GpsStatus.Listener{

		public void onLocationChanged(Location argLocation)
		{
            if(isAdded ()) {
                try {
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();

                    if (argLocation == null) return;

                    isGPSFix = true;

                    if (mapCord == null) {
                        mapCord = new MapCord();
                    }

					mapCord.setLatitude((argLocation.getLatitude()));
					mapCord.setLongitude((argLocation.getLongitude()));


                    checkPointDistances();
                   // loopThroughPolygons();

                } catch (Exception e) {
                    Log.e("log_tag", "Error : " + e.toString());
                }
            }
		}

		public void onProviderDisabled(String provider) {

			if(provider.contains(getString(R.string.GPS))){
				showCloseAlert(getString(R.string.GPS_IS_OFF),getString(R.string.NO_GPS_SERVICE),true);
			}
		}

		public void onProviderEnabled(String provider) {

		}

		public void onStatusChanged(String provider, int status, Bundle extras) {

			switch (status) {
			case LocationProvider.OUT_OF_SERVICE:

				//showRedDot();
				runToast(getString(R.string.GPS_STATUS_UNAVAILABLE));
				break;
			case LocationProvider.TEMPORARILY_UNAVAILABLE:

				//showRedDot();
				runToast(getString(R.string.GPS_STATUS_UNAVAILABLE));
				break;
			case LocationProvider.AVAILABLE:

				//showGreenDot();
				runToast(getString(R.string.GPS_STATUS_LOCATED));
				break;
			}
		}

		@Override
		public void onGpsStatusChanged(int event) {

			switch (event) {
			case GpsStatus.GPS_EVENT_STARTED:

				//showRedDot();
				runToast(getString(R.string.GPS_STATUS_UNAVAILABLE));
				break;

			case GpsStatus.GPS_EVENT_STOPPED:

				//showRedDot();
				runToast(getString(R.string.GPS_STATUS_UNAVAILABLE));
				break;

			case GpsStatus.GPS_EVENT_FIRST_FIX:

				//showGreenDot();
				runToast(getString(R.string.GPS_STATUS_LOCATED));
				break;

			case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
				// Do Something with mStatus info
				break;
			}	
		}
	}

	class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {
		private final WeakReference<ARViewSkin> imageViewReference;
		private final int imgHeightParm = 100;

		public ImageDownloaderTask(ARViewSkin imageView) {
			imageViewReference = new WeakReference<ARViewSkin>(imageView);
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			return downloadBitmap(params[0]);
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if (isCancelled()) {
				bitmap = null;
			}

			if (imageViewReference != null) {
				ARViewSkin imageView = imageViewReference.get();
				if (imageView != null)
				{
					if (bitmap != null)
					{
						int width = bitmap.getWidth();
						int height = bitmap.getHeight();
						float scaleWidth = ((float) imgHeightParm) / width;
						float scaleHeight = ((float) imgHeightParm) / height;
						Matrix matrix = new Matrix();
						matrix.postScale(scaleWidth, scaleHeight);
						Bitmap resizedBitmap = Bitmap.createBitmap(
								bitmap, 0, 0, width, height, matrix, false);
						bitmap.recycle();
						imageView.onMeasureCustom(resizedBitmap);
						RelativeLayout layout=(RelativeLayout) getActivity().findViewById(R.id.frame);
						layout.addView(imageView);
					}
				}
			}
		}
	}
}
