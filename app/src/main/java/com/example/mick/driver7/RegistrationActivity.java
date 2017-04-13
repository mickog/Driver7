package com.example.mick.driver7;

/**
 * Created by Mick on 13/02/2017.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegistrationActivity extends AppCompatActivity {

    //create fields for email and password to register
    private EditText txtUsername;
    private EditText txtEmailAddress;
    private EditText txtPassword;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_activity);

        txtUsername = (EditText)findViewById((R.id.username)) ;
        txtEmailAddress = (EditText) findViewById(R.id.txtEmailRegistration);
        txtPassword = (EditText) findViewById(R.id.txtPasswordRegistration);
        firebaseAuth = FirebaseAuth.getInstance();
        Button btnRegister =(Button)findViewById(R.id.btnRegister);
        TextView tv = (TextView)findViewById(R.id.link_to_login);
        tv.setOnClickListener(new View.OnClickListener() {
                                  @Override
                                  public void onClick(View v) {
                                      Intent i = new Intent(com.example.mick.driver7.RegistrationActivity.this, LoginActivity.class);
                                      startActivity(i);
                                  }
                              }
        );

    }

    public void btnRegistrationUser_Click(View v) {
        final String email = txtEmailAddress.getText().toString().trim();
        final String password = txtPassword.getText().toString().trim();
        final String username = txtUsername.getText().toString().trim();
        final ProgressDialog progressDialog = ProgressDialog.show(RegistrationActivity.this, "Please wait...", "Processing...", true);
        (firebaseAuth.createUserWithEmailAndPassword(email,password ))
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();

                        if (task.isSuccessful()) {
                            //Sign in the user here
                            signin(email,password,username);

                        }
                        else
                        {
                            Log.e("ERROR", task.getException().toString());
                            Toast.makeText(RegistrationActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void signin(String email, String password, final String username) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //New Account is signed in and now the Current User
                            FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();
                            Toast.makeText(RegistrationActivity.this, "curr user is "+user.getEmail(), Toast.LENGTH_LONG).show();
                            Toast.makeText(RegistrationActivity.this, "passed in username is "+username, Toast.LENGTH_LONG).show();
                            firebaseAuth.getInstance().signOut();

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(username)
                                    .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()) {
                                                Toast.makeText(RegistrationActivity.this, " updated display name ", Toast.LENGTH_LONG).show();

                                            }
                                        }
                                    });
//                            Toast.makeText(RegistrationActivity.this, "Registration successful", Toast.LENGTH_LONG).show();

                            Intent i = new Intent(RegistrationActivity.this, LoginActivity.class);
                            startActivity(i);
                        }
                    }
                });
    }
}
