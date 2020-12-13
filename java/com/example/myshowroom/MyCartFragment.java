package com.example.myshowroom;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.style.AlignmentSpan;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyCartFragment extends Fragment {

    public MyCartFragment() {
        // Required empty public constructor
    }
private RecyclerView cartItemsRecyclerview;
    private Button continueBtn;

    private Dialog loadingDialog;

    public static CartAdapter cartAdapter;
    private  TextView totalAmount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_my_cart, container, false);


        //////////////////loading dialog
        loadingDialog=new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.slide_background));

        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();

        //////////////////loading dialog


        cartItemsRecyclerview=view.findViewById(R.id.cart_items_recycler_view);
        continueBtn=view.findViewById(R.id.cart_continue_btn);
        totalAmount=view.findViewById(R.id.total_cart_amount);


        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        cartItemsRecyclerview.setLayoutManager(layoutManager);

//        if(DBqueries.cartItemModelList.size()==0){
//            DBqueries.cartlist.clear();
//            DBqueries.loadCartList(getContext(),loadingDialog,true,new TextView(getContext()),totalAmount);
//        }else{
//            if(DBqueries.cartItemModelList.get(DBqueries.cartItemModelList.size()-1).getType()==CartItemModel.TOTAL_AMOUNT){
//                LinearLayout parent=(LinearLayout) totalAmount.getParent().getParent();
//                parent.setVisibility(View.VISIBLE);
//
//            }
//            loadingDialog.dismiss();
//        }





//        cartItemModelList.add(new CartItemModel(0,R.drawable.banner,"pixel 1",2,"Rs.499999/-","RS.4999/-",1,0,0));
//        cartItemModelList.add(new CartItemModel(0,R.drawable.account,"pixel 1",0,"Rs.499999/-","RS.4999/-",1,1,0));
//        cartItemModelList.add(new CartItemModel(0,R.drawable.logo,"pixel 1",2,"Rs.499999/-","RS.4999/-",1,2,0));
     // DBqueries.cartItemModelList.add(new CartItemModel(1,"Price (3 items)","RS.19999999/-","Free","RS.11199/-","RS.59999/-"));

       cartAdapter=new CartAdapter(DBqueries.cartItemModelList,totalAmount,true);
        cartItemsRecyclerview.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();


        continueBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               DeliveryActivity.cartItemModelList=new ArrayList<>();

               DeliveryActivity.fromCart=true;

               for(int x=0;x<DBqueries.cartItemModelList.size();x++){
                   CartItemModel cartItemModel=DBqueries.cartItemModelList.get(x);
                   if(cartItemModel.isInStock()){
                       DeliveryActivity.cartItemModelList.add(cartItemModel);
                   }
               }
               DeliveryActivity.cartItemModelList.add(new CartItemModel(CartItemModel.TOTAL_AMOUNT));


               loadingDialog.show();
               if(DBqueries.addressesModelList.size()==0) {
                   DBqueries.loadAddresses(getContext(), loadingDialog);
               }
               else{
                   loadingDialog.dismiss();
                   Intent deliveryIntent=new Intent(getContext(),DeliveryActivity.class);
                   startActivity(deliveryIntent);
               }


           }
       });
        return view;
    }

    @Override
    public void onStart() {

        super.onStart();
    cartAdapter.notifyDataSetChanged();
        if(DBqueries.cartItemModelList.size()==0){
            DBqueries.cartlist.clear();
            DBqueries.loadCartList(getContext(),loadingDialog,true,new TextView(getContext()),totalAmount);
        }else{
            if(DBqueries.cartItemModelList.get(DBqueries.cartItemModelList.size()-1).getType()==CartItemModel.TOTAL_AMOUNT){
                LinearLayout parent=(LinearLayout) totalAmount.getParent().getParent();
                parent.setVisibility(View.VISIBLE);

            }
            loadingDialog.dismiss();
        }
    }



}
