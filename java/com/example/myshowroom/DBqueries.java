package com.example.myshowroom;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.myshowroom.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import javax.crypto.spec.DESKeySpec;
import java.util.*;

public class DBqueries {


    public static FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
    public static List<Categorymodel>     categorymodelList=new ArrayList<>();
//   public static List<HomePageModel> homePageModelList=new ArrayList<>();

   public static List<List<HomePageModel>> lists=new ArrayList<>();
   public static List<String> loadedCategoriesNames=new ArrayList<>();

   public static List<String> wishList=new ArrayList<>();
   public static List<WishlistModel> wishlistModelList=new ArrayList<>();

   public static List<String> myRatedIds=new ArrayList<>();
   public static List<Long> myRating=new ArrayList<>();

    public static List<String> cartlist=new ArrayList<>();
    public static List<CartItemModel> cartItemModelList=new ArrayList<>();


    public static List<AddressesModel> addressesModelList=new ArrayList<>();
    public static int selectedAddress=-1;


    public static void loadCategories(RecyclerView categoryRecyclerview, Context context){
        categorymodelList.clear();


        firebaseFirestore.collection("CATEGORIES").orderBy("index").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())){
                                categorymodelList.add(new Categorymodel(documentSnapshot.get("icon").toString(),documentSnapshot.get("categoryName").toString()));
                            }
                            Categoryadapter categoryadapter=new Categoryadapter(categorymodelList);
                            categoryRecyclerview.setAdapter(categoryadapter);
                            categoryadapter.notifyDataSetChanged();

                        }else{
                            String error=task.getException().getMessage();
                            Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
    public static void  loadFragmentData(RecyclerView homePageRecyclerview, Context context, final int index, String categoryName){


        firebaseFirestore.collection("CATEGORIES")
                .document(categoryName.toUpperCase())
                .collection("TOP_DEALS").orderBy("index").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())){

                                if(((long)documentSnapshot.get("view_type"))==0){
                                    List<SliderModel> sliderModelList=new ArrayList<>();
                                    long noOfBanners=(long)documentSnapshot.get("no_of_banners");
                                    for(long x=1;x<=noOfBanners;x++){
                                        sliderModelList.add(new SliderModel(documentSnapshot.get("banner_"+x).toString(),
                                                documentSnapshot.get("banner_"+x+"_background").toString()));

                                    }
                                    lists.get(index).add(new HomePageModel(0,sliderModelList));

                                }else if (((long)documentSnapshot.get("view_type"))==1){
                                    lists.get(index).add(new HomePageModel(1,documentSnapshot.get("strip_ad_banner").toString(),documentSnapshot.get("background").toString()));


                                }

                                else if(((long)documentSnapshot.get("view_type"))==2){
                                    List<WishlistModel> viewAllProductList=new ArrayList<>();








                                    List<HorizontalProductScrollModel> horizontalProductScrollModelList=new ArrayList<>();

                                    long no_of_products=(long)documentSnapshot.get("no_of_products");
                                    for(long x=1;x<=no_of_products;x++){
                                        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(documentSnapshot.get("product_ID_"+x).toString(),
                                                documentSnapshot.get("product_image_"+x).toString(),
                                                documentSnapshot.get("product_title_"+x).toString(),
                                                documentSnapshot.get("product_subtitle_"+x).toString(),
                                                documentSnapshot.get("product_price_"+x).toString()));

                                        viewAllProductList.add(new WishlistModel(documentSnapshot.get("product_ID_"+x).toString(),documentSnapshot.get("product_image_"+x).toString(),
                                                documentSnapshot.get("product_full_title_"+x).toString(),
                                                (long)documentSnapshot.get("free_coupens_"+x),
                                                documentSnapshot.get("average_rating_"+x).toString(),
                                                (long)documentSnapshot.get("total_ratings_"+x),
                                                documentSnapshot.get("product_price_"+x).toString(),
                                                documentSnapshot.get("cutted_price_"+x).toString(),
                                                (boolean)documentSnapshot.get("COD_"+x),
                                                (boolean)documentSnapshot.get("in_stock_"+x)

                                        ));

                                    }
                                    lists.get(index).add(new HomePageModel(2,documentSnapshot.get("layout_title").toString(),documentSnapshot.get("layout_background").toString(),horizontalProductScrollModelList,viewAllProductList));
                                }
                                else if(((long)documentSnapshot.get("view_type"))==3){
                                    List<HorizontalProductScrollModel> GridLayoutScrollModelList=new ArrayList<>();

                                    long no_of_products=(long)documentSnapshot.get("no_of_products");
                                    for(long x=1;x<=no_of_products;x++){
                                        GridLayoutScrollModelList.add(new HorizontalProductScrollModel(documentSnapshot.get("product_ID_"+x).toString(),
                                                documentSnapshot.get("product_image_"+x).toString(),
                                                documentSnapshot.get("product_title_"+x).toString(),
                                                documentSnapshot.get("product_subtitle_"+x).toString(),
                                                documentSnapshot.get("product_price_"+x).toString()));

                                    }
                                    lists.get(index).add(new HomePageModel(3,documentSnapshot.get("layout_title").toString(),documentSnapshot.get("layout_background").toString(),GridLayoutScrollModelList));



                                }



                            }
                            HomePageAdapter homePageAdapter=new HomePageAdapter(lists.get(index));
                            homePageRecyclerview.setAdapter(homePageAdapter);
                            homePageAdapter.notifyDataSetChanged();
                            HomeFragment.swipeRefreshLayout.setRefreshing(false);

                        }else{
                            String error=task.getException().getMessage();
                            Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
                        }

                    }
                });


    }
    public static void loadWishList(final Context context, Dialog dialog,final  boolean loadProductData){

        wishList.clear();
        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_WISHLIST")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){

                    for (long x=0;x<(long)task.getResult().get("list_size");x++){
                        wishList.add(task.getResult().get("product_ID_"+x).toString());
                        if(DBqueries.wishList.contains(ProductDetailsActivity.productID)){
                            ProductDetailsActivity.ALREADY_ADDED_TO_WISHLIST=true;
                            if(ProductDetailsActivity.addToWishListBtn!=null) {
                                ProductDetailsActivity.addToWishListBtn.setSupportImageTintList(context.getResources().getColorStateList(R.color.colorPrimary));
                            }
                        }
                        else{
                            if(ProductDetailsActivity.addToWishListBtn!=null) {

                                ProductDetailsActivity.addToWishListBtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#FDD0D0")));
                            }
                            ProductDetailsActivity.ALREADY_ADDED_TO_WISHLIST=false;
                        }
                        if(loadProductData){
                            wishlistModelList.clear();
                            String productId=   task.getResult().get("product_ID_"+x).toString();
                        firebaseFirestore.collection("PRODUCTS").document(task.getResult().get("product_ID_"+x).toString())
                                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()) {
                                    wishlistModelList.add(new WishlistModel(productId,task.getResult().get("product_image_1").toString(),
                                            task.getResult().get("product_title").toString(),
                                            (long) task.getResult().get("free_coupens"),
                                            task.getResult().get("average_rating").toString(),
                                            (long) task.getResult().get("total_ratings"),
                                            task.getResult().get("product_price").toString(),
                                            task.getResult().get("cutted_price").toString(),
                                            (boolean) task.getResult().get("COD"),
                                            (boolean) task.getResult().get("in_stock")

                                    ));

                                    MyWishlistFragment.wishlistAdapter.notifyDataSetChanged();


                                }else{
                                    String error=task.getException().getMessage();
                                    Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    }

}


                }else{
                    String error=task.getException().getMessage();
                    Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();

            }

        });
    }
    public static  void removeFromWishList(int index,final Context context){

        String removedProductId=wishList.get(index);
        wishList.remove(index);


        Map<String,Object> updateWishList=new HashMap<>();

        for (int x=0;x<wishList.size();x++){
            updateWishList.put("product_ID_"+x,wishList.get(x));
        }
        updateWishList.put("list_size",(long)wishList.size());


        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_WISHLIST")
                .set(updateWishList).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    if(wishlistModelList.size()!=0){
                        wishlistModelList.remove(index);

                        MyWishlistFragment.wishlistAdapter.notifyDataSetChanged();

                    }
                    ProductDetailsActivity.ALREADY_ADDED_TO_WISHLIST=false;
                    Toast.makeText(context,"Removed from wishlist successfully!",Toast.LENGTH_SHORT).show();


                }else{
                    if(ProductDetailsActivity.addToWishListBtn!=null) {

                        ProductDetailsActivity.addToWishListBtn.setSupportImageTintList(context.getResources().getColorStateList(R.color.colorPrimary));
                    }
                   wishList.add(index,removedProductId);
                    String error=task.getException().getMessage();
                    Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
                }
//                if(ProductDetailsActivity.addToWishListBtn!=null) {
//
//                    ProductDetailsActivity.addToWishListBtn.setEnabled(true);
//                }
                ProductDetailsActivity.running_wishlist_query=false;

            }
        });
    }
    public static void loadRatingList(final Context context){
        if(!ProductDetailsActivity.running_rating_query) {
            ProductDetailsActivity.running_rating_query=true;
            myRatedIds.clear();
            myRating.clear();

            firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_RATINGS")
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if (task.isSuccessful()) {

                        for (long x = 0; x < (long) task.getResult().get("list_size"); x++) {
                            myRatedIds.add(task.getResult().get("product_ID_" + x).toString());
                            myRating.add((long) task.getResult().get("rating_" + x));

                            if (task.getResult().get("product_ID_" + x).toString().equals(ProductDetailsActivity.productID)) {
                                ProductDetailsActivity.initialRating = Integer.parseInt(String.valueOf((long) task.getResult().get("rating_" + x))) - 1;
                                if(ProductDetailsActivity.rateNowContainer!=null) {
                                    ProductDetailsActivity.setRating(ProductDetailsActivity.initialRating);
                                }
                            }
                        }


                    } else {
                        String error = task.getException().getMessage();
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();

                    }
                    ProductDetailsActivity.running_rating_query=false;


                }
            });
        }
    }


    public static void loadCartList(final Context context, final Dialog dialog, final boolean loadProductData, final TextView badgeCount,final TextView cartTotalAmount){
        cartlist.clear();
       // cartItemModelList.clear();
        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_CART")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){

                    for (long x=0;x<(long)task.getResult().get("list_size");x++){
                       cartlist.add(task.getResult().get("product_ID_"+x).toString());
                        if(DBqueries.cartlist.contains(ProductDetailsActivity.productID)){
                            ProductDetailsActivity.ALREADY_ADDED_TO_CART=true;

                        }
                        else{

                            ProductDetailsActivity.ALREADY_ADDED_TO_CART=false;
                        }
                        if(loadProductData){
                            cartItemModelList.clear();
                            String productId=   task.getResult().get("product_ID_"+x).toString();
                            firebaseFirestore.collection("PRODUCTS").document(task.getResult().get("product_ID_"+x).toString())
                                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if(task.isSuccessful()) {
                                        int index=0;
                                        if(cartlist.size()>=2){
                                            index=cartlist.size()-2;

                                        }
                                        cartItemModelList.add(index ,new CartItemModel(CartItemModel.CART_ITEM,productId,task.getResult().get("product_image_1").toString(),
                                                task.getResult().get("product_title").toString(),
                                                (Long) task.getResult().get("free_coupens"),
                                                task.getResult().get("product_price").toString(),
                                                 task.getResult().get("cutted_price").toString(),
                                                (long)1,
                                                (long)0,
                                                (long)0,
                                                (boolean)task.getResult().get("in_stock"),
                                                (long)task.getResult().get("max-quantity")
                                                ));
                                        if(cartlist.size()==1){
                                            cartItemModelList.add(new CartItemModel(CartItemModel.TOTAL_AMOUNT));
                                            LinearLayout parent=(LinearLayout) cartTotalAmount.getParent().getParent();
                                            parent.setVisibility(View.VISIBLE);

                                        }
                                        if(cartlist.size()==0){
                                            cartItemModelList.clear();
                                        }

                                MyCartFragment.cartAdapter.notifyDataSetChanged();

                                    }

                                    else{
                                        String error=task.getException().getMessage();
                                        Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                        }

                    }
                    if(cartlist.size()!=0){
                        badgeCount.setVisibility(View.VISIBLE);
                    }
                    else{
                        badgeCount.setVisibility(View.INVISIBLE);
                    }
                    if(DBqueries.cartlist.size()<100) {

                        badgeCount.setText(String.valueOf(DBqueries.cartlist.size()));
                    }
                    else{
                        badgeCount.setText("99");

                    }

                }else{
                    String error=task.getException().getMessage();
                    Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();

            }
        });

    }
    public static void removeFromCart(final int index,final  Context context,final TextView cartTotalAmount){

        String removedProductId= cartlist.get(index);
        cartlist.remove(index);


        Map<String,Object> updateCartList=new HashMap<>();

        for (int x=0;x<cartlist.size();x++){
            updateCartList.put("product_ID_"+x,cartlist.get(x));
        }
        updateCartList.put("list_size",(long)cartlist.size());


        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_CART")
                .set(updateCartList).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    if(cartItemModelList.size()!=0){
                        cartItemModelList.remove(index);
                        MyCartFragment.cartAdapter.notifyDataSetChanged();

                    }
                    if(cartlist.size()==0){
                        LinearLayout parent=(LinearLayout) cartTotalAmount.getParent().getParent();
                        parent.setVisibility(View.GONE);

                        cartItemModelList.clear();
                    }

                    Toast.makeText(context,"Removed from cart successfully!",Toast.LENGTH_SHORT).show();


                }else{

                    cartlist.add(index,removedProductId);
                    String error=task.getException().getMessage();
                    Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
                }
//                if(ProductDetailsActivity.addToWishListBtn!=null) {
//
//                    ProductDetailsActivity.addToWishListBtn.setEnabled(true);
//                }
                ProductDetailsActivity.running_cart_query=false;

            }
        });

    }

    public static void loadAddresses(final Context context,Dialog loadingDialog){
        addressesModelList.clear();
        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_ADDRESSES")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    Intent deliveryIntent;

                    if((long)task.getResult().get("list_size") == 0){//0
                         deliveryIntent=new Intent(context,AddressActivity.class);
                         deliveryIntent.putExtra("INTENT","deliveryIntent");
                    }
                    else{
                        for(long x=1;x<(long)task.getResult().get("list_size")+1;x++){
                            addressesModelList.add(new AddressesModel(task.getResult().get("fullname_"+x).toString(),
                                    task.getResult().get("address_"+x).toString(),
                                    task.getResult().get("pincode_"+x).toString(),
                                    (boolean) task.getResult().get("selected_"+x),
                                    task.getResult().get("mobile_no_"+x).toString()
                                    ));

                                    if( (boolean) task.getResult().get("selected_"+x)){
                                        selectedAddress= Integer.parseInt(String.valueOf(x-1));

                                    }


                        }
                       deliveryIntent=new Intent(context, DeliveryActivity.class);


                    }
                    context.startActivity(deliveryIntent);

                }else{
                    String error=task.getException().getMessage();
                    Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dismiss();

            }
        });


    }


    public static void clearData(){
        categorymodelList.clear();
        lists.clear();
        loadedCategoriesNames.clear();
        wishList.clear();
        wishlistModelList.clear();
        cartlist.clear();
        cartItemModelList.clear();
        addressesModelList.clear();
        myRatedIds.clear();
        myRating.clear();

    }

}
