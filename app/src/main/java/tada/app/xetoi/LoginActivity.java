package tada.app.xetoi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;

import tada.app.xetoi.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    // Test binding - replace for R
    //View binding
    private ActivityLoginBinding loginBinding;

    private static final int RC_SIGN_IN = 100;
    private GoogleSignInClient googleSignInClient;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private CallbackManager callbackManager;

    private static final String TAG1 = "GOOGLE_SIGN_IN";
    private static final String TAG2 = "FACEBOOK_SIGN_IN";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(loginBinding.getRoot());

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        loginBinding.btnFacebookSignIn.setOnClickListener(v -> {
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email","user_photos","public_profile"));
            LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    Log.d(TAG2, "onSuccess: " + loginResult);
                    firebaseAuthWithFacebookAccount(loginResult.getAccessToken());
                }

                @Override
                public void onCancel() { }

                @Override
                public void onError(FacebookException error) { }
            });
        });

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();

        loginBinding.btnGoogleSignIn.setOnClickListener(v -> {
            Log.d(TAG1, "onClick: Start Google Sign In");
            Intent intent = googleSignInClient.getSignInIntent();
            startActivityForResult(intent, RC_SIGN_IN);
        });

        authStateListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                // User is signed in
                Log.d("TAG", "onAuthStateChanged:signed_in:" + user.getUid());
            } else {
                // User is signed out
                Log.d("TAG", "onAuthStateChanged:signed_out");
            }
        };
    }

    private void checkUser() {
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(user != null) {
            Log.d(TAG1, "checkUser: User has signed in before");
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    private void firebaseAuthWithFacebookAccount(AccessToken accessToken) {
        Log.d(TAG2, "firebaseAuthWithFacebookAccount: begin firebase auth with facebook account " + accessToken);
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Log.d(TAG2, "onSuccess: Signed in");

                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            //User info
                            String email = user.getEmail();

                            // Check old/new user
                            if(task.getResult().getAdditionalUserInfo().isNewUser()) {
                                Log.d(TAG2, "onSuccess: New user created: " + email);
                            }
                            else {
                                Log.d(TAG2, "onSuccess: User: " + email + "signed in");

                            }

                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        }
                        else {
                            Log.d(TAG2, "onFailure: Failed");
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN) {
            Log.d(TAG1, "onActivityResult: Google Signin Intent result");
            Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = accountTask.getResult(ApiException.class);
                Log.d(TAG1, "onActivityResult: Get result from Google sign in account");
                firebaseAuthWithGoogleAccount(account);
            }
            catch (Exception e) {
                Log.d(TAG1, "onActivityResult: " + e.getMessage());
            }
        }
    }

    private void firebaseAuthWithGoogleAccount(GoogleSignInAccount account) {
        Log.d(TAG1, "firebaseAuthWithGoogleAccount: begin firebase auth with google account " + account.getDisplayName());
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(@NonNull AuthResult authResult) {
                        Log.d(TAG1, "onSuccess: Signed in");
                        FirebaseUser user = firebaseAuth.getCurrentUser();

                        //User info
                        String email = user.getEmail();

                        // Check old/new user
                        if(authResult.getAdditionalUserInfo().isNewUser()) {
                            Log.d(TAG1, "onSuccess: New user created: " + email);
                            startActivity(new Intent(LoginActivity.this, UserDetailActivity.class));
                            finish();
                        }
                        else {
                            Log.d(TAG1, "onSuccess: User: " + email + "signed in");
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        }
                    }
                })
                .addOnFailureListener(e -> Log.d(TAG1, "onFailure: Failed: " + e.getMessage()));
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(authStateListener != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }
}