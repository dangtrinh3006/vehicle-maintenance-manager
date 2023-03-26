package tada.app.xetoi;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import tada.app.xetoi.Model.Vehicle;

public class VehicleDetailActivity extends AppCompatActivity {
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference _myRef;
    ImageView imgVehicle;
    TextView tvVehicleName,tvVehicleNumber,tvVehicleBrand,tvVehicleType;
    Button btnDelete;
    Vehicle vehicle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_detail);
        inItUi();
        getData();
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickDelete(vehicle);
            }
        });

    }

    public void inItUi(){
        imgVehicle =  findViewById(R.id.imgVehicle);
        tvVehicleName =  findViewById(R.id.tvVehicleName);
        tvVehicleNumber =  findViewById(R.id.tvVehicleNumber);
        tvVehicleBrand =  findViewById(R.id.tvVehicleBrand);
        tvVehicleType =  findViewById(R.id.tvVehicleType);
        btnDelete = findViewById(R.id.btnDelete);
    }
    public void getData(){
        Bundle bundle = getIntent().getExtras();
        if(bundle==null) return;

        vehicle = (Vehicle) bundle.get("object_vehicle");
        if (vehicle.getType() == "Car" ){

            imgVehicle.setImageResource(R.drawable.car);
        } else {
            imgVehicle.setImageResource(R.drawable.bike);
        }
        imgVehicle.setImageResource(R.drawable.bike);
        tvVehicleName.setText(vehicle.getName());
        tvVehicleNumber.setText(vehicle.getNumber());
        tvVehicleBrand.setText(vehicle.getBrand());
        tvVehicleType.setText(vehicle.getType());



    }
    private void onClickDelete(Vehicle p) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.app_name))
                .setMessage("Bạn muốn xóa không?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        _myRef = mDatabase.getReference("Vehicle");
                        _myRef.child(p.getId()).removeValue();
                        finish();
                    }
                })
                .setNegativeButton("Cancel",null)
                .show();
    }

}