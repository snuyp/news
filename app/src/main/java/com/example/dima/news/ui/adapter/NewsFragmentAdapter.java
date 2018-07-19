package com.example.dima.news.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.dima.news.R;
import com.example.dima.news.ui.fragments.BusinessNewsFragment;
import com.example.dima.news.ui.fragments.EntertainmentNewsFragment;
import com.example.dima.news.ui.fragments.GeneralNewsFragment;
import com.example.dima.news.ui.fragments.HealthNewsFragment;
import com.example.dima.news.ui.fragments.ScienceNewsFragment;
import com.example.dima.news.ui.fragments.SourceNewsFragment;
import com.example.dima.news.ui.fragments.SportsNewsFragment;

public class NewsFragmentAdapter extends FragmentPagerAdapter {
    private Context context;

    public NewsFragmentAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return SourceNewsFragment.getInstance();
            case 1:
                return ScienceNewsFragment.getInstance();
            case 2:
                return SportsNewsFragment.getInstance();
            case 3:
                return HealthNewsFragment.getInstance();
            case 4:
                return GeneralNewsFragment.getInstance();
            case 5:
                return EntertainmentNewsFragment.getInstance();
            case 6:
                return BusinessNewsFragment.getInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 7;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {//business, entertainment, general, health ,science, sports, technology
        switch (position) {
            case 0:
                return context.getString(R.string.sources);
            case 1:
                return context.getString(R.string.science);
            case 2:
                return context.getString(R.string.sports);
            case 3:
                return context.getString(R.string.health);
            case 4:
                return context.getString(R.string.general);
            case 5:
                return context.getString(R.string.entertainment);
            case 6:
                return context.getString(R.string.business);
        }
        return "";
    }
}
