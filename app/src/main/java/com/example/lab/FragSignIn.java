package com.example.lab;

import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.lab.databinding.FragSignInBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class FragSignIn extends Fragment {

    FragSignInBinding binding;
    FirebaseAuth userAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragSignInBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userAuth = FirebaseAuth.getInstance();

        binding.btnSignIn.setOnClickListener(v -> {
            String email = binding.edtEmail.getText().toString().trim();
            String password = binding.edtPassword.getText().toString().trim();

            boolean err = false;

            if (email.isEmpty()) {
                binding.edtEmail.setError("Please enter email!");
                err = true;
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.edtEmail.setError("Wrong email format!!");
                err = true;
            }
            if (password.isEmpty()) {
                binding.edtPassword.setError("Please enter password!");
                err = true;
            }else if (password.length()<6){
                binding.edtPassword.setError("Please enter more than 6 characters!");
                err = true;
            }

            if (!err) {
            userAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getActivity(), "Sign In Successful.", Toast.LENGTH_SHORT).show();
                        FragSignout fragSignout = new FragSignout();
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fr_signIn_Act, fragSignout);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    } else {
                        Toast.makeText(getActivity(), "Sign in failed!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            }
        });

        binding.txtForgotPass.setOnClickListener(v->{
            FragForgotPass fragForgotPass = new FragForgotPass();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fr_signIn_Act, fragForgotPass);
            transaction.addToBackStack(null);
            transaction.commit();
        });
        binding.txtRegister.setOnClickListener(v->{
            FragRegister fragRegister = new FragRegister();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fr_signIn_Act, fragRegister);
            transaction.addToBackStack(null);
            transaction.commit();
        });
    }
}