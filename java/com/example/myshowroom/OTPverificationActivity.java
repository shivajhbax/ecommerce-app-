package com.example.myshowroom;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class OTPverificationActivity extends AppCompatActivity {
private TextView phoneNo;
private EditText otp;
private Button verifyBtn;
private String userNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_pverification);

        phoneNo=findViewById(R.id.phone_no);
        otp=findViewById(R.id.otp);
        verifyBtn=findViewById(R.id.verify_otp);
        userNo=getIntent().getStringExtra("mobileNo");
        phoneNo.setText("Verification code has been sent to +91 "+userNo);

        Random random=new Random();
        int otp_number=random.nextInt(999999-111111)+111111;
        String SMS_API="https://www.fast2sms.com/dev/bulk";

        StringRequest stringRequest=new StringRequest(Request.Method.POST,SMS_API , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                verifyBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(otp.getText().toString().equals(String.valueOf(otp_number))){
                            DeliveryActivity.codOrderConfirmed=true;
                            finish();
                        }
                        else{
                            Toast.makeText(OTPverificationActivity.this,"incorrect OTP",Toast.LENGTH_SHORT).show();

                        }
                    }
                });

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                finish();
                Toast.makeText(OTPverificationActivity.this,"failed to sent an OTP verification code",Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers=new HashMap<>();
                headers.put("authorization","hiTUNwBozYnXd80l4VI2fOmQqASJEPv9aj7ks5xZbFWr6MHtcKxSHcqUbRLu9EVimXFd5YKPJMyDha8l");

                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> body=new HashMap<>();
                body.put("sender_id","FSTSMS");
                body.put("language","english");
                body.put("route","qt");
                body.put("numbers",userNo);
                body.put("message","33108");////////////
                body.put("variables","{#BB#}");
                body.put("variables_values", String.valueOf(otp_number));
               //  body.put("flash","");


                return body;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        RequestQueue requestQueue= Volley.newRequestQueue(OTPverificationActivity.this);
        requestQueue.add(stringRequest);

    }
}
