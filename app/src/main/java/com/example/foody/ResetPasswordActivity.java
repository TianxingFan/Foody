package com.example.foody;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

// Activity for password reset functionality
public class ResetPasswordActivity extends AppCompatActivity {
    // UI elements
    private TextInputEditText etEmail;
    private MaterialButton btnResetPassword;
    private TextView tvBackToLogin;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();
        initViews();
        setupClickListeners();
    }

    // Initialize UI elements
    private void initViews() {
        etEmail = findViewById(R.id.etEmail);
        btnResetPassword = findViewById(R.id.btnResetPassword);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);
    }

    // Set up button click listeners
    private void setupClickListeners() {
        btnResetPassword.setOnClickListener(v -> attemptResetPassword());
        tvBackToLogin.setOnClickListener(v -> finish());
    }

    // Handle password reset process
    private void attemptResetPassword() {
        String email = Objects.requireNonNull(etEmail.getText()).toString().trim();

        if (!validateEmail(email)) {
            return;
        }

        btnResetPassword.setEnabled(false);

        // Send password reset email using Firebase
        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        showSuccessMessage();
                    } else {
                        handleResetError(task.getException());
                    }
                });
    }

    // Check if email is valid
    private boolean validateEmail(String email) {
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Required");
            return false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Enter a valid email address");
            return false;
        }
        etEmail.setError(null);
        return true;
    }

    // Show success message and close activity
    private void showSuccessMessage() {
        Toast.makeText(this,
                "Password reset email sent. Please check your inbox.",
                Toast.LENGTH_LONG).show();
        finish();
    }

    // Handle errors during password reset
    private void handleResetError(Exception exception) {
        btnResetPassword.setEnabled(true);

        String errorMessage = "Failed to send reset email";
        if (exception != null && exception.getMessage() != null) {
            String message = exception.getMessage();
            if (message.contains("no user record")) {
                errorMessage = "No account found with this email";
            } else if (message.contains("network error")) {
                errorMessage = "Network error. Please check your connection";
            }
        }
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }
}