package com.denis.habibi;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.BaseAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.List;

public class HabitAdapter extends BaseAdapter {

    private Context context;
    private List<Habit> habitList;
    private SharedPreferences sharedPreferences;

    public HabitAdapter(Context context, List<Habit> habitList) {
        this.context = context;
        this.habitList = habitList;
        this.sharedPreferences = context.getSharedPreferences("HabitPrefs", Context.MODE_PRIVATE);
    }

    @Override
    public int getCount() {
        return habitList.size();
    }

    @Override
    public Object getItem(int position) {
        return habitList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.habit_item, parent, false);
        }

        Habit habit = habitList.get(position);

        TextView habitName = convertView.findViewById(R.id.habitName);
        TextView habitDate = convertView.findViewById(R.id.habitDate);
        ImageView habitIcon = convertView.findViewById(R.id.habitIcon);
        ImageView deleteButton = convertView.findViewById(R.id.deleteButton);

        habitName.setText(habit.getName());
        habitDate.setText(habit.getDate());
        habitIcon.setImageResource(habit.getIconResId());

        // Обработчик клика на кнопку удаления
        deleteButton.setOnClickListener(v -> {
            habitList.remove(position);
            saveHabits(); // Сохраняем изменения
            notifyDataSetChanged();
        });

        return convertView;
    }

    // Метод сохранения списка привычек
    private void saveHabits() {
        JSONArray jsonArray = new JSONArray();
        for (Habit habit : habitList) {
            try {
                jsonArray.put(habit.toJson());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        sharedPreferences.edit().putString("habits", jsonArray.toString()).apply();
    }
}