package com.example.foody;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

// Login screen activity that handles user authentication
public class LoginActivity extends AppCompatActivity {
    // UI elements
    private TextInputEditText etEmail, etPassword;
    private MaterialButton btnLogin, btnRegister;
    private TextView tvForgotPassword;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Get Firebase authentication instance
        firebaseAuth = FirebaseAuth.getInstance();

        initViews();
        setupClickListeners();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is already logged in
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            startMainActivity();
        }
    }

    // Initialize all UI elements
    private void initViews() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
    }

    // Set up click listeners for buttons
    private void setupClickListeners() {
        btnLogin.setOnClickListener(v -> attemptLogin());
        btnRegister.setOnClickListener(v -> handleRegister());
        tvForgotPassword.setOnClickListener(v -> handleForgotPassword());
    }

    // Handle login process
    private void attemptLogin() {
        String email = Objects.requireNonNull(etEmail.getText()).toString().trim();
        String password = Objects.requireNonNull(etPassword.getText()).toString().trim();

        if (!validateInput(email, password)) {
            return;
        }

        // Try to sign in with Firebase
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        startMainActivity();
                    } else {
                        handleLoginError(task.getException());
                    }
                });
    }

    // Handle different types of login errors
    private void handleLoginError(Exception exception) {
        String errorMessage = "Authentication failed";
        if (exception != null) {
            String message = exception.getMessage();
            if (message != null) {
                if (message.contains("password is invalid")) {
                    errorMessage = "Incorrect password";
                } else if (message.contains("no user record")) {
                    errorMessage = "No account found with this email";
                } else if (message.contains("network error")) {
                    errorMessage = "Network error. Please check your connection";
                }
            }
        }
        Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    // Go to registration screen
    private void handleRegister() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    // Go to password reset screen
    private void handleForgotPassword() {
        Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
        startActivity(intent);
    }

    // Check if email and password are valid
    private boolean validateInput(String email, String password) {
        boolean valid = true;

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Required");
            valid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Please enter a valid email address");
            valid = false;
        } else {
            etEmail.setError(null);
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Required");
            valid = false;
        } else if (password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            valid = false;
        } else {
            etPassword.setError(null);
        }

        return valid;
    }

    // Go to main screen and clear previous screens
    private void startMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}