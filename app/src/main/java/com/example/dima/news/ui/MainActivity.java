package com.example.dima.news.ui;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.example.dima.news.R;
import com.example.dima.news.mvp.model.news.Article;
import com.example.dima.news.mvp.model.weather.CurrentWeather;
import com.example.dima.news.mvp.presenter.CategoryNewsPresenter;
import com.example.dima.news.mvp.presenter.WeatherPresenter;
import com.example.dima.news.mvp.view.CategoryNewsView;
import com.example.dima.news.mvp.view.WeatherView;
import com.example.dima.news.ui.adapter.ListNewsAdapter;
import com.example.dima.news.ui.adapter.NewsFragmentAdapter;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;

import java.util.List;

import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;

public class MainActivity extends MvpAppCompatActivity implements WeatherView, CategoryNewsView{
    @InjectPresenter
    WeatherPresenter weatherPresenter;

    @InjectPresenter
    CategoryNewsPresenter categoryNewsPresenter;

    private SpotsDialog dialog;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView cityTemp, sun, windSpeedHumidity, pressure,searchTxt;
    private ImageView weatherImage;
    private AppBarLayout appBarLayout;
    private SlidingUpPanelLayout slideUp;

    private RecyclerView lstNews;
    private RecyclerView.LayoutManager layoutManager;
    private ListNewsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Paper.init(this);
        final String city = PreferenceManager.getDefaultSharedPreferences(this).getString("city", "");

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        ViewPager mViewPager = findViewById(R.id.viewPager);

        NewsFragmentAdapter adapter = new NewsFragmentAdapter(getSupportFragmentManager(),this);
        mViewPager.setAdapter(adapter);

        TabLayout mTabLayout = findViewById(R.id.tabLayout);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTabLayout.setupWithViewPager(mViewPager);

        appBarLayout = findViewById(R.id.app_bar_layout);

        lstNews = findViewById(R.id.lst_news);
        lstNews.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        lstNews.setLayoutManager(layoutManager);

        //Weather
        cityTemp = findViewById(R.id.weather_name_t);
        windSpeedHumidity = findViewById(R.id.weather_wind_speed);
        sun = findViewById(R.id.weather_sunrise);
        pressure = findViewById(R.id.weather_pressure);
        weatherImage = findViewById(R.id.weather_image);

        slideUp = findViewById(R.id.sliding_layout);
        slideUp.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);

        searchTxt = findViewById(R.id.search_text);

        dialog = new SpotsDialog(this);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);

        LinearLayout weatherForecast = findViewById(R.id.open_weather_forecast);
        weatherForecast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WeatherForecastActivity.class);
                startActivity(intent);
            }
        });
        ImageButton closeSlide = findViewById(R.id.close_slide);
        closeSlide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideUp.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
            }
        });


        weatherPresenter.loadWeather(city);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                appBarLayout.setExpanded(false);
                return false;
            }

        });

        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                categoryNewsPresenter.loadSearchArticles(getBaseContext(),query);
                slideUp.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
                slideUp.setTouchEnabled(false);
                searchTxt.setText(query);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        if (slideUp != null &&
                (slideUp.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || slideUp.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            slideUp.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                //Common.getPreviousDateOfEndDate(3).getToday();
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;

//            case R.id.action_search:
//                // User chose the "Favorite" action, mark the current item
//                // as a favorite...
//                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


    @Override
    public void weatherView(CurrentWeather currentWeather) {
        String cityT = currentWeather.getName() + " " + currentWeather.getMain().getStringTemp().toString();
        cityTemp.setText(cityT);
        pressure.setText(
                String.format("%s %s", getResources().getString(R.string.pressure),
                        currentWeather.getMain().getPressure()));

        windSpeedHumidity.setText(
                String.format("%s %s\n%s %s", getResources().getString(R.string.wind_speed),
                        currentWeather.getWind().getSpeed(), getResources().getString(R.string.humidity),
                        currentWeather.getMain().getHumidity()));

        sun.setText(String.format("%s%s", currentWeather.getSys().getSunrise(), currentWeather.getSys().getSunset()));

        Picasso.with(getBaseContext())
                .load(currentWeather.getWeather().get(0).getIcon())
                .into(weatherImage);
    }

    @Override
    public void error() {
        Toast.makeText(this, R.string.openWeatherMap_not_working, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setRefresh(boolean isRefreshing) {

    }

    @Override
    public void onLoadResult(List<Article> articles) {
        adapter = new ListNewsAdapter(articles);
        lstNews.setAdapter(adapter);
    }

    @Override
    public void dialogShow() {
        dialog.show();
    }

    @Override
    public void dialogDismiss() {
        dialog.dismiss();
    }
}
