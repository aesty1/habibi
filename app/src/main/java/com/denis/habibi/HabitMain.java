package com.denis.habibi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HabitMain extends AppCompatActivity {

    private ListView habitsListView;
    private HabitAdapter habitAdapter;
    private List<Habit> habitList;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        // Инициализация Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("habits");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Проверка авторизации
        if (currentUser == null) {
            Toast.makeText(this, "Требуется авторизация!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        // Инициализация UI
        habitsListView = findViewById(R.id.habitsListView);
        TextView addHabitButton = findViewById(R.id.addHabitButton);
        TextView logoutButton = findViewById(R.id.logoutButton);

        habitList = new ArrayList<>();
        habitAdapter = new HabitAdapter(this, habitList);
        habitsListView.setAdapter(habitAdapter);

        // Загрузка данных
        loadHabits();

        // Обработчики событий
        addHabitButton.setOnClickListener(v -> {
            startActivity(new Intent(HabitMain.this, AddHabitActivity.class));
        });

        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(HabitMain.this, LoginActivity.class));
            finish();
        });

        habitsListView.setOnItemClickListener((parent, view, position, id) -> {
            Habit selectedHabit = habitList.get(position);
            Toast.makeText(HabitMain.this,
                    "Выбрана привычка: " + selectedHabit.getName(),
                    Toast.LENGTH_SHORT).show();
        });
    }

    private void loadHabits() {
        databaseReference.orderByChild("userId").equalTo(currentUser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        habitList.clear();
                        for (DataSnapshot habitSnapshot : snapshot.getChildren()) {
                            Habit habit = habitSnapshot.getValue(Habit.class);
                            if (habit != null) {
                                habitList.add(habit);
                            }
                        }
                        habitAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(HabitMain.this,
                                "Ошибка загрузки данных: " + error.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Обновление данных при возвращении на экран
        if (habitAdapter != null) {
            habitAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Очистка ресурсов
        habitsListView = null;
        habitAdapter = null;
        habitList = null;
    }
}