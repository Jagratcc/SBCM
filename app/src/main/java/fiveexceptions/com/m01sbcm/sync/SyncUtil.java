package fiveexceptions.com.m01sbcm.sync;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import fiveexceptions.com.m01sbcm.activities.SyncInterface;
import fiveexceptions.com.m01sbcm.sync.model.Cast;
import fiveexceptions.com.m01sbcm.sync.model.City;
import fiveexceptions.com.m01sbcm.sync.model.Comment;
import fiveexceptions.com.m01sbcm.sync.model.EducationField;
import fiveexceptions.com.m01sbcm.sync.model.EducationStatus;
import fiveexceptions.com.m01sbcm.sync.model.EducationStreamMapping;
import fiveexceptions.com.m01sbcm.sync.model.Event;
import fiveexceptions.com.m01sbcm.sync.model.EventComment;
import fiveexceptions.com.m01sbcm.sync.model.EventLike;
import fiveexceptions.com.m01sbcm.sync.model.Manglic;
import fiveexceptions.com.m01sbcm.sync.model.MotherTongue;
import fiveexceptions.com.m01sbcm.sync.model.News;
import fiveexceptions.com.m01sbcm.sync.model.NewsComment;
import fiveexceptions.com.m01sbcm.sync.model.NewsEventSource;
import fiveexceptions.com.m01sbcm.sync.model.NewsLike;
import fiveexceptions.com.m01sbcm.sync.model.Profession;
import fiveexceptions.com.m01sbcm.sync.model.ProfessionField;
import fiveexceptions.com.m01sbcm.sync.model.ProfessionStreamMapping;
import fiveexceptions.com.m01sbcm.sync.model.Profile;
import fiveexceptions.com.m01sbcm.sync.model.ProfileColor;
import fiveexceptions.com.m01sbcm.sync.model.Region;
import fiveexceptions.com.m01sbcm.sync.model.State;
import fiveexceptions.com.m01sbcm.utility.Constants;
import fiveexceptions.com.m01sbcm.utility.Utility;

/**
 * Created by dell on 5/13/2016.
 */
public class SyncUtil {

    // sync process
    ProgressDialog pDialog = null;
    RequestQueue requestQueue;
    SyncUtil syncUtil;
    // AlertDialog alertDialog;
    Context context;
    SyncInterface syncInterface;

    public SyncUtil(Context context, SyncInterface syncInterface){
        // sync process

        // create volley request queue
        requestQueue = VolleySingleton.getsInstance().getmRequestQueue();
        this.context = context;
        this.syncInterface = syncInterface;
    }

    public JSONObject getDeviceData() {

        // SQLController db = new SQLController(getBaseContext());
        SQLController db = new SQLController(context);

        //////////////////////  create param value for API ////////////////////////////
        JSONObject jResult = new JSONObject();// json name
        JSONArray jArrayCommentEvent = new JSONArray();// / comment event jsonArray
        JSONArray jArrayCommentNews = new JSONArray();// / comment news jsonArray
        JSONArray jArrayLikeEvent = new JSONArray();// / like event jsonArray
        JSONArray jArrayLikeNews = new JSONArray();// / like news jsonArray

        List<Comment> commentNewsList = null;
        List<Comment> commentEventList = null;
        List<Comment> likeNewsList = null;
        List<Comment> likeEventList = null;

        try{
            jResult.put("strCommunityID", Constants.communityId);
        }catch(Exception e){

        }

        ////// get sync data from data base ////////////////////

        // get sync comment news sync list

        if (db != null) {
            commentNewsList = db.getCommentNewsListForServerSync();
            Log.d("TAG", "size commentNewsList-" + commentNewsList.size());
        }

        // get sync comment event sync list

        if (db != null) {
            commentEventList = db.getCommentEventListForServerSync();
            Log.d("TAG", "size commentEventList-" + commentEventList.size());
        }

        // get sync like  news sync list

        if (db != null) {
            likeNewsList = db.getLikeNewsListForServerSync();
            Log.d("TAG", "size likeNewsList-" + likeNewsList.size());
        }

        // get sync like event sync list

        if (db != null) {
            likeEventList = db.getLikeEventListForServerSync();
            Log.d("TAG", "size likeEventList-" + likeEventList.size());
        }

        // iterate like news list
        if (likeNewsList != null) {
            for (int i = 0; i < likeNewsList.size(); i++) {
                JSONObject jGroup = new JSONObject();// /sub Object

                try {
                    jGroup.put("intID", likeNewsList.get(i).getIntID());
                    jGroup.put("intDeviceID", likeNewsList.get(i).getIntDeviceID());
                    jGroup.put("intUserID", likeNewsList.get(i).getIntUserID());
                    jGroup.put("intNewsEventID", likeNewsList.get(i).getIntEventNewsID());

                    jArrayLikeNews.put(jGroup);

                    // /itemDetail Name is JsonArray Name
                    jResult.put("newsLikeList", jArrayLikeNews);

                } catch (JSONException e) {
                    Log.e("ERROR", "newsLikeList-" + e.getMessage());

                }
            }//for

        }//if

        // iterate like likeEventList
        if (likeEventList != null) {
            for (int i = 0; i < likeEventList.size(); i++) {
                JSONObject jGroup = new JSONObject();// /sub Object

                try {
                    jGroup.put("intID", likeEventList.get(i).getIntID());
                    jGroup.put("intDeviceID", likeEventList.get(i).getIntDeviceID());
                    jGroup.put("intUserID", likeEventList.get(i).getIntUserID());
                    jGroup.put("intNewsEventID", likeEventList.get(i).getIntEventNewsID());

                    jArrayLikeEvent.put(jGroup);

                    // /itemDetail Name is JsonArray Name
                    jResult.put("eventLikeList", jArrayLikeEvent);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("ERROR", "eventLikeList-" + e.getMessage());
                }
            }//for

        }//if

        // iterate comment news list
        if (commentNewsList != null) {
            for (int i = 0; i < commentNewsList.size(); i++) {
                JSONObject jGroup = new JSONObject();// /sub Object

                try {
                    jGroup.put("intID", commentNewsList.get(i).getIntID());
                    jGroup.put("intDeviceID", commentNewsList.get(i).getIntDeviceID());
                    jGroup.put("intUserID", commentNewsList.get(i).getIntUserID());
                    jGroup.put("strCommentText", commentNewsList.get(i).getStrCommentText());
                    jGroup.put("intEventNewsID", commentNewsList.get(i).getIntEventNewsID());

                    jArrayCommentNews.put(jGroup);

                    // /itemDetail Name is JsonArray Name
                    jResult.put("newsCommentList", jArrayCommentNews);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("ERROR", "newsCommentList-" + e.getMessage());
                }
            }//for

        }//if

        // iterate commentEventList
        if (commentEventList != null) {
            for (int i = 0; i < commentEventList.size(); i++) {
                JSONObject jGroup = new JSONObject();// /sub Object

                try {
                    jGroup.put("intID", commentEventList.get(i).getIntID());
                    jGroup.put("intDeviceID", commentEventList.get(i).getIntDeviceID());
                    jGroup.put("intUserID", commentEventList.get(i).getIntUserID());
                    jGroup.put("strCommentText", commentEventList.get(i).getStrCommentText());
                    jGroup.put("intEventNewsID", commentEventList.get(i).getIntEventNewsID());

                    jArrayCommentEvent.put(jGroup);

                    // /itemDetail Name is JsonArray Name
                    jResult.put("eventCommentList", jArrayCommentEvent);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("ERROR", "eventCommentList-" + e.getMessage());
                }
            }//for

        }//if

        return jResult;
    }
    /**
     * Method that parse json response
     * check  user is valid or not

     * @return Return true if user is valid
     */

    public int parseLoginJSONResponse(JSONObject response) {

        Boolean result = false;
        int userId = 0;
        try {

            if (response != null) {

                String responseCode = response.getString("responseCode");
                String message = response.getString("msg");
                String validUser = response.getString("validUser");
                String strUserId = response.getString("intUserID");

                if (responseCode.equals("1") && validUser.equals("true")) {
                    result = true;
                    Log.d("jsonResponse", "intUserId: " + strUserId);
                    Log.d("jsonResponse", "validUser: " + validUser);
                    Log.d("jsonResponse", "responseCode: " + responseCode);
                    Log.d("jsonResponse", "msg: " + message);

                    try{

                        userId = Integer.parseInt(strUserId);

                    }catch(Exception e){

                    }

                }

            }//if

        } catch (Exception e) {
            Log.e("ERROR", "login error response" + e.getMessage());
            result = false;
            Log.e("ERROR", "login error " + e.toString());
        }
        return userId;
    }//parseLoginJSONResponse



    public void parseDeviceToServerJSONResponse(JSONObject response) {
        if (response == null || response.length() == 0) {
            Log.d("TESTING", "json  response null in pstdsr");
            return;
        }

        //////// get response from api and store/ update to data base //////////////////////


        try {
            // SQLController db = new SQLController(getBaseContext());
            SQLController db = new SQLController(context);
            // db = new SQLController(getBaseContext());
            if (response != null) {
                String responseCode = response.getString("responseCode");
                String message = response.getString("msg");

                Log.d("jsonResponse", "responseCode: " + responseCode);
                Log.d("jsonResponse", "msg: " + message);


                // If responseCode is 1 or message is Success then parse json

                // checking response code nd message

                if (responseCode.equals("1") && message.equals("Success")) {

                    // parse newsCommentIdMapping list from json
                    JSONArray jsonNewsCommentIdMappingArray = null;
                    JSONArray jsonEventCommentIdMappingArray = null;
                    JSONArray jsonNewsLikeIdMappingArray = null;
                    JSONArray jsonEventLikeIdMappingArray = null;

                    try{
                        jsonNewsCommentIdMappingArray = response.getJSONArray("newsCommentIdMapping");
                    }catch(Exception e){
                          Log.e("ERROR", "jsonNewsCommentIdMappingArray parse device to server  " + e.toString());
                    }

                    if (jsonNewsCommentIdMappingArray != null) {
                        Log.d("jsonResponse", "newsCommentIdMapping: ");

                        List<Comment> commentList = new ArrayList<>();

                        for (int i = 0; i < jsonNewsCommentIdMappingArray.length(); i++) {

                            JSONObject c = jsonNewsCommentIdMappingArray.getJSONObject(i);

                            // create newsComment list
                            Comment comment = new Comment();


                            try {
                                comment.setIntID(Integer.parseInt(c.getString("intServerObjectID")));
                                comment.setIntDeviceID(Integer.parseInt(c.getString("intDeviceObjectID")));
                            } catch (NumberFormatException e) {
                                Log.e("ERROR", "educationFieldList" + e.getMessage());
                            }
                            commentList.add(comment);
                            Log.d("jsonResponse", "intDeviceObjectID: " + c.getString("intDeviceObjectID"));
                            Log.d("jsonResponse", "intServerObjectID: " + c.getString("intServerObjectID"));


                        } // for
                        // update data base records with new server id
                        if (db != null) {

                            db.updateNewsComment(commentList);
                        }

                    }//if

                    // parse eventCommentIdMapping list from json
                    try{
                        jsonEventCommentIdMappingArray = response.getJSONArray("eventCommentIdMapping");
                    }catch(Exception e){
                        Log.e("ERROR", "jsonEventCommentIdMappingArray parse device to server  " + e.toString());
                    }
                    if (jsonEventCommentIdMappingArray != null) {
                        Log.d("jsonResponse", "eventCommentIdMapping: ");

                        List<Comment> commentList = new ArrayList<>();

                        for (int i = 0; i < jsonEventCommentIdMappingArray.length(); i++) {

                            JSONObject c = jsonEventCommentIdMappingArray.getJSONObject(i);

                            // create educationFieldList

                            // create newsComment list
                            Comment comment = new Comment();


                            try {
                                comment.setIntID(Integer.parseInt(c.getString("intServerObjectID")));
                                comment.setIntDeviceID(Integer.parseInt(c.getString("intDeviceObjectID")));
                            } catch (NumberFormatException e) {
                                Log.e("ERROR", "educationFieldList" + e.getMessage());
                            }

                            commentList.add(comment);
                            Log.d("jsonResponse", "intDeviceObjectID: " + c.getString("intDeviceObjectID"));
                            Log.d("jsonResponse", "intServerObjectID: " + c.getString("intServerObjectID"));


                        } // for
                        // update data base records with new server id
                        if (db != null) {

                            db.updateEventComment(commentList);
                        }
                    }//if

                    // parse newsLikeIdMapping list from json
                    try{
                        jsonNewsLikeIdMappingArray = response.getJSONArray("newsLikeIdMapping");
                    }catch(Exception e){
                        Log.e("ERROR", "jsonNewsLikeIdMappingArray parse device to server  " + e.toString());
                    }
                    if (jsonNewsLikeIdMappingArray != null) {
                        Log.d("jsonResponse", "newsLikeIdMapping: ");
                        // delete old record from DB

                        List<Comment> commentList = new ArrayList<>();

                        for (int i = 0; i < jsonNewsLikeIdMappingArray.length(); i++) {

                            JSONObject c = jsonNewsLikeIdMappingArray.getJSONObject(i);

                            // create educationFieldList

                            // create newsComment list
                            Comment comment = new Comment();


                            try {
                                comment.setIntID(Integer.parseInt(c.getString("intServerObjectID")));
                                comment.setIntDeviceID(Integer.parseInt(c.getString("intDeviceObjectID")));
                            } catch (NumberFormatException e) {
                                Log.e("ERROR", "educationFieldList" + e.getMessage());
                            }

                            commentList.add(comment);
                            Log.d("jsonResponse", "intDeviceObjectID: " + c.getString("intDeviceObjectID"));
                            Log.d("jsonResponse", "intServerObjectID: " + c.getString("intServerObjectID"));


                        } // for

                        // update data base records with new server id
                        if (db != null) {

                            db.updateNewsLike(commentList);
                        }
                    }//if

                    // parse eventLikeIdMapping list from json
                    try{
                        jsonEventLikeIdMappingArray = response.getJSONArray("eventLikeIdMapping");
                    }catch(Exception e){
                        Log.e("ERROR", "jsonEventLikeIdMappingArray parse device to server  " + e.toString());
                    }
                    if (jsonEventLikeIdMappingArray != null) {
                        Log.d("jsonResponse", "eventLikeIdMapping: ");

                        List<Comment> commentList = new ArrayList<>();

                        for (int i = 0; i < jsonEventLikeIdMappingArray.length(); i++) {

                            JSONObject c = jsonEventLikeIdMappingArray.getJSONObject(i);

                            // create educationFieldList

                            // create newsComment list
                            Comment comment = new Comment();


                            try {
                                comment.setIntID(Integer.parseInt(c.getString("intServerObjectID")));
                                comment.setIntDeviceID(Integer.parseInt(c.getString("intDeviceObjectID")));
                            } catch (NumberFormatException e) {
                                Log.e("ERROR", "educationFieldList" + e.getMessage());
                            }

                            commentList.add(comment);
                            Log.d("jsonResponse", "intDeviceObjectID: " + c.getString("intDeviceObjectID"));
                            Log.d("jsonResponse", "intServerObjectID: " + c.getString("intServerObjectID"));


                        } // for

                        // update data base records with new server id
                        if (db != null) {

                            db.updateEventLike(commentList);
                        }
                    }//if

                } // if

            }//if

        } catch (Exception e) {
            Log.e("ERROR", "parse device to server  " + e.toString());
        }


    }


    // -------------------------------------------- Server to Device -----------------------------------------------------


    public void parseServerToDeviceJSONResponse(JSONObject response) {

        if (response == null || response.length() == 0) {
            Log.d("TESTING", "json  response null in pstdsr");
            return;
        }

        SQLController db =  new SQLController(context);

        try {

            if (response != null) {
                Log.d("TESTING", "json  response not null in pstdsr");
                String responseCode = response.getString("responseCode");
                String message = response.getString("msg");
                String serverSyncTime = response.getString("serverSyncTime");
                Log.d("TESTING", "responseCode: " + responseCode);
                Log.d("TESTING", "msg: " + message);
                Log.d("TESTING", "serverSyncTime: " + serverSyncTime);


                // If responseCode is 1 or message is Success then parse json

                // checking response code nd message

                if (responseCode.equals("1") && message.equals("Success")) {

                    // Save last sync time and date in shared preferences
                    Utility.savePreferences("last_sync_time",serverSyncTime);

                    // parse educationStreamList from json

                    JSONArray jsonEducationStreamArray = response.getJSONArray("educationStreamList");

                    if (jsonEducationStreamArray != null && jsonEducationStreamArray.length() > 0) {
                        // delete old record from DB
                        // db = new SQLController(getBaseContext());

                        if (db != null) {

                            db.deleteEducationStreamDetailsFromDatabase();
                        }


                        List<EducationField> educationFieldList = new ArrayList<>();

                        for (int i = 0; i < jsonEducationStreamArray.length(); i++) {

                            JSONObject c = jsonEducationStreamArray.getJSONObject(i);

                            // create educationFieldList
                            EducationField educationField = new EducationField();
                            try {
                                educationField.setId(Integer.parseInt(c.getString("id")));
                            } catch (NumberFormatException e) {
                                Log.e("ERROR", "educationFieldList" + e.getMessage());
                            }

                            educationField.setEducation(c.getString("stream"));
                            educationFieldList.add(educationField);

                            Log.d("jsonResponse", "id: " + c.getString("id"));
                            Log.d("jsonResponse", "stream: " + c.getString("stream"));


                        } // for

                        //   store educationField to data base
                        if (db != null) {

                            db.insertEducationStreamDetailsToDb(educationFieldList);

                        }


                    }//if


                    // parse professionList from json
                    JSONArray jsonProfessionArray = response.getJSONArray("professionList");

                    if (jsonProfessionArray != null && jsonProfessionArray.length() > 0) {
                        // delete old records from DB
                        if (db != null) {
                            db.deleteProfessionDetailsFromDatabase();
                        }

                        List<Profession> professionList = new ArrayList<>();

                        for (int i = 0; i < jsonProfessionArray.length(); i++) {

                            JSONObject c = jsonProfessionArray.getJSONObject(i);

                            // create profession list
                            Profession profession = new Profession();
                            try {
                                profession.setId(Integer.parseInt(c.getString("id")));
                            } catch (NumberFormatException e) {
                                Log.e("ERROR", "professionList" + e.getMessage());
                            }

                            profession.setProfName(c.getString("profName"));
                            professionList.add(profession);
                            Log.d("prof jsonResponse", "id: " + c.getString("id"));
                            Log.d("prof jsonResponse", "profName: " + c.getString("profName"));


                        } // for
                        //  store profession List to data base
                        if (db != null) {
                            db.insertProfessionDetailsToDb(professionList);
                        }

                    }

                    // parse castList from json
                    JSONArray jsonCastArray = response.getJSONArray("castList");
                    if (jsonCastArray != null && jsonCastArray.length() > 0) {
                        // delete old record from cast table
                        if (db != null) {
                            db.deleteCastDetailsFromDatabase();
                        }

                        List<Cast> castList = new ArrayList<>();
                        for (int i = 0; i < jsonCastArray.length(); i++) {
                            Cast cast = new Cast();
                            JSONObject c = jsonCastArray.getJSONObject(i);

                            // create cast list
                            try {
                                cast.setId(Integer.parseInt(c.getString("id")));
                            } catch (NumberFormatException e) {
                                Log.e("ERROR", "castlist" + e.getMessage());
                            }

                            cast.setCastName(c.getString("castName"));
                            castList.add(cast);
                            Log.d("jsonResponse", "cast_id: " + c.getString("id"));
                            Log.d("jsonResponse", "cast_name: " + c.getString("castName"));

                        } // for
                        // store cast details to data base
                        if (db != null) {
                            db.insertCastDetailsToDb(castList);
                        }

                    }//if


                    // parse educationList from json
                    JSONArray jsonEducationStatusArray = response.getJSONArray("educationList");

                    if (jsonEducationStatusArray != null && jsonEducationStatusArray.length() > 0) {

                        // delete old records from DB
                        if (db != null) {
                            db.deleteEducationStatusDetailsFromDatabase();
                        }

                        List<EducationStatus> educationStatusList = new ArrayList<>();


                        for (int i = 0; i < jsonEducationStatusArray.length(); i++) {

                            JSONObject c = jsonEducationStatusArray.getJSONObject(i);

                            // create EducationStatus list
                            EducationStatus educationStatus = new EducationStatus();
                            try {
                                educationStatus.setId(Integer.parseInt(c.getString("id")));
                            } catch (NumberFormatException e) {
                                Log.e("ERROR", "EducationStatus list" + e.getMessage());
                            }

                            educationStatus.setStatus(c.getString("education"));
                            educationStatusList.add(educationStatus);
                            Log.d("jsonResponse", "education_id: " + c.getString("id"));
                            Log.d("jsonResponse", "education_name: " + c.getString("education"));

                        } // for

                        //  store educationStatus to data base
                        if (db != null) {
                            db.insertEducationStatusDetailsToDb(educationStatusList);
                        }

                    }


                    // parse manglicList from json
                    JSONArray jsonManglicArray = response.getJSONArray("manglicList");

                    if (jsonManglicArray != null && jsonManglicArray.length() > 0) {
                        // delete old records from database
                        if (db != null) {
                            db.deleteManglicDetailsFromDatabase();
                        }
                        List<Manglic> manglicList = new ArrayList<>();

                        for (int i = 0; i < jsonManglicArray.length(); i++) {

                            JSONObject c = jsonManglicArray.getJSONObject(i);

                            // create manglic list
                            Manglic manglic = new Manglic();
                            try {
                                manglic.setId(Integer.parseInt(c.getString("id")));
                            } catch (NumberFormatException e) {
                                Log.e("ERROR", "castlist" + e.getMessage());
                            }

                            manglic.setManglicType(c.getString("manglicType"));

                            manglicList.add(manglic);
                            Log.d("jsonResponse", "manglic_id: " + c.getString("id"));
                            Log.d("jsonResponse", "manglicType: " + c.getString("manglicType"));

                        } // for
                        // store manglic details  to data base
                        if (db != null) {
                            db.insertManglicDetailsToDb(manglicList);
                        }
                    } //if


                    // parse motherTongueList from json
                    JSONArray jsonMotherTongueArray = response.getJSONArray("motherTongueList");

                    if (jsonMotherTongueArray != null && jsonMotherTongueArray.length() > 0) {

                        // delete old records from data base
                        if (db != null) {
                            db.deleteMotherTongueDetailsFromDatabase();
                        }

                        List<MotherTongue> motherTongueList = new ArrayList<>();

                        for (int i = 0; i < jsonMotherTongueArray.length(); i++) {

                            JSONObject c = jsonMotherTongueArray.getJSONObject(i);

                            // create MotherTongue list
                            MotherTongue motherTongue = new MotherTongue();
                            try {
                                motherTongue.setId(Integer.parseInt(c.getString("id")));
                            } catch (NumberFormatException e) {
                                Log.e("ERROR", "castlist" + e.getMessage());
                            }

                            motherTongue.setLanguage(c.getString("language"));
                            motherTongueList.add(motherTongue);
                            Log.d("jsonResponse", "id: " + c.getString("id"));
                            Log.d("jsonResponse", "language: " + c.getString("language"));

                        } // for
                        // store mother tongue details  to data base
                        if (db != null) {
                            db.insertMotherTongueDetailsToDb(motherTongueList);
                        }

                    } //if


                    // parse profileColorList from json
                    JSONArray jsonProfileColorArray = response.getJSONArray("profileColorList");
                    if (jsonProfileColorArray != null && jsonProfileColorArray.length() > 0) {
                        //delete old records from DB

                        if (db != null) {
                            db.deleteProfileColorDetailsFromDatabase();
                        }
                        List<ProfileColor> profileColorList = new ArrayList<>();

                        for (int i = 0; i < jsonProfileColorArray.length(); i++) {

                            JSONObject c = jsonProfileColorArray.getJSONObject(i);

                            // create profile color list
                            ProfileColor profileColor = new ProfileColor();
                            try {
                                profileColor.setId(Integer.parseInt(c.getString("id")));
                            } catch (NumberFormatException e) {
                                Log.e("ERROR", "profileColorList" + e.getMessage());
                            }

                            profileColor.setColorName(c.getString("colorName"));
                            profileColorList.add(profileColor);
                            Log.d("jsonResponse", "id: " + c.getString("id"));
                            Log.d("jsonResponse", "colorName: " + c.getString("colorName"));

                        } // for

                        // store profile Color details to data base
                        if (db != null) {
                            db.insertProfileColorDetailsToDb(profileColorList);
                        }

                    }///if

                    // parse cityList from json
                    JSONArray jsonCityArray = response.getJSONArray("cityList");
                    List<City> cities = new ArrayList<>();

                    if (jsonCityArray != null && jsonCityArray.length() > 0) {
                        //delete old records from city table
                        if (db != null) {
                            db.deleteCityDetailsFromDatabase();
                        }


                        for (int i = 0; i < jsonCityArray.length(); i++) {

                            JSONObject c = jsonCityArray.getJSONObject(i);
                            City city = new City();
                            // create city list

                            city.setCityName(c.getString("cityName"));
                            try {
                                city.setId(Integer.parseInt(c.getString("id")));
                                city.setRegionId(Integer.parseInt(c.getString("regionId")));
                                city.setStateId(Integer.parseInt(c.getString("stateId")));

                            } catch (NumberFormatException e) {
                                Log.e("ERROR", "no format ex at cityList" + e.getMessage());
                            }
                            cities.add(city);

                            Log.d("jsonResponse", "id: " + c.getString("id"));
                            Log.d("jsonResponse", "cityName: " + c.getString("cityName"));
                            Log.d("jsonResponse", "regionId: " + c.getString("regionId"));
                            Log.d("jsonResponse", "stateId: " + c.getString("stateId"));
                            Log.d("jsonResponse", "strLastUpdate: " + c.getString("strLastUpdate"));

                        } // for
                        // store city details to data base
                        if (db != null) {
                            db.insertCityDetailsToDb(cities);
                        }
                    }//if

                    // parse regionList from json
                    JSONArray jsonRegionArray = response.getJSONArray("regionList");
                    List<Region> regionList = new ArrayList<>();

                    if (jsonRegionArray != null && jsonRegionArray.length() > 0) {
                        //delete old records from region table
                        if (db != null) {
                            db.deleteRegionDetailsFromDatabase();
                        }


                        for (int i = 0; i < jsonRegionArray.length(); i++) {
                            Region region = new Region();

                            JSONObject c = jsonRegionArray.getJSONObject(i);

                            // create region list
                            try {
                                region.setId(Integer.parseInt(c.getString("id")));
                            } catch (NumberFormatException e) {
                                Log.e("ERROR", "regionList" + e.getMessage());
                            }

                            region.setRegionName(c.getString("regionName"));
                            regionList.add(region);
                            Log.d("jsonResponse", "id: " + c.getString("id"));
                            Log.d("jsonResponse", "regionName: " + c.getString("regionName"));
                            Log.d("jsonResponse", "strLastUpdate: " + c.getString("strLastUpdate"));

                        } // for
                        // store region details to data base
                        if (db != null) {
                            db.insertRegionDetailsToDb(regionList);
                        }

                    }//if


                    // parse stateList from json
                    JSONArray jsonStateArray = response.getJSONArray("stateList");

                    if (jsonStateArray != null && jsonStateArray.length() > 0) {
                        List<State> stateList = new ArrayList<>();
                        // delete old records from state table
                        if (db != null) {
                            db.deleteStateDetailsFromDatabase();
                        }

                        for (int i = 0; i < jsonStateArray.length(); i++) {

                            JSONObject c = jsonStateArray.getJSONObject(i);

                            // create state list
                            State state = new State();
                            try {
                                state.setId(Integer.parseInt(c.getString("id")));
                            } catch (NumberFormatException e) {
                                Log.e("ERROR", "stateList-" + e.getMessage());
                            }

                            state.setStateName(c.getString("stateName"));
                            stateList.add(state);
                            Log.d("jsonResponse", "id: " + c.getString("id"));
                            Log.d("jsonResponse", "stateName: " + c.getString("stateName"));
                            Log.d("jsonResponse", "strLastUpdate: " + c.getString("strLastUpdate"));

                        } // for
                        // store state details to data base
                        if (db != null) {
                            db.insertStateDetailsToDb(stateList);
                        }

                    }//if


                    // parse newsList from json
                    JSONArray jsonNewsArray = response.getJSONArray("newsList");

                    if (jsonNewsArray != null && jsonNewsArray.length() > 0) {
                        List<News> newsList = new ArrayList<>();

                        for (int i = 0; i < jsonNewsArray.length(); i++) {

                            JSONObject c = jsonNewsArray.getJSONObject(i);

                            // create news list
                            News news = new News();
                            try {

                                news.setId(Integer.parseInt(c.getString("id")));
                                news.setIntEventCityID(Integer.parseInt(c.getString("intCityID")));
                                news.setIntEventStateID(Integer.parseInt(c.getString("intStateID")));
                                news.setIntEventRegionID(Integer.parseInt(c.getString("intRegionID")));
                                news.setIntLikeCount(Integer.parseInt(c.getString("intLikeCount")));
                                news.setIntCommentCount(Integer.parseInt(c.getString("intCommentCount")));
                                news.setIntSourceID(Integer.parseInt(c.getString("intSourceID")));

                            } catch (NumberFormatException e) {
                                Log.e("ERROR", "eventList-" + e.getMessage());
                            }

                            news.setStrTitle(c.getString("strTitle"));
                            news.setStrDescription(c.getString("strDescription"));
                            news.setStrPhotoURL(c.getString("strImageURL"));
                            news.setStrThumbnailURL(c.getString("strThumbnailURL"));
                            news.setStrDateAddedOn(c.getString("strDateAddedOn"));
                            news.setStrEventDate(c.getString("strEventDate"));
                            news.setStrEffectiveDate(c.getString("strEffectiveDate"));
                            news.setStrExpirationDate(c.getString("strExpirationDate"));
                            news.setStrLastUpdate(c.getString("strLastUpdate"));

                            newsList.add(news);

                            Log.d("jsonResponse", "id: " + c.getString("id"));
                            Log.d("jsonResponse", "strTitle: " + c.getString("strTitle"));
                            Log.d("jsonResponse", "intTyep: " + c.getString("intTyep"));
                            Log.d("jsonResponse", "strDescription: " + c.getString("strDescription"));
                            // Log.d("jsonResponse", "strPhotoURL: " + c.getString("strPhotoURL"));
                            Log.d("jsonResponse", "intEventCityID: " + c.getString("intCityID"));
                            Log.d("jsonResponse", "intEventStateID: " + c.getString("intStateID"));
                            Log.d("jsonResponse", "strDateAddedOn: " + c.getString("strDateAddedOn"));
                            Log.d("jsonResponse", "intLikeCount: " + c.getString("intLikeCount"));
                            Log.d("jsonResponse", "intCommentCount: " + c.getString("intCommentCount"));
                            Log.d("jsonResponse", "strEffectiveDate: " + c.getString("strEffectiveDate"));
                            Log.d("jsonResponse", "strExpirationDate: " + c.getString("strExpirationDate"));
                            Log.d("jsonResponse", "intAuthCode: " + c.getString("intAuthCode"));
                            Log.d("jsonResponse", "intPaymentRefNo: " + c.getString("intPaymentRefNo"));
                            Log.d("jsonResponse", "strLastUpdate: " + c.getString("strLastUpdate"));
                            Log.d("jsonResponse", "intSourceID: " + c.getString("intSourceID"));

                        } // for
                        // store newsList to data base
                        if (db != null) {
                            db.insertNewsDetailsToDb(newsList);
                        }
                    }//if

                    // parse eventList from json
                    JSONArray jsonEventArray = response.getJSONArray("eventList");
                    if (jsonEventArray != null && jsonEventArray.length() > 0) {
                        List<Event> eventList = new ArrayList<>();
                        for (int i = 0; i < jsonEventArray.length(); i++) {

                            JSONObject c = jsonEventArray.getJSONObject(i);

                            // create event list
                            Event event = new Event();

                            try {
                                event.setId(Integer.parseInt(c.getString("id")));
                                event.setIntEventCityID(Integer.parseInt(c.getString("intCityID")));
                                event.setIntEventStateID(Integer.parseInt(c.getString("intStateID")));
                                event.setIntEventRegionID(Integer.parseInt(c.getString("intRegionID")));
                                event.setIntLikeCount(Integer.parseInt(c.getString("intLikeCount")));
                                event.setIntCommentCount(Integer.parseInt(c.getString("intCommentCount")));
                                event.setIntSourceID(Integer.parseInt(c.getString("intSourceID")));
                            } catch (NumberFormatException e) {
                                Log.e("ERROR", "eventList-" + e.getMessage());
                            }

                            event.setStrTitle(c.getString("strTitle"));
                            event.setStrDescription(c.getString("strDescription"));
                            event.setStrPhotoURL(c.getString("strImageURL")); //strThumbnailURL
                            event.setStrThumbnailURL(c.getString("strThumbnailURL"));
                            event.setStrDateAddedOn(c.getString("strDateAddedOn"));
                            event.setStrEffectiveDate(c.getString("strEffectiveDate"));
                            event.setStrExpirationDate(c.getString("strExpirationDate"));
                            event.setStrLastUpdate(c.getString("strLastUpdate"));
                            event.setStrEventDate(c.getString("strEventDate"));
                            eventList.add(event);

                            Log.d("jsonResponse", "id: " + c.getString("id"));
                            Log.d("jsonResponse", "strTitle: " + c.getString("strTitle"));
                            Log.d("jsonResponse", "intTyep: " + c.getString("intTyep"));
                            Log.d("jsonResponse", "strDescription: " + c.getString("strDescription"));
                            // Log.d("jsonResponse", "strPhotoURL: " + c.getString("strPhotoURL"));
                            Log.d("jsonResponse", "intEventCityID: " + c.getString("intCityID"));
                            Log.d("jsonResponse", "intEventStateID: " + c.getString("intStateID"));
                            Log.d("jsonResponse", "strDateAddedOn: " + c.getString("strDateAddedOn"));
                            Log.d("jsonResponse", "strEventDate: " + c.getString("strEventDate"));
                            Log.d("jsonResponse", "intLikeCount: " + c.getString("intLikeCount"));
                            Log.d("jsonResponse", "intCommentCount: " + c.getString("intCommentCount"));
                            Log.d("jsonResponse", "strEffectiveDate: " + c.getString("strEffectiveDate"));
                            Log.d("jsonResponse", "strExpirationDate: " + c.getString("strExpirationDate"));
                            Log.d("jsonResponse", "intAuthCode: " + c.getString("intAuthCode"));
                            Log.d("jsonResponse", "intPaymentRefNo: " + c.getString("intPaymentRefNo"));
                            Log.d("jsonResponse", "strLastUpdate: " + c.getString("strLastUpdate"));
                            Log.d("jsonResponse", "intSourceID: " + c.getString("intSourceID"));

                        } // for
                        // and store it to data base
                        if (db != null) {
                            db.insertEventDetailsToDb(eventList);
                        }
                    }//if


                    // parse newsEventSourceList from json
                    JSONArray jsonNewsEventSourceArray = response.getJSONArray("newsEventSourceList");
                    if (jsonNewsEventSourceArray != null && jsonNewsEventSourceArray.length() > 0) {
                        // delete old records from DB
                        if (db != null) {
                            db.deleteNewsEventSourceDetailsFromDatabase();
                        }


                        List<NewsEventSource> newsEventSourceList = new ArrayList<>();

                        for (int i = 0; i < jsonNewsEventSourceArray.length(); i++) {

                            JSONObject c = jsonNewsEventSourceArray.getJSONObject(i);

                            // create newsEventSourceList
                            NewsEventSource eventSource = new NewsEventSource();
                            try {
                                eventSource.setId(Integer.parseInt(c.getString("id")));
                            } catch (NumberFormatException e) {
                                Log.e("ERROR", "stateList-" + e.getMessage());
                            }

                            eventSource.setStrTitle(c.getString("strTitle"));
                            newsEventSourceList.add(eventSource);
                            Log.d("jsonResponse", "id: " + c.getString("id"));
                            Log.d("jsonResponse", "strTitle: " + c.getString("strTitle"));
                            Log.d("jsonResponse", "strLastUpdate: " + c.getString("strLastUpdate"));

                        } // for
                        // store NewsEventSource to data base
                        if (db != null) {
                            db.insertNewsEventSourceDetailsToDb(newsEventSourceList);
                        }

                    }//if

                    // parse newsCommentList from json
                    JSONArray jsonNewsCommentArray = response.getJSONArray("newsCommentList");
                    if (jsonNewsCommentArray != null && jsonNewsCommentArray.length() > 0) {
                        List<NewsComment> newsCommentList = new ArrayList<>();

                        for (int i = 0; i < jsonNewsCommentArray.length(); i++) {

                            JSONObject c = jsonNewsCommentArray.getJSONObject(i);

                            // create newsCommentList
                            NewsComment newsComment = new NewsComment();
                            try {
                                newsComment.setIntUserID(Integer.parseInt(c.getString("intUserID")));
                                newsComment.setServerID(Integer.parseInt(c.getString("intID")));
                                newsComment.setIntEventNewsID(Integer.parseInt(c.getString("intEventNewsID")));
                            } catch (NumberFormatException e) {
                                Log.e("ERROR", "newsCommentList-" + e.getMessage());
                            }
                            newsComment.setStrUserName(c.getString("strUserName"));
                            newsComment.setStrCommentText(c.getString("strCommentText"));
                            newsComment.setStrLastUpdate(c.getString("strLastUpdate"));
                            newsCommentList.add(newsComment);

                            Log.d("jsonResponse", "intID: " + c.getString("intID"));
                            Log.d("jsonResponse", "intEventNewsID: " + c.getString("intEventNewsID"));
                            Log.d("jsonResponse", "strCommentText: " + c.getString("strCommentText"));
                            Log.d("jsonResponse", "intUserID: " + c.getString("intUserID"));
                            Log.d("jsonResponse", "intDeviceID: " + c.getString("intDeviceID"));
                            Log.d("jsonResponse", "strLastUpdate: " + c.getString("strLastUpdate"));

                        } // for
                        //   store newsComment to data base

                        if (db != null) {
                            db.insertNewsCommentDetailsToDb(newsCommentList);
                        }

                    } //if


                    // parse eventCommentList from json
                    JSONArray jsonEventCommentArray = response.getJSONArray("eventCommentList");
                    if (jsonEventCommentArray != null && jsonEventCommentArray.length() > 0) {
                        List<EventComment> eventCommentList = new ArrayList<>();

                        for (int i = 0; i < jsonEventCommentArray.length(); i++) {

                            JSONObject c = jsonEventCommentArray.getJSONObject(i);

                            // create eventCommentList
                            EventComment eventComment = new EventComment();
                            try {
                                eventComment.setIntUserID(Integer.parseInt(c.getString("intUserID")));
                                eventComment.setServerID(Integer.parseInt(c.getString("intID")));
                                eventComment.setIntEventNewsID(Integer.parseInt(c.getString("intEventNewsID")));
                            } catch (NumberFormatException e) {
                                Log.e("ERROR", "eventCommentList -" + e.getMessage());
                            }
                            eventComment.setStrUserName(c.getString("strUserName"));
                            eventComment.setStrCommentText(c.getString("strCommentText"));
                            eventComment.setStrLastUpdate(c.getString("strLastUpdate"));

                            eventCommentList.add(eventComment);

                            Log.d("jsonResponse", "intID: " + c.getString("intID"));
                            Log.d("jsonResponse", "intEventNewsID: " + c.getString("intEventNewsID"));
                            Log.d("jsonResponse", "strCommentText: " + c.getString("strCommentText"));
                            Log.d("jsonResponse", "intUserID: " + c.getString("intUserID"));
                            Log.d("jsonResponse", "intDeviceID: " + c.getString("intDeviceID"));
                            Log.d("jsonResponse", "strLastUpdate: " + c.getString("strLastUpdate"));

                        } // for

                        //  store eventComment to data base
                        if (db != null) {
                            Log.d("LIST", "responseList-" + eventCommentList);
                            db.insertEventCommentDetailsToDb(eventCommentList);
                        }


                    }//if

                    // parse news likeList from json

                    JSONArray jsonNewsLikeArray = response.getJSONArray("newsLikeList");
                    if (jsonNewsLikeArray != null && jsonNewsLikeArray.length() > 0) {

                        List<NewsLike> newsLikeList = new ArrayList<>();

                        for (int i = 0; i < jsonNewsLikeArray.length(); i++) {

                            JSONObject c = jsonNewsLikeArray.getJSONObject(i);

                            // create newsLikeList
                            NewsLike newsLike = new NewsLike();
                            try {
                                newsLike.setIntServerID(Integer.parseInt(c.getString("intID")));
                                newsLike.setIntUserID(Integer.parseInt(c.getString("intUserID")));
                                newsLike.setIntDeviceID(Integer.parseInt(c.getString("intDeviceID")));
                                newsLike.setIntNewsEventID(Integer.parseInt(c.getString("intNewsEventID")));

                            } catch (NumberFormatException e) {
                                Log.e("ERROR", "newsLikeList -" + e.getMessage());
                            }
                            newsLike.setStrUserName(c.getString("strUserName"));
                            newsLike.setStrLastUpdate(c.getString("strLastUpdate"));
                            newsLikeList.add(newsLike);

                            Log.d("jsonResponse", "intID: " + c.getString("intID"));
                            Log.d("jsonResponse", "intNewsEventID: " + c.getString("intNewsEventID"));
                            Log.d("jsonResponse", "intUserID: " + c.getString("intUserID"));
                            Log.d("jsonResponse", "intDeviceID: " + c.getString("intDeviceID"));
                            Log.d("jsonResponse", "strLastUpdate: " + c.getString("strLastUpdate"));

                        } // for
                        //  store newsLikeList to data base
                        if (db != null) {
                            db.insertNewsLikeDetailsToDb(newsLikeList);
                        }

                    }//if

                    // parse eventLikeList from json

                    JSONArray jsonEventLikeArray = response.getJSONArray("eventLikeList");

                    if (jsonEventLikeArray != null && jsonEventLikeArray.length() > 0) {
                        List<EventLike> eventLikeList = new ArrayList<>();

                        for (int i = 0; i < jsonEventLikeArray.length(); i++) {

                            JSONObject c = jsonEventLikeArray.getJSONObject(i);

                            // create eventLikeList and store it to data base
                            EventLike eventLike = new EventLike();
                            try {
                                eventLike.setIntNewsEventID(Integer.parseInt(c.getString("intNewsEventID")));
                                eventLike.setIntDeviceID(Integer.parseInt(c.getString("intDeviceID")));
                                eventLike.setIntUserID(Integer.parseInt(c.getString("intUserID")));
                                eventLike.setIntServerID(Integer.parseInt(c.getString("intID")));
                            } catch (NumberFormatException e) {

                                Log.e("ERROR", "newsLikeList -" + e.getMessage());
                            }
                            eventLike.setStrUserName(c.getString("strUserName"));
                            eventLike.setStrLastUpdate(c.getString("strLastUpdate"));

                            eventLikeList.add(eventLike);
                            Log.d("jsonResponse", "intID: " + c.getString("intID"));
                            Log.d("jsonResponse", "intNewsEventID: " + c.getString("intNewsEventID"));
                            Log.d("jsonResponse", "intUserID: " + c.getString("intUserID"));
                            Log.d("jsonResponse", "intDeviceID: " + c.getString("intDeviceID"));
                            Log.d("jsonResponse", "strLastUpdate: " + c.getString("strLastUpdate"));

                        } // for

                        //  store eventLikeList to data base
                        if (db != null) {
                            Log.d("LIST", "responseList-" + eventLikeList);
                            db.insertEventLikeDetailsToDb(eventLikeList);
                        }

                    } // if

                    // parse professionStreamList from json

                    JSONArray jsonProfessionFieldArray = response.getJSONArray("professionStreamList");

                    if (jsonProfessionFieldArray != null && jsonProfessionFieldArray.length() > 0) {

                        // delete old records from DB
                        if (db != null) {
                            db.deleteProfessionStreamDetailsFromDatabase();
                        }

                        List<ProfessionField> professionFieldList = new ArrayList<>();

                        for (int i = 0; i < jsonProfessionFieldArray.length(); i++) {

                            JSONObject c = jsonProfessionFieldArray.getJSONObject(i);

                            // create professionFieldList
                            ProfessionField professionField = new ProfessionField();

                            try {
                                professionField.setIntID(Integer.parseInt(c.getString("intID")));

                            } catch (NumberFormatException e) {

                                Log.e("ERROR", "professionField -" + e.getMessage());
                            }
                            professionField.setStrProfessionField(c.getString("strProfessionStream"));
                            professionField.setStrLastUpdate(c.getString("strLastUpdate"));

                            professionFieldList.add(professionField);
                            Log.d("jsonResponse", "intID: " + c.getString("intID"));

                            Log.d("jsonResponse", "strProfessionStream: " + c.getString("strProfessionStream"));
                            Log.d("jsonResponse", "strLastUpdate: " + c.getString("strLastUpdate"));

                        } // for

                        //  store ProfessionStreamDetails to data base
                        if (db != null) {
                            db.insertProfessionStreamDetailsToDb(professionFieldList);

                        }

                    } // if

                    // parse profile List from json

                    JSONArray jsonProfileArray = response.getJSONArray("profileList");

                    if (jsonProfileArray != null && jsonProfileArray.length() > 0) {


                        List<Profile> profileList = new ArrayList<>();

                        for (int i = 0; i < jsonProfileArray.length(); i++) {

                            JSONObject c = jsonProfileArray.getJSONObject(i);

                            // create profile List
                            Profile p = new Profile();
                            try {
                                p.setIntGender(Integer.parseInt(c.getString("intGender")));
                                p.setIntID(Integer.parseInt(c.getString("intID")));
                                p.setIntMother_tongue_id(Integer.parseInt(c.getString("intMother_tongue_id")));
                                p.setIntCast_id(Integer.parseInt(c.getString("intCast_id")));
                                p.setIntManglic_id(Integer.parseInt(c.getString("intManglic_id")));
                                p.setIntBirth_city_id(Integer.parseInt(c.getString("intBirth_city_id")));
                                p.setIntColor_id(Integer.parseInt(c.getString("intColor_id")));

                                p.setIntEducationId(Integer.parseInt(c.getString("intEducation_id")));
                                p.setIntEducationStreamId(Integer.parseInt(c.getString("intEducation_stream_id")));

                                p.setIntIncome_per_month(Integer.parseInt(c.getString("intIncome_per_month")));

                                p.setIntProfessionId(Integer.parseInt(c.getString("intProfession_id")));
                                p.setIntProfessionStreamId(Integer.parseInt(c.getString("intProfession_stream_id")));

                                p.setIntResidence_city_id(Integer.parseInt(c.getString("intResidence_city_id")));
                                p.setIntResidence_state_id(Integer.parseInt(c.getString("intResidence_state_id")));
                                p.setIntRegion_id(Integer.parseInt(c.getString("intRegion_id")));
                                p.setIntMarital_status(Integer.parseInt(c.getString("intMarital_status")));
                                p.setIntStatus(Integer.parseInt(c.getString("intStatus")));
                                p.setIntPayment_amout(Integer.parseInt(c.getString("intPayment_amout")));
                                p.setIntAuthority_code(Integer.parseInt(c.getString("intAuthority_code")));
                                p.setIntPromotion(Integer.parseInt(c.getString("intPromotion")));
                            } catch (NumberFormatException e) {

                                Log.e("ERROR", "professionField -" + e.getMessage());
                            }

                            p.setStrName(c.getString("strName"));
                            p.setStrFather_name(c.getString("strFather_name"));
                            p.setStrHeight(c.getString("strHeight"));
                            p.setStrBirth_date(c.getString("strBirth_date"));
                            p.setStrBirth_display_date(c.getString("strBirth_display_date"));

                            p.setStrEducationFreeText(c.getString("strEducation_free_text"));
                            p.setStrProfessionFreeText(c.getString("strProfession_free_text"));

                            p.setStrAddress_line1(c.getString("strAddress_line1"));
                            p.setStrAddress_line2(c.getString("strAddress_line2"));
                            p.setStrZip_code(c.getString("strZip_code"));
                            p.setStrContact_number(c.getString("strContact_number"));
                            p.setStrPhoto_url(c.getString("strImageURL"));
                            p.setStrThumbnailURL(c.getString("strThumbnailURL"));
                            p.setStrDescription(c.getString("strDescription"));
                            p.setStrCreate_date(c.getString("strCreate_date"));
                            p.setStrUpdate_date(c.getString("strUpdate_date"));
                            p.setStrEffective_date(c.getString("strEffective_date"));
                            p.setStrExpiration_date(c.getString("strExpiration_date"));
                            p.setStrPayment_ref_num(c.getString("strPayment_ref_num"));
                            p.setStrPayment_received_date(c.getString("strPayment_received_date"));
                            p.setStrLast_update(c.getString("strLast_update"));
                            // p.setIntSourceId(c.getString("strSource"));
                            p.setIntSourceId(Integer.parseInt(c.getString("intSource_id")));
                            // p.setStrExpectation(c.getString("strExpectation"));

                            // add profile obj to profile list
                            profileList.add(p);

                            Log.d("jsonResponse", "intResidence_city_id: " + c.getString("intResidence_city_id"));
                            Log.d("jsonResponse", "intResidence_state_id: " + c.getString("intResidence_state_id"));
                            Log.d("jsonResponse", "strZip_code: " + c.getString("strZip_code"));
                            Log.d("jsonResponse", "strContact_number: " + c.getString("strContact_number"));
                            //  Log.d("jsonResponse", "strPhoto_url: " + c.getString("strPhoto_url"));
                            Log.d("jsonResponse", "intRegion_id: " + c.getString("intRegion_id"));
                            Log.d("jsonResponse", "intMarital_status: " + c.getString("intMarital_status"));
                            Log.d("jsonResponse", "strDescription: " + c.getString("strDescription"));
                            Log.d("jsonResponse", "strCreate_date: " + c.getString("strCreate_date"));
                            Log.d("jsonResponse", "strUpdate_date: " + c.getString("strUpdate_date"));
                            Log.d("jsonResponse", "intStatus: " + c.getString("intStatus"));
                            Log.d("jsonResponse", "strEffective_date: " + c.getString("strEffective_date"));
                            Log.d("jsonResponse", "strExpiration_date: " + c.getString("strExpiration_date"));
                            Log.d("jsonResponse", "intPayment_amout: " + c.getString("intPayment_amout"));
                            Log.d("jsonResponse", "intAuthority_code: " + c.getString("intAuthority_code"));
                            Log.d("jsonResponse", "strPayment_ref_num: " + c.getString("strPayment_ref_num"));
                            Log.d("jsonResponse", "strPayment_received_date: " + c.getString("strPayment_received_date"));
                            Log.d("jsonResponse", "intPromotion: " + c.getString("intPromotion"));
                            Log.d("jsonResponse", "strLast_update: " + c.getString("strLast_update"));
                            //-----------------------------------------------
                            Log.d("jsonResponse", "intID: " + c.getString("intID"));
                            Log.d("jsonResponse", "strName: " + c.getString("strName"));
                            Log.d("jsonResponse", "strFather_name: " + c.getString("strFather_name"));
                            Log.d("jsonResponse", "intGender: " + c.getString("intGender"));
                            Log.d("jsonResponse", "strHeight: " + c.getString("strHeight"));
                            Log.d("jsonResponse", "intMother_tongue_id: " + c.getString("intMother_tongue_id"));
                            Log.d("jsonResponse", "intCast_id: " + c.getString("intCast_id"));
                            Log.d("jsonResponse", "intManglic_id: " + c.getString("intManglic_id"));
                            Log.d("jsonResponse", "strBirth_date: " + c.getString("strBirth_date"));
                            Log.d("jsonResponse", "strBirth_date: " + c.getString("strBirth_display_date"));
                            Log.d("jsonResponse", "intBirth_city_id: " + c.getString("intBirth_city_id"));
                            Log.d("jsonResponse", "intColor_id: " + c.getString("intColor_id"));


                            Log.d("jsonResponse", "intIncome_per_month: " + c.getString("intIncome_per_month"));
                            Log.d("jsonResponse", "intProfession_id: " + c.getString("intProfession_id"));
                            //  Log.d("jsonResponse", "intProfession_field_id: " + c.getString("intProfession_field_id"));
                            //    Log.d("jsonResponse", "strProfession: " + c.getString("strProfession"));
                            Log.d("jsonResponse", "strAddress_line1: " + c.getString("strAddress_line1"));
                            Log.d("jsonResponse", "strAddress_line2: " + c.getString("strAddress_line2"));
                        } // for

                        //  store profile List to data base
                        if (db != null) {
                            db.insertProfileDetailsToDb(profileList);

                        }

                    } // if

                    // parse educationStreamMappingList from json

                    JSONArray jsonEducationStreamMappingArray = response.getJSONArray("educationStreamMappingList");

                    if (jsonEducationStreamMappingArray != null && jsonEducationStreamMappingArray.length() > 0) {


                        // delete old records from DB
                        if (db != null) {
                            db.deleteEducationStreamDMapFromDatabase();
                        }

                        List<EducationStreamMapping> esmList = new ArrayList<>();

                        for (int i = 0; i < jsonEducationStreamMappingArray.length(); i++) {

                            JSONObject c = jsonEducationStreamMappingArray.getJSONObject(i);

                            // create eventLikeList and store it to data base
                            EducationStreamMapping mapping = new EducationStreamMapping();
                            try {
                                mapping.setIntID(Integer.parseInt(c.getString("intID")));
                                mapping.setIntEducationID(Integer.parseInt(c.getString("intEducationID")));
                                mapping.setIntEducationStreamID(Integer.parseInt(c.getString("intEducationStreamID")));
                            } catch (NumberFormatException e) {

                                Log.e("ERROR", "newsLikeList -" + e.getMessage());
                            }
                            mapping.setStrLastUpdate(c.getString("strLastUpdate"));
                            esmList.add(mapping);
                            Log.d("jsonResponse", "intID: " + c.getString("intID"));
                            Log.d("jsonResponse", "intEducationID: " + c.getString("intEducationID"));
                            Log.d("jsonResponse", "intEducationStreamID: " + c.getString("intEducationStreamID"));
                            Log.d("jsonResponse", "strLastUpdate: " + c.getString("strLastUpdate"));

                        } // for

                        //  store eventLikeList to data base
                        if (db != null) {
                            db.insertEducationStreamMappingDetailsToDb(esmList);
                        }

                    } // if

                    // parse professionStreamMappingList from json

                    JSONArray jsonProfessionStreamMappingArray = response.getJSONArray("professionStreamMappingList");

                    if (jsonProfessionStreamMappingArray != null && jsonProfessionStreamMappingArray.length() > 0) {

                        // delete old records from DB
                        if (db != null) {
                            db.deleteProfessionStreamMapFromDatabase();
                        }

                        List<ProfessionStreamMapping> mappingList = new ArrayList<>();

                        for (int i = 0; i < jsonProfessionStreamMappingArray.length(); i++) {

                            JSONObject c = jsonProfessionStreamMappingArray.getJSONObject(i);

                            // create ProfessionStreamMapping
                            ProfessionStreamMapping mapping = new ProfessionStreamMapping();
                            try {
                                mapping.setIntID(Integer.parseInt(c.getString("intID")));
                                mapping.setIntProfessionID(Integer.parseInt(c.getString("intProfessionID")));
                                mapping.setIntProfessionStreamID(Integer.parseInt(c.getString("intProfessionStreamID")));
                            } catch (NumberFormatException e) {

                                Log.e("ERROR", "newsLikeList -" + e.getMessage());
                            }
                            mapping.setStrLastUpdate(c.getString("strLastUpdate"));
                            mappingList.add(mapping);
                            Log.d("prof sm jsonResponse", "intID: " + c.getString("intID"));
                            Log.d("prof sm jsonResponse", "intProfessionID: " + c.getString("intProfessionID"));
                            Log.d("prof sm jsonResponse", "intProfessionStreamID: " + c.getString("intProfessionStreamID"));
                            Log.d("prof sm jsonResponse", "strLastUpdate: " + c.getString("strLastUpdate"));

                        } // for

                        //  store ProfessionStreamMapping to data base
                        if (db != null) {
                            db.insertProfessionStreamMappingDetailsToDb(mappingList);
                        }

                    } // if inner

                    Log.d("delete duplicate", "true");
                    // delete duplicate records
                    if (db != null) {
                        db.deleteDuplicateCommentLike();
                    }

                } // if midle

            }//if main

        } catch (Exception e) {
            Log.e("ERROR", "InBG() " + e.toString());
        }

    }

    public void syncServerToDevice() {

        pDialog = new ProgressDialog(context);
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
               parseServerToDeviceJSONResponse(response);

                // download thumbnail from server
                // downloadNewsThumbnailImage();
                // downloadEventThumbnailImage();
                // downloadProfileThumbnailImage();


                if (pDialog.isShowing()) {
                    pDialog.dismiss(); //dismiss the progress dialog box
                }
                // thumbnail

                syncInterface.syncResult(true, 1, "Success");

               /*//todo use when we call above method from login or registration
                Intent I = new Intent(context, MainActivity.class);
                context.startActivity(I);
               // finish()*/

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
                showDialogMsg("Error : Unable to connect with server.");

            }
        });

        int socketTimeout = 15000;   //15 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jor.setRetryPolicy(policy);

        requestQueue.add(jor);

    }//syncServerToDevice()

    public void  syncDeviceToServer() {

        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Pls wait...");
        pDialog.setCancelable(false);
        pDialog.show(); // show dialog

        JSONObject jResult = getDeviceData();

        Log.d("DToS", "" + jResult.toString());        // Log.d("DToS",""+jResult.toString());

        Log.d("TESTING", "json  param list" + jResult.toString());
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, Constants.URL_DEVICE_TO_SERVER, jResult.toString(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
              parseDeviceToServerJSONResponse(response);
                Log.d("TESTING", "json  response " + response.toString());
                if (pDialog.isShowing()) {
                    pDialog.dismiss(); //dismiss the progress dialog box
                }

                if (Utility.isNetworkAvailable(context)) {
                    // sync server to device
                    syncServerToDevice();
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
                showDialogMsg("Error : Unable to connect with server.");
            }
        });

        int socketTimeout = 15000;   //15 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jor.setRetryPolicy(policy);

        requestQueue.add(jor);


    }//syncDeviceToServer()

    public void showDialogMsg(String msg) {

        /*
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
        */
        syncInterface.syncResult(false, 0, msg);

    }

}
