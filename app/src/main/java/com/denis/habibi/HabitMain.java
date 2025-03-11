package com.denis.habibi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class HabitMain extends AppCompatActivity {

    private ArrayList<Habit> habitList;
    private HabitAdapter habitAdapter;
    private ListView listViewHabits;
    private SharedPreferences sharedPreferences;
    private static final String HABITS_KEY = "habits";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        listViewHabits = findViewById(R.id.listViewHabits);
        sharedPreferences = getSharedPreferences("HabitPrefs", MODE_PRIVATE);

        loadHabits();

        habitAdapter = new HabitAdapter(this, habitList);
        listViewHabits.setAdapter(habitAdapter);

        Button addHabitButton = findViewById(R.id.addHabitButton);
        addHabitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HabitMain.this, AddHabitActivity.class));
            }
        });
    }

    private void loadHabits() {
        habitList = new ArrayList<>();
        String habitsJson = sharedPreferences.getString(HABITS_KEY, "[]");
        try {
            JSONArray jsonArray = new JSONArray(habitsJson);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonHabit = jsonArray.getJSONObject(i);
                habitList.add(new Habit(
                        jsonHabit.getString("name"),
                        jsonHabit.getString("date"),
                        R.drawable.exer
                ));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadHabits();
        habitAdapter = new HabitAdapter(this, habitList);
        listViewHabits.setAdapter(habitAdapter);
}}