package com.example.myshowroom;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static com.example.myshowroom.RegisterActivity.onresetpasswordfragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class signinFragment extends Fragment {

    public signinFragment() {
        // Required empty public constructor
    }
private TextView dontHaveAnAccount;
private FrameLayout parentframelayout;

private EditText email;
private EditText password;

private ImageButton closebtn;
private Button signinbtn;
private FirebaseAuth firebaseAuth;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";

    private ProgressBar progressBar;
    private TextView forgotpassword;
    public static boolean disableCloseBtn=false;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_signin, container, false);
        parentframelayout=getActivity().findViewById(R.id.register_frame_layout);
        dontHaveAnAccount=view.findViewById(R.id.tv_dont_have_an_account);

        forgotpassword=view.findViewById(R.id.sign_in_forgot_password);

        email=view.findViewById(R.id.sign_in_email);
        password=view.findViewById(R.id.sign_in_password);
        closebtn=view.findViewById(R.id.sign_in_close_btn);
        signinbtn=view.findViewById(R.id.sign_in_button);
        progressBar=view.findViewById(R.id.sign_in_progressbar);
        firebaseAuth=FirebaseAuth.getInstance();

        if (disableCloseBtn) {

            closebtn.setVisibility(View.GONE);
        }else {
            closebtn.setVisibility(View.VISIBLE);
        }

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dontHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new signupFragment());
            }
        });


        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onresetpasswordfragment=true;
                setFragment(new ResetPasswordFragment());
            }
        });


        closebtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        mainintent();
    }
});
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        signinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkemailandpassword();
            }
        });
    }


    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
fragmentTransaction.setCustomAnimations(R.anim.slide_from_right,R.anim.slideout_from_left);

        fragmentTransaction.replace(parentframelayout.getId(),fragment);
            fragmentTransaction.commit();


    }
    private void checkInputs() {
        if(!TextUtils.isEmpty(email.getText())){
            if(!TextUtils.isEmpty(password.getText())){
                signinbtn.setEnabled(true);
                signinbtn.setTextColor(Color.rgb(255,255,255));
            }
        else{
                signinbtn.setEnabled(false);
                signinbtn.setTextColor(Color.argb(50,255,255,255));

            }
        }
        else{
            signinbtn.setEnabled(false);
            signinbtn.setTextColor(Color.argb(50,255,255,255));

        }
    }

    private void checkemailandpassword(){
        if(email.getText().toString().matches(emailPattern)){
            if(password.length()>=8){
                progressBar.setVisibility(View.VISIBLE);
                signinbtn.setEnabled(false);
                signinbtn.setTextColor(Color.argb(50,255,255,255));
            firebaseAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                             mainintent();
                            }else{
                                progressBar.setVisibility(View.INVISIBLE);

                                signinbtn.setEnabled(true);
                                signinbtn.setTextColor(Color.rgb(255,255,255));
                            String error=task.getException().getMessage();
                            Toast.makeText(getActivity(),error,Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
            }
            else{
                Toast.makeText(getActivity(),"Invalid Email (OR) Password!)",Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(getActivity(),"Invalid Email (OR) Password!)",Toast.LENGTH_SHORT).show();

        }

    }
    private void mainintent(){
        if(disableCloseBtn){
            disableCloseBtn=false;
        }else {
            Intent mainintent = new Intent(getActivity(), MainActivity.class);
            startActivity(mainintent);
        }
        getActivity().finish();
    }

}
