package com.example.reverserecipe.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.reverserecipe.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

// TODO provide navigation from this page to the main activity
// TODO provide link to forgot password

public class LoginActivity extends AppCompatActivity  {
    public static final String TAG = LoginActivity.class.getSimpleName();

    @BindView(R.id.email_edittext) EditText mEmailEditText;
    @BindView(R.id.password_edittext) EditText mPasswordText;
    @BindView(R.id.custom_signin_button) Button mSignInButton;
    @BindView(R.id.custom_signup_button) Button mSignupButton;
    @BindView(R.id.guest_login) Button guestLogin;
    private ImageView background;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ProgressDialog mAuthProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        background = (ImageView) findViewById(R.id.s_img);



        Glide.with(this)
                .load(R.drawable.splash)
                .into(background);
        Typeface text = Typeface.createFromAsset(getAssets(), "fonts/caviar_dreams.ttf");
            mEmailEditText.setTypeface(text);
            mPasswordText.setTypeface(text);
            mSignInButton.setTypeface(text);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(LoginActivity.this, Fridge.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        };

        createAuthProgressDialog();
    }

    private void createAuthProgressDialog() {
        mAuthProgressDialog = new ProgressDialog(this);
        mAuthProgressDialog.setTitle("Loading...");
        mAuthProgressDialog.setMessage("User authentication in progress...");
        mAuthProgressDialog.setCancelable(false);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    @OnClick(R.id.guest_login) void guestLogin(){
        startActivity(new Intent(LoginActivity.this,Fridge.class));
    }
    @OnClick(R.id.custom_signup_button) void signup(){
        startActivity(new Intent(LoginActivity.this,CreateAccountActivity.class));
    }
    @OnClick (R.id.custom_signin_button) void signIn(){
        loginWithPassword();
    }


    private void loginWithPassword() {
        String email = mEmailEditText.getText().toString().trim();
        String password = mPasswordText.getText().toString().trim();

        if (email.equals("")) {
            mEmailEditText.setError("Please enter your email.");
            return;
        }
        if (password.equals("")) {
            mPasswordText.setError("Please enter your password.");
            return;
        }
        mAuthProgressDialog.show();
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mAuthProgressDialog.dismiss();
                Log.d(TAG, "signInWIthEmail:onComplete: " + task.isSuccessful());
                if (!task.isSuccessful()) {
                    Log.w(TAG, "signInWithEmail", task.getException());
                    Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

