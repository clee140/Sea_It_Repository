package com.seait.seait;

import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firestore.v1.WriteResult;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class utils {
    public static FirebaseFirestore db;
    public static StorageReference storage;
    public static String PROJECTID = "seait-411804";


    public static void initializeUtils(android.content.Context context) {
        FirebaseOptions.Builder builder = new FirebaseOptions.Builder().setProjectId(utils.PROJECTID)
                .setApplicationId("1:514107329762:android:e57867f02d306ae9ffe2bb")
                .setApiKey("AIzaSyAVerMMKQYas_N4Z1fGbJF5miE3Hd_17xk");
        if (FirebaseApp.getApps(context).size() == 0) {
            FirebaseApp.initializeApp(context, builder.build());
        }
        // Enable Firestore logging
        FirebaseFirestore.setLoggingEnabled(true);
        storage = FirebaseStorage.getInstance().getReference();
    }

    public static void exampleReadAndWrite() {
        // Read
        System.out.println(db.collection("scans").where(Filter.equalTo("title", "test title")).get().getResult());
        // Write
        Map<String, Object> scanData = new HashMap<String, Object>();
        Map<String, Double> labelData = new HashMap<String, Double>();
        labelData.put("ocean", 0.9);
        labelData.put("sand", 0.8);
        scanData.put("isPost", true);
        scanData.put("label", labelData);
        scanData.put("post", "HGE3mNzpuN5H2oeqV1OP");
        System.out.println(db.collection("scans").add(scanData).getResult());
    }
}
