package com.example.nikhi.getcabby;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText userMail , userPassword;
    private Button btnLogin;
    private ProgressBar loginProgress;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private String type;

    TextView signUp, signUpPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        userMail = findViewById(R.id.loginEmail);
        userPassword = findViewById(R.id.loginPassword);
        btnLogin = findViewById(R.id.loginBtn);
        loginProgress = findViewById(R.id.loginProgressBar);
        signUp = findViewById(R.id.loginRegister);
        signUpPhone = findViewById(R.id.loginPhone);
        firebaseAuth = FirebaseAuth.getInstance();

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent registerActivity = new Intent(getApplicationContext() , RegisterActivity.class);
                startActivity(registerActivity);
                finish();

            }
        });

        signUpPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerPhone = new Intent(getApplicationContext() , LoginActivityPhone.class);
                startActivity(registerPhone);
                finish();
            }
        });

        loginProgress.setVisibility(View.INVISIBLE);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginProgress.setVisibility(View.VISIBLE);
                btnLogin.setVisibility(View.INVISIBLE);

                final String mail = userMail.getText().toString();
                final String password = userPassword.getText().toString();

                if(mail.isEmpty() || password.isEmpty()){
                    showMessage("Verify All Fields");
                    btnLogin.setVisibility(View.VISIBLE);
                    loginProgress.setVisibility(View.INVISIBLE);
                }
                else{
                    signIn(mail , password);
                }

            }
        });

    }

    private void signIn(String mail, String password) {

        firebaseAuth.signInWithEmailAndPassword(mail , password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
//                    loginProgress.setVisibility(View.INVISIBLE);
//                    btnLogin.setVisibility(View.VISIBLE);


                    updateUI();

                   /* databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String user= dataSnapshot.getChildren().toString();
                            if(user.equalsIgnoreCase("Customer")){
                                updateUI();

                            }
                            else  {
                                Intent intent = new Intent(getApplicationContext(),DriverMap.class);
                                startActivity(intent);
                                finish();
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {


                        }
                    });*/



                }
                else{
                    showMessage(task.getException().getMessage());
                    btnLogin.setVisibility(View.VISIBLE);
                    loginProgress.setVisibility(View.INVISIBLE);
                }

            }
        });

    }

    private void updateUI() {


        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Type");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String type = dataSnapshot.getValue(String.class);
                if(type.equalsIgnoreCase("Customer")){
                    startActivity(new Intent(getApplicationContext(),MapsActivity.class));

                }
                else  {
                    startActivity(new Intent(getApplicationContext(),DriverMap.class));
                }
                finish();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext() , message , Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user != null){
            updateUI();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
