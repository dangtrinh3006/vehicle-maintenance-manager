package tada.app.xetoi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

import tada.app.xetoi.DAO.HistoryDAO;
import tada.app.xetoi.Model.History;
import tada.app.xetoi.Model.Vehicle;
import tada.app.xetoi.databinding.ActivityRefuelBinding;

public class RefuelActivity extends AppCompatActivity {
    private static final String TAG = "REFUEL ACTIVITY";
    private ActivityRefuelBinding binding;

    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference _myRef;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    ArrayList<Vehicle> listVehicle;
    ArrayList<String> listVehicleName = new ArrayList<>();
    ArrayList<String> lstvehicleId = new ArrayList<>();
    ArrayAdapter<String> adp;
    HistoryDAO historyDAO;
    Vehicle vehicleUpdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRefuelBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();

        onStartUp();

        getData();

        adp = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, listVehicleName);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = findViewById(R.id.spinerRefuelVehicle);
        spinner.setAdapter(adp);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                vehicleUpdate = listVehicle.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // Date picker
        binding.btnRefuelDate.setOnClickListener(v -> {
            Calendar calendar= Calendar.getInstance();
            int lastSelectedYear = calendar.get(Calendar.YEAR);
            int lastSelectedMonth = calendar.get(Calendar.MONTH);
            int lastSelectedDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog.OnDateSetListener dateSetListener = (view, year, monthOfYear, dayOfMonth)
                    -> binding.etRefuelDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    dateSetListener, lastSelectedYear, lastSelectedMonth, lastSelectedDayOfMonth);

            datePickerDialog.show();
        });

        // Back button click
        binding.ivFuelFinishButton.setOnClickListener(v -> {
            finish();
        });

        // Save button click
        binding.btnRefuelSave.setOnClickListener(v -> {
            addHistory(vehicleUpdate.getId());
            finish();
        });
    }
    public void getData() {
        listVehicle = new ArrayList<>();

        _myRef = mDatabase.getReference("User").child(firebaseUser.getUid()).child("ListVehicle");

        _myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data:snapshot.getChildren()) {
                    Vehicle vehicle = data.getValue(Vehicle.class);
                    if(vehicle != null) {
                        listVehicle.add(vehicle);
                        listVehicleName.add(vehicle.getName());
                        adp.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void onStartUp() {
        firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser == null) {
            startActivity(new Intent(RefuelActivity.this, SplashScreenActivity.class));
            finish();
        }
    }

    private void addHistory(String vehicleId) {
        String historyType = "Đổ xăng";
        String historyDate = binding.etRefuelDate.getText().toString();
        String stationName = binding.etRefuelStationName.getText().toString();
        int currentKilometer = Integer.parseInt(binding.etRefuelCurrentKm.getText().toString());
        String historyDetails =
                "Loại xăng: "+binding.etRefuelName.getText().toString() + "\n" +
                "Giá xăng: " + binding.etRefuelPrice.getText().toString() + "\n" +
                "Lượng xăng: " + binding.etRefuelAmount.getText().toString();

        _myRef = mDatabase.getReference("User").child(firebaseUser.getUid()).child("ListVehicle").child(vehicleId).child("ListHistory");

        String id = _myRef.push().getKey();

        if(id != null) {
            History history = new History(id, historyType, historyDate,stationName, currentKilometer, historyDetails);

            _myRef.child(id).setValue(history).addOnSuccessListener(s -> {
                Log.d(TAG, "addVehicle: Add history success");
            }).addOnFailureListener(f -> {
                Log.d(TAG, "addVehicle: Add history fail with " + f.getMessage());
            });
        }
    }
}
