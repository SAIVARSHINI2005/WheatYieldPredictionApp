package com.example.wheatyieldpredictionproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SoilHealthActivity extends AppCompatActivity {

    private EditText etPH, etMoisture, etOrganicMatter, etNitrogen, etPhosphorus, etPotassium;
    private Button btnSave;
    private TextView tvResult;

    private FirebaseFirestore db; // 🔥 Firestore Database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soil_health);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize UI elements
        etPH = findViewById(R.id.etPH);
        etMoisture = findViewById(R.id.etMoisture);
        etOrganicMatter = findViewById(R.id.etOrganicMatter);
        etNitrogen = findViewById(R.id.etNitrogen);
        etPhosphorus = findViewById(R.id.etPhosphorus);
        etPotassium = findViewById(R.id.etPotassium);
        btnSave = findViewById(R.id.btnSave);
        tvResult = findViewById(R.id.tvResult);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSoilHealthData();
                displaySoilHealthReport();
            }
        });
    }

    private void saveSoilHealthData() {
        String pHStr = etPH.getText().toString().trim();
        String moistureStr = etMoisture.getText().toString().trim();
        String organicMatterStr = etOrganicMatter.getText().toString().trim();
        String nitrogenStr = etNitrogen.getText().toString().trim();
        String phosphorusStr = etPhosphorus.getText().toString().trim();
        String potassiumStr = etPotassium.getText().toString().trim();

        if (pHStr.isEmpty() || moistureStr.isEmpty() || organicMatterStr.isEmpty() ||
                nitrogenStr.isEmpty() || phosphorusStr.isEmpty() || potassiumStr.isEmpty()) {
            Toast.makeText(this, "Please fill all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Store data in Firestore
        Map<String, Object> soilData = new HashMap<>();
        soilData.put("pH", pHStr);
        soilData.put("moisture", moistureStr);
        soilData.put("organicMatter", organicMatterStr);
        soilData.put("nitrogen", nitrogenStr);
        soilData.put("phosphorus", phosphorusStr);
        soilData.put("potassium", potassiumStr);

        db.collection("soil_health")
                .add(soilData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(SoilHealthActivity.this, "Soil Health Data Saved!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(SoilHealthActivity.this, "Error saving data", Toast.LENGTH_SHORT).show();
                });
    }

    private void displaySoilHealthReport() {
        try {
            double pH = Double.parseDouble(etPH.getText().toString().trim());
            double moisture = Double.parseDouble(etMoisture.getText().toString().trim());
            double nitrogen = Double.parseDouble(etNitrogen.getText().toString().trim());
            double phosphorus = Double.parseDouble(etPhosphorus.getText().toString().trim());
            double potassium = Double.parseDouble(etPotassium.getText().toString().trim());

            StringBuilder report = new StringBuilder("🔍 Soil Health Report:\n\n");

            if (pH < 6.0) report.append("⚠️ Soil is too acidic. Add lime.\n");
            else if (pH > 7.5) report.append("⚠️ Soil is too alkaline. Add organic matter.\n");
            else report.append("✅ Soil pH is good.\n");

            if (moisture < 20) report.append("⚠️ Low moisture. Increase irrigation.\n");
            else if (moisture > 40) report.append("⚠️ High moisture. Improve drainage.\n");
            else report.append("✅ Moisture level is optimal.\n");

            if (nitrogen < 20) report.append("⚠️ Low nitrogen. Apply nitrogen fertilizer.\n");
            if (phosphorus < 15) report.append("⚠️ Low phosphorus. Apply DAP fertilizer.\n");
            if (potassium < 100) report.append("⚠️ Low potassium. Apply MOP fertilizer.\n");
            if (nitrogen >= 20 && phosphorus >= 15 && potassium >= 100)
                report.append("✅ Nutrient levels are balanced.\n");

            tvResult.setText(report.toString());
            tvResult.setVisibility(View.VISIBLE);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numeric values.", Toast.LENGTH_SHORT).show();
        }
    }
}
