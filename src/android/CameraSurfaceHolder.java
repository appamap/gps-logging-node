package com.geteventro.plugin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.geteventro.app.R;
import java.io.IOException;
import java.util.List;


public class CameraSurfaceHolder extends SurfaceView implements Constants,  SurfaceHolder.Callback, Camera.AutoFocusCallback {

	SurfaceHolder holder;
	boolean running = false;
	private Camera camera=null;
	private Activity thisActivity;

	public CameraSurfaceHolder(Context context) {
		super(context);
	}

	public CameraSurfaceHolder(Context context, Activity activity, SurfaceView sh) {
		super(context);

		thisActivity = activity;
		holder = sh.getHolder();
		this.setZOrderMediaOverlay(true);
		holder.addCallback(this);
	}

	public void killAll()
	{
		try
		{
			if(holder!=null)
			{
				holder.removeCallback(this);
				holder=null;
			}

			if(camera!=null)
			{
				camera.stopPreview();
				camera.release();
				camera = null;	
			}

		} catch (Exception e ) {
			Log.e("log_tag", "Error : " + e.toString());
		}
	}

	private void showCloseAlert(String title,String message, final boolean closeDown)
	{
		try {
			AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity);
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


	@Override
	public void surfaceCreated(SurfaceHolder holder) {

		if(camera==null)
		{
			try
			{
				this.camera = Camera.open();

				if(this.camera==null)
				{
					showCloseAlert(getResources().getString(R.string.NO_BACKCAMERA_TITLE),getResources().getString(R.string.NO_BACKCAMERA),true);
				}
				else
				{
					this.camera.setPreviewDisplay(holder); 
				}
			}
			catch(IOException ioe)
			{
				ioe.printStackTrace(System.out);
			} 
		}	
	}


	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

		Camera.Parameters parameters = null;
		try {

			if(camera!=null)
			{
				parameters = camera.getParameters();
				List<String> focusModes = camera.getParameters().getSupportedFocusModes();

				try 
				{	
					if (focusModes != null && focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO))
					{
						try 
						{
							parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
							camera.autoFocus(this);
							
						} catch (Exception e ) {
							Log.e("log_tag", "Error : " + e.toString());  //can't focus camera
						}
					}

					else if (focusModes != null && focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE))
					{
						try 
						{
							parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
							camera.autoFocus(this);
							
						} catch (Exception e ) {
							Log.e("log_tag", "Error : " + e.toString());  //can't focus camera
						}
					}
				    else if(focusModes != null && focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO))
					{
				    	try 
						{
							parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
							camera.autoFocus(this);
							
						} catch (Exception e ) {
							Log.e("log_tag", "Error : " + e.toString());  //can't focus camera
						}
					}
			
				} catch (Exception e ) {
					Log.e("log_tag", "Error : " + e.toString());  //can't focus camera
				}

				parameters.setPreviewSize(width, height);
				camera.startPreview();
			}
			
		} catch (Exception e ) {
			Log.e("log_tag", "Error : " + e.toString());
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {

		try {
			if (camera != null) 
			{
				camera.stopPreview();
				camera.release();
				camera = null;
			}

		} catch (Exception e ) {
			Log.e("log_tag", "Error : " + e.toString());
		}
	}

	@Override
	public void onAutoFocus(boolean success, Camera camera) {

	}
}