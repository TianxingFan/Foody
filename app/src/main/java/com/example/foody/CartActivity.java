package com.example.foody;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnCartItemClickListener {
    private RecyclerView cartRecyclerView;
    private CartAdapter cartAdapter;
    private TextView tvSubtotal;
    private TextView tvDeliveryFee;
    private TextView tvTotal;
    private Button btnCheckout;
    private List<MenuItem> cartItems;
    private static final double DELIVERY_FEE = 2.99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        @SuppressWarnings("unchecked")
        ArrayList<MenuItem> items = (ArrayList<MenuItem>) getIntent().getSerializableExtra("cartItems");
        cartItems = items;

        setupViews();
        setupRecyclerView();
        updateTotals();
    }

    private void setupViews() {
        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvDeliveryFee = findViewById(R.id.tvDeliveryFee);
        tvTotal = findViewById(R.id.tvTotal);
        btnCheckout = findViewById(R.id.btnCheckout);

        btnCheckout.setOnClickListener(v -> processCheckout());
    }

    private void setupRecyclerView() {
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartAdapter = new CartAdapter(cartItems, this);
        cartRecyclerView.setAdapter(cartAdapter);
    }

    private void updateTotals() {
        double subtotal = 0;
        for (MenuItem item : cartItems) {
            subtotal += item.getPrice() * item.getQuantity();
        }

        double total = subtotal + DELIVERY_FEE;

        tvSubtotal.setText(String.format(Locale.US, "$%.2f", subtotal));
        tvDeliveryFee.setText(String.format(Locale.US, "$%.2f", DELIVERY_FEE));
        tvTotal.setText(String.format(Locale.US, "$%.2f", total));

        btnCheckout.setEnabled(subtotal > 0);
    }

    @Override
    public void onUpdateQuantity(MenuItem item) {
        updateTotals();
        if (item.getQuantity() == 0) {
            int position = cartItems.indexOf(item);
            cartItems.remove(item);
            cartAdapter.notifyItemRemoved(position);
        }
    }

    private void processCheckout() {
        if (cartItems == null || cartItems.isEmpty()) {
            Toast.makeText(this, "Cart is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        String orderId = generateOrderId();

        double subtotal = 0;
        for (MenuItem item : cartItems) {
            subtotal += item.getPrice() * item.getQuantity();
        }
        double total = subtotal + DELIVERY_FEE;

        StringBuilder orderDetails = new StringBuilder();
        orderDetails.append("Order#: ").append(orderId).append("\n\n");
        orderDetails.append("Order Summary:\n");

        for (MenuItem item : cartItems) {
            orderDetails.append(item.getName())
                    .append(" x").append(item.getQuantity())
                    .append("  $").append(String.format(Locale.US, "%.2f", item.getPrice() * item.getQuantity()))
                    .append("\n");
        }

        orderDetails.append("\nSubtotal: $").append(String.format(Locale.US, "%.2f", subtotal))
                .append("\nDelivery Fee: $").append(String.format(Locale.US, "%.2f", DELIVERY_FEE))
                .append("\nTotal: $").append(String.format(Locale.US, "%.2f", total));

        new MaterialAlertDialogBuilder(this)
                .setTitle("Order Successful!")
                .setMessage(orderDetails.toString())
                .setPositiveButton("Confirm", (dialog, which) -> {
                    setResult(RESULT_OK);
                    finish();
                })
                .setCancelable(false)
                .show();
    }

    private String generateOrderId() {
        return String.valueOf(System.currentTimeMillis()).substring(5);
    }
}