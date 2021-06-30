package com.noahvogt.miniprojekt;

import android.util.Patterns;
import android.widget.EditText;

public class mailFunctions {

    // TODO: resolve password endIcon conflict
    public static boolean validateName(EditText emailName) {
        String name = emailName.getText().toString().trim();

        if (name.isEmpty()) {
            emailName.setError("Field can't be empty");
            return false;
        } else if (name.length() > 50) {
            emailName.setError("Name too long");
            return false;
        } else {
            emailName.setError(null);
            return true;
        }
    }

    // TODO: resolve password endIcon conflict
    public static boolean validateEmail(EditText emailAddress) {
        String email = emailAddress.getText().toString().trim();

        if (email.isEmpty()) {
            emailAddress.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailAddress.setError("Please enter a valid email address");
            return false;
        } else {
            emailAddress.setError(null);
            return true;
        }
    }

    // TODO: resolve password endIcon conflicts
    public static boolean validatePassword(EditText emailPassword) {
        String password = emailPassword.getText().toString().trim();

        if (password.isEmpty()) {
            emailPassword.setError("Field can't be empty");
            return false;
        } else {
            emailPassword.setError(null);
            return true;
        }
    }
}
