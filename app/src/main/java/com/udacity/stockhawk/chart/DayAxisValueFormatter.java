package com.udacity.stockhawk.chart;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.SimpleDateFormat;


/**
 * Created by victoraldir on 28/12/2016.
 */

public class DayAxisValueFormatter implements IAxisValueFormatter {

    SimpleDateFormat dt;

    public DayAxisValueFormatter(){
        dt = new SimpleDateFormat("yyyyy-mm-dd");
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {

        java.sql.Timestamp timestamp = new java.sql.Timestamp((long)value);

        return dt.format(timestamp);
    }
}
