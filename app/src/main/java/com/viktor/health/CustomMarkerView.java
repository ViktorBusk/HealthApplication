package com.viktor.health;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

public class CustomMarkerView extends MarkerView {

    private TextView tvContent;
    private MPPointF drawingPoint;
    private MPPointF size;

    public CustomMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        // find your layout components
        tvContent = (TextView) findViewById(R.id.tvContent);
        tvContent.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        drawingPoint = new MPPointF(-1, -1);
        size = new MPPointF(tvContent.getMeasuredWidth(), tvContent.getMeasuredHeight());
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        drawingPoint.x = highlight.getDrawX();
        drawingPoint.y = highlight.getDrawY();
        DayEatingData dayEatingData = (DayEatingData)e.getData();
        tvContent.setText("Pain: " +  dayEatingData.painLevel + "\n" +
                        "Calories: " + dayEatingData.calories + "\n" +
                        "Carbs: " + dayEatingData.carbs + "\n" +
                        "Fat: " + dayEatingData.fat + "\n" +
                        "Protein: " + dayEatingData.protein);
        super.refreshContent(e, highlight);
    }

    private MPPointF mOffset;

    @Override
    public MPPointF getOffset() {

        if(mOffset == null) {
            // center the marker horizontally and vertically
            mOffset = new MPPointF(-(getWidth() / 2), -getHeight());
        }

        return mOffset;
    }
    public final MPPointF getDrawingPoint() {
        return drawingPoint;
    }
    public void setDrawingPoint(float x, float y) {
        drawingPoint.x = x;drawingPoint.y = y;
    }
    public final MPPointF getSize() {
        return size;
    }
}