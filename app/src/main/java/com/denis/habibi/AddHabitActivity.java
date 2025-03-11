package com.denis.habibi;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddHabitActivity extends AppCompatActivity {

    private EditText etHabitName;
    private TextView tvSelectedDate;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;
    private String selectedDate = "Дата не выбрана";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_habit);

        // Инициализация Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Проверка авторизации
        if (currentUser == null) {
            Toast.makeText(this, "Требуется авторизация!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        etHabitName = findViewById(R.id.etHabitName);
        tvSelectedDate = findViewById(R.id.tvSelectedDate);
        Button btnPickDate = findViewById(R.id.btnPickDate);
        Button btnSaveHabit = findViewById(R.id.btnSaveHabit);

        // Навигация назад
        TextView back = findViewById(R.id.backBtn);
        back.setOnClickListener(v -> {
            startActivity(new Intent(AddHabitActivity.this, HabitMain.class));
            finish();
        });

        // Выбор даты
        btnPickDate.setOnClickListener(v -> showDatePicker());

        // Сохранение привычки
        btnSaveHabit.setOnClickListener(v -> saveHabitToFirebase());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    selectedDate = String.format("%02d.%02d.%d", selectedDay, (selectedMonth + 1), selectedYear);
                    tvSelectedDate.setText(selectedDate);
                }, year, month, day);

        datePickerDialog.show();
    }

    private void saveHabitToFirebase() {
        String habitName = etHabitName.getText().toString().trim();
        String userId = currentUser.getUid();

        // Валидация данных
        if (habitName.isEmpty()) {
            etHabitName.setError("Введите название привычки");
            return;
        }

        if (selectedDate.equals("Дата не выбрана")) {
            Toast.makeText(this, "Выберите дату", Toast.LENGTH_SHORT).show();
            return;
        }

        // Генерация уникального ID
        String habitId = databaseReference.child("habits").push().getKey();

        // Создание объекта привычки
        Habit newHabit = new Habit(habitId, habitName, selectedDate, userId);

        // Сохранение в Firebase
        databaseReference.child("habits").child(habitId).setValue(newHabit)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(AddHabitActivity.this, "Привычка сохранена!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(AddHabitActivity.this, "Ошибка сохранения: " +
                                task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Очистка ресурсов
        etHabitName = null;
        tvSelectedDate = null;
    }
}