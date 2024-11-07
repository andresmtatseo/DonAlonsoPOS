package com.example.donalonsopos.util;

import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;

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

    // Valida el formato del teléfono (Ejemplo: formato general de 10 dígitos)
    public static boolean validatePhoneNumberField(EditText editText, String errorMessage) {
        String phone = editText.getText().toString().trim();
        if (!phone.matches("^[0-9]{10}$")) { // Puedes ajustar el regex según el formato que desees
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

}
