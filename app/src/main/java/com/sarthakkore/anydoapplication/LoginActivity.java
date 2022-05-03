package com.sarthakkore.anydoapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.firebase.auth.FirebaseUser;

import java.util.logging.Logger;

public class LoginActivity extends AppCompatActivity {

    private EditText loginEmail, loginPwd;
    private Button loginBtn;
    private TextView registerText;
    private ProgressDialog loader;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginEmail = findViewById(R.id.editTextTextEmailAddress);
        loginPwd = findViewById(R.id.editTextTextPassword);
        loginBtn = findViewById(R.id.buttonLogin);
        registerText = findViewById(R.id.textViewRegister);
        loader = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String loginEm = loginEmail.getText().toString().trim();
                String loginPw = loginPwd.getText().toString().trim();

                if(loginEm.isEmpty()){
                    loginEmail.setError("Email is empty");
                    return;
                }else if(loginPw.isEmpty() || loginPwd.length() < 8){
                    loginPwd.setError("Password should be greater that 8 characters");
                    return;
                }else{
                    loader.setMessage("Logging In");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();

                    mAuth.signInWithEmailAndPassword(loginEm,loginPw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                loader.dismiss();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);

                                getUserId();

                                finish();
                            }else{
                                loader.dismiss();
                                String error = task.getException().toString();
                                Toast.makeText(LoginActivity.this,"Login Failed : "+error,Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }

            }
        });

    }

    public void getUserId(){
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        String userId = "";

        userId = currentFirebaseUser.getUid();
        storeData(userId);
        Toast.makeText(this, "" + currentFirebaseUser.getUid(), Toast.LENGTH_SHORT).show();
    }

    public void storeData(String userID){
        SharedPreferences.Editor editor = getSharedPreferences("AnyDoApplication", MODE_PRIVATE).edit();
        editor.putString("UserID", userID.toString() );
        editor.apply();
        Toast.makeText(this, "" + userID, Toast.LENGTH_SHORT).show();
        Log.e("TAG", "storeData: StoredSuccesfully" );
    }

}