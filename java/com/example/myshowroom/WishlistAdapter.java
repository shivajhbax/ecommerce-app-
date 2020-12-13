package com.example.myshowroom;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder> {
    private List<WishlistModel> wishlistModelList;
    private Boolean wishlist;
    private int lastPosition=-1;


    public WishlistAdapter(List<WishlistModel> wishlistModelList,Boolean wishlist) {
        this.wishlistModelList = wishlistModelList;
        this.wishlist=wishlist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.wish_list_item_layout,viewGroup,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        String productId=wishlistModelList.get(position).getProductId();
        String resource=wishlistModelList.get(position).getProductImage();
        String title=wishlistModelList.get(position).getProductTitle();
        long freeCoupen=wishlistModelList.get(position).getFreeCoupens();
        String rating=wishlistModelList.get(position).getRating();
        long totalRatings=wishlistModelList.get(position).getTotalRatins();
        String productPrice=wishlistModelList.get(position).getProductPrice();
        String cuttedPrice=wishlistModelList.get(position).getCuttedPrice();
        boolean paymentMethod=wishlistModelList.get(position).isCOD();
        boolean inStock=wishlistModelList.get(position).isInstock();
        viewHolder.setData(productId,resource,title,freeCoupen,rating,totalRatings,productPrice,cuttedPrice,paymentMethod,position,inStock);

        if(lastPosition <position) {
            Animation animation = new AnimationUtils().loadAnimation(viewHolder.itemView.getContext(), R.anim.fade_in);
            viewHolder.itemView.setAnimation(animation);
            lastPosition=position;

        }

    }

    @Override
    public int getItemCount() {
        return wishlistModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView productImage;
        private TextView productTitle;
        private TextView freeCoupens;
        private ImageView coupenIcons;
        private TextView rating;
        private TextView totalRatings;
        private View pricecut;
        private TextView productPrice;
        private TextView cuttedPrice;

        private TextView paymentMethod;
        private ImageButton deleteBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage=itemView.findViewById(R.id.product_image);
            productTitle=itemView.findViewById(R.id.product_title);
            freeCoupens=itemView.findViewById(R.id.free_coupons);

            coupenIcons=itemView.findViewById(R.id.coupen_icon);
            rating=itemView.findViewById(R.id.tv_product_rating_mini_view);
            totalRatings=itemView.findViewById(R.id.total_ratings);
            pricecut=itemView.findViewById(R.id.price_cut);
            productPrice=itemView.findViewById(R.id.product_price);
            cuttedPrice=itemView.findViewById(R.id.cutted_price);

            paymentMethod=itemView.findViewById(R.id.payment_method);
            deleteBtn=itemView.findViewById(R.id.delete_btn);

        }
        private void setData(String productId,String resource, String title, long freeCoupensNo, String averageRate, long totalRatingsNo, String price, String cuttedPriceValue,boolean COD,final int index ,boolean inStock){

            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.iconplae)).into(productImage);
            productTitle.setText(title);
            if(freeCoupensNo!=0 && inStock) {
                coupenIcons.setVisibility(View.VISIBLE);
                if (freeCoupensNo == 1) {
                    freeCoupens.setText("free " + freeCoupensNo + " coupen");
                } else {
                    freeCoupens.setText("free " + freeCoupensNo + " coupens");
                }
            }else
                {
                    coupenIcons.setVisibility(View.INVISIBLE);
                    freeCoupens.setVisibility(View.INVISIBLE);
                }

            if(inStock){
                cuttedPrice.setVisibility(View.VISIBLE);

                cuttedPrice.setText("Rs."+cuttedPriceValue+"/-");

                if(COD){
                    paymentMethod.setVisibility(View.VISIBLE);
                    paymentMethod.setTextColor(itemView.getResources().getColor(R.color.black));

                }else {
                    paymentMethod.setVisibility(View.INVISIBLE);
                    paymentMethod.setTextColor(itemView.getResources().getColor(R.color.black));

                }
            }
            else{
                //paymentMethod.setVisibility(View.INVISIBLE);
                cuttedPrice.setVisibility(View.INVISIBLE);
                paymentMethod.setText("Out of stock ");
                paymentMethod.setTextColor(itemView.getResources().getColor(R.color.colorPrimary));
            }
            rating.setText(averageRate);
            totalRatings.setText("("+totalRatingsNo+" ratings)");
            productPrice.setText("Rs."+price+"/-");


           if(wishlist){
               deleteBtn.setVisibility(View.VISIBLE);
           }else
           {
               deleteBtn.setVisibility(View.GONE);
           }
                    deleteBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(!ProductDetailsActivity.running_wishlist_query){
                            ProductDetailsActivity.running_wishlist_query=true;

                                    DBqueries.removeFromWishList(index,itemView.getContext());
                        }}
                    });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent productDetailsIntent=new Intent(itemView.getContext(),ProductDetailsActivity.class);
                    productDetailsIntent.putExtra("PRODUCT_ID",productId);
                    itemView.getContext().startActivity(productDetailsIntent);

                }
            });


            }

        }

    }

