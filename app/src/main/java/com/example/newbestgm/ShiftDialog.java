package com.example.newbestgm;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import java.util.Calendar;

public class ShiftDialog extends AppCompatDialogFragment implements DatePickerDialog.OnDateSetListener {
    private EditText editTextDate, editTextMoney;
    private ShiftDialogListener listener;
    private final String SHIFT = "Shift", CANCEL = "cancel", OK = "ok", IMPLIMENT_MSG = "must implement ShiftDialogListener",
    BACKSLASH = "/";

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.shift_dialog, null);
        builder.setView(view)
                .setTitle(SHIFT)
                .setNegativeButton(CANCEL, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton(OK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String date = editTextDate.getText().toString();
                        String money = editTextMoney.getText().toString();
                        listener.applyTexts(date,money);

                    }
                });
        editTextDate = view.findViewById(R.id.edit_date);
        editTextMoney = view.findViewById(R.id.edit_money);
        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        return builder.create();
    }

    private void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(),
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (ShiftDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + IMPLIMENT_MSG);
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = dayOfMonth + BACKSLASH + (month+1) + BACKSLASH + year;
        editTextDate.setText(date);
    }

    public interface ShiftDialogListener{
        void applyTexts(String date, String money);
    }
}
