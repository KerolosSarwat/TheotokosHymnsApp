package com.example.theotokos;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.app.DatePickerDialog;
import java.util.Calendar;
import java.util.Objects;
import android.widget.Toast;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    private EditText etFullName, etPhone, etAddress, etChurch;
    private RadioGroup rgGender;
    private AutoCompleteTextView spStudentLevel;
    private Button btnSignup, btnPickDate;
    String genderValue;
    String[] studentLevels = {"حضانة", "أولى ابتدائى","ثانية ابتدائى", "ثالثة ابتدائى", "رابعة ابتدائى", "خامسة ابتدائى", "سادسة ابتدائى", "اعدادى", "ثانوى", "جامعيين و خريجين"};
    DataCache dataCache;
    String userID;
    DatePickerDialog.OnDateSetListener dateSetListener1;
    String selectedLevel = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Objects.requireNonNull(getSupportActionBar()).hide();
        EdgeToEdge.enable(this);

        btnPickDate = findViewById(R.id.btnPickDate);
        etFullName = findViewById(R.id.etFullName);

        etPhone = findViewById(R.id.etPhone);
        etAddress = findViewById(R.id.etAddress);
        etChurch = findViewById(R.id.etChurch);
        rgGender = findViewById(R.id.rgGender);
        spStudentLevel = findViewById(R.id.spStudentLevel);
        btnSignup = findViewById(R.id.btnSignup);
        FirebaseApp.initializeApp(this);

        dataCache = DataCache.getInstance(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item , studentLevels);
        spStudentLevel.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();

        userID = getIntent().getStringExtra("userID");
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("users");
        //fetchData(database);
        btnPickDate.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, dateSetListener1, year, month, day);
            datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//
            datePickerDialog.show();
        });

        dateSetListener1 = (view, year1, month1, day1) -> {
            // 1 number less for every month
            // example- for january month=0
            month1 = month1 + 1;
            String date = day1 + "-" + month1 + "-" + year1;
            btnPickDate.setText(date);
        };

        spStudentLevel.setOnItemClickListener((adapterView, view1, i, l) -> {
            Log.e( "onItemSelected 2: ", adapterView.getItemAtPosition(i).toString());
            Toast.makeText(SignupActivity.this, adapterView.getItemAtPosition(i).toString(), Toast.LENGTH_LONG).show();
            selectedLevel = adapterView.getItemAtPosition(i).toString();
        });

        btnSignup.setOnClickListener(view -> {

            genderValue = "";

            if (rgGender.getCheckedRadioButtonId() == R.id.rbMale)
                genderValue = "Male";

            else if (rgGender.getCheckedRadioButtonId() == R.id.rbFemale)
                genderValue = "Female";



            boolean isReady = true;

            if (etFullName.getText().toString().isEmpty()){
                Toast.makeText(this, "برجاء كتابة الأسم بشكل صحيح", Toast.LENGTH_LONG).show();
                isReady = false;
            }

            if(etPhone.getText().length() != 11){
                Toast.makeText(this, "برجاء كتابة رقم الهاتف بشكل صحيح", Toast.LENGTH_LONG).show();
                isReady = false;
            }

            if(btnPickDate.getText().equals("تاريخ الميلاد") || btnPickDate.getText().toString().isEmpty()){
                Toast.makeText(this, "برجاء اختر تاريخ الميلاد", Toast.LENGTH_LONG).show();
                isReady = false;
            }
            if (etAddress.getText().toString().isEmpty()){
                Toast.makeText(this, "برجاء كتابة العنوان", Toast.LENGTH_LONG).show();
                isReady = false;
            } if (etChurch.getText().toString().isEmpty()){
                Toast.makeText(this, "برجاء كتابة اسم الكنيسة", Toast.LENGTH_LONG).show();
                isReady = false;
            }
            if (genderValue.isEmpty()){
                Toast.makeText(this, "برجاء اختيار النوع", Toast.LENGTH_LONG).show();
                isReady = false;
            }
            if (selectedLevel.isEmpty())
            {
                Toast.makeText(this, "برجاء اختيار المرحلة الدراسية", Toast.LENGTH_LONG).show();
                isReady = false;
            }

            if(isReady) {
                try {
                User user = null;
                    Term term = new Term();
                    Degree degrees = new Degree(term, term, term);
                    user = new User(etFullName.getText().toString(), etPhone.getText().toString(), btnPickDate.getText().toString(), genderValue, etAddress.getText().toString(), etChurch.getText().toString(), selectedLevel, degrees);
                    submitUserData(user, database);
                } catch (Exception ex) {
                    Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

/*    private void fetchData(DatabaseReference database){
        database.child(userID).get()
                .addOnSuccessListener(dataSnapshot -> {
                        User user = dataSnapshot.getValue(User.class);
                        etFullName.setText(user.getFullName());
                        etPhone.setText(user.getPhoneNumber());
                        etAddress.setText(user.getAddress());
                        etChurch.setText(user.getChurch());
                        spStudentLevel.setSelection(Arrays.asList(studentLevels).indexOf(user.getLevel()));
                        btnPickDate.setText(user.getBirthdate());
                        RadioButton rb;
                        if(user.getGender().equals("Male")) {
                            rb = findViewById(R.id.rbMale);
                            rb.setChecked(true);
                        }else {
                            rb = findViewById(R.id.rbFemale);
                            rb.setChecked(true);
                        }

                });
    }
*/


    private void submitUserData(User user,  DatabaseReference database) {
        user.setCode(userID);
        database.child(userID).setValue(user)
                .addOnCompleteListener(task -> {
                        dataCache = new DataCache(SignupActivity.this);
                        dataCache.saveUser(user);
                        // Handle successful submission (e.g., clear fields, navigate to another screen)
                         Toast.makeText(SignupActivity.this, "تم التسجيل بنجاح", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                        startActivity(intent);
                }).addOnFailureListener(task -> Toast.makeText(SignupActivity.this, "خطأ فى تسجيل البيانات", Toast.LENGTH_LONG).show());
    }
}