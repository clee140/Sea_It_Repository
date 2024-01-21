package com.seait.seait;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.credentials.Credential;
import androidx.credentials.CustomCredential;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.PasswordCredential;
import androidx.credentials.PublicKeyCredential;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class auth extends AppCompatActivity {
    // Initialize variables
    SignInButton btSignIn;
    GoogleSignInClient googleSignInClient;
    FirebaseAuth firebaseAuth;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Assign variable
        btSignIn = findViewById(R.id.sign_in_button);

        // Initialize sign in options the client-id is copied form google-services.json file
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("438431947620-ecpi41uk3dhhf4mv8g8q993k3vs49ltm.apps.googleusercontent.com")
                .requestEmail()
                .build();

        // Initialize sign in client
        googleSignInClient = GoogleSignIn.getClient(MainActivity.this, googleSignInOptions);

        btSignIn.setOnClickListener((View.OnClickListener) view -> {
            // Initialize sign in intent
            Intent intent = googleSignInClient.getSignInIntent();
            // Start activity for result
            startActivityForResult(intent, 100);
        });

        // Initialize firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        // Initialize firebase user
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        // Check condition
        if (firebaseUser != null) {
            // When user already sign in redirect to profile activity
            startActivity(new Intent(MainActivity.this, ProfileActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }

//        Button btn = (Button)findViewById(R.id.sign_in_button);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                GetCredentialRequest request = new GetCredentialRequest.Builder()
//                        .addGetCredentialOption(googleIdOption)
//                        .build();
//
//                // Launch sign in flow and do getCredential Request to retrieve the credentials
//                credentialManager.getCredentialAsync(
//                        requireActivity(),
//                        request,
//                        cancellationSignal,
//                        <executor>,
//                        new CredentialManagerCallback<GetCredentialResponse, GetCredentialException>() {
//                            @Override
//                            public void onResult(GetCredentialResponse result) {
//                                handleSignIn(result);
//                            }
//
//                            @Override
//                            public void onError(GetCredentialException e) {
//                                handleFailure(e);
//                            }
//                        }
//);
//            }
//        });
    }

    public void handleSignIn(GetCredentialResponse result) {
        // Handle the successfully returned credential.
        Credential credential = result.getCredential();

        if (credential instanceof PublicKeyCredential) {
            String responseJson = ((PublicKeyCredential) credential).getAuthenticationResponseJson();
            // Share responseJson i.e. a GetCredentialResponse on your server to validate and authenticate
        } else if (credential instanceof PasswordCredential) {
            String username = ((PasswordCredential) credential).getId();
            String password = ((PasswordCredential) credential).getPassword();
            // Use id and password to send to your server to validate and authenticate
        } else if (credential instanceof CustomCredential) {
            if (GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL.equals(credential.getType())) {
                // Use googleIdTokenCredential and extract id to validate and
                // authenticate on your server
                GoogleIdTokenCredential googleIdTokenCredential = GoogleIdTokenCredential.createFrom(((CustomCredential) credential).getData());
            } else {
                // Catch any unrecognized custom credential type here.
                Log.e(TAG, "Unexpected type of credential");
            }
        } else {
            // Catch any unrecognized credential type here.
            Log.e(TAG, "Unexpected type of credential");
        }
    }
}
