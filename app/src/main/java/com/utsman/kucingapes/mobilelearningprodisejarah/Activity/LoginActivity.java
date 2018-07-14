package com.utsman.kucingapes.mobilelearningprodisejarah.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.utsman.kucingapes.mobilelearningprodisejarah.Content.ContentActivity;
import com.utsman.kucingapes.mobilelearningprodisejarah.Content.OpiniActivity;
import com.utsman.kucingapes.mobilelearningprodisejarah.R;

public class LoginActivity extends AppCompatActivity {
    private int RC_SIGN_IN = 9001;

    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    public ProgressDialog mProgressDialog;
    private String type;
    private String node;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle("");
        SignInButton signInButton = findViewById(R.id.sign_in_button);

        setOpenMarket();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        String nah = getIntent().getStringExtra("out");
        if (nah == null) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }

        if (nah != null && nah.equals("out")) {
            FirebaseDatabase.getInstance();
        }

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    private void setOpenMarket() {
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                String value = getIntent().getExtras().getString(key);

                if (value != null && key.equals("type") && value.equals("app")) {
                    final String appPackageName = getPackageName();
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException gueganteng) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                    finish();
                }
            }
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(LoginActivity.this, "Login gagal", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        showProgressDialog();
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            user = mAuth.getCurrentUser();
                            Toast.makeText(LoginActivity.this, "Sukses", Toast.LENGTH_SHORT).show();
                            updateUI(user);

                        } else {
                            Toast.makeText(LoginActivity.this, "Gagal, cek koneksi internet anda", Toast.LENGTH_SHORT).show();
                            hideProgressDialog();
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            //showProgressDialog();
            hideProgressDialog();

            if (getIntent().getExtras() != null) {

                //type = getIntent().getStringExtra("type");
                node = getIntent().getStringExtra("node");
                id = getIntent().getIntExtra("id", 0);

                for (String key : getIntent().getExtras().keySet()) {
                    String value = getIntent().getExtras().getString(key);

                    if (value != null && key.equals("type") && value.equals("materi")) {

                        if (node.equals("data-md")) {
                            showProgressDialog();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    hideProgressDialog();
                                    Intent intent = new Intent(getApplicationContext(), ContentActivity.class);
                                    intent.putExtra("id", id);
                                    startActivity(intent);
                                    finish();
                                }
                            }, 500);
                        }

                        if (node.equals("opini")) {
                            showProgressDialog();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    hideProgressDialog();
                                    Intent intent = new Intent(getApplicationContext(), OpiniActivity.class);
                                    intent.putExtra("id", id);
                                    startActivity(intent);
                                    finish();
                                }
                            }, 500);
                        }

                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Memuat...");
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
