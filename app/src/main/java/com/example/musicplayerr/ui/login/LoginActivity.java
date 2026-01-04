package com.example.musicplayerr.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.musicplayerr.MainActivity;
import com.example.musicplayerr.R;
import com.example.musicplayerr.ThemeUtils;

public class LoginActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // 1. Apply theme before creating the view
        ThemeUtils.applyTheme(this);
        super.onCreate(savedInstanceState);

        // 2. Set the layout
        setContentView(R.layout.activity_login);

        // 3. Find the UI elements
        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);

        // 4. The Click Listener - THIS IS WHAT WAS MISSING
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = usernameEditText.getText().toString();
                String pass = passwordEditText.getText().toString();

                // Simple check: If not empty, go to next screen
                if (!user.isEmpty() && !pass.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();

                    // THIS COMMAND OPENS THE MAIN PLAYER
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);

                    // Close login so we can't go back to it
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Please enter details", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}