package com.example.dima.news.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dima.news.R;
import com.example.dima.news.mvp.model.weather.ForecastList;
import com.squareup.picasso.Picasso;

class ListWeatherForecastViewHolder extends RecyclerView.ViewHolder {
    TextView t,time,humidity;
    ImageView weatgerImage;


    ListWeatherForecastViewHolder(View itemView) {
        super(itemView);
        t = itemView.findViewById(R.id.weather_forecast_t);
        humidity = itemView.findViewById(R.id.humidity);

//        date = itemView.findViewById(R.id.weather_forecast_date);
        time = itemView.findViewById(R.id.weather_forecast_time);
        weatgerImage = itemView.findViewById(R.id.weather_forecast_image);
    }
}


public class ListWeatherForecastAdapter extends RecyclerView.Adapter<ListWeatherForecastViewHolder> {
    private java.util.List<ForecastList> weatherForecastList;

    public ListWeatherForecastAdapter(java.util.List<ForecastList> weatherForecastList) {
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
        holder.t.setText(weatherForecastList.get(position).getMain().getStringTemp());

        holder.humidity.setText(
                String.format("%s %s", holder.itemView.getContext().getResources().getString(R.string.humidity),
                        weatherForecastList.get(position).getMain().getHumidity()));


         String [] date = weatherForecastList.get(position).getDtTxt().split(" "); //0 - date, 1 - time
        //holder.date.setText(date[0]);
        holder.time.setText(date[1].substring(0,5));
        Picasso.with(holder.itemView.getContext())
                .load(weatherForecastList.get(position).getWeather().get(0).getIcon())
                .into(holder.weatgerImage);
    }

    @Override
    public int getItemCount() {
        return weatherForecastList.size();
    }
}

