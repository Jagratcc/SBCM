package fiveexceptions.com.m01sbcm.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

import fiveexceptions.com.m01sbcm.R;
import fiveexceptions.com.m01sbcm.database.DataBaseManager;
import fiveexceptions.com.m01sbcm.model.User;
import fiveexceptions.com.m01sbcm.sync.SyncUtil;
import fiveexceptions.com.m01sbcm.sync.VolleySingleton;
import fiveexceptions.com.m01sbcm.utility.Constants;
import fiveexceptions.com.m01sbcm.utility.Utility;


public class SignUp extends AppCompatActivity implements SyncInterface{

    Button signup;
    TextView name, password, mobileno;
    SQLiteDatabase sqLiteDatabase;

    /////////////////////////popupWindow//////////////////////////////////
    private PopupWindow pw;
    AlertDialog alertDialog;
    // sync process
    ProgressDialog pDialog = null;
    RequestQueue requestQueue;
    SyncUtil syncUtil;


    String userName = "";
    String pass = "";
    String mobileNumber = "";
    String responseCode, message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        name = (TextView) findViewById(R.id.username);
        password = (TextView) findViewById(R.id.password);
        mobileno = (TextView) findViewById(R.id.mobile);
        signup = (Button) findViewById(R.id.button2);

        // sync process
        syncUtil = new SyncUtil(this, this);
        // create volley request queue
        requestQueue = VolleySingleton.getsInstance().getmRequestQueue();


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get userName , mobileNumber and password value from edit text

                userName = name.getText().toString();
                pass = password.getText().toString();
                mobileNumber = mobileno.getText().toString();
                // int socialMediaId=0;

                // check validations
                if (userName == null || userName.trim().length() == 0) {
                    Toast.makeText(SignUp.this, "Please enter username", Toast.LENGTH_LONG).show();
                    return;
                }
                if (pass == null || pass.trim().length() == 0) {
                    Toast.makeText(SignUp.this, "Please enter password", Toast.LENGTH_LONG).show();
                    return;
                }
                if (mobileNumber == null || mobileNumber.trim().length() == 0) {
                    Toast.makeText(SignUp.this, "Please enter mobile number", Toast.LENGTH_LONG).show();
                    return;
                }

                // initiatePopupWindow();

                //check network , if network is available then call app setting api
                if (!Utility.isNetworkAvailable(SignUp.this)) {
                    showDialogMsg("Please check your network connection.");
                } else {

                    syncAppSetting();
                }


            }
        });

        alertDialog = new AlertDialog.Builder(SignUp.this).create();

    }

    private void syncAppSetting() {

        // Using volley call api
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, Constants.URL_APP_SETTING, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // Parse API response
                if (response != null && response.length() != 0) {
                    //  syncUtil.parseLoginJSONResponse(response);
                    Log.d("APPSETTING", "json  response--" + response.toString());

                    try {
                        responseCode = response.getString("responseCode");
                        message = response.getString("msg");
                        if (responseCode.equals("1") && message.equals("Success")) {
                            JSONArray jsonAppSettingArray = response.getJSONArray("settingList");
                            if (jsonAppSettingArray != null && jsonAppSettingArray.length() > 0 && response.has("settingList")) {
                                for (int i = 0; i < jsonAppSettingArray.length(); i++) {
                                    JSONObject c = jsonAppSettingArray.getJSONObject(i);

                                    //Save app setting to shared preferences
                                    Utility.savePreferences(c.getString("strName"), c.getString("strValue"));

                                    String flag = c.getString("strValue");
                                    // If flag value is 1, flow  pass to passcode screen
                                    if (flag.equals("1")) {
                                        Intent intent = new Intent(SignUp.this, Passcode.class);
                                        intent.putExtra("username", userName);
                                        intent.putExtra("pwd", pass);
                                        intent.putExtra("mobileNo", mobileNumber);
                                        intent.putExtra("socialMediaId", 0);
                                        startActivity(intent);
                                        finish();

                                    } // if
                                    // If flag value is 0 then directly call registration api
                                    else {
                                        //check network connection
                                        //if network is available then call API  else show message
                                        if (!Utility.isNetworkAvailable(SignUp.this)) {
                                            showDialogMsg("Please check your network connection.");
                                        } else {

                                            registrationDataSyncTask();
                                        }

                                    } // else
                                    Log.d("APPSETTING", "strName  response--" + c.getString("strName"));
                                    Log.d("APPSETTING", "strValue  response--" + c.getString("strValue"));
                                }

                            }//if
                        }//if

                    } catch (JSONException e) {
                        Log.e("ERROR", "syncAppSetting - " + e.getMessage());
                    }//catch


                }//if

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR", "json error  syncAppSetting --" + error.getMessage());
                showDialogMsg("Error : Unable to connect with server.");
            }
        });
        requestQueue.add(jor);

    } //syncAppSetting()

    // Async task for registration details on server
    void registrationDataSyncTask() {

        String responseCode = "0";
        String message = "";
        // String userId = "";

        pDialog = new ProgressDialog(SignUp.this);
        pDialog.setMessage("Pls wait...");
        pDialog.setCancelable(false);
        pDialog.show(); // show dialog

        JSONObject jGroup = new JSONObject();//  Object for main json ie { }
        JSONObject jsonUserObject = new JSONObject();// /Object for user details

        try {
            jsonUserObject.put("id", "0");
            jsonUserObject.put("userName", userName);
            jsonUserObject.put("password", pass);
            jsonUserObject.put("mobileNum", mobileNumber);
            jsonUserObject.put("emailId", "");
            jsonUserObject.put("displayName", userName);
            jsonUserObject.put("socialMediaId", 0);

        } catch (JSONException e) {
            Log.e("ERROR", "userRegist" + e.getMessage());

        }

        try {
            jGroup.put("user", jsonUserObject);
            jGroup.put("strPasscode", "");
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

                    //check network connection
                    //if network is connected then call API  else show message
                    if (!Utility.isNetworkAvailable(SignUp.this)) {
                        showDialogMsg("Please check your network connection.");
                    } else {
                        // start server to device sync
                        syncServerToDevice();
                    }


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
                // showDialogMsg(error.getMessage());
                showDialogMsg("Error : Unable to connect with server.");
            }
        });
        requestQueue.add(jor);


    }

    boolean parseRegistrationJSONResponse(JSONObject jsonObj) {

        boolean result = false;

        ///////////////////////// Get API response /////////////////////////////
        try {

            if (jsonObj != null) {
                String responseCode = jsonObj.getString("responseCode");
                String message = jsonObj.getString("msg");
                String strUserId = jsonObj.getString("userId");

                Log.d("jsonResponse", "userId : " + strUserId);
                Log.d("jsonResponse", "responseCode: " + responseCode);
                Log.d("jsonResponse", "msg: " + message);

                int userId = Integer.parseInt(strUserId);

                if (responseCode.equals("1")) {
                    // enter registration info into local database;

                    result = true;
                    User user = new User();
                    user.setUserName(userName);
                    user.setEmail("");
                    user.setDisplayName(userName);
                    user.setMobiLeNumber(mobileNumber);
                    user.setPassword(pass);
                    user.setUserType(0);
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
            message = "Exception : " + e.getMessage();
        }

        return result;

    } // async task class

    private void syncServerToDevice() {

        pDialog = new ProgressDialog(SignUp.this);
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
        // String url=Constants.URL_SERVER_TO_DEVICE + "&last_sync_time=" + serverSyncTime;
        Log.d("MYTAG", "final url -" + url);
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
                Intent I = new Intent(SignUp.this, MainActivity.class);
                startActivity(I);
                finish();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TESTING", "error  response" + error.getMessage());
                // Log.d("TESTING", "json error  response--" + error.getMessage());
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss(); //dismiss the progress dialog box
                }
                showDialogMsg("Error : Unable to connect with server.");
                // showDialogMsg(error.getMessage());

            }
        });
        requestQueue.add(jor);

    }//syncServerToDevice()

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

    }


    /*
    // ------------------- Passcode POPUP WINDOW  ------------------------------------------------
    private void initiatePopupWindow() {
        try {
            //We need to get the instance of the LayoutInflater, use the context of this activity
            LayoutInflater inflater = (LayoutInflater)  SignUp.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Inflate the view from a predefined XML layout
            View layout = inflater.inflate(R.layout.popup, (ViewGroup) findViewById(R.id.pop));
            // create a 300px width and 470px height PopupWindow
            int Width= Utility.getScreenWidth(this).widthPixels;
            int Hight=Utility.getScreenWidth(this).heightPixels;
            pw = new PopupWindow(layout,Width, Hight, true);
            // display the popup in the center
            pw.showAtLocation(layout, Gravity.CENTER, 0, 0);


            Button next = (Button) layout.findViewById(R.id.button3);

            next.setOnClickListener(cancel_button_click_listener);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private View.OnClickListener cancel_button_click_listener = new View.OnClickListener() {
        public void onClick(View v) {
            pw.dismiss();
            // Intent I =new Intent(SignUp.this,  MainActivity.class);
            // startActivity(I);

        }
    };
    */

}
