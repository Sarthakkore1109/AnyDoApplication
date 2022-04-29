package com.sarthakkore.anydoapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private EditText regEmail, regPwd;
    private Button regBtnCred;
    private TextView logText;
    private FirebaseAuth mAuth;
    private ProgressDialog loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        regEmail = findViewById(R.id.editTextTextEmailAddressReg);
        regPwd = findViewById(R.id.editTextTextPasswordReg);
        regBtnCred = findViewById(R.id.buttonRegCred);
        logText = findViewById(R.id.textViewLogin);

        mAuth = FirebaseAuth.getInstance();
        loader = new ProgressDialog(this);

        logText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        regBtnCred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = regEmail.getText().toString().trim();
                String pass = regPwd.getText().toString().trim();

                Log.e("email",email);
                Log.e("pass",pass);

                if(email.isEmpty()){
                    regEmail.setError("Email is required");
                    return;
                }else if(pass.isEmpty() || pass.length() < 8){
                    regPwd.setError("Password is too short");
                    return;
                }else{

                    loader.setMessage("Registering");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();

                    mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(intent);
                                Log.e("registration done", "onComplete: ");
                                finish();
                                loader.dismiss();
                            }else{
                                String error = task.getException().toString();
                                Toast.makeText(RegisterActivity.this,"Registration Failed : "+error,Toast.LENGTH_LONG).show();
                                loader.dismiss();
                            }
                        }
                    });
                }

            }
        });

    }
}