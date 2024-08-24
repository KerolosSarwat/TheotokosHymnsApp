package com.example.theotokos;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.content.Intent;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin, btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       String username = DataCache.getData(this, "username",null);
       String password = DataCache.getData(this, "password",null);

       if(username != null && password != null) {
           Intent intent = new Intent(LoginActivity.this, MainActivity.class);
           startActivity(intent);
       }
        else {

           setContentView(R.layout.activity_login);
           etUsername = findViewById(R.id.etUsername);
           etPassword = findViewById(R.id.etPassword);
           btnLogin = findViewById(R.id.btnLogin);
           btnSignup = findViewById(R.id.btnSignup);

       }
    }

    @Override
    protected void onResume() {
        super.onResume();
        btnSignup.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);

        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Implement login logic here
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                // Validate credentials and proceed to next activity or display error
            }
        });
    }
}
