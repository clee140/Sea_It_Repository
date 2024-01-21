package com.seait.seait;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.GoogleAuthProvider;

import javax.annotation.Nullable;

public class sign_up extends AppCompatActivity {
    // Initialize variables
    SignInButton btSignIn;
    GoogleSignInClient googleSignInClient;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

//        // GOOGLE AUTH
//        System.out.println("TACO1");
//
//        // Assign variable
//        btSignIn = findViewById(R.id.sign_in_button);
//
//        // Initialize sign in options the client-id is copied from google-services.json file
//        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken("514107329762-k7ch4c5u4ammuq95f2edmsd4tgkv3fub.apps.googleusercontent.com")
//                .requestEmail()
//                .build();
//        System.out.println("TACO 2");
//        // Initialize sign in client
//        googleSignInClient = GoogleSignIn.getClient(sign_up.this, googleSignInOptions);
//
//        btSignIn.setOnClickListener((View.OnClickListener) view -> {
//            // Initialize sign in intent
//            Intent intent = googleSignInClient.getSignInIntent();
//            // Start activity for result
//            startActivityForResult(intent, 100);
//        });
//
//        System.out.println("TACO 3");
//
//        // Initialize firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
//        // Initialize firebase user
//        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
//        // Check condition
//        if (firebaseUser != null) {
//            // When user already sign in redirect to home
//            startActivity(new Intent(sign_up.this, home.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//        }

        // BUTTON SIGN UP
        Button btn = (Button)findViewById(R.id.sign_up_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve manual data
                EditText editText = (EditText) findViewById(R.id.editTextEmailAddress);
                String email = editText.getText().toString();
                editText = (EditText) findViewById(R.id.editTextUsername);
                String username = editText.getText().toString();
                editText = (EditText) findViewById(R.id.editTextPassword);
                String password = editText.getText().toString();

                // Create a new Firebase user
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(sign_up.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("Sign up success", "createUserWithEmail:success");
                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                    startActivity(new Intent(sign_up.this, home.class));
                                } else {
                                    // If sign up fails, display a message to the user.
                                    Log.w("Sign up failure", "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(sign_up.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        // Button to sign up page
        Button toLogin = (Button) findViewById(R.id.toLogin);
        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(sign_up.this, login.class));
            }
        });
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        System.out.println("TACO on activity result");
//        super.onActivityResult(requestCode, resultCode, data);
//        // Check condition
//        if (requestCode == 100) {
//            System.out.println("TACO on 100 request code");
//            // When request code is equal to 100 initialize task
//            Task<GoogleSignInAccount> signInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
//
//            // check condition
//                if (signInAccountTask.isSuccessful()) {
//                System.out.println("TACO on successful sign in");
//                // When google sign in successful initialize string
//                String s = "Google sign in successful";
//                // Display Toast
//                displayToast(s);
//                // Initialize sign in account
//                try {
//                    // Initialize sign in account
//                    GoogleSignInAccount googleSignInAccount = signInAccountTask.getResult(ApiException.class);
//                    System.out.println("TACO trying to sign in");
//                    // Check condition
//                    if (googleSignInAccount != null) {
//                        // When sign in account is not equal to null initialize auth credential
//                        AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
//                        // Check credential
//                        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<AuthResult> task) {
//                                // Check condition
//                                if (task.isSuccessful()) {
//                                    // When task is successful redirect to profile activity display Toast
//                                    startActivity(new Intent(sign_up.this, home.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//                                    displayToast("Authentication successful");
//                                } else {
//                                    // When task is unsuccessful display Toast
//                                    displayToast("Authentication Failed :" + task.getException().getMessage());
//                                }
//                            }
//                        });
//                    }
//                } catch (ApiException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//    private void displayToast(String s) {
//        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
//    }
}