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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText txtEmailLogin;
    private EditText txtPwd;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_login);

        txtEmailLogin = (EditText) findViewById(R.id.txtEmailLogin);
        txtPwd = (EditText) findViewById(R.id.txtPasswordLogin);
        firebaseAuth = FirebaseAuth.getInstance();
        Button button = (Button)findViewById(R.id.btnLogin);
    }
    public void btnRegistration_Click(View v) {
        Intent i = new Intent(com.example.mick.driver7.LoginActivity.this, RegistrationActivity.class);
        startActivity(i);
    }
    public void btnUserLogin_Click(View v) {
        if (txtEmailLogin.getText().toString().trim().length()>0 && txtPwd.getText().toString().trim().length()>0) {
            final ProgressDialog progressDialog = ProgressDialog.show(LoginActivity.this, "Please wait...", "Proccessing...", true);

            (firebaseAuth.signInWithEmailAndPassword(txtEmailLogin.getText().toString().trim(), txtPwd.getText().toString().trim()))
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();

                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_LONG).show();
                                String admin = "admin";
                                if (firebaseAuth.getCurrentUser().getEmail().toLowerCase().contains(admin)) {
                                    Intent i = new Intent(LoginActivity.this, AdminActivity.class);
                                    i.putExtra("Email", firebaseAuth.getCurrentUser().getEmail());
                                    startActivity(i);
                                } else {
                                    Intent i = new Intent(LoginActivity.this, ProfileActivity.class);
                                    i.putExtra("Email", firebaseAuth.getCurrentUser().getEmail());
                                    startActivity(i);
                                }
                            } else {
                                Log.e("ERROR", task.getException().toString());
                                Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                            }
                        }
                    });
        }
        else
        {
            Toast.makeText(LoginActivity.this, "You did not enter a username and password", Toast.LENGTH_LONG).show();

        }
    }

}
