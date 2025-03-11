package com.denis.habibi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextTextEmailAddress2, editTextTextPassword;
    private ImageView btnlog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_in2);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser !=null)
//            goNext();

        editTextTextEmailAddress2 = findViewById(R.id.editTextTextEmailAddress2);
        editTextTextPassword = findViewById(R.id.editTextTextPassword);
        findViewById(R.id.btnlog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextTextEmailAddress2.getText().toString().isEmpty() || editTextTextEmailAddress2.getText().toString().isEmpty())  {
                    Toast.makeText(LoginActivity.this, "Не все поля заполнены",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if(editTextTextPassword.getText().toString().length() < 6) {
                    Toast.makeText(LoginActivity.this, "Пароль должен состоять не менее 6 символов", Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.signInWithEmailAndPassword(editTextTextEmailAddress2.getText().toString(),
                        editTextTextPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(LoginActivity.this,"Готово!",Toast.LENGTH_SHORT).show();
                            goNext();
                        }else
                            Toast.makeText(LoginActivity.this, "Такого пользователя не найдено", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });



        TextView myTextView = findViewById(R.id.Register);
        myTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity3.class);
                startActivity(intent);
            }
        });

        TextView back = findViewById(R.id.backBtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    public void goNext(){
        startActivity(new Intent(LoginActivity.this,HabitMain.class));
        finish();
    }






}