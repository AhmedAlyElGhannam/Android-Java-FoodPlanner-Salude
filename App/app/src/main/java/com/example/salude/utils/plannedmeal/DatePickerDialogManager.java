package com.example.salude.utils.plannedmeal;

import android.app.DatePickerDialog;
import android.content.Context;

import java.util.Calendar;

public class DatePickerDialogManager {
    public interface DatePickerListener {
        void onDateSelected(String date);
    }

    public static void showDatePickerDialog(Context context, DatePickerListener listener) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create min and max dates (today and today + 6 days)
        Calendar minDate = Calendar.getInstance();
        Calendar maxDate = Calendar.getInstance();
        maxDate.add(Calendar.DAY_OF_MONTH, 6);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                context,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String selectedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
                    listener.onDateSelected(selectedDate);
                },
                year, month, day);

        // Set min and max dates
        datePickerDialog.getDatePicker().setMinDate(minDate.getTimeInMillis());
        datePickerDialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());

        datePickerDialog.show();
    }
}
