package com.example.parkmate;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Locale;

public class BookingspotActivity extends AppCompatActivity {
    private EditText editTextLotNo;
    private Button btnBook, btnCheckInTime, btnCheckOutTime;
    private TextView tvCheckInTime, tvCheckOutTime, tv_Fee,tvDate;
    private int mCheckInHour, mCheckInMinute, mCheckOutHour, mCheckOutMinute,mHour, mMinute;
    private CalendarView calendarView;
    private static final int feePerHour = 70;
    private DatabaseHelper databaseHelper;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookingspot);
        editTextLotNo = findViewById(R.id.editText_Lot_No);
        btnBook = findViewById(R.id.btnBook);
        tvCheckInTime = findViewById(R.id.tvCheckInTime);
        tvCheckOutTime = findViewById(R.id.tvCheckOutTime);
        tv_Fee = findViewById(R.id.tv_Fee);
        btnCheckInTime = findViewById(R.id.btnCheckInTime);
        btnCheckOutTime = findViewById(R.id.btnCheckOutTime);
        databaseHelper = new DatabaseHelper(this);
        calendarView = findViewById(R.id.calendarView);
        tvDate = findViewById(R.id.tv_date);
        // Set the initial date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        String selectedDate = formatDate(year, month, dayOfMonth);
        tvDate.setText(selectedDate);
        // Set an OnDateChangeListener to update the selected date
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String selectedDate = formatDate(year, month, dayOfMonth);
                tvDate.setText(selectedDate);
            }
        });
        // Set click listeners for the time picker buttons
        btnCheckInTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(tvCheckInTime);
                calFee();
            }
        });
        btnCheckOutTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(tvCheckOutTime);
                calFee();
            }
        });
        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lotNo = editTextLotNo.getText().toString();
                String date = tvDate.getText().toString();
                String checkInTime = tvCheckInTime.getText().toString();
                String checkOutTime = tvCheckOutTime.getText().toString();
                // Validate if all fields are not empty
                if (lotNo.isEmpty() || date.isEmpty() || checkInTime.isEmpty() || checkOutTime.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "All fields are required", Toast.LENGTH_SHORT).show();
                } else {
                    bookSpot();
                    startActivity(new Intent(BookingspotActivity.this, BookingsActivity.class));
                }
            }
        });
    }
    private String formatDate(int year, int month, int dayOfMonth) {
        // Months are zero-based, so increment the month by 1
        month += 1;
        return String.format(Locale.getDefault(), "%02d-%02d-%04d", dayOfMonth, month, year);
    }
    public void showTimePickerDialog(final TextView textView) {
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
                textView.setText(hourOfDay + ":" + minute);
                calFee();
            }
        }, mHour, mMinute, false);
        timePickerDialog.show();
    }
    public void calFee() {
        // Get the check-in and check-out times
        String checkInTime = tvCheckInTime.getText().toString();
        String checkOutTime = tvCheckOutTime.getText().toString();
        // Check if the check-in and check-out times are empty
        if (TextUtils.isEmpty(checkInTime) || TextUtils.isEmpty(checkOutTime)) {
            return;
        }
        // Split the check-in and check-out times into hour and minute components
        String[] checkInParts = checkInTime.split(":");
        mCheckInHour = Integer.parseInt(checkInParts[0]);
        mCheckInMinute = Integer.parseInt(checkInParts[1]);
        String[] checkOutParts = checkOutTime.split(":");
        mCheckOutHour = Integer.parseInt(checkOutParts[0]);
        mCheckOutMinute = Integer.parseInt(checkOutParts[1]);
        // Calculate the duration in hours and minutes
        int hourDiff = mCheckOutHour - mCheckInHour;
        int minuteDiff = mCheckOutMinute - mCheckInMinute;
        // Adjust the duration if the check-out time is earlier than the check-in time
        if (hourDiff < 0 || (hourDiff == 0 && minuteDiff < 0)) {
            hourDiff += 24;
        }
        if (minuteDiff < 0) {
            minuteDiff += 60;
            hourDiff--;
        }
        // Calculate the fee and display it
        float fee = hourDiff + (minuteDiff / 60.0f);
        fee *= feePerHour;
        tv_Fee.setText(String.format("%.2f", fee));
    }
    private void bookSpot() {
        // Get the lot number, date, check-in time, and check-out time
        String lotNo = editTextLotNo.getText().toString().trim();
        String date = tvDate.getText().toString();
        String checkInTime = tvCheckInTime.getText().toString();
        String checkOutTime = tvCheckOutTime.getText().toString();
        String fee =  tv_Fee.getText().toString();
        SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("Username", "");
        // Validate the input
        if (TextUtils.isEmpty(lotNo)  || TextUtils.isEmpty(checkInTime) || TextUtils.isEmpty(checkOutTime)) {
            return;
        }
        // Get an instance of the database for writing
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        // Check if the user already has a booking
        String query = "SELECT * FROM bookings WHERE Username = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username});
        // Create a new ContentValues object and put the data to be inserted
        ContentValues values = new ContentValues();
        values.put("Username", username);
        values.put("lotNo", lotNo);
        values.put("date", date);
        values.put("checkInTime", checkInTime);
        values.put("checkOutTime", checkOutTime);
        values.put("fee", fee);
        if (cursor.getCount() > 0) {
            // Update the existing row with the new data
            db.update("bookings", values, "Username = ?", new String[]{username});
        } else {
            // Insert a new row for the user
            db.insert("bookings", null, values);
        }
        // Close the database connection
        cursor.close();
        db.close();
    }
}



