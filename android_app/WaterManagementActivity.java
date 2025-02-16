package com.example.wheatyieldpredictionproject;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class WaterManagementActivity extends AppCompatActivity {

    private Spinner spinnerSoilType, spinnerSoilMoisture, spinnerIrrigationSource, spinnerCropStage;
    private Button btnGetGuidance;
    private ListView listViewGuidance;
    private ArrayAdapter<String> guidanceAdapter;
    private List<String> guidanceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_management);

        // Initialize Spinners
        spinnerSoilType = findViewById(R.id.spinner_soil_type);
        spinnerSoilMoisture = findViewById(R.id.spinner_soil_moisture);
        spinnerIrrigationSource = findViewById(R.id.spinner_irrigation_source);
        spinnerCropStage = findViewById(R.id.spinner_crop_stage);
        btnGetGuidance = findViewById(R.id.btn_get_guidance);
        listViewGuidance = findViewById(R.id.listview_guidance);

        // Spinner Data
        String[] soilTypes = {"Clay", "Loam", "Sandy"};
        String[] soilMoistureLevels = {"Dry", "Moderate", "Wet"};
        String[] irrigationSources = {"Canal", "Borewell", "Rainfed", "Drip Irrigation", "Sprinkler"};
        String[] cropStages = {"Germination", "Tillering", "Flowering", "Grain Filling", "Maturity"};

        // Set Adapters
        setSpinnerAdapter(spinnerSoilType, soilTypes);
        setSpinnerAdapter(spinnerSoilMoisture, soilMoistureLevels);
        setSpinnerAdapter(spinnerIrrigationSource, irrigationSources);
        setSpinnerAdapter(spinnerCropStage, cropStages);

        // Initialize ListView
        guidanceList = new ArrayList<>();
        guidanceAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, guidanceList);
        listViewGuidance.setAdapter(guidanceAdapter);

        // Button Click Listener
        btnGetGuidance.setOnClickListener(view -> generateGuidance());
    }

    private void setSpinnerAdapter(Spinner spinner, String[] items) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);
    }

    private void generateGuidance() {
        // Get user selections
        String soilType = spinnerSoilType.getSelectedItem().toString();
        String moistureLevel = spinnerSoilMoisture.getSelectedItem().toString();
        String irrigationSource = spinnerIrrigationSource.getSelectedItem().toString();
        String cropStage = spinnerCropStage.getSelectedItem().toString();

        // Clear previous guidance
        guidanceList.clear();

        // Generate Guidance based on selections
        if (moistureLevel.equals("Dry")) {
            guidanceList.add("Increase irrigation frequency for " + soilType + " soil.");
        } else if (moistureLevel.equals("Wet")) {
            guidanceList.add("Reduce irrigation, soil already has enough moisture.");
        }

        if (irrigationSource.equals("Drip Irrigation")) {
            guidanceList.add("Drip irrigation is optimal for water conservation.");
        } else if (irrigationSource.equals("Rainfed")) {
            guidanceList.add("Consider water harvesting techniques.");
        }

        if (cropStage.equals("Flowering") || cropStage.equals("Grain Filling")) {
            guidanceList.add("This is a critical stage, ensure adequate water supply.");
        } else if (cropStage.equals("Maturity")) {
            guidanceList.add("Reduce irrigation as the crop is nearing harvest.");
        }

        // Notify adapter to refresh ListView
        guidanceAdapter.notifyDataSetChanged();
    }
}
