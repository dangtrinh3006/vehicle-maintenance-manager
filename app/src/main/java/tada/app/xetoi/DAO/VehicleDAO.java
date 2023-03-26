package tada.app.xetoi.DAO;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import tada.app.xetoi.Model.History;
import tada.app.xetoi.Model.Vehicle;

public class VehicleDAO {
    private DatabaseReference databaseReference;
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference myRef = mDatabase.getReference("Vehicle");
    public VehicleDAO(){

        databaseReference = mDatabase.getReference(History.class.getSimpleName());
    }

    public Task<Void> addVehicle(Vehicle vehicle){
        String id = databaseReference.push().getKey();
        vehicle.setId(id);
        return databaseReference.child(id).setValue(vehicle);
    }

    public Task<Void> updateVehicle(String key, HashMap<String, Object> data){
        return databaseReference.child(key).updateChildren(data);
    }

    public Task<Void> deleteVehicle(String key){
        return databaseReference.child(key).removeValue();
    }
}
