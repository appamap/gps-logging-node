
package com.geteventro.plugin;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;


public class ARcode extends CordovaPlugin {

    JSONArray jsonArray;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {  //rotation changes
        super.onConfigurationChanged(newConfig);

        Context context=this.cordova.getActivity().getApplicationContext();


        
        
        
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //Toast.makeText(context, "landscape", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(context, CamFragActivity.class);
            intent.putExtra("JData",jsonArray.toString());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){

            //Toast.makeText(context, "Portrait", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {

        if (action.equals("arcodeview")) {

            String jsonString = data.getString(0);
            jsonArray = new JSONArray(jsonString);
            Log.d("ARview Information:", jsonString);
            String message = "JsonString is, " + jsonString;
            callbackContext.success(message);

            return true;

        } else {
            
            return false;

        }
    }
}