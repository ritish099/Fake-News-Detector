package com.example.fnd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    TextInputEditText name, email,password,confirmPassword;
    MaterialButton Register;

    FirebaseAuth auth;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name=findViewById(R.id.signup_username_text);
        email=findViewById(R.id.signup_email_text);
        password=findViewById(R.id.signup_password_text);
        confirmPassword=findViewById(R.id.signup_confirm_password_text);
        Register=findViewById(R.id.signup_button);

        auth=FirebaseAuth.getInstance();

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_username = name.getText().toString();
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
                String txt_confirm_password = confirmPassword.getText().toString();


                if (TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password) || TextUtils.isEmpty(txt_confirm_password))
                {
                    Toast.makeText(SignUpActivity.this, "All Fields are required", Toast.LENGTH_SHORT).show();
                }
                else if(txt_password.length()<6 || txt_confirm_password.length()<6)
                {
                    Toast.makeText(SignUpActivity.this, "Password Must be at least 6 character Long", Toast.LENGTH_SHORT).show();
                }
                else if(!password.getText().toString().trim().equals(confirmPassword.getText().toString().trim()))
                {
                    Toast.makeText(SignUpActivity.this, "Password and Retype Password Fields are not matching", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    SignUp(txt_username,txt_email,txt_password);
                }
            }
        });
    }

    private void SignUp(final String name, String email, String password)
    {
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            FirebaseUser firebaseUser=auth.getCurrentUser();
                            assert firebaseUser != null;
                            String userid = firebaseUser.getUid();

                            reference= FirebaseDatabase.getInstance().getReference("Users").child(userid);

                            HashMap<String, String> hashMap= new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("username", name);

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Intent intent= new Intent(SignUpActivity.this,LoginActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                }
                            });
                        }
                        else {
                            Toast.makeText(SignUpActivity.this, "You Cannot Register With This Email Or Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}