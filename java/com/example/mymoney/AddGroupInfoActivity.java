package com.example.mymoney;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddGroupInfoActivity extends AppCompatActivity {
    private static final String TAG = "AddGroupInfoActivity";

    TextView groupName;
    TextInputEditText noOfMembers;
    Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group_info);
        groupName = findViewById(R.id.groupName);
        nextButton = findViewById(R.id.nextButton);
        noOfMembers = findViewById(R.id.noOfMembers);

        final Group group = (Group) getIntent().getSerializableExtra("group");
        groupName.setText(group.getGroupname());

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(noOfMembers.getText().toString().length() != 0) {
                    final int number = Integer.parseInt(noOfMembers.getText().toString());
                    FirebaseFirestore.getInstance()
                            .collection("Groups")
                            .whereEqualTo("groupname", groupName)
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                                    for(DocumentSnapshot snapshot: snapshotList) {
                                        final Group group1 = snapshot.toObject(Group.class);
                                        group1.setNo_members(number);
                                        snapshot.getReference().set(group1)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d(TAG, "onSuccess: Updated");
                                                    }
                                                });
                                    }
                                }
                            });
                    Intent in = new Intent(getApplicationContext(), AddMembersActivity.class);
                    in.putExtra("group", group);
                    startActivity(in);
                }
                else {
                    Toast.makeText(AddGroupInfoActivity.this, "Fill no. of members", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void back(View view) {
        Intent in = new Intent(getApplicationContext(), GroupActivity.class);
        startActivity(in);
    }
}
