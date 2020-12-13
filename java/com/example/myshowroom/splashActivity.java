package com.example.myshowroom;

import android.content.Intent;
import android.os.SystemClock;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class splashActivity extends AppCompatActivity {
private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        firebaseAuth=FirebaseAuth.getInstance();


        SystemClock.sleep(3000);




    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentuser=firebaseAuth.getCurrentUser();
        if(currentuser==null){
            Intent loginintent=new Intent(splashActivity.this, RegisterActivity.class);

            startActivity(loginintent);
            finish();
        }
        else
        {
            Intent mainintent=new Intent(splashActivity.this, MainActivity.class);

            startActivity(mainintent);
            finish();
        }
    }
}
