package com.example.myshowroom;

import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import static com.example.myshowroom.DBqueries.*;

public class CategoryActivity extends AppCompatActivity {

    private RecyclerView categoryRecyclerView;
    private HomePageAdapter adapter;

    private List<HomePageModel> homePageModelFakeList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String title=getIntent().getStringExtra("CategoryName");
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ////////////home page FakeList
        List<SliderModel> sliderModelFakeList=new ArrayList<>();
        sliderModelFakeList.add(new SliderModel("null","#FFFFFF"));
        sliderModelFakeList.add(new SliderModel("null","#FFFFFF"));sliderModelFakeList.add(new SliderModel("null","#FFFFFF"));sliderModelFakeList.add(new SliderModel("null","#FFFFFF"));sliderModelFakeList.add(new SliderModel("null","#FFFFFF"));sliderModelFakeList.add(new SliderModel("null","#FFFFFF"));


        List<HorizontalProductScrollModel> horizontalProductScrollModelFakeList =new ArrayList<>();
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));


        homePageModelFakeList.add(new HomePageModel(0,sliderModelFakeList));
        homePageModelFakeList.add(new HomePageModel(1,"","#FFFFFF"));
        homePageModelFakeList.add(new HomePageModel(2,"","#FFFFFF",horizontalProductScrollModelFakeList,new ArrayList<WishlistModel>()));
        homePageModelFakeList.add(new HomePageModel(3,"","#FFFFFF",horizontalProductScrollModelFakeList));




        ////////////home page FakeList


        categoryRecyclerView=findViewById(R.id.category_recycler_view);


        ////Banner slider

        List<SliderModel> sliderModelList=new ArrayList<SliderModel>();
//        sliderModelList.add(new SliderModel(R.drawable.prof,"#077AE4"));
//        sliderModelList.add(new SliderModel(R.drawable.orders,"#077AE4"));
//        sliderModelList.add(new SliderModel(R.drawable.mail,"#077AE4"));
//
//
//
//
//        sliderModelList.add(new SliderModel(R.drawable.banner,"#077AE4"));
//        sliderModelList.add(new SliderModel(R.drawable.closw,"#077AE4"));
//        sliderModelList.add(new SliderModel(R.drawable.cancel,"#077AE4"));
//        sliderModelList.add(new SliderModel(R.drawable.cart,"#077AE4"));
//        sliderModelList.add(new SliderModel(R.drawable.logout,"#077AE4"));
//        sliderModelList.add(new SliderModel(R.drawable.wish,"#077AE4"));
//        sliderModelList.add(new SliderModel(R.drawable.wishlist,"#077AE4"));
//
//        sliderModelList.add(new SliderModel(R.drawable.cc,"#077AE4"));
//        sliderModelList.add(new SliderModel(R.drawable.profile,"#077AE4"));
//        sliderModelList.add(new SliderModel(R.drawable.prof,"#077AE4"));
//
//
//        sliderModelList.add(new SliderModel(R.drawable.orders,"#077AE4"));
//        sliderModelList.add(new SliderModel(R.drawable.mail,"#077AE4"));
//        sliderModelList.add(new SliderModel(R.drawable.banner,"#077AE4"));


        ////Banner slider

////horizontal  product layout



//        List<HorizontalProductScrollModel> horizontalProductScrollModelList=new ArrayList<>();
//        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.logo,"Redmi 5a","625","RS.5999/-"));
//        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.account,"Redmi 5a","625","RS.5999/-"));
//        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.banner,"Redmi 5a","625","RS.5999/-"));
//        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.cancel,"Redmi 5a","625","RS.5999/-"));
//        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.clo,"Redmi 5a","625","RS.5999/-"));
//        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.cart,"Redmi 5a","625","RS.5999/-"));
//        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.cl,"Redmi 5a","625","RS.5999/-"));
//        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.closw,"Redmi 5a","625","RS.5999/-"));
//        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.wish,"Redmi 5a","625","RS.5999/-"));
//        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.rewards,"Redmi 5a","625","RS.5999/-"));



////horizontal  product layout

        ///////////
        LinearLayoutManager testinglayoutmanager=new LinearLayoutManager(this);
        testinglayoutmanager.setOrientation(LinearLayoutManager.VERTICAL);
        categoryRecyclerView.setLayoutManager(testinglayoutmanager);

        adapter=new HomePageAdapter(homePageModelFakeList);



        int listPosition=0;
        for(int x=0;x<loadedCategoriesNames.size();x++){
            if(loadedCategoriesNames.get(x).equals(title.toUpperCase())){
                listPosition=x;
            }
        }
        if(listPosition==0){
            loadedCategoriesNames.add(title.toUpperCase());
            lists.add(new ArrayList<HomePageModel>());
            loadFragmentData(categoryRecyclerView,this,loadedCategoriesNames.size() - 1,title);

        }
        else {
            adapter=new HomePageAdapter(lists.get(listPosition));


        }
        categoryRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_icon, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.main_search_icon) {
            return true;
        }
        else if(id==android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
