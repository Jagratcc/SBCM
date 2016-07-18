package fiveexceptions.com.m01sbcm.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import fiveexceptions.com.m01sbcm.R;
import fiveexceptions.com.m01sbcm.database.DataBaseManager;


public class LikeList extends AppCompatActivity {

    ListView listview;

    String id;
    String flag;
    TextView toolbartext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_likelist);
        toolbartext=(TextView)findViewById(R.id.head);

        /*
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        */



        Intent intent = getIntent();

        id = intent.getStringExtra("eventid");
        flag=intent.getStringExtra("flag");
        String[] like=null;
        if(flag.equals("news"))
        {
             toolbartext.setText((getResources().getString(R.string.News_Like)));
            DataBaseManager dataBaseManager=new DataBaseManager();
             like =dataBaseManager.getNewsLikeList(LikeList.this, id);
            Log.d("TAGmanku", "news "+id);
        }


        if(flag.equals("event"))
        {
            toolbartext.setText((getResources().getString(R.string.Event_Like)));

            DataBaseManager dataBaseManager=new DataBaseManager();
             like =dataBaseManager.getEventLikeList(LikeList.this,id);
            Log.d("TAGmanku", "event"+id);
        }


        listview= (ListView)findViewById(R.id.likelist);

        // Inflate the layout for this fragment
        ArrayAdapter myadapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,like);
        listview.setAdapter(myadapter);




    }
}
