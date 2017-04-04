package com.artgram.artgram;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by sonal on 03-04-2017.
 */
public class RegisterActivity extends AppCompatActivity {
    private EditText nameField;
    private EditText useridField;
    private EditText emailidField;
    private EditText passwordField;
    private Button registerButton;
    private FirebaseAuth mAuth;
    private ProgressDialog progress;
    private DatabaseReference database;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("", "register");

        setContentView(R.layout.register);
        mAuth=FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance().getReference().child("User");
        progress=new ProgressDialog(this);
        nameField=(EditText)findViewById(R.id.name);
        useridField=(EditText)findViewById(R.id.userId);
        emailidField=(EditText)findViewById(R.id.emailAddress);
        passwordField=(EditText)findViewById(R.id.password);
        registerButton=(Button)findViewById(R.id.register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startRegister();
                Intent tabIntent=new Intent(RegisterActivity.this,TabActivity.class);
                tabIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(tabIntent);

            }
        });
    }
    private void startRegister()
    {
        final String name=nameField.getText().toString().trim();
        final String userId=useridField.getText().toString().trim();
        String email=emailidField.getText().toString().trim();
        String password=passwordField.getText().toString().trim();
        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(userId) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password))
        {
            progress.setMessage("Signing up...");
            progress.show();
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                       String user_id=mAuth.getCurrentUser().getUid();
                        DatabaseReference current_user_db=database.child(user_id);
                        current_user_db.child("name").setValue(name);
                        current_user_db.child("user_id").setValue(userId);
                        progress.dismiss();

                    }
                }
            });
        }


    }
    }


