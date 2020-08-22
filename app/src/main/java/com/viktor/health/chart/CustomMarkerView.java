package com.viktor.health.chart;

import android.content.Context;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import com.viktor.health.R;
import com.viktor.health.data.DayData;
import com.viktor.health.utils.Utils;
import com.viktor.health.data.DayEatingData;

public class CustomMarkerView extends MarkerView {

    private TextView tvContent;
    private MPPointF drawingPoint;
    public DayData dayData;

    public CustomMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        // find your layout components
        tvContent = (TextView) findViewById(R.id.markerBackground);
        tvContent.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        drawingPoint = new MPPointF(-1, -1);
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        this.setY(e.getY());
        this.setX(e.getX());
        drawingPoint.x = highlight.getDrawX();
        drawingPoint.y = highlight.getDrawY();
        dayData = (DayData) e.getData();
        super.refreshContent(e, highlight);
    }

    private MPPointF mOffset;

    @Override
    public MPPointF getOffset() {

        if (mOffset == null) {
            // center the marker horizontally and vertically
            mOffset = new MPPointF(-(getWidth() / 2), -getHeight());
        }

        return mOffset;
    }

    public final MPPointF getDrawingPoint() {
        return drawingPoint;
    }

    public void setDrawingPoint(float x, float y) {
        drawingPoint.x = x;
        drawingPoint.y = y;
    }
}