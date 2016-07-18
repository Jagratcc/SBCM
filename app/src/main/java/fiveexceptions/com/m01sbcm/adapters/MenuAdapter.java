package fiveexceptions.com.m01sbcm.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import fiveexceptions.com.m01sbcm.R;
import fiveexceptions.com.m01sbcm.activities.Contact;
import fiveexceptions.com.m01sbcm.activities.Favourite;
import fiveexceptions.com.m01sbcm.activities.Login;
import fiveexceptions.com.m01sbcm.activities.MainActivity;
import fiveexceptions.com.m01sbcm.database.DataBaseManager;
import fiveexceptions.com.m01sbcm.model.User;
import fiveexceptions.com.m01sbcm.utility.Utility;

/**
 * Created by amit on 11/5/16.
 */
public class MenuAdapter extends BaseAdapter
{

    MainActivity mainActivity;
    Activity context;
    String[] result;
    int[]    imageId;
    private static LayoutInflater inflater=null;


 public MenuAdapter(Activity activity, String[] prgmNameList)
 {
     // TODO Auto-generated constructor stub
     result=prgmNameList;
     context=activity;

     if (context instanceof MainActivity) {
         // context.pager.setCurrentItem(position, true);
         mainActivity = (MainActivity)context;
     }
     //  this.pager=pager;
     inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 }


    @Override
    public int getCount() {
        return result.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }



    public class Holder
    {
        TextView tv;
        ImageView img;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.menu, null);
        holder.tv=(TextView) rowView.findViewById(R.id.textView34);
        holder.tv.setText(result[position]);

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (position < 3) {

                    if (mainActivity != null) {
                        mainActivity.pager.setCurrentItem(position, true);
                    } else {

                        Intent intent = new Intent(context, MainActivity.class);
                        intent.putExtra("TabId", position);
                        context.startActivity(intent);
                        context.finish();
                    }
                }

                if (position == 3) {
                    Intent i = new Intent(context, Favourite.class);
                    context.startActivity(i);
                    context.finish();
                }


                if (position == 4) {
                    Intent i = new Intent(context, Contact.class);
                    context.startActivity(i);
                    context.finish();
                }


                if (position == 5) {

                    User u = Utility.getuser();
                    int id = u.getUserId();


                    DataBaseManager dataBaseManager = new DataBaseManager();

                    if (mainActivity != null) {

                        Boolean B = dataBaseManager.updateuser(mainActivity, id, 0);
                        mainActivity.finish();

                    }
                    else {

                        Boolean B = dataBaseManager.updateuser(context, id, 0);
                        context.finish();
                    }



                    Utility.destroyuser();
                    Intent a = new Intent(context, Login.class);
                    a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(a);


                }

                if (mainActivity != null) {


                    mainActivity.drawerLayout.closeDrawers();
                }

                // Toast.makeText(context, "You Clicked " + result[position], Toast.LENGTH_LONG).show();
            }
        });
        return rowView;
    }





}
