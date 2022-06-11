package com.potentnetwork.win;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.potentnetwork.win.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUp extends AppCompatActivity {
    private TextInputLayout mUsername,mPassword;
    private String username,password;
    private Button signUp;
    ProgressBar progressBar;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mUsername = findViewById(R.id.signusername);
        mPassword = findViewById(R.id.sgnpassword);
        signUp = findViewById(R.id.signupbutton);
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                username = mUsername.getEditText().getText().toString().trim();
                password = mPassword.getEditText().getText().toString().trim();
               if (isValid()){
                   progressBar.setVisibility(View.VISIBLE);
                   signUp.setEnabled(false);
                   mAuth.signInWithEmailAndPassword(username,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {
                           if (task.isSuccessful()){
                               progressBar.setVisibility(View.INVISIBLE);
                               Intent chooseIntent = new Intent(SignUp.this,ChooseOption.class);
                               startActivity(chooseIntent);
                           }else {
                               progressBar.setVisibility(View.INVISIBLE);
                               Toast.makeText(SignUp.this, "Admin Not Recognized", Toast.LENGTH_SHORT).show();
                           }

                       }
                   });
               }
            }
        });

    }
    public boolean isValid(){
        mUsername.setErrorEnabled(false);
        mUsername.setError("");
        mPassword.setErrorEnabled(false);
        mPassword.setError("");

        boolean isValid = false,isValidUsername = false, isValidPassword = false;
        if (TextUtils.isEmpty(username)){
            mUsername.setErrorEnabled(true);
            mUsername.setError("Email is Required");
        }else {
            isValidUsername = true;
        }
        if (TextUtils.isEmpty(password)){
            mPassword.setErrorEnabled(true);
            mPassword.setError("Password is Required");
        }else {
            isValidPassword = true;
        }
        isValid = isValidUsername && isValidPassword;
        return isValid;
    }

    @Override
    protected void onStart() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null){
          Intent intent = new Intent(SignUp.this,ChooseOption.class);
            startActivity(intent);
        }
        super.onStart();
    }
}