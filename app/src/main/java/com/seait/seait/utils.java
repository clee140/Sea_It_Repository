package com.seait.seait;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.firestore.FirebaseFirestore;

public class utils {
    public static FirebaseFirestore db;
    public static String PROJECTID = "seait-411804";


    public static void initializeUtils(android.content.Context context) {
        FirebaseOptions.Builder builder = new FirebaseOptions.Builder().setProjectId(utils.PROJECTID)
                .setApplicationId("1:514107329762:android:e57867f02d306ae9ffe2bb")
                .setApiKey("AIzaSyAVerMMKQYas_N4Z1fGbJF5miE3Hd_17xk");
        FirebaseApp.initializeApp(context, builder.build());
        // Enable Firestore logging
        FirebaseFirestore.setLoggingEnabled(true);
    }
}
