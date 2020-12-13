package com.example.myshowroom;

import android.view.KeyEvent;
import android.widget.FrameLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class RegisterActivity extends AppCompatActivity {
private FrameLayout frameLayout;
public static boolean onresetpasswordfragment=false;
public static Boolean setSignUpFragment=false;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
frameLayout=findViewById(R.id.register_frame_layout);

if(setSignUpFragment){
    setSignUpFragment=false;
    setDefaultFragment(new signupFragment());
}
else {
    setDefaultFragment(new signinFragment());
}


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            signupFragment.disableCloseBtn=false;
             signinFragment.disableCloseBtn=false;
            if(onresetpasswordfragment){
                onresetpasswordfragment=false;
                setFragment(new signinFragment());
                return false;

            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void setDefaultFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(frameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }


    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_left,R.anim.slide_out_from_right);

        fragmentTransaction.replace(frameLayout.getId(),fragment);
        fragmentTransaction.commit();


    }
}
