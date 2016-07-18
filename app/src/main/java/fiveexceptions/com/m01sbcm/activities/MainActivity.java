package fiveexceptions.com.m01sbcm.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.TextView;

import fiveexceptions.com.m01sbcm.R;
import fiveexceptions.com.m01sbcm.adapters.MenuAdapter;
import fiveexceptions.com.m01sbcm.adapters.ViewpageAdapter;
import fiveexceptions.com.m01sbcm.database.DataBaseHelper;
import fiveexceptions.com.m01sbcm.database.DataBaseManager;
import fiveexceptions.com.m01sbcm.lib.SlidingTabLayout;
import fiveexceptions.com.m01sbcm.utility.Constants;


public class MainActivity extends AppCompatActivity {
    ActionBarDrawerToggle actionBarDrawerToggle;
    public DrawerLayout drawerLayout;
    ListView listview;
    public ViewPager pager;
    ViewpageAdapter adapter;
    SlidingTabLayout tabs;

    TextView toolbarTitle;

    int Numboftabs =3;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("");

        // delete the expired profile, news and events
        DataBaseManager dataBaseManager = new DataBaseManager();
        final DataBaseHelper dataBaseHelper = new DataBaseHelper(getBaseContext());
        dataBaseManager.deleteExpiredDataEvent(dataBaseHelper);
        dataBaseManager.deleteExpiredDataNews(dataBaseHelper);
        dataBaseManager.deleteExpiredDataProfile(dataBaseHelper);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.navigation_drawer);
        listview= (ListView)findViewById(R.id.left_drawer);
        setSupportActionBar(toolbar);

        CharSequence Titles[]={this.getResources().getString(R.string.news),this.getResources().getString(R.string.event),this.getResources().getString(R.string.profile)};

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
        adapter =  new ViewpageAdapter(this, getSupportFragmentManager(),Titles,Numboftabs);


        toolbarTitle = (TextView)findViewById(R.id.toolbar_title);


        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);


        //Assiging the Sliding Tab Layout View
       tabs = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
       //tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width
       tabs.setViewPager(pager);

        // setting the tab view adptor
        TabInterface tab1 = (TabInterface) adapter.getItem(0);
        TabInterface tab2 = (TabInterface) adapter.getItem(1);
        TabInterface tab3 = (TabInterface) adapter.getItem(2);
        tabs.setTabInterface(tab1, tab2, tab3);


        String[] mobileArray = new String[]{getResources().getString(R.string.news),getResources().getString(R.string.event),getResources().getString(R.string.profile),getResources().getString(R.string.Favourite),getResources().getString(R.string.ContectUs),getResources().getString(R.string.logout)};
        // Inflate the layout for this fragment

        // ArrayAdapter myadapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,mobileArray);


        MenuAdapter menu   =  new MenuAdapter(this,mobileArray);
        listview.setAdapter(menu);






        // hideSoftKeyboard();

        // show the selected tab
        int tabId = getIntent().getIntExtra("TabId", 0);
        pager.setCurrentItem(tabId, true);





    }

    public void setToolbarTitle(int tabId){

        if(tabId == 1){
            toolbarTitle.setText(getResources().getString(R.string.news));
        }
        if(tabId == 2){
            toolbarTitle.setText(getResources().getString(R.string.event));
        }
        if(tabId == 3){
            toolbarTitle.setText(getResources().getString(R.string.profile));
        }

    }

    /**
     * Hides the soft keyboard
     */
    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // got response from other activity
        if(data != null)
        {


            boolean isProfileDetail = data.getBooleanExtra("profile", false);
            if(isProfileDetail){
                return;
            }



            boolean isChangeInCommentLike = data.getBooleanExtra("result", false);
            boolean isChangedInAdvSearch = data.getBooleanExtra("flag", false);

            if (isChangedInAdvSearch) {

                int page = pager.getCurrentItem();
                TabInterface tab = (TabInterface) adapter.getItem(page);
                tab.refreshWithAdvSearch();

            } else {

                Log.d(Constants.TAG, " isChanged: " + isChangeInCommentLike);
                if (isChangeInCommentLike) {
                    int page = pager.getCurrentItem();
                    TabInterface tab = (TabInterface) adapter.getItem(page);
                    // tab.refreshData();
                    // tab.refeshData(true);
                    tab.refreshWithAdvSearch();

                }
            }
        }

    }


    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.app_name)
                .setMessage("Are you sure you want to exit?")
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


}
