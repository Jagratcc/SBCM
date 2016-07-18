package fiveexceptions.com.m01sbcm.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

import fiveexceptions.com.m01sbcm.R;
import fiveexceptions.com.m01sbcm.database.DataBaseManager;
import fiveexceptions.com.m01sbcm.model.User;
import fiveexceptions.com.m01sbcm.sync.SyncUtil;
import fiveexceptions.com.m01sbcm.sync.VolleySingleton;
import fiveexceptions.com.m01sbcm.sync.model.Event;
import fiveexceptions.com.m01sbcm.utility.Constants;
import fiveexceptions.com.m01sbcm.utility.Utility;


/*
https://www.captechconsulting.com/blogs/android-volley-library-tutorial
*/

public class Passcode extends AppCompatActivity implements SyncInterface{

    TextView passcodeView;
    String userName, pwd, mobileNo, email, displayName;
    int socialMediaId;
    String passcodeVal;

    // sync process
    ProgressDialog pDialog = null;
    RequestQueue requestQueue;
    SyncUtil syncUtil;

    Event event = null;

    /////////////////////////popupWindow//////////////////////////////////
    // private PopupWindow pw;

    String responseCode, message, serverSyncTime = null;
    // SQLController db = null;
    // Button serverToDevice, DeviceToServer, login, registration, image;
    boolean registationSuccess = false;

    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passcode);

        // method call from 3 places : FB Login, Gmail and custom logic button
        Intent intent = getIntent();
        userName = intent.getStringExtra("username");
        pwd = intent.getStringExtra("pwd");
        mobileNo = intent.getStringExtra("mobileNo");
        socialMediaId = intent.getIntExtra("socialMediaId", 0);
        displayName = intent.getStringExtra("displayName");
        email = "";
        if (displayName == null) displayName = userName;

        // sync process
        syncUtil = new SyncUtil(this, this);
        // create volley request queue
        requestQueue = VolleySingleton.getsInstance().getmRequestQueue();

        passcodeView = (TextView) findViewById(R.id.passcode);

        Button submitBtn = (Button) findViewById(R.id.submit_passcode_btn);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passcodeVal = passcodeView.getText().toString();

                // validations
                if (passcodeVal == null || passcodeVal.trim().length() == 0) {
                    Toast.makeText(Passcode.this, "Please enter passcode", Toast.LENGTH_LONG).show();
                    return;
                }

                if (!Utility.isNetworkAvailable(Passcode.this)) {
                    showDialogMsg("Please check your network connection.");
                } else {
                    // new RegistrationDataSyncTask().execute("");
                    registrationDataSyncTask();
                }
            }
        });

        alertDialog = new AlertDialog.Builder(Passcode.this).create();

    }


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


    // Async task for registration details on server
    void registrationDataSyncTask() {

        String responseCode = "0";
        String message = "";
        // String userId = "";

        pDialog = new ProgressDialog(Passcode.this);
        pDialog.setMessage("Pls wait...");
        pDialog.setCancelable(false);
        pDialog.show(); // show dialog

        /////////// Create param values for API ///////////////////
        //  {"user":{"id":"0","userName":"rakeshsoni1","password":"abcd","mobileNum":"9867456745",
        // "emailId":"a@a.com","displayName":"abc","socialMediaId":"0"},
        // "strPasscode":"100","strCommuniyID":"1"}

        JSONObject jGroup = new JSONObject();//  Object for main json ie { }
        JSONObject jsonUserObject = new JSONObject();// /Object for user details

        try {
            jsonUserObject.put("id", "0");
            jsonUserObject.put("userName", userName);
            jsonUserObject.put("password", pwd);
            jsonUserObject.put("mobileNum", mobileNo);
            jsonUserObject.put("emailId", email);
            jsonUserObject.put("displayName", displayName);
            jsonUserObject.put("socialMediaId", socialMediaId);

        } catch (JSONException e) {
            Log.e("ERROR", "userRegist" + e.getMessage());

        }

        try {
            jGroup.put("user", jsonUserObject);
            jGroup.put("strPasscode", passcodeVal);
            jGroup.put("strCommuniyID", Constants.communityId);

        } catch (JSONException e) {
            Log.e("ERROR", "registrations Api param -" + e.getMessage());

        }

//             String productImgUrl = URL_LOGIN.replaceAll(" ", "%20");
        // call Api and get response
        // String result = nt.getHttpConnection(Constants.URL_REGISTRATION, "POST", jGroup.toString());

        // Using volley call API
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, Constants.URL_REGISTRATION, jGroup.toString(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                // Parse API response
                Log.d("TESTING", "json  response success " + response.toString());
                boolean result = parseRegistrationJSONResponse(response);

                if (pDialog.isShowing()) {
                    pDialog.dismiss(); //dismiss the progress dialog box
                }

                if (result) {
                    // start server to device sync
                    syncServerToDevice();
                    // pDialog.setMessage("Please wait... \nDownloading data from server....");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Log.d("TESTING", "json error  response--" + error.getMessage());
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss(); //dismiss the progress dialog box
                }
                Log.e("Error", " in registration response : " + error.getMessage() + " : " + error.toString());
                // showDialogMsg(error.toString());
                showDialogMsg("Error : Unable to connect with server.");

            }
        });


        int socketTimeout = 30000;   //30 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jor.setRetryPolicy(policy);
        requestQueue.add(jor);


    }


    boolean parseRegistrationJSONResponse(JSONObject jsonObj) {

        // Log.d("REGIST", "REGIST--" + jGroup);
        // Log.d("REGIST", "REGIST Response--" + result);
        // Log.d("REGIST", "REGIST Url--" + Constants.URL_REGISTRATION);
        boolean result = false;

        ///////////////////////// Get API response /////////////////////////////
        try {

            // Creating JSON Parser instance
            // JSONObject jsonObj = new JSONObject(result);
            if (jsonObj != null) {
                responseCode = jsonObj.getString("responseCode");
                message = jsonObj.getString("msg");
                String strUserId = jsonObj.getString("userId");

                Log.d("jsonResponse", "userId : " + strUserId);
                Log.d("jsonResponse", "responseCode: " + responseCode);
                Log.d("jsonResponse", "msg: " + message);

                int userId = Integer.parseInt(strUserId);

                if (responseCode.equals("1")) {
                    // enter registration info into local database;
                    // showDialogMsg("1 "+ message);
                    result = true;
                    User user = new User();
                    user.setUserName(userName);
                    user.setEmail(email);
                    user.setDisplayName(displayName);
                    user.setMobiLeNumber(mobileNo);
                    user.setPassword(pwd);
                    user.setUserType(socialMediaId);
                    user.setUserId(userId);

                    DataBaseManager dataBaseManager = new DataBaseManager();
                    dataBaseManager.insertUser(getBaseContext(), user);

                    // call the main activity after setting global user
                    // set for globally access
                    Utility.setuser(user);


                } else {
                    showDialogMsg(message);
                }

            }//if

        } catch (Exception e) {
            Log.e("ERROR", "registrations error response" + e.getMessage());
            Log.e("ERROR", "registrations error " + e.toString());
            message = "Exception : " + e.toString();
        }

        return result;

    } // async task class


    private void syncServerToDevice() {

        pDialog = new ProgressDialog(Passcode.this);
        pDialog.setMessage("Please wait...\nDownloading data from server");
        pDialog.setCancelable(false);
        pDialog.show(); // show dialog

        String serverSyncTime= Utility.loadPreferences("last_sync_time");
        String serverSyncTimeEncode = "";

        try{
            serverSyncTimeEncode = URLEncoder.encode(serverSyncTime, "UTF-8");
        }catch(Exception e){
            Log.e("Error", "encoding server sync time");
        }

        Log.d("ServerToDevice", "Sync time-" + serverSyncTime);
        String url=Constants.URL_SERVER_TO_DEVICE+"&last_sync_time=" + serverSyncTimeEncode;
        // String url=Constants.URL_SERVER_TO_DEVICE + "&last_sync_time=" + serverSyncTime;
        Log.d("MYTAG","final url -"+url);
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("TESTING", "json  response" + response.toString());

                syncUtil.parseServerToDeviceJSONResponse(response);

                // downloading the thumbnail images
                // downloadThumbnailImages();
                // syncDeviceToServer();

                if (pDialog.isShowing()) {
                    pDialog.dismiss(); //dismiss the progress dialog box
                }

                // sync completed start the main activity
                Intent I = new Intent(Passcode.this, MainActivity.class);
                startActivity(I);
                DataBaseManager dataBaseManager = new DataBaseManager();
                Boolean B = dataBaseManager.updateuser(Passcode.this,Utility.getuser().getUserId(), 1);
                finishAffinity();
               // finish();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TESTING", "error  response" + error.getMessage());
                // Log.d("TESTING", "json error  response--" + error.getMessage());
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss(); //dismiss the progress dialog box
                }
                // showDialogMsg(error.toString());
                showDialogMsg("Error : Unable to connect with server.");

            }
        });

        int socketTimeout = 30000;   //30 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jor.setRetryPolicy(policy);

        requestQueue.add(jor);

    }//syncServerToDevice()

    @Override
    public void syncResult(boolean result, int responseCode, String msg) {

    }

    /*
    private void syncDeviceToServer() {

        pDialog = new ProgressDialog(Passcode.this);
        pDialog.setMessage("Please wait...\nUploading device data to server.");
        pDialog.setCancelable(false);
        pDialog.show(); // show dialog

        JSONObject jResult = syncUtil.getDeviceData();

        Log.d("TESTING", "json  param list" + jResult.toString());
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, Constants.URL_DEVICE_TO_SERVER, jResult.toString(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                syncUtil.parseDeviceToServerJSONResponse(response);
                Log.d("TESTING", "json  response " + response.toString());
                if (pDialog.isShowing()) {
                    pDialog.dismiss(); //dismiss the progress dialog box
                }

                // sync completed start the main activity
                Intent I = new Intent(Passcode.this, MainActivity.class);
                startActivity(I);
                finish();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TESTING", "json error  response--" + error.getMessage());
                if (pDialog.isShowing()) {
                    pDialog.dismiss(); //dismiss the progress dialog box
                }
                showDialogMsg(error.getMessage());
            }
        });
        requestQueue.add(jor);


    }//syncDeviceToServer()



    // Download profile thumbnail image from server
    private void downloadProfileThumbnailImage() {
        String imageUrl = "";

        // Create db object of SQLController class to call data base method
        db = new SQLController(getBaseContext());

        // Get list of image url from data base
        List<Profile> profileList = db.getProfileThumbnailImage();
        if (profileList != null && profileList.size() != 0) {
            for (int i = 0; i < profileList.size(); i++) {
                p = profileList.get(i);
                String dbUrl = p.getStrThumbnailURL();
                imageUrl = "http://52.69.236.183:8080/Melawa/images/c1/profiles/" + dbUrl;
                Log.d("VIJESH", "image url-" + imageUrl);


                if (!imageUrl.isEmpty()) {

                    // Download image from server using volley
                    ImageLoader imageLoader = VolleySingleton.getsInstance().getImageLoader();
                    imageLoader.get(imageUrl, new ImageLoader.ImageListener() {
                        @Override
                        public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                            bitmap = response.getBitmap();

                            if (bitmap != null) {

                                // save image on internal storage
                                saveToInternalStorage(bitmap,"profile");

                                // Save image to data base
                                db.saveProfileThumbnailImage(p.getIntID(), bitmap);

                            }

                        }

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("ERROR", "downloadProfileThumbnailImage - " + error.getMessage());
                        }
                    });


                }//if
            } //for
        } // if
    } // method

    // Download mews thumbnail image from server
    private void downloadThumbnailImages() {

        String imageUrl = "";
        // Create db object of SQLController class to call data base method
        db = new SQLController(getBaseContext());

        // Get list of image url from data base
        List<News> newsList = db.getNewsThumbnailImage();
        List<Profile> profileList = db.getProfileThumbnailImage();
        List<Event> eventList = db.getEventThumbnailImage();

        // total number of images
        int totalImg = newsList.size() + profileList.size() + eventList.size();

        if (newsList != null && newsList.size() > 0) {

            for (int i = 0; i < newsList.size(); i++) {
                n = newsList.get(i);
                String dbUrl = n.getStrThumbnailURL();
                imageUrl = "http://52.69.236.183:8080/Melawa/images/c1/news/" + dbUrl;
                Log.d("VIJESH", "image url-" + imageUrl);

                // Download image from server using volley
                if (!imageUrl.isEmpty()) {
                    ImageLoader imageLoader = VolleySingleton.getsInstance().getImageLoader();
                    imageLoader.get(imageUrl, new ImageLoader.ImageListener() {
                        @Override
                        public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                            bitmap = response.getBitmap();

                            if (bitmap != null) {

                                // save image on internal storage
                                saveToInternalStorage(bitmap,"news");
                                db.saveNewsThumbnailImage(n.getId(), bitmap);

                            }

                        }

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("ERROR", " downloadNewsThumbnailImage - " + error.getMessage());

                        }
                    });


                }//if
            } //for
        } // if
    } // method


    // Download mews thumbnail image from server
    private void downloadEventThumbnailImage() {
        String imageUrl = "";
        // Create db object of SQLController class to call data base method
        db = new SQLController(getBaseContext());
        // Get list of image url from data base
        List<Event> eventList = db.getEventThumbnailImage();
        if (eventList != null && eventList.size() != 0) {
            for (int i = 0; i < eventList.size(); i++) {
                event = eventList.get(i);
                String dbUrl = event.getStrThumbnailURL();
                imageUrl = "http://52.69.236.183:8080/Melawa/images/c1/events/" + dbUrl;
                Log.d("VIJESH", "image url-" + imageUrl);

                // Download image from server using volley
                if (!imageUrl.isEmpty()) {
                    ImageLoader imageLoader = VolleySingleton.getsInstance().getImageLoader();
                    imageLoader.get(imageUrl, new ImageLoader.ImageListener() {
                        @Override
                        public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                            bitmap = response.getBitmap();

                            if (bitmap != null) {
                                // save image on internal storage
                                saveToInternalStorage(bitmap,"event");
                                db.saveEventThumbnailImage(event.getId(), bitmap);


                            }

                        }

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("ERROR", " downloadNewsThumbnailImage - " + error.getMessage());

                        }
                    });


                }//if
            } //for
        } // if
    } // method

    // save bitmap image to internal storage
    private void saveToInternalStorage(Bitmap bitmapImage,String folderName) {
        if (bitmapImage != null) {
            try {

                // create byte array of bitmap image
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmapImage.compress(Bitmap.CompressFormat.JPEG, 40, bytes);

                //Create Folder/directory in internal storage
                File folder = new File(Environment.getExternalStorageDirectory().toString() + "/Melawa/Images/"+folderName);
                folder.mkdirs();

                //Save the path as a string value
                String extStorageDirectory = folder.toString();

                //Create New file and name it Image with milli second time
                // get current time in milli second
                long time = System.currentTimeMillis();
                File file = new File(extStorageDirectory, "Image" + time + ".jpg");

                //write the bytes in file
                FileOutputStream fo = new FileOutputStream(file);
                fo.write(bytes.toByteArray());

                // remember close de FileOutput
                fo.close();

            } catch (Exception e) {
                Log.e("saveToInternal", "errror-" + e.getMessage());
            }

        }

    } // saveToInternalStorage()

    */

}
