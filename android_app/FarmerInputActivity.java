package com.example.wheatyieldpredictionproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FarmerInputActivity extends AppCompatActivity {

    private EditText etRainfall, etTemperature, etNitrogen, etPhosphorus, etPotassium, etSowingDate, etHarvestingDate;
    private Spinner spinnerSoilType;
    private Button btnPredict;
    private TextView tvPredictionResult;

    private FirebaseFirestore db; // 🔥 Firestore Database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_input);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        etRainfall = findViewById(R.id.et_rainfall);
        etTemperature = findViewById(R.id.et_temperature);
        etNitrogen = findViewById(R.id.et_nitrogen);
        etPhosphorus = findViewById(R.id.et_phosphorus);
        etPotassium = findViewById(R.id.et_potassium);
        etSowingDate = findViewById(R.id.et_sowing_date);
        etHarvestingDate = findViewById(R.id.et_harvesting_date);
        spinnerSoilType = findViewById(R.id.spinner_soil_type);
        btnPredict = findViewById(R.id.btn_predict);
        tvPredictionResult = findViewById(R.id.tv_prediction_result);

        btnPredict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFarmerData();
                predictYield();
            }
        });
    }

    private void saveFarmerData() {
        String rainfall = etRainfall.getText().toString();
        String temperature = etTemperature.getText().toString();
        String nitrogen = etNitrogen.getText().toString();
        String phosphorus = etPhosphorus.getText().toString();
        String potassium = etPotassium.getText().toString();
        String sowingDate = etSowingDate.getText().toString();
        String harvestingDate = etHarvestingDate.getText().toString();
        String soilType = spinnerSoilType.getSelectedItem().toString();

        // Create a data object
        Map<String, Object> farmerData = new HashMap<>();
        farmerData.put("rainfall", rainfall);
        farmerData.put("temperature", temperature);
        farmerData.put("nitrogen", nitrogen);
        farmerData.put("phosphorus", phosphorus);
        farmerData.put("potassium", potassium);
        farmerData.put("sowingDate", sowingDate);
        farmerData.put("harvestingDate", harvestingDate);
        farmerData.put("soilType", soilType);

        // Save to Firestore
        db.collection("farmer_inputs")
                .add(farmerData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(FarmerInputActivity.this, "Data Saved Successfully!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(FarmerInputActivity.this, "Failed to Save Data", Toast.LENGTH_SHORT).show();
                });
    }

    private void predictYield() {
        double rainfall = Double.parseDouble(etRainfall.getText().toString());
        double temperature = Double.parseDouble(etTemperature.getText().toString());
        double nitrogen = Double.parseDouble(etNitrogen.getText().toString());
        double phosphorus = Double.parseDouble(etPhosphorus.getText().toString());
        double potassium = Double.parseDouble(etPotassium.getText().toString());

        // Dummy ML Model Prediction
        double linearRegressionYield = (rainfall * 0.4) + (temperature * 0.6) + (nitrogen * 1.2) + (phosphorus * 1.1) + (potassium * 1.3) + 10;
        double decisionTreeYield = (rainfall * 0.5) + (temperature * 0.7) + (nitrogen * 1.1) + (phosphorus * 1.2) + (potassium * 1.4) + 12;
        double randomForestYield = (rainfall * 0.45) + (temperature * 0.65) + (nitrogen * 1.15) + (phosphorus * 1.25) + (potassium * 1.35) + 11;

        double finalPrediction = (linearRegressionYield + decisionTreeYield + randomForestYield) / 3;

        tvPredictionResult.setText("Predicted Yield: " + String.format("%.2f", finalPrediction) + " quintals per hectare");
    }
}
