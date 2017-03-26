package com.sr.masharef.masharef.voting;

/**
 * Created by welcome on 3/14/2017.
 */

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.sr.masharef.masharef.R;

import java.util.ArrayList;

public class Chartactivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        BarChart chart = (BarChart) findViewById(R.id.chart);

      /*  BarData data = new BarData(getXAxisValues(), getDataSet());
        chart.setData(data);
        chart.setDescription("My Chart");
        chart.animateXY(2000, 2000);
        chart.invalidate();*/
    }

    private ArrayList<BarDataSet> getDataSet() {
        ArrayList<BarDataSet> dataSets = null;

        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        BarEntry v1e1 = new BarEntry(110.000f, 0); // Jan
        valueSet1.add(v1e1);
        BarEntry v1e2 = new BarEntry(40.000f, 1); // Feb
        valueSet1.add(v1e2);
        BarEntry v1e3 = new BarEntry(60.000f, 2); // Mar
        valueSet1.add(v1e3);
        BarEntry v1e4 = new BarEntry(30.000f, 3); // Apr
        valueSet1.add(v1e4);
        BarEntry v1e5 = new BarEntry(90.000f, 4); // May
        valueSet1.add(v1e5);
        BarEntry v1e6 = new BarEntry(100.000f, 5); // Jun
        valueSet1.add(v1e6);

        ArrayList<BarEntry> valueSet2 = new ArrayList<>();
        BarEntry v2e1 = new BarEntry(150.000f, 0); // Jan
        valueSet2.add(v2e1);
        BarEntry v2e2 = new BarEntry(90.000f, 1); // Feb
        valueSet2.add(v2e2);
        BarEntry v2e3 = new BarEntry(120.000f, 2); // Mar
        valueSet2.add(v2e3);
        BarEntry v2e4 = new BarEntry(60.000f, 3); // Apr
        valueSet2.add(v2e4);
        BarEntry v2e5 = new BarEntry(20.000f, 4); // May
        valueSet2.add(v2e5);
        BarEntry v2e6 = new BarEntry(80.000f, 5); // Jun
        valueSet2.add(v2e6);

        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Brand 1");
        barDataSet1.setColor(Color.rgb(0, 155, 0));
        BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Brand 2");
        barDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);

        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        dataSets.add(barDataSet2);
        return dataSets;
    }

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("JAN");
        xAxis.add("FEB");
        xAxis.add("MAR");
        xAxis.add("APR");
        xAxis.add("MAY");
        xAxis.add("JUN");
        return xAxis;
    }
}

/*

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Typeface;
import android.os.Bundle;
        import android.support.v7.app.ActionBarActivity;

        import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
        import com.github.mikephil.charting.data.BarDataSet;
        import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.sr.masharef.R;

import java.util.ArrayList;
import java.util.List;

public class Chartactivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        BarChart chart = (BarChart) findViewById(R.id.chart);
        PieChart pieChart = (PieChart) findViewById(R.id.pieChart);

       // BarData data = new BarData(getDataSet());
      */
/*  chart.setData(data);
      //  chart.setDescription("My Chart");
        chart.animateXY(1000, 1000);
        chart.invalidate();*//*


        PieData pieData = new PieData(getPieDataSet());
        pieChart.setData(pieData);
        pieChart.animateXY(1000, 1000);
        pieChart.invalidate();
    }
    private IPieDataSet getPieDataSet() {
        IPieDataSet dataSets = null;
        ArrayList<PieEntry> valueSet2 = new ArrayList<>();
        PieEntry v2e1 = new PieEntry(10.000f, 0); // Jan
        valueSet2.add(v2e1);
        PieEntry v2e2 = new PieEntry(9.000f, 1); // Feb
        valueSet2.add(v2e2);
        PieEntry v2e3 = new PieEntry(12.000f, 2); // Mar
        valueSet2.add(v2e3);
        PieEntry v2e4 = new PieEntry(6.000f, 3); // Apr
        valueSet2.add(v2e4);
//        IPieDataSet pieDataSet = new PieDataSet(valueSet2, "Brand 2");
//        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSets = new IPieDataSet() {
            @Override
            public float getSliceSpace() {
                return 0;
            }

            @Override
            public boolean isAutomaticallyDisableSliceSpacingEnabled() {
                return false;
            }

            @Override
            public float getSelectionShift() {
                return 0;
            }

            @Override
            public PieDataSet.ValuePosition getXValuePosition() {
                return null;
            }

            @Override
            public PieDataSet.ValuePosition getYValuePosition() {
                return null;
            }

            @Override
            public int getValueLineColor() {
                return 0;
            }

            @Override
            public float getValueLineWidth() {
                return 0;
            }

            @Override
            public float getValueLinePart1OffsetPercentage() {
                return 0;
            }

            @Override
            public float getValueLinePart1Length() {
                return 0;
            }

            @Override
            public float getValueLinePart2Length() {
                return 0;
            }

            @Override
            public boolean isValueLineVariableLength() {
                return false;
            }

            @Override
            public float getYMin() {
                return 0;
            }

            @Override
            public float getYMax() {
                return 0;
            }

            @Override
            public float getXMin() {
                return 0;
            }

            @Override
            public float getXMax() {
                return 0;
            }

            @Override
            public int getEntryCount() {
                return 0;
            }

            @Override
            public void calcMinMax() {

            }

            @Override
            public void calcMinMaxY(float fromX, float toX) {

            }

            @Override
            public PieEntry getEntryForXValue(float xValue, float closestToY, DataSet.Rounding rounding) {
                return null;
            }

            @Override
            public PieEntry getEntryForXValue(float xValue, float closestToY) {
                return null;
            }

            @Override
            public List<PieEntry> getEntriesForXValue(float xValue) {
                return null;
            }

            @Override
            public PieEntry getEntryForIndex(int index) {
                return null;
            }

            @Override
            public int getEntryIndex(float xValue, float closestToY, DataSet.Rounding rounding) {
                return 0;
            }

            @Override
            public int getEntryIndex(PieEntry e) {
                return 0;
            }

            @Override
            public int getIndexInEntries(int xIndex) {
                return 0;
            }

            @Override
            public boolean addEntry(PieEntry e) {
                return false;
            }

            @Override
            public void addEntryOrdered(PieEntry e) {

            }

            @Override
            public boolean removeFirst() {
                return false;
            }

            @Override
            public boolean removeLast() {
                return false;
            }

            @Override
            public boolean removeEntry(PieEntry e) {
                return false;
            }

            @Override
            public boolean removeEntryByXValue(float xValue) {
                return false;
            }

            @Override
            public boolean removeEntry(int index) {
                return false;
            }

            @Override
            public boolean contains(PieEntry entry) {
                return false;
            }

            @Override
            public void clear() {

            }

            @Override
            public String getLabel() {
                return null;
            }

            @Override
            public void setLabel(String label) {

            }

            @Override
            public YAxis.AxisDependency getAxisDependency() {
                return null;
            }

            @Override
            public void setAxisDependency(YAxis.AxisDependency dependency) {

            }

            @Override
            public List<Integer> getColors() {
                return null;
            }

            @Override
            public int getColor() {
                return 0;
            }

            @Override
            public int getColor(int index) {
                return 0;
            }

            @Override
            public boolean isHighlightEnabled() {
                return false;
            }

            @Override
            public void setHighlightEnabled(boolean enabled) {

            }

            @Override
            public void setValueFormatter(IValueFormatter f) {

            }

            @Override
            public IValueFormatter getValueFormatter() {
                return null;
            }

            @Override
            public boolean needsFormatter() {
                return false;
            }

            @Override
            public void setValueTextColor(int color) {

            }

            @Override
            public void setValueTextColors(List<Integer> colors) {

            }

            @Override
            public void setValueTypeface(Typeface tf) {

            }

            @Override
            public void setValueTextSize(float size) {

            }

            @Override
            public int getValueTextColor() {
                return 0;
            }

            @Override
            public int getValueTextColor(int index) {
                return 0;
            }

            @Override
            public Typeface getValueTypeface() {
                return null;
            }

            @Override
            public float getValueTextSize() {
                return 0;
            }

            @Override
            public Legend.LegendForm getForm() {
                return null;
            }

            @Override
            public float getFormSize() {
                return 0;
            }

            @Override
            public float getFormLineWidth() {
                return 0;
            }

            @Override
            public DashPathEffect getFormLineDashEffect() {
                return null;
            }

            @Override
            public void setDrawValues(boolean enabled) {

            }

            @Override
            public boolean isDrawValuesEnabled() {
                return false;
            }

            @Override
            public void setVisible(boolean visible) {

            }

            @Override
            public boolean isVisible() {
                return false;
            }
        };
        dataSets.addEntry(v2e1);
        dataSets.addEntry(v2e2);
        dataSets.addEntry(v2e3);
        dataSets.addEntry(v2e4);


        return dataSets;
    }
    private ArrayList<IBarDataSet> getDataSet() {
        ArrayList<IBarDataSet> dataSets = null;
      */
/*  ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        BarEntry v1e1 = new BarEntry(110.000f, 0); // Jan
        valueSet1.add(v1e1);
        BarEntry v1e2 = new BarEntry(40.000f, 1); // Feb
        valueSet1.add(v1e2);
        BarEntry v1e3 = new BarEntry(60.000f, 2); // Mar
        valueSet1.add(v1e3);
        BarEntry v1e4 = new BarEntry(30.000f, 3); // Apr
        valueSet1.add(v1e4);
        BarEntry v1e5 = new BarEntry(90.000f, 4); // May
        valueSet1.add(v1e5);
        BarEntry v1e6 = new BarEntry(100.000f, 5); // Jun
        valueSet1.add(v1e6);*//*


        ArrayList<BarEntry> valueSet2 = new ArrayList<>();
        BarEntry v2e1 = new BarEntry(10.000f, 0); // Jan
        valueSet2.add(v2e1);
        BarEntry v2e2 = new BarEntry(9.000f, 1); // Feb
        valueSet2.add(v2e2);
        BarEntry v2e3 = new BarEntry(12.000f, 2); // Mar
        valueSet2.add(v2e3);
        BarEntry v2e4 = new BarEntry(6.000f, 3); // Apr
        valueSet2.add(v2e4);
      */
/*  BarEntry v2e5 = new BarEntry(20.000f, 4); // May
        valueSet2.add(v2e5);
        BarEntry v2e6 = new BarEntry(80.000f, 5); // Jun
        valueSet2.add(v2e6);*//*


//        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Brand 1");
//        barDataSet1.setColor(Color.rgb(0, 155, 0));
        BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Brand 2");
        barDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);

        dataSets = new ArrayList<>();
     //   dataSets.add(barDataSet1);
        dataSets.add(barDataSet2);
        return dataSets;
    }

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("JAN");
        xAxis.add("FEB");
        xAxis.add("MAR");
        xAxis.add("APR");
        xAxis.add("MAY");
        xAxis.add("JUN");
        return xAxis;
    }
}*/
