package fiveexceptions.com.m01sbcm.sync;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import fiveexceptions.com.m01sbcm.database.DataBaseHelper;
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


/**
 * Created by Vijesh jat on 2/9/2016.
 */
public class SQLController {
    Context context;
    DataBaseHelper dbHelper;
    SQLiteDatabase database;
    String TAG;

    public SQLController(Context context) {
        this.context = context;
        TAG = context.getClass().getName();
        dbHelper = new DataBaseHelper(context);
    }

    public List<Comment> getCommentNews() {
        List<Comment> commentList = new ArrayList<>();

        try {
            database = dbHelper.openDataBase();

            String sql = "SELECT * FROM news_comment ";

            Cursor cursor = database.rawQuery(sql, null);
            if (cursor.moveToFirst()) {

                do {
                    Comment comment = new Comment();
                    comment.setIntDeviceID(Integer.parseInt(cursor.getString(cursor.getColumnIndex("comment_id"))));
                    comment.setStrCommentText(cursor.getString(cursor.getColumnIndex("comment_discription")));
                    comment.setIntUserID(Integer.parseInt(cursor.getString(cursor.getColumnIndex("user_id"))));
                    comment.setIntEventNewsID(Integer.parseInt(cursor.getString(cursor.getColumnIndex("news_id"))));
                    comment.setIntID(Integer.parseInt(cursor.getString(cursor.getColumnIndex("server_id"))));

                    commentList.add(comment);
                    Log.d("VIJESH", "comment obj-" + comment);

                } while (cursor.moveToNext());
            }


        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ERROR", e.toString());
        } finally {
            // Closing database connection
            if (database != null) database.close();
        }
        Log.d("VIJESH","comment list-"+commentList.toString());
        return commentList;
    }
    public void saveProfileThumbnailImage(int id, Bitmap bitmap){
        // Convert the image into byte array
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        byte[] buffer=out.toByteArray();
        ContentValues values;

        try
        {
            database = dbHelper.openDataBase();
            values = new ContentValues();

            values.put("profile_image_t", buffer);
            values.put("thumb_img_sync", 1);
            // Insert Row
            database.update("profile", values, "profile_id = " + id, null);

            // Insert into database successfully.


        }
        catch (SQLiteException e)
        {
            e.printStackTrace();

        }
        finally
        {

            database.close();
            // Close database
        }

    }
    public List<Profile> getProfileThumbnailImage() {

        List<Profile> profileList = new ArrayList<>();

        try {
            database = dbHelper.openDataBase();

            String sql = "SELECT profile_id,image_t_url FROM profile where thumb_img_sync =0 ";

            Cursor cursor = database.rawQuery(sql, null);
            if (cursor.moveToFirst()) {

                do {
                    Profile profile = new Profile();

                    profile.setStrThumbnailURL(cursor.getString(cursor.getColumnIndex("image_t_url")));
                    profile.setIntID(Integer.parseInt(cursor.getString(cursor.getColumnIndex("profile_id"))));

                    profileList.add(profile);
                    Log.d("VIJESH", "comment obj-" + profile);

                } while (cursor.moveToNext());
            }


        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ERROR", e.toString());
        } finally {
            // Closing database connection
            if (database != null) database.close();
        }
        Log.d("VIJESH","comment list-"+profileList.toString());
        return profileList;
    }

    public List<News> getNewsThumbnailImage() {

        List<News> newsList = new ArrayList<>();

        try {
            database = dbHelper.openDataBase();

            String sql = "SELECT news_id,image_t_url FROM news  where thumb_img_sync = 0 and image_t_url is not null and image_t_url !=''";

            Cursor cursor = database.rawQuery(sql, null);
            if (cursor.moveToFirst()) {

                do {
                    News news = new News();

                    news.setStrThumbnailURL(cursor.getString(cursor.getColumnIndex("image_t_url")));
                    news.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex("news_id"))));

                    newsList.add(news);
                    Log.d("VIJESH", " news url-" + cursor.getString(cursor.getColumnIndex("image_t_url")));
                    Log.d("VIJESH", "news id-" + cursor.getString(cursor.getColumnIndex("news_id")));

                } while (cursor.moveToNext());
            }


        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ERROR", e.toString());
        } finally {
            // Closing database connection
            if (database != null) database.close();
        }

        return newsList;
    }

    public void saveNewsThumbnailImage(int id, Bitmap bitmap){
        // Convert the image into byte array
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        byte[] buffer=out.toByteArray();
        ContentValues values;

        try
        {
            database = dbHelper.openDataBase();
            values = new ContentValues();

            values.put("news_image_t", buffer);
            values.put("thumb_img_sync", 1);
            // Insert Row
            database.update("news", values, "news_id = " + id, null);

            // Insert into database successfully.


        }
        catch (SQLiteException e)
        {
            e.printStackTrace();

        }
        finally
        {

            if (database != null) database.close();
            // Close database
        }

    }

    public List<Event> getEventThumbnailImage() {

        List<Event> eventList = new ArrayList<>();

        try {
            database = dbHelper.openDataBase();

            String sql = "SELECT event_id,image_t_url FROM event thumb_img_sync =0 ";

            Cursor cursor = database.rawQuery(sql, null);
            if (cursor.moveToFirst()) {

                do {
                    Event event = new Event();

                    event.setStrThumbnailURL(cursor.getString(cursor.getColumnIndex("image_t_url")));
                    event.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex("event_id"))));

                    eventList.add(event);
                    Log.d("VIJESH", "comment obj-" + event);

                } while (cursor.moveToNext());
            }


        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ERROR", e.toString());
        } finally {
            // Closing database connection
            if (database != null) database.close();
        }
        Log.d("VIJESH","comment list-"+eventList.toString());
        return eventList;
    }

    public void saveEventThumbnailImage(int id, Bitmap bitmap){
        // Convert the image into byte array
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        byte[] buffer=out.toByteArray();
        ContentValues values;

        try
        {
            database = dbHelper.openDataBase();
            values = new ContentValues();

            values.put("event_image_t", buffer);
            values.put("thumb_img_sync", 1);

            // Insert Row
            database.update("event", values, "event_id = " + id, null);

            // Insert into database successfully.


        }
        catch (SQLiteException e)
        {
            e.printStackTrace();

        }
        finally
        {

            if (database != null) database.close();
            // Close database
        }

    }


    public void insertCityDetailsToDb(List<City> cityList) {


        try {
            database = dbHelper.openDataBase();
            if (cityList != null) {
                for (int i = 0; i < cityList.size(); i++) {
                    City city = cityList.get(i);
                    ContentValues values = new ContentValues(); // like map data structure
                    //         col name            values
                    values.put("id", city.getId());
                    values.put("name", city.getCityName());
                    values.put("region_id", city.getRegionId());
                    values.put("state_id", city.getStateId());

                    // Inserting Row into DB by calling insert()
                    long result = database.insert("city ", null, values);

                    Log.d("MELAWA", "DB City Response --" + result);

                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if (database != null) database.close();
        }

    } // method

    public void insertRegionDetailsToDb(List<Region> regionList) {

        try{
            database = dbHelper.openDataBase();
            if (regionList != null) {
                for (int i = 0; i < regionList.size(); i++) {
                    Region region = regionList.get(i);
                    ContentValues values = new ContentValues(); // like map data structure
                    //         col name            values
                    values.put("id", region.getId());
                    values.put("name", region.getRegionName());


                    // Inserting Row into DB by calling insert()
                    long result = database.insert("region", null, values);

                    Log.d("MELAWA", " DB Region response--" + result);

                }

            } //if
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if (database != null) database.close();
        }

    } // method

    public void insertStateDetailsToDb(List<State> stateList) {


        try{

            database = dbHelper.openDataBase();
            if (stateList != null) {
                for (int i = 0; i < stateList.size(); i++) {
                    State state = stateList.get(i);
                    ContentValues values = new ContentValues(); // like map data structure
                    //         col name            values
                    values.put("id", state.getId());
                    values.put("name", state.getStateName());


                    // Inserting Row into DB by calling insert()
                    long result = database.insert("state", null, values);

                    Log.d("MELAWA", "DB State Response --" + result);

                }

            } //if
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if (database != null) database.close();
        }

    } // method

    public void insertCastDetailsToDb(List<Cast> castList) {

        try{

            database = dbHelper.openDataBase();
            if (castList != null) {
                for (int i = 0; i < castList.size(); i++) {
                    Cast cast = castList.get(i);
                    ContentValues values = new ContentValues(); // like map data structure
                    //         col name            values
                    values.put("id", cast.getId());
                    values.put("cast_name", cast.getCastName());


                    // Inserting Row into DB by calling insert()
                    long result = database.insert("cast", null, values);

                    Log.d("MELAWA", "DB cast Response --" + result);

                }

            } //if
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if (database != null) database.close();
        }
    } // insertCastDetailsToDb method

    public void insertManglicDetailsToDb(List<Manglic> manglicList) {

        try{
            database = dbHelper.openDataBase();
            if (manglicList != null) {
                for (int i = 0; i < manglicList.size(); i++) {
                    Manglic manglic = manglicList.get(i);
                    ContentValues values = new ContentValues(); // like map data structure
                    //         col name            values
                    values.put("id", manglic.getId());
                    values.put("value", manglic.getManglicType());


                    // Inserting Row into DB by calling insert()
                    long result = database.insert("manglic_type", null, values);

                    Log.d("MELAWA", "DB manglic_type Response --" + result);

                }

            } //if
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if (database != null) database.close();
        }
    } // insertManglicDetailsToDb method

    public void insertMotherTongueDetailsToDb(List<MotherTongue> motherTongueList) {

        try{
            database = dbHelper.openDataBase();
            if (motherTongueList != null) {
                for (int i = 0; i < motherTongueList.size(); i++) {
                    MotherTongue motherTongue = motherTongueList.get(i);
                    ContentValues values = new ContentValues(); // like map data structure
                    //         col name            values
                    values.put("id", motherTongue.getId());
                    values.put("language", motherTongue.getLanguage());


                    // Inserting Row into DB by calling insert()
                    long result = database.insert("mother_tongue", null, values);

                    Log.d("MELAWA", "DB mother_tongue Response --" + result);

                }

            } //if

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if (database != null) database.close();
        }
    } // insertMotherTongueDetailsToDb method

    public void insertProfileColorDetailsToDb(List<ProfileColor> profileColorList) {

        try{
            database = dbHelper.openDataBase();
            if (profileColorList != null) {
                for (int i = 0; i < profileColorList.size(); i++) {
                    ProfileColor profileColor = profileColorList.get(i);
                    ContentValues values = new ContentValues(); // like map data structure
                    //         col name            values
                    values.put("color_id", profileColor.getId());
                    values.put("color_name", profileColor.getColorName());


                    // Inserting Row into DB by calling insert()
                    long result = database.insert("profile_color", null, values);

                    Log.d("MELAWA", "DB profile_color Response --" + result);

                }

            } //if

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if (database != null) database.close();
        }
    } // insertProfileColorDetailsToDb method

    public void insertNewsEventSourceDetailsToDb(List<NewsEventSource> newsEventSourceList) {

        try{
            database = dbHelper.openDataBase();
            if (newsEventSourceList != null) {
                for (int i = 0; i < newsEventSourceList.size(); i++) {
                    NewsEventSource eventSource = newsEventSourceList.get(i);
                    ContentValues values = new ContentValues(); // like map data structure
                    //         col name            values
                    values.put("id", eventSource.getId());
                    values.put("name", eventSource.getStrTitle());


                    // Inserting Row into DB by calling insert()
                    long result = database.insert("source", null, values);

                    Log.d("MELAWA", "DB source Response --" + result);

                }

            } //if

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if (database != null) database.close();
        }
    } // insertNewsEventSourceDetailsToDb method

    public void insertEducationStreamDetailsToDb(List<EducationField> educationFieldList) {

        try{
            database = dbHelper.openDataBase();
            if (educationFieldList != null) {
                for (int i = 0; i < educationFieldList.size(); i++) {
                    EducationField educationField = educationFieldList.get(i);
                    ContentValues values = new ContentValues(); // like map data structure
                    //         col name            values
                    values.put("edustr_Id", educationField.getId());
                    values.put("edstr_name", educationField.getEducation());


                    // Inserting Row into DB by calling insert()
                    long result = database.insert("education_stream", null, values);
                    Log.d("TESTING", "DB education_stream Response -- "+ result);
                    Log.d("MELAWA", "DB education_stream Response --" + result);

                }

            } //if
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if (database != null) database.close();
        }
    } // insertEducationFieldDetailsToDb method

    public void insertProfessionDetailsToDb(List<Profession> professionList) {

        try{
            database = dbHelper.openDataBase();
            if (professionList != null) {
                for (int i = 0; i < professionList.size(); i++) {
                    Profession profession = professionList.get(i);
                    ContentValues values = new ContentValues(); // like map data structure
                    //         col name            values
                    values.put("profession_id", profession.getId());
                    values.put("profession_name", profession.getProfName());


                    // Inserting Row into DB by calling insert()
                    long result = database.insert("profession", null, values);

                    Log.d("MELAWA", "DB profession Response --" + result);

                }

            } //if
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if (database != null) database.close();
        }
    } // insertProfessionDetailsToDb method

    public void insertEducationStatusDetailsToDb(List<EducationStatus> educationStatusList) {

        try{
            database = dbHelper.openDataBase();
            if (educationStatusList != null) {
                for (int i = 0; i < educationStatusList.size(); i++) {
                    EducationStatus educationStatus = educationStatusList.get(i);
                    ContentValues values = new ContentValues(); // like map data structure
                    //         col name            values
                    values.put("edu_id", educationStatus.getId());
                    values.put("edu_name", educationStatus.getStatus());


                    // Inserting Row into DB by calling insert()
                    long result = database.insert("education", null, values);

                    Log.d("MELAWA", "DB education Response  --" + result);

                }

            } //if

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if (database != null) database.close();
        }
    } // insertEducationStatusDetailsToDb method

    public void insertNewsCommentDetailsToDb(List<NewsComment> newsCommentList) {


        try{

            database = dbHelper.openDataBase();
            if (newsCommentList != null) {
                for (int i = 0; i < newsCommentList.size(); i++) {
                    NewsComment newsComment = newsCommentList.get(i);
                    ContentValues values = new ContentValues(); // like map data structure
                    //         col name            values
                  //  values.put("id", newsComment.getServerID());
                    values.put("server_id", newsComment.getServerID());
                    values.put("discription", newsComment.getStrCommentText());
                    values.put("user_id", newsComment.getIntUserID());
                    values.put("news_id", newsComment.getIntEventNewsID());
                    values.put("datetime", newsComment.getStrLastUpdate());//
                    values.put("user_name", newsComment.getStrUserName());

                    // Inserting Row into DB by calling insert()
                    long result = database.insert("news_comment", null, values);

                    Log.d("MELAWA", "DB news_comment Response  --" + result);

                }

            } //if

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if (database != null) database.close();
        }
    } // insertNewsCommentDetailsToDb method

    public void insertEventCommentDetailsToDb(List<EventComment> eventCommentList) {

        try{
            database = dbHelper.openDataBase();
            if (eventCommentList != null) {
                for (int i = 0; i < eventCommentList.size(); i++) {
                    EventComment eventComment = eventCommentList.get(i);
                    ContentValues values = new ContentValues(); // like map data structure
                    //         col name            values
                  //  values.put("id", eventComment.getServerID());
                    values.put("server_id", eventComment.getServerID());
                    values.put("discription", eventComment.getStrCommentText());
                    values.put("user_id", eventComment.getIntUserID());
                    values.put("event_id", eventComment.getIntEventNewsID());
                    values.put("datetime", eventComment.getStrLastUpdate());
                    values.put("user_name", eventComment.getStrUserName());


                    // Inserting Row into DB by calling insert()
                    long result = database.insert("event_comment", null, values);

                    Log.d("MELAWA", "DB event_comment Response  --" + result);

                }

            } //if

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if (database != null) database.close();
        }
    } // insertEducationStatusDetailsToDb method

    public void insertNewsLikeDetailsToDb(List<NewsLike> newsLikeList) {

        try{
            database = dbHelper.openDataBase();
            if (newsLikeList != null) {
                for (int i = 0; i < newsLikeList.size(); i++) {
                    NewsLike newsLike = newsLikeList.get(i);
                    ContentValues values = new ContentValues(); // like map data structure
                    //         col name            values
                   // values.put("id", newsLike.getIntServerID());
                    values.put("server_id", newsLike.getIntServerID());
                    values.put("user_id", newsLike.getIntUserID());
                    values.put("news_id", newsLike.getIntNewsEventID());
                    values.put("user_name", newsLike.getStrUserName());


                    // Inserting Row into DB by calling insert()
                    long result = database.insert("news_like", null, values);

                    Log.d("MELAWA", "DB news_like Response  --" + result);

                }

            } //if

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if (database != null) database.close();
        }
    } // insertNewsLikeDetailsToDb method

    public void insertEventLikeDetailsToDb(List<EventLike> eventLikeList) {

        try{
            database = dbHelper.openDataBase();
            if (eventLikeList != null) {
                for (int i = 0; i < eventLikeList.size(); i++) {
                    EventLike eventLike = eventLikeList.get(i);
                    ContentValues values = new ContentValues(); // like map data structure
                    //         col name            values
                    // values.put("id", newsLike.getIntServerID());
                    values.put("server_id", eventLike.getIntServerID());
                    values.put("user_id", eventLike.getIntUserID());
                    values.put("event_id", eventLike.getIntNewsEventID());
                    values.put("user_name", eventLike.getStrUserName());


                    // Inserting Row into DB by calling insert()
                    long result = database.insert("event_like", null, values);

                    Log.d("MELAWA", "DB event_like Response  --" + result);

                    Log.d("BABA", "DB eventLikeList  --" + eventLike.getStrUserName());
                }

            } //if

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if (database != null) database.close();
        }
    } // insertNewsLikeDetailsToDb method

    public void insertEducationStreamMappingDetailsToDb(List<EducationStreamMapping> mappingList) {

        try{
            database = dbHelper.openDataBase();
            if (mappingList != null) {
                for (int i = 0; i < mappingList.size(); i++) {
                    EducationStreamMapping mapping = mappingList.get(i);
                    ContentValues values = new ContentValues(); // like map data structure
                    //         col name            values
                    values.put("eid", mapping.getIntEducationID());
                    values.put("sid", mapping.getIntEducationStreamID());


                    // Inserting Row into DB by calling insert()
                    // long result = database.insert("education_stream_map", null, values);
                    long result = database.insertWithOnConflict("education_stream_map", null, values, SQLiteDatabase.CONFLICT_REPLACE);

                    Log.d("MELAWA", "DB education_stream_map Response  --" + result);

                }

            } //if

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if (database != null) database.close();
        }
    } // insertNewsLikeDetailsToDb method

    public void insertProfessionStreamMappingDetailsToDb(List<ProfessionStreamMapping> mappingList) {

        try{
            database = dbHelper.openDataBase();
            if (mappingList != null) {
                for (int i = 0; i < mappingList.size(); i++) {
                    ProfessionStreamMapping mapping = mappingList.get(i);
                    ContentValues values = new ContentValues(); // like map data structure
                    //         col name            values
                    values.put("prof_id", mapping.getIntProfessionID());
                    values.put("sid", mapping.getIntProfessionStreamID());

                    Log.d("prof sm insert prof_id", "-" + mapping.getIntProfessionID());
                    Log.d("prof sm insert sid", "-" + mapping.getIntProfessionStreamID());


                    // Inserting Row into DB by calling insert()
                    // long result = database.insert("profession_stream_map", null, values);
                    long result = database.insertWithOnConflict("profession_stream_map", null, values, SQLiteDatabase.CONFLICT_REPLACE);


                    Log.d("MELAWA", "DB profession_stream_map Response  --" + result);



                }

            } //if
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if (database != null) database.close();
        }
    } // insertNewsLikeDetailsToDb method

    public void insertProfessionStreamDetailsToDb(List<ProfessionField> professionFieldList) {

        try{
            database = dbHelper.openDataBase();
            if (professionFieldList != null) {
                for (int i = 0; i < professionFieldList.size(); i++) {
                    ProfessionField professionField = professionFieldList.get(i);
                    ContentValues values = new ContentValues(); // like map data structure
                    //         col name            values
                    values.put("ps_id", professionField.getIntID());
                    values.put("stream_name", professionField.getStrProfessionField());


                    // Inserting Row into DB by calling insert()
                    long result = database.insert("profession_stream", null, values);

                    Log.d("MELAWA", "DB profession_stream Response  --" + result);

                }

            } //if
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if (database != null) database.close();
        }
    } // insertProfessionFieldDetailsToDb method

    public void insertEventDetailsToDb(List<Event> eventList) {

        try{
            database = dbHelper.openDataBase();
            if (eventList != null) {
                for (int i = 0; i < eventList.size(); i++) {
                    Event event = eventList.get(i);
                    ContentValues values = new ContentValues(); // like map data structure
                    //         col name            values
                    values.put("event_id    ", event.getId());
                    values.put("event_heading", event.getStrTitle());  //event_date
                    values.put("event_date   ", event.getStrEventDate());
                    values.put("event_discription   ", event.getStrDescription());
                    values.put("event_add_date", event.getStrDateAddedOn());
                    values.put("no_of_comment", event.getIntCommentCount());
                    values.put("no_of_like", event.getIntLikeCount());
                    values.put("source_id", event.getIntSourceID());

                    values.put("effective_date", event.getStrEffectiveDate());
                    values.put("expiration_date", event.getStrExpirationDate());
                    values.put("image_url", event.getStrPhotoURL());
                    values.put("image_t_url", event.getStrThumbnailURL());
                    values.put("event_city_id", event.getIntEventCityID());
                    values.put("event_state_id", event.getIntEventStateID());
                    values.put("event_region_id", event.getIntEventRegionID());
                    values.put("last_update",event.getStrLastUpdate());
                    values.put("thumb_img_sync", 0);

                    // Inserting Row into DB by calling insert()
                    //  long result = database.insert("event", null, values);
                    long result = database.insertWithOnConflict("event", null, values, SQLiteDatabase.CONFLICT_REPLACE);

                    Log.d("MELAWA", "DB event Response  --" + result);

                    // yourRequestQueue.getCache().remove(url);

                }
        } //if

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if (database != null) database.close();
        }
    } // insertEventDetailsToDb method

    public void insertNewsDetailsToDb(List<News> newsList) {

        try{

            database = dbHelper.openDataBase();
            if (newsList != null) {
                for (int i = 0; i < newsList.size(); i++) {
                    News News = newsList.get(i);
                    ContentValues values = new ContentValues(); // like map data structure
                    //         col name            values
                    values.put("news_id", News.getId());
                    values.put("news_heading", News.getStrTitle());

                    values.put("news_discription", News.getStrDescription());
                    values.put("news_add_date", News.getStrDateAddedOn());
                    values.put("no_of_comment", News.getIntCommentCount());
                    values.put("no_of_like", News.getIntLikeCount());
                    values.put("source_id", News.getIntSourceID());
                    values.put("news_date", News.getStrEventDate());
                    values.put("effective_date", News.getStrEffectiveDate());
                    values.put("expiration_date", News.getStrExpirationDate()); //image_t_url

                    values.put("image_t_url", News.getStrThumbnailURL());
                    values.put("image_url", News.getStrPhotoURL());

                    values.put("news_city_id", News.getIntEventCityID());
                    values.put("news_state_id", News.getIntEventStateID());
                    values.put("news_region_id", News.getIntEventRegionID());
                    values.put("thumb_img_sync", 0);
                    values.put("last_update",News.getStrLastUpdate());


                    // Inserting Row into DB by calling insert()
                    //  long result = database.insert("news", null, values);
                    long result = database.insertWithOnConflict("news", null, values, SQLiteDatabase.CONFLICT_REPLACE);
                    Log.d("MELAWA", "DB event news  --" + result);

                }

                database.close(); // Closing database connection
            } //if
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if (database != null) database.close();
        }
    } // insertNewsDetailsToDb method

    public void insertProfileDetailsToDb(List<Profile> profileList) {

        try{

            database = dbHelper.openDataBase();
            if (profileList != null) {
                for (int i = 0; i < profileList.size(); i++) {
                    Profile p = profileList.get(i);
    /*
                String query ="INSERT OR REPLACE INTO profile " +
            "(profile_id, candidate_name, father_name, birth_date,birth_city_id,color_id,hight,mother_tounge," +
            "cast_id,mangalik,income,address1,address2,residence_city,residence_state,zip_cope,contact_number," +
            "image_url,region,marital_status,description,create_date,update_date,profile_status,profile_activation_date," +
            "profile_expiration_date,professionstream_id,educationstream_id,gender,education_id,profession_id," +
            "education_free_text,profession_free_text) " +
            "VALUES ('"+p.getIntID()+"'," +
            " '"+ p.getStrName()+"'," +
            " '"+p.getStrFather_name()+"'," +
            " '"+p.getStrBirth_date()+"'," +
            " '"+ p.getIntBirth_city_id()+"'," +
            " '"+p.getIntColor_id() +"'," +
            " '"+p.getStrHeight() +"'," +
            " '"+ p.getIntMother_tongue_id() +"'," +
            " '"+p.getIntCast_id() +"'," +
            " '"+p.getIntManglic_id() +"'," +

            " '"+p.getIntIncome_per_month() +"'," +
            " '"+p.getStrAddress_line1() +"'," +
            " '"+p.getStrAddress_line2() +"'," +
            " '"+ p.getIntResidence_city_id() +"'," +
            " '"+p.getIntResidence_state_id() +"'," +
            " '"+p.getStrZip_code() +"'," +

            " '"+ p.getStrContact_number() +"'," +
            " '"+ p.getStrPhoto_url() +"'," +
            " '"+ p.getIntRegion_id() +"'," +
            " '"+ p.getIntMarital_status() +"'," +
            " '"+ p.getStrDescription() +"'," +
            " '"+ p.getStrCreate_date() +"'," +

            " '"+ p.getStrUpdate_date() +"'," +
            " '"+ p.getIntStatus() +"'," +
            " '"+ p.getStrEffective_date() +"'," +
            " '"+ p.getStrExpiration_date() +"'," +
            " '"+ p.getIntProfessionStreamId() +"'," +
            " '"+ p.getIntEducationStreamId() +"'," +

            " '"+ p.getIntGender() +"'," +
            " '"+  p.getIntEducationId() +"'," +
            " '"+ p.getIntProfessionId() +"'," +
            " '"+ p.getStrEducationFreeText() +"'," +
            " '"+ p.getStrProfessionFreeText() +"'" +

            ")";
                    database.rawQuery(query,null);


    Log.d("QQ","query-"+query);
                    */

                    ContentValues values = new ContentValues(); // like map data structure
                    //         col name            values
                    values.put("profile_id", p.getIntID());
                    values.put("candidate_name", p.getStrName());
                    values.put("father_name", p.getStrFather_name());
                    values.put("birth_date", p.getStrBirth_date());
                    values.put("birth_display_date", p.getStrBirth_display_date());
                    values.put("birth_city_id", p.getIntBirth_city_id());
                    //  values.put("age", p.getA);
                    values.put("color_id", p.getIntColor_id());
                    values.put("hight", p.getStrHeight());
                    values.put("mother_tounge", p.getIntMother_tongue_id());
                    values.put("cast_id", p.getIntCast_id());
                    values.put("mangalik", p.getIntManglic_id());

                    values.put("income", p.getIntIncome_per_month());

                    values.put("address1", p.getStrAddress_line1());
                    values.put("address2", p.getStrAddress_line2());
                    values.put("residence_city_id", p.getIntResidence_city_id());
                    values.put("residence_state_id", p.getIntResidence_state_id());
                    values.put("zip_cope", p.getStrZip_code());
                    values.put("contact_number", p.getStrContact_number());
                    values.put("image_url", p.getStrPhoto_url());
                    values.put("image_t_url", p.getStrThumbnailURL());
                    values.put("region_id", p.getIntRegion_id());
                    values.put("marital_status", p.getIntMarital_status());
                    values.put("description", p.getStrDescription());
                    values.put("create_date", p.getStrCreate_date());
                    values.put("update_date", p.getStrUpdate_date());
                    values.put("profile_status", p.getIntStatus());

                    values.put("profile_activation_date", p.getStrEffective_date());
                    values.put("profile_expiration_date", p.getStrExpiration_date());
                    // values.put("profile_payment_amount", );
                    // values.put("Authority_code", );
                    //  values.put("payment_reference_number", );
                    //  values.put("profile_payment_received_date", );
                    //  values.put("profile_promotionals", );
                    values.put("pro_stream_id", p.getIntProfessionStreamId());
                    values.put("edu_stream_id", p.getIntEducationStreamId());
                    values.put("gender", p.getIntGender());
                    values.put("education_id", p.getIntEducationId());
                    values.put("profession_id", p.getIntProfessionId());
                    values.put("education_free_text", p.getStrEducationFreeText());
                    values.put("profession_free_text", p.getStrProfessionFreeText());
                    values.put("thumb_img_sync", 0);
                    // values.put("expectation", p.getStrExpectation());
                    // values.put("source", p.getStrSource());
                    values.put("sourceid", p.getIntSourceId());
                    values.put("last_update",p.getStrLast_update());

                    /*
                    if(p.getIntID() < 3){
                        Log.d("Expiration "+p.getIntID(), p.getStrExpiration_date());

                    }
                    */

                    // Inserting Row into DB by calling insert()
                    // long result = database.insert("profile", null, values);
                    database.insertWithOnConflict("profile", null, values, SQLiteDatabase.CONFLICT_REPLACE);
                    //  Log.d("JAT", "res profile --" + result);

                }


            } //if

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if (database != null) database.close();
        }
    } // insertProfileDetailsToDb method


    public void deleteEducationStatusDetailsFromDatabase() {

        try {
            database = dbHelper.openDataBase();

            database.execSQL("delete from education");
        } catch (Exception e) {
            Log.e("ERROR", "deleteEducationStatusDetailsFromDatabase " + e.getMessage());
        } finally {
            if (database != null) {

                database.close();
            }

        }//finally
    }//deleteEducationStatusDetailsFromDatabase()

    public void deleteProfessionStreamDetailsFromDatabase() {

        try {
            database = dbHelper.openDataBase();

            database.execSQL("delete from profession_stream");
        } catch (Exception e) {
            Log.e("ERROR", "deleteProfessionStreamDetailsFromDatabase " + e.getMessage());
        } finally {
            if (database != null) {

                database.close();
            }

        }//finally
    }//deleteProfessionFieldDetailsFromDatabase()


    public void deleteProfessionStreamMapFromDatabase() {

        try {
            database = dbHelper.openDataBase();

            database.execSQL("delete from profession_stream_map");
        } catch (Exception e) {
            Log.e("ERROR", "deleteProfessionStreamDetailsFromDatabase " + e.getMessage());
        } finally {
            if (database != null) {

                database.close();
            }

        }//finally
    }//deleteProfessionFieldDetailsFromDatabase()



    public void deleteProfessionDetailsFromDatabase() {

        try {
            database = dbHelper.openDataBase();

            database.execSQL("delete from profession");
        } catch (Exception e) {
            Log.e("ERROR", "deleteProfessionDetailsFromDatabase " + e.getMessage());
        } finally {
            if (database != null) {

                database.close();
            }

        }//finally
    }//deleteProfessionDetailsFromDatabase()


    public void deleteEducationStreamDetailsFromDatabase() {

        try {
            database = dbHelper.openDataBase();

            database.execSQL("delete from education_stream");
        } catch (Exception e) {
            Log.e("ERROR", "deleteEducationStreamDetailsFromDatabase " + e.getMessage());
        } finally {
            if (database != null) {

                database.close();
            }

        }//finally
    }//deleteEducationFieldDetailsFromDatabase()

    public void deleteEducationStreamDMapFromDatabase() {

        try {
            database = dbHelper.openDataBase();

            database.execSQL("delete from education_stream_map");
        } catch (Exception e) {
            Log.e("ERROR", "Delete education_stream_map  " + e.getMessage());
        } finally {
            if (database != null) {

                database.close();
            }

        }//finally
    }//deleteEducationFieldDetailsFromDatabase()

    public void deleteNewsEventSourceDetailsFromDatabase() {

        try {
            database = dbHelper.openDataBase();

            database.execSQL("delete from source");
        } catch (Exception e) {
            Log.e("ERROR", "deleteNewsEventSourceDetailsFromDatabase " + e.getMessage());
        } finally {
            if (database != null) {

                database.close();
            }

        }//finally
    }//deleteNewsEventSourceDetailsFromDatabase()

    public void deleteProfileColorDetailsFromDatabase() {

        try {
            database = dbHelper.openDataBase();

            database.execSQL("delete from profile_color");
        } catch (Exception e) {
            Log.e("ERROR", "deleteProfileColorDetailsFromDatabase " + e.getMessage());
        } finally {
            if (database != null) {

                database.close();
            }

        }//finally
    }//deleteProfileColorDetailsFromDatabase()

    public void deleteMotherTongueDetailsFromDatabase() {

        try {
            database = dbHelper.openDataBase();

            database.execSQL("delete from mother_tongue");
        } catch (Exception e) {
            Log.e("ERROR", "deleteMotherTongueDetailsFromDatabase " + e.getMessage());
        } finally {
            if (database != null) {

                database.close();
            }

        }//finally
    }//deleteManglicDetailsFromDatabase()

    public void deleteManglicDetailsFromDatabase() {

        try {
            database = dbHelper.openDataBase();

            database.execSQL("delete from manglic_type");
        } catch (Exception e) {
            Log.e("ERROR", "deleteManglicDetailsFromDatabase " + e.getMessage());
        } finally {
            if (database != null) {

                database.close();
            }

        }//finally
    }//deleteManglicDetailsFromDatabase()

    public void deleteCastDetailsFromDatabase() {

        try {
            database = dbHelper.openDataBase();

            database.execSQL("delete from cast");
        } catch (Exception e) {
            Log.e("ERROR", "deleteCastDetailsFromDatabase " + e.getMessage());
        } finally {
            if (database != null) {

                database.close();
            }

        }//finally

    } // deleteCastDetailsFromDatabase method

    public void deleteStateDetailsFromDatabase() {

        try {
            database = dbHelper.openDataBase();

            database.execSQL("delete from state");
        } catch (Exception e) {
            Log.e("ERROR", "deleteStateDetailsFromDatabase " + e.getMessage());
        } finally {
            if (database != null) {

                database.close();
            }

        }//finally

    } //method

    public void deleteCityDetailsFromDatabase() {

        try {
            database = dbHelper.openDataBase();

            database.execSQL("delete from city");
        } catch (Exception e) {
            Log.e("ERROR", "deleteCityDetailsFromDatabase " + e.getMessage());
        } finally {
            if (database != null) {

                database.close();
            }

        }//finally

    } //method

    public void deleteRegionDetailsFromDatabase() {

        try {
            database = dbHelper.openDataBase();

            database.execSQL("delete from region");
        } catch (Exception e) {
            Log.e("ERROR", "deleteRegionDetailsFromDatabase " + e.getMessage());
        } finally {
            if (database != null) {

                database.close();
            }

        }//finally

    } //method


    public boolean deleteDuplicateCommentLike() {

        database = dbHelper.openDataBase();
        boolean result = false;
        try {

            String sqlQuery1  = " DELETE from event_comment where id in(SELECT min(id) FROM event_comment where server_id  > 0 group by server_id having count(1) > 1)";
            String sqlQuery2  = " DELETE from news_comment where id in(SELECT min(id) FROM news_comment where server_id  > 0 group by server_id having count(1) > 1)";
            String sqlQuery3  = " DELETE from event_like where id in(SELECT min(id) FROM event_like where server_id  > 0 group by server_id having count(1) > 1)";
            String sqlQuery4  = " DELETE from news_like where id in(SELECT min(id) FROM news_like where server_id  > 0 group by server_id having count(1) > 1)";

            Log.d("SQL Query", "delete Duplicate Comment  " + sqlQuery1);
            database.execSQL(sqlQuery1);
            database.execSQL(sqlQuery2);
            Log.d("SQL Query", "delete Duplicate Like  " + sqlQuery1);
            database.execSQL(sqlQuery3);
            database.execSQL(sqlQuery4);

            result = true;
        }
        catch (Exception e) {
            Log.e("ERROR", "delete Duplicate CommentLike " + e.getMessage());
        } finally {
            if (database != null) {
                database.close();
            }

        }//finally


        return result;
    }


    /* public void updateTripInfo(TripInfo tripInfo) {

         database = dbHelper.openDataBase();
         ContentValues values = new ContentValues();

         values.put("trip_end_date", tripInfo.getEndTripDate());

         database.update("trip_info", values, "trip_id =" + tripInfo.getTripId(), null);
         database.close();
     }
  public String getVegetableDescription(Vegetables vegetables) {
         String vegDesc = null;
        // SQLiteDatabase mDataBase = null;

         try {
             database = dbHelper.openDataBase();

             String sql = "SELECT VegitableDetailDescription from tbl_vegetables WHERE  VegitableName = '" + vegetables.getVegetablesName() + "'";

             Cursor cursor = database.rawQuery(sql, null);
             if (cursor.moveToFirst()) {

                 do {

                     vegDesc = cursor.getString(0);

                 } while (cursor.moveToNext());
             }


         } catch (Exception e) {
             e.printStackTrace();
             Log.d("Error", e.toString());
         } finally {
             // Closing database connection
             if (database != null) database.close();
         }

 */
    public List<Comment> getCommentNewsListForServerSync() {
        List<Comment> commentList = new ArrayList<>();

        try {
            database = dbHelper.openDataBase();

            String sql = "SELECT * FROM news_comment where server_id = 0";

            Cursor cursor = database.rawQuery(sql, null);
            if (cursor.moveToFirst()) {

                do {
                    Comment comment = new Comment();
                    comment.setIntDeviceID(Integer.parseInt(cursor.getString(cursor.getColumnIndex("id"))));
                    comment.setStrCommentText(cursor.getString(cursor.getColumnIndex("discription")));
                    comment.setIntUserID(Integer.parseInt(cursor.getString(cursor.getColumnIndex("user_id"))));
                    comment.setIntEventNewsID(Integer.parseInt(cursor.getString(cursor.getColumnIndex("news_id"))));
                    comment.setIntID(Integer.parseInt(cursor.getString(cursor.getColumnIndex("server_id"))));
                    Log.d("MYTAG","coment ibj--"+comment);
                    commentList.add(comment);
                } while (cursor.moveToNext());
            }


        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ERROR", e.toString());
        } finally {
            // Closing database connection
            if (database != null) database.close();
        }
        return commentList;
    }

    public List<Comment> getCommentEventListForServerSync() {
        List<Comment> commentList = new ArrayList<>();

        try {
            database = dbHelper.openDataBase();

            String sql = "SELECT * FROM event_comment where server_id = 0";

            Cursor cursor = database.rawQuery(sql, null);
            if (cursor.moveToFirst()) {

                do {
                    Comment comment = new Comment();
                    comment.setIntDeviceID(Integer.parseInt(cursor.getString(cursor.getColumnIndex("id"))));
                    comment.setStrCommentText(cursor.getString(cursor.getColumnIndex("discription")));
                    comment.setIntUserID(Integer.parseInt(cursor.getString(cursor.getColumnIndex("user_id"))));
                    comment.setIntEventNewsID(Integer.parseInt(cursor.getString(cursor.getColumnIndex("event_id"))));
                    comment.setIntID(Integer.parseInt(cursor.getString(cursor.getColumnIndex("server_id"))));

                    commentList.add(comment);
                } while (cursor.moveToNext());
            }


        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ERROR", e.toString());
        } finally {
            // Closing database connection
            if (database != null) database.close();
        }
        return commentList;
    }

    public List<Comment> getLikeEventListForServerSync() {
        List<Comment> commentList = new ArrayList<>();

        try {
            database = dbHelper.openDataBase();

            String sql = "SELECT * FROM event_like where server_id = 0";

            Cursor cursor = database.rawQuery(sql, null);
            if (cursor.moveToFirst()) {

                do {
                    Comment comment = new Comment();
                    comment.setIntDeviceID(Integer.parseInt(cursor.getString(cursor.getColumnIndex("id"))));
                    comment.setIntUserID(Integer.parseInt(cursor.getString(cursor.getColumnIndex("user_id"))));
                    comment.setIntEventNewsID(Integer.parseInt(cursor.getString(cursor.getColumnIndex("event_id"))));
                    comment.setIntID(Integer.parseInt(cursor.getString(cursor.getColumnIndex("server_id"))));

                    commentList.add(comment);
                } while (cursor.moveToNext());
            }


        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ERROR", "getLikeEventListForServerSync--" + e.toString());
        } finally {
            // Closing database connection
            if (database != null) database.close();
        }
        return commentList;
    }//getLikeEventListForServerSync

    public List<Comment> getLikeNewsListForServerSync() {
        List<Comment> commentList = new ArrayList<>();

        try {
            database = dbHelper.openDataBase();

            String sql = "SELECT * FROM news_like where server_id = 0";

            Cursor cursor = database.rawQuery(sql, null);
            if (cursor.moveToFirst()) {

                do {
                    Comment comment = new Comment();
                    comment.setIntDeviceID(Integer.parseInt(cursor.getString(cursor.getColumnIndex("id"))));
                    comment.setIntUserID(Integer.parseInt(cursor.getString(cursor.getColumnIndex("user_id"))));
                    comment.setIntEventNewsID(Integer.parseInt(cursor.getString(cursor.getColumnIndex("news_id"))));
                    comment.setIntID(Integer.parseInt(cursor.getString(cursor.getColumnIndex("server_id"))));

                    commentList.add(comment);
                } while (cursor.moveToNext());
            }


        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ERROR", "getLikeNewsListForServerSync--" + e.toString());
        } finally {
            // Closing database connection
            if (database != null) database.close();
        }
        return commentList;
    }///getLikeNewsListForServerSync

    public void updateNewsComment(List<Comment> commentList) {
        Log.d("DATABASE", "updateNewsComment");
        Log.d("DATABASE", "size updateNewsComment-" + commentList.size());

        database = dbHelper.openDataBase();

        try {
            if (commentList != null) {
                for (int i = 0; i < commentList.size(); i++) {


                    ContentValues values = new ContentValues();
                    Comment comment = commentList.get(i);
                    values.put("server_id", comment.getIntID());

                    Log.d("DATABASE", "News IntDeviceID- " + comment.getIntDeviceID());
                    Log.d("DATABASE", "News IntID -"+comment.getIntID());


                    database.update("news_comment", values, "id = " + comment.getIntDeviceID(), null);


                }//for

            }//if

        } catch (Exception e) {
            Log.d("DATABASE", "Error news " + e.getMessage());
        }

        database.close();

    }//updateNewsComment

    public void updateEventComment(List<Comment> commentList) {
        Log.d("DATABASE", "size updateEventComment-" + commentList.size());
        try {
            if (commentList != null) {
                for (int i = 0; i < commentList.size(); i++) {

                    database = dbHelper.openDataBase();
                    ContentValues values = new ContentValues();
                    Comment comment = commentList.get(i);
                    Log.d("DATABASE", "comment IntDeviceID- " + comment.getIntDeviceID());
                    Log.d("DATABASE", "comment IntID -"+comment.getIntID());


                    values.put("server_id", comment.getIntID());

                    database.update("event_comment", values, "id = " + comment.getIntDeviceID(), null);
                    database.close();
                }//for

            }//if
        } catch (Exception e) {
            Log.d("DATABASE", "ERROR EVENT" + e.getMessage());
        }


    }//updateEventComment

    public void updateEventLike(List<Comment> commentList) {
        Log.d("DATABASE", "size updateEventLike-" + commentList.size());
        try {
            if (commentList != null) {
                for (int i = 0; i < commentList.size(); i++) {

                    database = dbHelper.openDataBase();
                    ContentValues values = new ContentValues();
                    Comment comment = commentList.get(i);
                    Log.d("DATABASE", "Like IntDeviceID- " + comment.getIntDeviceID());
                    Log.d("DATABASE", "Like IntID -"+comment.getIntID());


                    values.put("server_id", comment.getIntID());

                    database.update("event_like", values, "id = " + comment.getIntDeviceID(), null);
                    database.close();
                }//for

            }//if
        } catch (Exception e) {
            Log.d("DATABASE", "ERROR EVENT" + e.getMessage());
        }


    }//updateEventComment

    public void updateNewsLike(List<Comment> commentList) {
        Log.d("DATABASE", "size updateNewsLike-" + commentList.size());
        try {
            if (commentList != null) {
                for (int i = 0; i < commentList.size(); i++) {

                    database = dbHelper.openDataBase();
                    ContentValues values = new ContentValues();
                    Comment comment = commentList.get(i);
                    Log.d("DATABASE", "Like IntDeviceID- " + comment.getIntDeviceID());
                    Log.d("DATABASE", "Like IntID -"+comment.getIntID());


                    values.put("server_id", comment.getIntID());

                    database.update("news_like", values, "id = " + comment.getIntDeviceID(), null);
                    database.close();
                }//for

            }//if
        } catch (Exception e) {
            Log.d("DATABASE", "ERROR EVENT" + e.getMessage());
        }


    }//updateEventComment

    /*
    public List<User> getUserListForServerSync() {
        List<User> userList = new ArrayList<>();

        try {
            database = dbHelper.openDataBase();

            String sql = "SELECT * FROM user ";

            Cursor cursor = database.rawQuery(sql, null);
            if (cursor.moveToFirst()) {

                do {
                    User user = new User();
                    user.setUserName(cursor.getString(5));
                    user.setPassword(cursor.getString(1));
                    user.setEmailId(cursor.getString(3));
                    user.setDisplayName(cursor.getString(5));
                    user.setMobileNum(cursor.getString(2));
                    user.setId(cursor.getInt(0));
                    user.setUserType(cursor.getInt(4));

                    userList.add(user);
                } while (cursor.moveToNext());
            }


        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ERROR", "getUserListForServerSync--" + e.toString());
        } finally {
            // Closing database connection
            if (database != null) database.close();
        }
        return userList;
    }///getUserListForServerSync
    */

}
