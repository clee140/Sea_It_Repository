package com.seait.seait;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class home_page_2 extends AppCompatActivity {
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page2);

        imageView = findViewById(R.id.imageView3);

        Uri imageUri = Uri.parse(getIntent().getStringExtra("image"));


        imageView.setImageURI(imageUri);







        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView2);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navigation_profile) {
                    startActivity(new Intent(home_page_2.this, user_profile.class));
                    return true;
                }

                if (item.getItemId() == R.id.navigation_scan) {
                    startActivity(new Intent(home_page_2.this, scan.class));
                    return true;
                }

                // Handle item selection logic here
                return true; // Return true to consume the selection event
            }
        });
    }
}