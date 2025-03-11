package com.denis.habibi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class HabitAdapter extends BaseAdapter {

    private Context context;
    private List<Habit> habitList;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;

    public HabitAdapter(Context context, List<Habit> habitList) {
        this.context = context;
        this.habitList = habitList;
        this.databaseReference = FirebaseDatabase.getInstance().getReference("habits");
        this.currentUser = FirebaseAuth.getInstance().getCurrentUser();
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
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.habit_item, parent, false);
            holder = new ViewHolder();
            holder.habitName = convertView.findViewById(R.id.habitName);
            holder.habitDate = convertView.findViewById(R.id.habitDate);
            holder.habitIcon = convertView.findViewById(R.id.habitIcon);
            holder.deleteButton = convertView.findViewById(R.id.deleteButton);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Habit habit = habitList.get(position);

        holder.habitName.setText(habit.getName());
        holder.habitDate.setText(habit.getDate());
        holder.habitIcon.setImageResource(getIconResource(habit.getName()));

        // Удаление привычки
        holder.deleteButton.setOnClickListener(v -> {
            if (currentUser != null && habit.getUserId().equals(currentUser.getUid())) {
                deleteHabit(habit.getId(), position);
            } else {
                Toast.makeText(context, "Нельзя удалить чужую привычку", Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }

    private void deleteHabit(String habitId, int position) {
        databaseReference.child(habitId).removeValue()
                .addOnSuccessListener(aVoid -> {
                    habitList.remove(position);
                    notifyDataSetChanged();
                    Toast.makeText(context, "Привычка удалена", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(context, "Ошибка удаления: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    // Метод для обновления списка из Firebase
    public void updateData(List<Habit> newHabits) {
        habitList.clear();
        habitList.addAll(newHabits);
        notifyDataSetChanged();
    }

    private int getIconResource(String habitName) {
        // Добавьте свою логику для иконок
        switch (habitName.toLowerCase()) {
            case "спорт":
                return R.drawable.exer;
            case "чтение":
                return R.drawable.re;
            case "вода":
                return R.drawable.vac;
            default:
                return R.drawable.pen;
        }
    }

    private static class ViewHolder {
        TextView habitName;
        TextView habitDate;
        ImageView habitIcon;
        ImageView deleteButton;
    }

    // Метод для инициализации слушателя изменений
    public void initializeDatabaseListener() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                habitList.clear();
                for (DataSnapshot habitSnapshot : snapshot.getChildren()) {
                    Habit habit = habitSnapshot.getValue(Habit.class);
                    if (habit != null && habit.getUserId().equals(currentUser.getUid())) {
                        habitList.add(habit);
                    }
                }
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Ошибка загрузки данных: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}