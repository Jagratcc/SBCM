package fiveexceptions.com.m01sbcm.sync;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by amit on 11/5/16.
 */
public class VolleySingleton {

    private RequestQueue mRequestQueue;
    private static VolleySingleton sInstance = null;
    // private static Context mCtx;
    private ImageLoader imageLoader;

    private VolleySingleton() {

         mRequestQueue= Volley.newRequestQueue(MyApplication.getAppContext());
        //// mRequestQueue = getRequestQueue();

        // for image
        imageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache()
        {
            private LruCache<String,Bitmap> cache =new LruCache<>((int) (Runtime.getRuntime().maxMemory()/1024/8));

            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
            cache.put(url,bitmap);
            }
        });
    }

    public static VolleySingleton getsInstance() {
        if (sInstance == null) {
            sInstance = new VolleySingleton();
        }
        return sInstance;
    }


    public RequestQueue getmRequestQueue(){
        return mRequestQueue;
    }

    /*
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            Cache cache = new DiskBasedCache(MyApplication.getAppContext().getCacheDir(), 10 * 1024 * 1024);
            Network network = new BasicNetwork(new HurlStack());
            mRequestQueue = new RequestQueue(cache, network);
            // Don't forget to start the volley request queue
            mRequestQueue.start();
        }
        return mRequestQueue;
    }
    */

    public  ImageLoader getImageLoader(){
        return imageLoader;
    }

}


