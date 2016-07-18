package fiveexceptions.com.m01sbcm.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

import fiveexceptions.com.m01sbcm.R;
import fiveexceptions.com.m01sbcm.adapters.NewsAdapter;
import fiveexceptions.com.m01sbcm.database.DataBaseHelper;
import fiveexceptions.com.m01sbcm.database.DataBaseManager;
import fiveexceptions.com.m01sbcm.model.News;
import fiveexceptions.com.m01sbcm.sync.SyncUtil;
import fiveexceptions.com.m01sbcm.utility.Constants;


/**
 * A simple {@link Fragment} subclass.
 */
public class Tab3 extends Fragment  implements TabInterface, SyncInterface
{
    List<News> newsList=new ArrayList<News>();
    View view;
    ImageButton searchbutton;
    EditText newssearch;
    static String searchquery=new String();


    Context context = null;
    View v;
    int totalRecords = 0;
    private SwipeRefreshLayout swipeContainer;


    public Tab3() {
        // Required empty public constructor
    }

    public void setActivityContext(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(Constants.TAG, "refeshData: News refresh end ");

    // Toast.makeText(getContext(),"on create activity 1",Toast.LENGTH_LONG).show();
        // Inflate the layout for this fragment
        // THIS IS FOR  LAYOUT  INFLATE FROM XML
     v= inflater.inflate(R.layout.fragment_tab3, container, false);
         this. view=v;
     /*   searchbutton=(ImageButton)v.findViewById(R.id.pinButton);
        // searchbutton=(ImageButton)v.findViewById(R.id.pinButton);
        searchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
////////////////// THIS IS POP WINDOW  CREATE  FUNCTION;
               initiatePopupWindow();
            }
        });


*/
           ///  THIS IS FOR DATA BASE AND GET DATA FRON ASSETS DATA BASE  THIS IS FOR DISPLAY NEWS FROM  NEWS TABLE IN RECYCLE VIEW
        // getActivity().setTitle("News");
        DataBaseHelper dataBaseHelper=new DataBaseHelper(context);
        DataBaseManager dataBaseManager=new DataBaseManager();
        /*
        String query="select e.*, \n" +
                "case when temp.comment_count is null then 0 else temp.comment_count end c_count, \n" +
                "case when temp2.like_count is null then 0 else temp2.like_count end l_count \n" +
                "from news e  left join (SELECT count(1) comment_count, news_id from commentnews group by news_id ) temp on  e.news_id = temp.news_id left join (SELECT count(1) like_count, news_id from likenews group by news_id ) temp2 on  e.news_id = temp2.news_id   ";
        */


        newsList= dataBaseManager.getNews(dataBaseHelper,"");
        totalRecords = newsList.size();


        newssearch=(EditText)v.findViewById(R.id.newssearch);
        RecyclerView recList = (RecyclerView)v.findViewById(R.id.cardList);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        NewsAdapter newsAdapter=new NewsAdapter(newsList,getActivity());

        if(getActivity()==null)
        {

            // Toast.makeText(getContext(),"I AM I NULL REFRENCE",Toast.LENGTH_LONG).show();

        }
        recList.setAdapter(newsAdapter);
        newsAdapter.notifyDataSetChanged();

        // Toast.makeText(getContext(),"on create activity 4",Toast.LENGTH_LONG).show();


        newssearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {


                String news = s.toString();
                List<News> listnews = filter(news);
                RecyclerView recList = (RecyclerView) v.findViewById(R.id.cardList);

                LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                recList.setLayoutManager(llm);
                NewsAdapter newsAdapter = new NewsAdapter(listnews, getActivity());
                recList.setAdapter(newsAdapter);
                newsAdapter.notifyDataSetChanged();
                // recList.noti


            }


        });


        searchbutton=(ImageButton)v.findViewById(R.id.pinButton);


        searchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(),AdvSearchNews.class);
                startActivityForResult(i, 1);
            }
        });

        // Vijesh code : for pull to refresh functionality by using
        // Swipe recycle view
        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout)v. findViewById(R.id.swipeRefreshLayout);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                pullToRefresh();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        return  v;
    }



    public void pullToRefresh() {
        // Send the network request to fetch the updated data
        // If network is available then sync device data on server
        SyncUtil syncUtil = new SyncUtil(getActivity(), this);
        syncUtil.syncDeviceToServer();
       // getActivity().finish();
        swipeContainer.setRefreshing(false);


    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        // Toast.makeText(getContext(),"on create activity 2",Toast.LENGTH_LONG).show();




    }

    @Override
    public void onResume() {
        super.onResume();


        // Toast.makeText(getContext(),"on create activity 3",Toast.LENGTH_LONG).show();

    }


    // THIS IS FOR CREATION OF POPUP WINDOW ALL FUNCTION OF POP UP WINDOW  WRITTEN IN THIS FUNCTIONS




    public List<News> filter(String searchChar)
    {
        // List<News>  news=new ArrayList<>();


        String whereCondition = " and ( news_heading like '%" + searchChar + "%' or  s.name like '%" + searchChar + "%' or  c.name like '%" + searchChar + "%')";



        //String whereCondition =searchquery.concat( " and news_heading like '%" + searchChar + "%'");

        /*
        // user must enter at least 3 character to search
        if(searchChar == null || searchChar.length() < 3){
            whereCondition = "";
        }
        */

        DataBaseHelper dataBaseHelper=new DataBaseHelper(context);
        DataBaseManager  dataBaseManager=new DataBaseManager();
        /*
        String query="select e.*, \n" +
                "case when temp.comment_count is null then 0 else temp.comment_count end c_count, \n" +
                "case when temp2.like_count is null then 0 else temp2.like_count end l_count \n" +
                "from news e  left join (SELECT count(1) comment_count, news_id from commentnews group by news_id ) temp on  e.news_id = temp.news_id left join (SELECT count(1) like_count, news_id from likenews group by news_id ) temp2 on  e.news_id = temp2.news_id   ";
        */

        // String whereCondition = " where profile_id = 1 ";


        newsList= dataBaseManager.getNews(dataBaseHelper, whereCondition);



        return newsList;

    }

    @Override
    public void refeshWhenScroll(boolean forshRefresh) {


        Log.d(Constants.TAG, "Page show to user");

        if(forshRefresh){
            newssearch.setText("");
        }

        Editable s = newssearch.getText();
        String stringforfilter = s.toString();

        // user has not perfored any search
        if (stringforfilter.equals("")) {

            // Log.d("Tab22222", "s is emapy" + totalRecords + " : " + eventlist.size());

            // when user scroll the tabs
            if (forshRefresh || totalRecords != newsList.size()) {

                searchquery="";
                Log.d(Constants.TAG, "refeshData:i am in news refresh");
                DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
                DataBaseManager dataBaseManager = new DataBaseManager();
                newsList = dataBaseManager.getNews(dataBaseHelper, searchquery);


                RecyclerView recList = (RecyclerView) v.findViewById(R.id.cardList);
                LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                recList.setLayoutManager(llm);
                NewsAdapter newsAdapter = new NewsAdapter(newsList, getActivity());
                recList.setAdapter(newsAdapter);
                newsAdapter.notifyDataSetChanged();
                Log.d(Constants.TAG, "refeshData: News refresh end ");

                totalRecords = newsList.size();


            }

        }
        /*
        else {

            if (forshRefresh) {

                newsList = filter(stringforfilter);

                RecyclerView recList = (RecyclerView) v.findViewById(R.id.cardList);
                LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                recList.setLayoutManager(llm);
                NewsAdapter newsAdapter = new NewsAdapter(newsList, getActivity());
                recList.setAdapter(newsAdapter);
                newsAdapter.notifyDataSetChanged();
                Log.d(Constants.TAG, "refeshData: News refresh end ");

            }
        }
        */
    }

    @Override
    public void refreshWithAdvSearch() {


        Editable s = newssearch.getText();
        String stringforfilter = s.toString();

        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        DataBaseManager dataBaseManager = new DataBaseManager();


        // user has not perfored any search
        if (stringforfilter.equals("")) {
            Log.d(Constants.TAG, "refreshData: "+searchquery);
            newsList = dataBaseManager.getNews(dataBaseHelper, searchquery);

        }else{
            newsList = filter(stringforfilter);
        }


        // searchquery="";
        RecyclerView recList = (RecyclerView) v.findViewById(R.id.cardList);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        NewsAdapter newsAdapter = new NewsAdapter(newsList, getActivity());
        recList.setAdapter(newsAdapter);
        newsAdapter.notifyDataSetChanged();
        Log.d(Constants.TAG, "refeshData: News refresh end ");


    }

    @Override
    public void syncResult(boolean result, int responseCode, String msg) {

        if(result){
            refeshWhenScroll(true);
        }

    }


}



