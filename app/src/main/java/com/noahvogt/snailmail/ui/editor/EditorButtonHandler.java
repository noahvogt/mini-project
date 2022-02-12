package com.noahvogt.snailmail.ui.editor;

import static com.noahvogt.snailmail.ui.editor.EditorFragment.EXTRA_BCC;
import static com.noahvogt.snailmail.ui.editor.EditorFragment.EXTRA_CC;
import static com.noahvogt.snailmail.ui.editor.EditorFragment.EXTRA_FROM;
import static com.noahvogt.snailmail.ui.editor.EditorFragment.EXTRA_MESSAGE;
import static com.noahvogt.snailmail.ui.editor.EditorFragment.EXTRA_SUBJECT;
import static com.noahvogt.snailmail.ui.editor.EditorFragment.EXTRA_TO;
import static com.noahvogt.snailmail.ui.editor.EditorFragment.replyIntent;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.noahvogt.snailmail.MainActivity;
import com.noahvogt.snailmail.data.MailServerCredentials;
import com.noahvogt.snailmail.data.MailFunctions;
import com.noahvogt.snailmail.mail.SendMail;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class EditorButtonHandler {

    String jsonData;
    ArrayList<MailServerCredentials> credentialsArrayList;
    String smtpHost, password;
    int smtpPort = 587;
    String sendingAddress, receivingAddress, subject, messageBody, ccStr, bccStr;
    public static final int RESULT_CANCELED = 0;
    public static final int RESULT_OK = -1;

    public void handleSendButton(Activity activity, SharedPreferences credentialPreferences, Dialog dialog, String currentMailUser,
                                 EditText sendingAddressObject, EditText receivingAddressObject, EditText subjectObject, EditText messageBodyObject,
                                 EditText ccObject, EditText bccObject, Context context) {
        getCredentialsJsonData(credentialPreferences);

        if (jsonData.isEmpty()) {
            Toast.makeText(activity, "Please setup an account before sending", Toast.LENGTH_SHORT).show();
        } else {
            getCredentials(currentMailUser);
            getUserInput(sendingAddressObject, receivingAddressObject, subjectObject, messageBodyObject, ccObject, bccObject);

            if (isValidMessage(messageBodyObject, subjectObject, receivingAddressObject, sendingAddressObject)) {
                try {
                    Toast.makeText(activity, "Sending ... ", Toast.LENGTH_SHORT).show();
                    sendMessageViaNewThread(context);
                    dialog.dismiss();
                } catch (com.chaquo.python.PyException pyException) {
                    Toast.makeText(activity, "Could not send message", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(activity, "Please check your input", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void handleCloseButton(Activity activity, SharedPreferences credentialPreferences, Dialog dialog, String currentMailUser,
                                  EditText sendingAddressObject, EditText receivingAddressObject, EditText subjectObject, EditText messageBodyObject,
                                  EditText ccObject, EditText bccObject) {
        getCredentialsJsonData(credentialPreferences);

        if (jsonData.isEmpty())
            dialog.dismiss();
        else {
            getUserInput(sendingAddressObject, receivingAddressObject, subjectObject, messageBodyObject, ccObject, bccObject);

            /* give alert dialog box to user in case input fields are not empty */
            if (subject.isEmpty() && messageBody.isEmpty()) {
                dialog.dismiss();
            } else {
                askForSavingDraftMessageDialog(activity, dialog, sendingAddressObject, receivingAddressObject, subjectObject, messageBodyObject, ccObject, bccObject);
            }
        }
    }

        public void getCredentialsJsonData (SharedPreferences credentials){
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

    public void sendMessage(Dialog dialog, Context context) {
        MailFunctions.sendStarttlsMail(smtpHost, sendingAddress, receivingAddress, password, messageBody,
                subject, ccStr, bccStr, smtpPort);
        dialog.dismiss();
    }

    public void askForSavingDraftMessageDialog(Activity activity, Dialog dialog, EditText sendingAddressObject, EditText receivingAddressObject, EditText subjectObject,
                                       EditText messageBodyObject, EditText ccObject, EditText bccObject) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setTitle("Warning");
        alertDialogBuilder
                .setMessage("Do you want to save your Draft?")
                .setCancelable(false)
                .setPositiveButton("Yes", (CloseDialog, id) -> {

                    activity.setResult(RESULT_CANCELED, replyIntent);

                    String from = sendingAddressObject.getText().toString();
                    String to = receivingAddressObject.getText().toString();
                    String subject1 = subjectObject.getText().toString();
                    String message = messageBodyObject.getText().toString();
                    String cc = ccObject.getText().toString();
                    String bcc = bccObject.getText().toString();


                    replyIntent.putExtra(EXTRA_FROM, from);
                    replyIntent.putExtra(EXTRA_TO, to);
                    replyIntent.putExtra(EXTRA_CC, cc);
                    replyIntent.putExtra(EXTRA_BCC, bcc);
                    replyIntent.putExtra(EXTRA_SUBJECT, subject1);
                    replyIntent.putExtra(EXTRA_MESSAGE, message);
                    activity.setResult(RESULT_OK, replyIntent);

                    activity.finish();

                    Intent intent = new Intent(activity, NewDraftMessageActivity.class);
                    activity.startActivityForResult(intent, MainActivity.NEW_WORD_ACTIVITY_REQUEST_CODE);

                    dialog.dismiss();
                })
                .setNegativeButton("No", (closeDialog, id) -> {
                    dialog.dismiss();
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void sendMessageViaNewThread(Context context) {
        Thread thread = new Thread(() -> {
            try {
                SendMail.sendMessage(sendingAddress, receivingAddress, smtpHost, smtpPort, sendingAddress, password, subject, messageBody, context, ccStr, bccStr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        thread.start();
    }
}