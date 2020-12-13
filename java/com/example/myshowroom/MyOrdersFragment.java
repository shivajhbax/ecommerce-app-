package com.example.myshowroom;

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
public class MyOrdersFragment extends Fragment {

    public MyOrdersFragment() {
        // Required empty public constructor
    }
private RecyclerView myOrdersRecyclerview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_my_orders, container, false);
        myOrdersRecyclerview=view.findViewById(R.id.my_orders_recyclerview);

        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myOrdersRecyclerview.setLayoutManager(layoutManager);

        List<MyOrderItemModel> myOrderItemModelList=new ArrayList<>();

        myOrderItemModelList.add(new MyOrderItemModel(R.drawable.account,2,"camera sony","Delivered on monday 15th aug 2020"));
        myOrderItemModelList.add(new MyOrderItemModel(R.drawable.banner,1,"camera sony","Delivered on monday 15th aug 2020"));
        myOrderItemModelList.add(new MyOrderItemModel(R.drawable.logo,0,"camera sony","cancelled"));
        myOrderItemModelList.add(new MyOrderItemModel(R.drawable.wish,4,"camera sony","Delivered on monday 15th aug 2020"));

        MyOrderAdapter myOrderAdapter=new MyOrderAdapter(myOrderItemModelList);
        myOrdersRecyclerview.setAdapter(myOrderAdapter);
        myOrderAdapter.notifyDataSetChanged();
        return view;
    }
}
