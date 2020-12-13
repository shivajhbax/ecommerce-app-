package com.example.myshowroom;

import android.app.Dialog;
import android.os.Bundle;
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
public class MyWishlistFragment extends Fragment {

    public MyWishlistFragment() {
        // Required empty public constructor
    }
private Dialog loadingDialog;
private RecyclerView wishlistRecyclerview;
public static WishlistAdapter wishlistAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_my_wishlist, container, false);

        //////////////////loading dialog
        loadingDialog=new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.slide_background));

        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();

        //////////////////loading dialog

        wishlistRecyclerview=   view.findViewById(R.id.my_wishlist_recyclerview);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        wishlistRecyclerview.setLayoutManager(linearLayoutManager);


    //        List<WishlistModel> wishlistModelList=new ArrayList<>();

        if(DBqueries.wishlistModelList.size()==0){
            DBqueries.wishList.clear();

            DBqueries.loadWishList(getContext(),loadingDialog,true);
        }else{
            loadingDialog.dismiss();
        }

//        DBqueries.wishList.clear();
//        DBqueries.wishlistModelList.clear();

        wishlistAdapter=new WishlistAdapter(DBqueries.wishlistModelList,true);
        wishlistRecyclerview.setAdapter(wishlistAdapter);
        wishlistAdapter.notifyDataSetChanged();

        return view;
    }

}
