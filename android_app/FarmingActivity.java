package com.example.wheatyieldpredictionproject;



import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FarmingActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FarmingAdapter adapter;
    private List<FarmingBestPractice> bestPracticesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farming);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        bestPracticesList = new ArrayList<>();
        loadFarmingBestPractices();

        adapter = new FarmingAdapter(bestPracticesList);
        recyclerView.setAdapter(adapter);
    }

    private void loadFarmingBestPractices() {
        bestPracticesList.add(new FarmingBestPractice("Soil Testing", "Test soil before planting to determine nutrient levels and pH."));
        bestPracticesList.add(new FarmingBestPractice("Proper Irrigation", "Use drip irrigation to save water and provide adequate moisture."));
        bestPracticesList.add(new FarmingBestPractice("Crop Rotation", "Rotate crops annually to prevent soil depletion and pest buildup."));
        bestPracticesList.add(new FarmingBestPractice("Pest Management", "Use organic pesticides and integrated pest management techniques."));
        bestPracticesList.add(new FarmingBestPractice("Weed Control", "Mulching helps suppress weeds and retain soil moisture."));
        bestPracticesList.add(new FarmingBestPractice("Fertilizer Usage", "Apply fertilizers based on soil test recommendations."));
        bestPracticesList.add(new FarmingBestPractice("Weather Monitoring", "Track weather conditions for better planting and harvesting decisions."));
        bestPracticesList.add(new FarmingBestPractice("Post-Harvest Handling", "Store crops in cool, dry places to maintain quality."));
    }
}
