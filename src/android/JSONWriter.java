package com.geteventro.plugin;

import android.util.Log;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class JSONWriter implements Constants {

	private static final String TAG = "GPSLogging";
	

public static void serverGPSLoggerObj(String deviceIDParm, String latParm, String lngParm)
{
	myThread obj = new myThread(deviceIDParm, latParm, lngParm );
	obj.start();
}

	private static class myThread extends Thread{

		String deviceIDParm, latParm, lngParm  ;
		public myThread(String deviceIDParm, String latParm, String lngParm ) {
			this.deviceIDParm = deviceIDParm;
			this.latParm = latParm;
			this.lngParm = lngParm;
		}
		@Override
		public void run() {

				String request = Constants.AWSConnectionString; //ip endpoint for AWS

			try
			{
				URL url = new URL(request);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setDoOutput(true);
				connection.setDoInput(true);
				connection.setInstanceFollowRedirects(false);
				connection.setRequestMethod("POST");
				connection.setRequestProperty("Content-Type", "application/json");
				connection.setRequestProperty("Accept", "application/json");
				connection.setUseCaches (false);

				DataOutputStream wr = new DataOutputStream(connection.getOutputStream ());
				JSONObject jsonParam = new JSONObject();
				jsonParam.put("lat", this.latParm);
				jsonParam.put("lng", this.lngParm);
				jsonParam.put("deviceid", this.deviceIDParm);

				wr.writeBytes(jsonParam.toString());

				InputStream in = new BufferedInputStream(connection.getInputStream());
				Log.d(TAG, "Output Ststus -   " + convertStreamToString(in));

				wr.flush();
				wr.close();

			} catch (Exception ex)
			{
				Log.d(TAG, "Error Report  " + ex);
			}
		}
	}

	static String convertStreamToString(java.io.InputStream is) {
		try {
			return new java.util.Scanner(is).useDelimiter("\\A").next();
		} catch (java.util.NoSuchElementException e) {
			return "";
		}
	}
}
