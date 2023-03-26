package tada.app.xetoi.UI.history;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import tada.app.xetoi.Adapter.CarAdapter;
import tada.app.xetoi.Model.Vehicle;
import tada.app.xetoi.R;
import tada.app.xetoi.SplashScreenActivity;
import tada.app.xetoi.databinding.FragmentHistoryBinding;

public class HistoryFragment extends Fragment {
    private FragmentHistoryBinding binding;

    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference _myRef;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    CarAdapter carAdapter;
    ArrayList<Vehicle> listVehicle;
    ListView lvCar;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        firebaseAuth = FirebaseAuth.getInstance();

        onStartUp();

        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        lvCar = binding.lvCar;

        listVehicle = new ArrayList<>();

        carAdapter = new CarAdapter(inflater.getContext(), R.layout.item_vehicle, listVehicle);
        lvCar.setAdapter(carAdapter);
        getData();
        lvCar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                CarHistoryFragment carHistoryFragment = new CarHistoryFragment();
                Bundle bundle = new Bundle();
                bundle.putString("vehicleId", listVehicle.get(i).getId());
                //bundle.putSerializable("car", listVehicle.get(i));
                carHistoryFragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment_activity_main, carHistoryFragment)
                        .addToBackStack("0")
                        .commit();
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;

    }

    private void onStartUp() {
        firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser == null) {
            startActivity(new Intent(getContext(), SplashScreenActivity.class));
        }
    }

    public void getData() {
        _myRef = mDatabase.getReference("User").child(firebaseUser.getUid()).child("ListVehicle");
        _myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Vehicle vehicle = snapshot.getValue(Vehicle.class);
                if (vehicle != null) {
                    listVehicle.add(vehicle);
                    carAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

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
}