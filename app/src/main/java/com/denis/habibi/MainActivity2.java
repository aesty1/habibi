//package com.denis.habibi;
//
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageButton;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//
//public class MainActivity2 extends AppCompatActivity {
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main2);
//
//        ImageButton btnFacebook = findViewById(R.id.vk);
//        ImageButton btnTwitter = findViewById(R.id.mail);
//        ImageButton btnEmail = findViewById(R.id.telegram);
//        Button btnBack = findViewById(R.id.backBtn);
//
//        btnFacebook.setOnClickListener(v -> {
//            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com"));
//            startActivity(intent);
//        });
//
//        btnTwitter.setOnClickListener(v -> {
//            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.twitter.com"));
//            startActivity(intent);
//        });
//
//        btnEmail.setOnClickListener(v -> {
//            Intent intent = new Intent(Intent.ACTION_SENDTO);
//            intent.setData(Uri.parse("mailto:"));
//            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"example@example.com"});
//            intent.putExtra(Intent.EXTRA_SUBJECT, "Тема письма");
//            startActivity(intent);
//        });
//        btnBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity2.this, MainActivity.class);
//                startActivity(intent);
//            }
//        });
//    }
//}