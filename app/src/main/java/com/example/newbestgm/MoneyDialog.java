package com.example.newbestgm;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class MoneyDialog extends AppCompatDialogFragment {
    private EditText editTextMoney;
    private MoneyDialogListener listener;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private final String TITLE = "Add money to shift", CANCEL = "cancel", OK = "ok",
            MONEY_DIALOG = "must implement MoneyDialogListener";

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.money_dialog, null);
        builder.setView(view).setTitle(TITLE).setPositiveButton(OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    String money = editTextMoney.getText().toString();
                        listener.applyTexts(money);
                } catch (Exception ex) {
                }
            }
        });
        editTextMoney = view.findViewById(R.id.edit_money);
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (MoneyDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + MONEY_DIALOG);
        }
    }

    public interface MoneyDialogListener {
        void applyTexts(String money);
    }


}