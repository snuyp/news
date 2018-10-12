package com.example.dima.news.ui;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
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
import com.bumptech.glide.Glide;
import com.example.dima.news.R;
import com.example.dima.news.common.Common;
import com.example.dima.news.mvp.model.news.Article;
import com.example.dima.news.mvp.model.weather.CurrentWeather;
import com.example.dima.news.mvp.presenter.CategoryNewsPresenter;
import com.example.dima.news.mvp.presenter.WeatherPresenter;
import com.example.dima.news.mvp.view.CategoryNewsView;
import com.example.dima.news.mvp.view.WeatherView;
import com.example.dima.news.ui.adapter.ListNewsAdapter;
import com.example.dima.news.ui.adapter.NewsFragmentAdapter;
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.List;

import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;
import io.nlopez.smartlocation.SmartLocation;
import io.paperdb.Paper;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends MvpAppCompatActivity implements WeatherView, CategoryNewsView {
    @InjectPresenter
    WeatherPresenter weatherPresenter;

    @InjectPresenter
    CategoryNewsPresenter categoryNewsPresenter;

    private SpotsDialog dialog;
    private TextView cityTemp, sun, pressure, searchTxt,humidity,windSpeed;
    private ImageView weatherImage,image_planet;
    private AppBarLayout appBarLayout;
    private SlidingUpPanelLayout slideUp;

    private RecyclerView lstNews;
    private RecyclerView.LayoutManager layoutManager;
    private ListNewsAdapter adapter;
    private Disposable internetDisposable;
    private Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Paper.init(this);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        ViewPager mViewPager = findViewById(R.id.viewPager);

        NewsFragmentAdapter adapter = new NewsFragmentAdapter(getSupportFragmentManager(), this);
        mViewPager.setAdapter(adapter);

        TabLayout mTabLayout = findViewById(R.id.tabLayout);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTabLayout.setupWithViewPager(mViewPager);

        onGrantedPermission();


        appBarLayout = findViewById(R.id.app_bar_layout);

        lstNews = findViewById(R.id.lst_news);
        lstNews.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        lstNews.setLayoutManager(layoutManager);

        //Weather
        cityTemp = findViewById(R.id.weather_name_t);
        windSpeed = findViewById(R.id.weather_wind_speed);
        sun = findViewById(R.id.weather_sunrise);
        pressure = findViewById(R.id.weather_pressure);
        weatherImage = findViewById(R.id.weather_image);
        humidity = findViewById(R.id.weather_humidity);

        image_planet = findViewById(R.id.image_planet);

        slideUp = findViewById(R.id.sliding_layout);
        slideUp.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);

        searchTxt = findViewById(R.id.search_text);

        dialog = new SpotsDialog(this);

        image_planet.setOnClickListener(v -> {
            startActivity(new Intent(this,WeatherForecastActivity.class));
        });

        LinearLayout weatherForecast = findViewById(R.id.open_weather_forecast);
        weatherForecast.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, WeatherForecastActivity.class);
            startActivity(intent);
        });
        ImageButton closeSlide = findViewById(R.id.close_slide);
        closeSlide.setOnClickListener(v -> slideUp.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN));
        snackbar = Snackbar.make(slideUp, R.string.check_connection, Snackbar.LENGTH_INDEFINITE);
    }
    public void getWeather()
    {
            SmartLocation.with(this).location()
                    .oneFix()
                    .start(location -> {
                        Common.lat = location.getLatitude();
                        Common.lon = location.getLongitude();
                        weatherPresenter.loadWeather(location.getLatitude(),location.getLongitude());
                    });
    }

    private void onGrantedPermission() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        getWeather();
                    }
                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) { }
                })
                .check();
    }

    @Override
    protected void onResume() {
        super.onResume();
        internetDisposable = ReactiveNetwork.observeInternetConnectivity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isConnected -> {
                    if(!isConnected) {
                       snackbar.show();
                    } else {
                       snackbar.dismiss();
                    }
                });
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
                categoryNewsPresenter.loadSearchArticles(getBaseContext(), query);
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
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_charts:
                startActivity(new Intent(this,WeatherForecastActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }


    @Override
    public void weatherView(CurrentWeather currentWeather) {
        String cityT = currentWeather.getName();
        String t = currentWeather.getMain().getStringTemp().toString();
        if(cityT.length() > 13 ) {
            cityTemp.setText(String.format("%s... %s", cityT.substring(0, 10), t));
        } else {
            cityTemp.setText(String.format("%s %s", cityT, t));
        }
        pressure.setText(
                String.format("%s %s", getResources().getString(R.string.pressure),
                        currentWeather.getMain().getPressure()));

        windSpeed.setText(String.format("%s %s",
                getResources().getString(R.string.wind_speed), currentWeather.getWind().getSpeed()));

        humidity.setText(String.format("%s %s",
                getResources().getString(R.string.humidity), currentWeather.getMain().getHumidity()));

        sun.setText(String.format("%s%s",
                currentWeather.getSys().getSunrise(), currentWeather.getSys().getSunset()));

        Glide.with(getBaseContext())
                .load(currentWeather.getWeather().get(0).getIcon())
                .into(weatherImage);
    }

    @Override
    protected void onPause() {
        super.onPause();
        safelyDispose(internetDisposable);
    }

    private void safelyDispose(Disposable... disposables) {
        for (Disposable subscription : disposables) {
            if (subscription != null && !subscription.isDisposed()) {
                subscription.dispose();
            }
        }
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
    public void error(String error) {
        Toasty.error(getBaseContext(), error, Toast.LENGTH_SHORT, true).show();
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
