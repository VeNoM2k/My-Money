package com.example.mymoney;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Date;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;


public class BillActivity extends AppCompatActivity implements BillRecyclerAdaptor.BillListener {

    RecyclerView recyclerView;
    BillRecyclerAdaptor billsRecyclerAdaptor;
    private static final String TAG = "BillActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        recyclerView = findViewById(R.id.billrecyclerview);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        FloatingActionButton billfab = findViewById(R.id.floatingActionButton3);
        billfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddbillActivity.class);
                startActivity(intent);
                //startExamplesActivity();
            }
        });
        initRecyclerView(FirebaseAuth.getInstance().getCurrentUser());
    }

    private void startExamplesActivity() {
        Intent intent = new Intent(this, ExampleActivity.class);
        startActivity(intent);
    }

    private void initRecyclerView(FirebaseUser user) {
        Query query = FirebaseFirestore.getInstance()
                .collection("Bills")
                .whereEqualTo("userId", user.getUid())
                .orderBy("paid", Query.Direction.ASCENDING)         //Ordering and Indexing
                .orderBy("imp", Query.Direction.DESCENDING)
                .orderBy("date", Query.Direction.ASCENDING)
                .orderBy("created", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Bill> options = new FirestoreRecyclerOptions.Builder<Bill>()
                .setQuery(query, Bill.class)
                .build();

        billsRecyclerAdaptor = new BillRecyclerAdaptor(options, this);
        recyclerView.setAdapter(billsRecyclerAdaptor);
        billsRecyclerAdaptor.startListening();

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            if (direction == ItemTouchHelper.LEFT) {
                BillRecyclerAdaptor.BillViewHolder billViewHolder = (BillRecyclerAdaptor.BillViewHolder) viewHolder;
                billViewHolder.deleteItem();
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addBackgroundColor(ContextCompat.getColor(BillActivity.this, R.color.colorAccent))
                    .addActionIcon(R.drawable.ic_delete_black_24dp)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    /*@Override
    public void handleCheckChanged(boolean isChecked, DocumentSnapshot snapshot) {
        Log.d(TAG, "handleCheckChanged: " + isChecked);
        snapshot.getReference().update("paid", isChecked)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: ");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
                    }
                });
    }*/

    @Override
    public void handleEditBill(final DocumentSnapshot snapshot) {
        final Bill bill = snapshot.toObject(Bill.class);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        final View editBillView = layoutInflater.inflate(R.layout.editbill, null);
        final EditText editamount = (EditText) editBillView.findViewById(R.id.editamount);
        final EditText editdes = (EditText) editBillView.findViewById(R.id.editdes);
        final CheckBox editimp = (CheckBox) editBillView.findViewById(R.id.editimp);
        final CheckBox editpaid = (CheckBox) editBillView.findViewById(R.id.editpaid);
        editamount.setText(bill.getAmount());
        editamount.setSelection(bill.getAmount().length());
        editdes.setText(bill.getDescription());
        editdes.setSelection(bill.getDescription().length());
        editimp.setChecked(bill.isImp());
        editpaid.setChecked(bill.isPaid());
        new AlertDialog.Builder(this)
                .setTitle("Edit Bill")
                .setView(editBillView)
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newamount = editamount.getText().toString();
                        String newdes = editdes.getText().toString();
                        Boolean newimp = editimp.isChecked();
                        Boolean newpaid = editpaid.isChecked();
                        bill.setAmount(newamount);
                        bill.setDescription(newdes);
                        bill.setImp(newimp);
                        bill.setPaid(newpaid);
                        snapshot.getReference().set(bill)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "onSuccess: ");
                                    }
                                });
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void handleDeleteItem(DocumentSnapshot snapshot) {
        final DocumentReference documentReference = snapshot.getReference();
        final Bill bill = snapshot.toObject(Bill.class);
        documentReference.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: Bill Deleted");
                        Snackbar.make(recyclerView, "Bill Deleted", Snackbar.LENGTH_LONG)
                                .setAction("Undo", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        documentReference.set(bill);
                                    }
                                })
                                .show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(BillActivity.this, "Bill deletion failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void back(View view) {
        Intent in = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(in);
    }
}