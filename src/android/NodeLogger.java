
package com.geteventro.plugin;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class NodeLogger extends CordovaPlugin {

    JSONArray jsonArray;


    private boolean isMyServiceRunning(Class<?> serviceClass) {

        Context context=this.cordova.getActivity().getApplicationContext();

        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    @Override
    public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {

        if (action.equals("nodeloggerview")) {

            String jsonString = data.getString(0);
            jsonArray = new JSONArray(jsonString);
            Log.d("ARview Information:", jsonString);
            String message = "JsonString is, " + jsonString;
            callbackContext.success(message);

            String deviceID="";
            String setting="";

            boolean shareGPS = false;

            try {

                    JSONObject mJsonObject = new JSONObject(jsonString.replaceAll("\\[|\\]", ""));

                    deviceID = mJsonObject.getString("deviceID");
                    setting = mJsonObject.getString("setting");


                shareGPS = Boolean.parseBoolean(setting);


            } catch (JSONException e) {
                e.printStackTrace();
            }

            //start service

            if(shareGPS)
            {
                if(!isMyServiceRunning(LocService.class))
                {
                    Intent myIntent = new Intent(this.cordova.getActivity(), LocService.class);
                    myIntent.putExtra("deviceID",deviceID);
                    myIntent.putExtra("setting",setting);
                    this.cordova.getActivity().startService(myIntent);
                }
            }
            else
            {
                this.cordova.getActivity().stopService(new Intent(this.cordova.getActivity(), LocService.class));
            }



            return true;

        } else {
            
            return false;

        }
    }
}