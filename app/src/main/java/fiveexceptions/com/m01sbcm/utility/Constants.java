package fiveexceptions.com.m01sbcm.utility;

/**
 * Created by amit on 3/21/16.
 */
public class Constants {

    public static String communityId = "2";

    // public static String DB_PATH = "/data/data/com.example.amit/databases/";
    public static String DB_PATH = "";        // set from db helper on application startup
    public  static final String DB_NAME="melawa.sqlite";
    public  static final int DB_VERSION=1;

    // server url
    public static final String BASE_RUL = "http://52.69.236.183:8080/Melawa/";
    // public static final String BASE_RUL = "http://192.168.1.250:8080/Melawa/";
    public static final String URL_SERVER_TO_DEVICE = BASE_RUL + "ws/serverToDeviceSync?user_id=1&community_id=" + communityId;
    public static final String URL_DEVICE_TO_SERVER = BASE_RUL + "ws/deviceToServerSync";
    public static final String URL_LOGIN = BASE_RUL + "ws/login";
    public static final String URL_REGISTRATION = BASE_RUL + "ws/registerUser";
    public static final String URL_APP_SETTING = BASE_RUL + "ws/syncAppSettings?community_id=" + communityId;
    public static final String URL_PROFILE_IMG = BASE_RUL + "files/c" + communityId + "/profiles/";
    public static final String URL_NEWS_IMG = BASE_RUL + "files/c" + communityId + "/news/";
    public static final String URL_EVENT_IMG = BASE_RUL + "files/c" + communityId + "/events/";
    public static final String TAG = "Melawa";


    // LOGIN type
    public static int LOGIN_TYPE_CUSTOM = 0;
    public static int LOGIN_TYPE_FB = 1;
    public static int LOGIN_TYPE_GMAIL = 2;


    /*
    // IMG directory : set from splash screen on application startup
    public static String PROFILE_IMG_DIR_PATH = "";
    public static String EVENT_IMG_DIR_PATH = "";
    public static String NEWS_IMG_DIR_PATH = "";
    */

}
