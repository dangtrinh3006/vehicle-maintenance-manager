package tada.app.xetoi.UI.test;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import tada.app.xetoi.R;
import tada.app.xetoi.databinding.FragmentTestBinding;


public class TestFragment extends Fragment {
    FragmentTestBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTestBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnCloseTest.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().popBackStack("0", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        });
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}