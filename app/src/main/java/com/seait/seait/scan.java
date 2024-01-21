package com.seait.seait;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabelerOptionsBase;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;

import java.io.IOException;
import java.util.List;

public class scan extends AppCompatActivity {
    private MaterialButton inputImageButton; // UI view
    private MaterialButton recognizeTextButton; // UI view
    private ShapeableImageView imageView; // UI view
    private EditText recognizedTextEt; // UI view
    private static final String TAG = "MAIN_TAG"; // TAG
    private Uri imageURI = null; // Uri of image from the camera/gallery.
    private static final int CAMERA_REQUEST_CODE = 100; // handles camera/gallery permissions.
    private static final int STORAGE_REQUEST_CODE = 101; // handles camera/gallery permissions.
    private String[] cameraPermissions; // Permission to pick image from camera/gallery.
    private String[] storagePermissions; // Permission to pick image from camera/gallery.
    private ProgressBar progressBar;
    private String recognizedText;
    private ImageLabeler imageLabeler;
    private TextView classification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        inputImageButton = findViewById(R.id.inputImageButton);
        recognizeTextButton = findViewById(R.id.recognizeTextButton);
        imageView = findViewById(R.id.imageView);
        classification = findViewById(R.id.textView2);
        //recognizedTextEt = findViewById(R.id.recognizedText);
        cameraPermissions = new String[]{Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        progressBar = new ProgressBar(this);

        imageLabeler = ImageLabeling.getClient(new ImageLabelerOptions.Builder()
                        .setConfidenceThreshold(0.7f)
                        .build());


        inputImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputImageDialog();
            }
        });
        recognizeTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageURI == null) {
                    Toast.makeText(scan.this, "Pick image first", Toast.LENGTH_SHORT).show();
                }

                Bitmap bitmap = loadUri(imageURI);
                imageView.setImageBitmap(bitmap);
                runClassification(bitmap);
            }
        });
    }

    private void showInputImageDialog() {
        PopupMenu popupMenu = new PopupMenu(this, inputImageButton);
        popupMenu.getMenu().add(Menu.NONE, 1, 1, "CAMERA");
        popupMenu.getMenu().add(Menu.NONE, 2, 2, "GALLERY");
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Log.d(TAG, "oneMenuItemClick: Camera clicked");
                int id = menuItem.getItemId();
                if (id == 1) {
                    if (checkCameraPermissions()) {
                        pickImageCamera();
                    } else {
                        requestCameraPermission();
                    }
                } else if (id == 2) {
                    Log.d(TAG, "onMenuItemClick: GalleryClicked");
                    if (checkStoragePermission()) {
                        pickImageFromGallery();
                    } else {
                        requestStoragePermission();
                    }
                }
                return false;
            }
        });
    }

    private void pickImageFromGallery() {
        Log.d(TAG, "pickImageFromGallery: ");

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryActivityResultLauncher.launch(intent);
    }

    private ActivityResultLauncher<Intent> galleryActivityResultLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult o) {
                            if (o.getResultCode() == Activity.RESULT_OK) {
                                Intent data = o.getData();
                                imageURI = data.getData();
                                Log.d(TAG, "onActivityResult: imageUri" + imageURI);
                                imageView.setImageURI(imageURI);
                            } else {
                                Toast.makeText(scan.this, "Cancelled",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
            );

    private void pickImageCamera() {
        Log.d(TAG, "pickImageFromCamera");
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Sample Title");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Sample Description");
        imageURI = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageURI);
        cameraActivityResultLauncher.launch(intent);
    }

    private ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                // Receives the image, if taken from the camera.
                @Override
                public void onActivityResult(ActivityResult o) {
                    if (o.getResultCode() == Activity.RESULT_OK) {
                        Log.d(TAG, "onActivityResult: imageUri " + imageURI);

                        // Image is taken.
                        imageView.setImageURI(imageURI);
                    } else {
                        Log.d(TAG, "onActivityResult: cancelled");

                        // Unsuccessful image.
                        Toast.makeText(scan.this, "Cancelled",
                                Toast.LENGTH_SHORT).show();
                    }

                }
            }
    );

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermissions() {
        boolean cameraResult = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean storageResult = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return cameraResult && storageResult;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    pickImageCamera();
                } else {
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
                }
            }
            break;
            case STORAGE_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (storageAccepted) {
                        pickImageFromGallery();
                    } else {
                        Toast.makeText(this, "Storage permission is required.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }
    }

    private void runClassification(Bitmap bitmap) {
        InputImage inputImage = InputImage.fromBitmap(bitmap, 0);
        imageLabeler.process(inputImage).addOnSuccessListener(new OnSuccessListener<List<ImageLabel>>() {
            @Override
            public void onSuccess(List<ImageLabel> imageLabels) {
                if (imageLabels.size() > 0) {
                    StringBuilder builder = new StringBuilder();
                    for (ImageLabel label : imageLabels) {
                        builder.append(label.getText())
                                .append(" : ")
                                .append(label.getConfidence())
                                .append("\n");
                    }
                    classification.setText(builder);

                } else {
                    classification.setText("Could not classify");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
    }

    private Bitmap loadUri(Uri uri) {
        Bitmap bitmap = null;

        try {
            ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), uri);
            bitmap = ImageDecoder.decodeBitmap(source);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;

    }
}