package com.viktor.health.data;

public class DayEatingData extends EatingData {
    public Meal[] meals;

    public DayEatingData(Meal[] meals){
        this.meals = meals;
        calculateDayData();
    }

    private void calculateDayData()
    {
        for (Meal meal: meals) {
            this.painLevel+=meal.painLevel;
            this.calories+=meal.calories;
            this.carbs+=meal.carbs;
            this.fat+=meal.fat;
            this.protein+=meal.protein;
        }
        this.painLevel /= meals.length;
    }
}


