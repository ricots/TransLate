package by.vshkl.translate.utilities;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import by.vshkl.translate.R;
import by.vshkl.translate.listeners.DeleteConfirmationListener;
import by.vshkl.translate.listeners.StopEditListener;
import by.vshkl.translate.listeners.StopsDialogListener;

public class DialogHelper {

    public static void showEditStopDialog(AppCompatActivity activity, final String stopUrl, String stopName,
                                          String stopDirection, final StopEditListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_stop_edit, null);
        builder.setView(dialogView);

        final EditText etName = (EditText) dialogView.findViewById(R.id.et_name);
        final EditText etDirection = (EditText) dialogView.findViewById(R.id.et_direction);

        etName.setText(stopName);
        etDirection.setText(stopDirection);

        builder.setTitle(activity.getString(R.string.dialog_stop_edit_title));
        builder.setPositiveButton(activity.getString(R.string.dialog_stop_edit_button_positive),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onStopEdited(stopUrl, etName.getText().toString(), etDirection.getText().toString());
                    }
                });
        builder.setNegativeButton(activity.getString(R.string.dialog_stop_edit_button_negative),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

        builder.create().show();
    }

    public static void showStopsDialog(AppCompatActivity activity, final StopsDialogListener listener,
                                       final int stopPosition) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_stops, null);
        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();

        final TextView tvEdit = (TextView) dialogView.findViewById(R.id.tv_edit);
        final TextView tvDelete = (TextView) dialogView.findViewById(R.id.tv_delete);

        tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                listener.onStopEdit(stopPosition);
            }
        });

        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                listener.onStopDelete(stopPosition);
            }
        });

        dialog.show();
    }

    public static void showDeleteConfirmationDialog(AppCompatActivity activity, final DeleteConfirmationListener listener,
                                                    final int stopPosition) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.dialog_stop_delete_title);
        builder.setPositiveButton(R.string.dialog_stop_delete_button_positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onDeleteConfirmed(stopPosition);
            }
        });
        builder.setNegativeButton(R.string.dialog_stop_delete_button_negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create().show();
    }
}
