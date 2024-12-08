package com.example.foody;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private RecyclerView rvRestaurants;
    private RestaurantAdapter restaurantAdapter;
    private final List<Restaurant> allRestaurants = new ArrayList<>();
    private EditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        initViews();
        setupSearchFunction();
        setupRestaurantsRecyclerView();
        setupBottomNavigation();
    }

    private void initViews() {
        rvRestaurants = findViewById(R.id.rvRestaurants);
        etSearch = findViewById(R.id.etSearch);
    }

    private void setupSearchFunction() {
        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch(etSearch.getText().toString());
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
                return true;
            }
            return false;
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                performSearch(s.toString());
            }
        });
    }

    private void performSearch(String query) {
        query = query.toLowerCase().trim();
        List<Restaurant> filteredList = new ArrayList<>();

        if (query.isEmpty()) {
            filteredList.addAll(allRestaurants);
        } else {
            for (Restaurant restaurant : allRestaurants) {
                if (restaurant.getName().toLowerCase().contains(query) ||
                        restaurant.getCuisine().toLowerCase().contains(query)) {
                    filteredList.add(restaurant);
                }
            }
        }

        restaurantAdapter.updateData(filteredList);
    }

    private void setupRestaurantsRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvRestaurants.setLayoutManager(layoutManager);

        allRestaurants.add(new Restaurant(
                "Asian Restaurant",
                "https://images.pexels.com/photos/1640777/pexels-photo-1640777.jpeg",
                4.8f,
                "Asian • Chinese • Japanese",
                "20-30 min",
                "$$"
        ));

        allRestaurants.add(new Restaurant(
                "Pizza Hub",
                "https://images.pexels.com/photos/1566837/pexels-photo-1566837.jpeg",
                4.5f,
                "Italian • Pizza • Pasta",
                "25-35 min",
                "$$"
        ));

        allRestaurants.add(new Restaurant(
                "Burger Queen",
                "https://images.pexels.com/photos/2271107/pexels-photo-2271107.jpeg",
                4.1f,
                "American • Burgers • Fast Food",
                "15-25 min",
                "$"
        ));

        restaurantAdapter = new RestaurantAdapter(this, new ArrayList<>(allRestaurants));
        rvRestaurants.setAdapter(restaurantAdapter);

        int spacing = (int) (16 * getResources().getDisplayMetrics().density);
        rvRestaurants.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                                       @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.bottom = spacing;
            }
        });
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.nav_home);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            startLoginActivity();
        }
    }

    private void startLoginActivity() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}