package com.example.theotokos;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.content.Intent;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin, btnSignup;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private DataCache dataCache;
    private static final int REQUEST_STORAGE_PERMISSION = 100;
    private static final int STORAGE_PERMISSION_CODE = 23;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION);
        }
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        dataCache = DataCache.getInstance(this);
        User user = dataCache.getUser();
        if (user != null){
            navigateToMainActivity();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignup = findViewById(R.id.btnSignup);

        dataCache = new DataCache(this);

        btnLogin.setOnClickListener(view -> {
            // Implement login logic here
            String username = etUsername.getText().toString();
            String password = etPassword.getText().toString();

            if (username.isEmpty() || password.isEmpty()){
                Toast.makeText(LoginActivity.this, "برجاء ادخال اسم المستخدم أو كلمة المرور",Toast.LENGTH_LONG).show();
            }
            else {
                if (NetworkUtils.isNetworkConnected(LoginActivity.this)) {

                    DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

                    usersRef.orderByChild("username")
                            .equalTo(username)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull
                                        DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren())
                                        {
                                            User user = childSnapshot.getValue(User.class);
                                            if (user.getPassword().equals(password))
                                            {
                                                // User found, handle login
                                                dataCache.saveUser(user);
                                                navigateToMainActivity();
                                            } else {
                                            // Incorrect password
                                                Toast.makeText(LoginActivity.this, "كلمة المرور خطأ", Toast.LENGTH_LONG).show();
                                        }
                                        }
                                    } else {
                                        // User not found
                                        Toast.makeText(LoginActivity.this, "المستخدم غير موجود", Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }

                            });
                }
                else {
                    Toast.makeText(LoginActivity.this, "برجاء الاتصال بالانترنت", Toast.LENGTH_LONG).show();
                }
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
                        //DataCache.saveData(this, "username",documentSnapshot.getString("username"));
                        //DataCache.saveData(this, "password",documentSnapshot.getString("password"));
                        // User exists, proceed to next activity or show user data
                        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                        startActivity(intent);

                    } else {
                        Toast.makeText(this, "أسم المستخدم او كلمة المرور غير صحيحة", Toast.LENGTH_LONG).show();
                        // User does not exist, handle accordingly
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "خطأ !!! برجاء التسجيل من جديد", Toast.LENGTH_LONG).show();

                    // Handle errors
                });
    }

    private void fetchUserData(String uid) {
        db.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class);

                        dataCache.saveUser(user);
                        navigateToMainActivity();
                    } else {
                        // Handle case where user does not exist
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle errors
                });
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with your code
            } else {
                // Permission denied, handle accordingly
            }
        }
    }
}
