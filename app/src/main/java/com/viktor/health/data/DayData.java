package com.viktor.health.data;

import androidx.lifecycle.Lifecycle;

public class DayData {
    public DayEatingData dayEatingData;
    public DayLifeData lifeData;

    public DayData(DayEatingData dayEatingData, DayLifeData lifeData)
    {
        this.dayEatingData = dayEatingData;
        this.lifeData = lifeData;
    }


}
