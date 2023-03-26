package tada.app.xetoi.UI.history;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tada.app.xetoi.Model.Vehicle;
import tada.app.xetoi.Model.History;
import tada.app.xetoi.databinding.FragmentHistoryDetailBinding;

public class HistoryDetailFragment extends Fragment {

    FragmentHistoryDetailBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHistoryDetailBinding.inflate(inflater, container, false);




        return binding.getRoot();
    }
}