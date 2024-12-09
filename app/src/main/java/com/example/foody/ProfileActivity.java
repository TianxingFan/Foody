package com.example.foody;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

// Activity for displaying user profile information and handling logout
public class ProfileActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();

        TextView tvEmail = findViewById(R.id.tvEmail);
        Button btnLogout = findViewById(R.id.btnLogout);

        // Display user email if user is logged in
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            tvEmail.setText(currentUser.getEmail());
        }

        btnLogout.setOnClickListener(v -> {
            firebaseAuth.signOut();

            // Navigate to login screen and clear activity stack
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}