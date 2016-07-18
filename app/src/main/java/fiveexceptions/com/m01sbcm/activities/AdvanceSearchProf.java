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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import fiveexceptions.com.m01sbcm.R;
import fiveexceptions.com.m01sbcm.database.DataBaseHelper;
import fiveexceptions.com.m01sbcm.database.DataBaseManager;
import fiveexceptions.com.m01sbcm.lib.RangeSeekBar;
import fiveexceptions.com.m01sbcm.model.Education;
import fiveexceptions.com.m01sbcm.model.EducationStream;
import fiveexceptions.com.m01sbcm.model.Profession;
import fiveexceptions.com.m01sbcm.model.ProfessionStream;
import fiveexceptions.com.m01sbcm.model.Region;
import fiveexceptions.com.m01sbcm.utility.Utility;

public class AdvanceSearchProf extends AppCompatActivity {

    List<Education>       educationList;
    List<EducationStream> educationStreamsList;
    List<Profession>      professionList;
    List<ProfessionStream> professionStreamList;
    List<Region>   regionList;
    ImageButton close;
    View view;
    Button bride,groom,any,search,yes,no,partial,many;
    RangeSeekBar<Integer> rangeSeekBar;
    Spinner education,educationStrem,profession,professionStream,region;
    int educationvalue,educationstreamvalue,professionId,professionstreamvalue,regionvalue;
    String  manglik=new String();
    String  type=   new String();
    String loc1= new String();
    AutoCompleteTextView actv;
    DataBaseHelper dataBaseHelper;
    DataBaseManager dataBaseManager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popupprof);

        manglik=new String("");
        type=   new String("");
        loc1= new String("");


        educationList =new ArrayList<>();
        professionList=new ArrayList<>();


      //  LayoutInflater inflater = (LayoutInflater) context
              //  .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //Inflate the view from a predefined XML layout
        //View layout = inflater.inflate(R.layout.popupprof,
               // (ViewGroup) view.findViewById(R.id.prof);
        // create a 300px width and 470px height PopupWindow
        int Width = Utility.getScreenWidth(this).widthPixels;
        int Hight = Utility.getScreenWidth(this).heightPixels;
        int popupHeight = Hight;

       // pw = new PopupWindow(layout, Width, popupHeight, true);
        // display the popup in the center
       // pw.showAtLocation(layout, Gravity.BOTTOM, 0, 0);




        actv = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextView1);
        DataBaseManager dataBaseManager=new DataBaseManager();
        DataBaseHelper  dataBaseHelper=new DataBaseHelper(AdvanceSearchProf.this);
        String[] adress  =dataBaseManager.getcity(dataBaseHelper);









       ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,adress);
        actv.setAdapter(adapter);
        actv.setThreshold(1);

        bride= (Button) findViewById(R.id.bridebutton);
        any = (Button) findViewById(R.id.anybutton);
        search = (Button) findViewById(R.id.search);
        groom= (Button) findViewById(R.id.groombutton);
        many= (Button) findViewById(R.id.many);
        yes= (Button) findViewById(R.id.myes);
        no= (Button) findViewById(R.id.mno);
        partial= (Button) findViewById(R.id.mpartial);
        rangeSeekBar=(RangeSeekBar)findViewById(R.id.view);
        education = (Spinner) findViewById(R.id.spinnereducation);
        educationStrem = (Spinner)findViewById(R.id.edustream);
        profession = (Spinner)findViewById(R.id.profession);
        professionStream = (Spinner)findViewById(R.id.profstream);
        region = (Spinner)findViewById(R.id.spinnerrigion);

        close=(ImageButton)findViewById(R.id.close);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             finish();

            }
        });




        Education educationAny=new Education(123456789,this.getResources().getString(R.string.Any));
        educationList.add(educationAny);
        educationList.addAll(dataBaseManager.getEducation(dataBaseHelper));
        final List<String> listeducationString = new ArrayList<String>();

        for (int i = 0; i < educationList.size(); i++)
        {
            Education e =educationList.get(i);
            listeducationString.add(e.getName());
        }

        ArrayAdapter<String> dataAdaptereducation = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listeducationString);
        dataAdaptereducation.setDropDownViewResource(android.R.layout.simple_list_item_1);

        education.setAdapter(dataAdaptereducation);

        education.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                String item = parent.getItemAtPosition(position).toString();


                for (int i = 0; i < listeducationString.size(); i++)
                {

                    Education r = educationList.get(i);
                    String name = r.getName();
                    if (name.equalsIgnoreCase(item)) {

                        educationvalue = r.getId();
                        break;

                    }

                }

                updateEductionStreamSpiner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


            //You need to add the following line for this solution to work; thanks skayred

        });



        ////////////////////THIS IS FOR  GET DATA FROM  Region table   for display at Regionsppiner




        Profession pr=new Profession(123456789,this.getResources().getString(R.string.Any));
        professionList.add(pr);
        professionList.addAll(dataBaseManager.getProfession(dataBaseHelper));

        final List<String> listprofessionString = new ArrayList<String>();


        for (int i = 0; i < professionList.size(); i++)

        {
            Profession s = professionList.get(i);


            listprofessionString.add(s.getName());

        }


        ArrayAdapter<String> dataAdapterprofession = new ArrayAdapter<String>(AdvanceSearchProf.this, android.R.layout.simple_list_item_1, listprofessionString);
        dataAdapterprofession.setDropDownViewResource(android.R.layout.simple_list_item_1);


        profession.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String item = parent.getItemAtPosition(position).toString();
                Log.d("TAG", "updateProfessionStreamSpiner: "+position);


                for (int i = 0; i < listprofessionString.size(); i++) {
                    Profession r = professionList.get(i);
                    String name = r.getName();
                    if (name.equalsIgnoreCase(item)) {

                        professionId = r.getId();
                        break;

                    }


                }
                updateProfessionStreamSpiner();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        profession.setAdapter(dataAdapterprofession);


        ImageView educationimage=(ImageView)findViewById(R.id.imageeducation);
        final ImageView educationstreamimag  =(ImageView)findViewById(R.id.imageeducationstream);
        ImageView professionimage=(ImageView)findViewById(R.id.imageprofession);
        ImageView professionstreamimag  =(ImageView)findViewById(R.id.imageprofessionstram);
        ImageView rigionimage  =(ImageView)findViewById(R.id.imagrigion);


        educationimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                education.performClick();

            }
        });

        educationstreamimag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                educationStrem.performClick();

            }
        });

        professionimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                profession.performClick();
            }
        });

        professionstreamimag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                professionStream.performClick();

            }
        });

        rigionimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                region.performClick();

            }
        });



        bride.setBackground(getResources().getDrawable(R.drawable.button));
        groom.setBackground(getResources().getDrawable(R.drawable.button));
        any.setBackground(getResources().getDrawable(R.drawable.button_sel));
        any.setTextColor(getResources().getColor(R.color.white));
        bride.setTextColor(getResources().getColor(R.color.black));
        groom.setTextColor(getResources().getColor(R.color.black));




        no.setBackground(getResources().getDrawable(R.drawable.button));
        many.setBackground(getResources().getDrawable(R.drawable.button_sel));
        yes.setBackground(getResources().getDrawable(R.drawable.button));
        partial.setBackground(getResources().getDrawable(R.drawable.button));
        many.setTextColor(getResources().getColor(R.color.white));
        no.setTextColor(getResources().getColor(R.color.black));
        partial.setTextColor(getResources().getColor(R.color.black));
        yes.setTextColor(getResources().getColor(R.color.black));




        groom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type=new String("groom");

                bride.setBackground(getResources().getDrawable(R.drawable.button));
                groom.setBackground(getResources().getDrawable(R.drawable.button_sel));
                any.setBackground(getResources().getDrawable(R.drawable.button));
                groom.setTextColor(getResources().getColor(R.color.white));
                bride.setTextColor(getResources().getColor(R.color.black));
                any.setTextColor(getResources().getColor(R.color.black));


            }
        });


        bride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type=new String("bride");
                bride.setBackground(getResources().getDrawable(R.drawable.button_sel));
                groom.setBackground(getResources().getDrawable(R.drawable.button));
                any.setBackground(getResources().getDrawable(R.drawable.button));
                bride.setTextColor(getResources().getColor(R.color.white));
                any.setTextColor(getResources().getColor(R.color.black));
                groom.setTextColor(getResources().getColor(R.color.black));


            }
        });

        any.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type=new String("any");
                bride.setBackground(getResources().getDrawable(R.drawable.button));
                groom.setBackground(getResources().getDrawable(R.drawable.button));
                any.setBackground(getResources().getDrawable(R.drawable.button_sel));
                any.setTextColor(getResources().getColor(R.color.white));
                bride.setTextColor(getResources().getColor(R.color.black));
                groom.setTextColor(getResources().getColor(R.color.black));

            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                manglik=new String("yes");
                many.setBackground(getResources().getDrawable(R.drawable.button));
                yes.setBackground(getResources().getDrawable(R.drawable.button_sel));
                no.setBackground(getResources().getDrawable(R.drawable.button));
                partial.setBackground(getResources().getDrawable(R.drawable.button));
                yes.setTextColor(getResources().getColor(R.color.white));
                many.setTextColor(getResources().getColor(R.color.black));
                partial.setTextColor(getResources().getColor(R.color.black));
                no.setTextColor(getResources().getColor(R.color.black));


            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manglik=new String("no");
                many.setBackground(getResources().getDrawable(R.drawable.button));
                no.setBackground(getResources().getDrawable(R.drawable.button_sel));
                yes.setBackground(getResources().getDrawable(R.drawable.button));
                partial.setBackground(getResources().getDrawable(R.drawable.button));
                no.setTextColor(getResources().getColor(R.color.white));
                many.setTextColor(getResources().getColor(R.color.black));
                partial.setTextColor(getResources().getColor(R.color.black));
                yes.setTextColor(getResources().getColor(R.color.black));


            }
        });


        many.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manglik=new String("ANY");

                no.setBackground(getResources().getDrawable(R.drawable.button));
                many.setBackground(getResources().getDrawable(R.drawable.button_sel));
                yes.setBackground(getResources().getDrawable(R.drawable.button));
                partial.setBackground(getResources().getDrawable(R.drawable.button));
                many.setTextColor(getResources().getColor(R.color.white));
                no.setTextColor(getResources().getColor(R.color.black));
                partial.setTextColor(getResources().getColor(R.color.black));
                yes.setTextColor(getResources().getColor(R.color.black));

            }
        });


        partial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manglik=new String("partial");

                no.setBackground(getResources().getDrawable(R.drawable.button));
                partial.setBackground(getResources().getDrawable(R.drawable.button_sel));
                yes.setBackground(getResources().getDrawable(R.drawable.button));
                many.setBackground(getResources().getDrawable(R.drawable.button));
                partial.setTextColor(getResources().getColor(R.color.white));
                no.setTextColor(getResources().getColor(R.color.black));
                many.setTextColor(getResources().getColor(R.color.black));
                yes.setTextColor(getResources().getColor(R.color.black));


            }
        });



        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Editable loc =actv.getText();
                loc1=loc.toString();

                Integer max = rangeSeekBar.getSelectedMaxValue();
                Integer min = rangeSeekBar.getSelectedMinValue();

                // String   whereCondition=new String("SELECT * FROM profile where 1=1")
                String   whereCondition= "";


                // Looking for
                if(type.equals("any") && type.equals("")){
                    whereCondition=whereCondition;
                }
                if(type.equals("groom")){
                    whereCondition=whereCondition.concat(" and gender =1");
                }
                if(type.equals("bride")){
                    whereCondition=whereCondition.concat(" and gender=0");
                }

                // Age
                if(max==100 && min==0){
                    whereCondition=whereCondition;
                }else
                {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

                    Calendar c1=Calendar.getInstance();
                    c1.add(Calendar.YEAR,-max);
                    Date maxtoday=c1.getTime();
                    String tomaxString = sdf.format(maxtoday);

                    Calendar c2=Calendar.getInstance();
                    c2.add(Calendar.YEAR, -min);
                    Date mintoday=c2.getTime();
                    String tominString = sdf.format(mintoday);

                    whereCondition=whereCondition.concat(" and birth_date BETWEEN '"+tomaxString+"' AND '"+tominString+"'");




                }

                if(educationvalue==123456789)
                {

                    whereCondition=whereCondition;

                }


                else

                {

                    whereCondition=whereCondition.concat(" and p.education_id = '"+educationvalue+"'");

                }



                if(educationstreamvalue==123456789)
                {

                    whereCondition=whereCondition;

                }


                else

                {

                    whereCondition=whereCondition.concat(" and p.edu_stream_id = '"+educationstreamvalue+"'");

                }



                if(professionId==123456789)
                {

                    whereCondition=whereCondition;

                }


                else

                {

                    whereCondition=whereCondition.concat(" and p.profession_id = '"+professionId+"'");

                }


                if(professionstreamvalue==123456789)
                {

                    whereCondition=whereCondition;

                }


                else

                {

                    whereCondition=whereCondition.concat(" and p.pro_stream_id = '"+professionstreamvalue+"'");

                }


                if(type.equals("any") && type.equals(""))
                {

                    whereCondition=whereCondition;

                }


                if(manglik.equals("yes"))
                {

                    whereCondition=whereCondition.concat(" and mangalik =1");

                }



                if(manglik.equals("no"))
                {

                    whereCondition=whereCondition.concat(" and mangalik=2");


                }


                if(manglik.equals("ANY"))
                {

                    whereCondition=whereCondition;


                }

                if(manglik.equals("partial"))
                {

                    whereCondition=whereCondition.concat(" and mangalik=3");


                }

                if(regionvalue==123456789)
                {
                    whereCondition=whereCondition;

                }

                else
                {

                    whereCondition=whereCondition.concat(" and p.region_id = "+regionvalue);
                }

                if(loc1.equals(""))
                {
                    whereCondition=whereCondition;

                }

                else
                {

                    whereCondition=whereCondition.concat(" and (address1 LIKE '%"+loc1+"%'" +" or address2 LIKE '%"+loc1+"%'"+" or  c2.name  LIKE '%"+loc1+"%'"+" or  c2.name  LIKE '%"+loc1+"%')"   );
                }

                Tab1.searchquery = whereCondition;



                Log.d("TAG", " i am in main activity "+whereCondition);

                // searchquery  =searchquery.concat(whereCondition);

                /*DataBaseHelper dataBaseHelper=new DataBaseHelper(AdvanceSearchProf.this);
                DataBaseManager dataBaseManager=new DataBaseManager();
                profileList=dataBaseManager.getprofile(dataBaseHelper,whereCondition);
                //Add to layout
                RecyclerView recList = (RecyclerView)findViewById(R.id.cardList);
                LinearLayoutManager llm = new LinearLayoutManager(AdvanceSearchProf.this);
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                recList.setLayoutManager(llm);
                profileAdapter=new ProfileAdapter(profileList, AdvanceSearchProf.this);
                recList.setAdapter(profileAdapter);
                pw.dismiss();
                            */

                Intent returnIntent = getIntent();

                returnIntent.putExtra("flag", true);
                setResult(Activity.RESULT_OK, returnIntent);





                finish();




            }
        });

        regionList=new ArrayList<>();
        Region r=new Region(123456789,this.getResources().getString(R.string.Any));
        regionList.add(r);
        regionList.addAll(dataBaseManager.getRegion(dataBaseHelper));

        //////////////// this is for  get name from object to display at adapter  at the name store  in list of string

        List<String> listregionString = new ArrayList<String>();


        for (int i = 0; i < regionList.size(); i++)

        {
            r = regionList.get(i);

            listregionString.add(r.name);
        }


        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listregionString);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_list_item_1);

        region.setAdapter(dataAdapter1);

        region.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();

                for(int i=0;i<regionList.size();i++)
                {
                    Region r= regionList.get(i);
                    String name= r.name;
                    if(name.equalsIgnoreCase(item))
                    {

                        regionvalue=r.id;
                        break;

                    }

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }





    public void updateEductionStreamSpiner()
    {
        educationStreamsList=new ArrayList<>();

        if (educationvalue != 0)
        {


            DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
            DataBaseManager dataBaseManager=new DataBaseManager();
            EducationStream ed=new EducationStream(123456789,this.getResources().getString(R.string.Any));
            educationStreamsList.add(ed);
            educationStreamsList.addAll(dataBaseManager.getEducationStream(dataBaseHelper, educationvalue));


            final List<String> listeducationstreamString = new ArrayList<String>();


            for (int i = 0; i < educationStreamsList.size(); i++)
            {
                EducationStream s = educationStreamsList.get(i);
                listeducationstreamString.add(s.getName());
            }



            ////////////////////////////////////////// now set data  of source list to spinner
            ArrayAdapter<String> dataAdaptereducationstream = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listeducationstreamString);
            dataAdaptereducationstream.setDropDownViewResource(android.R.layout.simple_list_item_1);
            // Drop down layout style - list view with radio button

            educationStrem.setAdapter(dataAdaptereducationstream);

            educationStrem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                    String item = parent.getItemAtPosition(position).toString();

                    for (int i = 0; i < listeducationstreamString.size(); i++) {
                        EducationStream r = educationStreamsList.get(i);
                        String name = r.getName();
                        if (name.equalsIgnoreCase(item)) {
                            educationstreamvalue = r.getId();
                            break;
                        }

                    }


                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });





        }



    }






    public void  updateProfessionStreamSpiner() {
        professionStreamList = new ArrayList<>();


        DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
        DataBaseManager dataBaseManager = new DataBaseManager();

        ProfessionStream pr = new ProfessionStream(123456789,this.getResources().getString(R.string.Any));
        professionStreamList.add(pr);

        professionStreamList.addAll(dataBaseManager.getProfessionStream(dataBaseHelper, professionId));

        ////////////////////////////////  same thing for source

        /////////////////////////////////////////////////////////////////

        final List<String> listprofessionstreamString = new ArrayList<String>();
        Log.d("TAG", "updateProfessionStreamSpiner: " + professionStreamList.size());


        for (int i = 0; i < professionStreamList.size(); i++)

        {
            ProfessionStream s = professionStreamList.get(i);


            listprofessionstreamString.add(s.getName());
        }
        Log.d("TAG", "updateProfessionStreamSpiner: "+listprofessionstreamString.size());

        ArrayAdapter<String> dataAdapterprofessionstream = new ArrayAdapter<String>(AdvanceSearchProf.this, android.R.layout.simple_list_item_1, listprofessionstreamString);
        dataAdapterprofessionstream.setDropDownViewResource(android.R.layout.simple_list_item_1);
        // Drop down layout style - list view with radio button


        professionStream.setAdapter(dataAdapterprofessionstream);



        professionStream.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String item = parent.getItemAtPosition(position).toString();

                for (int i = 0; i < listprofessionstreamString.size(); i++) {
                    ProfessionStream r = professionStreamList.get(i);
                    String name = r.getName();
                    if (name.equalsIgnoreCase(item)) {

                        professionstreamvalue = r.getId();
                        break;

                    }

                }

            }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });



        }












            }


