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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

import fiveexceptions.com.m01sbcm.R;
import fiveexceptions.com.m01sbcm.adapters.ProfileAdapter;
import fiveexceptions.com.m01sbcm.database.DataBaseHelper;
import fiveexceptions.com.m01sbcm.database.DataBaseManager;
import fiveexceptions.com.m01sbcm.model.Profile;
import fiveexceptions.com.m01sbcm.sync.SyncUtil;
import fiveexceptions.com.m01sbcm.utility.Constants;


/**
 * A simple {@link Fragment} subclass.
 */
public class Tab1 extends Fragment implements TabInterface, SyncInterface {


    List<Profile> profileList = new ArrayList<Profile>();
    ImageButton searchbutton;
    View view;
    EditText profilesearch;
    Context context = null;
    int totalRecords = 0;
    static String searchquery = " and 1=1 ";
    ProfileAdapter profileAdapter;
    DataBaseHelper dataBaseHelper;
    DataBaseManager dataBaseManager;

    private SwipeRefreshLayout swipeContainer=null;
    private View v;


    public Tab1() {
    }

    public void setActivityContext(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_tab1, container, false);
        this.view = v;

        dataBaseHelper = new DataBaseHelper(getActivity());
        dataBaseManager = new DataBaseManager();


        searchbutton = (ImageButton) v.findViewById(R.id.pinButton);
        profilesearch = (EditText) v.findViewById(R.id.newssearch);

        profilesearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // user must enter at least 3 char to perform search operation
                // if(s.length() < 3) return;

                String name = s.toString();
                List<Profile> list = filter(name);
                // searchquery= " and 1=1 ";
                RecyclerView recList = (RecyclerView) v.findViewById(R.id.cardList);
                LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                recList.setLayoutManager(llm);
                profileAdapter = new ProfileAdapter(list, getActivity());
                recList.setAdapter(profileAdapter);
                profileAdapter.notifyDataSetChanged();

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

        searchbutton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), AdvanceSearchProf.class);
                startActivityForResult(i, 1);
                // advanceSearchPopup();
            }
        });


        profileList = dataBaseManager.getprofile(dataBaseHelper, "");

        if (profileList != null) {
            totalRecords = profileList.size();
        }


        //Add to layout
        RecyclerView recList = (RecyclerView) v.findViewById(R.id.cardList);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        profileAdapter = new ProfileAdapter(profileList, getActivity());
        recList.setAdapter(profileAdapter);

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
        SyncUtil syncUtil = new SyncUtil(getContext(), this);
        syncUtil.syncDeviceToServer();

        swipeContainer.setRefreshing(false);


    }


    @Override
    public void refeshWhenScroll(boolean forshRefresh) {

        Log.d(Constants.TAG, "Page show to user");

        if(forshRefresh){
            profilesearch.setText("");
        }

        if (forshRefresh || totalRecords != profileList.size()) {

            searchquery = " and 1=1 ";
            DataBaseHelper dataBaseHelper = new DataBaseHelper(getActivity());
            DataBaseManager dataBaseManager = new DataBaseManager();
            profileList = dataBaseManager.getprofile(dataBaseHelper, "");
            //Add to layout
            RecyclerView recList = (RecyclerView) view.findViewById(R.id.cardList);
            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            recList.setLayoutManager(llm);
            profileAdapter = new ProfileAdapter(profileList, getActivity());
            recList.setAdapter(profileAdapter);

        }


    }

    @Override
    public void refreshWithAdvSearch() {


        Editable s = profilesearch.getText();
        String stringforfilter = s.toString();

        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        DataBaseManager dataBaseManager = new DataBaseManager();


        // user has not perfored any search
        if (stringforfilter.equals("")) {
            Log.d(Constants.TAG, "refreshData: " + searchquery);
            profileList = dataBaseManager.getprofile(dataBaseHelper, " and 1=1 " + searchquery);

        } else {
            profileList = filter(stringforfilter);
        }


        // searchquery="";
        RecyclerView recList = (RecyclerView) view.findViewById(R.id.cardList);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        ProfileAdapter profileAdapter = new ProfileAdapter(profileList, getActivity());
        recList.setAdapter(profileAdapter);
        profileAdapter.notifyDataSetChanged();
        Log.d(Constants.TAG, "refeshData: News refresh end ");


    }


    //  Normal Text search
    public List<Profile> filter(String searchChar) {

        List<Profile> list = new ArrayList<>();

        // String whereCondition = " where profile_id = 1 ";


        String whereCondition = searchquery.concat(" and (candidate_name like '%" + searchChar + "%' or  birth_city like '%" + searchChar + "%' or  edu_name like '%" + searchChar + "%' or  edstr_name like '%" + searchChar + "%' or profession_name like '%" + searchChar + "%' or education_free_text like '%" + searchChar + "%' or  profession_free_text like '%" + searchChar + "%' or stream_name like '%" + searchChar + "%' or cast_name like '%" + searchChar + "%' or profile_id like '%" + searchChar + "%'or father_name like '%" + searchChar + "%'  or  contact_number like '%" + searchChar + "%' )");

        // String whereCondition=" and candidate_name like '%" + searchChar + "%' or  birth_city like '%" + searchChar + "%' or  edu_name like '%" + searchChar +"%' or  edstr_name like '%" + searchChar + "%' or profession_name like '%\" + searchChar + \"%' or edu_name like '%" + searchChar + "%' or  edstr_name like '%" + searchChar + "%' or stream_name like '%" + searchChar + "%'";
        /*
        // user must enter at least 3 character to search
        if(searchChar == null || searchChar.length() < 3){
            return filter;
        }
        */
        Log.d(Constants.TAG, "filter: " + whereCondition);
        DataBaseHelper dataBaseHelper = new DataBaseHelper(getActivity());
        DataBaseManager dataBaseManager = new DataBaseManager();
        list = dataBaseManager.getprofile(dataBaseHelper, whereCondition);


        return list;

    }


    @Override
    public void syncResult(boolean result, int responseCode, String msg) {

        if(result){
            refeshWhenScroll(true);
        }

    }
}

