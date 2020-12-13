package com.example.myshowroom;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.myshowroom.MainActivity.showcart;
import static com.example.myshowroom.RegisterActivity.setSignUpFragment;

public class ProductDetailsActivity extends AppCompatActivity {
    private ViewPager productImagesViewPager;
    private TextView productTitle;
    private TextView    averageRatingMiniView;
    private TextView    totalRatingMiniView;
    private TextView    productPrice;
    private TextView    cuttedPrice;
    private ImageView codIndicator;
    private TextView tvCodIndicator;

    private TabLayout viewpagerIndicator;

    private LinearLayout coupenRedeemLayout;
    private Button coupenRedeemBtn;

    private TextView rewardTitle;
    private TextView rewardBody;

//product descn
    private TextView productOnlyDescriptionBody;
    private ConstraintLayout productDetailsOnlyContainer;
    private ConstraintLayout productDetailsTabsContainer;
    private   List<ProductSpecificationModel> productSpecificationModelList=new ArrayList<>();

    private String productDescription;
    private String productOtherDetails;


    ///coupenDialog
    public static TextView coupenTitle;
    public static TextView  coupenExpiryDate;
    public static TextView coupenBody;

    public static Activity productDetailsActivity;

    ///coupenDialog
private Dialog loadingDialog;

    private ViewPager productDetailsViewpager;
    private TabLayout productDetailsTabLayout;
    public static RecyclerView coupensRecyclerview;
    public static  LinearLayout selectedCoupen;




    /////////rating layout
    public static int initialRating;
    public static LinearLayout rateNowContainer;
    private TextView totalRatings;
    private LinearLayout ratingsNoContainer;
    private TextView totalRatingsFigure;
    private LinearLayout ratingsProgressbarContainer;
    private TextView averageRating;



    /////////rating layout
    private Button buyNowBtn;
    private LinearLayout addTOCartBtn;
    public static MenuItem cartItem;
    public static boolean ALREADY_ADDED_TO_WISHLIST=false;
    public static boolean ALREADY_ADDED_TO_CART=false;

    public static FloatingActionButton addToWishListBtn;

    private FirebaseFirestore firebaseFirestore;

    private Dialog signInDialog;

    private FirebaseUser currentUser;
    public static String productID;
    private DocumentSnapshot documentSnapshot;

    public static boolean running_wishlist_query=false;
    public static boolean running_rating_query=false;
    public static boolean running_cart_query=false;


    private TextView badgeCount;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        productImagesViewPager=findViewById(R.id.product_images_viewpager);
        viewpagerIndicator=findViewById(R.id.viewpager_indicator);
        addToWishListBtn=findViewById(R.id.add_to_wish_list_btn);
        productDetailsViewpager=findViewById(R.id.product_details_viewpager);
        productDetailsTabLayout=findViewById(R.id.product_details_tabLayout);
        buyNowBtn=findViewById(R.id.buy_now_btn);
        coupenRedeemBtn=findViewById(R.id.coupon_redeem_btn);
        productTitle=findViewById(R.id.product_title);
        averageRatingMiniView=findViewById(R.id.tv_product_rating_mini_view);
        totalRatingMiniView=findViewById(R.id.total_ratings_mini_view);
        productPrice=findViewById(R.id.product_price);
        cuttedPrice=findViewById(R.id.cutted_price);
        tvCodIndicator=findViewById(R.id.tv_cod_indicator);
        codIndicator=findViewById(R.id.cod_indicator_image_view);
        rewardTitle=findViewById(R.id.reward_title);
        rewardBody=findViewById(R.id.reward_body);

        productDetailsTabsContainer=findViewById(R.id.product_details_tab_container);
        productDetailsOnlyContainer=findViewById(R.id.product_details_container);
        productOnlyDescriptionBody=findViewById(R.id.product_details_body);
        totalRatings=findViewById(R.id.total_ratings);
        ratingsNoContainer=findViewById(R.id.ratings_numbers_container);
        totalRatingsFigure=findViewById(R.id.total_ratings_figure);
        ratingsProgressbarContainer=findViewById(R.id.ratings_progressbar_container);
        averageRating=findViewById(R.id.average_rating);
        addTOCartBtn=findViewById(R.id.add_to_cart_btn);
        coupenRedeemLayout=findViewById(R.id.coupon_redeem_layout);
        initialRating=-1;


        //////////////////loading dialog
        loadingDialog=new Dialog(ProductDetailsActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slide_background));

        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();

        //////////////////loading dialog






        firebaseFirestore=FirebaseFirestore.getInstance();
        List<String> productImages=new ArrayList<>();
        productID=getIntent().getStringExtra("PRODUCT_ID");

        firebaseFirestore.collection("PRODUCTS").document(productID)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){

                      documentSnapshot=task.getResult();


                    for (long x = 1; x<((long) documentSnapshot.get("no_of_product_images"))+1; x++){
                     productImages.add(documentSnapshot.get("product_image_"+x).toString()) ;

                    }
                    ProductImagesAdapter productImagesAdapter=new ProductImagesAdapter(productImages);
                    productImagesViewPager.setAdapter(productImagesAdapter);

                    productTitle.setText(documentSnapshot.get("product_title").toString());
                                            averageRatingMiniView.setText(documentSnapshot.get("average_rating").toString());
                    totalRatingMiniView.setText("("+documentSnapshot.get("total_ratings")+") ratings");
                    productPrice.setText("Rs."+documentSnapshot.get("product_price").toString()+"/-");
                    cuttedPrice.setText("Rs."+documentSnapshot.get("cutted_price").toString()+"/-");

                    if((boolean)documentSnapshot.get("COD")){
                        codIndicator.setVisibility(View.VISIBLE);
                        tvCodIndicator.setVisibility(View.VISIBLE);


                    }else {
                        codIndicator.setVisibility(View.INVISIBLE);
                        tvCodIndicator.setVisibility(View.INVISIBLE);
                    }
                    rewardTitle.setText((long)documentSnapshot.get("free_coupens") +" "+documentSnapshot.get("free_coupen_title").toString());
                    rewardBody.setText(documentSnapshot.get("free_coupen_body").toString());

                    if ((boolean)documentSnapshot.get("use_tab_layout")){
                        productDetailsTabsContainer.setVisibility(View.VISIBLE);
                        productDetailsOnlyContainer.setVisibility(View.GONE);
                        productDescription=documentSnapshot.get("product_description").toString();
                        productOtherDetails=documentSnapshot.get("product_other_details").toString();


                        for (long x=1;x<=(long)documentSnapshot.get("total_spec_titles");x++){
                       productSpecificationModelList.add(new ProductSpecificationModel(0, documentSnapshot.get("spec_title_"+x).toString()));

                            for (long y=1;y<=(long)documentSnapshot.get("spec_title_"+x+"_total_fields");y++){
                          productSpecificationModelList.add(new ProductSpecificationModel(documentSnapshot.get("spec_title_"+x+"_field_"+y+"_name").toString(),documentSnapshot.get("spec_title_"+x+"_field_"+y+"_value").toString(),1))     ;
                            }


                            }


                    }else {

                        productDetailsTabsContainer.setVisibility(View.GONE);
                        productDetailsOnlyContainer.setVisibility(View.VISIBLE);
                        productOnlyDescriptionBody.setText(documentSnapshot.get("product_description").toString());

                    }

                    totalRatings.setText((long)documentSnapshot.get("total_ratings")+" ratings");

                    for (int x=0;x<5;x++){
                            TextView rating = (TextView) ratingsNoContainer.getChildAt(x);
                            rating.setText(String.valueOf((long)documentSnapshot.get((5-x)+"_star")));

                        ProgressBar progressBar=(ProgressBar) ratingsProgressbarContainer.getChildAt(x);

                       int maxProgress= Integer.parseInt(String.valueOf((long)documentSnapshot.get("total_ratings")));
                       progressBar.setMax(maxProgress);
                       progressBar.setProgress(Integer.parseInt(String.valueOf((long)documentSnapshot.get((5-x)+"_star"))));

                    }
                    totalRatingsFigure.setText(String.valueOf((long)documentSnapshot.get("total_ratings")));
                    averageRating.setText(documentSnapshot.get("average_rating").toString());
                    productDetailsViewpager.setAdapter(new ProductDetailsAdapter(getSupportFragmentManager(),productDetailsTabLayout.getTabCount(),productDescription,productOtherDetails,productSpecificationModelList));

                    if(currentUser!=null) {

                        if(DBqueries.myRating.size()==0){
                            DBqueries.loadRatingList(ProductDetailsActivity.this);

                        }
                        if (DBqueries.cartlist.size() == 0) {
                            DBqueries.loadCartList(ProductDetailsActivity.this, loadingDialog,false,badgeCount,new TextView(ProductDetailsActivity.this));

                        }
                        if (DBqueries.wishList.size() == 0) {
                            DBqueries.loadWishList(ProductDetailsActivity.this, loadingDialog,false);

                        } else {
                            loadingDialog.dismiss();
                        }
                    }
                    else{
                        loadingDialog.dismiss();
                    }
                    if(DBqueries.myRatedIds.contains(productID)){
                        int index=DBqueries.myRatedIds.indexOf(productID);
                        initialRating= Integer.parseInt(String.valueOf(DBqueries.myRating.get(index)))-1;
                        setRating(initialRating);
                    }


                    if(DBqueries.cartlist.contains(productID)){
                        ALREADY_ADDED_TO_CART=true;

                    }
                    else{
                        ALREADY_ADDED_TO_CART =false;
                    }


                    if(DBqueries.wishList.contains(productID)){
                        ALREADY_ADDED_TO_WISHLIST=true;
                        addToWishListBtn.setSupportImageTintList(getResources().getColorStateList(R.color.colorPrimary));

                    }
                    else{
                        addToWishListBtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#FDD0D0")));

                        ALREADY_ADDED_TO_WISHLIST=false;
                    }
                    if((boolean)documentSnapshot.get("in_stock")){
                        addTOCartBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(currentUser==null){
                                    signInDialog.show();
                                }
                                else {


                                    //////////todo at cart btn
                                    if(!running_cart_query){
                                        running_cart_query=true;

                                        if(ALREADY_ADDED_TO_CART){
                                            running_cart_query=false;
                                            Toast.makeText(ProductDetailsActivity.this,"Already added to cart!",Toast.LENGTH_SHORT).show();


                                        }else {


                                            Map<String, Object> addProduct = new HashMap<>();
                                            addProduct.put("product_ID_" + String.valueOf(DBqueries.cartlist.size()), productID);

                                            addProduct.put("list_size", (long) (DBqueries.cartlist.size() + 1));


                                            firebaseFirestore.collection("USERS").document(currentUser.getUid()).collection("USER_DATA").document("MY_CART")
                                                    .update(addProduct).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {


                                                        if (DBqueries.cartItemModelList.size() != 0) {

                                                            DBqueries.cartItemModelList.add( 0,new CartItemModel(CartItemModel.CART_ITEM,productID,documentSnapshot.get("product_image_1").toString(),
                                                                    documentSnapshot.get("product_title").toString(),
                                                                    (long)documentSnapshot.get("free_coupens"),
                                                                    documentSnapshot.get("product_price").toString(),
                                                                    documentSnapshot.get("cutted_price").toString(),
                                                                    (long)1,
                                                                    (long)0,
                                                                    (long)0,
                                                                    (boolean)documentSnapshot.get("in_stock"),
                                                                    (long)documentSnapshot.get("max-quantity")
                                                            ));


                                                        }

                                                        ALREADY_ADDED_TO_CART = true;
                                                        DBqueries.cartlist.add(productID);
                                                        Toast.makeText(ProductDetailsActivity.this, "Added to cart successfully", Toast.LENGTH_SHORT).show();
                                                        invalidateOptionsMenu();
                                                        running_cart_query=false;
                                                    } else {
                                                        running_cart_query=false;
                                                        String error = task.getException().getMessage();
                                                        Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                                    }

                                                }
                                            });

                                        }

                                    } }

                            }


                        });


                    }
                    else{
                        buyNowBtn.setVisibility(View.GONE);
                        TextView outOfStock=(TextView) addTOCartBtn.getChildAt(0);
                        outOfStock.setText("Out of Stock");
                        outOfStock.setTextColor(getResources().getColor(R.color.colorPrimary));
                        outOfStock.setCompoundDrawables(null,null,null,null);



                    }

                }else{
                    loadingDialog.dismiss();
                    String error=task.getException().getMessage();
                    Toast.makeText(ProductDetailsActivity.this,error,Toast.LENGTH_SHORT).show();
                }

            }
        });






        viewpagerIndicator.setupWithViewPager(productImagesViewPager,true);

        addToWishListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(currentUser==null){
                    signInDialog.show();
                }

                else{
//                    addToWishListBtn.setEnabled(false);
                    if(!running_wishlist_query){
                        running_wishlist_query=true;

                if(ALREADY_ADDED_TO_WISHLIST){
                    int index=DBqueries.wishList.indexOf(productID);
                    DBqueries.removeFromWishList(index,ProductDetailsActivity.this);

                    addToWishListBtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#FDD0D0")));


                }else {
                    addToWishListBtn.setSupportImageTintList(getResources().getColorStateList(R.color.colorPrimary));


                    Map<String, Object> addProduct = new HashMap<>();
                    addProduct.put("product_ID_" + String.valueOf(DBqueries.wishList.size()), productID);
                    addProduct.put("list_size", (long) (DBqueries.wishList.size() + 1));


                    firebaseFirestore.collection("USERS").document(currentUser.getUid()).collection("USER_DATA").document("MY_WISHLIST")
                            .update(addProduct).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                if (DBqueries.wishlistModelList.size() != 0) {

                                                DBqueries.wishlistModelList.add(new WishlistModel(productID, documentSnapshot.get("product_image_1").toString(),
                                                        documentSnapshot.get("product_title").toString(),
                                                        (long) documentSnapshot.get("free_coupens"),
                                                        documentSnapshot.get("average_rating").toString(),
                                                        (long) documentSnapshot.get("total_ratings"),
                                                        documentSnapshot.get("product_price").toString(),
                                                        documentSnapshot.get("cutted_price").toString(),
                                                        (boolean) documentSnapshot.get("COD"),
                                                        (boolean) documentSnapshot.get("in_stock")


                                                ));
                                            }

                                            ALREADY_ADDED_TO_WISHLIST = true;
                                            addToWishListBtn.setSupportImageTintList(getResources().getColorStateList(R.color.colorPrimary));

                                            DBqueries.wishList.add(productID);


                                Toast.makeText(ProductDetailsActivity.this, "Added wishlist successfully", Toast.LENGTH_SHORT).show();

                            } else {
//                                addToWishListBtn.setEnabled(true);
                            addToWishListBtn.setSupportImageTintList(getResources().getColorStateList(R.color.colorPrimary));


                                String error = task.getException().getMessage();
                                Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                            }
                            running_wishlist_query=false;


                        }
                    });

                }


                }
                }
//                MyWishlistFragment.wishlistAdapter.notifyDataSetChanged();


            }
        });

        productDetailsViewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(productDetailsTabLayout));
        productDetailsTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                productDetailsViewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        //////////rating layout
        rateNowContainer=findViewById(R.id.order_rate_now_container);
        for(int x=0;x<rateNowContainer.getChildCount();x++){
            final int starPositon=x;
            rateNowContainer.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(currentUser==null){
                        signInDialog.show();
                    }
                    else {
                        if(starPositon!=initialRating){
                        if(!running_rating_query) {
                            running_rating_query=true;
                            setRating(starPositon);
                            Map<String, Object> updateRating = new HashMap<>();
                            if (DBqueries.myRatedIds.contains(productID)) {
                                TextView oldRating = (TextView) ratingsNoContainer.getChildAt(5 - initialRating -  1);
                                TextView finalRating = (TextView) ratingsNoContainer.getChildAt(5 - starPositon - 1);


                              updateRating.put(initialRating+1+"_star",Long.parseLong(oldRating.getText().toString())-1);
                              updateRating.put(starPositon+1+"_star",Long.parseLong(finalRating.getText().toString())+ 1);
                              updateRating.put("average_rating",calculateAverageRating((long)starPositon-initialRating,true));

                            }
                            else {
                                updateRating.put(starPositon + 1 + "_star", (long) documentSnapshot.get(starPositon + 1 + "_star") + 1);
//                                productRating.put("average_rating", calculateAverageRating(starPositon + 1));
                                updateRating.put("average_rating",calculateAverageRating((long)starPositon+1,false));
                                updateRating.put("total_ratings", (long) documentSnapshot.get("total_ratings") + 1);

                            }
                            firebaseFirestore.collection("PRODUCTS").document(productID)
                                    .update(updateRating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Map<String, Object> myRating = new HashMap<>();

                                        if (DBqueries.myRatedIds.contains(productID)) {
                                            myRating.put("rating_"+DBqueries.myRatedIds.indexOf(productID),(long)starPositon+1);


                                        }
                                        else{
                                            myRating.put("list_size",(long)DBqueries.myRatedIds.size()+1);
                                            myRating.put("product_ID_" + DBqueries.myRatedIds.size(), productID);
                                            myRating.put("rating_" + DBqueries.myRatedIds.size(), (long) starPositon + 1);

                                        }


                                        firebaseFirestore.collection("USERS").document(currentUser.getUid()).collection("USER_DATA").document("MY_RATINGS")
                                                .update(myRating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    if (DBqueries.myRatedIds.contains(productID)) {

                                                        DBqueries.myRating.set(DBqueries.myRatedIds.indexOf(productID),(long)starPositon+1);


                                                            TextView oldRating = (TextView) ratingsNoContainer.getChildAt(5 - initialRating -  1);
                                                            TextView finalRating = (TextView) ratingsNoContainer.getChildAt(5 - starPositon - 1);



                                                        oldRating.setText(String.valueOf(Integer.parseInt(oldRating.getText().toString()) - 1));
                                                        finalRating.setText(String.valueOf(Integer.parseInt(finalRating.getText().toString()) + 1));


                                                    }
                                                    else {


                                                        DBqueries.myRatedIds.add(productID);
                                                        DBqueries.myRating.add((long) starPositon + 1);

                                                        TextView rating = (TextView) ratingsNoContainer.getChildAt(5 - starPositon - 1);
                                                        rating.setText(String.valueOf(Integer.parseInt(rating.getText().toString()) + 1));


                                                        totalRatingMiniView.setText("(" + ((long) documentSnapshot.get("total_ratings") + 1) + ") ratings");
                                                        totalRatings.setText(((long) documentSnapshot.get("total_ratings") + 1) + " ratings");
                                                        totalRatingsFigure.setText(String.valueOf((long) documentSnapshot.get("total_ratings") + 1));

                                                        Toast.makeText(ProductDetailsActivity.this, "Thank you! for rating.", Toast.LENGTH_SHORT).show();

                                                    }

                                                    for (int x = 0; x < 5; x++) {
                                                        TextView ratingfigures = (TextView) ratingsNoContainer.getChildAt(x);

                                                        ProgressBar progressBar = (ProgressBar) ratingsProgressbarContainer.getChildAt(x);

                                                            int maxProgress = Integer.parseInt(totalRatingsFigure.getText().toString());
                                                            progressBar.setMax(maxProgress);

                                                        progressBar.setProgress(Integer.parseInt(ratingfigures.getText().toString()));

                                                    }
                                                    initialRating=starPositon;
                                                    averageRating.setText(calculateAverageRating(0,true));
                                                    averageRatingMiniView.setText(calculateAverageRating(0,true));


                                                    if(DBqueries.wishList.contains(productID) && DBqueries.wishlistModelList.size()!=0){

                                                        int index=DBqueries.wishList.indexOf(productID);
                                                        DBqueries.wishlistModelList.get(index).setRating(averageRating.getText().toString());
                                                        DBqueries.wishlistModelList.get(index).setTotalRatins(Long.parseLong(totalRatingsFigure.getText().toString()));


                                                    }

                                                } else {
                                                    setRating(initialRating);


                                                    String error = task.getException().getMessage();
                                                    Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();

                                                }
                                                running_rating_query=false;


                                            }
                                        });

                                    } else {
                                        running_rating_query=false;
                                        setRating(initialRating);
                                        String error = task.getException().getMessage();
                                        Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                        }

                    }}
                }
            });
        }
        //////////rating layout

        buyNowBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(currentUser==null){
                    signInDialog.show();
                }
                else {
                    DeliveryActivity.fromCart=false;
                    loadingDialog.show();
                    productDetailsActivity=ProductDetailsActivity.this;



                    DeliveryActivity.cartItemModelList=new ArrayList<>();
                  //  DeliveryActivity.cartItemModelList.clear();
                    DeliveryActivity.cartItemModelList.add( new CartItemModel(CartItemModel.CART_ITEM,productID,documentSnapshot.get("product_image_1").toString(),
                            documentSnapshot.get("product_title").toString(),
                            (long)documentSnapshot.get("free_coupens"),
                            documentSnapshot.get("product_price").toString(),
                            documentSnapshot.get("cutted_price").toString(),
                            (long)1,
                            (long)0,
                            (long)0,
                            (boolean)documentSnapshot.get("in_stock"),
                            (long)documentSnapshot.get("max-quantity")
                    ));
                    DeliveryActivity.cartItemModelList.add(new CartItemModel(CartItemModel.TOTAL_AMOUNT));

                    if(DBqueries.addressesModelList.size()==0) {
                        DBqueries.loadAddresses(ProductDetailsActivity.this, loadingDialog);
                    }
                    else{
                        loadingDialog.dismiss();
                        Intent deliveryIntent=new Intent(ProductDetailsActivity.this,DeliveryActivity.class);
                        startActivity(deliveryIntent);
                    }
                }
            }
        });





/////coupen dialog
        final Dialog checkCoupenPriceDialog=new Dialog(ProductDetailsActivity.this);
        checkCoupenPriceDialog.setContentView(R.layout.coupen_redeem_dialog);
        checkCoupenPriceDialog.setCancelable(true);
        checkCoupenPriceDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        ImageView toggleRecyclerview=checkCoupenPriceDialog.findViewById(R.id.toggle_recyclerview);
        coupensRecyclerview=checkCoupenPriceDialog.findViewById(R.id.coupens_recyclerview);
        selectedCoupen=checkCoupenPriceDialog.findViewById(R.id.selected_coupen);

        coupenTitle=checkCoupenPriceDialog.findViewById(R.id.coupen_title);
        coupenExpiryDate=checkCoupenPriceDialog.findViewById(R.id.coupen_validity);
        coupenBody=checkCoupenPriceDialog.findViewById(R.id.coupen_body);


        TextView orginalPrice=checkCoupenPriceDialog.findViewById(R.id.original_price);
        TextView discountedPrice=checkCoupenPriceDialog.findViewById(R.id.discounted_price);

        LinearLayoutManager layoutManager=new LinearLayoutManager(ProductDetailsActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        coupensRecyclerview.setLayoutManager(layoutManager);

        List<RewardModel> rewardModelList=new ArrayList<>();

        rewardModelList.add(new RewardModel("Cashback","till 2nd june 2020","Get 20% off on product prchase above 2000"));
        rewardModelList.add(new RewardModel("Cashback","till 2nd june 2020","Get 20% off on product prchase above 2000"));
        rewardModelList.add(new RewardModel("Discount","till 2nd june 2020","Get 20% off on product prchase above 2000"));
        rewardModelList.add(new RewardModel("Diwali sale","till 2nd june 2020","Get 20% off on product prchase above 2000"));
        rewardModelList.add(new RewardModel("Boom offer","till 2nd june 2020","Get 20% off on product prchase above 2000"));
        rewardModelList.add(new RewardModel("buy 2 get 1 free","till 2nd june 2020","Get 20% off on product prchase above 2000"));
        rewardModelList.add(new RewardModel("Dasara sale","till 2nd june 2020","Get 20% off on product prchase above 2000"));
        rewardModelList.add(new RewardModel("ganesh fesival sale","till 2nd june 2020","Get 20% off on product prchase above 2000"));
        rewardModelList.add(new RewardModel("Cashback","till 2nd june 2020","Get 20% off on product prchase above 2000"));
        rewardModelList.add(new RewardModel("independence day sale","till 2nd june 2020","Get 20% off on product prchase above 2000"));
        rewardModelList.add(new RewardModel("big blow","till 2nd june 2020","Get 20% off on product prchase above 2000"));

        MyRewardsAdapter myRewardsAdapter =new MyRewardsAdapter(rewardModelList,true);
        coupensRecyclerview.setAdapter(myRewardsAdapter);
        myRewardsAdapter.notifyDataSetChanged();

        toggleRecyclerview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogRecyclerview();
            }
        });
/////coupen dialog

        coupenRedeemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkCoupenPriceDialog.show();
            }
        });
///sign in dialog
        signInDialog=new Dialog(ProductDetailsActivity.this);
        signInDialog.setContentView(R.layout.sign_in_dialog);
        signInDialog.setCancelable(true);

        signInDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        Button dialogSignInBtn=signInDialog.findViewById(R.id.sign_in_btn);
        Button dialogSignUpBtn=signInDialog.findViewById(R.id.sign_up_btn);


        final Intent registerIntent=new Intent(ProductDetailsActivity.this,RegisterActivity.class);

        dialogSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signinFragment.disableCloseBtn=true;
                signupFragment.disableCloseBtn=true;

                signInDialog.dismiss();
                setSignUpFragment=false;
                startActivity(registerIntent);

            }
        });


        dialogSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signupFragment.disableCloseBtn=true;
                signinFragment.disableCloseBtn=true;
                signInDialog.dismiss();
                setSignUpFragment=true;
                startActivity(registerIntent);

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        currentUser= FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser==null){
            coupenRedeemLayout.setVisibility(View.GONE);

        }
        else{
            coupenRedeemLayout.setVisibility(View.VISIBLE);

        }
        if(currentUser!=null) {


            if(DBqueries.myRating.size()==0){
                DBqueries.loadRatingList(ProductDetailsActivity.this);

            }

            if (DBqueries.wishList.size() == 0) {
                DBqueries.loadWishList(ProductDetailsActivity.this, loadingDialog,false);

            } else {
                loadingDialog.dismiss();

            }
        }
        else{
            loadingDialog.dismiss();
        }
        if(DBqueries.myRatedIds.contains(productID)){
            int index=DBqueries.myRatedIds.indexOf(productID);
            initialRating= Integer.parseInt(String.valueOf(DBqueries.myRating.get(index)))-1;
            setRating(initialRating);
        }
        if(DBqueries.cartlist.contains(productID)){
            ALREADY_ADDED_TO_CART=true;

        }
        else{
            ALREADY_ADDED_TO_CART =false;
        }


            if(DBqueries.wishList.contains(productID)){
            ALREADY_ADDED_TO_WISHLIST=true;
            addToWishListBtn.setSupportImageTintList(getResources().getColorStateList(R.color.colorPrimary));

        }
        else{
            addToWishListBtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#FDD0D0")));

            ALREADY_ADDED_TO_WISHLIST=false;
        }
        invalidateOptionsMenu();


    }

    public static void showDialogRecyclerview(){
        if(coupensRecyclerview.getVisibility()==View.GONE){
            coupensRecyclerview.setVisibility(View.VISIBLE);
            selectedCoupen.setVisibility(View.GONE);

        }
        else {
            coupensRecyclerview.setVisibility(View.GONE);
            selectedCoupen.setVisibility(View.VISIBLE);

        }

    }

    public static void setRating(int starPositon) {
        for (int x=0;x<rateNowContainer.getChildCount();x++){
            ImageView starBtn=(ImageView)rateNowContainer.getChildAt(x);
            starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#bebebe")));
            if(x<=starPositon){
                starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ffbb00")));

            }
        }
    }

    private String calculateAverageRating(long currentUserRating,boolean update){
        Double totalStars= Double.valueOf(0);
        for (int x=1;x<6;x++){
            TextView ratingno= (TextView) ratingsNoContainer.getChildAt(5-x);
                totalStars = totalStars +( Long.parseLong(ratingno.getText().toString()) * x);


        }
        totalStars=totalStars+currentUserRating;

        if(update){
            return String.valueOf(totalStars / Long.parseLong(totalRatingsFigure.getText().toString())).substring(0,3);

        }else
        {
            return String.valueOf(totalStars / (Long.parseLong(totalRatingsFigure.getText().toString())+1)).substring(0,3);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_and_cart_icon, menu);
         cartItem=menu.findItem(R.id.main_cart_icon);

            cartItem.setActionView(R.layout.badge_layout);
            ImageView badgeIcon=cartItem.getActionView().findViewById(R.id.badge_icon);
            badgeIcon.setImageResource(R.drawable.cart);
             badgeCount=cartItem.getActionView().findViewById(R.id.badge_count);

        if(currentUser!=null){
            if (DBqueries.cartlist.size() == 0) {
                DBqueries.loadCartList(ProductDetailsActivity.this, loadingDialog,false,badgeCount,new TextView(ProductDetailsActivity.this));

            }
            else{
                badgeCount.setVisibility(View.VISIBLE);

                if(DBqueries.cartlist.size()<100) {

                    badgeCount.setText(String.valueOf(DBqueries.cartlist.size()));
                }
                else{
                    badgeCount.setText("99");

                }
            }
        }

            cartItem.getActionView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(currentUser==null){
                        signInDialog.show();
                    }else {
                        Intent cartIntent = new Intent(ProductDetailsActivity.this, MainActivity.class);
                        showcart = true;
                        startActivity(cartIntent);
                    }
                }
            });


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==android.R.id.home){
            productDetailsActivity  =null;
            finish();
            return true;

        }else if(id==R.id.main_search_icon){
            return true;


        }
        else if(id==R.id.main_cart_icon){
            if(currentUser==null){
                signInDialog.show();
            }else {
                Intent cartIntent = new Intent(ProductDetailsActivity.this, MainActivity.class);
                showcart = true;
                startActivity(cartIntent);
                return true;
            }

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        productDetailsActivity=null;
        super.onBackPressed();

    }
}
