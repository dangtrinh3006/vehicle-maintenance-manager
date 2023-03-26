package tada.app.xetoi.UI.home;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import tada.app.xetoi.Adapter.TemplateButtonAdapter;
import tada.app.xetoi.Model.TemplateButton;
import tada.app.xetoi.R;
import tada.app.xetoi.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<TemplateButton> listButton = new ArrayList<>();
        listButton.add(new TemplateButton("Đổ xăng", R.drawable.refuel_logo));
        listButton.add(new TemplateButton("Thay nhớt", R.drawable.viscous));
        listButton.add(new TemplateButton("Sửa linh kiện", R.drawable.fix_logo));
        listButton.add(new TemplateButton("Tìm vị trí", R.drawable.map_logo));

        TemplateButtonAdapter adapter = new TemplateButtonAdapter(getActivity(), listButton);
        RecyclerView rcvMenu = view.findViewById(R.id.rcvMenu);
        rcvMenu.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        rcvMenu.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        FragmentManager fm = getActivity().getSupportFragmentManager();
        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }
}