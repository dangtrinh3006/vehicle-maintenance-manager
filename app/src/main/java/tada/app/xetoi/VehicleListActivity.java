package tada.app.xetoi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import tada.app.xetoi.Adapter.VehicleAdapter;
import tada.app.xetoi.Model.Vehicle;

public class VehicleListActivity extends AppCompatActivity {
    private static final String TAG = "VEHICLE LIST";

    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference _myRef;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    private RecyclerView rcvVehicle;
    List<Vehicle> vehicles;

    VehicleAdapter vehicleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();

        onStartUp();

        setContentView(R.layout.activity_vehicle_list);
        rcvVehicle = findViewById(R.id.rcvVehicle);
        getData();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvVehicle.setLayoutManager(linearLayoutManager);
        vehicleAdapter = new VehicleAdapter(this,vehicles);
        rcvVehicle.setAdapter(vehicleAdapter);

    }

    private void onStartUp() {
        firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser == null) {
            startActivity(new Intent(VehicleListActivity.this, SplashScreenActivity.class));
            finish();
        }
    }

    public void getData() {
        vehicles = new ArrayList<>();

        _myRef = mDatabase.getReference("User").child(firebaseUser.getUid()).child("ListVehicle");

        _myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Vehicle vehicle = snapshot.getValue(Vehicle.class);
                vehicles.add(vehicle);
                vehicleAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                getData();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuAdd:
                Intent i = new Intent(this, VehicleFormActivity.class);
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}