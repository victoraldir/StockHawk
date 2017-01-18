package com.udacity.stockhawk.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextClock;
import android.widget.TextView;

import com.udacity.stockhawk.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.udacity.stockhawk.ui.DetailFragment.FRAGMENT_DETAIL_TAG;
import static com.udacity.stockhawk.ui.DetailFragment.SYMBOL_PARAM;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) public Toolbar toolbar;
    @BindView(R.id.change) TextView stockChange;
    @BindView(R.id.timezone) TextClock timeZone;
    @BindView(R.id.stockPrice) TextView stockPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeActionContentDescription(R.string.cd_up_navigator_button);

        if (savedInstanceState == null) {

            Bundle arguments = new Bundle();
            arguments.putParcelable(SYMBOL_PARAM,
                    getIntent().getData());

            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment, fragment, FRAGMENT_DETAIL_TAG)
                    .commit();
        }
    }

}
