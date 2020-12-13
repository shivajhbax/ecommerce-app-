package com.example.myshowroom;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class Categoryadapter extends RecyclerView.Adapter<Categoryadapter.ViewHolder> {
    private List<Categorymodel> categorymodelList;
private int lastPosition=-1;
    public Categoryadapter(List<Categorymodel> categorymodellist) {
        this.categorymodelList = categorymodellist;
    }

    @NonNull
    @Override
    public Categoryadapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.categoryitem,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Categoryadapter.ViewHolder viewHolder, int position) {

    String icon=categorymodelList.get(position).getCategoryiconlink();

        String name=categorymodelList.get(position).getCategoryname();
        viewHolder.setCategory(name,position);
        viewHolder.setCategoryicon(icon);
        if(lastPosition <position) {
            Animation animation = new AnimationUtils().loadAnimation(viewHolder.itemView.getContext(), R.anim.fade_in);
            viewHolder.itemView.setAnimation(animation);
            lastPosition=position;

        }
    }

    @Override
    public int getItemCount() {
        return categorymodelList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView categoryicon;
        private TextView categoryname;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryicon=itemView.findViewById(R.id.category_icon);
            categoryname=itemView.findViewById(R.id.category_name);

        }
        private void setCategoryicon(String iconUrl){
            ////set category icons
            if(!iconUrl.equals("null")) {
                Glide.with(itemView.getContext()).load(iconUrl).apply(new RequestOptions().placeholder(R.drawable.iconplae)).into(categoryicon);
            }
            else
            {
                categoryicon.setImageResource(R.drawable.home);
            }

        }
        private void setCategory(final String name, final int positon){
            categoryname.setText(name);
            if(!name.equals("")){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(positon!=0) {
                        Intent categoryIntent = new Intent(itemView.getContext(), CategoryActivity.class);
                        categoryIntent.putExtra("CategoryName", name);
                        itemView.getContext().startActivity(categoryIntent);
                    }
                }
            });}

        }
    }
}
