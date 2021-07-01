package com.noahvogt.miniprojekt;

import android.util.Patterns;
import android.widget.EditText;

public class mailFunctions {

    // TODO: resolve endIcon style conflict

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

    public static boolean validateSubject(EditText emailSubject) {
        String subject = emailSubject.getText().toString();
        // TODO: check email protocol specification for what is allowed for subjects
        return true;
    }

    public static boolean validateMessageBody(EditText emailMessageBody) {
        String messageBody = emailMessageBody.getText().toString();
        // TODO: check email protocol specification for what is allowed for message bodies
        return true;
    }


    public static boolean checkForSameEmail(EditText firstAddress, EditText secondAddress) {
        String firstEmail = firstAddress.getText().toString();
        String secondEmail = secondAddress.getText().toString();

        if (firstEmail.equals(secondEmail)) {
            firstAddress.setError("Fields cannot be the same");
            secondAddress.setError("Fields cannot be the same");
            return true;
        } else {
            firstAddress.setError(null);
            secondAddress.setError(null);
            return false;
        }
    }
}
