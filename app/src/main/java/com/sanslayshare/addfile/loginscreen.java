package com.sanslayshare.addfile;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class loginscreen extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText emailbut , passwordbut;
    String email , password;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginscreen);
        mAuth = FirebaseAuth.getInstance();
        emailbut = (EditText)findViewById(R.id.email);
        passwordbut = (EditText)findViewById(R.id.password);


        login = (Button)findViewById(R.id.login);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailbut.getText().toString().trim();
                password = passwordbut.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(loginscreen.this, "enter email", Toast.LENGTH_SHORT).show();
                }

                if(TextUtils.isEmpty(password)){
                    Toast.makeText(loginscreen.this, "enter password", Toast.LENGTH_SHORT).show();
                }


                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(loginscreen.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                 startActivity(new Intent(getApplicationContext(),file_activity.class));
                                } else {
                                    Toast.makeText(loginscreen.this, "authentication failede", Toast.LENGTH_SHORT).show();
                                }

                                // ...
                            }
                        });

            }
        });




    }
}