package com.noahvogt.miniprojekt;

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

    public static PyObject getIMAPConnection(String host, String email, String password) {
        Python python = Python.getInstance();
        PyObject pythonMailFunctions = python.getModule("mailFunctions");
        return pythonMailFunctions.callAttr("connect", host, email, password, 993);
    }

    public static List listMailboxes(PyObject IMAPConnection) {
        Python python = Python.getInstance();
        PyObject pythonMailFunctions = python.getModule("mailFunctions");
        return pythonMailFunctions.callAttr("listMailboxes", IMAPConnection).asList();
    }

    public static List fetchMailsFromBox(PyObject IMAPConnection, String Folder, String InputType) {
        Python python = Python.getInstance();
        PyObject pythonMailFunctions = python.getModule("mailFunctions");
        return pythonMailFunctions.callAttr("fetchMails", IMAPConnection, Folder, InputType).asList();
    }

    public static String fetchSubject(int messageIndex){
        Python python = Python.getInstance();
        PyObject pythonMailFunction = python.getModule("mailFunctions");
        return pythonMailFunction.callAttr("printSubject", messageIndex).toString();
    }

    public static String fetchFrom(int messageIndex){
        Python python = Python.getInstance();
        PyObject pythonMailFunction = python.getModule("mailFunctions");
        return pythonMailFunction.callAttr("printFrom", messageIndex).toString();
    }
    public static String fetchTo(int messageIndex){
        Python python = Python.getInstance();
        PyObject pythonMailFunction = python.getModule("mailFunctions");
        return pythonMailFunction.callAttr("printTo", messageIndex).toString();
    }
    public static String fetchBcc(int messageIndex){
        Python python = Python.getInstance();
        PyObject pythonMailFunction = python.getModule("mailFunctions");
        return pythonMailFunction.callAttr("printBcc", messageIndex).toString();
    }
    public static String fetchCC(int messageIndex){
        Python python = Python.getInstance();
        PyObject pythonMailFunction = python.getModule("mailFunctions");
        return pythonMailFunction.callAttr("printCc", messageIndex).toString();
    }
    public static String fetchDate(int meassageIndex){
        Python python = Python.getInstance();
        PyObject pythonMailFunction = python.getModule("mailFunctions");
        return pythonMailFunction.callAttr("printDate", meassageIndex).toString();
    }
    public static String fetchContent(int messageIndex){
        Python python = Python.getInstance();
        PyObject pythonMailFunction = python.getModule("mailFunctions");
        return pythonMailFunction.callAttr("printContent", messageIndex).toString();
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
