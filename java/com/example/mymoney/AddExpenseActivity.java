package com.example.mymoney;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;

public class AddExpenseActivity extends AppCompatActivity {

    private static final String TAG = "AddExpenseActivity";

    TextView expdate;
    DatePickerDialog.OnDateSetListener setListener;
    TextInputEditText expdes;
    TextInputEditText expamount;
    Button expbutton;

    String Amount;
    String Date;
    String Showdate;
    String Description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        expdate = findViewById(R.id.expdate);
        expdes = findViewById(R.id.expdestextView);
        expamount = findViewById(R.id.expamount);
        expbutton = findViewById(R.id.expbutton);

        Calendar calendar = Calendar.getInstance();
        final int year= calendar.get(Calendar.YEAR);
        final int month= calendar.get(Calendar.MONTH);
        final int day= calendar.get(Calendar.DAY_OF_MONTH);


        expdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        AddExpenseActivity.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,setListener,year,month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;

                if((month<10) && (dayOfMonth<10))
                    Date = year+"/0"+month+"/0"+dayOfMonth;
                else if((month<10) && (dayOfMonth>9))
                    Date = year+"/0"+month+"/"+dayOfMonth;
                else if((month>9) && (dayOfMonth<10))
                    Date = year+"/"+month+"/0"+dayOfMonth;
                else
                    Date = year+"/"+month+"/"+dayOfMonth;

                Showdate = dayOfMonth+"/"+month+"/"+year;
                expdate.setText(Showdate);
            }
        };

    }

    public void saveexp(View view) {

        Description = expdes.getText().toString();
        Amount = expamount.getText().toString();
        addexpense(Amount, Date, Showdate, Description);
    }

    private void addexpense(String amount, String date, String showdate, String des) {
        if ((amount != null) && (date != null)) {
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            Expense expense = new Expense(amount, date, showdate, des, new Timestamp(new Date()), userId);

            FirebaseFirestore.getInstance()
                    .collection("Expenses")
                    .add(expense)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "onSuccess: Expense sucessfully added");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddExpenseActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
            Intent in = new Intent(getApplicationContext(), ExpenseActivity.class);
            startActivity(in);
        }
        else{
            new AlertDialog.Builder(this)
                    .setTitle("Fill in all details")
                    .show();
        }
    }

    public void back(View view) {
        Intent in = new Intent(getApplicationContext(), ExpenseActivity.class);
        startActivity(in);
    }
}
