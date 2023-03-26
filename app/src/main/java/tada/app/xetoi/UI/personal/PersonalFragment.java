package tada.app.xetoi.UI.personal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import tada.app.xetoi.Adapter.PersonAdapter;
import tada.app.xetoi.LoginActivity;
import tada.app.xetoi.Model.TemplateButton;
import tada.app.xetoi.R;
import tada.app.xetoi.databinding.FragmentPersonalBinding;

public class PersonalFragment extends Fragment {

    private FragmentPersonalBinding binding;
    private FirebaseAuth firebaseAuth;

    private GoogleSignInClient googleSignInClient;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
       binding = FragmentPersonalBinding.inflate(inflater,container,false);
       setHasOptionsMenu(true);
       return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();

        List<TemplateButton> listButton = new ArrayList<>();
        listButton.add(new TemplateButton("Thông tin cá nhân", R.drawable.user_logo));
        listButton.add(new TemplateButton("Xe của tôi", R.drawable.car_logo));
        listButton.add(new TemplateButton("SOS", R.drawable.sos_logo));

        PersonAdapter adapter = new PersonAdapter(getActivity(), listButton);
        RecyclerView rcvMenu = view.findViewById(R.id.rcvPerson);

        rcvMenu.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        rcvMenu.setAdapter(adapter);
    }

    private void checkUser() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user == null) {
            startActivity(new Intent(this.getActivity(), LoginActivity.class));
            this.getActivity().finish();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.user_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.itemLogout:
                signOut();
                checkUser();
        }
        return super.onOptionsItemSelected(item);
    }

    private void signOut() {
        GoogleSignInOptions gso = new GoogleSignInOptions.
                Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                build();

        Context context = this.getContext();
        if(context == null) return;
        googleSignInClient = GoogleSignIn.getClient(context, gso);

        googleSignInClient.signOut();
        firebaseAuth.signOut();
    }
}