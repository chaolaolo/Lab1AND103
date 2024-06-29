package com.example.lab;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CountryAdapter extends FirestoreRecyclerAdapter<CountryModel, CountryAdapter.ItemViewHolder> {

    private Context context;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public CountryAdapter(@NonNull FirestoreRecyclerOptions<CountryModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ItemViewHolder holder, int position, @NonNull CountryModel model) {
        holder.bind(model);
        DocumentSnapshot snapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
        String documentId = snapshot.getId();

        holder.btnDelete.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Delete Item");
            builder.setMessage("Are you sure to delete '" + model.getName() + "'?");
            builder.setNegativeButton("NO", (dialog, which) -> {
                dialog.dismiss();
            });
            builder.setPositiveButton("YES", (dialog, which) -> {
                db.collection("cities")
                        .document(documentId)
                        .delete()
                        .addOnSuccessListener(unused -> {
                            Toast.makeText(context, "Item deleted successfully!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(context, "Error deleting item!", Toast.LENGTH_SHORT).show();
                        });
            });
            builder.create().show();
        });

    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_country, parent, false);
        return new ItemViewHolder(view);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtSize, txtPrice;
        ImageView imgProdImage;
        ImageButton btnDelete;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtSize = itemView.findViewById(R.id.txtSize);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            imgProdImage = itemView.findViewById(R.id.imgProdImage);
            btnDelete = itemView.findViewById(R.id.btnDelete);

            itemView.setOnLongClickListener(v -> {
                DocumentSnapshot snapshot = getSnapshots().getSnapshot(getAdapterPosition());
                String documentId = snapshot.getId();
                CountryModel model = snapshot.toObject(CountryModel.class);
                Log.d("TAG", "Document ID: " + documentId);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflater = LayoutInflater.from(context);
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

                txtTitleDialog.setText("Update Shirt");
                btnSave.setText("Update Now");
                btnCancel.setText("Cancel");

                if (model != null) {
                    edtLinkimg.setText(model.getImage());
                    edtName.setText(model.getName());
                    edtSize.setText(model.getSize());
                    edtPrice.setText(String.valueOf(model.getPrice()));
                    edtQuantity.setText(String.valueOf(model.getQuantity()));
                }
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
                    } else {
                        try {
                            int priceNum = Integer.parseInt(price);
                            if (priceNum <= 0) {
                                edtPrice.setError("Please enter number > 0!");
                                err = true;
                            }
                        } catch (NumberFormatException e) {
                            edtPrice.setError("Please enter a valid price (number)!");
                            err = true;
                        }
                    }
                    if (quantity.isEmpty()) {
                        edtQuantity.setError("Please enter shirt quantity!");
                        err = true;
                    } else {
                        try {
                            int quan = Integer.parseInt(quantity);
                            if (quan <= 0) {
                                edtQuantity.setError("Please enter number > 0!");
                                err = true;
                            }
                        } catch (NumberFormatException e) {
                            edtQuantity.setError("Please enter a valid quantity (number)!");
                            err = true;
                        }
                    }
                    if (!err) {
                        Map<String, Object> updates = new HashMap<>();
                        updates.put("image", linkimg);
                        updates.put("name", name);
                        updates.put("size", size);
                        updates.put("price", Integer.parseInt(price));
                        updates.put("quantity", Integer.parseInt(quantity));
                        db.collection("cities").document(documentId)
                                .update(updates)
                                .addOnSuccessListener(documentReference -> {
                                    Toast.makeText(context, "Update successfully", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(context, "Error updating item!", Toast.LENGTH_SHORT).show();
                                });
                    }

                });
                dialog.show();
                return true;
            });

        }

        public void bind(CountryModel model) {
            txtName.setText(model.getName());
            txtSize.setText(model.getSize());
            txtPrice.setText(String.valueOf(model.getPrice()));
            if (model.getImage() != null && !model.getImage().isEmpty()) {
                Picasso.get()
                        .load(model.getImage())
                        .error(com.firebase.ui.firestore.R.drawable.common_google_signin_btn_icon_light)
                        .into(imgProdImage, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError(Exception e) {
                                Picasso.get().load(com.firebase.ui.firestore.R.drawable.common_google_signin_btn_icon_light).into(imgProdImage);
                            }
                        });
            } else {
                Picasso.get().load(com.firebase.ui.firestore.R.drawable.common_google_signin_btn_icon_light).into(imgProdImage);
            }
        }

    }

}
