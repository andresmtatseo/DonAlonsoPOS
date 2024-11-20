package com.example.donalonsopos.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class Utils {

    // Valida si un campo es obligatorio (no está vacío)
    public static boolean validateRequiredField(EditText editText, String errorMessage) {
        if (editText.getText().toString().trim().isEmpty()) {
            editText.setError(errorMessage);
            return false;
        }
        return true;
    }

    // Valida si un número es positivo
    public static boolean validatePositiveNumberField(EditText editText, String errorMessage) {
        try {
            double value = Double.parseDouble(editText.getText().toString().trim());
            if (value <= 0) {
                editText.setError(errorMessage);
                return false;
            }
        } catch (NumberFormatException e) {
            editText.setError(errorMessage);
            return false;
        }
        return true;
    }

    public static boolean validatePhoneNumberField(EditText editText, String errorMessage) {
        String phone = editText.getText().toString().trim();

        // Elimina caracteres no numéricos
        phone = phone.replaceAll("[^0-9+]", "");

        if (!phone.matches("^\\+?[0-9]{10,15}$")) {
            editText.setError(errorMessage);
            return false;
        }

        return true;
    }


    // Valida el formato de la dirección (Ejemplo básico de no vacío)
    public static boolean validateAddressField(EditText editText, String errorMessage) {
        String address = editText.getText().toString().trim();
        if (address.isEmpty() || address.length() < 5) { // Por ejemplo, dirección mínima de 5 caracteres
            editText.setError(errorMessage);
            return false;
        }
        return true;
    }

    public static boolean validateEmailField(EditText emailField, String errorMessage) {
        String email = emailField.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            emailField.setError(errorMessage);
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailField.setError(errorMessage);
            return false;
        }

        return true;
    }

    // Método auxiliar para seleccionar un valor en un Spinner desde un arreglo
    public static void setSpinnerSelection(Context context, Spinner spinner, String value, int arrayResId) {
        // Cargar los datos del arreglo
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                context, arrayResId, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Buscar la posición del valor a seleccionar
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).toString().equals(value)) {
                spinner.setSelection(i);
                break;
            }
        }
    }

}
