package fiveexceptions.com.m01sbcm.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
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
import fiveexceptions.com.m01sbcm.adapters.EventAdapter;
import fiveexceptions.com.m01sbcm.database.DataBaseHelper;
import fiveexceptions.com.m01sbcm.database.DataBaseManager;
import fiveexceptions.com.m01sbcm.model.Event;
import fiveexceptions.com.m01sbcm.sync.SyncUtil;
import fiveexceptions.com.m01sbcm.utility.Constants;


/**
 * A simple {@link Fragment} subclass.
 */
public class Tab2 extends Fragment implements TabInterface, SyncInterface
{
    View v;
    List<Event> eventlist = new ArrayList<>();
    ImageButton  searchbutton;
    Context context = null;
    EditText eventsearch;
    int totalRecords = 0;
    static String searchquery=new String();
    private SwipeRefreshLayout swipeContainer;


    public Tab2()
    {
        // Required empty public constructor
    }

    public void setActivityContext(Context context)
    {
        this.context = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,


                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment

        // Inflate the layout for this fragment

        ColorDrawable dw = new ColorDrawable(0xb0000000);
        getActivity().getWindow().setBackgroundDrawable(dw);

        v = inflater.inflate(R.layout.fragment_tab2, container, false);




        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        DataBaseManager dataBaseManager = new DataBaseManager();

        String whereCondition = "";

        Log.d(Constants.TAG, whereCondition);

        eventlist = dataBaseManager.getEvent(dataBaseHelper, whereCondition);


        totalRecords = eventlist.size();

        RecyclerView recList = (RecyclerView) v.findViewById(R.id.cardList);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        EventAdapter eventAdapter = new EventAdapter(eventlist, getActivity());
        recList.setAdapter(eventAdapter);

        eventsearch = (EditText) v.findViewById(R.id.newssearch);
        searchbutton = (ImageButton) v.findViewById(R.id.pinButton);


        searchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), AdvSearchEvent.class);
                startActivityForResult(i, 1);
            }
        });

        eventsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String edittext = s.toString();
                eventlist = filter(edittext);
                RecyclerView recList = (RecyclerView) v.findViewById(R.id.cardList);
                LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                recList.setLayoutManager(llm);
                EventAdapter eventAdapter = new EventAdapter(eventlist, getActivity());
                recList.setAdapter(eventAdapter);
                eventAdapter.notifyDataSetChanged();


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



        return v;


    }

    public void pullToRefresh() {
        // Send the network request to fetch the updated data
        // If network is available then sync device data on server

        SyncUtil syncUtil = new SyncUtil(getActivity(), this);
        syncUtil.syncDeviceToServer();
      //  getActivity().finish();
        swipeContainer.setRefreshing(false);


    }


    public List<Event>  filter(String searchChar)
    {


        String whereCondition = " and ( event_heading like '%" + searchChar + "%' OR s.name like '%" + searchChar + "%' OR  c.name like '%" + searchChar + "%') ";

        searchquery.concat(whereCondition);



        DataBaseHelper dataBaseHelper=new DataBaseHelper(context);
        DataBaseManager dataBaseManager=new DataBaseManager();
        Log.d(Constants.TAG, "filter: "+searchquery.concat(whereCondition));



        eventlist=dataBaseManager.getEvent(dataBaseHelper, searchquery.concat(whereCondition));


        return eventlist;
    }



    @Override
    public void refeshWhenScroll(boolean forshRefresh) {


        Log.d(Constants.TAG, "Page show to user");

        if(forshRefresh){
            eventsearch.setText("");
        }

        Editable s = eventsearch.getText();

        String stringforfilter = s.toString();

        // user has not perfored any search
        if (stringforfilter.equals("")) {

            // Log.d("Tab22222", "s is emapy" + totalRecords + " : " + eventlist.size());

            // when user scroll the tabs
            if (forshRefresh || totalRecords != eventlist.size()) {

                searchquery="";
                Log.d(Constants.TAG, "refeshData:i am in news refresh");
                DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
                DataBaseManager dataBaseManager = new DataBaseManager();
                eventlist = dataBaseManager.getEvent(dataBaseHelper, searchquery);


                RecyclerView recList = (RecyclerView) v.findViewById(R.id.cardList);
                LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                recList.setLayoutManager(llm);
                EventAdapter eventAdapter = new EventAdapter(eventlist, getActivity());
                recList.setAdapter(eventAdapter);
                eventAdapter.notifyDataSetChanged();
                Log.d(Constants.TAG, "refeshData: News refresh end ");

                totalRecords = eventlist.size();


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
                Log.d("TAG", "refeshData: News refresh end ");

            }
        }
        */
    }

    @Override
    public void refreshWithAdvSearch() {


        Editable s = eventsearch.getText();
        String stringforfilter = s.toString();

        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        DataBaseManager dataBaseManager = new DataBaseManager();


        // user has not perfored any search
        if (stringforfilter.equals("")) {
            Log.d(Constants.TAG, "refreshData: "+searchquery);
            eventlist = dataBaseManager.getEvent(dataBaseHelper, searchquery);

        }else{
            eventlist = filter(stringforfilter);
        }


        // searchquery="";
        RecyclerView recList = (RecyclerView) v.findViewById(R.id.cardList);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        EventAdapter eventAdapter = new EventAdapter(eventlist, getActivity());
        recList.setAdapter(eventAdapter);
        eventAdapter.notifyDataSetChanged();
        Log.d(Constants.TAG, "refeshData: News refresh end ");


    }

    @Override
    public void syncResult(boolean result, int responseCode, String msg) {

        if(result){
            refeshWhenScroll(true);
        }

    }


}





