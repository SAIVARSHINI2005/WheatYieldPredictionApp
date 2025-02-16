package com.example.wheatyieldpredictionproject;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;


public class FarmConnectFragment extends Fragment {

    private CardView cardGovernmentSchemes, cardWaterManagement, cardMarketPrices;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_farm_connect, container, false);

        // Initialize card views
        cardGovernmentSchemes = view.findViewById(R.id.card_government_schemes);
        cardWaterManagement = view.findViewById(R.id.card_water_management);
        cardMarketPrices = view.findViewById(R.id.card_market_prices);

        // Set click listeners
        cardGovernmentSchemes.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), GovernmentSchemeActivity.class);
            startActivity(intent);
        });


        cardWaterManagement.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), WaterManagementActivity.class);
            startActivity(intent);
        });



        cardMarketPrices.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MarketPriceTrendsActivity.class);
            startActivity(intent);
        });


        return view;
    }
}
