package com.example.foody;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Locale;

// Adapter class for managing and displaying cart items in a RecyclerView
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private final List<MenuItem> cartItems;
    private final OnCartItemClickListener listener;

    // Interface for handling cart item quantity updates
    public interface OnCartItemClickListener {
        void onUpdateQuantity(MenuItem item);
    }

    // Constructor initializing the adapter with cart items and click listener
    public CartAdapter(List<MenuItem> cartItems, OnCartItemClickListener listener) {
        this.cartItems = cartItems;
        this.listener = listener;
    }

    // Create new views for cart items
    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }

    // Bind data to views for each cart item
    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        MenuItem item = cartItems.get(position);

        // Set item details
        holder.tvName.setText(item.getName());
        holder.tvPrice.setText(String.format(Locale.US, "$%.2f", item.getPrice()));
        holder.tvQuantity.setText(String.valueOf(item.getQuantity()));
        holder.tvItemTotal.setText(String.format(Locale.US, "$%.2f", item.getPrice() * item.getQuantity()));

        // Load item image using Glide
        Glide.with(holder.itemView.getContext())
                .load(item.getImageUrl())
                .centerCrop()
                .into(holder.ivMenuItem);

        // Set up add button click listener
        holder.btnAdd.setOnClickListener(v -> {
            item.setQuantity(item.getQuantity() + 1);
            notifyItemChanged(position);
            listener.onUpdateQuantity(item);
        });

        // Set up remove button click listener
        holder.btnRemove.setOnClickListener(v -> {
            if (item.getQuantity() > 0) {
                item.setQuantity(item.getQuantity() - 1);
                notifyItemChanged(position);
                listener.onUpdateQuantity(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    // ViewHolder class to cache view references for cart items
    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView ivMenuItem;
        TextView tvName;
        TextView tvPrice;
        TextView tvQuantity;
        TextView tvItemTotal;
        ImageButton btnAdd;
        ImageButton btnRemove;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize view references
            ivMenuItem = itemView.findViewById(R.id.ivMenuItem);
            tvName = itemView.findViewById(R.id.tvMenuItemName);
            tvPrice = itemView.findViewById(R.id.tvMenuItemPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvItemTotal = itemView.findViewById(R.id.tvItemTotal);
            btnAdd = itemView.findViewById(R.id.btnAdd);
            btnRemove = itemView.findViewById(R.id.btnRemove);
        }
    }
}