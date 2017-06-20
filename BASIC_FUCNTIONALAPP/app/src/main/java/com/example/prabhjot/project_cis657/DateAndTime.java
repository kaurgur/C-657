package com.example.prabhjot.project_cis657;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import com.borax12.materialdaterangepicker.date.DatePickerDialog;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;



public class DateAndTime extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    @BindView(R.id.dateview) TextView dateview;


    private DateTime select_date;
    private DatePickerDialog dpDialog;
    private String hour_value = "00";
    private String min_value = "00";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_and_time);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DateTime today = DateTime.now();
        dpDialog = DatePickerDialog.newInstance(this, today.getYear(), today.getMonthOfYear() - 1, today.getDayOfMonth());


        dateview.setText(formatted(today));

        select_date = today;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("hour_value", hour_value);
                intent.putExtra("min_value", min_value);
                intent.putExtra("date", select_date);
                setResult(MainScreenActivity.Date_and_time_result,intent);
                finish();
            }
        });


        Spinner spinnerhour = (Spinner) findViewById(R.id.hour_value);
        ArrayAdapter<CharSequence> adapterDistance = ArrayAdapter.createFromResource(this,
                R.array.hour, android.R.layout.simple_spinner_item);
        adapterDistance.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerhour.setAdapter(adapterDistance);

        spinnerhour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                hour_value = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        Spinner spinnerminutes  = (Spinner) findViewById(R.id.min_value);
        ArrayAdapter<CharSequence> adapterBearing = ArrayAdapter.createFromResource(this,
                R.array.minute, android.R.layout.simple_spinner_item);
        adapterBearing.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerminutes.setAdapter(adapterBearing);
        spinnerminutes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                min_value = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }



    @OnClick(R.id.dateview)
    public void datePressed() {
        dpDialog.show(getFragmentManager(), "daterangedialog");
    }


    private String formatted(DateTime d) {
        return d.monthOfYear().getAsShortText(Locale.getDefault()) + " " +
                d.getDayOfMonth() + ", " + d.getYear();
    }
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {
        select_date = new DateTime(year, monthOfYear + 1, dayOfMonth, 0, 0);

        dateview.setText(formatted(select_date));

    }



}




