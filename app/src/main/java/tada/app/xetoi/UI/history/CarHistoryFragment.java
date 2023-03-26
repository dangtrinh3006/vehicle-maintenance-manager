package tada.app.xetoi.UI.history;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import tada.app.xetoi.Adapter.CarHistoryAdapter;
import tada.app.xetoi.HistoryDetailActivity;
import tada.app.xetoi.Model.Vehicle;
import tada.app.xetoi.Model.History;
import tada.app.xetoi.R;
import tada.app.xetoi.SplashScreenActivity;
import tada.app.xetoi.databinding.FragmentCarHistoryBinding;

public class CarHistoryFragment extends Fragment {
    private static final String TAG = "CAR HISTORY FRAGMENT";
    FragmentCarHistoryBinding binding;
    List<History> listHistory = new ArrayList<>();
    ListView lvHistory;
    CarHistoryAdapter adapter;

    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference _myRef;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    String vehicleId;
    String carName;
    Vehicle vehicle;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCarHistoryBinding.inflate(inflater, container, false);

        firebaseAuth = FirebaseAuth.getInstance();

        onStartUp();

        lvHistory = binding.lvHistory;

        adapter = new CarHistoryAdapter(inflater.getContext(),R.layout.item_history, listHistory);
        lvHistory.setAdapter(adapter);
        getData();
        lvHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), HistoryDetailActivity.class);
                intent.putExtra("vehicleID", vehicleId);
                intent.putExtra("historyID", listHistory.get(i).getId());
                startActivity(intent);
            }
        });

        lvHistory.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle("Warning");
                alert.setIcon(R.mipmap.ic_launcher);
                alert.setMessage("Are you sure to delete this history!");

                alert.setNegativeButton("No ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int where) {

                    }
                });

                alert.setPositiveButton("Yes ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int where) {
                        _myRef = mDatabase.getReference("User")
                                .child(firebaseUser.getUid())
                                .child("ListVehicle")
                                .child(vehicleId)
                                .child("ListHistory")
                                .child(listHistory.get(i).getId());
                        _myRef.removeValue();
                        listHistory.remove(i);
                        adapter.notifyDataSetChanged();
                        //Log.d(TAG, "YES: " + listHistory.get(i).getId());
                    }
                });
                alert.show();
                return false;
            }
        });

        return binding.getRoot();
    }

    private void onStartUp() {
        firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser == null) {
            startActivity(new Intent(getContext(), SplashScreenActivity.class));
        }
    }

    public void getData() {
        Bundle bundle = this.getArguments();

        if(bundle != null){
            vehicleId = bundle.get("vehicleId").toString();
        }
        _myRef = mDatabase.getReference("User").child(firebaseUser.getUid()).child("ListVehicle").child(vehicleId).child("ListHistory");

        _myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                History history = snapshot.getValue(History.class);
                if(history != null) {
                    listHistory.add(0,history);
                    adapter.notifyDataSetChanged();
                }
                /*Vehicle p = snapshot.getValue(Vehicle.class);
                if (p.getId().equals(carID)) {
                    for (int i = 0; i < p.getLstHistory().size(); i++) {
                        listHistory.add(p.getLstHistory().get(i));
                    }
                    carName = p.getName();
                    binding.tvCarNameTitle.setText(carName);
                }
                adapter.notifyDataSetChanged();
                */
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                /*listHistory.clear();
                Vehicle p = snapshot.getValue(Vehicle.class);
                for (int i = 0; i < p.getLstHistory().size(); i++) {
                    listHistory.add(p.getLstHistory().get(i));

                }
                adapter.notifyDataSetChanged();*/
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                /*listHistory.clear();
                Vehicle p = snapshot.getValue(Vehicle.class);
                for (int i = 0; i < p.getLstHistory().size(); i++) {
                    listHistory.add(p.getLstHistory().get(i));

                }
                adapter.notifyDataSetChanged();*/
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