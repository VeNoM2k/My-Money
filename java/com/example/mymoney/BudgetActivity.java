package com.example.mymoney;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.Document;

import java.util.List;

public class BudgetActivity extends AppCompatActivity {

    private static final String TAG = "BudgetActivity";

    TextInputEditText updateBudgetEditText;
    Button updateBudgetButton;
    ProgressBar progressBar4;

    String USERID;
    int flag=0;
    String BUDGET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        updateBudgetEditText = findViewById(R.id.updateBudgetEditText);
        updateBudgetButton = findViewById(R.id.updateBudgetButton);
        progressBar4 = findViewById(R.id.progressBar4);
        progressBar4.setVisibility(View.GONE);
        USERID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore.getInstance()
                .collection("Budget")
                .whereEqualTo("userId", USERID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot snapshot: snapshotList) {
                            flag=1;
                            final Budget budget = snapshot.toObject(Budget.class);
                            updateBudgetEditText.setText(budget.getBudget());
                            updateBudgetEditText.setSelection(budget.getBudget().length());
                        }
                    }
                });
        
        updateBudgetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                v.setEnabled(false);
                progressBar4.setVisibility(View.VISIBLE);

                BUDGET = updateBudgetEditText.getText().toString();
                if(flag==0) {
                    //add new budget
                    Budget budget = new Budget();
                    budget.setBudget(BUDGET);
                    budget.setUserId(USERID);
                    FirebaseFirestore.getInstance()
                            .collection("Budget")
                            .add(budget)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d(TAG, "onSuccess: Added");
                                    v.setEnabled(true);
                                    progressBar4.setVisibility(View.INVISIBLE);
                                    Toast.makeText(BudgetActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e(TAG, "onFailure: ", e);
                                    Toast.makeText(BudgetActivity.this, "Failed to update", Toast.LENGTH_SHORT).show();
                                    v.setEnabled(true);
                                    progressBar4.setVisibility(View.INVISIBLE);
                                }
                            });
                }
                else {
                    //update
                    FirebaseFirestore.getInstance()
                            .collection("Budget")
                            .whereEqualTo("userId", USERID)
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                                    for(DocumentSnapshot snapshot: snapshotList) {
                                        final Budget budget = snapshot.toObject(Budget.class);
                                        budget.setBudget(BUDGET);
                                        snapshot.getReference().set(budget)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d(TAG, "onSuccess: Updated");
                                                        v.setEnabled(true);
                                                        progressBar4.setVisibility(View.INVISIBLE);
                                                        Toast.makeText(BudgetActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.e(TAG, "onFailure: ", e);
                                                        Toast.makeText(BudgetActivity.this, "Failed to update", Toast.LENGTH_SHORT).show();
                                                        v.setEnabled(true);
                                                        progressBar4.setVisibility(View.INVISIBLE);
                                                    }
                                                });
                                    }
                                }
                            });

                }
            }
        });
    }
}
