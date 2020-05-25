package com.example.mymoney;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class MonitorActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "MonitorActivity";

    TextView showBillsAmount;
    TextView showExpenseAmount;
    TextView textView19;
    TextView shownpaidAmount;
    TextView showBudget;
    TextInputEditText yearselect;
    Button showSpendings;
    Spinner monthSpinner;
    ArrayAdapter<CharSequence> adapter;
    ProgressBar progressBar3;

    String text;
    int month;
    int year;
    String fromdate;
    String todate;
    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    int flag=0;

    int BILLSPEND = 0;
    int EXPENSESPEND = 0;
    int UNPAIDIMPBILLS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor);

        showBillsAmount = findViewById(R.id.showBillsAmount);
        showExpenseAmount = findViewById(R.id.showExpenseAmount);
        textView19 = findViewById(R.id.textView19);
        showBudget = findViewById(R.id.showBudget);
        shownpaidAmount = findViewById(R.id.shownpaidAmount);
        yearselect = findViewById(R.id.yearselect);
        showSpendings = findViewById(R.id.showSpendings);
        progressBar3 = findViewById(R.id.progressBar3);

        monthSpinner = findViewById(R.id.monthSpinner);
        adapter = ArrayAdapter.createFromResource(this, R.array.month, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(adapter);
        monthSpinner.setOnItemSelectedListener(this);
        textView19.setText("Monthly Spendings");

        progressBar3.setVisibility(View.GONE);

        /*showSpendings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(yearselect.getText().toString().length() == 0) {
                    Toast.makeText(MonitorActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
                else {
                    BILLSPEND = 0;
                    EXPENSESPEND = 0;
                    TOTALSPEND = 0;
                    UNPAIDIMPBILLS = 0;
                    year = Integer.parseInt(yearselect.getText().toString());
                    textView19.setText("Monthly Spendings for "+text+ " " + year);
                    if(month<10) {
                        fromdate = year+"/0"+month+"/01";
                        todate = year+"/0"+month+"/31";
                    }
                    else {
                        fromdate = year+"/"+month+"/01";
                        todate = year+"/"+month+"/31";
                    }
                    calculateBillSpend();
                    calculateExpenseSpend();
                    calculateUnpaidImpBills();
                    TOTALSPEND = BILLSPEND + EXPENSESPEND;
                    showTotalAmount.setText(String.valueOf(TOTALSPEND));
                }
            }
        });*/

        FirebaseFirestore.getInstance()
                .collection("Budget")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot snapshot: snapshotList) {
                            flag=1;
                            final Budget budget = snapshot.toObject(Budget.class);
                            showBudget.setText(budget.getBudget());
                        }
                    }
                });

        if (flag == 0) {
            showBudget.setText("Update Budget");
        }
    }

    public void back(View view) {
        Intent in = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(in);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        text = parent.getItemAtPosition(position).toString();
        month = position+1;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(this, "Select Month", Toast.LENGTH_SHORT).show();
    }

    public void calculateBillSpend() {
        FirebaseFirestore.getInstance()
                .collection("Bills")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot snapshot: snapshotList) {
                            if(snapshot.getBoolean("paid") == true) {
                                String DATE = snapshot.getString("date");
                                if ((DATE.compareTo(todate)<=0) && (DATE.compareTo(fromdate) >= 0)) {
                                    String AMOUNT = snapshot.getString("amount");
                                    int amount = Integer.parseInt(AMOUNT);
                                    BILLSPEND = BILLSPEND + amount;
                                }
                            }
                        }
                        showBillsAmount.setText(String.valueOf(BILLSPEND));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: ", e);
                    }
                });
    }

    public void calculateExpenseSpend() {
        FirebaseFirestore.getInstance()
                .collection("Expenses")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot snapshot: snapshotList) {
                            String DATE = snapshot.getString("date");
                            if (DATE.compareTo(todate)<=0) {
                                if (DATE.compareTo(fromdate) >= 0) {
                                    int amount = Integer.parseInt(snapshot.getString("amount"));
                                    EXPENSESPEND = EXPENSESPEND + amount;
                                }
                            }
                        }
                        showExpenseAmount.setText(String.valueOf(EXPENSESPEND));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: ", e);
                    }
                });
    }

    public void calculateUnpaidImpBills() {
        FirebaseFirestore.getInstance()
                .collection("Bills")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot snapshot: snapshotList) {
                            if(snapshot.getBoolean("paid") == false && snapshot.getBoolean("imp") == true) {
                                String DATE = snapshot.getString("date");
                                if ((DATE.compareTo(todate)<=0) && (DATE.compareTo(fromdate) >= 0)) {
                                    String AMOUNT = snapshot.getString("amount");
                                    int amount = Integer.parseInt(AMOUNT);
                                    UNPAIDIMPBILLS = UNPAIDIMPBILLS + amount;
                                }
                            }
                        }
                        shownpaidAmount.setText(String.valueOf(UNPAIDIMPBILLS));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: ", e);
                    }
                });
    }

    public void showspendings(View view) {
        view.setEnabled(false);
        progressBar3.setVisibility(View.VISIBLE);

        if(yearselect.getText().toString().length() == 0) {
            Toast.makeText(MonitorActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            view.setEnabled(true);
            progressBar3.setVisibility(View.GONE);
        }
        else {
            BILLSPEND = 0;
            EXPENSESPEND = 0;
            UNPAIDIMPBILLS = 0;
            showBillsAmount.setText("0");
            showExpenseAmount.setText("0");
            shownpaidAmount.setText("0");
            year = Integer.parseInt(yearselect.getText().toString());
            if(month<10) {
                fromdate = year+"/0"+month+"/01";
                todate = year+"/0"+month+"/31";
            }
            else {
                fromdate = year+"/"+month+"/01";
                todate = year+"/"+month+"/31";
            }
            calculateBillSpend();
            calculateExpenseSpend();
            calculateUnpaidImpBills();
            textView19.setText("Monthly Spendings for "+text+ " " + year);
            view.setEnabled(true);
            progressBar3.setVisibility(View.GONE);
        }
    }
}
