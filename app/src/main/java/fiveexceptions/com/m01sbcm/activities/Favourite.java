package fiveexceptions.com.m01sbcm.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fiveexceptions.com.m01sbcm.R;
import fiveexceptions.com.m01sbcm.adapters.MenuAdapter;
import fiveexceptions.com.m01sbcm.adapters.ProfileAdapter;
import fiveexceptions.com.m01sbcm.database.DataBaseHelper;
import fiveexceptions.com.m01sbcm.database.DataBaseManager;
import fiveexceptions.com.m01sbcm.model.Profile;

public class Favourite extends AppCompatActivity {

    ActionBarDrawerToggle actionBarDrawerToggle;
    public DrawerLayout drawerLayout;
    ListView listview;
    TextView toolbarTitle;




    List<Profile>  list=new ArrayList<>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        Boolean   flag = data.getBooleanExtra("flag",false);
        Log.d("Tag....Item profileId", "  i am at  favirote start activity ");
        Log.d("Tag....Item profileId", "  i am at  favirote start activity "+flag);

        if(flag)
        {
            DataBaseHelper dataBaseHelper=new DataBaseHelper(getBaseContext());
            DataBaseManager dataBaseManager=new DataBaseManager();
            list=dataBaseManager.getprofile(dataBaseHelper," and fav_flag= 1");
            RecyclerView recList = (RecyclerView)findViewById(R.id.cardList);
            LinearLayoutManager llm = new LinearLayoutManager(getBaseContext());
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            recList.setLayoutManager(llm);
            ProfileAdapter myviewAdapterProfile=new ProfileAdapter(list,Favourite.this);
            recList.setAdapter(myviewAdapterProfile);




        }










        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faviraoute);



        DataBaseHelper dataBaseHelper=new DataBaseHelper(getBaseContext());
        DataBaseManager dataBaseManager=new DataBaseManager();
        list=dataBaseManager.getprofile(dataBaseHelper," and fav_flag= 1");



        //Add to layout



        RecyclerView recList = (RecyclerView)findViewById(R.id.cardList);
        LinearLayoutManager llm = new LinearLayoutManager(getBaseContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        ProfileAdapter myviewAdapterProfile=new ProfileAdapter(list,Favourite.this);
        recList.setAdapter(myviewAdapterProfile);



        setTitle("");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.navigation_drawer);
        listview= (ListView)findViewById(R.id.left_drawer);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.toggle);

        getSupportActionBar().setHomeButtonEnabled(true);


        toolbarTitle = (TextView)findViewById(R.id.toolbar_title);


        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {

                //getSupportActionBar().setHomeAsUpIndicator(R.drawable.draweropen);

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                // getSupportActionBar().setHomeAsUpIndicator(R.drawable.drawerclose);

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });


      /*  Bundle extras = getIntent().getExtras();
        String title = "Default Headline";
        if (extras != null && extras.containsKey("title")) {
            title = getIntent().getStringExtra("title");
        }


          */
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.toggle);

        getSupportActionBar().setHomeButtonEnabled(true);


        toolbarTitle = (TextView)findViewById(R.id.toolbar_title);

        String[] mobileArray = new String[]{getResources().getString(R.string.news),getResources().getString(R.string.event),getResources().getString(R.string.profile),getResources().getString(R.string.Favourite),getResources().getString(R.string.ContectUs),getResources().getString(R.string.logout)};
        // Inflate the layout for this fragment
        ArrayAdapter myadapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,mobileArray);
        MenuAdapter menu   =  new MenuAdapter(this,mobileArray);
        listview.setAdapter(menu);
    }



    @Override
    public void onBackPressed() {

        Intent i= new Intent(this,MainActivity.class);

        startActivity(i);
        finish();


    }

    /*    new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Melawa")
                .setMessage("Are you sure you want to Exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();

        // super.onBackPressed();

    }
    */


}
