package com.example.foody;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

// Adapter class for displaying menu items in a RecyclerView with quantity controls
public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {
    private final List<MenuItem> menuItems;
    private final OnMenuItemClickListener listener;

    // Interface for handling menu item quantity changes
    public interface OnMenuItemClickListener {
        void onAddItem(MenuItem item);
        void onRemoveItem(MenuItem item);
    }

    // Constructor initializing the adapter with menu items and click listener
    public MenuAdapter(List<MenuItem> menuItems, OnMenuItemClickListener listener) {
        this.menuItems = new ArrayList<>(menuItems);
        this.listener = listener;
    }

    // Update menu items using DiffUtil for efficient updates
    public void updateMenuItems(List<MenuItem> newItems) {
        MenuItemDiffCallback diffCallback = new MenuItemDiffCallback(menuItems, newItems);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        menuItems.clear();
        menuItems.addAll(newItems);
        diffResult.dispatchUpdatesTo(this);
    }

    // Create new views for menu items
    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.menu_item, parent, false);
        return new MenuViewHolder(view);
    }

    // Bind data to views for each menu item
    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        MenuItem item = menuItems.get(position);

        // Set text fields
        holder.tvName.setText(item.getName());
        holder.tvDescription.setText(item.getDescription());
        holder.tvPrice.setText(String.format(Locale.US, "$%.2f", item.getPrice()));
        holder.tvQuantity.setText(String.valueOf(item.getQuantity()));

        // Load item image using Glide if available
        if (item.getImageUrl() != null && !item.getImageUrl().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(item.getImageUrl())
                    .centerCrop()
                    .into(holder.ivMenuItem);
        }

        // Set up add button click listener
        holder.btnAdd.setOnClickListener(v -> {
            item.setQuantity(item.getQuantity() + 1);
            holder.tvQuantity.setText(String.valueOf(item.getQuantity()));
            listener.onAddItem(item);
        });

        // Set up remove button click listener
        holder.btnRemove.setOnClickListener(v -> {
            if (item.getQuantity() > 0) {
                item.setQuantity(item.getQuantity() - 1);
                holder.tvQuantity.setText(String.valueOf(item.getQuantity()));
                listener.onRemoveItem(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    // ViewHolder class to cache view references
    public static class MenuViewHolder extends RecyclerView.ViewHolder {
        ImageView ivMenuItem;
        TextView tvName;
        TextView tvDescription;
        TextView tvPrice;
        TextView tvQuantity;
        ImageButton btnAdd;
        ImageButton btnRemove;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            ivMenuItem = itemView.findViewById(R.id.ivMenuItem);
            tvName = itemView.findViewById(R.id.tvMenuItemName);
            tvDescription = itemView.findViewById(R.id.tvMenuItemDescription);
            tvPrice = itemView.findViewById(R.id.tvMenuItemPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            btnAdd = itemView.findViewById(R.id.btnAdd);
            btnRemove = itemView.findViewById(R.id.btnRemove);
        }
    }

    // DiffUtil callback for efficient list updates
    private static class MenuItemDiffCallback extends DiffUtil.Callback {
        private final List<MenuItem> oldList;
        private final List<MenuItem> newList;

        public MenuItemDiffCallback(List<MenuItem> oldList, List<MenuItem> newList) {
            this.oldList = oldList;
            this.newList = newList;
        }

        @Override
        public int getOldListSize() {
            return oldList.size();
        }

        @Override
        public int getNewListSize() {
            return newList.size();
        }

        // Check if items are the same based on ID
        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return Objects.equals(oldList.get(oldItemPosition).getId(), newList.get(newItemPosition).getId());
        }

        // Check if item contents are the same
        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            MenuItem oldItem = oldList.get(oldItemPosition);
            MenuItem newItem = newList.get(newItemPosition);
            return oldItem.equals(newItem);
        }
    }
}