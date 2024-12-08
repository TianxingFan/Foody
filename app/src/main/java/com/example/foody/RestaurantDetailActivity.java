package com.example.foody;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RestaurantDetailActivity extends AppCompatActivity implements MenuAdapter.OnMenuItemClickListener {
    private RecyclerView menuRecyclerView;
    private MenuAdapter menuAdapter;
    private View bottomSheet;
    private BottomSheetBehavior<View> bottomSheetBehavior;
    private TextView tvCartTotal;
    private Button btnViewCart;
    private final Map<String, MenuItem> cartItems = new HashMap<>();
    private Restaurant currentRestaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);

        currentRestaurant = (Restaurant) getIntent().getSerializableExtra("restaurant");

        setupViews();
        setupBottomSheet();
        loadRestaurantDetails(currentRestaurant);
        setupMenuRecyclerView();
        loadMenuItems();
    }

    private void setupViews() {
        menuRecyclerView = findViewById(R.id.menuRecyclerView);
        bottomSheet = findViewById(R.id.bottomSheet);
        tvCartTotal = findViewById(R.id.tvCartTotal);
        btnViewCart = findViewById(R.id.btnViewCart);

        btnViewCart.setOnClickListener(v -> {
            Intent intent = new Intent(this, CartActivity.class);
            intent.putExtra("cartItems", new ArrayList<>(cartItems.values()));
            startActivity(intent);
        });
    }

    private void setupBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void loadRestaurantDetails(Restaurant restaurant) {
        ImageView ivRestaurantImage = findViewById(R.id.ivRestaurantImage);
        TextView tvRestaurantName = findViewById(R.id.tvRestaurantName);
        TextView tvCuisine = findViewById(R.id.tvCuisine);
        TextView tvRating = findViewById(R.id.tvRating);
        TextView tvDeliveryTime = findViewById(R.id.tvDeliveryTime);
        TextView tvPriceRange = findViewById(R.id.tvPriceRange);

        Glide.with(this)
                .load(restaurant.getImageUrl())
                .centerCrop()
                .into(ivRestaurantImage);

        tvRestaurantName.setText(restaurant.getName());
        tvCuisine.setText(restaurant.getCuisine());
        tvRating.setText(String.format(Locale.US, "%.1f★", restaurant.getRating()));
        tvDeliveryTime.setText(restaurant.getDeliveryTime());
        tvPriceRange.setText(restaurant.getPriceRange());
    }

    private void setupMenuRecyclerView() {
        menuRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<MenuItem> menuItems = new ArrayList<>();
        menuAdapter = new MenuAdapter(menuItems, this);
        menuRecyclerView.setAdapter(menuAdapter);
    }

    private void loadMenuItems() {
        List<MenuItem> menuItems = new ArrayList<>();
        String cuisine = currentRestaurant.getCuisine().toLowerCase();

        if (cuisine.contains("asian") || cuisine.contains("chinese") || cuisine.contains("japanese")) {
            menuItems.add(new MenuItem(
                    "asian1",
                    "Signature Sushi Platter",
                    "Premium selection of fresh nigiri and maki rolls including salmon, tuna, and yellowtail",
                    28.99,
                    "https://images.pexels.com/photos/2098085/pexels-photo-2098085.jpeg"
            ));
            menuItems.add(new MenuItem(
                    "asian2",
                    "Dim Sum Basket",
                    "Steamed assortment of shrimp dumplings, pork siu mai, and BBQ pork buns",
                    18.99,
                    "https://images.pexels.com/photos/955137/pexels-photo-955137.jpeg"
            ));
            menuItems.add(new MenuItem(
                    "asian3",
                    "Tonkotsu Ramen",
                    "Rich pork bone broth with chashu pork, soft-boiled egg, bamboo shoots, and nori",
                    16.99,
                    "https://images.pexels.com/photos/884600/pexels-photo-884600.jpeg"
            ));
            menuItems.add(new MenuItem(
                    "asian4",
                    "Kung Pao Chicken",
                    "Wok-fried chicken with peanuts, dried chilies, and vegetables in spicy sauce",
                    17.99,
                    "https://images.pexels.com/photos/2347311/pexels-photo-2347311.jpeg"
            ));
        } else if (cuisine.contains("italian") || cuisine.contains("pizza")) {
            menuItems.add(new MenuItem(
                    "italian1",
                    "Wood-Fired Margherita Pizza",
                    "San Marzano tomatoes, fresh mozzarella, basil, and extra virgin olive oil",
                    19.99,
                    "https://images.pexels.com/photos/825661/pexels-photo-825661.jpeg"
            ));
            menuItems.add(new MenuItem(
                    "italian2",
                    "Homemade Lasagna",
                    "Layers of pasta, beef ragù, béchamel sauce, and Parmigiano-Reggiano",
                    21.99,
                    "https://images.pexels.com/photos/5949901/pexels-photo-5949901.jpeg"
            ));
            menuItems.add(new MenuItem(
                    "italian3",
                    "Seafood Linguine",
                    "Fresh pasta with shrimp, mussels, and calamari in white wine sauce",
                    24.99,
                    "https://images.pexels.com/photos/725997/pexels-photo-725997.jpeg"
            ));
            menuItems.add(new MenuItem(
                    "italian4",
                    "Tiramisu",
                    "Classic dessert with layers of coffee-soaked ladyfingers and mascarpone cream",
                    9.99,
                    "https://images.pexels.com/photos/6133305/pexels-photo-6133305.jpeg"
            ));
        } else if (cuisine.contains("american") || cuisine.contains("burger")) {
            menuItems.add(new MenuItem(
                    "american1",
                    "Signature Burger",
                    "Half-pound Angus beef patty, aged cheddar, bacon, lettuce, tomato, and special sauce",
                    16.99,
                    "https://images.pexels.com/photos/1639557/pexels-photo-1639557.jpeg"
            ));
            menuItems.add(new MenuItem(
                    "american2",
                    "BBQ Baby Back Ribs",
                    "Slow-cooked ribs with house-made BBQ sauce, coleslaw, and fries",
                    27.99,
                    "https://images.pexels.com/photos/410648/pexels-photo-410648.jpeg"
            ));
            menuItems.add(new MenuItem(
                    "american3",
                    "Buffalo Wings",
                    "Crispy wings tossed in buffalo sauce, served with celery and blue cheese dip (12 pieces)",
                    15.99,
                    "https://images.pexels.com/photos/2338407/pexels-photo-2338407.jpeg"
            ));
            menuItems.add(new MenuItem(
                    "american4",
                    "New York Cheesecake",
                    "Classic creamy cheesecake with graham cracker crust and berry compote",
                    8.99,
                    "https://images.pexels.com/photos/1126359/pexels-photo-1126359.jpeg"
            ));
        }

        menuAdapter.updateMenuItems(menuItems);
    }

    @Override
    public void onAddItem(MenuItem item) {
        cartItems.put(item.getId(), item);
        updateCartTotal();
    }

    @Override
    public void onRemoveItem(MenuItem item) {
        if (item.getQuantity() == 0) {
            cartItems.remove(item.getId());
        } else {
            cartItems.put(item.getId(), item);
        }
        updateCartTotal();
    }

    private void updateCartTotal() {
        double total = 0;
        int itemCount = 0;
        for (MenuItem item : cartItems.values()) {
            total += item.getPrice() * item.getQuantity();
            itemCount += item.getQuantity();
        }

        tvCartTotal.setText(String.format(Locale.US, "$%.2f (%d items)", total, itemCount));

        if (itemCount > 0) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            btnViewCart.setVisibility(View.VISIBLE);
        } else {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            btnViewCart.setVisibility(View.GONE);
        }
    }
}