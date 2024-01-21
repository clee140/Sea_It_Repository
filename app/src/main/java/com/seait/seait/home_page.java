package com.seait.seait;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.FileInputStream;

import javax.annotation.Nullable;

public class home_page extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        utils.initializeUtils(this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navigation_profile) {
                    startActivity(new Intent(home_page.this, user_profile.class));
                    return true;
                }

                if (item.getItemId() == R.id.navigation_scan) {
                    startActivity(new Intent(home_page.this, scan.class));
                    return true;
                }

                // Handle item selection logic here
                return true; // Return true to consume the selection event
            }
        });
    }
}