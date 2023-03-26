package tada.app.xetoi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import tada.app.xetoi.Model.User;
import tada.app.xetoi.Model.Vehicle;

public class UserDetailActivity extends AppCompatActivity {
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference _myRef;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    TextView edtBirthday;
    EditText edtName,edtPhone,edtCountry;
    RadioGroup rdGender;
    RadioButton rdMale,rdFemale;
    Button btnSave;
    LinearLayout pickerBirthday;

    boolean isEditUser;
    private static final String TAG = "UserDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        firebaseAuth = FirebaseAuth.getInstance();

        inIt();
        getData();

        btnSave.setOnClickListener(v -> {
            saveUserData();
        });

        pickerBirthday.setOnClickListener(v -> {
            Calendar calendar= Calendar.getInstance();
            int lastSelectedYear = calendar.get(Calendar.YEAR);
            int lastSelectedMonth = calendar.get(Calendar.MONTH);
            int lastSelectedDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog.OnDateSetListener dateSetListener = (view, year, monthOfYear, dayOfMonth) -> {
                String time = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                edtBirthday.setText(time);
            };

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    dateSetListener, lastSelectedYear, lastSelectedMonth, lastSelectedDayOfMonth);

            datePickerDialog.show();
        });
    }

    public void getData(){
        Log.d(TAG, "getData: Begin get data");
        firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser == null) {
            startActivity(new Intent(UserDetailActivity.this, SplashScreenActivity.class));
            finish();
        }

        _myRef = mDatabase.getReference("User");

        _myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "onDataChange: " + firebaseUser.getUid());
                User user;

                if (snapshot.child(firebaseUser.getUid()).exists()){
                    user = snapshot.child(firebaseUser.getUid()).getValue(User.class);
                    Log.d(TAG, "onDataChange: " + user.getFullName());
                    edtName.setText(user.getFullName());
                    edtPhone.setText(user.getPhone());
                    edtBirthday.setText(user.getBirthDay());
                    edtCountry.setText(user.getCountry());

                    if (user.isGender()){
                        rdMale.setChecked(true);
                    } else {
                        rdFemale.setChecked(true);
                    }
                    isEditUser = true;
                }
                else {
                    isEditUser = false;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void inIt(){
        edtName = findViewById(R.id.edtName);
        edtPhone = findViewById(R.id.edtPhone);
        edtBirthday = findViewById(R.id.edtBirthday);
        edtCountry = findViewById(R.id.edtCountry);
        rdGender = findViewById(R.id.grGender);
        rdMale = findViewById(R.id.rdMale);
        rdFemale = findViewById(R.id.rdFemale);
        btnSave = findViewById(R.id.btnSave);
        pickerBirthday = findViewById(R.id.pickerBirthday);
    }

    public void saveUserData(){
        User user = new User(
                firebaseUser.getUid(),
                edtName.getText().toString(),
                edtPhone.getText().toString(),
                rdMale.isChecked(),
                edtBirthday.getText().toString(),
                edtCountry.getText().toString()
        );

        _myRef = mDatabase.getReference("User");
        _myRef.child(user.getId()).setValue(user)
                .addOnSuccessListener(unused -> {
                    Log.d(TAG, "saveUserData: Save succeeded user: " + user.getFullName());
                })
                .addOnFailureListener(e -> {
                    Log.d(TAG, "addUser: Save failed: " + e.getMessage());
                });

        if(!isEditUser) {
            Toast.makeText(this, "Chào mừng bạn đến với ứng dụng Xe Tôi", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(UserDetailActivity.this, MainActivity.class));
            finish();
        }
        else {
            Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}