package com.example.glamfinal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<Item> items;
    private String userPhoneNo;  // User's phone number passed from the activity
    private boolean isAdmin; // Boolean flag to indicate admin status

    public RecyclerViewAdapter(List<Item> items, String userPhoneNo, boolean isAdmin) {
        this.items = items;
        this.userPhoneNo = userPhoneNo;
        this.isAdmin = isAdmin; // Assign isAdmin parameter to the field
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.services_rv, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = items.get(position);
        holder.tvName.setText(item.getName());
        holder.tvInfo.setText(item.getInfo());
        holder.tvPrice.setText("Rs " + item.getPrice());

        // Set visibility of imgAdd based on isAdmin
        holder.imgAdd.setVisibility(isAdmin ? View.GONE : View.VISIBLE);

        holder.imgAdd.setOnClickListener(v -> {
            // Display a toast message
            Toast.makeText(v.getContext(), "Service added to cart !!!", Toast.LENGTH_SHORT).show();
            // Add item to cart
            addToCart(item);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private void addToCart(Item item) {
        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("Cart");
        DatabaseReference newServiceRef = cartRef.child(userPhoneNo).push();
        newServiceRef.setValue(new ServiceDetail(item.getName(), item.getPrice()));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName;
        public TextView tvInfo;
        public TextView tvPrice;
        public ImageView imgAdd;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvInfo = itemView.findViewById(R.id.tv_info);
            tvPrice = itemView.findViewById(R.id.tv_price);
            imgAdd = itemView.findViewById(R.id.imgAdd);
        }
    }

    static class ServiceDetail {
        public String name;
        public String price;

        public ServiceDetail(String name, String price) {
            this.name = name;
            this.price = price;
        }
    }
}
