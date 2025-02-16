package com.example.wheatyieldpredictionproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CropInfoFragment extends Fragment {

    private Button btnGetInput, btnUploadCsv;
    private ActivityResultLauncher<Intent> filePickerLauncher;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crop_info, container, false);

        btnGetInput = view.findViewById(R.id.btn_get_input);
        btnUploadCsv = view.findViewById(R.id.btn_upload_csv);

        // Navigate to Farmer Input Form
        btnGetInput.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), FarmerInputActivity.class);
            startActivity(intent);
        });

        // File picker launcher for CSV upload
        filePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                        Uri uri = result.getData().getData();
                        if (uri != null) {
                            processCSV(uri);
                        }
                    }
                });

        // Open File Picker for CSV selection
        btnUploadCsv.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("text/csv");  // Filter only CSV files
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            filePickerLauncher.launch(intent);
        });

        return view;
    }

    private void processCSV(Uri uri) {
        try {
            InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder csvData = new StringBuilder();
            String line;
            int rowCount = 0;

            while ((line = reader.readLine()) != null) {
                csvData.append(line).append("\n");
                rowCount++;

                // Show only first 3 rows in Toast (can be used for ML processing)
                if (rowCount <= 3) {
                    Toast.makeText(getActivity(), "Row " + rowCount + ": " + line, Toast.LENGTH_SHORT).show();
                }
            }
            reader.close();

            Log.d("CSV_UPLOAD", "CSV Content:\n" + csvData.toString());

            Toast.makeText(getActivity(), "CSV Uploaded! Total Rows: " + rowCount, Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Log.e("CSV_ERROR", "Error processing CSV: " + e.getMessage());
            Toast.makeText(getActivity(), "Error reading CSV file!", Toast.LENGTH_SHORT).show();
        }
    }
}
