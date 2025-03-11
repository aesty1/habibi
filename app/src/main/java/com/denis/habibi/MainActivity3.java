package com.denis.habibi;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity3 extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText Nickname, emailline, passwordLine, PasswordLine;
    private ImageButton btnFacebook, btnTwitter, btnEmail;
    private Button btnBack, btnLogin, btnGoLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        mAuth = FirebaseAuth.getInstance();

        Nickname = findViewById(R.id.Nickname);
        emailline = findViewById(R.id.Emailline);
        passwordLine = findViewById(R.id.PasswordLine);
        PasswordLine = findViewById(R.id.PasswordLine2);

        btnBack = findViewById(R.id.backBtn);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoLogin = findViewById(R.id.btnGoLogin);


        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity3.this, MainActivity.class);
            startActivity(intent);
        });
        btnGoLogin.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity3.this, LoginActivity.class);
            startActivity(intent);
        });

        btnLogin.setOnClickListener(v -> registerUser());
    }

    private void openUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }



    private void registerUser() {
        String nickname = Nickname.getText().toString().trim();
        String email = emailline.getText().toString().trim();
        String password = passwordLine.getText().toString();
        String confirmPassword = PasswordLine.getText().toString();

        if (nickname.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Не все поля заполнены", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "Пароль должен составлять не менее 6 символов", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Пароли не совпадают", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(MainActivity3.this, "Готово!", Toast.LENGTH_SHORT).show();
                goNext();
            } else {
                Toast.makeText(MainActivity3.this, "Такой пользователь существует", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goNext() {
        startActivity(new Intent(MainActivity3.this, LoginActivity.class));
        finish();
    }
}