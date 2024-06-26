package com.example.lab;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.lab.databinding.FragSignInPhoneBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class FragSignInPhone extends Fragment {

    FragSignInPhoneBinding binding;
    FirebaseAuth userAuth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;
    String mVerificationId;
    PhoneAuthProvider.ForceResendingToken mResendingToken;
    PhoneAuthCredential credential;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragSignInPhoneBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userAuth = FirebaseAuth.getInstance();
        mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                Log.d("TAG", "Verification Completed with credential: " + credential);
                Toast.makeText(getActivity(), "Verification Completed: " + credential, Toast.LENGTH_SHORT).show();
                binding.edtOTP.setText(credential.getSmsCode());
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Log.e("TAG", "Invalid requests", e);
                    Toast.makeText(getActivity(), "Invalid phone number", Toast.LENGTH_SHORT).show();
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Log.e("TAG", "Too many requests", e);
                    Toast.makeText(getActivity(), "Too many requests. Please try again later.", Toast.LENGTH_LONG).show();
                } else if(e instanceof FirebaseAuthMissingActivityForRecaptchaException){
                    Log.e("TAG", "Verification failed", e);
                    Toast.makeText(getActivity(), "Verification null activity: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }else {
                    Log.e("TAG", "Verification failed", e);
                }
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                Log.d("TAG", "VerificationId(s): "+s);
                mVerificationId = s;
                mResendingToken=forceResendingToken;
                Toast.makeText(getActivity(), "OTP sent successfully", Toast.LENGTH_SHORT).show();
            }
        };


        binding.btnGetOTP.setOnClickListener(v -> {
            String phoneNumber = binding.edtPhoneNumber.getText().toString().trim();
            if (phoneNumber.isEmpty()){
                binding.edtPhoneNumber.setError("Please enter your phone number");
            }
            else if (!isValidPhoneNumber(phoneNumber)) {
                binding.edtPhoneNumber.setError("Please enter start +84 and just 9-10 numbers");
            }else{
                Toast.makeText(getActivity(), "OTP sent, please check your phone SMS", Toast.LENGTH_SHORT).show();
                PhoneAuthOptions options = PhoneAuthOptions.newBuilder(userAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(requireActivity())
                        .setCallbacks(mCallBacks)
                        .build();
                PhoneAuthProvider.verifyPhoneNumber(options);
            }
        });
        binding.btnSignInPhone.setOnClickListener(v -> {
            String otp = binding.edtOTP.getText().toString().trim();
            if (otp.isEmpty()) {
                binding.edtOTP.setError("Please enter OTP");
            }else if (otp.length() < 6){
                binding.edtOTP.setError("Please enter enough 6 characters");
            }else if(otp.equals("548386")){
                credential = PhoneAuthProvider.getCredential(mVerificationId, otp);
                signInWithPhoneAuthCredential(credential);

            }

        });

    }

    public boolean isValidPhoneNumber(String phoneNumber) {
        String phoneNumberPattern = "^\\+84[0-9]{9,10}$";
        return phoneNumber.matches(phoneNumberPattern);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        userAuth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = task.getResult().getUser();
                            Toast.makeText(getActivity(), "Sign In Successfully", Toast.LENGTH_SHORT).show();
//                            FragSignout fragSignout = new FragSignout();
//                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                            transaction.replace(R.id.fr_signIn_Act, fragSignout);
//                            transaction.addToBackStack(null);
//                            transaction.commit();
                        } else {
                            Log.d("TAG", "Sign In Failed", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(getActivity(), "Invalid OTP", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}