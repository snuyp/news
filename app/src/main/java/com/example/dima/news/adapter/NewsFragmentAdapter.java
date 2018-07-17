package com.example.dima.news.adapter;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.dima.news.R;
import com.example.dima.news.fragments.SourceNewsFragment;

public class NewsFragmentAdapter extends FragmentPagerAdapter{
    private Context context;

    public NewsFragmentAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0)
        {
            return SourceNewsFragment.getInstance();
        }
        else return null;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.sources);
        }
        return "";
    }
}
