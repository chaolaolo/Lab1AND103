package com.example.lab;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.lab.databinding.CollectionFragmentBinding;


public class CollectionFragment extends Fragment {

    CollectionFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = CollectionFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.txtLoginEmail.setOnClickListener(v -> {
            FragSignIn fragSignIn = new FragSignIn();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fr_signIn_Act,fragSignIn);
            transaction.addToBackStack(null);
            transaction.commit();
        });
        binding.txtLoginPhoneNum.setOnClickListener(v->{
//            startActivity(new Intent(getActivity(),ActivitySignInPhone.class));
            FragSignInPhone fragSignInPhone = new FragSignInPhone();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fr_signIn_Act,fragSignInPhone);
            transaction.addToBackStack(null);
            transaction.commit();
        });
        binding.txtRegisterNew.setOnClickListener(v->{
            FragRegister fragRegister = new FragRegister();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fr_signIn_Act,fragRegister);
            transaction.addToBackStack(null);
            transaction.commit();
        });
    }
}