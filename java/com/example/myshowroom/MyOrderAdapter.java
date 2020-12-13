package com.example.myshowroom;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.Viewholder> {
    private List<MyOrderItemModel> myOrderItemModelList;

    public MyOrderAdapter(List<MyOrderItemModel> myOrderItemModelList) {
        this.myOrderItemModelList = myOrderItemModelList;
    }

    @NonNull
    @Override
    public MyOrderAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_order_item_layout,viewGroup,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrderAdapter.Viewholder viewholder, int position) {

        int resource=myOrderItemModelList.get(position).getProductImage();
        int rating=myOrderItemModelList.get(position).getRating();
        String title=myOrderItemModelList.get(position).getProductTitle();
        String deliveredDate=myOrderItemModelList.get(position).getDeliveryStatus();
        viewholder.setData(resource,title,deliveredDate,rating);


    }

    @Override
    public int getItemCount() {
        return myOrderItemModelList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private ImageView productImage;
        private ImageView orderIndicator;
        private TextView productTitle;
        private TextView deliveryStatus;
        private LinearLayout rateNowContainer;

        public Viewholder(@NonNull final View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.order_product_image);
            productTitle = itemView.findViewById(R.id.product_title);
            orderIndicator = itemView.findViewById(R.id.order_indicator);
            rateNowContainer = itemView.findViewById(R.id.order_rate_now_container);

            deliveryStatus = itemView.findViewById(R.id.order_delivered_date);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentOrderDetails=new Intent(itemView.getContext(),OrderDetailsActivity.class);
                    itemView.getContext().startActivity(intentOrderDetails);
                }
            });
        }

        private void setData(int resource, String title, String deliveredDate,int rating) {
            productImage.setImageResource(resource);
            productTitle.setText(title);

            if (deliveredDate.equals("cancelled")) {
                orderIndicator.setImageTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.colorPrimary)));
            } else {
                orderIndicator.setImageTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.successGreen)));

            }
            deliveryStatus.setText(deliveredDate);

            //////////rating layout
            setRating(rating);

            for (int x = 0; x < rateNowContainer.getChildCount(); x++) {
                final int starPositon = x;
                rateNowContainer.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setRating(starPositon);
                    }
                });
            }
            //////////rating layout
        }

        private void setRating(int starPositon) {
            for (int x = 0; x < rateNowContainer.getChildCount(); x++) {
                ImageView starBtn = (ImageView) rateNowContainer.getChildAt(x);
                starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#bebebe")));
                if (x <= starPositon) {
                    starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ffbb00")));

                }
            }
        }

    }
}
