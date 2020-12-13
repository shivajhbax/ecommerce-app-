package com.example.myshowroom;

import android.app.Dialog;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.myshowroom.DeliveryActivity.SELECT_ADDRESS;

public class MyAddressesActivity extends AppCompatActivity {
    private int previousAddress;

    private LinearLayout addNewAddressBtn;
    private RecyclerView myAddressesRecyclerview;
    private Button deliverHereBtn;
    private TextView addressesSaved;
    private Dialog loadingDialog;


    private static AddressesAdapter addressesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_addresses);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("My Addresses");
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        //////////////////loading dialog
        loadingDialog=new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(this.getDrawable(R.drawable.slide_background));

        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);


        //////////////////loading dialog

        myAddressesRecyclerview=findViewById(R.id.address_recyclerview);
       deliverHereBtn=findViewById(R.id.deliver_here_btn);
       addNewAddressBtn=findViewById(R.id.add_new_address_btn);
       addressesSaved=findViewById(R.id.address_saved);
       previousAddress=DBqueries.selectedAddress;


        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        myAddressesRecyclerview.setLayoutManager(layoutManager);


//        List<AddressesModel> addressesModelList=new ArrayList<>();
//        addressesModelList.add(new AddressesModel("john doe","wesrdtyuioiuytdrsedtfgyuhijohgyutrdesdtfgyuhij","789654",true));
//        addressesModelList.add(new AddressesModel("dd doe","fghjkl","789654",false));
//        addressesModelList.add(new AddressesModel("sddd doe","wesrdtyuioiuytdrsedtsxdfcgvbhjnmdxcfgvbhjnkdcfgvbhjndcftvgybhnjmfgyuhijohgyutrdesdtfgyuhij","789654",false));
//        addressesModelList.add(new AddressesModel("john doe","wesrdtyuioiuytdrsedtfgyuhijohgyutrdesdtfgyuhij","789654",false));
//        addressesModelList.add(new AddressesModel("john doe","fghjkl","789654",false));
//        addressesModelList.add(new AddressesModel("ssss doe","wesrdtyuioiuytdrsedtsxdfcgvbhjnmdxcfgvbhjnkdcfgvbhjndcftvgybhnjmfgyuhijohgyutrdesdtfgyuhij","789654",false));


        int mode=getIntent().getIntExtra("MODE",-1);
        if(mode==SELECT_ADDRESS){
            deliverHereBtn.setVisibility(View.VISIBLE);
        }
        else{
            deliverHereBtn.setVisibility(View.GONE);
        }

        deliverHereBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(DBqueries.selectedAddress!=previousAddress){
                    final int previousAddressIndex=previousAddress;
                    loadingDialog.show();

                    Map<String,Object> updateselection=new HashMap<>();
                    updateselection.put("selected_"+String.valueOf(previousAddress+1),false);
                    updateselection.put("selected_"+String.valueOf(DBqueries.selectedAddress+1),true);

                    previousAddress=DBqueries.selectedAddress;

                    FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_ADDRESSES")
                            .update(updateselection).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                finish();
                            }
                            else{
                                previousAddress=previousAddressIndex;
                                String error=task.getException().getMessage();
                                Toast.makeText(MyAddressesActivity.this,error,Toast.LENGTH_SHORT).show();

                            }

                            loadingDialog.dismiss();
                        }
                    });


                }
                else{
                    finish();
                }

            }

        });


     addressesAdapter=new AddressesAdapter(DBqueries.addressesModelList,mode);
    myAddressesRecyclerview.setAdapter(addressesAdapter);
        ((SimpleItemAnimator)myAddressesRecyclerview.getItemAnimator()).setSupportsChangeAnimations(false);
    addressesAdapter.notifyDataSetChanged();
    addNewAddressBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent addAddressIntent=new Intent(MyAddressesActivity.this,AddressActivity.class);
            addAddressIntent.putExtra("INTENT","null");
            startActivity(addAddressIntent);
        }
    });
    }

    @Override
    protected void onStart() {
        super.onStart();
        addressesSaved.setText(String.valueOf(DBqueries.addressesModelList.size())+" saved addresses");

    }

    public static void refreshItem(int deselect, int select){
addressesAdapter.notifyItemChanged(deselect);
    addressesAdapter.notifyItemChanged(select);
}
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(DBqueries.selectedAddress!=previousAddress){
            DBqueries.addressesModelList.get(DBqueries.selectedAddress).setSelected(false);
            DBqueries.addressesModelList.get(previousAddress).setSelected(true);
            DBqueries.selectedAddress=previousAddress;
        }
        if(item.getItemId()==android.R.id.home){

            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {
        if(DBqueries.selectedAddress!=previousAddress){
            DBqueries.addressesModelList.get(DBqueries.selectedAddress).setSelected(false);
            DBqueries.addressesModelList.get(previousAddress).setSelected(true);
            DBqueries.selectedAddress=previousAddress;
        }
        super.onBackPressed();

    }
}
