package fiveexceptions.com.m01sbcm.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import fiveexceptions.com.m01sbcm.model.User;
import fiveexceptions.com.m01sbcm.sync.MyApplication;

/**
 * Created by amit on 14/4/16.
 */
public class Utility  {


    private static User u;

    ///////////////////THIS FUNCTION IS FOR GET WIDTH AND HIGHT OF DEIVICE///////////////////////////////////////////////////////////
    public static DisplayMetrics getScreenWidth(Context context)
    {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        // int width = metrics.widthPixels;
        // int height = metrics.heightPixels;
        // return "{" + width + "," + height + "}";
        return metrics;
    }


    //////////////////////////////THIS FUNCTION USE FOR  CONVERT BYTE ARRAY IMAGE INTO  DRAWABLE////////////////

    public static Drawable getDrawable(byte[] imageByteArray)
    {

        if (imageByteArray != null) {
            ByteArrayInputStream imageStream = new ByteArrayInputStream(
                    imageByteArray);
            Bitmap company_logo = BitmapFactory
                    .decodeStream(imageStream);
         Drawable   d = new BitmapDrawable(Resources.getSystem(), company_logo);
            return d;
        }

        return null;
    }

    public static User getuser()
    {

        return u;
    }

    public  static void setuser(User user)
    {
       u=user;

    }

    public static void destroyuser()

    {

       u=null;

    }

    public  static boolean isNetworkAvailable(final Context context) {
        return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }

    public static void savePreferences(String key, String value){
        SharedPreferences sharedPreferences = MyApplication.getAppContext().getSharedPreferences("MY_PREFERENCE", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
        Log.d("MYTAG", "final key -" + key);
        Log.d("MYTAG", "final value -" + value);
    }

    public static String loadPreferences(String key){
        SharedPreferences sharedPreferences =  MyApplication.getAppContext().getSharedPreferences("MY_PREFERENCE", Context.MODE_PRIVATE);
        String value = sharedPreferences.getString(key, "0");
        Log.d("MYTAG", "final get value -" + value);
        return value;
    }


    public static String getDate(String inputDate){

        String dateInString = "0000-00-00";

        if(inputDate != null && inputDate.length() > 9) {
            dateInString = inputDate.substring(0, 10);
        }

        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatter2 = new SimpleDateFormat("dd-MMM-yy");
        String output ="";

        try{
                Date date = formatter1.parse(dateInString);
                output = formatter2.format(date);
        }catch(Exception e){
            e.printStackTrace();
        }
        return output;
    }

    public static String getDateTime(String inputDateTime){

        String dateInString = "0000-00-00";
        String timeInString = "00:00";


        if(inputDateTime != null && inputDateTime.length() > 9) {
            dateInString = inputDateTime.substring(0, 10);

        }
        if(inputDateTime != null && inputDateTime.length() > 15) {
            timeInString = inputDateTime.substring(11, 16);
        }

        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatter2 = new SimpleDateFormat("dd-MMM-yy");
        String output ="";

        try{
            Date date = formatter1.parse(dateInString);
            Log.d("TAG", "getDateTime: "+date);
            output = formatter2.format(date);
            Log.d("TAG", "getDateTime: "+output);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return output + " " + timeInString;
        //return  inputDateTime;
    }


    /*
    public static boolean createImageDir(Context context){

        boolean result = false;

        try{


            // application internal storage path /data/data/your_package/  : removed when application uninstall
            File profileDir = new File(Constants.PROFILE_IMG_DIR_PATH);
            File newsDir = new File(Constants.NEWS_IMG_DIR_PATH);
            File eventDir = new File(Constants.EVENT_IMG_DIR_PATH);


            if (!profileDir.exists())   {     profileDir.mkdir();   }
            if (!newsDir.exists())      {     newsDir.mkdir();      }
            if (!eventDir.exists())     {     eventDir.mkdir();     }

            result = true;

        }catch(Exception e){
            Log.e("Exception", "Exception while creating the image directories");
            e.printStackTrace();
        }

        return result;

        //  kind of storage
        // application internal storage path /data/data/your_package/  : removed when application uninstall
        // PATH = context.getExternalFilesDir(null);

        // path to files folder inside Android/data/data/your_package/ on your SD card.....  : removed when application uninstall
        // context.getFilesDir();
        // this is not require the WRITE_EXTERNAL_STORAGE permission

        // root path to your SD card (e.g mnt/sdcard/)
        // PATH = Environment.getExternalStorageDirectory() + "/melawa/" + imgFolder + "/";

        // media directory exists as well

    }

    */



}
