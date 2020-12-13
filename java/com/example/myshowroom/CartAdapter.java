package com.example.myshowroom;

import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter {
    private List<CartItemModel> cartItemModelList;
    private int lastPosition=-1;
    private TextView cartTotalAmount;
    private boolean showDeleteBtn;


    public CartAdapter(List<CartItemModel> cartItemModelList,TextView cartTotalAmount,boolean showDeleteBtn) {
        this.cartItemModelList = cartItemModelList;
        this.cartTotalAmount=cartTotalAmount;
        this.showDeleteBtn=showDeleteBtn;
    }

    @Override
    public int getItemViewType(int position) {
        switch (cartItemModelList.get(position).getType()){
            case 0:
              return   CartItemModel.CART_ITEM;
            case 1:
                return CartItemModel.TOTAL_AMOUNT;
            default:
                return -1;
        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        switch (viewType){
            case CartItemModel.CART_ITEM:
            View cartItemview= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_item_layout,viewGroup,false);
            return new CartItemViewHolder(cartItemview);

                case CartItemModel.TOTAL_AMOUNT:
                    View cartTotalview= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_total_amount_layout,viewGroup,false);
                    return new CartTotalAmountsViewHolder(cartTotalview);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        switch (cartItemModelList.get(position).getType()){
            case CartItemModel.CART_ITEM:
                String productID=cartItemModelList.get(position).getProductID();
                String resource=cartItemModelList.get(position).getProductImage();
                String title=cartItemModelList.get(position).getProductTitle();
                Long freeCoupens=cartItemModelList.get(position).getFreeCoupens();
                String productPrice=cartItemModelList.get(position).getProductPrice();
                String cuttedPrice=cartItemModelList.get(position).getCuttedPrice();
                Long offersApplied=cartItemModelList.get(position).getOffersApplied();
                boolean inStock=cartItemModelList.get(position).isInStock();
                Long productQuantity=cartItemModelList.get(position).getProductQuantity();
                Long maxQuantity=cartItemModelList.get(position).getMaxQuantity();

                ((CartItemViewHolder)viewHolder).setItemDetails(productID,resource,title,freeCoupens,productPrice,cuttedPrice,offersApplied,position,inStock,String.valueOf(productQuantity),maxQuantity);
                break;

                case CartItemModel.TOTAL_AMOUNT:
                    int totalItems=0;
                    int totalItemsPrice=0;
                    String deliveryPrice;
                    int totalAmount;
                    int savedAmount=0;

                    for(int x=0;x<cartItemModelList.size();x++){

                        if(cartItemModelList.get(x).getType()==CartItemModel.CART_ITEM  && cartItemModelList.get(x).isInStock()){
                            totalItems++;
                            totalItemsPrice= totalItemsPrice+Integer.parseInt(cartItemModelList.get(x).getProductPrice());


                        }

                    }
                    if(totalItemsPrice>500){
                        deliveryPrice="free";
                        totalAmount=totalItemsPrice;
                    }
                    else{
                        deliveryPrice="60";
                        totalAmount=totalItemsPrice+60;
                    }


//                    String totalItem=cartItemModelList.get(position).getTotalItems();
//                    =cartItemModelList.get(position).getDeliveryPrice();
//                    String totalItemsPrice=cartItemModelList.get(position).getTotalItemPrice();
                    //String savedAmount=cartItemModelList.get(position).getSavedAmount();
                    ((CartTotalAmountsViewHolder)viewHolder).setTotal(totalItems,totalItemsPrice,deliveryPrice,totalAmount,savedAmount);

                    break;

            default:return;
        }
        if(lastPosition <position) {
            Animation animation = new AnimationUtils().loadAnimation(viewHolder.itemView.getContext(), R.anim.fade_in);
            viewHolder.itemView.setAnimation(animation);
            lastPosition=position;

        }

    }

    @Override
    public int getItemCount() {
        return cartItemModelList.size();
    }
    class CartItemViewHolder extends RecyclerView.ViewHolder{
        private ImageView productImage;
        private ImageView freeCoupenIcon;
        private TextView productTitle;
        private TextView freeCoupens;
        private TextView productPrice;
        private TextView cuttedPrice;
        private TextView offersApplied;
        private TextView coupensApplied;
        private TextView productQuantity;

        private LinearLayout coupenRedeemLayout;

        private LinearLayout deleteBtn;



        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage=itemView.findViewById(R.id.order_product_image);
            productTitle=itemView.findViewById(R.id.product_title);
            freeCoupenIcon=itemView.findViewById(R.id.free_coupon_icon);
            freeCoupens=itemView.findViewById(R.id.tv_free_coupon);
             productPrice=itemView.findViewById(R.id.product_price);
             cuttedPrice=itemView.findViewById(R.id.cutted_price);
             offersApplied=itemView.findViewById(R.id.offers_applied);
            coupensApplied=itemView.findViewById(R.id.coupen_applied);
            productQuantity=itemView.findViewById(R.id.product_quantity);
            coupenRedeemLayout=itemView.findViewById(R.id.coupon_redeem_layout);

            deleteBtn=itemView.findViewById(R.id.remove_item_btn);


        }
        private void setItemDetails(String productID,String resource,String title,Long freeCoupensNo,String productPriceText,String cuttedPriceText,Long offersAppliedNo,int position,boolean inStock,String quantity,Long maxQuantity){

            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.iconplae)).into(productImage);
            productTitle.setText(title);






            if(inStock) {
                if(freeCoupensNo>0){
                    freeCoupenIcon.setVisibility(View.VISIBLE);
                    freeCoupens.setVisibility(View.VISIBLE);
                    if (freeCoupensNo==1)
                        freeCoupens.setText("free "+freeCoupensNo+" coupen");
                    else {
                        freeCoupens.setText("free "+freeCoupensNo+" coupens");
                    }

                }
                else {
                    freeCoupenIcon.setVisibility(View.INVISIBLE);
                    freeCoupens.setVisibility(View.INVISIBLE);
                }


                productPrice.setText("Rs."+productPriceText+"/-");
                productPrice.setTextColor(itemView.getContext().getResources().getColor(R.color.black));

                cuttedPrice.setText("Rs."+cuttedPriceText+"/-");
                coupenRedeemLayout.setVisibility(View.VISIBLE);

                productQuantity.setText("Quantity: "+quantity);



                productQuantity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Dialog quantityDialog=new Dialog(itemView.getContext());
                        quantityDialog.setContentView(R.layout.quantity_dialog);
                        quantityDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                        quantityDialog.setCancelable(false);
                        final EditText quantityNo=quantityDialog.findViewById(R.id.quantity_no);
                        Button cancelBtn=quantityDialog.findViewById(R.id.cancel_btn);
                        Button okBtn=quantityDialog.findViewById(R.id.ok_btn);

                        quantityNo.setHint("Max "+ String.valueOf(maxQuantity));

                        cancelBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                quantityDialog.dismiss();
                            }
                        });

                        okBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(!TextUtils.isEmpty(quantityNo.getText()))
                                {
                                    if(Long.valueOf(quantityNo.getText().toString()) <= maxQuantity && Long.valueOf(quantityNo.getText().toString()) != 0){
                                        if(itemView.getContext() instanceof MainActivity){
                                            DBqueries.cartItemModelList.get(position).setProductQuantity(Long.valueOf(quantityNo.getText().toString()));

                                        }
                                        else{
                                        if(DeliveryActivity.fromCart) {
                                            DBqueries.cartItemModelList.get(position).setProductQuantity(Long.valueOf(quantityNo.getText().toString()));
                                        }else{
                                            DeliveryActivity.cartItemModelList.get(position).setProductQuantity(Long.valueOf(quantityNo.getText().toString()));
                                        }}
                                        productQuantity.setText("Quantity: "+quantityNo.getText());
                                    }
                                    else{
                                        Toast.makeText(itemView.getContext(), "Max quantity should not be greater than or equal to "+(maxQuantity)+" and not less than 1", Toast.LENGTH_SHORT).show();
                                    }


                                }
                                else{
                                    Toast.makeText(itemView.getContext(), "Text field should not be empty!!", Toast.LENGTH_SHORT).show();
                                }
                                quantityDialog.dismiss();





                            }
                        });
                        quantityDialog.show();

                    }
                });
                if(offersAppliedNo>0){
                    offersApplied.setVisibility(View.VISIBLE);
                    offersApplied.setText(offersAppliedNo+" Offers Applied");
                }else {
                    offersApplied.setVisibility(View.INVISIBLE);
                }

            }
            else{
                productPrice.setText("Out of stock");
                productPrice.setTextColor(itemView.getContext().getResources().getColor(R.color.colorPrimary));
                cuttedPrice.setText("");
                coupenRedeemLayout.setVisibility(View.GONE);
                freeCoupens.setVisibility(View.INVISIBLE);
//                productQuantity.setText("Quantity: "+0);
//                productQuantity.setCompoundDrawableTintList(ColorStateList.valueOf(Color.parseColor("#70000000")));
//                productQuantity.setTextColor(Color.parseColor("#70000000"));
//                productQuantity.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#70000000")));
                productQuantity.setVisibility(View.INVISIBLE);
                coupensApplied.setVisibility(View.GONE);
                offersApplied.setVisibility(View.GONE);
                freeCoupenIcon.setVisibility(View.INVISIBLE);

            }






            if(showDeleteBtn){
                deleteBtn.setVisibility(View.VISIBLE);
            }
            else{
                deleteBtn.setVisibility(View.GONE);

            }
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!ProductDetailsActivity.running_cart_query) {
                        ProductDetailsActivity.running_cart_query=true;
                        DBqueries.removeFromCart(position,itemView.getContext(),cartTotalAmount);
                    }
                }
            });
        }
    }

   public class CartTotalAmountsViewHolder extends RecyclerView.ViewHolder{

         private TextView  totalItem;
        private TextView  totalItemsPrice;
        private TextView  deliveryPrice;
        private TextView  totalPrice;
        private TextView  savedAmount;


        public CartTotalAmountsViewHolder(@NonNull View itemView) {
            super(itemView);
           totalItem= (TextView) itemView.findViewById(R.id.total_items);
            totalItemsPrice=(TextView)  itemView.findViewById(R.id.total_items_price);
            deliveryPrice= (TextView) itemView.findViewById(R.id.delivery_price);
            totalPrice= (TextView) itemView.findViewById(R.id.total_price);
            savedAmount= (TextView) itemView.findViewById(R.id.saved_amount);


        }

        public void setTotal(int totalItemText,int totalItemPriceText,String deliveryPriceText,int totalAmountText,int savedAmountText){
   try {
    totalItem.setText("Price(" + totalItemText + "items)");
    totalItemsPrice.setText("Rs." + totalItemPriceText + "/-");
    if (deliveryPriceText.equals("free")){
        deliveryPrice.setText(deliveryPriceText);

    }else{
        deliveryPrice.setText("Rs." + deliveryPriceText + "/-");

    }

    totalPrice.setText("Rs."+totalAmountText+"/-");
    cartTotalAmount.setText("Rs."+totalAmountText+"/-");


    savedAmount.setText("You saved Rs."+savedAmountText+"/- on this order.");

       LinearLayout parent=(LinearLayout) cartTotalAmount.getParent().getParent();
       if(totalItemPriceText ==0){
        DBqueries.cartItemModelList.remove(DBqueries.cartItemModelList.size()-1);
       parent.setVisibility(View.GONE);
        }else{
           parent.setVisibility(View.VISIBLE);

       }


    }catch (java.lang.NullPointerException ex){
    System.out.println(ex);
    }
    return; }}

}



