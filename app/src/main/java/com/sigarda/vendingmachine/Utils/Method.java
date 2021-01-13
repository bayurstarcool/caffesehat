package com.sigarda.vendingmachine.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


import com.sigarda.vendingmachine.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Method {

    private Activity activity;
    public static boolean share = false, onBackPress = false, loginBack = false, allowPermitionExternalStorage = false, personalization_ad = false;
    public static boolean category = false, categoryRecyclerView = false, event_delete = false, location_app = false, map_type = false;
    public static Bitmap mbitmap;


    public SharedPreferences pref;
    public SharedPreferences.Editor editor;
    private final String myPreference = "login";
    public String pref_login = "pref_login";
    private String firstTime = "firstTime";
    public String userId = "userId";
    public String profileId = "profileId";
    public String userEmail = "userEmail";
    public String userPassword = "userPassword";
    public String userName = "userName";
    public String first_name = "first_name";
    public String last_name = "last_name";
    public String fullname = "fullname";
    public String cuurentLocation = "currentLocation";
    public String userImage = "user_image";
    public String token = "token";
    public String access_token = "access_token";
    public String notificationCount = "notificationCount";
    public String fcm_token ="fcm_token";

    private static Typeface scriptable;
    public static Bitmap resizeBitmap(Bitmap source, int maxLength) {
        try {
            if (source.getHeight() >= source.getWidth()) {
                int targetHeight = maxLength;
                if (source.getHeight() <= targetHeight) { // if image already smaller than the required height
                    return source;
                }

                double aspectRatio = (double) source.getWidth() / (double) source.getHeight();
                int targetWidth = (int) (targetHeight * aspectRatio);

                Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
                if (result != source) {
                }
                return result;
            } else {
                int targetWidth = maxLength;

                if (source.getWidth() <= targetWidth) { // if image already smaller than the required height
                    return source;
                }

                double aspectRatio = ((double) source.getHeight()) / ((double) source.getWidth());
                int targetHeight = (int) (targetWidth * aspectRatio);

                Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
                if (result != source) {
                }
                return result;

            }
        }
        catch (Exception e)
        {
            return source;
        }
    }
    public static String getURLForResource (int resourceId) {
        //use BuildConfig.APPLICATION_ID instead of R.class.getPackage().getName() if both are not same
        return Uri.parse("android.resource://"+R.class.getPackage().getName()+"/" +resourceId).toString();
    }
    public static String formatValue(float value) {
        String arr[] = {"", "K", "M", "B", "T", "P", "E"};
        int index = 0;
        while ((value / 1000) >= 1) {
            value = value / 1000;
            index++;
        }
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return String.format("%s %s", decimalFormat.format(value),arr[index]);
    }
    public Method(Activity activity) {
        this.activity = activity;
        pref = activity.getSharedPreferences(myPreference, 0); // 0 - for private mode
        editor = pref.edit();
        scriptable = Typeface.createFromAsset(activity.getAssets(), "fonts/montserrat_regular.ttf");
    }

    public String getDistance(double lat1, double lon1, double lat2, double lon2) {
        double earthRadius = 3958.75;
        double latDiff = Math.toRadians(lat1 - lat2);
        double lngDiff = Math.toRadians(lon1 - lon2);
        double a = Math.sin(latDiff / 2) * Math.sin(latDiff / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(lngDiff / 2)
                * Math.sin(lngDiff / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = earthRadius * c;

        int meterConversion = 1609;
        double kmConvertion = 1.6093;
        // return new Float(distance * meterConversion).floatValue();
        return String.format("%.1f", new Float(distance * kmConvertion).floatValue()) + " km";
        // return String.format("%.2f", distance)+" m";
    }
    public void login() {
        if (!pref.getBoolean(firstTime, false)) {
            editor.putBoolean(pref_login, false);
            editor.putBoolean(firstTime, true);
            editor.commit();
        }
    }



    //google map application installation or not check
    public static boolean isAppInstalled(Activity activity) {
        String packageName = "com.google.android.apps.maps";
        Intent mIntent = activity.getPackageManager().getLaunchIntentForPackage(packageName);
        if (mIntent != null) {
            return true;
        } else {
            return false;
        }
    }


    //network check
    public static boolean isNetworkAvailable(Activity activity) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public int getScreenWidth() {
        int columnWidth;
        WindowManager wm = (WindowManager) activity
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();


        final Point point = new Point();
        Point outPoint = new Point();
        if (Build.VERSION.SDK_INT >= 19) {
            // include navigation bar
            display.getRealSize(outPoint);
        } else {
            // exclude navigation bar
            display.getSize(outPoint);
        }
        if (outPoint.y > outPoint.x) {
            point.y = outPoint.y;
            point.x = outPoint.x;
        } else {
            point.x = outPoint.x;
            point.y = outPoint.y;
        }
        columnWidth = point.x;
        return columnWidth;
    }

    /*<---------------------- share & downlode and methodes ---------------------->*/

    public void share_save(String image, String eventName, String location, String date, String number, String email, String web, String detail) {
        new DownloadImageTask().execute(image, eventName, location, date, number, email, web, detail);
    }

    @SuppressLint("StaticFieldLeak")
    public class DownloadImageTask extends AsyncTask<String, String, String> {

        String eventName, location, date, number, email, web, detail;

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                eventName = params[1];
                location = params[2];
                date = params[3];
                number = params[4];
                email = params[5];
                web = params[6];
                detail = params[7];
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                mbitmap = BitmapFactory.decodeStream(input);
            } catch (IOException e) {
                // Log exception
                return null;
            }
            return null;
        }

    }



}