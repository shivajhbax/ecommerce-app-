package com.example.myshowroom;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import static com.example.myshowroom.DeliveryActivity.SELECT_ADDRESS;
import static com.example.myshowroom.MYAccountFragment.MANAGE_ADDRESS;
import static com.example.myshowroom.MyAddressesActivity.refreshItem;

public class AddressesAdapter extends RecyclerView.Adapter<AddressesAdapter.Viewholder> {
    private List<AddressesModel> addressesModelList;
    private int MODE;
    private int preSelectedPosition;


    public AddressesAdapter(List<AddressesModel> addressesModelList,int MODE) {
        this.addressesModelList = addressesModelList;
        this.MODE=MODE;
        preSelectedPosition=DBqueries.selectedAddress;
    }

    @NonNull
    @Override
    public AddressesAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.addresses_item_layout,viewGroup,false);
        return new Viewholder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull AddressesAdapter.Viewholder viewholder, int position) {
        String name=addressesModelList.get(position).getFullname();
        String mobileNO=addressesModelList.get(position).getMobileNo();
        String address=addressesModelList.get(position).getAddress();
        String pincode=addressesModelList.get(position).getPincode();
        Boolean selected=addressesModelList.get(position).getSelected();

        viewholder.setData(name,address,pincode,selected,position,mobileNO);


    }

    @Override
    public int getItemCount() {
        return addressesModelList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private TextView fullname;
        private TextView address;
        private TextView pincode;
        private ImageView icon;
        private LinearLayout opationContainer;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            fullname=itemView.findViewById(R.id.name);
            address=itemView.findViewById(R.id.address);
            pincode=itemView.findViewById(R.id.pincode);
            icon=itemView.findViewById(R.id.icon_view);
            opationContainer=itemView.findViewById(R.id.option_container);

        }
        private void setData(String username, String useraddress, String userpincode, final Boolean selected, final int position,String moblieNo){
            fullname.setText(username+" - "+moblieNo);
            address.setText(useraddress);
            pincode.setText(userpincode);
            if(MODE==SELECT_ADDRESS){
                opationContainer.setVisibility(View.GONE);
                icon.setImageResource(R.drawable.check);
                if(selected){
                    icon.setVisibility(View.VISIBLE);
                    preSelectedPosition=position;
                }
                else
                {
                    icon.setVisibility(View.GONE);
                }

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(preSelectedPosition!=position) {


                            addressesModelList.get(position).setSelected(true);
                            addressesModelList.get(preSelectedPosition).setSelected(false);
                            refreshItem(preSelectedPosition, position);
                            preSelectedPosition = position;
                            DBqueries.selectedAddress=position;
                        }

                    }
                });

            }else if(MODE==MANAGE_ADDRESS){
                opationContainer.setVisibility(View.GONE);
                icon.setImageResource(R.drawable.vertical);
                icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        opationContainer.setVisibility(View.VISIBLE);
                        refreshItem(preSelectedPosition,preSelectedPosition);
                        preSelectedPosition=position;

                    }
                });

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        refreshItem(preSelectedPosition,preSelectedPosition);
                        preSelectedPosition=-1;
                    }
                });

            }

        }
    }
}
