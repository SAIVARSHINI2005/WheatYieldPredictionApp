package com.example.wheatyieldpredictionproject;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;


public class MarketPriceTrendsActivity extends AppCompatActivity {

    private TextView txtWheatPrice, txtMarketTrends;
    private Button btnCheckPrices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_price_trends);

        // Initialize Views
        txtWheatPrice = findViewById(R.id.txt_wheat_price);
        txtMarketTrends = findViewById(R.id.txt_market_trends);
        btnCheckPrices = findViewById(R.id.btn_check_prices);

        // Fetch Market Prices and Trends (Replace with actual API call)
        fetchMarketData();

        // Check More Prices Button Click
        btnCheckPrices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MarketPriceTrendsActivity.this,HomeActivity.class);
                startActivity(intent);
                // Intent to Open Detailed Price Trends (Replace with actual class)
                // startActivity(new Intent(MarketPriceTrendsActivity.this, PriceDetailsActivity.class));
            }
        });
    }

    private void fetchMarketData() {
        // Simulate Fetching Data (Replace with actual API call)
        txtWheatPrice.setText("₹2,200 per quintal ");
        txtMarketTrends.setText("Wheat prices have increased by 5% this week due to high demand.");
    }
}
