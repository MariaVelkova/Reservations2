package bg.mentormate.academy.reservations.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.DatePicker;

import java.lang.reflect.Field;

import bg.mentormate.academy.reservations.common.Validator;

/**
 * Created by Maria on 2/23/2015.
 */
public class CustomDatePickerDialog  extends DatePickerDialog {
    private DatePicker datePicker;
    private final OnDateSetListener callback;
    public CustomDatePickerDialog(Context context, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth) {
        super(context, callBack, year, monthOfYear, dayOfMonth);
        this.callback = callBack;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (callback != null && datePicker != null) {
            datePicker.clearFocus();
            callback.onDateSet(datePicker, datePicker.getYear(),
                    datePicker.getMonth(), datePicker.getDayOfMonth());
        }
    }

    @Override
    protected void onStop() {
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        try {
            Class<?> classForid = Class.forName("com.android.internal.R$id");
            Field datePickerField = classForid.getField("datePicker");
            this.datePicker = (DatePicker) findViewById(datePickerField
                    .getInt(null));
            this.datePicker.setMinDate(Validator.getCurrentGMTTime() - 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
