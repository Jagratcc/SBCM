package fiveexceptions.com.m01sbcm.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import fiveexceptions.com.m01sbcm.activities.Tab1;
import fiveexceptions.com.m01sbcm.activities.Tab2;
import fiveexceptions.com.m01sbcm.activities.Tab3;


/**
 * Created by amit on 30/3/16.
 */
public class ViewpageAdapter extends FragmentStatePagerAdapter {
    CharSequence Titles[];
    int NumbOfTabs;
    Context context = null;
    Tab1 tab1;
    Tab2 tab2;
    Tab3 tab3;

    public ViewpageAdapter(Context context, FragmentManager fm,CharSequence[] title,int number) {
        super(fm);
        this.Titles = title;
        this.NumbOfTabs =number;
        this.context = context;

        tab1=new Tab1();
        tab1.setActivityContext(context);

        tab2=new Tab2();
        tab2.setActivityContext(context);

        tab3=new Tab3();
        tab3.setActivityContext(context);

    }

    @Override
    public Fragment getItem(int position)
    {
        if(position == 0)
        {
            return(tab3);
        }
        if(position==1)
        {
            return(tab2);
        }

        if(position==2)
        {
            return(tab1);
        }
        else
        {
            return null;
        }

    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}
