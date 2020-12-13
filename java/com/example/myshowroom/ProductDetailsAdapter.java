package com.example.myshowroom;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class ProductDetailsAdapter extends FragmentPagerAdapter {
    private int totalTabs;
    private String productDescrption;
    private String productOtherDetails;
    private List<ProductSpecificationModel> productSpecificationModelList;

    public ProductDetailsAdapter(@NonNull FragmentManager fm,int totalTabs, String productDescrption, String productOtherDetails, List<ProductSpecificationModel> productSpecificationModelList) {
        super(fm);
        this.productDescrption = productDescrption;
        this.productOtherDetails = productOtherDetails;
        this.productSpecificationModelList = productSpecificationModelList;
        this.totalTabs=totalTabs;

    }



    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
           case 0:
                ProductDescriptionFragment productDescriptionFragment1=new ProductDescriptionFragment();
                productDescriptionFragment1.body=productDescrption;
                return productDescriptionFragment1;
            case 1:
                ProductSpecificationFragment productSpecificationFragment=new ProductSpecificationFragment();
                productSpecificationFragment.productSpecificationModelList=productSpecificationModelList;
                return productSpecificationFragment;

            case 2:
                ProductDescriptionFragment productDescriptionFragment2=new ProductDescriptionFragment();
                productDescriptionFragment2.body=productOtherDetails;
                return productDescriptionFragment2;
            default:
                    return null;
        }

    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
