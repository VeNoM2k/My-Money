package com.example.mymoney;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class GroupActivity extends AppCompatActivity implements GroupRecyclerAdaptor.GroupListener {

    RecyclerView recyclerView;
    GroupRecyclerAdaptor groupRecyclerAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        recyclerView = findViewById(R.id.grouprecyclerview);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        initRecyclerView(FirebaseAuth.getInstance().getCurrentUser());
    }

    public void back(View view) {
        Intent in = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(in);
    }

    public void startnewgroup(View view) {
        Intent in = new Intent(getApplicationContext(), StartNewGroupActivity.class);
        startActivity(in);
    }

    private void initRecyclerView(FirebaseUser user) {
        Query query = FirebaseFirestore.getInstance()
                .collection("Groups")
                .whereEqualTo("userId", user.getUid());

        FirestoreRecyclerOptions<Group> options = new FirestoreRecyclerOptions.Builder<Group>()
                .setQuery(query, Group.class)
                .build();

        groupRecyclerAdaptor = new GroupRecyclerAdaptor(options, this);
        recyclerView.setAdapter(groupRecyclerAdaptor);

        groupRecyclerAdaptor.startListening();
    }

    @Override
    public void handleEditGroup(DocumentSnapshot snapshot) {
        Group group = snapshot.toObject(Group.class);
        if(group.getNo_members() == 1) {
            Intent in = new Intent(getApplicationContext(), AddGroupInfoActivity.class);
            in.putExtra("group", group);
            startActivity(in);
        }
        else {
            Intent in = new Intent(getApplicationContext(), AboutGroupActivity.class);
            startActivity(in);
        }
    }
}
