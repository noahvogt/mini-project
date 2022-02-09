package com.noahvogt.snailmail.ui.editor;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.noahvogt.snailmail.MailServerCredentials;
import com.noahvogt.snailmail.data.MailFunctions;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ButtonHandler {

    String jsonData;
    ArrayList<MailServerCredentials> credentialsArrayList;
    String smtpHost, password;
    int smtpPort = 587;
    String sendingAddress, receivingAddress, subject, messageBody, ccStr, bccStr;

    public void handleSendButton(Activity activity, SharedPreferences credentialPreferences, Dialog dialog, String currentMailUser,
                                 EditText sendingAddressObject, EditText receivingAddressObject, EditText subjectObject, EditText messageBodyObject,
                                 EditText ccObject, EditText bccObject) {
        getCredentialsJsonData(credentialPreferences);

        if (jsonData.isEmpty()) {
            Toast.makeText(activity, "Please setup an account before sending", Toast.LENGTH_SHORT).show();
        } else {
            getCredentials(currentMailUser);
            getUserInput(sendingAddressObject, receivingAddressObject, subjectObject, messageBodyObject, ccObject, bccObject);

            if (isValidMessage(messageBodyObject, subjectObject, receivingAddressObject, sendingAddressObject)) {
                try {
                    Toast.makeText(activity, "Sending ... ", Toast.LENGTH_SHORT).show();
                    sendMessage(dialog);
                } catch (com.chaquo.python.PyException pyException){
                    Toast.makeText(activity, "Could not send message", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(activity, "Please check your input", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void getCredentialsJsonData(SharedPreferences credentials) {
        jsonData = credentials.getString("data", "");
    }

    public void getCredentialsArraylist(String jsonData) {
        Gson gson = new Gson();
        Type credentialsType = new TypeToken<ArrayList<MailServerCredentials>>(){}.getType();
        credentialsArrayList = gson.fromJson(jsonData, credentialsType);
    }

    public void getCredentials(String currentMailUser) {
        getCredentialsArraylist(jsonData);

        for (int i = 0; i < credentialsArrayList.size(); i++){
            if (credentialsArrayList.get(i).getUsername().equals(currentMailUser)) {
                smtpHost = credentialsArrayList.get(i).getSmtpHost();
                smtpPort = credentialsArrayList.get(i).getSmtpPort();
                password = credentialsArrayList.get(i).getPassword();
                break;
            }
        }
    }

    public void getUserInput(EditText sendingAddressObject, EditText receivingAddressObject, EditText subjectObject,
                             EditText messageBodyObject, EditText ccObject, EditText bccObject) {
        sendingAddress = sendingAddressObject.getText().toString();
        receivingAddress = receivingAddressObject.getText().toString();
        subject = subjectObject.getText().toString();
        messageBody = messageBodyObject.getText().toString();
        ccStr = ccObject.getText().toString();
        bccStr = bccObject.getText().toString();
    }

    public boolean isValidMessage(EditText messageBodyObject, EditText subjectObject, EditText receivingAddressObject,
                                  EditText sendingAddressObject) {
        return  MailFunctions.validateMessageBody(messageBodyObject) && MailFunctions.validateSubject(subjectObject) &&
                MailFunctions.validateEmail(receivingAddressObject) && MailFunctions.validateEmail(sendingAddressObject);
    }

    public void sendMessage(Dialog dialog) {
        MailFunctions.sendStarttlsMail(smtpHost, sendingAddress, receivingAddress, password, messageBody,
                subject, ccStr, bccStr, smtpPort);
        dialog.dismiss();
    }
}