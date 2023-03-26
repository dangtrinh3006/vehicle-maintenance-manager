package tada.app.xetoi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import tada.app.xetoi.Model.History;
import tada.app.xetoi.Model.NotifyReceiver;
import tada.app.xetoi.Model.User;
import tada.app.xetoi.Model.Vehicle;

public class VehicleFormActivity extends AppCompatActivity {
    private static final String TAG = "VEHICLE FORM";
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference _myRef;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    LinearLayout pickerBaoDuong, pickerDangKiem, pickerBaoHiem;
    EditText edtName, edtVehicleNumber, edtBrandName;
    TextView edtBaoDuong, edtDangKiem, edtBaoHiem;
    RadioGroup grType;
    RadioButton rdCar, rdBike;
    Button btnSave, btnCancel;

    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_form);

        firebaseAuth = FirebaseAuth.getInstance();

        onStartUp();

        inIt();

        _myRef = mDatabase.getReference("User").child(firebaseUser.getUid()).child("ListVehicle");

        getData();

        pickerBaoDuong.setOnClickListener(v -> {
            Calendar calendar= Calendar.getInstance();
            int lastSelectedYear = calendar.get(Calendar.YEAR);
            int lastSelectedMonth = calendar.get(Calendar.MONTH);
            int lastSelectedDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog.OnDateSetListener dateSetListener = (view, year, monthOfYear, dayOfMonth) -> {
                String time = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                edtBaoDuong.setText(time);
            };

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    dateSetListener, lastSelectedYear, lastSelectedMonth, lastSelectedDayOfMonth);

            datePickerDialog.show();
        });

        pickerDangKiem.setOnClickListener(v -> {
            Calendar calendar= Calendar.getInstance();
            int lastSelectedYear = calendar.get(Calendar.YEAR);
            int lastSelectedMonth = calendar.get(Calendar.MONTH);
            int lastSelectedDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog.OnDateSetListener dateSetListener = (view, year, monthOfYear, dayOfMonth) -> {
                String time = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                edtDangKiem.setText(time);
            };

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    dateSetListener, lastSelectedYear, lastSelectedMonth, lastSelectedDayOfMonth);

            datePickerDialog.show();
        });

        pickerBaoHiem.setOnClickListener(v -> {
            Calendar calendar= Calendar.getInstance();
            int lastSelectedYear = calendar.get(Calendar.YEAR);
            int lastSelectedMonth = calendar.get(Calendar.MONTH);
            int lastSelectedDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog.OnDateSetListener dateSetListener = (view, year, monthOfYear, dayOfMonth) -> {
                String time = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                edtBaoHiem.setText(time);
            };

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    dateSetListener, lastSelectedYear, lastSelectedMonth, lastSelectedDayOfMonth);

            datePickerDialog.show();
        });

        btnSave.setOnClickListener(v -> {
            if(edtName.getText().toString().matches("") ||
                    edtVehicleNumber.getText().toString().matches("") ||
                    edtBrandName.getText().toString().matches("") ||
                    edtDangKiem.getText().toString().matches("") ||
                    edtBaoDuong.getText().toString().matches("") ||
                    edtBaoHiem.getText().toString().matches("")
            ) {
                new AlertDialog.Builder(this)
                        .setTitle("Vui lòng nhập đầy đủ thông tin")
                        .setMessage("Cannot save your note with empty title or content!")
                        .setNegativeButton("OK", null)
                        .show();
            }
            else {
                addVehicle();

                // Notification
                setNotificationBaoDuong();
                setNotificationDangKiem();
                setNotificationBaoHiem();
                finish();
            }
        });

        btnCancel.setOnClickListener(view -> {
            finish();
        });
    }

    public void inIt(){
        pickerBaoDuong = findViewById(R.id.pickerBaoDuong);
        pickerDangKiem = findViewById(R.id.pickerDangKiem);
        pickerBaoHiem = findViewById(R.id.pickerBaoHiem);
        edtName = findViewById(R.id.edtName);
        edtVehicleNumber = findViewById(R.id.edtVehicleNumber);
        edtBrandName = findViewById(R.id.edtBrandName);
        edtBaoDuong = findViewById(R.id.edtBaoDuong);
        edtDangKiem = findViewById(R.id.edtDangKiem);
        edtBaoHiem = findViewById(R.id.edtBaoHiem);
        grType = findViewById(R.id.grType);
        rdCar = findViewById(R.id.rdCar);
        rdBike = findViewById(R.id.rdBike);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
    }

    private void onStartUp() {
        firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser == null) {
            startActivity(new Intent(VehicleFormActivity.this, SplashScreenActivity.class));
            finish();
        }
    }

    private void getData() {
        Bundle bundle = getIntent().getExtras();
        if(bundle == null) {
            id = _myRef.push().getKey();
            return;
        }

        Vehicle vehicle = (Vehicle) bundle.get("object_vehicle");
        if(vehicle == null) return;

        id = vehicle.getId();

        edtName.setText(vehicle.getName());
        edtVehicleNumber.setText(vehicle.getNumber());
        edtBrandName.setText(vehicle.getBrand());
        edtBaoDuong.setText(vehicle.getBaoduong());
        edtDangKiem.setText(vehicle.getDangkiem());
        edtBaoHiem.setText(vehicle.getBaohiem());
        if(vehicle.getType().equalsIgnoreCase("Car")) {
            rdCar.setChecked(true);
        }
        else {
            rdBike.setChecked(true);
        }
    }

    private void addVehicle() {
        String vehicleName = edtName.getText().toString();
        String vehicleNumber = edtVehicleNumber.getText().toString();
        boolean isCar = rdCar.isChecked();
        String vehicleBrand = edtBrandName.getText().toString();
        String baoDuong = edtBaoDuong.getText().toString();
        String dangKiem = edtDangKiem.getText().toString();
        String baoHiem = edtBaoHiem.getText().toString();

        if(id != null) {
            Vehicle vehicle = new Vehicle(
                    id,
                    vehicleName,
                    vehicleNumber,
                    isCar? "Car" : "Bike",
                    vehicleBrand,
                    baoDuong,
                    dangKiem,
                    baoHiem
            );

            _myRef.child(id).setValue(vehicle).addOnSuccessListener(s -> {
                Log.d(TAG, "addVehicle: Add history success");
            }).addOnFailureListener(f -> {
                Log.d(TAG, "addVehicle: Add history fail with " + f.getMessage());
            });
        }
    }

    private void setNotificationBaoDuong() {
        long when = getNotificationTime(edtDangKiem.getText().toString());

        Log.d(TAG, "setNotificationDangKiem: Bao duong");

        AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotifyReceiver.class);
        intent.putExtra("bd", "bd");
        intent.putExtra("titleBaoDuong", "Sắp đến hạn bảo dưỡng rồi bạn ơi!!!");
        intent.putExtra("contentBaoDuong", "Ngày bảo dưỡng của xe "+ edtName.getText().toString() +" là: " + edtBaoDuong.getText().toString());

        PendingIntent pendingIntentBefore = PendingIntent.getBroadcast(this, new Random().nextInt(10000), intent, PendingIntent.FLAG_ONE_SHOT);
        //am.set(AlarmManager.RTC_WAKEUP, when - 259200000L, pendingIntentBefore); // Before 3 days
        PendingIntent pendingIntentAtTime = PendingIntent.getBroadcast(this, new Random().nextInt(10000), intent, PendingIntent.FLAG_ONE_SHOT);
        am.set(AlarmManager.RTC_WAKEUP, when, pendingIntentAtTime);
    }

    private void setNotificationDangKiem() {
        long when = getNotificationTime(edtDangKiem.getText().toString());

        Log.d(TAG, "setNotificationDangKiem: Dang kiem");

        AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotifyReceiver.class);
        intent.putExtra("dk", "dk");
        intent.putExtra("titleDangKiem", "Sắp đến hạn đăng kiểm rồi bạn ơi!!!");
        intent.putExtra("contentDangKiem", "Ngày đăng kiểm của xe "+ edtName.getText().toString() +" là: " + edtDangKiem.getText().toString());

        PendingIntent pendingIntentBefore = PendingIntent.getBroadcast(this, new Random().nextInt(10000), intent, PendingIntent.FLAG_ONE_SHOT);
        //am.set(AlarmManager.RTC_WAKEUP, when - 259200000L, pendingIntentBefore); // Before 3 days
        PendingIntent pendingIntentAtTime = PendingIntent.getBroadcast(this, new Random().nextInt(10000), intent, PendingIntent.FLAG_ONE_SHOT);
        am.set(AlarmManager.RTC_WAKEUP, when, pendingIntentAtTime);
    }

    private void setNotificationBaoHiem() {
        long when = getNotificationTime(edtDangKiem.getText().toString());

        Log.d(TAG, "setNotificationDangKiem: Bao hiem");

        AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotifyReceiver.class);
        intent.putExtra("bh", "bh");
        intent.putExtra("titleBaoHiem", "Sắp đến hạn bảo hiểm rồi bạn ơi!!!");
        intent.putExtra("contentBaoHiem", "Ngày bảo hiểm của xe "+ edtName.getText().toString() +" là: " + edtBaoHiem.getText().toString());

        PendingIntent pendingIntentBefore = PendingIntent.getBroadcast(this, new Random().nextInt(10000), intent, PendingIntent.FLAG_ONE_SHOT);
        //am.set(AlarmManager.RTC_WAKEUP, when - 259200000L, pendingIntentBefore); // Before 3 days
        PendingIntent pendingIntentAtTime = PendingIntent.getBroadcast(this, new Random().nextInt(10000), intent, PendingIntent.FLAG_ONE_SHOT);
        am.set(AlarmManager.RTC_WAKEUP, when, pendingIntentAtTime);
    }

    private long getNotificationTime(String time) {
        long milliseconds = System.currentTimeMillis() + 10000L; // 10 second after click
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        if(!time.matches("")) {
            try {
                Date d = dateFormat.parse(time);
                //milliseconds = d.getTime() + 420000L ; //At 7am of date
                Log.d(TAG, "setNotification: " + d.toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return milliseconds;
    }

}