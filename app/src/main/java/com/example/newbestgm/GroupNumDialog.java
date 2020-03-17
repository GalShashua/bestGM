package com.example.newbestgm;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import java.util.Random;

public class GroupNumDialog extends AppCompatDialogFragment {
    private Random rand;
    private int bigNum = 9999, littleNum = 1000;
    private final String NEW_ID = "New Group ID" , OK = "ok", SHOW_ID = "This is your group id: ";

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        rand = new Random();
        int theNum = rand.nextInt(bigNum - littleNum) + bigNum;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(NEW_ID).setMessage(SHOW_ID + theNum).
                setPositiveButton(OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return builder.create();
    }
}
