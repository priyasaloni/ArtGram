package com.artgram.artgram;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private Button login_button;
    private EditText login_emailid;
    private EditText login_password;
    private TextView signup;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login_emailid= (EditText) findViewById(R.id.login_emailAddress);
        login_password= (EditText) findViewById(R.id.login_password);
        login_button = (Button) findViewById(R.id.login_button);
        signup= (TextView) findViewById(R.id.signup);
        mAuth=FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Users");
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checklogin();
            }
        });



    }

    private void checklogin() {
        String login_email=login_emailid.getText().toString().trim();
        String login_pass=login_password.getText().toString().trim();
        if(!TextUtils.isEmpty(login_email)&&!TextUtils.isEmpty(login_pass))
        {
            mAuth.signInWithEmailAndPassword(login_email,login_pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        checkUserExist();//checks if user exist in database or not
                    }
                    else
                    {
                        Toast.makeText(LoginActivity.this,"Error while logging!!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

    private void checkUserExist() {
        final String user_id=mAuth.getCurrentUser().getUid();//executes only when users enters correct login id and password
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(user_id))
                {
                    Intent Login_intent=new Intent(LoginActivity.this,MainActivity.class);

                    Login_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(Login_intent);
                }
                else
                {
                    Toast.makeText(LoginActivity.this,"Set Up your account dude!!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });      //checks user exist in database or not
    }
}

