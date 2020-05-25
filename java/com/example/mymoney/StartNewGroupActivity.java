package com.example.mymoney;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class StartNewGroupActivity extends AppCompatActivity {

    private static final String TAG = "StartNewGroupActivity";
    TextInputEditText groupname;
    Button savegroupbutton;

    String GROUPNAME;
    int number=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_new_group);

        groupname = findViewById(R.id.groupname);
        savegroupbutton = findViewById(R.id.savegroupbutton);
    }

    public void back(View view) {
        Intent in = new Intent(getApplicationContext(), GroupActivity.class);
        startActivity(in);
    }

    public void savegroup(View view) {
        GROUPNAME = groupname.getText().toString();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Group group = new Group(GROUPNAME, userId, number);
        FirebaseFirestore.getInstance()
                .collection("Groups")
                .add(group)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "onSuccess: Group sucessfully added");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(StartNewGroupActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        Intent in = new Intent(getApplicationContext(), GroupActivity.class);
        startActivity(in);
    }
}
