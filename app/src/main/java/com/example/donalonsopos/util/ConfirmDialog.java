package com.example.donalonsopos.util;

import android.app.AlertDialog;
import android.content.Context;
import com.example.donalonsopos.R;

public class ConfirmDialog {

    private final Context context;

    // Constructor
    public ConfirmDialog(Context context) {
        this.context = context;
    }

    // Método para mostrar el cuadro de diálogo de confirmación
    public void showConfirmationDialog(String title, String message, final Runnable onConfirm) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("Sí", (dialog, which) -> {
                    // Acción de confirmación
                    onConfirm.run();
                    dialog.dismiss();
                })
                .setNegativeButton("No", (dialog, which) -> {
                    // Cierra el diálogo
                    dialog.dismiss();
                });

        // Crear el diálogo
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(dlg -> {
            // Asegúrate de que no haya fugas de memoria
            dialog.setOnDismissListener(dlg2 -> {
                // Manejo adicional si es necesario
            });
        });

        // Mostrar el diálogo
        dialog.show();
    }
}
