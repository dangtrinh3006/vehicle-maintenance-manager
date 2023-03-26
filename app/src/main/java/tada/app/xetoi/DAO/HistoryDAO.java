package tada.app.xetoi.DAO;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import tada.app.xetoi.Model.History;

public class HistoryDAO  {
    private DatabaseReference databaseReference;
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference myRef = mDatabase.getReference("History");
    public HistoryDAO(){

        databaseReference = mDatabase.getReference(History.class.getSimpleName());
    }

    public Task<Void> addHistory(History history){
        String id = databaseReference.push().getKey();
        history.setId(id);
        return databaseReference.child(id).setValue(history);
    }

    public Task<Void> updateHistory(String key, HashMap<String, Object> data){
        return databaseReference.child(key).updateChildren(data);
    }

    public Task<Void> deleteHistory(String key){
        return databaseReference.child(key).removeValue();
    }

}

