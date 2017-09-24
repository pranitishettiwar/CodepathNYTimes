package com.codepath.nytimessearch.activities;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Spinner;

import com.codepath.nytimessearch.R;
import com.codepath.nytimessearch.fragments.DatePickerFragment;

public class FilterActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    Button mButton, mDate;
    Spinner mDropdown;
    CheckBox mChkFashion, mChkSports, mChkArt;
    String beginDate, sortOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        mButton = (Button) findViewById(R.id.btn_apply);
        mDate = (Button) findViewById(R.id.btn_date);
        mDropdown = (Spinner) findViewById(R.id.spinner_order);
        mChkArt = (CheckBox) findViewById(R.id.checkbox_arts);
        mChkFashion = (CheckBox) findViewById(R.id.checkbox_fashion);
        mChkSports = (CheckBox) findViewById(R.id.checkbox_sports);

        mButton.setOnClickListener(v -> {
            sortOrder = mDropdown.getSelectedItem().toString().toLowerCase();

            Boolean arts = mChkArt.isChecked();
            Boolean fashion = mChkFashion.isChecked();
            Boolean sports = mChkSports.isChecked();

            //Send data to main activity
            Intent data = new Intent();
            // Pass relevant data back as a result
            data.putExtra("beginDate", beginDate);
            data.putExtra("sortOrder", sortOrder);
            data.putExtra("arts", arts);
            data.putExtra("fashion", fashion);
            data.putExtra("sports", sports);
            data.putExtra("code", 200); // ints work too
            // Activity finished ok, return the data
            setResult(RESULT_OK, data); // set result code and bundle data for response
            finish(); // closes the activity, pass data to parent

        });

    }

    // attach to an onclick handler to show the date picker
    public void showDatePickerDialog(View v) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    // handle the date selected
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        // store the values selected into a Calendar instance
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        int month = Integer.valueOf(view.getMonth()) + 1;
        beginDate = String.valueOf(year) + String.valueOf(month) + String.valueOf(dayOfMonth);

        Button btn = (Button) findViewById(R.id.btn_date);
        btn.setText(view.getYear() + "/" + String.valueOf(month) + "/" + view.getDayOfMonth());

    }

}
