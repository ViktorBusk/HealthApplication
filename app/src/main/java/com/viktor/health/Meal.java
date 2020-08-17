package com.viktor.health;

import android.widget.TimePicker;

public class Meal extends EatingData {
    String description;
    TimePicker time;

    public Meal(String description, TimePicker time, float painLevel, int calories, int carbs, int fat, int protein) {
        this.description = description;
        this.time = time;
        this.painLevel = painLevel;
        this.calories = calories;
        this.carbs = carbs;
        this.fat = fat;
        this.protein = protein;
    }
}
