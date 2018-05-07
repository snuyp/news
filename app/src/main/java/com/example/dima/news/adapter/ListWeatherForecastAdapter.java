package com.example.dima.news.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dima.news.R;
import com.example.dima.news.model.weather.List;
import com.example.dima.news.model.weather.Main;
import com.squareup.picasso.Picasso;

/**
 * Created by Dima on 02.05.2018.
 */
class ListWeatherForecastViewHolder extends RecyclerView.ViewHolder {
    public TextView t, tMin, tMax, date, time;
    public ImageView weatgerImage;

    public ListWeatherForecastViewHolder(View itemView) {
        super(itemView);
        t = itemView.findViewById(R.id.weather_forecast_t);
        date = itemView.findViewById(R.id.weather_forecast_date);
        time = itemView.findViewById(R.id.weather_forecast_time);
        weatgerImage = itemView.findViewById(R.id.weather_forecast_image);
    }
}


public class ListWeatherForecastAdapter extends RecyclerView.Adapter<ListWeatherForecastViewHolder> {
    private Context context;
    private java.util.List<List> weatherForecastList;

    public ListWeatherForecastAdapter(Context context, java.util.List<List> weatherForecastList) {
        this.context = context;
        this.weatherForecastList = weatherForecastList;
    }

    @NonNull
    @Override
    public ListWeatherForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.weather_forecast_card_layout, parent, false);

        return new ListWeatherForecastViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ListWeatherForecastViewHolder holder, int position) {
        holder.t.setText(weatherForecastList.get(position).getMain().getTemp());
        String [] date = weatherForecastList.get(position).getDtTxt().split(" "); //0 - date, 1 - time
        holder.date.setText(date[0]);
        holder.time.setText(date[1]);
        Picasso.with(context)
                .load(weatherForecastList.get(position).getWeather().get(0).getIcon())
                .into(holder.weatgerImage);
    }

    @Override
    public int getItemCount() {
        return weatherForecastList.size();
    }
}

