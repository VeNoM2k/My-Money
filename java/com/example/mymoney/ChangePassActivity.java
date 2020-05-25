package com.example.mymoney;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class ChangePassActivity extends AppCompatActivity {

    private static final String TAG = "ChangePassActivity" ;

    EditText editText;
    Button updatepass;
    FirebaseAuth auth;
    ProgressBar progressBar;

    String NEW_PASS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);

        editText = findViewById(R.id.editText);
        updatepass = findViewById(R.id.updatepass);
        auth = FirebaseAuth.getInstance();
        //dialog = new ProgressDialog();
        progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(View.GONE);
    }

    public void changePassword(final View view) {
        view.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);

        NEW_PASS = editText.getText().toString();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

       // UserProfileChangeRequest request = new  UserProfileChangeRequest.Builder()

        user.updatePassword(NEW_PASS)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        view.setEnabled(true);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(ChangePassActivity.this, "Password Updated", Toast.LENGTH_SHORT).show();
                        auth.signOut();
                        Intent intent = new Intent(getApplicationContext(), LoginRegisterActivity.class);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        view.setEnabled(true);
                        progressBar.setVisibility(View.GONE);
                        Log.e(TAG, "onFailure: ", e);
                        Toast.makeText(ChangePassActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
