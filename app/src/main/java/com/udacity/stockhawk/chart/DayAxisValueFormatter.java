package com.udacity.stockhawk.chart;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.SimpleDateFormat;


/**
 * Created by victoraldir on 28/12/2016.
 */

public class DayAxisValueFormatter implements IAxisValueFormatter {

    private SimpleDateFormat dt;
    private Float referenceTime;

    public DayAxisValueFormatter(Float referenceTime){
        dt = new SimpleDateFormat("MMM yyyy");
        this.referenceTime = referenceTime;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {

        java.sql.Timestamp timestamp = new java.sql.Timestamp((long) (value + referenceTime));

        return dt.format(timestamp);
    }
}
