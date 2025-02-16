package com.example.wheatyieldpredictionproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class GovernmentSchemeActivity extends AppCompatActivity {

    private Button btnPolicyDetails, btnFinancialAidDetails, btnSupportProgramDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_government_scheme);

        // Initialize Buttons
        btnPolicyDetails = findViewById(R.id.btn_policy_details);
        btnFinancialAidDetails = findViewById(R.id.btn_financial_aid_details);
        btnSupportProgramDetails = findViewById(R.id.btn_support_program_details);

        // Click Listeners
        btnPolicyDetails.setOnClickListener(view -> {
            Toast.makeText(GovernmentSchemeActivity.this, "Opening Agricultural Policy Details...", Toast.LENGTH_SHORT).show();
            openWebPage("https://visionias.in/current-affairs/news-today/2025-02-03/economics-(indian-economy)/government-announces-key-initiatives-for-agricultural-productivity-and-resilience-in-budget-2025-26");
        });

        btnFinancialAidDetails.setOnClickListener(view -> {
            Toast.makeText(GovernmentSchemeActivity.this, "Opening Farmer Financial Aid Program...", Toast.LENGTH_SHORT).show();
            openWebPage("https://schemes.vikaspedia.in/viewcontent/schemesall/schemes-for-farmers/financial-assistance-to-organic-farmers?lgn=en");
        });

        btnSupportProgramDetails.setOnClickListener(view -> {
            Toast.makeText(GovernmentSchemeActivity.this, "Opening Kisan Yojna Details...", Toast.LENGTH_SHORT).show();
            openWebPage("https://groww.in/p/savings-schemes/pm-kisan-samman-nidhi-yojana");
        });
    }

    private void openWebPage(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}
