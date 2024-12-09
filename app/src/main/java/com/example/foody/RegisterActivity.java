package com.example.foody;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

// Activity for user registration
public class RegisterActivity extends AppCompatActivity {
    // UI elements
    private TextInputEditText etEmail, etPassword, etConfirmPassword;
    private MaterialButton btnRegister;
    private TextView tvBackToLogin;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();
        initViews();
        setupClickListeners();
    }

    // Initialize all UI elements
    private void initViews() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);
    }

    // Set up click listeners for buttons
    private void setupClickListeners() {
        btnRegister.setOnClickListener(v -> attemptRegister());
        tvBackToLogin.setOnClickListener(v -> finish());
    }

    // Handle the registration process
    private void attemptRegister() {
        String email = Objects.requireNonNull(etEmail.getText()).toString().trim();
        String password = Objects.requireNonNull(etPassword.getText()).toString().trim();
        String confirmPassword = Objects.requireNonNull(etConfirmPassword.getText()).toString().trim();

        if (!validateInput(email, password, confirmPassword)) {
            return;
        }

        btnRegister.setEnabled(false);

        // Create new user with Firebase
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        startMainActivity();
                    } else {
                        handleRegistrationError(task.getException());
                    }
                });
    }

    // Validate user input for registration
    private boolean validateInput(String email, String password, String confirmPassword) {
        boolean valid = true;

        // Check email
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Required");
            valid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Enter a valid email address");
            valid = false;
        } else {
            etEmail.setError(null);
        }

        // Check password
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Required");
            valid = false;
        } else if (password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            valid = false;
        } else {
            etPassword.setError(null);
        }

        // Check password confirmation
        if (TextUtils.isEmpty(confirmPassword)) {
            etConfirmPassword.setError("Required");
            valid = false;
        } else if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            valid = false;
        } else {
            etConfirmPassword.setError(null);
        }

        return valid;
    }

    // Handle registration errors and show appropriate messages
    private void handleRegistrationError(Exception exception) {
        btnRegister.setEnabled(true);
        String errorMessage = "Registration failed";
        if (exception != null) {
            String message = exception.getMessage();
            if (message != null) {
                if (message.contains("email address is already in use")) {
                    errorMessage = "Email already registered";
                } else if (message.contains("network error")) {
                    errorMessage = "Network error. Please check your connection";
                }
            }
        }
        Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    // Start main activity after successful registration
    private void startMainActivity() {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}