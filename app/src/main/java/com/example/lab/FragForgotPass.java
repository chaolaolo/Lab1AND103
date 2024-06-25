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

import com.example.lab.databinding.FragForgotPassBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class FragForgotPass extends Fragment {

    FragForgotPassBinding binding;
    FirebaseAuth userAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragForgotPassBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userAuth = FirebaseAuth.getInstance();

        binding.btnSend.setOnClickListener(v -> {
            String email = binding.edtEmail.getText().toString().trim();

            boolean err = false;

            if (email.isEmpty()) {
                binding.edtEmail.setError("Please enter email!");
                err = true;
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.edtEmail.setError("Wrong email format!!");
                err = true;
            }

            if(!err){
                userAuth.sendPasswordResetEmail(email).addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Sent, check your email to set new password!", Toast.LENGTH_SHORT).show();
                            binding.btnSend.setText("Back");
                            binding.btnSend.setOnClickListener(v->{
                                FragSignIn fragSignIn = new FragSignIn();
                                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.fr_signIn_Act, fragSignIn);
                                transaction.addToBackStack(null);
                                transaction.commit();
                            });
                        } else {
                            Toast.makeText(getActivity(), "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


    }
}