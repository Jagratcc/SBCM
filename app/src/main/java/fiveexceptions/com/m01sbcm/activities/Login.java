package fiveexceptions.com.m01sbcm.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

import fiveexceptions.com.m01sbcm.R;
import fiveexceptions.com.m01sbcm.database.DataBaseManager;
import fiveexceptions.com.m01sbcm.model.User;
import fiveexceptions.com.m01sbcm.sync.SQLController;
import fiveexceptions.com.m01sbcm.sync.SyncUtil;
import fiveexceptions.com.m01sbcm.sync.VolleySingleton;
import fiveexceptions.com.m01sbcm.sync.model.Event;
import fiveexceptions.com.m01sbcm.sync.model.News;
import fiveexceptions.com.m01sbcm.utility.Constants;
import fiveexceptions.com.m01sbcm.utility.Utility;

public class Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, SyncInterface
{

    //google api varible intialize*\
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;

    private ProgressDialog mProgressDialog;
    private View gmailBtn;
    //facebook  api varible intialize*\
    LoginButton loginbuttonFb;
    ProfileTracker mProfileTracker;
    View v;
    CallbackManager mCallBackManager;

    //custom layout varible
    EditText username,password;
    TextView signup;
    Button signin;

    AlertDialog alertDialog;
    // SQLiteDatabase sqLiteDatabase;
    DataBaseManager dataBaseManager=new DataBaseManager();

    // sync process
    ProgressDialog pDialog = null;
    RequestQueue requestQueue;
    SyncUtil syncUtil;

    Bitmap bitmap = null;
    fiveexceptions.com.m01sbcm.sync.model.Profile p = null;
    News n = null;
    Event event = null;
    SQLController db = null;
    String responseCode, message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //facebook sdk intialize and this should be before setcontent view
        FacebookSdk.sdkInitialize(this);
        mCallBackManager = CallbackManager.Factory.create();
         //reading from layout
        setContentView(R.layout.activity_login);
        //reading text view


        // sync process
        syncUtil = new SyncUtil(this, this);
        // create volley request queue
        requestQueue = VolleySingleton.getsInstance().getmRequestQueue();

        // -------------------------  Facebook  api code start from here -------------------------
        //reading facebook button from xml
        loginbuttonFb=(LoginButton)findViewById(R.id.login_button);
        //apply permission for  read permission for public profile view for facebook
        loginbuttonFb.setReadPermissions("public_profile");
        //apply method when click on facebook button this method will call
        loginbuttonFb.registerCallback(mCallBackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {

                AccessToken acesstoken = loginResult.getAccessToken();
                if (Profile.getCurrentProfile() == null) {
                    mProfileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                            Log.d("Facebook login", "1111111");
                            if (currentProfile == null) {
                                facebookLoginSuccess(oldProfile);
                            } else {
                                facebookLoginSuccess(currentProfile);
                            }
                        }
                    };
                    mProfileTracker.startTracking();

                } else {
                    Log.d("Facebook login", "22222222");
                    facebookLoginSuccess(Profile.getCurrentProfile());
                }

                LoginManager.getInstance().logOut();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

                if (!Utility.isNetworkAvailable(Login.this)) {
                    showDialogMsg("Please check your network connection.");
                } else {
                    showDialogMsg("Facebook Error " + error.toString());
                }
            }
        });
        // Button listeners


        // -------------------------  GOOGLE  api code start from here -------------------------
         //apply click listener for google sign in button
        gmailBtn = findViewById(R.id.sign_in_button);
        gmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gmailSignIn();
            }
        });

        // [START configure_signin]
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // [END configure_signin]

        // [START build_client]
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        // [END build_client]


        // -------------------------  CUSTOM SIGNIN code start from here -------------------------

        username=(EditText)findViewById(R.id.editText2);
        username.setImeOptions(EditorInfo.IME_ACTION_DONE);
        password=(EditText)findViewById(R.id.editText3);
        password.setImeOptions(EditorInfo.IME_ACTION_DONE);

        // username.setText("ajay");
        // password.setText("12345");

        signin=(Button)findViewById(R.id.button);
        signup=(TextView)findViewById(R.id.noofcomment);
        signup.setPaintFlags(signup.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Intent I =new Intent(Login.this,SignUp.class);
                startActivity(I);
            }
        });

        // ---------------------------------------------------------------------------------------

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(username.getText().toString().equals(""))
                {

                    Toast.makeText(getBaseContext(),"Enter the user name",Toast.LENGTH_LONG).show();
                    return;

                }

                if(password.getText().toString().equals(""))
                {

                    Toast.makeText(getBaseContext(),"Enter the password",Toast.LENGTH_LONG).show();
                    return;

                }

             User u=dataBaseManager.getValidUser(getBaseContext(), username.getText().toString(), password.getText().toString());
             if(u==null)    {
                 //check network connection
                 //if network is connected then call API  else show message
                 if (!Utility.isNetworkAvailable(Login.this)) {
                     showDialogMsg("Please check your network connection.");
                 } else {
                     // network is available
                     // call API
                     syncLoginCredentialsOnServer(username.getText().toString(), password.getText().toString(), Constants.LOGIN_TYPE_CUSTOM,username.getText().toString());

                 }
               //  showDialogMsg("Invalid Username or Password");
             } else {
                 loginSuccess(u);
             }
            }
        });

        alertDialog = new AlertDialog.Builder(Login.this).create();

    }


    public void showDialogMsg(String msg){

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


    public void facebookLoginSuccess(Profile fbProfile){

        // Profile fbProfile = Profile.getCurrentProfile();
        String fbName = fbProfile.getName();
        String fbId = fbProfile.getId();

        User user = dataBaseManager.getUser(getBaseContext(),fbId);

        // user does not exist into our system, register it
        if (user == null) {
            //check network connection
            //if network is connected then call API  else show message
            if (!Utility.isNetworkAvailable(Login.this)) {
                showDialogMsg("Please check your network connection.");
            } else {
                // network is available
                // call API
                syncLoginCredentialsOnServer(fbId, null, Constants.LOGIN_TYPE_FB,fbName); // uname , pass, userType

            }

        } else {
            loginSuccess(user);
        }

    } // facebookLoginSuccess


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //if google api crediatial matches  then it will go in side if condition
        if (requestCode == RC_SIGN_IN)
        {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleGmailSignInResult(result);
        }
         ////////////////////////////////code for facebook////////////////////////////////
        mCallBackManager.onActivityResult(requestCode, resultCode, data);

    }

    //if connection failed this method call this mathod automatic override from GoogleApiClient.OnConnectionFailedListener interface
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this, "Please check your network connection.",Toast.LENGTH_LONG).show();
    }

    //this mathod is use for sign in google sign in process
    private void gmailSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        //this method send result to onActivityResult(int requestCode, int resultCode, Intent data)
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    // Gmail
    // this method call  in  onActivityResult(int requestCode, int resultCode, Intent data)  and handle result of sign in sucess or not
    private void handleGmailSignInResult(GoogleSignInResult result) {

        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            String  userName = acct.getEmail();
            // String  emailid = userName;
            String  name    = acct.getDisplayName();
            // int userType= Constants.LOGIN_TYPE_GMAIL;

            // google sign out
            gmailSignOut();

            User user = dataBaseManager.getUser(getBaseContext(), userName);

            // user does not exist into our system, register it
            if(user == null){

                //check network connection
                //if network is available  then call API  else show message
                if (!Utility.isNetworkAvailable(Login.this)) {
                    showDialogMsg("Please check your network connection.");
                } else {

                    syncLoginCredentialsOnServer(userName, null, Constants.LOGIN_TYPE_GMAIL,name);

                }


            } else {
                // user exist in our system , flow goes to main screen
                loginSuccess(user);
            }

            //  updateUI(true);
        } else {
            showDialogMsg("Gmail Error : invalid email or password:");
            // Signed out, show unauthenticated UI.
            //  updateUI(false);
        }
    }


    //this method use for sign out from google account
    private void gmailSignOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        // updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }

    void loginSuccess(User user){

        // set for globally access
        Utility.setuser(user);
        int id=user.getUserId();
        DataBaseManager dataBaseManager=new DataBaseManager();

        Boolean B    =dataBaseManager.updateuser(this, id, 1);
        Log.d("MEALWAAPP", " login user -" +user);
        Log.d("MEALWAAPP", " login user ID  -" +id);
        Log.d("MEALWAAPP", " login user Boolean  -" + B);

        if(B) {
            // online mode
            if (Utility.isNetworkAvailable(Login.this)) {
             //todo use it for reduce code optimizations
               // syncUtil.syncDeviceToServer();
                syncDeviceToServer();
            }
            // offlime mode
            else {
                Log.d(TAG, "loginSuccess:i am in finish");

                Intent I = new Intent(Login.this, MainActivity.class);
                startActivity(I);
                finish();
            }
        }

    } //loginSuccess

    // To sync login details on server
    Boolean ApiResponse = false;

    private Boolean syncLoginCredentialsOnServer(final String userName, final String password, final int userType,final String dispalyName) {
        /////////// Create param values for API ///////////////////

        JSONObject jGroup = new JSONObject();//  Object

        try {
            jGroup.put("strUserName", userName);
            jGroup.put("strPassword", password);
            jGroup.put("strCommunityID", Constants.communityId);
            jGroup.put("strSocialMediaID", userType);
            Log.d("MEALWAAPP", " login param value -" + jGroup.toString());
        } catch (JSONException e) {
            Log.e("ERROR", "login Api param -" + e.getMessage());

        }

        // Using volley call api
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, Constants.URL_LOGIN, jGroup.toString(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // Parse API response
                if (response != null && response.length() != 0) {
                    int userId = syncUtil.parseLoginJSONResponse(response);
                    Log.d("VIJESH"," loginresponswe ---"+response.toString());
                    if(userId > 0){
                        ApiResponse = true;
                    }else{
                        ApiResponse = false;
                    }

                    if (ApiResponse) {
                        // success -> DB entry + call loginSuccess(user);
                        User user = new User();
                        user.setUserId(userId);
                        user.setUserName(userName);
                        user.setPassword(password);
                        user.setDisplayName(dispalyName);
                        user.setUserType(userType);

                        Log.d("MEALWAAPP", " user deatil strore in db-" + user.toString());
                        dataBaseManager.insertUser(getBaseContext(), user);
                        // User u = dataBaseManager.getValidUser(getBaseContext(), userName, password);
                        // u = user;
                        loginSuccess(user);
                    } else {
                        //Dialog message for custom user
                        if (userType == Constants.LOGIN_TYPE_CUSTOM) {
                            showDialogMsg("Invalid Username or Password");
                        }
                        //Dialog message for facebook user
                        if (userType == Constants.LOGIN_TYPE_FB) {
                            if (!Utility.isNetworkAvailable(Login.this)) {
                                showDialogMsg("Please check your network connection.");
                            } else {

                                syncAppSetting(userName, Constants.LOGIN_TYPE_FB, dispalyName);
                            }
                        }
                        //Dialog message for gmail  user
                        if (userType == Constants.LOGIN_TYPE_GMAIL) {
                            if (!Utility.isNetworkAvailable(Login.this)) {
                                showDialogMsg("Please check your network connection.");
                            } else {

                                syncAppSetting(userName,Constants.LOGIN_TYPE_GMAIL, dispalyName);
                            }
                        }
                    }
                    Log.d("TESTING", "json  response " + response.toString());
                }//if

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TESTING", "json error  response--" + error.getMessage());
                // Log.d("TESTING", "json error  response--" + error.getMessage());
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss(); //dismiss the progress dialog box
                }
                showDialogMsg("Error : Unable to connect with server.");
            }
        });
        requestQueue.add(jor);

        return ApiResponse;
    }//syncLoginCredentialsOnServer()

    private void syncAppSetting(final String userName,final int mediaIdType,final String displayName) {

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
                                    Utility.savePreferences(c.getString("strName"),c.getString("strValue"));

                                    String flag = c.getString("strValue");
                                    if (flag.equals("1")) {
                                        Intent intent = new Intent(Login.this, Passcode.class);
                                        intent.putExtra("username", userName);
                                        intent.putExtra("pwd", "");
                                        intent.putExtra("mobileNo", "");
                                        intent.putExtra("displayName", displayName);
                                        intent.putExtra("socialMediaId", mediaIdType);
                                        startActivity(intent);
                                        finish();

                                    } else {
                                        //check network connection
                                        //if network is connected then call API  else show message
                                        if (!Utility.isNetworkAvailable(Login.this)) {
                                            showDialogMsg("Please check your network connection.");
                                        } else {

                                            registrationDataSyncTask(userName, mediaIdType,displayName);
                                        }

                                    }
                                    Log.d("APPSETTING", "strName  response--" + c.getString("strName"));
                                    Log.d("APPSETTING", "strValue  response--" + c.getString("strValue"));
                                }

                            }//if
                        }//if

                    } catch (JSONException e) {
                        Log.e("ERROR", "" + e.getMessage());
                    }


                }//if

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TESTING", "json error  response--" + error.getMessage());
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss(); //dismiss the progress dialog box
                }
                showDialogMsg("Error : Unable to connect with server.");

            }
        });
        requestQueue.add(jor);

    }//syncAppSetting
    void registrationDataSyncTask(final String userName ,final int mediaIdType,final String displayName) {

        String responseCode = "0";
        String message = "";
        // String userId = "";

        pDialog = new ProgressDialog(Login.this);
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
            jsonUserObject.put("password", "");
            jsonUserObject.put("mobileNum", "");
            jsonUserObject.put("emailId", "");
            jsonUserObject.put("displayName", displayName);
            jsonUserObject.put("socialMediaId", mediaIdType);

        } catch (JSONException e) {
            Log.e("ERROR", "userRegist" + e.getMessage());

        }

        try {
            jGroup.put("user", jsonUserObject);
            jGroup.put("strPasscode", "");
            jGroup.put("strCommuniyID", Constants.communityId);

            Log.d("MELAWA", "regist param values-" + jGroup.toString());
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
                boolean result = parseRegistrationJSONResponse(response, userName,displayName);

                if (pDialog.isShowing()) {
                    pDialog.dismiss(); //dismiss the progress dialog box
                }

                if (result) {

                    //check network connection
                    //if network is connected then call API  else show message
                    if (!Utility.isNetworkAvailable(Login.this)) {
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
                if (pDialog != null &&  pDialog.isShowing()) {
                    pDialog.dismiss(); //dismiss the progress dialog box
                }
                Log.e("Error", " in registration response : " + error.getMessage() + " : " + error.toString());
                showDialogMsg("Error : Unable to connect with server.");
            }
        });
        requestQueue.add(jor);


    }//registrationDataSyncTask

    boolean parseRegistrationJSONResponse(JSONObject jsonObj, String userName,String displayName) {

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
                    user.setDisplayName(displayName);
                    user.setMobiLeNumber("");
                    user.setPassword("");
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

    } // parseRegistrationJSONResponse



    private void syncServerToDevice() {

        pDialog = new ProgressDialog(Login.this);
        pDialog.setMessage("Pls wait...");
        pDialog.setCancelable(false);
        pDialog.show(); // show dialog

        String serverSyncTime= Utility.loadPreferences("last_sync_time");
        String serverSyncTimeEncode = "";

        try{
            serverSyncTimeEncode =URLEncoder.encode(serverSyncTime, "UTF-8");
        }catch(Exception e){
            Log.e("Error", "encoding server sync time");
        }

        Log.d("ServerToDevice", "Sync time-" + serverSyncTime);

        String url=Constants.URL_SERVER_TO_DEVICE+"&last_sync_time=" + serverSyncTimeEncode;

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

                Intent I =new Intent(Login.this,  MainActivity.class);
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
                showDialogMsg("Error : Unable to connect with server.");

            }
        });
        requestQueue.add(jor);

    }//syncServerToDevice()

/*
    // Download profile thumbnail image from server
    private void downloadProfileThumbnailImage() {
        String imageUrl = "";

        // Create db object of SQLController class to call data base method
        db = new SQLController(getBaseContext());

        // Get list of image url from data base
        List<fiveexceptions.com.melawa.sync.model.Profile> profileList = db.getProfileThumbnailImage();
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
    private void downloadNewsThumbnailImage() {
        String imageUrl = "";
        // Create db object of SQLController class to call data base method
        db = new SQLController(getBaseContext());
        // Get list of image url from data base
        List<News> newsList = db.getNewsThumbnailImage();
        if (newsList != null && newsList.size() != 0) {

            ImageLoader imageLoader = VolleySingleton.getsInstance().getImageLoader();

            for (int i = 0; i < newsList.size(); i++) {
                n = newsList.get(i);
                String dbUrl = n.getStrThumbnailURL();

                if (!dbUrl.isEmpty()) {

                    imageUrl = "http://52.69.236.183:8080/Melawa/images/c1/news/" + dbUrl;
                    Log.d("VIJESH", "image url-" + imageUrl);

                    // Download image from server using volley

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

    private void syncDeviceToServer() {

        // To fix the facebook issue : when activity already finished
        if((Login.this).isFinishing())
        {
            // Intent I = new Intent(Login.this, MainActivity.class);
            // startActivity(I);
            // finish();
            return;
        }

        pDialog = new ProgressDialog(Login.this);
        pDialog.setMessage("Pls wait...");
        pDialog.setCancelable(false);
        pDialog.show(); // show dialog

        JSONObject jResult = syncUtil.getDeviceData();

        Log.d("DToS",""+jResult.toString());
        // Log.d("DToS",""+jResult.toString());

        Log.d("TESTING", "json  param list" + jResult.toString());
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, Constants.URL_DEVICE_TO_SERVER, jResult.toString(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                if(response == null){
                    Log.d("TESTING", "json  response is null");
                }else{
                    Log.d("TESTING", "json  response is not null");
                }

                syncUtil.parseDeviceToServerJSONResponse(response);
                Log.d("TESTING", "json  response " + response.toString());
                if (pDialog.isShowing()) {
                    pDialog.dismiss(); //dismiss the progress dialog box
                }

                if (Utility.isNetworkAvailable(Login.this)) {
                    // sync server to device
                    syncServerToDevice();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TESTING", "json error  response--" + error.getMessage());
                if (pDialog != null &&  pDialog.isShowing()) {
                    pDialog.dismiss(); //dismiss the progress dialog box
                }
                // showDialogMsg(error.getMessage());
                showDialogMsg("Error : Unable to connect with server.");
            }
        });
        requestQueue.add(jor);


    }//syncDeviceToServer()

    @Override
    public void syncResult(boolean result, int responseCode, String msg) {

    }
}
