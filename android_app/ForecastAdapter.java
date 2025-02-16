package com.example.wheatyieldpredictionproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder> {

    private List<Forecast> forecastList;

    public ForecastAdapter(List<Forecast> forecastList) {
        this.forecastList = forecastList;
    }

    @Override
    public ForecastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.forecast_item, parent, false);
        return new ForecastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ForecastViewHolder holder, int position) {
        Forecast forecast = forecastList.get(position);
        holder.dateTextView.setText(forecast.getDate());
        holder.tempTextView.setText("Temp: " + forecast.getTemperature() + "°C");
        holder.humidityTextView.setText("Humidity: " + forecast.getHumidity() + "%");
        holder.windSpeedTextView.setText("Wind Speed: " + forecast.getWindSpeed() + " m/s");
    }

    @Override
    public int getItemCount() {
        return forecastList.size();
    }

    public static class ForecastViewHolder extends RecyclerView.ViewHolder {
        public TextView dateTextView, tempTextView, humidityTextView, windSpeedTextView;

        public ForecastViewHolder(View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            tempTextView = itemView.findViewById(R.id.tempTextView);
            humidityTextView = itemView.findViewById(R.id.humidityTextView);
            windSpeedTextView = itemView.findViewById(R.id.windSpeedTextView);
        }
    }
}
