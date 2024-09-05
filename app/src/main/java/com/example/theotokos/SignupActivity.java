package com.example.theotokos;
import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.app.DatePickerDialog;
import android.widget.TextView;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicBoolean;

import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

        btnPickDate.setOnClickListener(view -> showDatePickerDialog());

        btnSignup.setOnClickListener(view -> {

            genderValue = "";
            if (rgGender.getCheckedRadioButtonId() == R.id.rbMale) {
                genderValue = "Male";
            } else if (rgGender.getCheckedRadioButtonId() == R.id.rbFemale) {
                genderValue = "Female";
            }

            Log.d("onResume: ", genderValue);

            if (spStudentLevel.getSelectedItem().toString().equals("أختر المرحلة")){
                Toast.makeText(this, "برجاء أختيار المرحلة", Toast.LENGTH_LONG).show();
            }
            else if (tvBirthdate.getText() == null ||tvBirthdate.getText().equals("")){
                Toast.makeText(this, "برجاء اختيار تاريخ الميلاد", Toast.LENGTH_LONG).show();
            }
            else {
                User user = new User(etFullName.getText().toString(), etPhone.getText().toString(), tvBirthdate.getText().toString(), genderValue, etAddress.getText().toString(), etChurch.getText().toString(), etUsername.getText().toString(), etPassword.getText().toString(), spStudentLevel.getSelectedItem().toString());
                DatabaseReference database = FirebaseDatabase.getInstance().getReference("users");

                try {
                    submitUserData(user, database);
                } catch (Exception ex) {
                    Log.e(TAG, "onResume: Submit Field Kiro");
                }
            }
        });

    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, Year, monthOfYear, dayOfMonth) -> {
            // Handle the selected date
            String selectedDate = Year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
            tvBirthdate.setText(selectedDate);
        },
                year, month, day);
        datePickerDialog.show();
    }


    private boolean isUserExists(User user, DatabaseReference database){
        String userId = database.push().getKey();
        //boolean flag;
        database.child(userId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            User usr = dataSnapshot.getValue(User.class);
                            if(usr != null && usr.getUsername().equals(user.getUsername())) {
                                Toast.makeText(SignupActivity.this, "المستخدم موجود بالفعل", Toast.LENGTH_SHORT).show();
                                //flag = false;
                                // Handle the fetched user data
                                Log.e("RealtimeDatabase", "User data: " + usr.toString());

                                //return flag;
                            }
                            else {
                                // Handle case where user does not exist
                                // set user data into database

                            }
                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle errors
                        Log.w("RealtimeDatabase", "loadPost:onCancelled", databaseError.toException());
                    }
                });
        return false;
    }

    private void submitUserData(User user,  DatabaseReference database) {
        ///Return True if user submitted successfully
        ///Return False if user already exists
        String userId = database.push().getKey();

        database.child(userId).setValue(user)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                    {
                        Log.e( "onDataChange: ","Success" );
                        // Handle successful submission (e.g., clear fields, navigate to another screen)
                        Toast.makeText(SignupActivity.this, "User data submitted successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                        startActivity(intent);

                    } else {
                        Log.e( "onDataChange: ","Error" );

                        Toast.makeText(SignupActivity.this, "خطأ فى تسجيل البيانات", Toast.LENGTH_SHORT).show();
                        // Handle error (e.g., display error message)
                    }
                });
    }
}