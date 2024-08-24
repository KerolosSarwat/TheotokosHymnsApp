package com.example.theotokos;
import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.app.DatePickerDialog;
import android.widget.TextView;
import java.util.Calendar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    private EditText etFullName, etPhone, etAddress, etChurch, etUsername, etPassword;
    ;
    private RadioGroup rgGender;
    private Spinner spStudentLevel;
    private Button btnSignup, btnPickDate;
    private TextView tvBirthdate;
    String genderValue;
    private final String[] studentLevels = {"أختر المرحلة","حضانة", "أولى ابتدائى","ثانية ابتدائى", "ثالثة ابتدائى", "رابعة ابتدائى", "خامسة ابتدائى", "سادسة ابتدائى", "اعدادى", "ثانوى", "جامعيين و خريجين"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        btnPickDate = findViewById(R.id.btnPickDate);
        etFullName = findViewById(R.id.etFullName);
        tvBirthdate = findViewById(R.id.tvBirthdate);
        etPhone = findViewById(R.id.etPhone);
        etAddress = findViewById(R.id.etAddress);
        etChurch = findViewById(R.id.etChurch);
        rgGender = findViewById(R.id.rgGender);
        spStudentLevel = findViewById(R.id.spStudentLevel);
        btnSignup = findViewById(R.id.btnSignup);
        tvBirthdate = findViewById(R.id.tvBirthdate);
        btnPickDate = findViewById(R.id.btnPickDate);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        FirebaseApp.initializeApp(this);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, studentLevels);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spStudentLevel.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();

        btnPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View
                                        view) {
                showDatePickerDialog();
            }
        });

        btnSignup.setOnClickListener(view -> {

            genderValue = "";
            if (rgGender.getCheckedRadioButtonId() == R.id.rbMale) {
                genderValue = "Male";
            } else if (rgGender.getCheckedRadioButtonId() == R.id.rbFemale) {
                genderValue = "Female";
            }

            Log.d(TAG, "onResume: "+ genderValue);

            User user = new User(etFullName.getText().toString(), etPhone.getText().toString(), tvBirthdate.getText().toString(), genderValue, etAddress.getText().toString(), etChurch.getText().toString(), etUsername.getText().toString(), etPassword.getText().toString(), spStudentLevel.getSelectedItem().toString());
            try {
                submitUserData();
                DataCache.saveData(this, "username", user.username);
                DataCache.saveData(this, "password", user.password);


                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                startActivity(intent);
            }catch (Exception ex){
                Log.e(TAG, "onResume: Submit Field Kiro" );
            }
        });
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);

        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this, (view, Year, monthOfYear, dayOfMonth) -> {
            // Handle the selected date
            String selectedDate = Year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
            tvBirthdate.setText(selectedDate);
        },
                year, month, day);
        datePickerDialog.show();
    }

    private void submitUserData() {

        User user = new User(etFullName.getText().toString(), etPhone.getText().toString(), tvBirthdate.getText().toString(), genderValue, etAddress.getText().toString(), etChurch.getText().toString(), etUsername.getText().toString(), etPassword.getText().toString(), spStudentLevel.getSelectedItem().toString());

        DatabaseReference database = FirebaseDatabase.getInstance().getReference("users");
        String userId = database.push().getKey();
        database.child(userId).setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(SignupActivity.this, "User data submitted successfully", Toast.LENGTH_SHORT).show();
                            // Handle successful submission (e.g., clear fields, navigate to another screen)
                        } else {
                            Toast.makeText(SignupActivity.this, "Error submitting user data", Toast.LENGTH_SHORT).show();
                            // Handle error (e.g., display error message)
                        }
                    }
                });
    }
}