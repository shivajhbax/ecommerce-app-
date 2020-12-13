package com.example.myshowroom;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.transition.TransitionManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
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
import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class ResetPasswordFragment extends Fragment {

    public ResetPasswordFragment() {
        // Required empty public constructor
    }
private EditText registeredemail;
    private Button resetpasswordbtn;
    private TextView goback;
    private FrameLayout parentFrameLayout;
private FirebaseAuth firebaseAuth;
private ViewGroup emailiconcontainer;
private ImageView emailicon;
private TextView emailicontext;
private ProgressBar progressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_reset_password, container, false);
            registeredemail= view.findViewById(R.id.forgot_password_email);
            resetpasswordbtn=view.findViewById(R.id.reset_password_btn);
            goback=view.findViewById(R.id.tv_forget_password_go_back);

            emailiconcontainer=view.findViewById(R.id.forgot_password_email_icon_container);
            emailicon=view.findViewById(R.id.forgot_password_email_icon);
            emailicontext=view.findViewById(R.id.forgot_password_email_icon_text);
            progressBar=view.findViewById(R.id.forgot_password_progressbar);



parentFrameLayout=getActivity().findViewById(R.id.register_frame_layout);



firebaseAuth=FirebaseAuth.getInstance();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        registeredemail.addTextChangedListener(new TextWatcher() {
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

        resetpasswordbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransitionManager.beginDelayedTransition(emailiconcontainer);
                emailicon.setVisibility(View.GONE);




                TransitionManager.beginDelayedTransition(emailiconcontainer);
                emailicon.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);



                resetpasswordbtn.setEnabled(false);
                resetpasswordbtn.setTextColor(Color.argb(50,255,255,255));
            firebaseAuth.sendPasswordResetEmail(registeredemail.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                ScaleAnimation scaleAnimation = new ScaleAnimation(1,0,1,0,emailicon.getWidth()/2,emailicon.getHeight()/2);
                                scaleAnimation.setDuration(100);
                                scaleAnimation.setInterpolator(new AccelerateInterpolator());
                                scaleAnimation.setRepeatMode(Animation.REVERSE);
                                scaleAnimation.setRepeatCount(1);

                                scaleAnimation.setAnimationListener(new Animation.AnimationListener(){
                                    @Override
                                    public void onAnimationStart(Animation animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animation animation) {
                                        emailicontext.setText("Recovery email sent successfully! check your inbox");
                                        emailicontext.setTextColor(getResources().getColor(R.color.successGreen));

                                        TransitionManager.beginDelayedTransition(emailiconcontainer);
                                        emailicontext.setVisibility(View.VISIBLE);
                                    }

                                    @Override
                                    public void onAnimationRepeat(Animation animation) {

                                        emailicon.setImageResource(R.drawable.mail);
                                    }
                                });

                                emailicon.startAnimation(scaleAnimation);



                            }else{
                            String error=task.getException().getMessage();

                                emailicontext.setText(error);
                                emailicontext.setTextColor(getResources().getColor(R.color.colorPrimary));
                                resetpasswordbtn.setEnabled(true);
                                resetpasswordbtn.setTextColor(Color.rgb(255,255,255));
                                TransitionManager.beginDelayedTransition(emailiconcontainer);
                                emailicontext.setVisibility(View.VISIBLE);


                            }
                            progressBar.setVisibility(View.GONE);


                        }
                    });
            }
        });

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            setFragment(new signinFragment());
            }
        });

    }
    private void checkInputs(){
        if(TextUtils.isEmpty(registeredemail.getText())){
            resetpasswordbtn.setEnabled(false);
            resetpasswordbtn.setTextColor(Color.argb(50,255,255,255));

        }else {

            resetpasswordbtn.setEnabled(true);
            resetpasswordbtn.setTextColor(Color.rgb(255,255,255));
        }
    }
    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_left,R.anim.slide_out_from_right);

        fragmentTransaction.replace(parentFrameLayout.getId(),fragment);
        fragmentTransaction.commit();


    }
}
