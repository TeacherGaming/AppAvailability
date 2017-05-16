package com.ohh2ahh.appavailability;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.content.pm.PackageManager;

public class AppAvailability extends CordovaPlugin {
    @Override
    public boolean execute(final String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {
        if(action.equals("checkAvailability")) {
            final AppAvailability classInstance = this;
            // Run in thread
            cordova.getThreadPool().execute(new Runnable() {
                public void run() {
                    String uri = args.getString(0);
                    try {
                        classInstance.checkAvailability(uri, callbackContext);
                        callbackContext.success(); // Thread-safe.
                    }
                    catch (JSONException e) { 
                        e.printStackTrace();
                        callbackContext.error(""); // Thread-safe.
                    }
                }
            });
        }
    }
    
    // Thanks to http://floresosvaldo.com/android-cordova-plugin-checking-if-an-app-exists
    public boolean appInstalled(String uri) {
        Context ctx = this.cordova.getActivity().getApplicationContext();
        final PackageManager pm = ctx.getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }
        catch(PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }
    
    private void checkAvailability(String uri, CallbackContext callbackContext) {
        if(appInstalled(uri)) {
            callbackContext.success();
        }
        else {
            callbackContext.error("");
        }
    }
}
