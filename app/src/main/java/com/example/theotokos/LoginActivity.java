package com.example.theotokos;

import android.os.Bundle;
import android.util.Log;
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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.gms.tasks.OnCompleteListener;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin, btnSignup;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
       String username = DataCache.getData(this, "username",null);
       String password = DataCache.getData(this, "password",null);
        auth = FirebaseAuth.getInstance();
        if(username != null && password != null) {
           Intent intent = new Intent(LoginActivity.this, MainActivity.class);
           startActivity(intent);
       }

    }

    @Override
    protected void onResume() {
        super.onResume();
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignup = findViewById(R.id.btnSignup);
        auth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Implement login logic here
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                auth.signInWithEmailAndPassword(username, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // User signed in successfully
                                FirebaseUser user = auth.getCurrentUser();
                                if (user != null) {
                                    // Check if user exists in your Firestore database
                                    checkUserExists(user.getUid());
                                } else {
                                    // Handle case where user is null
                                }
                                // ...
                            } else {
                                // Handle sign-in errors
                                Log.w("FirebaseAuth", "signInWithEmail:failure", task.getException());
                            }
                        });

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                // Validate credentials and proceed to next activity or display error
            }
        });
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    private void checkUserExists(String uid) {
        db.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        DataCache.saveData(this, "username",documentSnapshot.getString("username"));
                        DataCache.saveData(this, "password",documentSnapshot.getString("password"));
                        // User exists, proceed to next activity or show user data
                        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                        startActivity(intent);

                    } else {
                        Toast.makeText(this, "أسم المستخدم او كلمة المرور غير صحيحة", Toast.LENGTH_LONG);
                        // User does not exist, handle accordingly
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "خطأ !!! برجاء التسجيل من جديد", Toast.LENGTH_LONG);

                    // Handle errors
                });
    }
}
