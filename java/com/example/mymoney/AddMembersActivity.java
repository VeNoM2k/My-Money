package com.example.mymoney;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AddMembersActivity extends AppCompatActivity {

    TextView groupName1;
    TextView members;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_members);

        groupName1 = findViewById(R.id.groupName1);
        members = findViewById(R.id.members);

        final Group group = (Group) getIntent().getSerializableExtra("group");
        groupName1.setText(group.getGroupname());
        members.setText(group.getNo_members());
    }

    public void back(View view) {
        Intent in = new Intent(getApplicationContext(), AddGroupInfoActivity.class);
        startActivity(in);
    }
}
