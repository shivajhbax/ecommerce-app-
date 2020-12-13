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
public class MyRewardsFragment extends Fragment {

    public MyRewardsFragment() {
        // Required empty public constructor
    }
private RecyclerView rewardsRecyclerview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_my_rewards, container, false);
        rewardsRecyclerview=view.findViewById(R.id.my_rewards_recylclerview);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rewardsRecyclerview.setLayoutManager(layoutManager);

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
        MyRewardsAdapter myRewardsAdapter =new MyRewardsAdapter(rewardModelList,false);
        rewardsRecyclerview.setAdapter(myRewardsAdapter);
        myRewardsAdapter.notifyDataSetChanged();
        return view;
    }
}
