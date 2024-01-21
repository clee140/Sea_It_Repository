package com.seait.seait;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class home extends AppCompatActivity {
    private TextView textView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<Map<String, Object>> data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        textView = findViewById(R.id.textView);

        // Display images
        data = accessDatabase();

        for (Map<String, Object> map: data) {

        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navigation_profile) {
                    startActivity(new Intent(home.this, user_profile.class));
                    return true;
                }

                if (item.getItemId() == R.id.navigation_scan) {
                    startActivity(new Intent(home.this, scan.class));
                    return true;
                }

                if (item.getItemId() == R.id.navigation_signOut) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(home.this, login.class));
                    return true;
                }

                // Handle item selection logic here
                return true; // Return true to consume the selection event
            }
        });
        accessDatabase();
    }

    public List<Map<String, Object>> accessDatabase() {
        List<Map<String, Object>> docs = new List<Map<String, Object>>() {
            @Override
            public int size() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public boolean contains(@Nullable Object o) {
                return false;
            }

            @NonNull
            @Override
            public Iterator<Map<String, Object>> iterator() {
                return null;
            }

            @NonNull
            @Override
            public Object[] toArray() {
                return new Object[0];
            }

            @NonNull
            @Override
            public <T> T[] toArray(@NonNull T[] ts) {
                return null;
            }

            @Override
            public boolean add(Map<String, Object> stringObjectMap) {
                return false;
            }

            @Override
            public boolean remove(@Nullable Object o) {
                return false;
            }

            @Override
            public boolean containsAll(@NonNull Collection<?> collection) {
                return false;
            }

            @Override
            public boolean addAll(@NonNull Collection<? extends Map<String, Object>> collection) {
                return false;
            }

            @Override
            public boolean addAll(int i, @NonNull Collection<? extends Map<String, Object>> collection) {
                return false;
            }

            @Override
            public boolean removeAll(@NonNull Collection<?> collection) {
                return false;
            }

            @Override
            public boolean retainAll(@NonNull Collection<?> collection) {
                return false;
            }

            @Override
            public void clear() {

            }

            @Override
            public Map<String, Object> get(int i) {
                return null;
            }

            @Override
            public Map<String, Object> set(int i, Map<String, Object> stringObjectMap) {
                return null;
            }

            @Override
            public void add(int i, Map<String, Object> stringObjectMap) {

            }

            @Override
            public Map<String, Object> remove(int i) {
                return null;
            }

            @Override
            public int indexOf(@Nullable Object o) {
                return 0;
            }

            @Override
            public int lastIndexOf(@Nullable Object o) {
                return 0;
            }

            @NonNull
            @Override
            public ListIterator<Map<String, Object>> listIterator() {
                return null;
            }

            @NonNull
            @Override
            public ListIterator<Map<String, Object>> listIterator(int i) {
                return null;
            }

            @NonNull
            @Override
            public List<Map<String, Object>> subList(int i, int i1) {
                return null;
            }
        };
        db.collection("scans")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TACO", document.getId() + " => " + document.getData());
                                docs.add(document.getData());
                            }
                        } else {
                            Log.d("TACO", "Error getting documents: ", task.getException());
                        }
                    }
                });
        return docs;
    }
}