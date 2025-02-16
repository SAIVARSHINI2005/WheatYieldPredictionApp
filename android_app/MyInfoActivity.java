package com.example.wheatyieldpredictionproject;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MyInfoActivity extends AppCompatActivity {

    private static final String TAG = "MyInfoActivity";
    private static final int CAMERA_PERMISSION_REQUEST = 100;
    private TextView displayName, displayEmail;
    private EditText inputLocation;
    private Button updateProfileButton, pickImageButton, takePhotoButton;
    private ImageView profileImageView;
    private Uri photoURI;
    private String currentPhotoPath;

    private ActivityResultLauncher<Intent> pickImageLauncher;
    private ActivityResultLauncher<Intent> takePhotoLauncher;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);

        displayName = findViewById(R.id.display_name);
        displayEmail = findViewById(R.id.display_email);
        inputLocation = findViewById(R.id.input_location);
        updateProfileButton = findViewById(R.id.update_profile_button);
        pickImageButton = findViewById(R.id.pick_image_button);
        takePhotoButton = findViewById(R.id.take_photo_button);
        profileImageView = findViewById(R.id.profile_image);

        sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);

        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();
                        if (selectedImageUri != null) {
                            saveImageToInternalStorage(selectedImageUri);
                        }
                    }
                });

        takePhotoLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        profileImageView.setImageURI(Uri.fromFile(new File(currentPhotoPath)));
                        sharedPreferences.edit().putString("PROFILE_IMAGE", currentPhotoPath).apply();
                    }
                });

        pickImageButton.setOnClickListener(v -> openGallery());
        takePhotoButton.setOnClickListener(v -> requestCameraPermission());
        updateProfileButton.setOnClickListener(v -> updateProfile());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserData();
    }

    private void loadUserData() {
        String name = sharedPreferences.getString("USERNAME", "");
        String email = sharedPreferences.getString("EMAIL", "");
        String location = sharedPreferences.getString("USER_LOCATION", "");
        String imagePath = sharedPreferences.getString("PROFILE_IMAGE", "");

        displayName.setText(name);
        displayEmail.setText(email);
        inputLocation.setText(location);

        if (!imagePath.isEmpty()) {
            File imgFile = new File(imagePath);
            if (imgFile.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                profileImageView.setImageBitmap(bitmap);
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageLauncher.launch(intent);
    }

    private void requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST);
        } else {
            takePhoto();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePhoto();
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.e(TAG, "Error creating image file", ex);
            }
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this, "com.example.wheatyieldpredictionproject.fileprovider", photoFile);
                currentPhotoPath = photoFile.getAbsolutePath();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                takePhotoLauncher.launch(takePictureIntent);
            }
        }
    }

    private File createImageFile() throws IOException {
        String imageFileName = "JPEG_" + System.currentTimeMillis() + "_";
        File storageDir = getFilesDir();
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    private void saveImageToInternalStorage(Uri imageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            File file = new File(getFilesDir(), "profile_image.jpg");
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            sharedPreferences.edit().putString("PROFILE_IMAGE", file.getAbsolutePath()).apply();
            profileImageView.setImageBitmap(bitmap);
        } catch (Exception e) {
            Log.e(TAG, "Error saving image: " + e.getMessage());
        }
    }

    private void updateProfile() {
        sharedPreferences.edit()
                .putString("USER_LOCATION", inputLocation.getText().toString())
                .apply();
        Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
    }
}
