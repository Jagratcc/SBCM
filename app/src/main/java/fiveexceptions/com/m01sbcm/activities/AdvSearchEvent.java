package fiveexceptions.com.m01sbcm.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import fiveexceptions.com.m01sbcm.R;
import fiveexceptions.com.m01sbcm.database.DataBaseHelper;
import fiveexceptions.com.m01sbcm.database.DataBaseManager;
import fiveexceptions.com.m01sbcm.model.Event;
import fiveexceptions.com.m01sbcm.model.Source;
import fiveexceptions.com.m01sbcm.utility.Utility;

public class AdvSearchEvent extends AppCompatActivity {


    SQLiteDatabase sqLiteDatabase;
    View v;
    List<Event> eventlist = new ArrayList<>();
    // List<News> list=new ArrayList<>();
    List<Source> sourceList;
    List<fiveexceptions.com.m01sbcm.model.Region> Regionlist;
    ImageButton close;
    View view;
    Spinner rigionspinner;
    Spinner sourcespinner;
    SeekBar eventon;
    TextView days;
    Button upcoming, past, any, search;

    String button;
    int day;
    int Regionvalue;
    int Sourcevalue;

    ImageView sourceim, rigionim;


    AutoCompleteTextView actv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popupeventdemo);


        // Log.d("popup init" , "invoke");
        button = new String("");

        ///this to list for store object  of regin and source class
        sourceList = new ArrayList<>();
        /////////////////this object add becouse if we want all data  from database so we will get all id of this object we understand user want all data
        Source s = new Source(123456789, this.getResources().getString(R.string.Any));
        sourceList.add(s);
        Regionlist = new ArrayList<>();
        /////////////////this object add becouse if we want all data  from database so we will get all id of this object we understand user want all data
        fiveexceptions.com.m01sbcm.model.Region r = new fiveexceptions.com.m01sbcm.model.Region(123456789,this.getResources().getString(R.string.Any));
        Regionlist.add(r);


        day = 0;

        //THIS IS FOR GET DATA  FROM SOURCE TABLE DISPLAY  AT SOURCESPPINNER

        try {


            DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
            DataBaseManager dataBaseManager = new DataBaseManager();
            sourceList.addAll(dataBaseManager.getsource(dataBaseHelper));


            ////////////////////THIS IS FOR  GET DATA FROM  Region table   for display at Regionsppiner


            Regionlist.addAll(dataBaseManager.getRegion(dataBaseHelper));


            //////////////// this is for  get name from object to display at adapter  at the name store  in list of string

            List<String> listregionString = new ArrayList<String>();


            for (int i = 0; i < Regionlist.size(); i++)

            {
                r = Regionlist.get(i);
                Log.d("Error", "i am in for of rigion");

                listregionString.add(r.name);
            }

            ////////////////////////////////  same thing for source
            List<String> listsourceString = new ArrayList<String>();


            for (int i = 0; i < sourceList.size(); i++)

            {
                s = sourceList.get(i);


                listsourceString.add(s.name);
            }


            // create a 300px width and 470px height PopupWindow
            int Width = Utility.getScreenWidth(this).widthPixels;
            int Hight = Utility.getScreenWidth(this).heightPixels;




            actv = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextView1);
            dataBaseManager=new DataBaseManager();
            dataBaseHelper=new DataBaseHelper(AdvSearchEvent.this);
            String[]  city  =dataBaseManager.getcity(dataBaseHelper);


            ArrayAdapter adapter = new ArrayAdapter(AdvSearchEvent.this,android.R.layout.simple_list_item_1,city);
            actv.setAdapter(adapter);
            actv.setThreshold(1);







            //pw = new PopupWindow(layout, Width, Hight, true);
            // display the popup in the center
            //  pw.showAtLocation(layout, Gravity.CENTER, 0, 0);
            //  intialize  all element of popupevent xml
            days = (TextView) findViewById(R.id.day);
            upcoming = (Button) findViewById(R.id.upcoming);
            past = (Button) findViewById(R.id.past);
            any = (Button) findViewById(R.id.any);
            search = (Button) findViewById(R.id.search);
            search.setOnClickListener(SearchPopupBtnClickListener);
            upcoming.setOnClickListener(SearchPopupBtnClickListener);
            any.setOnClickListener(SearchPopupBtnClickListener);
            past.setOnClickListener(SearchPopupBtnClickListener);
            rigionspinner = (Spinner) findViewById(R.id.region);
            sourcespinner = (Spinner) findViewById(R.id.dis);

            eventon = (SeekBar) findViewById(R.id.seekBar);
            sourceim = (ImageView) findViewById(R.id.sim);
            rigionim = (ImageView) findViewById(R.id.rim);

            close = (ImageButton) findViewById(R.id.close);

            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                   finish();


                }
            });








            past.setBackground(getResources().getDrawable(R.drawable.button));
            any.setBackground(getResources().getDrawable(R.drawable.button_sel));
            upcoming.setBackground(getResources().getDrawable(R.drawable.button));
            past.setTextColor(getResources().getColor(R.color.black));
            any.setTextColor(getResources().getColor(R.color.white));
            upcoming.setTextColor(getResources().getColor(R.color.black));


            sourceim.setClickable(true);
            rigionim.setClickable(true);

            sourceim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sourcespinner.performClick();
                    // Toast.makeText(getActivity(), "click on source", Toast.LENGTH_LONG).show();
                }
            });

            rigionim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rigionspinner.performClick();
                    // Toast.makeText(getActivity(), "click on reggion", Toast.LENGTH_LONG).show();
                }
            });


            /////////////    get seekbar  value at the time of working
            eventon.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    days.setText("" + progress + "days");
                    day = progress;
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {


                }
            });

            /////////////////////////  make button to same color  when popwindow  creating because  after   ever search operation  we want same color button becose we we click
            /////////////////////////  they change thier color
            //   past.setBackgroundColor(getResources().getColor(R.color.white));
            //    any.setBackgroundColor(getResources().getColor(R.color.white));
            //    upcoming.setBackgroundColor(getResources().getColor(R.color.white));


            //////////////////////////////////////////now set  data of rigon list in to  spinner
            ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listregionString);
            dataAdapter1.setDropDownViewResource(android.R.layout.simple_list_item_1);
            // Drop down layout style - list view with radio button


            ////////////////////////////////////////// now set data  of source list to spinner
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listsourceString);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
            // Drop down layout style - list view with radio button


            // attaching data adapter to spinner
            rigionspinner.setAdapter(dataAdapter1);
            sourcespinner.setAdapter(dataAdapter);
            rigionspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    String item = parent.getItemAtPosition(position).toString();

                    for (int i = 0; i < Regionlist.size(); i++) {
                        fiveexceptions.com.m01sbcm.model.Region r = Regionlist.get(i);
                        String name = r.name;
                        if (name.equalsIgnoreCase(item)) {

                            Regionvalue = r.id;
                            break;

                        }

                    }


                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {


                }
            });

            sourcespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    String item = parent.getItemAtPosition(position).toString();

                    for (int i = 0; i < sourceList.size(); i++) {
                        Source s = sourceList.get(i);
                        String name = s.name;
                        if (name.equalsIgnoreCase(item)) {

                            Sourcevalue = s.id;
                            break;

                        }


                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private View.OnClickListener SearchPopupBtnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            if (v instanceof Button) {

                Button b = (Button) v;
                past.setBackground(getResources().getDrawable(R.drawable.button));
                any.setBackground(getResources().getDrawable(R.drawable.button));
                upcoming.setBackground(getResources().getDrawable(R.drawable.button));
                past.setTextColor(getResources().getColor(R.color.black));
                any.setTextColor(getResources().getColor(R.color.black));
                upcoming.setTextColor(getResources().getColor(R.color.black));


                if (b == upcoming) {
                    button = new String("UPCOMING");
                    upcoming.setBackground(getResources().getDrawable(R.drawable.button_sel));
                    upcoming.setTextColor(getResources().getColor(R.color.white));

                }

                if (b == past) {
                    button = new String("PAST");
                    past.setBackground(getResources().getDrawable(R.drawable.button_sel));
                    past.setTextColor(getResources().getColor(R.color.white));
                }

                if (b == any) {
                    button = new String("ANY");
                    any.setBackground(getResources().getDrawable(R.drawable.button_sel));
                    any.setTextColor(getResources().getColor(R.color.white));
                }


                if (b == search) {
                  //  Editable loc = location.getText();
                 //   String location = loc.toString();

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


                    Calendar c = Calendar.getInstance();
                    Date today = c.getTime();
                    String todateString = sdf.format(today);

                    Calendar c1 = Calendar.getInstance();
                    c1.add(Calendar.DATE, day);
                    Date dateAfter = c1.getTime();
                    String dateAfterString = sdf.format(dateAfter);

                    Calendar c2 = Calendar.getInstance();
                    c2.add(Calendar.DATE, -day);
                    Date datebefore = c2.getTime();
                    String datebeforeString = sdf.format(datebefore);

                    // String whereCondition= new String("SELECT * FROM event where 1 = 1");
                    String whereCondition = new String();

                    if (button.equals("ANY") || button.equals("")) {
                        if (day == 0) {
                            // whereCondition=whereCondition;
                        } else {

                            whereCondition = whereCondition.concat(" and event_date BETWEEN '" + datebeforeString + "' AND '" + dateAfterString + "'");
                        }
                    }


                    if (button.equals("PAST")) {
                        if (day == 0) {
                            whereCondition = whereCondition.concat(" and event_date < '" + todateString + "'");
                        } else {

                            whereCondition = whereCondition.concat(" and event_date BETWEEN '"+ datebeforeString+ "' AND '" + todateString + "'");
                            Log.d("TAG", "onClick: "+whereCondition);

                        }


                    }


                    if (button.equals("UPCOMING")) {
                        if (day == 0) {
                            whereCondition = whereCondition.concat(" and event_date > '" + todateString + "'");

                        } else {

                            whereCondition = whereCondition.concat(" and event_date BETWEEN '" + todateString + "' AND '" + dateAfterString + "'");

                        }
                    }

                    if (Regionvalue == 123456789) {
                        whereCondition = whereCondition;

                    } else {
                        //region id does not exist
                        whereCondition = whereCondition.concat(" and event_region_id = " + Regionvalue);
                    }


                    if (Sourcevalue == 123456789) {
                        // whereCondition=whereCondition;
                    } else {
                        // source id
                        whereCondition = whereCondition.concat(" and source_id = " + Sourcevalue);


                    }

               String  location    = actv.getText().toString();




                    if (location.equals("")) {
                        whereCondition = whereCondition;

                    } else {
                        // event place column does not exist
                        whereCondition = whereCondition.concat(" and c.name like '%" + location + "%'");
                    }


                    //Toast.makeText(getActivity(), button+day+Regionvalue+Sourcevalue+text+todateString, Toast.LENGTH_LONG).show();

                    //Toast.makeText(getActivity(), whereCondition, Toast.LENGTH_LONG).show();
                    Log.d("TAG", "onClick: " + whereCondition);

                    Tab2.searchquery = whereCondition;
                    Intent returnIntent = getIntent();
                    returnIntent.putExtra("flag", true);
                    setResult(Activity.RESULT_OK, returnIntent);





                    finish();




                 /*   DataBaseHelper dataBaseHelper = new DataBaseHelper(getActivity());
                    DataBaseManager dataBaseManager = new DataBaseManager();
                    eventlist = dataBaseManager.getEvent(dataBaseHelper,"where 1=1 "+whereCondition);

                    RecyclerView recList = (RecyclerView) view.findViewById(R.id.cardList);
                    LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                    llm.setOrientation(LinearLayoutManager.VERTICAL);
                    recList.setLayoutManager(llm);
                    EventAdapter eventAdapter = new EventAdapter(eventlist, getActivity());
                    recList.setAdapter(eventAdapter);

                    pw.dismiss();
*/

                }


            }
        }


    };
}
