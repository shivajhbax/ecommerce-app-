package com.example.myshowroom;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.myshowroom.ui.home.HomeFragment;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import static com.example.myshowroom.RegisterActivity.setSignUpFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
private FrameLayout frameLayout;
private FirebaseUser currentUser;

private static final int HOME_FRAGMENT=0;
private static final int CART_FRAGMENT=1;
private static final int ORDERS_FRAGMENT=2;
private static final int WISHLIST_FRAGMENT=3;
public static Activity mainAcitvity;
public static boolean resetMainActivity=false;


    private static final int REWARDS_FRAGMENT=4;
    private static final int ACCOUNT_FRAGMENT=5;
    public static  Boolean showcart=false;

private  NavigationView navigationView;
private ImageView actionBarLogo;

private  int currentFragment=-1;
private Toolbar toolbar;
private Window window;
private Dialog signInDialog;

private TextView badgeCount;

private int scrollFlags;
private  AppBarLayout.LayoutParams params;

    public static DrawerLayout drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
         toolbar = findViewById(R.id.toolbar);
        actionBarLogo=findViewById(R.id.action_bar_logo);
        setSupportActionBar(toolbar);
        window=getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
       params=(AppBarLayout.LayoutParams)toolbar.getLayoutParams();
        scrollFlags=params.getScrollFlags();



         drawer =(DrawerLayout) findViewById(R.id.drawer_layout);
         navigationView = (NavigationView)findViewById(R.id.nav_view);
         navigationView.setNavigationItemSelectedListener(this);
         navigationView.getMenu().getItem(0).setChecked(true);
         frameLayout=findViewById(R.id.main_framelayout);


        if (showcart) {
            mainAcitvity=this;
            drawer.setDrawerLockMode(1);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            gotoFragment("My cart", new MyCartFragment(), -2);

        } else {
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
            );
            drawer.addDrawerListener(toggle);
            toggle.syncState();
            setFragment(new HomeFragment(), HOME_FRAGMENT);
        }




       signInDialog=new Dialog(MainActivity.this);
        signInDialog.setContentView(R.layout.sign_in_dialog);
        signInDialog.setCancelable(true);

        signInDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        Button dialogSignInBtn=signInDialog.findViewById(R.id.sign_in_btn);
        Button dialogSignUpBtn=signInDialog.findViewById(R.id.sign_up_btn);


        final Intent registerIntent=new Intent(MainActivity.this,RegisterActivity.class);

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
        currentUser=FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser==null){
            navigationView.getMenu().getItem(navigationView.getMenu().size()-1).setEnabled(false);

        }else{
            navigationView.getMenu().getItem(navigationView.getMenu().size()-1).setEnabled(true);

        }
        if(resetMainActivity){
            actionBarLogo.setVisibility(View.VISIBLE);

            resetMainActivity=false;
            setFragment(new HomeFragment(),HOME_FRAGMENT);
            navigationView.getMenu().getItem(0).setChecked(true);
        }

        invalidateOptionsMenu();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(currentFragment==HOME_FRAGMENT){
                currentFragment=-1;
                super.onBackPressed();

            }
            else {
                if(showcart){
                    mainAcitvity=null;
                    showcart=false;
                    finish();

                }else {
                    actionBarLogo.setVisibility(View.VISIBLE);
                    invalidateOptionsMenu();
                    setFragment(new HomeFragment(), HOME_FRAGMENT);
                    navigationView.getMenu().getItem(0).setChecked(true);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (currentFragment==HOME_FRAGMENT){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getMenuInflater().inflate(R.menu.main, menu);

            MenuItem cartItem=menu.findItem(R.id.main_cart_icon);
                cartItem.setActionView(R.layout.badge_layout);
                ImageView badgeIcon=cartItem.getActionView().findViewById(R.id.badge_icon);
                badgeIcon.setImageResource(R.drawable.cart);
               badgeCount=cartItem.getActionView().findViewById(R.id.badge_count);

               if(currentUser!=null){
                   if (DBqueries.cartlist.size() == 0) {
                       DBqueries.loadCartList(MainActivity.this, new Dialog(MainActivity.this),false,badgeCount,new TextView(MainActivity.this));


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
                        }
                        else {
                            gotoFragment("My Cart",new MyCartFragment(),CART_FRAGMENT);

                        }
                    }
                });
            }




        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.main_search_icon){
            return true;

        }else if(id==R.id.main_notification_icon){
            return true;


        }
        else if(id==R.id.main_cart_icon){


            if(currentUser==null){
                signInDialog.show();
            }
            else {
                gotoFragment("My Cart",new MyCartFragment(),CART_FRAGMENT);

            }

             return true;

        }
        else if(id==android.R.id.home){
            if(showcart){
                mainAcitvity=null;
                showcart=false;
                finish();
                 return true;
            }
        }
        return super.onOptionsItemSelected(item);

    }

    private void gotoFragment(String title,Fragment fragment,int fragmentNo) {
        actionBarLogo.setVisibility(View.GONE);

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(title);
        invalidateOptionsMenu();
        setFragment(fragment,fragmentNo);
        if(fragmentNo==CART_FRAGMENT || showcart) {
            navigationView.getMenu().getItem(3).setChecked(true);
            params.setScrollFlags(0);

        }else{
            params.setScrollFlags(scrollFlags);
        }

    }




    MenuItem menuItem;



           @Override
            public boolean onNavigationItemSelected(MenuItem item) {
               DrawerLayout drawer=(DrawerLayout) findViewById(R.id.drawer_layout);
               drawer.closeDrawer(GravityCompat.START);
               menuItem=item;



               if(currentUser != null){
                   drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
                       @Override
                       public void onDrawerClosed(View drawerView) {
                           super.onDrawerClosed(drawerView);
                           int id = menuItem.getItemId();
                           if(id==R.id.nav_myshowroom){


                               actionBarLogo.setVisibility(View.VISIBLE);
                               invalidateOptionsMenu();
                               setFragment(new HomeFragment(),HOME_FRAGMENT);

                           }
                           else if (id == R.id.nav_my_orders) {
                               gotoFragment("My Orders",new MyOrdersFragment(),ORDERS_FRAGMENT);

                           }
                           else if(id==R.id.nav_my_rewards){
                               gotoFragment("My Rewards",new MyRewardsFragment(),REWARDS_FRAGMENT);
                           }

                           else if(id==R.id.nav_my_cart){

                               gotoFragment("My Cart",new MyCartFragment(),CART_FRAGMENT);



                           }
                           else if(id==R.id.nav_my_wishlist){
                               gotoFragment("My Wishlist",new MyWishlistFragment(),WISHLIST_FRAGMENT);

                           }
                           else if(id==R.id.nav_my_profile){
                               gotoFragment("My Account",new MYAccountFragment(),ACCOUNT_FRAGMENT);

                           }
                           else if(id==R.id.nav_sign_out){
                               FirebaseAuth.getInstance().signOut();
                               DBqueries.clearData();
                               Intent registerIntent=new Intent(MainActivity.this,RegisterActivity.class);
                               startActivity(registerIntent);
                               finish();

                           }
                       }
                   });


//                   drawer.closeDrawer(GravityCompat.START);

                    return true;
               }
               else{

                   signInDialog.show();return false;
               }


            }





private void setFragment(Fragment fragment,int fragmentNo){
        currentFragment=fragmentNo;
        if (fragmentNo==currentFragment){//!==
    if(fragmentNo==REWARDS_FRAGMENT){

        window.setStatusBarColor(Color.parseColor("#5B04B1"));
        toolbar.setBackgroundColor(Color.parseColor("#5B04B1"));

    }else {
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

    }

    FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
    fragmentTransaction.setCustomAnimations(R.anim.fade_in,R.anim.fade_out);
    fragmentTransaction.replace(frameLayout.getId(),fragment);
    fragmentTransaction.commit();
        }
}


}
