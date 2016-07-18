package fiveexceptions.com.m01sbcm.sync;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by dell on 5/15/2016.
 * http://stackoverflow.com/questions/1812244/simplest-way-to-serve-static-data-from-outside-the-application-server-in-a-java
 * http://stackoverflow.com/questions/2876250/tomcat-cache-control
 * http://stackoverflow.com/questions/2872613/caching-images-served-by-servlet
 *
 */
public class DownloadImg extends AsyncTask<String,Integer,Long> {

    Activity activity;
    ProgressDialog mProgressDialog = null;
    String strFolderName;
    String PATH = "";
    String targetFileName = "";

        public void DownloadImg(Activity activity){
            this.activity = activity;
            mProgressDialog = new ProgressDialog(activity);

            String imgFolder = "profile";

            // application internal storage path /data/data/your_package/  : removed when application uninstall
            PATH = activity.getExternalFilesDir(null)  + "/melawa/" + imgFolder + "/";


        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.setMessage("Downloading images....");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(100);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.show();
        }


        @Override
        protected Long doInBackground(String... aurl) {
            int count;
            try {
                URL url = new URL((String) aurl[0]);
                URLConnection conexion = url.openConnection();
                conexion.connect();
                // String targetFileName="Name"+".rar";//Change name and subname
                int lenghtOfFile = conexion.getContentLength();
                // String PATH = Environment.getExternalStorageDirectory()+ "/"+downloadFolder+"/";
                File folder = new File(PATH);
                if(!folder.exists()){
                    folder.mkdir();//If there is no folder it will be created.
                }
                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = new FileOutputStream(PATH+targetFileName);
                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishProgress ((int)(total*100/lenghtOfFile));
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {}
            return null;
        }

        protected void onProgressUpdate(Integer... progress) {
            mProgressDialog.setProgress(progress[0]);
            if(mProgressDialog.getProgress()==mProgressDialog.getMax()){
                mProgressDialog.dismiss();
                // Toast.makeText(fa, "File Downloaded", Toast.LENGTH_SHORT).show();
            }
        }
        protected void onPostExecute(String result) {
        }




}
