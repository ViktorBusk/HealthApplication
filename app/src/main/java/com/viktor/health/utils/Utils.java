package com.viktor.health.utils;

import android.view.View;
import android.view.ViewTreeObserver;

public abstract class Utils {

    public static float map(float value, float start1, float stop1, float start2, float stop2) {
        float outgoing = start2 + (stop2 - start2) * ((value - start1) / (stop1 - start1));
        return outgoing;
    }
    public static float getDistance(float x1, float y1, float x2, float y2) {
        return (float)Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
    }
}
