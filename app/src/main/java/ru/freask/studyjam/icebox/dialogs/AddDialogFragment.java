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
public class AddDialogFragment extends DialogFragment implements
        DialogInterface.OnClickListener {
    private View layout;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        layout = inflater.inflate(R.layout.add_dialog, (ViewGroup) getActivity().findViewById(R.id.add_dialog_linearlayout));
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.product_add);
        builder.setView(layout);
        builder.setPositiveButton("Add", this);
        builder.setNegativeButton("Cancel", null);
        return (builder.create());

    }
    @Override
    public void onClick(DialogInterface dialog, int which) {
        final EditText name = (EditText) layout.findViewById(R.id.add_text);
        final EditText count = (EditText) layout.findViewById(R.id.add_count);
        final EditText like_count = (EditText) layout.findViewById(R.id.add_like_count);

        String name_str = name.getText().toString();
        String count_str = count.getText().toString();
        String like_count_str = like_count.getText().toString();

        if (!name_str.equals("") && !count_str.equals("") && !like_count_str.equals(""))
            ((MainActivity)getActivity()).addProduct(name_str, Integer.parseInt(count_str), Integer.parseInt(like_count_str));
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