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
import android.widget.CheckBox;
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

public class AddbillActivity extends AppCompatActivity {

    private static final String TAG = "AddbillActivity";

    TextView billdate;
    DatePickerDialog.OnDateSetListener setListener;
    TextInputEditText billdescription;
    TextInputEditText billamount;
    CheckBox imp;
    Button savebill;

    String Amount;
    String Date;
    String ShowDate;
    String Description;
    Boolean Imp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addbill);

        billamount = findViewById(R.id.billamount);
        billdescription = findViewById(R.id.billdescription);
        billdate = findViewById(R.id.billdate);
        imp = findViewById(R.id.imp);
        savebill = findViewById(R.id.savebill);

        Calendar calendar = Calendar.getInstance();
        final int year= calendar.get(Calendar.YEAR);
        final int month= calendar.get(Calendar.MONTH);
        final int day= calendar.get(Calendar.DAY_OF_MONTH);

        billdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        AddbillActivity.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,setListener,year,month,day);
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

                ShowDate = dayOfMonth+"/"+month+"/"+year;
                billdate.setText(ShowDate);

            }
        };
    }

    public void saveBill(View view) {
        Description = billdescription.getText().toString();
        Amount = billamount.getText().toString();

        if (imp.isChecked()) Imp = true;
        else Imp = false;

        addbill(Amount, Description, Imp, Date, ShowDate);
    }

    private void addbill(String amount, String description, Boolean imp, String date, String showdate) {
        if ((amount != null) && (date != null) ) {
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            Bill bill = new Bill(amount, date, showdate, imp, description, new Timestamp(new Date()), false, userId);

            FirebaseFirestore.getInstance()
                    .collection("Bills")
                    .add(bill)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "onSuccess: Bill sucessfully added");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddbillActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
            Intent in = new Intent(getApplicationContext(), BillActivity.class);
            startActivity(in);
        }
        else{
            new AlertDialog.Builder(this)
                    .setTitle("Fill in all details")
                    .show();
        }
    }

    public void back(View view) {
        Intent in = new Intent(getApplicationContext(), BillActivity.class);
        startActivity(in);
    }
}
