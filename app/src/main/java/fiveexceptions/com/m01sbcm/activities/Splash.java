package fiveexceptions.com.m01sbcm.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.Locale;

import fiveexceptions.com.m01sbcm.R;
import fiveexceptions.com.m01sbcm.database.DataBaseHelper;
import fiveexceptions.com.m01sbcm.database.DataBaseManager;
import fiveexceptions.com.m01sbcm.model.User;
import fiveexceptions.com.m01sbcm.sync.SyncUtil;
import fiveexceptions.com.m01sbcm.sync.VolleySingleton;
import fiveexceptions.com.m01sbcm.utility.Constants;
import fiveexceptions.com.m01sbcm.utility.Utility;


public class Splash extends AppCompatActivity implements SyncInterface{

    // sync process
    ProgressDialog pDialog = null;
    RequestQueue requestQueue;
    SyncUtil syncUtil;
    Locale myLocale;


    private static int SPLASH_TIME_OUT = 2000;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalash);
        //setLocale("prr");
        syncUtil = new SyncUtil(this, this);
        alertDialog = new AlertDialog.Builder(Splash.this).create();

        // create volley request queue
        requestQueue = VolleySingleton.getsInstance().getmRequestQueue();
        // constant must be set before performing any operation
        setConstantValue();


        // copy database from asset folder to device internal storage
        final DataBaseHelper dataBaseHelper = new DataBaseHelper(getBaseContext());
        dataBaseHelper.createDataBaseIfNotExist();

        // create image folder to store the images for profile, news and events
        // Utility.createImageDir(getBaseContext());


        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity

                DataBaseManager dataBaseManager = new DataBaseManager();
                User u = dataBaseManager.getUser(Splash.this);

                if (u == null)
                {
                    Intent i = new Intent(Splash.this, Login.class);
                    startActivity(i);
                    finish();

                } else {

                    Utility.setuser(u);

                    //check network connection
                    //if network is connected then call API  else show message
                    if (Utility.isNetworkAvailable(Splash.this)) {

                        // sync call by Vijesh
                        // device to server and server to device
                        syncDeviceToServer();
                        // syncServerToDevice();

                    } else {

                        Intent i = new Intent(Splash.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }

                }

                // close this activity
            }
        }, SPLASH_TIME_OUT);


    }

    private void setConstantValue() {
        // DB_PATH=cw.getFilesDir().getAbsolutePath()+ "/databases/"; //edited to databases
        // DB_PATH=  context.getFilesDir().getAbsolutePath() + "/databases/"; //edited to databases
        Constants.DB_PATH = this.getFilesDir().getAbsolutePath();

        Log.d("Volley cache size=", Runtime.getRuntime().maxMemory() / 1024 / 8 + " MB");

        /*
        Constants.PROFILE_IMG_DIR_PATH = this.getExternalFilesDir(null)  + "/profile/";
        Constants.EVENT_IMG_DIR_PATH = this.getExternalFilesDir(null)  + "/event/";
        Constants.NEWS_IMG_DIR_PATH = this.getExternalFilesDir(null)  + "/news/";
        */
    }

    private void syncServerToDevice() {

        pDialog = new ProgressDialog(Splash.this);
        pDialog.setMessage("Pls wait...");
        pDialog.setCancelable(false);
        pDialog.show(); // show dialog

        String serverSyncTime = Utility.loadPreferences("last_sync_time");
        String serverSyncTimeEncode = "";

        try {
            serverSyncTimeEncode = URLEncoder.encode(serverSyncTime, "UTF-8");
        } catch (Exception e) {
            Log.e("Error", "encoding server sync time");
        }

        Log.d("ServerToDevice", "Sync time-" + serverSyncTime);

        String url = Constants.URL_SERVER_TO_DEVICE + "&last_sync_time=" + serverSyncTimeEncode;

        Log.d("ServerToDevice", "final url -" + url);

        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("TESTING", "json  response" + response.toString());
                syncUtil.parseServerToDeviceJSONResponse(response);

                // download thumbnail from server
                // downloadNewsThumbnailImage();
                // downloadEventThumbnailImage();
                // downloadProfileThumbnailImage();


                if (pDialog.isShowing()) {
                    pDialog.dismiss(); //dismiss the progress dialog box
                }
                // thumbnail
                // delete expired records
                /*
                DataBaseManager dataBaseManager = new DataBaseManager();
                final DataBaseHelper dataBaseHelper = new DataBaseHelper(getBaseContext());
                dataBaseManager.deleteExpiredDataEvent(dataBaseHelper);
                dataBaseManager.deleteExpiredDataNews(dataBaseHelper);
                dataBaseManager.deleteExpiredDataProfile(dataBaseHelper);
                */


                Intent I = new Intent(Splash.this, MainActivity.class);
                startActivity(I);
                finish();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TESTING", "error  response" + error.getMessage());
                // Log.d("TESTING", "json error  response--" + error.getMessage());
                if (pDialog != null &&  pDialog.isShowing()) {
                    pDialog.dismiss(); //dismiss the progress dialog box
                }
                // showDialogMsg(error.getMessage());
                // showDialogMsg("Error : Unable to connect with server.");
                Intent i = new Intent(Splash.this, MainActivity.class);
                startActivity(i);
                finish();

            }
        });

        int socketTimeout = 30000;   //30 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jor.setRetryPolicy(policy);

        requestQueue.add(jor);

    }//syncServerToDevice()

    private void syncDeviceToServer() {

        pDialog = new ProgressDialog(Splash.this);
        pDialog.setMessage("Pls wait...");
        pDialog.setCancelable(false);
        pDialog.show(); // show dialog

        JSONObject jResult = syncUtil.getDeviceData();

        Log.d("DToS", "" + jResult.toString());
        // Log.d("DToS",""+jResult.toString());

        Log.d("TESTING", "json  param list" + jResult.toString());
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, Constants.URL_DEVICE_TO_SERVER, jResult.toString(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("TESTING", "json  response " + response.toString());
                syncUtil.parseDeviceToServerJSONResponse(response);
                Log.d("TESTING", "json  response " + response.toString());
                if (pDialog.isShowing()) {
                    pDialog.dismiss(); //dismiss the progress dialog box
                }

                if (Utility.isNetworkAvailable(Splash.this)) {
                    // sync server to device
                    syncServerToDevice();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TESTING", "json error  response--" + error.getMessage());
                if (pDialog.isShowing()) {
                    pDialog.dismiss(); //dismiss the progress dialog box
                }
                // showDialogMsg(error.getMessage());
                // showDialogMsg("Error : Unable to connect with server.");

                Intent i = new Intent(Splash.this, MainActivity.class);
                startActivity(i);
                finish();

            }
        });

        int socketTimeout = 15000;   //15 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jor.setRetryPolicy(policy);

        requestQueue.add(jor);


    }//syncDeviceToServer()

    public void showDialogMsg(String msg) {

        // Setting Dialog Title
        alertDialog.setTitle("Error");

        // Setting Dialog Message
        alertDialog.setMessage(msg);

        // Setting Icon to Dialog
        // alertDialog.setIcon(R.drawable.icon);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
                // Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
            }
        });

        alertDialog.show();
    }

    @Override
    public void syncResult(boolean result, int responseCode, String msg) {

        if(result == false){
            showDialogMsg(msg);
        }

    }


    /*
    public void setLocale(String lang) {

        myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Log.d("TAG", "setLocale: ");
    }
    */



}

/*

Ad mob id : <string name="banner_ad_unit_id">ca-app-pub-3940256099942544/6300978111</string>

 */