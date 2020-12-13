package com.example.myshowroom.ui.home;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;
import com.bumptech.glide.Glide;
import com.example.myshowroom.*;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.*;

import static com.example.myshowroom.DBqueries.*;

public class HomeFragment extends Fragment {
public HomeFragment(){}

    public static SwipeRefreshLayout swipeRefreshLayout;

private List<Categorymodel> categorymodelFakeList=new ArrayList<>();
    private HomeViewModel homeViewModel;
    private RecyclerView categoryrecyclerview;
    private Categoryadapter categoryadapter;
   private RecyclerView homePageRecyclerview;
   private List<HomePageModel> homePageModelFakeList=new ArrayList<>();


    private ImageView noInterenetConnection;
   private   ConnectivityManager connectivityManager;
   private NetworkInfo networkInfo;
   private Button retryBtn;

    HomePageAdapter adapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
     /*
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
//                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

       /* view.findViewById(R.id.button_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeFragmentDirections.ActionHomeFragmentToHomeSecondFragment action =
                        HomeFragmentDirections.actionHomeFragmentToHomeSecondFragment
                                ("From HomeFragment");
                NavHostFragment.findNavController(HomeFragment.this)
                        .navigate(action);
            }
        });*/

        // Inflate the layout for this fragment3

        View view= inflater.inflate(R.layout.fragment_home, container, false);
        swipeRefreshLayout=view.findViewById(R.id.refresh_layout);
        noInterenetConnection=view.findViewById(R.id.no_internet_connection);
        retryBtn=view.findViewById(R.id.retry_btn);

        categoryrecyclerview=view.findViewById(R.id.category_recycler_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        categoryrecyclerview.setLayoutManager(layoutManager);
        swipeRefreshLayout.setColorSchemeColors(getContext().getResources().getColor(R.color.grey),getContext().getResources().getColor(R.color.colorPrimary),getContext().getResources().getColor(R.color.successGreen));



        homePageRecyclerview=view.findViewById(R.id.home_page_recylcer_view);
        LinearLayoutManager testinglayoutmanager=new LinearLayoutManager(getContext());
        testinglayoutmanager.setOrientation(LinearLayoutManager.VERTICAL);
        homePageRecyclerview.setLayoutManager(testinglayoutmanager);

////////////categorymodelFakeList

        categorymodelFakeList.add(new Categorymodel("null",""));
        categorymodelFakeList.add(new Categorymodel("",""));
        categorymodelFakeList.add(new Categorymodel("",""));
        categorymodelFakeList.add(new Categorymodel("",""));
        categorymodelFakeList.add(new Categorymodel("",""));
        categorymodelFakeList.add(new Categorymodel("",""));
        categorymodelFakeList.add(new Categorymodel("",""));
        categorymodelFakeList.add(new Categorymodel("",""));
        categorymodelFakeList.add(new Categorymodel("",""));
        categorymodelFakeList.add(new Categorymodel("",""));
        categorymodelFakeList.add(new Categorymodel("",""));



////////////categorymodelFakeList

        ////////////home page FakeList
        List<SliderModel> sliderModelFakeList=new ArrayList<>();
        sliderModelFakeList.add(new SliderModel("null","#dfdfdf"));
        sliderModelFakeList.add(new SliderModel("null","#dfdfdf"));
        sliderModelFakeList.add(new SliderModel("null","#dfdfdf"));
        sliderModelFakeList.add(new SliderModel("null","#dfdfdf"));
        sliderModelFakeList.add(new SliderModel("null","#dfdfdf"));
        sliderModelFakeList.add(new SliderModel("null","#dfdfdf"));


        List<HorizontalProductScrollModel> horizontalProductScrollModelFakeList =new ArrayList<>();
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));


        homePageModelFakeList.add(new HomePageModel(0,sliderModelFakeList));
        homePageModelFakeList.add(new HomePageModel(1,"","#dfdfdf"));
        homePageModelFakeList.add(new HomePageModel(2,"","#dfddf",horizontalProductScrollModelFakeList,new ArrayList<WishlistModel>()));
        homePageModelFakeList.add(new HomePageModel(3,"","#dfdfdf",horizontalProductScrollModelFakeList));




        ////////////home page FakeList

        categoryadapter=new Categoryadapter(categorymodelFakeList);


        adapter=new HomePageAdapter(homePageModelFakeList);





        connectivityManager=(ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
       networkInfo=connectivityManager.getActiveNetworkInfo();




        if(networkInfo!=null && networkInfo.isConnected()==true) {
            MainActivity.drawer.setDrawerLockMode(0);
            noInterenetConnection.setVisibility(View.GONE);
                retryBtn.setVisibility(View.GONE);
            categoryrecyclerview.setVisibility(View.VISIBLE);
            homePageRecyclerview.setVisibility(View.VISIBLE);


            if (categorymodelList.size()==0){

                loadCategories(categoryrecyclerview,getContext());

            }
            else{
                categoryadapter=new Categoryadapter(categorymodelList);
                categoryadapter.notifyDataSetChanged();
            }
            categoryrecyclerview.setAdapter(categoryadapter);



            if (lists.size()==0){
                loadedCategoriesNames.add("SPORTS");
                lists.add(new ArrayList<HomePageModel>());


                loadFragmentData(homePageRecyclerview,getContext(),0,"SPORTS");
            }
            else{
                adapter=new HomePageAdapter(lists.get(0));
                adapter.notifyDataSetChanged();
            }

            homePageRecyclerview.setAdapter(adapter);


        }
        else{
            MainActivity.drawer.setDrawerLockMode(1);

            categoryrecyclerview.setVisibility(View.GONE);
            homePageRecyclerview.setVisibility(View.GONE);
            Glide.with(this).load(R.drawable.nn).into(noInterenetConnection);
            noInterenetConnection.setVisibility(View.VISIBLE);
            retryBtn.setVisibility(View.VISIBLE);

        }



        ////////////refresh latout

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                reloadPage();
               }
        });

        ////////////refresh latout

        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swipeRefreshLayout.setRefreshing(true);
                reloadPage();
            }
        });


        return view;

    }


    private void reloadPage(){
        networkInfo=connectivityManager.getActiveNetworkInfo();
        DBqueries.clearData();

//        categorymodelList.clear();
//        lists.clear();
//        loadedCategoriesNames.clear();

        if(networkInfo!=null && networkInfo.isConnected()==true) {
            MainActivity.drawer.setDrawerLockMode(0);

            categoryadapter=new Categoryadapter(categorymodelFakeList);
            adapter=new HomePageAdapter(homePageModelFakeList);
            retryBtn.setVisibility(View.GONE);
            noInterenetConnection.setVisibility(View.GONE);
            categoryrecyclerview.setVisibility(View.VISIBLE);
            homePageRecyclerview.setVisibility(View.VISIBLE);
            categoryrecyclerview.setAdapter(categoryadapter);
            homePageRecyclerview.setAdapter(adapter);


            loadCategories(categoryrecyclerview,getContext());

            loadedCategoriesNames.add("SPORTS");
            lists.add(new ArrayList<HomePageModel>());
            loadFragmentData(homePageRecyclerview,getContext(),0,"SPORTS");



        }
        else{
            MainActivity.drawer.setDrawerLockMode(1);

            Toast.makeText(getContext(),"No Internet Connection! ",Toast.LENGTH_SHORT).show();
            categoryrecyclerview.setVisibility(View.GONE);
            homePageRecyclerview.setVisibility(View.GONE);


            Glide.with(getContext()).load(R.drawable.nn).into(noInterenetConnection);
            noInterenetConnection.setVisibility(View.VISIBLE);
            retryBtn.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setRefreshing(false);

        }


    }

}

