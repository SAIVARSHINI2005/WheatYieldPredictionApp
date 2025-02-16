package com.example.wheatyieldpredictionproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;


public class HomeFragment extends Fragment {

    private CardView cardWeather, cardFarming, cardSoil;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize CardViews
        cardWeather = root.findViewById(R.id.card_weather);
        cardFarming = root.findViewById(R.id.card_farming);
        cardSoil = root.findViewById(R.id.card_soil);

        // Set OnClickListeners for each CardView to navigate to corresponding activities
        cardWeather.setOnClickListener(v -> navigateToActivity(WeatherActivity.class));
        cardFarming.setOnClickListener(v -> navigateToActivity(FarmingActivity.class));
        cardSoil.setOnClickListener(v -> navigateToActivity(SoilHealthActivity.class));

        return root;
    }

    // Helper method to start activity
    private void navigateToActivity(Class<?> activityClass) {
        Intent intent = new Intent(getActivity(), activityClass);
        startActivity(intent);
    }
}
