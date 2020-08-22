package com.viktor.health.data;

import android.widget.TimePicker;

import java.sql.Time;

public class DayLifeData {

    public int exercise;
    public String exerciseDescription;
    public float mental;
    public int bowelMovements;
    public String bowelMovementDescription;
    public TimePicker hoursOfSleep;

    public DayLifeData(int exercise, String exerciseDescription, float mental,
                       int bowelMovements, String bowelMovementDescription,
                       TimePicker hoursOfSleep){
        this.exercise = exercise;
        this.exerciseDescription = exerciseDescription;
        this.mental = mental;
        this.bowelMovements = bowelMovements;
        this.bowelMovementDescription = bowelMovementDescription;
        this.hoursOfSleep = hoursOfSleep;
    }

    public DayLifeData()
    {
        this.exercise = -1;
        this.exerciseDescription = "none";
        this.mental = -1;
        this.bowelMovements = -1;
        this.bowelMovementDescription = "none";
        this.hoursOfSleep = null;
    }

}
