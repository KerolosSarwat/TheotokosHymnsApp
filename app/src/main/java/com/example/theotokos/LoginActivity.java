package com.example.theotokos;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.content.Intent;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private DataCache dataCache;
    private static final int REQUEST_STORAGE_PERMISSION = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        try {
            Objects.requireNonNull(getSupportActionBar()).hide();
            getSupportActionBar().hide();
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION);
            dataCache = DataCache.getInstance(this);
            User user = dataCache.getUser();
            if (user != null){
                navigateToMainActivity();
        }
    }catch (Exception ex){
        dataCache = DataCache.getInstance(this);
        dataCache.clearCache();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        dataCache = new DataCache(this);

        btnLogin.setOnClickListener(view -> {
            // Implement login logic here
            String username = etUsername.getText().toString();
            String password = etPassword.getText().toString();
            try {
                if (username.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "برجاء ادخال اسم المستخدم أو كلمة المرور", Toast.LENGTH_LONG).show();
                } else {
                    if (NetworkUtils.isNetworkConnected(LoginActivity.this)) {
                        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
                        try {
                            usersRef.child(username).get().addOnSuccessListener(dataSnapshot -> {
                                User user = dataSnapshot.getValue(User.class);
                                if (user != null) {
                                    String code = usersRef.child(username).getKey();
                                    user.setCode(code);
                                    //Log.e( "onResume: ", user.getPhoneNumber());
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        if (password.isEmpty()) {
                                            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                                            intent.putExtra("userID", username);
                                            startActivity(intent);
                                        } else if (!user.getPassword().isEmpty() && PasswordHasher.validatePassword(user.getPassword(), password)) {
                                            // User found, handle login
                                            dataCache.saveUser(user);
                                            navigateToMainActivity();
                                        } else {
                                            // Incorrect password
                                            Toast.makeText(LoginActivity.this, "كلمة المرور خطأ", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                } else {
                                    Toast.makeText(LoginActivity.this, "المستخدم غير موجود", Toast.LENGTH_LONG).show();

                                }
                            }).addOnFailureListener(e -> {
                                // User not found
                                Toast.makeText(LoginActivity.this, "المستخدم غير موجود", Toast.LENGTH_LONG).show();

                            }).addOnCanceledListener(() -> Toast.makeText(LoginActivity.this, "برجاء المحاولة فى وقت لاحق", Toast.LENGTH_LONG).show());
                        } catch (NullPointerException ex) {
                            Toast.makeText(LoginActivity.this, "اسم المستخدم غير موجود", Toast.LENGTH_LONG).show();
                        } catch (Exception ex) {
                            Toast.makeText(LoginActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Toast.makeText(LoginActivity.this, "برجاء الاتصال بالانترنت", Toast.LENGTH_LONG).show();
                    }
                }
            }catch (Exception ex){
                Log.e("onResume: ", ex.getMessage() );
            }
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
