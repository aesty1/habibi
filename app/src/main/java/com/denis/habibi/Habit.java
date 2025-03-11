package com.denis.habibi;

public class Habit {
    private String id; // Добавьте уникальный ID
    private String name;
    private String date;
    private String userId; // Добавьте ID пользователя

    // Конструктор по умолчанию необходим для Firebase
    public Habit() {}

    public Habit(String id, String name, String date, String userId) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.userId = userId;
    }

    // Добавьте геттеры и сеттеры для всех полей
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
}