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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class signupFragment extends Fragment {

    public signupFragment() {
        // Required empty public constructor
    }

private TextView alreadyHaveAnAccount;
    private FrameLayout parentFrameLayout;
    private EditText email;
    private EditText fullname;
    private EditText password;
    private EditText confirmpassword;
    private ImageView closeButton;
    private Button signUpBtn;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
    public static boolean disableCloseBtn=false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view=inflater.inflate(R.layout.fragment_signup, container, false);

       alreadyHaveAnAccount=view.findViewById(R.id.tv_already_have_account);

       parentFrameLayout=getActivity().findViewById(R.id.register_frame_layout);

email=view.findViewById(R.id.sign_up_email);
fullname=view.findViewById(R.id.sign_up_fullname);
password=view.findViewById(R.id.sign_up_password);
confirmpassword=view.findViewById(R.id.sign_up_confirm_password);

closeButton=view.findViewById(R.id.sign_up_close);
signUpBtn=view.findViewById(R.id.sign_up_btn);

progressBar=view.findViewById(R.id.sign_up_progressbar);
firebaseAuth=FirebaseAuth.getInstance();
firebaseFirestore=FirebaseFirestore.getInstance();
        if (disableCloseBtn) {

            closeButton.setVisibility(View.GONE);
        }else {
            closeButton.setVisibility(View.VISIBLE);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        alreadyHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new signinFragment());


            }
        });
            closeButton.setOnClickListener(new View.OnClickListener() {
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
checkInput();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        fullname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInput();
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
                checkInput();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        confirmpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInput();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });




        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //to send data firebase
                checkEmailAndPassword();


            }
        });
    }
    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
fragmentTransaction.setCustomAnimations(R.anim.slide_from_left,R.anim.slide_out_from_right);

        fragmentTransaction.replace(parentFrameLayout.getId(),fragment);
        fragmentTransaction.commit();


    }
    private void checkInput(){
        if(!TextUtils.isEmpty(email.getText())){
            if(!TextUtils.isEmpty(fullname.getText())){
                if(!TextUtils.isEmpty(password.getText()) && password.length()>=8){

                    if(!TextUtils.isEmpty(confirmpassword.getText())){
                        signUpBtn.setEnabled(true);
                        signUpBtn.setTextColor(Color.rgb(255,255,255));


                    }
                    else {
                        signUpBtn.setEnabled(false);
                        signUpBtn.setTextColor(Color.argb(50,255,255,255));
                    }

                }
                else {
                    signUpBtn.setEnabled(false);
                    signUpBtn.setTextColor(Color.argb(50,255,255,255));
                }

            }
            else {
                signUpBtn.setEnabled(false);
                signUpBtn.setTextColor(Color.argb(50,255,255,255));
            }
        }
        else {
            signUpBtn.setEnabled(false);
            signUpBtn.setTextColor(Color.argb(50,255,255,255));

        }

    }
    private void checkEmailAndPassword(){
    if(email.getText().toString().matches(emailPattern)){

                if(password.getText().toString().equals(confirmpassword.getText().toString())){

                    progressBar.setVisibility(View.VISIBLE);
                    signUpBtn.setEnabled(false);
                    signUpBtn.setTextColor(Color.argb(50,255,255,255));

                    firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){

                                    Map<String,Object> userdata=new HashMap<>();
                                    userdata.put("fullname",fullname.getText().toString());

                                    firebaseFirestore.collection("USERS").document(firebaseAuth.getUid())
                                            .set(userdata)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){

                                                        CollectionReference userDataReference=firebaseFirestore.collection("USERS").document(firebaseAuth.getUid()).collection("USER_DATA");

                                                        //////Maps


                                                        Map<String,Object> wishListMap=new HashMap<>();
                                                        wishListMap.put("list_size",(long) 0);

                                                        Map<String,Object> ratingsMap=new HashMap<>();
                                                        ratingsMap.put("list_size",(long) 0);


                                                        Map<String,Object> cartMap=new HashMap<>();
                                                        cartMap.put("list_size",(long) 0);

                                                        Map<String,Object> myAddressesMap=new HashMap<>();
                                                        myAddressesMap.put("list_size",(long) 0);

                                                        //////Maps




                                                        List<String> documentNames=new ArrayList<>();
                                                        documentNames.add("MY_WISHLIST");
                                                        documentNames.add("MY_RATINGS");
                                                        documentNames.add("MY_CART");
                                                        documentNames.add("MY_ADDRESSES");



                                                        List<Map<String,Object>> documentFields=new ArrayList<>();
                                                        documentFields.add(wishListMap);
                                                        documentFields.add(ratingsMap);
                                                        documentFields.add(cartMap);
                                                        documentFields.add(myAddressesMap);

                                                            for (int x=0;x<documentNames.size();x++){
                                                                int finalX = x;
                                                                userDataReference.document(documentNames.get(x))
                                                                        .set(documentFields.get(x)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()){
                                                                            if(finalX ==documentNames.size()-1){
                                                                            mainintent();
                                                                            }


                                                                        }
                                                                        else {
                                                                            progressBar.setVisibility(View.INVISIBLE);
                                                                            signUpBtn.setEnabled(true);
                                                                            signUpBtn.setTextColor(Color.rgb(255,255,255));
                                                                            String error=task.getException().getMessage();
                                                                            Toast.makeText(getActivity(),error,Toast.LENGTH_SHORT).show();

                                                                        }


                                                                    }
                                                                });
                                                            }


                                                    }else{

                                                        String error=task.getException().getMessage();
                                                        Toast.makeText(getActivity(),error,Toast.LENGTH_SHORT).show();

                                                    }

                                                }
                                            });




                                }
                                else{
                                    progressBar.setVisibility(View.INVISIBLE);
                                    signUpBtn.setEnabled(true);
                                    signUpBtn.setTextColor(Color.rgb(255,255,255));
                                    String error=task.getException().getMessage();
                                    Toast.makeText(getActivity(),error,Toast.LENGTH_SHORT).show();

                                }
                                }
                            });
                }else{
                    confirmpassword.setError("Password doesn't matched!");

                        }
    }else {
            email.setError("Invalid Email!");
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
