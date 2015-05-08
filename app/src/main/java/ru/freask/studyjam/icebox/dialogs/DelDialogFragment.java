package ru.freask.studyjam.icebox.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import ru.freask.studyjam.icebox.MainActivity;
import ru.freask.studyjam.icebox.R;


/**
 * Created by Alexander.Kashin01 on 08.05.2015.
 */
public class DelDialogFragment extends DialogFragment{

    public static DelDialogFragment newInstance(long id) {
        DelDialogFragment f = new DelDialogFragment();
        Bundle args = new Bundle();
        args.putLong("id", id);
        f.setArguments(args);
        return f;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.deleteAlert)
                .setMessage(R.string.ayousure)
                .setCancelable(false)
                .setPositiveButton(R.string.todelete,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                MainActivity.delProduct(getArguments().getLong("id"));
                            }
                        })
                .setNegativeButton("Cancel", null);
        return (builder.create());

    }

    @Override
    public void onDismiss(DialogInterface unused) {
        super.onDismiss(unused);
    }
    @Override
    public void onCancel(DialogInterface unused) {
        super.onCancel(unused);
    }
}