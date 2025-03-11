package com.denis.habibi;
import org.json.JSONException;
import org.json.JSONObject;

public class Habit {
    private String name;
    private String date;
    private int iconResId;

    public Habit(String name, String date, int iconResId) {
        this.name = name;
        this.date = date;
        this.iconResId = iconResId;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public int getIconResId() {
        return iconResId;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", name);
        jsonObject.put("date", date);
        return jsonObject;
    }
}