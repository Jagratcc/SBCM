package fiveexceptions.com.m01sbcm.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.List;

import fiveexceptions.com.m01sbcm.R;
import fiveexceptions.com.m01sbcm.adapters.CommentAdapter;
import fiveexceptions.com.m01sbcm.database.DataBaseHelper;
import fiveexceptions.com.m01sbcm.database.DataBaseManager;
import fiveexceptions.com.m01sbcm.model.Comment;
import fiveexceptions.com.m01sbcm.model.User;
import fiveexceptions.com.m01sbcm.sync.SyncUtil;
import fiveexceptions.com.m01sbcm.sync.VolleySingleton;
import fiveexceptions.com.m01sbcm.utility.Constants;
import fiveexceptions.com.m01sbcm.utility.Utility;

/**
 * Created by amit on 23/4/16.
 */
public class CommentDetail extends Activity implements SyncInterface {

    EditText editText;
   ImageButton addcomment;
    List<Comment> Commenteslist;
    RecyclerView recyclerview;
    String eventornewsid;
    String flag;
    boolean is_update = false;
    private ProgressDialog pDialog;
    private SyncUtil syncUtil;
    RequestQueue requestQueue;
    AlertDialog alertDialog;
    private SwipeRefreshLayout swipeContainer;
    private RecyclerView recList;
    TextView toolbartext;

    DataBaseHelper dataBaseHelper;

    DataBaseManager dataBaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commentdetail);

        editText = (EditText) findViewById(R.id.editText6);
        addcomment = (ImageButton) findViewById(R.id.comment);
        recyclerview = (RecyclerView) findViewById(R.id.cardList);
        toolbartext=(TextView)findViewById(R.id.heading);


        Intent intent = getIntent();
        flag = intent.getStringExtra("flag");
        eventornewsid = intent.getStringExtra("id");

        // sync process
        syncUtil = new SyncUtil(this, this);
        // create volley request queue
        requestQueue = VolleySingleton.getsInstance().getmRequestQueue();

        // initailize database
        dataBaseHelper = new DataBaseHelper(getBaseContext());
        // sqLiteDatabase = dataBaseHelper.openDataBase();
        dataBaseManager = new DataBaseManager();



        if (flag.equals("news"))
        {

            toolbartext.setText(getResources().getString(R.string.News_Comment));


        } else if (flag.equals("event"))
        {
            toolbartext.setText(getResources().getString(R.string.Event_Comment));

        }


        addcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Editable e = editText.getText();
                String s = e.toString();

                String my_new_s = s.replaceAll("'", "''");

                if (s.equals("")) {

                    Toast.makeText(getBaseContext(), "Please enter the comment", Toast.LENGTH_LONG).show();

                } else {

                    User user = Utility.getuser();

                    int userid = user.getUserId();
                    Log.d("TAG", "userid " + userid);
                    // insert comment into the DB
                    dataBaseManager.insertComments(dataBaseHelper, flag, my_new_s, userid, eventornewsid);

                    // referesh the comment list
                    refreshCommentList();
                    editText.setText("");
                    // Toast.makeText(getBaseContext(),"add comment sucessfully",Toast.LENGTH_LONG).show();
                    is_update = true;

                    // If network is available then send new comment to server by calling syncDeviceToServer
                    if (Utility.isNetworkAvailable(CommentDetail.this)) {
                        // sync server to device
                        syncDeviceToServer();
                        Toast.makeText(getBaseContext(), "Please enter the comment after sysnc", Toast.LENGTH_LONG).show();
                    }

                }

            }
        });  // inner on click listener

        // code to populate data into the list view
        Commenteslist = dataBaseManager.getComment(dataBaseHelper, flag, eventornewsid);

        if (Commenteslist == null) {
            Log.d("TAG", "onCreate: null ");

        } else {
            Log.d("TAG size", "size=" + Commenteslist.size());
        }
        //Add to layout

        recList = (RecyclerView) findViewById(R.id.cardList);
        LinearLayoutManager llm = new LinearLayoutManager(getBaseContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        CommentAdapter commentAdapter = new CommentAdapter(Commenteslist, CommentDetail.this);
        recList.setAdapter(commentAdapter);


        // Vijesh code : for pull to refresh functionality by using
        // Swipe recycle view
        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                pullToRefresh();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    } // on create


    // click on add comment button and pullToRefresh event
    private void refreshCommentList(){

        Commenteslist = dataBaseManager.getComment(dataBaseHelper, flag, eventornewsid);

        if (Commenteslist == null) {
            Log.d("TAG", "onCreate: null ");
        } else {
            Log.d("TAG size", "size=" + Commenteslist.size());
        }
        //Add to layout

        /*
        RecyclerView recList = (RecyclerView) findViewById(R.id.cardList);
        LinearLayoutManager llm = new LinearLayoutManager(getBaseContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        */
        CommentAdapter commentAdapter = new CommentAdapter(Commenteslist, CommentDetail.this);
        recList.setAdapter(commentAdapter);

        swipeContainer.setRefreshing(false);

    }


    public void pullToRefresh() {
        // Send the network request to fetch the updated data
        // If network is available then sync device data on server
        if (Utility.isNetworkAvailable(CommentDetail.this)) {
            // sync server to device
            syncServerToDevice();
        }else{
            swipeContainer.setRefreshing(false);
        }

    }


    // Call API to sync device data on server
    private void syncDeviceToServer() {

        pDialog = new ProgressDialog(CommentDetail.this);
        pDialog.setMessage("Please wait...\nSyncing data to server.");
        pDialog.setCancelable(false);
        pDialog.show(); // show dialog


        JSONObject jResult = syncUtil.getDeviceData();

        Log.d("DToS", "" + jResult.toString());
        // Log.d("DToS",""+jResult.toString());

        Log.d("TESTING", "json  param list" + jResult.toString());
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, Constants.URL_DEVICE_TO_SERVER, jResult.toString(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                syncUtil.parseDeviceToServerJSONResponse(response);
                Log.d("TESTING", "json  response " + response.toString());
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss(); //dismiss the progress dialog box
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TESTING", "json error  response--" + error.getMessage());
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss(); //dismiss the progress dialog box
                }
                // showDialogMsg(error.getMessage());
                // showDialogMsg("Error : Unable to connect with server.");
            }
        });
        requestQueue.add(jor);


    }//syncDeviceToServer()


    private void syncServerToDevice() {

        pDialog = new ProgressDialog(CommentDetail.this);
        pDialog.setMessage("Please wait...\nDownloading data from server.");
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

        Log.d("ServerToDevice", "final url -" + url);

        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("TESTING", "json  response" + response.toString());
                syncUtil.parseServerToDeviceJSONResponse(response);

                if (pDialog.isShowing()) {
                    pDialog.dismiss(); //dismiss the progress dialog box
                }
                refreshCommentList();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TESTING", "error  response" + error.getMessage());
                // Log.d("TESTING", "json error  response--" + error.getMessage());
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss(); //dismiss the progress dialog box
                }
                // showDialogMsg(error.getMessage());
                // showDialogMsg("Error : Unable to connect with server.");

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
    public void onBackPressed() {

        Log.d("TAG", "onBackPressed: i am back in news and in back");
        String id = "0";

        if (flag.equals("news")) {
            id = eventornewsid;
        } else if (flag.equals("event")) {
            id = eventornewsid;
        }

        Log.d("Detail id", "id==" + id);
        Log.d("Detail", "is_comment_added=" + is_update);

        // Intent returnIntent = new Intent();
        Intent returnIntent = getIntent();
        returnIntent.putExtra("id", id);
        returnIntent.putExtra("is_comment_added", is_update);
        setResult(Activity.RESULT_OK, returnIntent);

        /*
        Intent intent = getIntent();
        intent.putExtra("Date",dateSelected);
        setResult(RESULT_OK, intent);
        finish();
        */

        // finish();

        super.onBackPressed();


    }

    @Override
    public void syncResult(boolean result, int responseCode, String msg) {

    }
}
