package com.example.lab;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab.databinding.FragSignoutBinding;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragSignout extends Fragment {

    FragSignoutBinding binding;
    FirebaseAuth userAuth;
    FirebaseFirestore db;
    private RecyclerView recyclerView;
    private FloatingActionButton btnAdd;
    private CountryAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragSignoutBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();
        recyclerView = view.findViewById(R.id.rcCountries);
        btnAdd = view.findViewById(R.id.btnAdd);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Query query = db.collection("cities");
        userAuth = FirebaseAuth.getInstance();

        FirestoreRecyclerOptions<CountryModel> options = new FirestoreRecyclerOptions.Builder<CountryModel>()
                .setQuery(query, CountryModel.class)
                .build();

        adapter = new CountryAdapter(options,getContext());
        recyclerView.setAdapter(adapter);

        btnAdd.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog, null);
            builder.setView(dialogView);
            AlertDialog dialog = builder.create();

            TextView txtTitleDialog = dialogView.findViewById(R.id.txtTitleDialog);
            TextInputEditText edtLinkimg = dialogView.findViewById(R.id.edtLinkimg);
            TextInputEditText edtName = dialogView.findViewById(R.id.edtName);
            TextInputEditText edtSize = dialogView.findViewById(R.id.edtSize);
            TextInputEditText edtPrice = dialogView.findViewById(R.id.edtPrice);
            TextInputEditText edtQuantity = dialogView.findViewById(R.id.edtQuantity);
            Button btnCancel = dialogView.findViewById(R.id.btnCancel);
            Button btnSave = dialogView.findViewById(R.id.btnSave);

            txtTitleDialog.setText("Add New Shirt");
            btnSave.setText("Add Now");
            btnCancel.setText("Cancel");

            btnCancel.setOnClickListener(v1 -> dialog.dismiss());
            btnSave.setOnClickListener(v12 -> {
                String linkimg = edtLinkimg.getText().toString().trim();
                String name = edtName.getText().toString().trim();
                String size = edtSize.getText().toString().trim();
                String price = edtPrice.getText().toString().trim();
                String quantity = edtQuantity.getText().toString().trim();

                boolean err = false;

                if (linkimg.isEmpty()) {
                    edtLinkimg.setError("Please enter link image!");
                    err = true;
                }
                if (name.isEmpty()) {
                    edtName.setError("Please enter shirt name!");
                    err = true;
                }
                if (size.isEmpty()) {
                    edtSize.setError("Please enter shirt size!");
                    err = true;
                }
                if (price.isEmpty()) {
                    edtPrice.setError("Please enter shirt price!");
                    err = true;
                }
                if (quantity.isEmpty()) {
                    edtQuantity.setError("Please enter shirt quantity!");
                    err = true;
                }
                if (!err) {
                    CountryModel countryModel = new CountryModel(linkimg, name, size, Integer.parseInt(price), Integer.parseInt(quantity));
                    db.collection("cities")
                            .add(countryModel)
                            .addOnSuccessListener(documentReference -> {
                                Toast.makeText(getContext(), "Add successfully", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(getContext(), "Error adding item!", Toast.LENGTH_SHORT).show();
                            });
                }

            });

            dialog.show();

        });


    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}