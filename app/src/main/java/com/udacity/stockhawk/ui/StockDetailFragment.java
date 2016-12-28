package com.udacity.stockhawk.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.udacity.stockhawk.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class StockDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String SYMBOL_PARAM = "symbom_param";
    public static final String FRAGMENT_DETAIL_TAG = "fragment_detail";

    public StockDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_stock_detail, container, false);

        ButterKnife.bind(this, rootView);

        final LineChart mLineChart = (LineChart) rootView.findViewById(R.id.stock_chart);

        List<Entry> stockList = new ArrayList<>();

        stockList.add(new Entry(4f, 0));
        stockList.add(new Entry(8f, 1));
        stockList.add(new Entry(6f, 2));
        stockList.add(new Entry(12f, 3));
        stockList.add(new Entry(18f, 4));
        stockList.add(new Entry(9f, 5));

        LineDataSet dataSet = new LineDataSet(stockList, "Stock rater over time");

//        dataSet.setColor(R.color.colorAccent);
//        dataSet.setValueTextColor(R.color.colorPrimary);

        LineData lineData = new LineData(dataSet);
        mLineChart.setData(lineData);
//        mLineChart.invalidate(); // refresh


        Button btn = (Button) rootView.findViewById(R.id.btn_refresh);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLineChart.invalidate();
            }
        });


        Bundle arguments = getArguments();

        Toast.makeText(getActivity(),arguments.getParcelable(SYMBOL_PARAM).toString(), Toast.LENGTH_LONG).show();

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        mLineChart.invalidate(); // refresh
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
