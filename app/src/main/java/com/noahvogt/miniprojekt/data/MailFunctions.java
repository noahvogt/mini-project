package com.noahvogt.miniprojekt.data;

import android.util.Patterns;
import android.widget.EditText;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;

import java.util.List;

public class MailFunctions {

    public static boolean canConnect(String host, String email, String password) {
        Python python = Python.getInstance();
        PyObject pythonMailFunctions = python.getModule("mailFunctions");
        return pythonMailFunctions.callAttr("checkConnection", host, email, password, 993).toBoolean();
    }

    public static void sendStarttlsMail(String host, String sendingMail, String receivingMail, String password, String message, String subject, String cc, String bcc) {
        Python python = Python.getInstance();
        PyObject pythonMailFunctions = python.getModule("mailFunctions");
        pythonMailFunctions.callAttr("sendStarttls", host, sendingMail, receivingMail, password, message, subject, 587, cc, bcc);
    }

    public static PyObject getIMAPConnection(String host, String email, String password, int port) {
        Python python = Python.getInstance();
        PyObject pythonMailFunctions = python.getModule("mailFunctions");
        return pythonMailFunctions.callAttr("connect", host, email, password, port);
    }

    public static List listMailboxes(PyObject IMAPConnection) {
        Python python = Python.getInstance();
        PyObject pythonMailFunctions = python.getModule("mailFunctions");
        return pythonMailFunctions.callAttr("listMailboxes", IMAPConnection).asList();
    }


    public static String fetchMailsFromBox(PyObject IMAPConnection, String Folder) {
        Python python = Python.getInstance();
        PyObject pythonMailFunctions = python.getModule("mailFunctions");
        return pythonMailFunctions.callAttr("fetchMails", IMAPConnection, Folder).toString();
    }


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

    public static String getImapHostFromEmail(String email) {
        String topLevelHost = email.substring(email.lastIndexOf("@") + 1);
        if (topLevelHost.endsWith("edubs.ch")) {
            return "teamwork.edubs.ch";

        }else if (topLevelHost.endsWith("yahoo.com")){
            return "imap.mail.yahoo.com";

        } else {
            return "imap." + topLevelHost;
        }
    }

    public static String getSmtpHostFromEmail(String email) {
        String topLevelHost = email.substring(email.lastIndexOf("@") + 1);
        if (topLevelHost.equals("noahvogt.com")) {
            return "mail.noahvogt.com";

        } else if (topLevelHost.endsWith("yahoo.com")){
            return "smtp.mail.yahoo.com";
        }
        else {
            return "smtp." + topLevelHost;
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
        /* TODO: check email protocol specification for what is allowed for subjects */
        return true;
    }

    public static boolean validateMessageBody(EditText emailMessageBody) {
        String messageBody = emailMessageBody.getText().toString();
        /* TODO: check email protocol specification for what is allowed for message bodies */
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
