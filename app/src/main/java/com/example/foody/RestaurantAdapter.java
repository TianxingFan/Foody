package com.example.foody;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

// Adapter class for displaying restaurant list in RecyclerView
public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder> {
    private final Context context;
    private List<Restaurant> restaurants;  // Removed final modifier to allow updates

    public RestaurantAdapter(Context context, List<Restaurant> restaurants) {
        this.context = context;
        this.restaurants = restaurants;
    }

    // Method to update restaurant list data
    public void updateData(List<Restaurant> newRestaurants) {
        this.restaurants = newRestaurants;
        notifyDataSetChanged();
    }

    // Creates new views for list items
    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.restaurant_item, parent, false);
        return new RestaurantViewHolder(view);
    }

    // Binds data to views for each list item
    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position) {
        Restaurant restaurant = restaurants.get(position);

        // Set text data
        holder.tvRestaurantName.setText(restaurant.getName());
        holder.tvCuisine.setText(restaurant.getCuisine());
        holder.tvRating.setText(String.format(Locale.US, "%.1f", restaurant.getRating()));
        holder.tvDeliveryTime.setText(restaurant.getDeliveryTime());
        holder.tvPriceRange.setText(restaurant.getPriceRange());

        // Load restaurant image using Glide
        Glide.with(holder.itemView.getContext())
                .load(restaurant.getImageUrl())
                .centerCrop()
                .into(holder.ivRestaurant);

        // Set click listener to open restaurant details
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, RestaurantDetailActivity.class);
            intent.putExtra("restaurant", restaurant);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    // ViewHolder class to cache view references
    public static class RestaurantViewHolder extends RecyclerView.ViewHolder {
        ImageView ivRestaurant;
        TextView tvRestaurantName;
        TextView tvCuisine;
        TextView tvRating;
        TextView tvDeliveryTime;
        TextView tvPriceRange;

        // Initialize view references
        RestaurantViewHolder(@NonNull View itemView) {
            super(itemView);
            ivRestaurant = itemView.findViewById(R.id.ivRestaurant);
            tvRestaurantName = itemView.findViewById(R.id.tvRestaurantName);
            tvCuisine = itemView.findViewById(R.id.tvCuisine);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvDeliveryTime = itemView.findViewById(R.id.tvDeliveryTime);
            tvPriceRange = itemView.findViewById(R.id.tvPriceRange);
        }
    }
}