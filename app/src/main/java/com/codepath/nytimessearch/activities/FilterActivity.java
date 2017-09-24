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

    private Button mButton;
    private Button mDate;
    private Spinner mDropdown;
    private CheckBox mChkFashion;
    private CheckBox mChkSports;
    private String beginDate;
    private String sortOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        mButton = (Button) findViewById(R.id.btn_apply);
        mDate = (Button) findViewById(R.id.btn_date);
        mDropdown = (Spinner) findViewById(R.id.spinner_order);
        CheckBox mChkArt = (CheckBox) findViewById(R.id.checkbox_arts);
        mChkFashion = (CheckBox) findViewById(R.id.checkbox_fashion);
        mChkSports = (CheckBox) findViewById(R.id.checkbox_sports);

        mButton.setOnClickListener(v -> {
            beginDate = mDate.getText().toString();
            sortOrder = mDropdown.getSelectedItem().toString();

            String mArt = "";
            if (mChkArt.isChecked()) {
                mArt = ("ARTS");

            }
            //Toast.makeText(FilterActivity.this, "Please" + beginDate + sortOrder + mArt, Toast.LENGTH_SHORT).show();

            //Send data to main activity
            Intent data = new Intent();
            // Pass relevant data back as a result
            data.putExtra("beginDate", beginDate);
            data.putExtra("sortOrder", sortOrder);
            data.putExtra("arts", mArt);
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

        Button btn = (Button) findViewById(R.id.btn_date);
        btn.setText(view.getMonth() + "/" + view.getDayOfMonth() + "/" + view.getYear());

    }

}

//    Intent i = new Intent(getApplicationContext(), ArticleActivity.class);
//            i.putExtra("article", Parcels.wrap(article));
//                startActivity(i);