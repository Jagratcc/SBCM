package fiveexceptions.com.m01sbcm.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import fiveexceptions.com.m01sbcm.model.Address;
import fiveexceptions.com.m01sbcm.model.Comment;
import fiveexceptions.com.m01sbcm.model.Education;
import fiveexceptions.com.m01sbcm.model.EducationStream;
import fiveexceptions.com.m01sbcm.model.Event;
import fiveexceptions.com.m01sbcm.model.News;
import fiveexceptions.com.m01sbcm.model.Profession;
import fiveexceptions.com.m01sbcm.model.ProfessionStream;
import fiveexceptions.com.m01sbcm.model.Profile;
import fiveexceptions.com.m01sbcm.model.Region;
import fiveexceptions.com.m01sbcm.model.Source;
import fiveexceptions.com.m01sbcm.model.State;
import fiveexceptions.com.m01sbcm.model.User;
import fiveexceptions.com.m01sbcm.utility.Utility;

/**
 * Created by amit on 14/4/16.
 */
public class DataBaseManager {



    private List<Event> listevent;
    private List<Profile> listprofile;
    private List<Region> listregion;
    private List<Education> listeducation;
    private List<Profession> listprofession;
    private List<ProfessionStream> listprofessionstream;
    private List<EducationStream> listeducationstream;
    private SQLiteDatabase sqLiteDatabase;
    private void openDatabase(DataBaseHelper db) {

        try {
            sqLiteDatabase = db.openDataBase();
        } catch (SQLException e) {
            Log.e("Error", "Error while Opening Database");
            e.printStackTrace();
            // trying to close the DB if already open
            closeDatabase(db);
        }

    }

    private void closeDatabase(DataBaseHelper db) {

        try {
            db.close();
        } catch (SQLException e) {
            Log.e("Error", "Error while Closing Database");
            e.printStackTrace();
        }

    }


    public List<News> getNews(DataBaseHelper db, String whereCondition) {

        /*
        String query="select e.*, \n" +
            "case when temp.comment_count is null then 0 else temp.comment_count end c_count, \n" +
            "case when temp2.like_count is null then 0 else temp2.like_count end l_count \n" +
            "from news e  left join (SELECT count(1) comment_count, news_id from commentnews group by news_id ) temp on  e.news_id = temp.news_id left join (SELECT count(1) like_count, news_id from like_news group by news_id ) temp2 on  e.news_id = temp2.news_id   ";
        */

        // start from 0 : 18 + 1 + 1
        String query = "select n.*, c.name, s.name, " +
                " case when temp.comment_count is null then 0 else temp.comment_count end c_count, " +
                " case when temp2.like_count is null then 0 else temp2.like_count end l_count " +
                " from news n join city c on n.news_city_id = c.id" +
                " left join source s on s.id = n.source_id " +
                " left join (SELECT count(1) comment_count, news_id from news_comment group by news_id ) temp " +
                " on  n.news_id = temp.news_id " +
                " left join (SELECT count(1) like_count, news_id from news_like group by news_id ) temp2 " +
                " on  n.news_id = temp2.news_id " +
                " where expiration_date >= date() "
                + whereCondition+" ORDER BY last_update DESC";


        List<News> listnews = new ArrayList<News>();
        openDatabase(db);

        try {

            //defining cursor and select all rows from table named "student"
            Cursor c = sqLiteDatabase.rawQuery(query, null);
            Drawable d = null;


            Log.d("total record", c.getCount() + "");
            Log.d("total record", "query=" + query);


            //put cursor on the first position
            c.moveToFirst();
            while (!c.isAfterLast()) {
                //   Toast.makeText(getActivity(),c.getString(0)+ " "+c.getString(1),Toast.LENGTH_LONG).show();
                // Log.d("total record", c.getString(0) + " " + c.getString(2));

                // thumbnail image
                // byte[] imageByteArray = c.getBlob(4);
                // d = Utility.getDrawable(imageByteArray);

                /*
                News(int newsId, String newsHeading, int newsType, String newsDate,
                    String newsPlace, String newsDiscription, Drawable newsImage,
                    String newsaddon, int noOfComment, int noOfLike, newssource)
                */
                News news = new News(c.getInt(0), c.getString(1), c.getInt(2), c.getString(11),
                        c.getString(21), c.getString(3), c.getString(6), c.getString(7),
                        c.getString(12), c.getInt(23), c.getInt(24), c.getString(22));

                //fetching all records from cursor until reaching last record
                //closing cursor}
                listnews.add(news);

                c.moveToNext();
            }
            c.close();
        } catch (Exception e) {
            Log.e("Error", "error in cursor");
            e.printStackTrace();
        }


        closeDatabase(db);

        return (listnews);


    }

    // Save lats sync time and date in db
    public void saveSyncDateTime(Context context, String serverSyncTime) {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        openDatabase(dataBaseHelper);

        try {

            ContentValues values = new ContentValues();

            values.put("value", serverSyncTime);
            values.put("key", "serverSyncTime");

            // Inserting Row into DB by calling insert()
            long result = sqLiteDatabase.insert("tbl_key_value", null, values);

        } catch (SQLiteException e) {
            Log.e("ERROR", "saveSyncDateTime errror - " + e.getMessage());
            e.printStackTrace();

        } finally {
            // Close database connection
            if (sqLiteDatabase != null) {
                sqLiteDatabase.close();
            }

        } // finally

    } // saveSyncDateTime

    // Get last sync date and time
    public String getSyncDateTime(Context context){

        String lastSyncDate="";
        try {
            DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
            openDatabase(dataBaseHelper);

            String sql = "SELECT value  FROM tbl_key_value";

            Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
            if (cursor.moveToFirst()) {

                do {
                    lastSyncDate =cursor.getString(cursor.getColumnIndex("value"));
                } while (cursor.moveToNext());
            }


        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ERROR", "getSyncDateTime error - " + e.toString());
        } finally {
            // Closing database connection
            if (sqLiteDatabase != null) sqLiteDatabase.close();
        }

        return lastSyncDate;
    }

    public List<Event> getEvent(DataBaseHelper db, String whereCondition)

    {

        listevent = new ArrayList<>();

        // start from zero : 18 + 1 + 1
        String query = "select e.*, c.name, s.name, " +
                " case when temp.comment_count is null then 0 else temp.comment_count end c_count, " +
                " case when temp2.like_count is null then 0 else temp2.like_count end l_count " +
                " from event e join city c on e.event_city_id = c.id" +
                " left join source s on s.id = e.source_id " +
                " left join (SELECT count(1) comment_count, event_id from event_comment group by event_id ) temp " +
                " on  e.event_id = temp.event_id " +
                " left join (SELECT count(1) like_count, event_id from event_like group by event_id ) temp2 " +
                " on  e.event_id = temp2.event_id " +
                " where expiration_date >= date()  "
                + whereCondition+" ORDER BY last_update DESC";

        Log.d("total record", "query = " + query);

        openDatabase(db);
        //defining cursor and select all rows from table named "student"

        try {

            Cursor c = sqLiteDatabase.rawQuery(query, null);
            Drawable d = null;

            Log.d("total record", c.getCount() + "");

            //put cursor on the first position
            c.moveToFirst();
            while (!c.isAfterLast()) {
                //   Toast.makeText(getActivity(),c.getString(0)+ " "+c.getString(1),Toast.LENGTH_LONG).show();
                Log.d("total record", c.getString(0) + " " + c.getString(2));

                // byte[] imageByteArray = c.getBlob(4);
                // d = Utility.getDrawable(imageByteArray);


                 /*
                 public Event(int eventId, String eventHeading, int eventType, String eventDate,
                     String eventPlace, String eventDiscription, Drawable eventImage,
                     String eventaddon, int noOfComment, int noOfLike,int eventsource)
                 */




                Event event = new Event(c.getInt(0), c.getString(1), c.getInt(2), c.getString(11),
                        c.getString(21), c.getString(3), c.getString(6), c.getString(7),
                        c.getString(12), c.getInt(23), c.getInt(24), c.getString(22));

                //fetching all records from cursor until reaching last record
                //closing cursor}
                listevent.add(event);

                c.moveToNext();
            }
            c.close();
        } catch (Exception e) {
            Log.e("Error", "error in cursor");
            e.printStackTrace();
        }


        closeDatabase(db);

        return listevent;


    }


    public List<Profile> getprofile(DataBaseHelper db, String whereCondition) {


        listprofile = new ArrayList<>();

        openDatabase(db);

        //defining cursor and select all rows from table named "student"

        String query = "Select candidate_name,  father_name, birth_date,  hight, income, address1, address2,  " +
                " zip_cope, contact_number, image_url,  description, future_prefrence, create_date, update_date, " +
                " profile_status, profile_activation_date, profile_expiration_date, profile_promotionals,  " +
                " c1.name as birth_city, color_name,  language, cast_name,  m.value," +
                " edu_name,  edstr_name,  education_free_text,  profession_name,  stream_name, " +
                " profession_free_text, c2.name as res_city,  " +
                " st.name as res_state,  rg.name  as region_name,  marital_status,  gender, profile_id, fav_flag, image_t_url,source,description,s.name as sourcename"+
                " from profile p" +
                " left join source s on s.id= p.sourceid" +
                " left join city c1  on c1.id =   p.birth_city_id" +
                " left join city c2  on c2.id =   p.residence_city_id" +
                " left join profile_color pc  on pc.color_id =   p.color_id" +
                " left join mother_tongue mt  on mt.id =   p.mother_tounge" +
                " left join cast ca on ca.id =   p.cast_id" +
                " left join manglic_type m on m.id = p.mangalik" +
                " left join education e on e.edu_id = p.education_id" +
                " left join education_stream es on es.edustr_id = p.edu_stream_id" +
                " left join profession pro on pro.profession_id = p.profession_id" +
                " left join profession_stream ps on ps.ps_id = p.profession_id" +
                " left join state st on st.id = p.residence_state_id" +
                " left join region rg on rg.id = p.region_id" +
                " where profile_expiration_date >= date() " + whereCondition+" ORDER BY last_update DESC";

        Log.d("profile db query", query + "");


        try {


            Cursor c = sqLiteDatabase.rawQuery(query, null);
            Drawable d = null;


            Log.d("profile db total", c.getCount() + "");


            //put cursor on the first position
            c.moveToFirst();
            while (!c.isAfterLast()) {
                //   Toast.makeText(getActivity(),c.getString(0)+ " "+c.getString(1),Toast.LENGTH_LONG).show();
                Log.d("profile db record", c.getString(0) + " " + c.getString(2));

//                byte[] imageByteArray = c.getBlob(23);


                //  d= Utility.getDrawable(imageByteArray);

                Profile profile = new Profile();
                profile.setProfileId(c.getInt(c.getColumnIndex("profile_id")));
                // profile.setProfileId(c.getInt(34));
                profile.setFlag(c.getInt(35));
                profile.setFlag(c.getInt(35));
                profile.setSource(c.getString(39));
              //  profile.setBirthdisplaydate(c.getString(40));


                profile.setFullImgName(c.getString(c.getColumnIndex("image_url")));
                profile.setThumbnailImgName(c.getString(c.getColumnIndex("image_t_url")));


                profile.setCandidateName(c.getString(0));
                profile.setFatherName(c.getString(1));
                profile.setMotherName(c.getString(20));
                //  profile.setProfileType(c.getInt(4) > 0);
                profile.setDateBirth(c.getString(2));
                profile.setExpectation(c.getString(38));


                int todayyear = Calendar.getInstance().get(Calendar.YEAR);
                String strDate = c.getString(2);

                strDate.substring(0, 10);
                Log.d("TAG", "date 1 " + strDate);
                Log.d("TAG", "date 2 " + strDate.substring(0, 10));

                int age = 0;

                if (strDate != null && strDate.length() >= 10) {

                    try {
                        int year = Integer.parseInt(strDate.substring(0, 4));
                        age = todayyear - year;
                    } catch (Exception e) {
                    }


                    /*
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
                    try {
                        Date javaSqlDate = format.parse(strDate);
                        // java.sql.Date javaSqlDate = java.sql.Date.valueOf(date.substring(0,10));
                        Log.d("TAG", "getprofile : " + javaSqlDate);
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(javaSqlDate);
                        int year = cal.get(Calendar.YEAR);
                        age=todayyear-year;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    */
                }

                profile.setCityBirth(c.getString(18));
                profile.setAge(age);
                profile.setColor(c.getString(19));
                profile.setHight(c.getString(3));
                profile.setCast(c.getString(21));
                profile.setManaglik(c.getString(22));
                profile.setEducation(c.getString(25) );
                profile.setIncome(c.getString(4));

                profile.setProfession(c.getString(28) );
                profile.setCityRes(c.getString(29));

                profile.setAddress(c.getString(5) + " , " + c.getString(6) +  " , " + c.getString(29)+ " - " + c.getString(7));

                profile.setContectNo(c.getString(8));
                profile.setDiscription(c.getString(9));

                //  profile.setIncome(c.getString(14));
                // profile.setImage(d);
                //   profile.setFlag(c.getInt(40));
                // News news=new News(c.getInt(0),c.getString(1),c.getInt(2),c.getString(3),c.getString(4),c.getString(5),d,c.getString(7),c.getInt(8),c.getInt(9));
                //fetching all records from cursor until reaching last record
                //closing cursor}
                listprofile.add(profile);

                c.moveToNext();
            }
            c.close();
        } catch (Exception e) {
            Log.e("Error", "error in cursor");
            e.printStackTrace();
        }

        closeDatabase(db);


        return listprofile;
    }


   


    public List<Region> getRegion(DataBaseHelper db)

    {
        String query = "select * from Region";

        listregion = new ArrayList<>();

        openDatabase(db);


        try {

            //defining cursor and select all rows from table named "student"
            Cursor c1 = sqLiteDatabase.rawQuery(query, null);

            Log.d("total record", c1.getCount() + "");

            //put cursor on the first position
            c1.moveToFirst();
            while (!c1.isAfterLast()) {
                Region r = new Region(c1.getInt(0), c1.getString(1));
                listregion.add(r);
                // Log.d("Error", " i am in curssor of region");
                c1.moveToNext();
            }
            c1.close();
        } catch (Exception e) {
            Log.e("Error", "error in cursor" + e.toString());
            e.printStackTrace();
        }

        closeDatabase(db);

        return listregion;
    }


    public List<Education> getEducation(DataBaseHelper db)

    {

        String query = "select * from education";

        listeducation = new ArrayList<>();

        openDatabase(db);



        try {

            //defining cursor and select all rows from table named "student"
            Cursor c1 = sqLiteDatabase.rawQuery(query, null);

            Log.d("total record", c1.getCount() + "");

            //put cursor on the first position
            c1.moveToFirst();
            while (!c1.isAfterLast()) {
                Education e = new Education(c1.getInt(0), c1.getString(1));
                listeducation.add(e);
                Log.e("Error", " i am in curssor of region");
                c1.moveToNext();
            }
            c1.close();
        } catch (Exception e) {
            Log.e("Error", "error in cursor");
            e.printStackTrace();
        }

        closeDatabase(db);

        return listeducation;
    }


    public List<Profession> getProfession(DataBaseHelper db)
    {

        String query = "select * from profession";

        listprofession = new ArrayList<>();

        openDatabase(db);


        try {

            //defining cursor and select all rows from table named "student"
            Cursor c1 = sqLiteDatabase.rawQuery(query, null);
            Log.d("total record", c1.getCount() + "");

            //put cursor on the first position
            c1.moveToFirst();
            while (!c1.isAfterLast()) {
                Profession p = new Profession(c1.getInt(0), c1.getString(1));
                listprofession.add(p);
                c1.moveToNext();
            }
            c1.close();
        } catch (Exception e) {
            Log.e("Error", "error in cursor");
            e.printStackTrace();
        }

        closeDatabase(db);
        return listprofession;
    }

    public List<ProfessionStream> getProfessionStream(DataBaseHelper db, int professionId)

    {

        // String query = "select * from profession_stream";

       // String query = "Select edustr_Id, edstr_name  from education_stream s, education_stream_map m " +
        //        " Where m.eid = " + educationId +
         //       " and m.sid = s.edustr_Id ";

        String query = "Select ps_id, stream_name  from profession_stream s, profession_stream_map m " +
               " Where m.prof_id = " + professionId +
               " and m.sid = s.ps_id ";


        // String query = "Select * from profession_stream_map m ";


        Log.d("TAG", "getProfessionStream: "+query);

        listprofessionstream = new ArrayList<>();

        openDatabase(db);

        try {

            //defining cursor and select all rows from table named "student"
            Cursor c1 = sqLiteDatabase.rawQuery(query, null);
            Log.d("getProfessionStream: ", c1.getCount() + "");

            //put cursor on the first position
            c1.moveToFirst();
            while (!c1.isAfterLast()) {
                ProfessionStream p = new ProfessionStream(c1.getInt(0), c1.getString(1));
                listprofessionstream.add(p);
                Log.e("Error", "getProfessionStream: " + c1.getCount());
                c1.moveToNext();
            }
            c1.close();
        } catch (Exception e) {
            Log.e("Error", "error in cursor");
            e.printStackTrace();
        }

        closeDatabase(db);


        return listprofessionstream;
    }


    public List<EducationStream> getEducationStream(DataBaseHelper db, int educationId)
    {

        // String query = "SELECT * FROM education_stream where edustr_id =" + educationId;

        // String query = "select * from profession_stream where ps_id =" + professionId;
        String query = "Select edustr_Id, edstr_name  from education_stream s, education_stream_map m " +
                " Where m.eid = " + educationId +
                " and m.sid = s.edustr_Id ";


        listeducationstream = new ArrayList<>();

        openDatabase(db);

        try {

            //defining cursor and select all rows from table named "student"
            Cursor c1 = sqLiteDatabase.rawQuery(query, null);
            Log.d("total record", c1.getCount() + "");

            //put cursor on the first position
            c1.moveToFirst();
            while (!c1.isAfterLast()) {
                EducationStream p = new EducationStream(c1.getInt(0), c1.getString(1));
                listeducationstream.add(p);
                c1.moveToNext();
            }
            c1.close();
        } catch (Exception e) {
            Log.e("Error", "error in cursor");
            e.printStackTrace();
        }

        closeDatabase(db);

        return listeducationstream;
    }


    public boolean insertComments(DataBaseHelper dataBaseHelper, String flag, String strComment, int userid, String eventornewsid) {

        openDatabase(dataBaseHelper);
        boolean result = false;
        try {

            String userDisplayName = "";
            if(Utility.getuser() != null){
                userDisplayName = Utility.getuser().getDisplayName();
            }

            if (flag.equals("event")) {
                String query = "insert into  event_comment(discription, user_id, event_id, user_name)" +
                        " values('" + strComment + "','" + userid + "','" + eventornewsid + "', '" + userDisplayName + "')";
                // Log.d("sql", "add comment-" + query);
                sqLiteDatabase.execSQL(query);
            }

            if (flag.equals("news")) {
                String query = "insert into  news_comment(discription, user_id, news_id, user_name)" +
                        " values('" + strComment + "','" + userid + "','" + eventornewsid + "', '" + userDisplayName + "')";
                // Log.d("sql", "add comment-" + query);
                sqLiteDatabase.execSQL(query);
            }
            result = true;

        } catch (Exception e) {
            Log.e("Error", "insertUser() in cursor");
            e.printStackTrace();
        }
        closeDatabase(dataBaseHelper);
        return result;
    }


    public List<Comment> getComment(DataBaseHelper db, String flag, String eventornewsid) {

        List<Comment> list = new ArrayList<Comment>();

        String query = null;

        if (flag.equals("news")) {
            query = "SELECT *  FROM news_comment where news_id='" + eventornewsid + "' order by id desc";
        }
        if (flag.equals("event")) {
            query = "SELECT *  FROM event_comment where event_id='" + eventornewsid + "' order by id desc";
        }

        Log.d("query=", query);

        sqLiteDatabase = db.openDataBase();

        try {

            //defining cursor and select all rows from table named "student"
            Cursor c1 = sqLiteDatabase.rawQuery(query, null);

            Log.d("total recordmayank", c1.getCount() + "");

            //put cursor on the first position
            c1.moveToFirst();
            while (!c1.isAfterLast()) {
                Comment comment = new Comment();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
                // CLASS.setUserDisplayName(userDisplayName);
                Date date = sdf.parse(c1.getString(5));
                long timeInMillisSinceEpoch = date.getTime();

                if(flag.equals("news"))
                {  //    setEvent(int id,String dis,int userid,int eventid,long time)
                    comment.setNewsComment(c1.getInt(0), c1.getInt(1), c1.getString(2), c1.getInt(3), c1.getInt(4), timeInMillisSinceEpoch, c1.getString(6));
                }
                else{
                    comment.setEventComment(c1.getInt(0), c1.getInt(1), c1.getString(2), c1.getInt(3), c1.getInt(4), timeInMillisSinceEpoch, c1.getString(6));
                }
                list.add(comment);
                c1.moveToNext();
            }
            c1.close();
        } catch (Exception e) {
            Log.e("Error", "error in cursor" + e);
            e.printStackTrace();
        }

        closeDatabase(db);
        return list;
    }


    public String getsource1(DataBaseHelper db, int eventsourceid) {
        String name = new String();
        sqLiteDatabase = db.openDataBase();

        //defining cursor and select all rows from table named "student"



        try {

        Log.d("query=", "select name from source where id =" + eventsourceid);


        Cursor c1 = sqLiteDatabase.rawQuery("select name from source where id =" + eventsourceid, null);

            //put cursor on the first position
            c1.moveToFirst();
            while (!c1.isAfterLast()) {
                name = c1.getString(0);
                c1.moveToNext();
            }
            c1.close();
        } catch (Exception e) {
            Log.e("Error", "error in cursor" + e);
            e.printStackTrace();
        }

        closeDatabase(db);


        return name;
    }


    public List<Source> getsource(DataBaseHelper db) {
        List<Source> name=new ArrayList<>();
        sqLiteDatabase = db.openDataBase();

        //defining cursor and select all rows from table named "student"



        try {
;


            Cursor c1 = sqLiteDatabase.rawQuery("select * from source", null);

            //put cursor on the first position
            c1.moveToFirst();
            while (!c1.isAfterLast()) {

                Source S=new Source(c1.getInt(0),c1.getString(1));
              name.add(S);
                c1.moveToNext();
            }
            c1.close();
        } catch (Exception e) {
            Log.e("Error", "error in cursor" + e);
            e.printStackTrace();
        }

        closeDatabase(db);


        return name;
    }
















    public User getUser(Context context, String userName) {

        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        openDatabase(dataBaseHelper);
        int cnt = 0;
        User u = null;

        try {

            String countQuery = "SELECT userid, username, password, mobileno, email, usertype, display_name FROM user where username = '" + userName + "'";
            Cursor cursor = sqLiteDatabase.rawQuery(countQuery, null);
            cnt = cursor.getCount();

            if (cnt > 0) {
                //put cursor on the first position
                cursor.moveToFirst();
                if (!cursor.isAfterLast()) {
                    u = new User();
                    u.setUserId(cursor.getInt(0));
                    u.setUserName(cursor.getString(1));
                    u.setPassword(cursor.getString(2));
                    u.setMobiLeNumber(cursor.getString(3));
                    u.setEmail(cursor.getString(4));
                    u.setUserType(cursor.getInt(5));
                    u.setDisplayName(cursor.getString(6));
                }

            }

            cursor.close();

        } catch (Exception e) {
            Log.e("Error", "error in cursor");
            e.printStackTrace();
        }
        closeDatabase(dataBaseHelper);

        return u;

    }


    public boolean insertUser(Context context, User user) {

        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        openDatabase(dataBaseHelper);
        boolean result = false;
        try {

            // insert SQL query
            String sql = "insert into  user ( userid, username, password, mobileno, email, display_name, usertype)  values(" +
                    "'" + user.getUserId() + "'," +
                    "'" + user.getUserName() + "'," +
                    "'" + user.getPassword() + "'," +
                    "'" + user.getMobiLeNumber() + "'," +
                    "'" + user.getEmail() + "'," +
                    "'" + user.getDisplayName() + "'," +
                    "'" + user.getUserType() + "')";


            sqLiteDatabase.execSQL(sql);
            // String countQuery = "SELECT userid, password, mobileno, email, usertype, display_name FROM user where email = '"+emailId+"'";


            result = true;
        } catch (Exception e) {
            Log.e("Error", "insertUser() in cursor");
            e.printStackTrace();
        }
        closeDatabase(dataBaseHelper);
        return result;
    }


    public User getValidUser(Context context, String userName, String password) {

        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        openDatabase(dataBaseHelper);
        int cnt = 0;
        User u = null;

        try {

            String countQuery = "SELECT userid, username, password, mobileno, email, usertype, display_name FROM user" +
                    " where username = '" + userName + "'";

            Cursor cursor = sqLiteDatabase.rawQuery(countQuery, null);
            cnt = cursor.getCount();

            if (cnt > 0) {
                //put cursor on the first position
                cursor.moveToFirst();
                if (!cursor.isAfterLast()) {
                    u = new User();
                    u.setUserId(cursor.getInt(0));
                    u.setUserName(cursor.getString(1));
                    u.setPassword(cursor.getString(2));
                    u.setMobiLeNumber(cursor.getString(3));
                    u.setEmail(cursor.getString(4));
                    u.setUserType(cursor.getInt(5));
                    Log.d("TAG", "getUserverification: ");
                    Log.d("TAG", "getUserverification: ");
                    u.setDisplayName(cursor.getString(6));

                    // check password
                    if (u.getPassword() != null && u.getPassword().equals(password)) {
                        return u;
                    }

                }

            }

            cursor.close();

        } catch (Exception e) {
            Log.e("Error", "error in cursor");
            e.printStackTrace();
        }
        closeDatabase(dataBaseHelper);

        return null;

    }

    public boolean insertEventLike(Context context, int eventid, int userid) {

        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        openDatabase(dataBaseHelper);
        boolean result = false;

        try {

            String userDisplayName = "";
            if(Utility.getuser() != null){
                userDisplayName = Utility.getuser().getDisplayName();
            }
            // String countQuery = "SELECT userid, password, mobileno, email, usertype, display_name FROM user where email = '"+emailId+"'";
            sqLiteDatabase.execSQL("insert into  event_like ( event_id, user_id, user_name)  " +
                    " values(" + eventid + "," + userid + ", '" + userDisplayName + "')");

            result = true;
        } catch (Exception e) {
            Log.e("Error", "insertUser() in cursor");
            e.printStackTrace();
        }
        closeDatabase(dataBaseHelper);
        return result;
    }


    public boolean insertNewsLike(Context context, int newsid, int userid) {

        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        openDatabase(dataBaseHelper);
        boolean result = false;

        try {

            String userDisplayName = "";
            if(Utility.getuser() != null){
                userDisplayName = Utility.getuser().getDisplayName();
            }

            sqLiteDatabase.execSQL("insert into  news_like ( news_id, user_id, user_name)  " +
                    " values(" + newsid + "," + userid + ", '" + userDisplayName + "')");

            result = true;
        } catch (Exception e) {
            Log.e("Error", "insertUser() in cursor");
            e.printStackTrace();
        }
        closeDatabase(dataBaseHelper);
        return result;
    }

    public boolean checkEventLike(Context context, int eventid, int userid) {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        openDatabase(dataBaseHelper);
        Boolean result = false;

        try{

            String countQuery = "SELECT * FROM event_like where event_id = " + eventid + " and user_id = " + userid;
            Cursor cursor = sqLiteDatabase.rawQuery(countQuery, null);
            int cnt = cursor.getCount();

            if (cnt > 0) {
                result = true;
            }
            cursor.close();

        } catch (Exception e) {
            Log.e("Error", "insertUser() in cursor");
            e.printStackTrace();
        }

        closeDatabase(dataBaseHelper);

        return result;
    }

    public boolean checkNewsLike(Context context, int newsid, int userid) {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        openDatabase(dataBaseHelper);
        Boolean result = false;

        try{
            String countQuery = "SELECT * FROM news_like where news_id = " + newsid + " and user_id = " + userid;
            Cursor cursor = sqLiteDatabase.rawQuery(countQuery, null);
            int cnt = cursor.getCount();

            if (cnt > 0) {
                result = true;
            }
            cursor.close();

        } catch (Exception e) {
            Log.e("Error", "insertUser() in cursor");
            e.printStackTrace();
        }

        closeDatabase(dataBaseHelper);

        return result;
    }

    public int noOfEventLike(Context context, int eventid) {

        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        openDatabase(dataBaseHelper);
        int cnt = 0;

        try{
            String countQuery = "SELECT * FROM event_like where event_id = " + eventid;
            Cursor cursor = sqLiteDatabase.rawQuery(countQuery, null);
            cnt = cursor.getCount();
            cursor.close();

        } catch (Exception e) {
            Log.e("Error", "insertUser() in cursor");
            e.printStackTrace();
        }
        closeDatabase(dataBaseHelper);
        return cnt;
    }

    public int noOfNewsLike(Context context, int newsid) {

        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        openDatabase(dataBaseHelper);
        int cnt = 0;

        try{

            String countQuery = "SELECT * FROM news_like where news_id = " + newsid;
            Cursor cursor = sqLiteDatabase.rawQuery(countQuery, null);
            cnt = cursor.getCount();
            cursor.close();
        } catch (Exception e) {
            Log.e("Error", "insertUser() in cursor");
            e.printStackTrace();
        }

        closeDatabase(dataBaseHelper);
        return cnt;
    }


    public String[] getEventLikeList(Context context, String eventid) {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        openDatabase(dataBaseHelper);
        String[] name = new String[0];


        String countQuery = "SELECT user_name FROM event_like  where event_id = " + eventid;

        int i = 0;
        try {

            Cursor cursor = sqLiteDatabase.rawQuery(countQuery, null);
            Log.d("TAG", "listlike:" + countQuery);
            int cnt = cursor.getCount();
            name = new String[cnt];

            //put cursor on the first position
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {

                name[i] = cursor.getString(0);
                if (name[i] == null) {
                    name[i] = "";
                }
                cursor.moveToNext();
                i++;
            }
            cursor.close();
        } catch (Exception e) {
            Log.e("Error", "error in cursor" + e);
            e.printStackTrace();
        }


        closeDatabase(dataBaseHelper);

        return name;
    }



    public String[] getNewsLikeList(Context context, String newsid) {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        openDatabase(dataBaseHelper);
        String[] name = new String[0];
        int i = 0;
        try {

            String countQuery = "SELECT user_name FROM news_like where news_id = " + newsid;
            Cursor cursor = sqLiteDatabase.rawQuery(countQuery, null);
            Log.d("TAG countquery", "" + countQuery);
            int cnt = cursor.getCount();
            name = new String[cnt];

            //put cursor on the first position
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {

                name[i] = cursor.getString(0);
                if (name[i] == null) {
                    name[i] = "";
                }
                Log.d("TAG", "newslistlike:" + name[i]);
                cursor.moveToNext();
                i++;
            }
            cursor.close();
        } catch (Exception e) {
            Log.e("Error", "error in cursor" + e);
            e.printStackTrace();
        }


        closeDatabase(dataBaseHelper);

        return name;
    }


    public boolean setProfileFavFlag(Context context, int flag, int id) {

        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        openDatabase(dataBaseHelper);
        boolean result = false;
        try {
            // String countQuery = "SELECT userid, password, mobileno, email, usertype, display_name FROM user where email = '"+emailId+"'";

            /* UPDATE table_name
            SET column1=value1,column2=value2,...
            WHERE some_column=some_value;*/


            sqLiteDatabase.execSQL("UPDATE  profile SET  fav_flag=" + flag + " where profile_id =" + id);


            result = true;
        } catch (Exception e) {
            Log.e("Error", "insertUser() in cursor");
            e.printStackTrace();
        }

        closeDatabase(dataBaseHelper);


        return result;


    }


    public boolean updateuser(Context context,int userid,int flag)

    {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        openDatabase(dataBaseHelper);
        boolean result = false;
        try {
            // String countQuery = "SELECT userid, password, mobileno, email, usertype, display_name FROM user where email = '"+emailId+"'";

            /* UPDATE table_name
            SET column1=value1,column2=value2,...
            WHERE some_column=some_value;*/


            sqLiteDatabase.execSQL("UPDATE user  SET  remember=" + flag + " where userid =" + userid);


            result = true;
        }
        catch (Exception e)
        {
            Log.e("Error", "insertUser() in cursor");
            e.printStackTrace();
        }

        closeDatabase(dataBaseHelper);


        return result;





    }



    public User getUser(Context context ) {

        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        openDatabase(dataBaseHelper);
        int cnt = 0;
        User u = null;

        try {

            String countQuery = "SELECT userid, username, password, mobileno, email, usertype, display_name FROM user where remember = 1 ";
            Cursor cursor = sqLiteDatabase.rawQuery(countQuery, null);
            cnt = cursor.getCount();

            if (cnt > 0) {
                //put cursor on the first position
                cursor.moveToFirst();
                if (!cursor.isAfterLast()) {
                    u = new User();
                    u.setUserId(cursor.getInt(0));
                    u.setUserName(cursor.getString(1));
                    u.setPassword(cursor.getString(2));
                    u.setMobiLeNumber(cursor.getString(3));
                    u.setEmail(cursor.getString(4));
                    u.setUserType(cursor.getInt(5));
                    u.setDisplayName(cursor.getString(6));
                }

            }

            cursor.close();

        } catch (Exception e) {
            Log.e("Error", "error in cursor");
            e.printStackTrace();
        }
        closeDatabase(dataBaseHelper);

        return u;

    }




    public List<State> getstate(DataBaseHelper db)

    {
        String query = "select * from state";

      List<State>  liststate = new ArrayList<>();

        openDatabase(db);


        try {

            //defining cursor and select all rows from table named "student"
            Cursor c1 = sqLiteDatabase.rawQuery(query, null);

            Log.d("total record", c1.getCount() + "");

            //put cursor on the first position
            c1.moveToFirst();
            while (!c1.isAfterLast()) {
                State r = new State(c1.getInt(0), c1.getString(1));
                liststate.add(r);
                // Log.d("Error", " i am in curssor of region");
                c1.moveToNext();
            }
            c1.close();
        } catch (Exception e) {
            Log.e("Error", "error in cursor" + e.toString());
            e.printStackTrace();
        }

        closeDatabase(db);

        return liststate;
    }



public List<Address>  getadress(DataBaseHelper dataBaseHelper)

{
   List<Address>  addresslist=new ArrayList<>();

    String query = "select address1,address2 from profile";



    openDatabase(dataBaseHelper);


    try {

        //defining cursor and select all rows from table named "student"
        Cursor c1 = sqLiteDatabase.rawQuery(query, null);

        Log.d("total record", c1.getCount() + "");

        //put cursor on the first position
        c1.moveToFirst();
        while (!c1.isAfterLast()) {

            Address address=new Address(c1.getString(0)+c1.getString(1));
            addresslist.add(address);
            c1.moveToNext();
        }
        c1.close();
    } catch (Exception e) {
        Log.e("Error", "error in cursor" + e.toString());
        e.printStackTrace();
    }

    closeDatabase(dataBaseHelper);

   return  addresslist;

}


    public String[]  getcity(DataBaseHelper dataBaseHelper)

    {



        String query = "select name from city";

        String[]  city=null;

        openDatabase(dataBaseHelper);


        try {

            //defining cursor and select all rows from table named "student"
            Cursor c1 = sqLiteDatabase.rawQuery(query, null);

              city=new String[c1.getCount()];

            Log.d("total record city india", c1.getCount() + "");

            //put cursor on the first position
            c1.moveToFirst();

            int i=0;
            while (!c1.isAfterLast()) {


                city[i]=c1.getString(0);

                i++;

             //   Address address=new Address(c1.getString(0)+c1.getString(1));
              //  addresslist.add(address);
              c1.moveToNext();

            }
            c1.close();
        } catch (Exception e) {
            Log.e("Error", "error in cursor" + e.toString());
            e.printStackTrace();
        }

        closeDatabase(dataBaseHelper);




        return city;

    }


public void deleteExpiredDataEvent(DataBaseHelper dataBaseHelper)
{
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    Calendar cal=Calendar.getInstance();
    Date maxtoday=cal.getTime();
    String tomaxString = sdf.format(maxtoday);


    String query= "";

    openDatabase(dataBaseHelper);

    try {

        query="delete from event_like where event_id IN(SELECT event_id from event where expiration_date < '" +tomaxString+"')";
        //defining cursor and select all rows from table named "student"

        Log.d("TESTING eventlike", "delete response" + query);

        Cursor c1 = sqLiteDatabase.rawQuery(query, null);
        c1.close();

        }

     catch (Exception e) {
        Log.e("Error", "delete in eventlike" + e.toString());
        e.printStackTrace();
    }

    // openDatabase(dataBaseHelper);
    try {

        query="delete from event_comment where event_id IN(SELECT event_id from event where expiration_date < '" +tomaxString+"')";
        //defining cursor and select all rows from table named "student"
        Log.d("TESTING eventcomment", "delete  response" + query);
        Cursor c1 = sqLiteDatabase.rawQuery(query, null);
        c1.close();

    }

    catch (Exception e) {
        Log.e("Error", "delete in eventcomment" + e.toString());
        e.printStackTrace();
    }


    // openDatabase(dataBaseHelper);

    try {

        query="delete from event where  expiration_date < '" +tomaxString+"'";
        //defining cursor and select all rows from table named "student  Log.d("TESTING", "error  response" + query);"
        Log.d("TESTING", "delete in event" + query);
        Cursor c1 = sqLiteDatabase.rawQuery(query, null);
        c1.close();

    }

    catch (Exception e) {
        Log.e("Error", "deleteerror in event" + e.toString());
        e.printStackTrace();
    }

    closeDatabase(dataBaseHelper);

}


    public void deleteExpiredDataNews(DataBaseHelper dataBaseHelper)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar cal=Calendar.getInstance();
        Date maxtoday=cal.getTime();
        String tomaxString = sdf.format(maxtoday);


        String query="";

        openDatabase(dataBaseHelper);

        try {
            query="delete from news_like where news_id IN(SELECT news_id from news where expiration_date < '" +tomaxString+"')";
            //defining cursor and select all rows from table named "student"
            Log.d("delete", "deleteExpired news like: " +query);
            Cursor c1 = sqLiteDatabase.rawQuery(query, null);
            c1.close();

        }

        catch (Exception e) {
            Log.e("Error", "delete in news error" + e.toString());
            e.printStackTrace();
        }


        // openDatabase(dataBaseHelper);

        try {

            //defining cursor and select all rows from table named "student"
            query="delete from news_comment where news_id IN(SELECT news_id from news where expiration_date < '" +tomaxString+"')";
            Cursor c1 = sqLiteDatabase.rawQuery(query, null);
            c1.close();

        }

        catch (Exception e) {
            Log.e("Error", "error in cursor" + e.toString());
            e.printStackTrace();
        }


        // openDatabase(dataBaseHelper);

        try {
            query="delete from news where  expiration_date < '" +tomaxString+"'";
            Log.d("delete", "deleteExpiredDataProfile: " +query);
            //defining cursor and select all rows from table named "student"
            Cursor c1 = sqLiteDatabase.rawQuery(query, null);
            c1.close();

        }

        catch (Exception e) {
            Log.e("Error", "error in cursor" + e.toString());
            e.printStackTrace();
        }

        closeDatabase(dataBaseHelper);

    }



    public void deleteExpiredDataProfile(DataBaseHelper dataBaseHelper)

    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar cal=Calendar.getInstance();
        Date maxtoday=cal.getTime();
        String tomaxString = sdf.format(maxtoday);



     String   query="delete from profile where profile_expiration_date < '" +tomaxString+"'";
        Log.d("delete", "deleteExpiredDataProfile: " +query);


        openDatabase(dataBaseHelper);

        try {

            //defining cursor and select all rows from table named "student"
            Cursor c1 = sqLiteDatabase.rawQuery(query, null);
            c1.close();

        }

        catch (Exception e) {
            Log.e("Error", "error in cursor" + e.toString());
            e.printStackTrace();
        }

        closeDatabase(dataBaseHelper);


    }

}
