package com.example.lab;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.lab.databinding.FragSignoutBinding;
import com.google.firebase.auth.FirebaseAuth;

public class FragSignout extends Fragment {

    FragSignoutBinding binding;
    FirebaseAuth userAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragSignoutBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userAuth = FirebaseAuth.getInstance();

        binding.btnSignout.setOnClickListener(v -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Sign Out")
                    .setMessage("Are your sure to Sign Out?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        userAuth.signOut();
                        Toast.makeText(getActivity(), "Sign Out Successfully", Toast.LENGTH_SHORT).show();

                        CollectionFragment collectionFragment = new CollectionFragment();
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fr_signIn_Act, collectionFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    })
                    .setNegativeButton("No", (dialog, which) -> dialog.cancel())
                    .show();
        });

    }
}