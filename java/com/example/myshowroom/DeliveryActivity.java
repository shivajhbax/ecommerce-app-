package com.example.myshowroom;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.JsonObject;
import com.paytm.pgsdk.PaytmMerchant;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.*;

public class DeliveryActivity extends AppCompatActivity implements PaymentResultListener {


    private RecyclerView deliveryRecyclerView;
    private Button changeOrAddNewAddressBtn;

    public static final int SELECT_ADDRESS=0;
    private TextView totalAmount;
    private TextView fullname;
    private String name,mobileNo;
    private TextView fullAddress;
    private TextView pincode;
    public static List<CartItemModel> cartItemModelList;
    private Button continueBtn;
    private Dialog loadingDialog;
    private Dialog paymentMethodDialog;
    private ImageButton paytm,cod;
    private boolean isReceiverRegistered = false;
    private ConstraintLayout orderConfirmationLayout;
    private ImageButton continueShoppingBtn;
    private TextView ordeId;

    private boolean successResponse=false;
    public static boolean getQtyIDs=true;

    public static boolean fromCart;
private    String order_id;
public static boolean codOrderConfirmed=false;
private FirebaseFirestore firebaseFirestore;

private boolean  allProductsAvailable=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Delivery");


        deliveryRecyclerView=findViewById(R.id.delivery_recyclerview);
        changeOrAddNewAddressBtn=findViewById(R.id.change_or_add_address_btn);
        totalAmount=findViewById(R.id.total_cart_amount);
        fullname=findViewById(R.id.fullname);
        fullAddress=findViewById(R.id.address);
        pincode=findViewById(R.id.pincode);
        continueBtn=findViewById(R.id.cart_continue_btn);
        orderConfirmationLayout=findViewById(R.id.order_confirmation_layout);
        continueShoppingBtn=findViewById(R.id.continue_shopping_btn);
        ordeId=findViewById(R.id.order_id);

        Checkout.preload(getApplicationContext());
       

        //////////////////loading dialog
        loadingDialog=new Dialog(DeliveryActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slide_background));

        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        //////////////////loading dialog

        //////////////////payment dialog
        paymentMethodDialog=new Dialog(DeliveryActivity.this);
        paymentMethodDialog.setContentView(R.layout.payment_method);
        paymentMethodDialog.setCancelable(true);
        paymentMethodDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slide_background));

        paymentMethodDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        paytm=paymentMethodDialog.findViewById(R.id.paytm);
        cod=paymentMethodDialog.findViewById(R.id.cod_btn);


        //////////////////payment dialog
        firebaseFirestore=FirebaseFirestore.getInstance();
        getQtyIDs=true;
        order_id= UUID.randomUUID().toString().substring(0,28);



        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        deliveryRecyclerView.setLayoutManager(layoutManager);



        CartAdapter cartAdapter=new CartAdapter(cartItemModelList,totalAmount,false);
        deliveryRecyclerView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();


        changeOrAddNewAddressBtn.setVisibility(View.VISIBLE);
        changeOrAddNewAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getQtyIDs=false;
                Intent myAddressesIntent=new Intent(DeliveryActivity.this,MyAddressesActivity.class);
                myAddressesIntent.putExtra("MODE",SELECT_ADDRESS);
                startActivity(myAddressesIntent);
            }
        });

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(allProductsAvailable) {

                    paymentMethodDialog.show();
                }else{
                    ////nothing
                }

            }
        });

        cod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getQtyIDs=false;
                paymentMethodDialog.dismiss();
                Intent otpIntent=new Intent(DeliveryActivity.this,OTPverificationActivity.class);
                otpIntent.putExtra("mobileNo",mobileNo.substring(0,10));

                startActivity(otpIntent);
            }
        });

        paytm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getQtyIDs=false;
                paymentMethodDialog.dismiss();
                loadingDialog.show();

                if(ContextCompat.checkSelfPermission(DeliveryActivity.this, Manifest.permission.READ_SMS)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(DeliveryActivity.this,new String[]{Manifest.permission.READ_SMS,Manifest.permission.RECEIVE_SMS},101);

                }

              final   String Mid="HLYOON75409609441310";
               final String customer_id= FirebaseAuth.getInstance().getUid();
                String url="https://myshowrooom.000webhostapp.com/paytm/generateChecksum.php";
                final String callBackUrl="https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp";

                RequestQueue requestQueue= Volley.newRequestQueue(DeliveryActivity.this);
                StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            if(jsonObject.has("CHECKSUMHASH")){
                                String CHECKSUMHASH=jsonObject.getString("CHECKSUMHASH");

                               PaytmPGService paytmPGService=PaytmPGService.getStagingService("https://securegw-stage.paytm.in/order/process");
//                                PaytmMerchant Merchant = new PaytmMerchant("https://pguat.paytm.com/merchant-chksum/ChecksumGenerator","https://pguat.paytm.com/merchant-chksum/ValidateChksum");
                                HashMap<String, String> paramMap = new HashMap<String, String>();
//                                paramMap.put("REQUEST_TYPE", "DEFAULT");
                                paramMap.put("MID", Mid);
                                paramMap.put("ORDER_ID", order_id);
                                paramMap.put("CUST_ID", customer_id);
                                paramMap.put("CHANNEL_ID", "WAP");
                                paramMap.put("TXN_AMOUNT",totalAmount.getText().toString().substring(3,totalAmount.getText().length()-2));
                                paramMap.put("WEBSITE", "WEBSTAGING");
                                paramMap.put("INDUSTRY_TYPE_ID", "Retail");
                                paramMap.put("CALLBACK_URL", callBackUrl);
                                paramMap.put("CHECKSUMHASH",CHECKSUMHASH);
////                                ;
//                                paramMap.put("THEME", "merchant");
//
                                PaytmOrder paytmOrder=new PaytmOrder(paramMap);
//
                                paytmPGService.initialize(paytmOrder,null);
                                paytmPGService.startPaymentTransaction(DeliveryActivity.this, true, true, new PaytmPaymentTransactionCallback() {
                                    @Override
                                    public void onTransactionResponse(Bundle bundle) {

//                                        Toast.makeText(getApplicationContext(), "Payment Transaction response " + bundle.toString(), Toast.LENGTH_LONG).show();
                                        if(bundle.getString("STATUS").equals("TXN_SUCCESS")){

                                            showConfirmationLayout();
                    }
                                    }

                                    @Override
                                    public void networkNotAvailable() {
                                        Toast.makeText(getApplicationContext(), "no network available " , Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onErrorProceed(String s) {

                                    }

                                    @Override
                                    public void clientAuthenticationFailed(String s) {
                                        Toast.makeText(getApplicationContext(), "Client authentication failed " + s.toString(), Toast.LENGTH_LONG).show();

                                    }

                                    @Override
                                    public void someUIErrorOccurred(String s) {
                                        Toast.makeText(getApplicationContext(), "UI error " + s.toString(), Toast.LENGTH_LONG).show();

                                    }

                                    @Override
                                    public void onErrorLoadingWebPage(int i, String s, String s1) {
                                        Toast.makeText(getApplicationContext(), "Unable load webbage " + s.toString(), Toast.LENGTH_LONG).show();

                                    }

                                    @Override
                                    public void onBackPressedCancelTransaction() {
                                        Toast.makeText(getApplicationContext(), "Transaction cancelled  " , Toast.LENGTH_LONG).show();

                                    }

                                    @Override
                                    public void onTransactionCancel(String s, Bundle bundle) {
                                        Toast.makeText(getApplicationContext(), "Transaction cancelled " + s.toString(), Toast.LENGTH_LONG).show();

                                    }
                                });



                            }

                        }
                        catch (JSONException ex){
                            ex.printStackTrace();

                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loadingDialog.dismiss();
                        Toast.makeText(DeliveryActivity.this,"some thing went wrong!",Toast.LENGTH_SHORT).show();

                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> paramMap = new HashMap<String, String>();
                        paramMap.put("MID", Mid);
                        paramMap.put("ORDER_ID", order_id);
                        paramMap.put("CUST_ID", customer_id);
                        paramMap.put("CHANNEL_ID", "WAP");
                        paramMap.put("TXN_AMOUNT",totalAmount.getText().toString().substring(3,totalAmount.getText().length()-2));
                        paramMap.put("WEBSITE", "WEBSTAGING");
                        paramMap.put("INDUSTRY_TYPE_ID", "Retail");
                        paramMap.put("CALLBACK_URL", callBackUrl);
//                        paramMap.put("THEME", "merchant");
//                        paramMap.put("REQUEST_TYPE", "DEFAULT");
                        return paramMap;
                    }
                };

                requestQueue.add(stringRequest);



            }
        });

//paytm.setOnClickListener(new View.OnClickListener() {
//    @Override
//    public void onClick(View v) {
//        startPayment();
//    }
//});

    }

    @Override
    protected void onStart() {
        super.onStart();
//////////////////accessing quantity
        if(getQtyIDs){
        for(int x=0;x<cartItemModelList.size()-1;x++){

            int finalX = x;
            firebaseFirestore.collection("PRODUCTS").document(cartItemModelList.get(x).getProductID()).collection("QUANTITY").orderBy("available", Query.Direction.DESCENDING).limit(cartItemModelList.get(x).getProductQuantity())
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(final QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                                if((boolean)queryDocumentSnapshot.get("available")){
                                    boolean bb=(boolean)false;

                                    firebaseFirestore.collection("PRODUCTS").document(cartItemModelList.get(finalX).getProductID()).collection("QUANTITY").document(queryDocumentSnapshot.getId()).update("available",bb)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        cartItemModelList.get(finalX).getQtyIds().add(queryDocumentSnapshot.getId());

                                                    }else{
                                                        String error=task.getException().getMessage();
                                                        Toast.makeText(DeliveryActivity.this, error, Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });



                                }else{
                                    ///not available\
                                    allProductsAvailable=false;
                                    Toast.makeText(DeliveryActivity.this, "all products may not be available at required quantity! ", Toast.LENGTH_SHORT).show();
                                    break;
                                }

                            }

                        }else{
                            String error=task.getException().getMessage();
                            Toast.makeText(DeliveryActivity.this, error, Toast.LENGTH_SHORT).show();

                        }
                    }
                });
        }}
        else{
            getQtyIDs=true;
        }
//////////////////accessing quantity

        name=DBqueries.addressesModelList.get(DBqueries.selectedAddress).getFullname();
        mobileNo=DBqueries.addressesModelList.get(DBqueries.selectedAddress).getMobileNo();
        fullname.setText(name+" - "+mobileNo);
        fullAddress.setText(DBqueries.addressesModelList.get(DBqueries.selectedAddress).getAddress());
        pincode.setText(DBqueries.addressesModelList.get(DBqueries.selectedAddress).getPincode());
        if(codOrderConfirmed){
            showConfirmationLayout();
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onPause() {
        super.onPause();
        loadingDialog.dismiss();

        if (getQtyIDs){
        for (int x = 0; x < cartItemModelList.size() - 1; x++) {

            if(!successResponse){
            for(String qtyID: cartItemModelList.get(x).getQtyIds()){
                boolean tt=(boolean)true;
                firebaseFirestore.collection("PRODUCTS").document(cartItemModelList.get(x).getProductID()).collection("QUANTITY").document(qtyID).update("available",tt);
            }}
            cartItemModelList.get(x).getQtyIds().clear();


        }}
    }
    public void startPayment() {

        Checkout checkout=new Checkout();
        checkout.setKeyID("rzp_test_vc3wdvIWQAC8Mp");
        /**
         * Instantiate Checkout
         */

        /**
         * Set your logo here
         */
        checkout.setImage(R.drawable.logo);

        /**
         * Reference to current activity
         */
        final Activity activity = this;

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            options.put("name", "My showRoom");
            options.put("description", "Buy product");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
            options.put("amount", totalAmount.getText().toString().substring(3,totalAmount.getText().length()-2));//pass amount in currency subunits
            JSONObject preFill = new JSONObject();
            preFill.put("email", "kamal.bunkar07@gmail.com");
            preFill.put("contact", "9144040888");

            options.put("prefill", preFill);


            checkout.open(activity, options);
        } catch(Exception e) {
            Log.e("razor", "Error in starting Razorpay Checkout", e);
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        Log.e("success", " payment successfull "+ s.toString());
        Toast.makeText(this, "Payment successfully done! " +s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Log.e("failed",  "error code "+String.valueOf(i)+" -- Payment failed "+s.toString()  );
        try {
            Toast.makeText(this, "Payment error please try again", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("OnPaymentError", "Exception in onPaymentError", e);
        }
    }

    @Override
    public void onBackPressed() {
        if(successResponse){
            finish();
            return;
        }
        super.onBackPressed();
    }



    private void showConfirmationLayout(){
        successResponse=true;
        codOrderConfirmed=false;
        getQtyIDs=false;


        for (int x = 0; x < cartItemModelList.size() - 1; x++) {
            for(String qtyID: cartItemModelList.get(x).getQtyIds()){
                firebaseFirestore.collection("PRODUCTS").document(cartItemModelList.get(x).getProductID()).collection("QUANTITY").document(qtyID).update("user_ID",FirebaseAuth.getInstance().getUid());
            }

        }

        if(MainActivity.mainAcitvity!=null){
            MainActivity.mainAcitvity.finish();
            MainActivity.mainAcitvity=null;
            MainActivity.showcart=false;

        }else{
            MainActivity.resetMainActivity=true;

        }
        if(ProductDetailsActivity.productDetailsActivity!=null){
            ProductDetailsActivity.productDetailsActivity.finish();
            ProductDetailsActivity.productDetailsActivity=null;

        }
        //////////send confirmation sms
        String SMS_API="https://www.fast2sms.com/dev/bulk";

        StringRequest stringRequest=new StringRequest(Request.Method.POST, SMS_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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
                body.put("numbers",mobileNo);
                body.put("message","33138");////////////
                body.put("variables","{#FF#}");
                body.put("variables_values", order_id);
                //  body.put("flash","");


                return body;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        RequestQueue requestQueue= Volley.newRequestQueue(DeliveryActivity.this);
        requestQueue.add(stringRequest);

        //////////send confirmation sms


//        successResponse=true;
//        codOrderConfirmed=false;

        if(fromCart){
            Map<String,Object> updateCartList=new HashMap<>();
            long cartListSize=0;
            List<Integer> indexList=new ArrayList<>();

            for (int x=0;x<DBqueries.cartlist.size();x++){
                if(!cartItemModelList.get(x).isInStock()){
                    updateCartList.put("product_ID_"+cartListSize,cartItemModelList.get(x).getProductID());
                    cartListSize++;

                }else{
                    indexList.add(x);
                }
                // updateCartList.put("product_ID_"+x,cartlist.get(x));
            }
            updateCartList.put("list_size",cartListSize);
            FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_CART")
                    .set(updateCartList).addOnCompleteListener(new OnCompleteListener<Void>() {
                 @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        loadingDialog.show();

                        for (int x=0;x<indexList.size();x++){
                            DBqueries.cartlist.remove(indexList.get(x).intValue());
                            DBqueries.cartItemModelList.remove(indexList.get(x).intValue());
                            DBqueries.cartItemModelList.remove(DBqueries.cartItemModelList.size()-1);

                        }
                    }else{
                        String error=task.getException().getMessage();
                        Toast.makeText(DeliveryActivity.this,error,Toast.LENGTH_SHORT).show();

                    }
                    loadingDialog.dismiss();

                }
            });


        }

        continueBtn.setEnabled(false);
        changeOrAddNewAddressBtn.setEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);



        ordeId.setText("Order Id: "+order_id);
        orderConfirmationLayout.setVisibility(View.VISIBLE);
        continueShoppingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
