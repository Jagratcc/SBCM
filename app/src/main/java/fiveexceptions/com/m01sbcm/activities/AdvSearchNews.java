package fiveexceptions.com.m01sbcm.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import fiveexceptions.com.m01sbcm.R;
import fiveexceptions.com.m01sbcm.database.DataBaseHelper;
import fiveexceptions.com.m01sbcm.database.DataBaseManager;
import fiveexceptions.com.m01sbcm.model.News;
import fiveexceptions.com.m01sbcm.model.Region;
import fiveexceptions.com.m01sbcm.model.Source;
import fiveexceptions.com.m01sbcm.utility.Utility;

public class AdvSearchNews extends AppCompatActivity {



    List<News> newsList=new ArrayList<News>();
    List<Source> sourceList;
    List<Region> regionlist;


    View view;
    PopupWindow pw;
    Spinner rigionspinner;
    Spinner sourcespinner;
    ImageButton close;

    Button search;
    EditText addedsince;
    String button;

    int Regionvalue;
    int Sourcevalue;
    AutoCompleteTextView actv;
    View v;




















    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popupnewsdemo);

        // Log.d("popup init" , "invoke");
        button=new String("");

        ///this to list for store object  of regin and source class
        sourceList=new ArrayList<>();
        /////////////////this object add becouse if we want all data  from database so we will get all id of this object we understand user want all data
        Source s= new Source(123456789,this.getResources().getString(R.string.Any));
        sourceList.add(s);
        regionlist=new ArrayList<>();
        /////////////////this object add becouse if we want all data  from database so we will get all id of this object we understand user want all data
        Region r=new Region(123456789,this.getResources().getString(R.string.Any));
        regionlist.add(r);




        //THIS IS FOR GET DATA  FROM SOURCE TABLE DISPLAY  AT SOURCESPPINNER

        try {


            DataBaseHelper dataBaseHelper = new DataBaseHelper(AdvSearchNews.this);
            DataBaseManager dataBaseManager=new DataBaseManager();
            sourceList.addAll(dataBaseManager.getsource(dataBaseHelper));


////////////////////THIS IS FOR  GET DATA FROM  Region table   for display at Regionsppiner


            regionlist.addAll(dataBaseManager.getRegion(dataBaseHelper));



            //////////////// this is for  get name from object to display at adapter  at the name store  in list of string

            List<String> listregionString = new ArrayList<String>();


            for (int i = 0; i < regionlist.size(); i++)

            {
                r = regionlist.get(i);
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

            //We need to get the instance of the LayoutInflater, use the context of this activity
            ////// this is inflating popupevent xml
          //  LayoutInflater inflater = (LayoutInflater) getActivity()
            //        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Inflate the view from a predefined XML layout
            //View layout = inflater.inflate(R.layout.popupnewsdemo,
              //      (ViewGroup) view.findViewById(R.id.news));
            // create a 300px width and 470px height PopupWindow
            int Width = Utility.getScreenWidth(this).widthPixels;
            int Hight = Utility.getScreenWidth(this).heightPixels;
          //  pw = new PopupWindow(layout, Width,  Hight , true);
            // display the popup in the center
            //pw.showAtLocation(layout, Gravity.CENTER, 0, 0);
            //  intialize  all element of popupevent xml

            addedsince = (EditText)findViewById(R.id.daybefore);
            search = (Button)findViewById(R.id.search);
            search.setOnClickListener(SearchPopupBtnClickListener);
            rigionspinner = (Spinner)findViewById(R.id.regionenews);
            sourcespinner = (Spinner)findViewById(R.id.sourcenews);

            ImageView rigionimage=(ImageView)findViewById(R.id.regionimg);
            final ImageView sourceimage=(ImageView)findViewById(R.id.sourceimag);


            actv = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextView1);
            dataBaseManager=new DataBaseManager();
            dataBaseHelper=new DataBaseHelper(AdvSearchNews.this);
            String[]  city  =dataBaseManager.getcity(dataBaseHelper);


            ArrayAdapter adapter = new ArrayAdapter(AdvSearchNews.this,android.R.layout.simple_list_item_1,city);
            actv.setAdapter(adapter);
            actv.setThreshold(1);





            rigionimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rigionspinner.performClick();
                }
            });

            sourceimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    sourcespinner.performClick();

                }
            });


            close=(ImageButton)findViewById(R.id.close);

            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                   finish();


                }
            });




            /////////////////////////  make button to same color  when popwindow  creating because  after   ever search operation  we want same color button becose we we click
            /////////////////////////  they change thier color



            //////////////////////////////////////////now set  data of rigon list in to  spinner
            ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(AdvSearchNews.this, android.R.layout.simple_list_item_1, listregionString);
            dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Drop down layout style - list view with radio button
            dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);






            ////////////////////////////////////////// now set data  of source list to spinner
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(AdvSearchNews.this, android.R.layout.simple_list_item_1, listsourceString);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Drop down layout style - list view with radio button


            // attaching data adapter to spinner
            rigionspinner.setAdapter(dataAdapter1);
            sourcespinner.setAdapter(dataAdapter);
            rigionspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    String item = parent.getItemAtPosition(position).toString();

                    for(int i=0;i<regionlist.size();i++)
                    {
                        Region r=regionlist.get(i);
                        String name= r.name;
                        if(name.equalsIgnoreCase(item))
                        {

                            Regionvalue=r.id;
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

                    for(int i=0;i<sourceList.size();i++) {
                        Source s = sourceList.get(i);
                        String name = s.name;
                        if (name.equalsIgnoreCase(item))
                        {

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


    private View.OnClickListener SearchPopupBtnClickListener= new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            if(v instanceof Button){

                Button b = (Button)v;


                if(b==search)
                {
                    Editable loc= actv.getText();
                    String text=loc.toString();

                    // String whereCondition= new String("SELECT * FROM news where 1 = 1");
                    String whereCondition=new String();

                    try {
                        Editable addscience = addedsince.getText();
                        int addsci = Integer.parseInt(addscience.toString());
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Calendar c1=Calendar.getInstance();
                        c1.add(Calendar.DATE,-addsci);
                        Date dateAfter=c1.getTime();
                        String datebeforeString = sdf.format(dateAfter);


                        Calendar c2=Calendar.getInstance();
                        Date datetodate=c2.getTime();
                        String todateString = sdf.format(datetodate);


                        whereCondition=whereCondition.concat(" and news_add_date BETWEEN '"+datebeforeString+"' AND '"+todateString+"'");

                    }

                    catch ( RuntimeException el)
                    {
                        whereCondition=whereCondition;

                    }




                    if(Regionvalue==123456789)
                    {
                        whereCondition=whereCondition;

                    }

                    else
                    {
                        // region id does not exist
                        whereCondition=whereCondition.concat(" and news_region_id = "+Regionvalue);
                    }


                    if(Sourcevalue==123456789)
                    {
                        whereCondition=whereCondition;

                    }

                    else
                    {

                        whereCondition=whereCondition.concat(" and source_id = "+Sourcevalue);
                    }


                    Log.d("TAG", "onClick: "+text);

                    if(text.equals(""))
                    {

                        whereCondition=whereCondition;

                    }

                    else
                    {
                        // event place column does not exist
                         whereCondition=whereCondition.concat(" and c.name LIKE '%"+text+"%'");
                    }

                    Tab3.searchquery  = whereCondition;

                    Intent returnIntent = getIntent();
                    returnIntent.putExtra("flag", true);
                    setResult(Activity.RESULT_OK, returnIntent);


                    Log.d("TAG"," "+whereCondition);
                    // Toast.makeText(getActivity(), whereCondition, Toast.LENGTH_LONG).show();

                      /*DataBaseHelper dataBaseHelper = new DataBaseHelper(AdvSearchNews.this);
                    DataBaseManager dataBaseManager=new DataBaseManager();
                    newsList=dataBaseManager.getNews(dataBaseHelper,"where 1=1"+whereCondition);

                    RecyclerView recList = (RecyclerView)view.findViewById(R.id.cardList);

                    LinearLayoutManager llm = new LinearLayoutManager(AdvSearchNews.this);
                    llm.setOrientation(LinearLayoutManager.VERTICAL);
                    recList.setLayoutManager(llm);
                    NewsAdapter myViewAdapter=new NewsAdapter(newsList,AdvSearchNews.this);
                    recList.setAdapter(myViewAdapter);

                        */

                  //  pw.dismiss();
                    finish();



                }


            }

        }
    };























}

