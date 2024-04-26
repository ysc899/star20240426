package kr.co.seesoft.nemo.starnemoapp.ui.dialog;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import java.util.Calendar;

import androidx.fragment.app.DialogFragment;
import kr.co.seesoft.nemo.starnemoapp.R;


public class HourMinutePickerDialog extends DialogFragment {

    private DatePickerDialog.OnDateSetListener listener;
    public Calendar cal = Calendar.getInstance();

    public void setListener(DatePickerDialog.OnDateSetListener listener) {
        this.listener = listener;
    }

    private Button btnConfirm, btnCancel;

    private int hour;
    private int minute;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialog = inflater.inflate(R.layout.dialog_yymm_picker, null);

        btnConfirm = dialog.findViewById(R.id.btn_confirm);
        btnCancel = dialog.findViewById(R.id.btn_cancel);

        final NumberPicker minutePicker = (NumberPicker) dialog.findViewById(R.id.picker_month);
        final NumberPicker hourPicker = (NumberPicker) dialog.findViewById(R.id.picker_year);

        btnCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                HourMinutePickerDialog.this.getDialog().cancel();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                listener.onDateSet(null, hourPicker.getValue(), minutePicker.getValue(), 0);
                HourMinutePickerDialog.this.getDialog().cancel();
            }
        });

        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(59);
        minutePicker.setValue(minute);

        hourPicker.setMinValue(0);
        hourPicker.setMaxValue(23);
        hourPicker.setValue(hour);

        builder.setView(dialog);

        return builder.create();
    }

    public void setValue(int hour ,  int minute)
    {
        this.hour = hour;
        this.minute = minute;
    }
}
