package com.example.musicplayerr; // Adjust this if your folder is different

import android.content.Intent;
import android.os.Bundle;
import android.view.View; // Added missing import
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

// These imports connect your other files
import com.example.musicplayerr.R;
import com.example.musicplayerr.MainActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. Point to your activity_login.xml
        setContentView(R.layout.activity_login);

        // 2. Find the views by the IDs we set in the XML
        // We use "login" because that is what we set in the XML android:id
        Button loginBtn = findViewById(R.id.login);
        EditText username = findViewById(R.id.username);
        EditText password = findViewById(R.id.password);

        // 3. Set the click listener
        if (loginBtn != null) {
            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Go to MainActivity
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }
}