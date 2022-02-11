package com.noahvogt.snailmail.data;

import android.widget.EditText;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;

import java.util.List;
import java.util.regex.Pattern;

public class MailFunctions {

    public static boolean canConnect(String host, String email, String password) {
        Python python = Python.getInstance();
        PyObject pythonMailFunctions = python.getModule("mailFunctions");
        return pythonMailFunctions.callAttr("checkConnection", host, email, password, 993).toBoolean();
    }

    public static void sendStarttlsMail(String host, String sendingMail, String receivingMail, String password, String message,
                                        String subject, String cc, String bcc, int port) {
        Python python = Python.getInstance();
        PyObject pythonMailFunctions = python.getModule("mailFunctions");
        pythonMailFunctions.callAttr("sendStarttls", host, sendingMail, receivingMail, password,
                message, subject, port, cc, bcc);
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
    public static String fetchMailsFromBox(PyObject IMAPConnection, String FolderServer, String FolderLocal) {
        Python python = Python.getInstance();
        PyObject pythonMailFunctions = python.getModule("mailFunctions");
        return pythonMailFunctions.callAttr("fetchMails", IMAPConnection, FolderServer, FolderLocal).toString();
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
        } else if (topLevelHost.endsWith("yahoo.com")){
            return "imap.mail.yahoo.com";
        } else if (topLevelHost.endsWith("gmx.ch")){
            return "imap.gmx.net";
        } else if (topLevelHost.endsWith("gmx.de")){
            return "imap.gmx.net";
        } else if (topLevelHost.equals("noahvogt.com")) {
            return "mail.noahvogt.com";
        }else if (topLevelHost.endsWith("outlook.com")) {
            return "outlook.office365.com";
        }else if (topLevelHost.endsWith("icloud.com")){
            return "imap.mail.me.com";
        }else if (topLevelHost.endsWith("outlook.ch")) {
            return "outlook.office365.com";
        }else if (topLevelHost.endsWith("hotmail.com")) {
            return "outlook.office365.com";
        }else if (topLevelHost.endsWith("hotmail.ch")) {
            return "outlook.office365.com";
        }else if (topLevelHost.endsWith("web.de")) {
            return "imap.web.de ";
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
        }else if (topLevelHost.endsWith("gmx.ch")){
            return  "mail.gmx.net";
        }else if (topLevelHost.endsWith("gmx.de")) {
            return "mail.gmx.net";
        } else if (topLevelHost.endsWith("edubs.ch")) {
            return "smtp.edubs.ch";
        }else if (topLevelHost.endsWith("outlook.com")) {
            return "smtp-mail.outlook.com";
        }else if (topLevelHost.endsWith("outlook.ch")) {
            return "smtp-mail.outlook.com";
        }else if (topLevelHost.endsWith("hotmail.com")) {
            return "smtp-mail.outlook.com";
        }else if (topLevelHost.endsWith("hotmail.ch")) {
            return "smtp-mail.outlook.com";
        }else if (topLevelHost.endsWith("icloud.com")){
            return "smtp.mail.me.com";
        }else if (topLevelHost.endsWith("web.de")){
            return "smtp.web.de";
        } else {
            return "smtp." + topLevelHost;
        }
    }


    public static boolean validateEmail(EditText emailAddress) {
        String email = emailAddress.getText().toString().trim();

        if (email.isEmpty()) {
            emailAddress.setError("Field can't be empty");
            return false;
        } else if (!Pattern.matches(".+@.+", email)) {
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
        return true;
    }

    public static boolean validateMessageBody(EditText emailMessageBody) {
        String messageBody = emailMessageBody.getText().toString();
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
