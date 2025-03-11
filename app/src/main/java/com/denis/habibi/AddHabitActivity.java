package com.denis.habibi;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Calendar;

public class AddHabitActivity extends AppCompatActivity {

    private EditText etHabitName;
    private TextView tvSelectedDate;
    private SharedPreferences sharedPreferences;
    private static final String HABITS_KEY = "habits";
    private String selectedDate = "Дата не выбрана";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_habit);

        TextView back = findViewById(R.id.backBtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddHabitActivity.this, HabitMain.class);
                startActivity(intent);
            }
        });


        etHabitName = findViewById(R.id.etHabitName);
        tvSelectedDate = findViewById(R.id.tvSelectedDate);
        Button btnPickDate = findViewById(R.id.btnPickDate);
        Button btnSaveHabit = findViewById(R.id.btnSaveHabit);
        sharedPreferences = getSharedPreferences("HabitPrefs", MODE_PRIVATE);

        // Открытие календаря
        btnPickDate.setOnClickListener(v -> showDatePicker());

        // Сохранение привычки
        btnSaveHabit.setOnClickListener(v -> saveHabit());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) -> {
            selectedDate = selectedDay + "." + (selectedMonth + 1) + "." + selectedYear;
            tvSelectedDate.setText(selectedDate);
        }, year, month, day);
        datePickerDialog.show();
    }

    private void saveHabit() {
        String habitName = etHabitName.getText().toString().trim();

        if (habitName.isEmpty() || selectedDate.equals("Дата не выбрана")) {
            Toast.makeText(this, "Заполните все поля!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            JSONArray jsonArray = new JSONArray(sharedPreferences.getString(HABITS_KEY, "[]"));
            JSONObject jsonHabit = new JSONObject();
            jsonHabit.put("name", habitName);
            jsonHabit.put("date", selectedDate);
            jsonArray.put(jsonHabit);

            sharedPreferences.edit().putString(HABITS_KEY, jsonArray.toString()).apply();
            Toast.makeText(this, "Привычка добавлена!", Toast.LENGTH_SHORT).show();
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}