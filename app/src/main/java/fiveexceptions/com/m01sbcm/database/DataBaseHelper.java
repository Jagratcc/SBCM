package fiveexceptions.com.m01sbcm.database;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import fiveexceptions.com.m01sbcm.utility.Constants;

/**
 * Created by 5exception on 2/9/2016.
 */

public class DataBaseHelper extends SQLiteOpenHelper {
    //  private static String DB_PATH = "/data/data/com.garden.tree/databases/";
//  private static String DB_PATH =" ";


    private final Context mContext;
    // private String DB_PATH = "";

    //   private  static final String DB_NAME="GardenIdDb";
    //   private  static final int DB_VERSION=1;
    private SQLiteDatabase mDataBase;


    public DataBaseHelper(Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
        //  DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        this.mContext = context;
        ContextWrapper cw =new ContextWrapper(mContext);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void createDataBaseIfNotExist() {

        //IT RETURN TRUE IF DB IS EXIST ELSE FALSE
        boolean dbExist = DBExists();
        Log.d("sql helper", "db exist=" + dbExist);

        if (!dbExist) {
            //now we copy data base that we include
            copyDBFromResource();
        }

    }//createDB()

    //IF DB NOT EXISTS THEN THEN IT RETURN FALSE
    private boolean DBExists() {

        mDataBase = null;
        boolean isDbExist = false;
        try {

            String databasePath =  Constants.DB_PATH + Constants.DB_NAME;
            File f = new File(databasePath);
            isDbExist = f.exists();

        } catch (SQLiteException e) {
            Log.e("sql helper", "data base not found");

        }
        return isDbExist;
    }

    //COPY THE DB FROM ASSETS FOLDER
    private void copyDBFromResource() {
        Log.e("sql helper", "copy database from resource");
        InputStream inputStream = null;
        OutputStream outputStream = null;
        String dbFilePath = Constants.DB_PATH + Constants.DB_NAME;

        Log.d("copy db path ", Constants.DB_PATH);
        Log.d("copy db name ",Constants.DB_NAME);



        try {

            // ContextWrapper cw =new ContextWrapper(getApplicationContext());

            inputStream = mContext.getAssets().open(Constants.DB_NAME);
            outputStream = new FileOutputStream(dbFilePath);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();

        } catch (IOException e) {
            Log.e("sql helper", "data base not copy");
            Log.e("Error", e.getMessage());
            e.printStackTrace();
            // throw new Error("problem while coping data base from resource");
        }
    }


    public SQLiteDatabase openDataBase() throws SQLException {

        setDbPath();

        String mPath = Constants.DB_PATH + Constants.DB_NAME;

        Log.d("open db path ", Constants.DB_PATH);
        Log.d("open db name ", Constants.DB_NAME);
        Log.d("database helper", mPath);
        mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.OPEN_READWRITE);

        return mDataBase;
    }


    private void setDbPath() {
        Constants.DB_PATH = mContext.getFilesDir().getAbsolutePath();
    }

    /**
     * This method close database connection and released occupied memory
     **/
    /*
    @Override
    public synchronized void close() throws SQLException{
        SQLiteDatabase db = null;
        if (db != null)
            db.close();
        SQLiteDatabase.releaseMemory();
        super.close();
    }
    */
}
