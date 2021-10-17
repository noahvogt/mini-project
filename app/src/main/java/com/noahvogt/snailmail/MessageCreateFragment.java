package com.noahvogt.snailmail;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.noahvogt.snailmail.DataBase.Message;
import com.noahvogt.snailmail.data.EmailViewModel;
import com.noahvogt.snailmail.data.MailFunctions;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MessageCreateFragment extends DialogFragment implements PopupMenu.OnMenuItemClickListener {

    public static final String EXTRA_TO = "com.example.android.namelistsql.NAME";
    public static final String EXTRA_FROM = "com.example.android.namelistsql.FROM";
    public static final String EXTRA_SUBJECT = "com.example.android.namelistsql.SUBJECT";
    public static final String EXTRA_MESSAGE = "com.example.android.namelistsql.MESSAGE";
    public static final String EXTRA_CC = "com.example.android.namelistsql.CC";
    public static final String EXTRA_BCC = "com.example.android.namelistsql.BCC";
    public static final String EXTRA_DATE = "com.example.android.namelistsql.DATE";

    public EditText sendingAddressObject;
    public EditText receivingAddressObject;
    public EditText subjectObject;
    public EditText messageBodyObject;
    public EditText ccObject;
    public EditText bccObject;

    Message mMessage = null;
    public EmailViewModel mEmailViewModel = null;

    public static final int RESULT_CANCELED = 0;
    public static final int RESULT_OK = -1;

    public Activity activity = new Activity();
    public static Intent replyIntent = new Intent();

    public static MessageCreateFragment newInstance() {
        return new MessageCreateFragment();
    }

    public MessageCreateFragment getMessage(Message message, EmailViewModel emailViewModel, MessageCreateFragment messageCreateFragment){
        this.mEmailViewModel = emailViewModel;
        this.mMessage = message;
        return messageCreateFragment;
    }

    private AlertDialog dialog;

    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    /* set theming style */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.messageCreateTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /* set and inflate layout */
        View view = inflater.inflate(R.layout.message_create_fragment, container, false);

        /* init vars */
        ImageButton closeButton = view.findViewById(R.id.create_message_close_button);
        final ImageButton sendButton = view.findViewById(R.id.create_message_send_button);
        ImageButton dotButton = view.findViewById(R.id.create_message_dots_button);
        ImageButton attachButton = view.findViewById(R.id.create_message_attach_button);

         sendingAddressObject = (EditText) view.findViewById(R.id.create_message_sending_address_text);
         receivingAddressObject = (EditText) view.findViewById(R.id.create_message_receiving_address_text);
         ccObject = (EditText) view.findViewById(R.id.create_message_cc_text);
         bccObject = (EditText) view.findViewById(R.id.create_message_bcc_text);
         subjectObject = (EditText) view.findViewById(R.id.create_message_subject_text);
         messageBodyObject = (EditText) view.findViewById(R.id.create_message_body_text);

        SharedPreferences mailServerCredentials = getContext().getSharedPreferences("Credentials", Context.MODE_PRIVATE);
        
        /* set logged in email address as sending address */
        String currentMailUser = mailServerCredentials.getString("currentUser", "");
        sendingAddressObject.setText(currentMailUser);

        /* get string vars, MAYBE NOT HERE */
        if (mMessage != null) {
            sendingAddressObject.setText(mMessage.getFrom());
            receivingAddressObject.setText(mMessage.getTo());
            subjectObject.setText(mMessage.getSubject());
            messageBodyObject.setText(mMessage.getTextContent());
            bccObject.setText(mMessage.getBcc());
            ccObject.setText(mMessage.getCc());
        }

        /* TODO: add cc + bcc functionality */

        /* doesn't work cause Activity is not extended and used as variable */






        /* button listeners */
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subject = subjectObject.getText().toString();
                String messageBody = messageBodyObject.getText().toString();

                /* give alert dialog box to user in case input fields are not empty */

                if (subject.isEmpty() && messageBody.isEmpty()) {
                    dismiss();
                }
                else {
                    /* setup dialog for saving draft message */
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                    alertDialogBuilder.setTitle("Warning");
                    alertDialogBuilder
                            .setMessage("Do you want to save your Draft?")
                            .setCancelable(false)
                            .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    activity.setResult(RESULT_CANCELED, replyIntent);

                                    String from = sendingAddressObject.getText().toString();
                                    String to = receivingAddressObject.getText().toString();
                                    String subject = subjectObject.getText().toString();
                                    String message = messageBodyObject.getText().toString();
                                    String cc = ccObject.getText().toString();
                                    String bcc = bccObject.getText().toString();


                                    replyIntent.putExtra(EXTRA_FROM, from);
                                    replyIntent.putExtra(EXTRA_TO, to);
                                    replyIntent.putExtra(EXTRA_CC, cc);
                                    replyIntent.putExtra(EXTRA_BCC, bcc);
                                    replyIntent.putExtra(EXTRA_SUBJECT, subject);
                                    replyIntent.putExtra(EXTRA_MESSAGE, message);
                                    activity.setResult(RESULT_OK, replyIntent);

                                    activity.finish();

                                    Intent intent = new Intent(getContext(), NewDraftMessageActivity.class);
                                    startActivityForResult(intent, MainActivity.NEW_WORD_ACTIVITY_REQUEST_CODE);

                                    /* close the whole fragment */
                                    dismiss();
                                }
                            })
                            .setNegativeButton("No",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    /* if this button is clicked, close the hole fragment */
                                    dismiss();
                                }
                            });

                    /* create + show alert dialog */
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            }
        });

        attachButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* TODO: add file attachment functionality */
            }
        });

        dotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getActivity(), v);
                popupMenu.setOnMenuItemClickListener(MessageCreateFragment.this::onMenuItemClick);
                popupMenu.inflate(R.menu.create_message_options_menu);
                popupMenu.show();
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* init vars, MAYBE NEEDED FOR LATER */
                String sendingAddress = sendingAddressObject.getText().toString();
                String receivingAddress = receivingAddressObject.getText().toString();
                String subject = subjectObject.getText().toString();
                String messageBody = messageBodyObject.getText().toString();
                String ccStr = ccObject.getText().toString();
                String bccStr = bccObject.getText().toString();

                Gson gson = new Gson();

                /* get string vars, MAYBE NOT HERE */
                String jsonData = mailServerCredentials.getString("data", "");
                Type credentialsType = new TypeToken<ArrayList<MailServerCredentials>>(){}.getType();
                ArrayList<MailServerCredentials> allUsersCredentials = gson.fromJson(jsonData, credentialsType);
                
                String smtpHost = null, password = null; int smtpPort = 587;

                for (int i = 0; i < allUsersCredentials.size(); i++) {
                    if (allUsersCredentials.get(i).getUsername().equals(currentMailUser)) {
                        smtpHost = allUsersCredentials.get(i).getSmtpHost();
                        smtpPort = allUsersCredentials.get(i).getSmtpPort();
                        password = allUsersCredentials.get(i).getPassword();
                        break;
                    }
                }
                
                /* check for valid input */
                if (MailFunctions.validateMessageBody(messageBodyObject) && MailFunctions.validateSubject(subjectObject) &&
                MailFunctions.validateEmail(receivingAddressObject) && MailFunctions.validateEmail(sendingAddressObject)) {
                    try {


                        MailFunctions.sendStarttlsMail(smtpHost, sendingAddress, receivingAddress, password, messageBody,
                                subject, ccStr, bccStr, smtpPort);
                        Toast.makeText(getActivity(), "Sending ... ", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }catch (com.chaquo.python.PyException pyException){
                        Toast.makeText(getActivity(), "Couldn't send message", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Please check your input", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return view;
    }

    /* TODO: add useful functionality to the menu + consider not using Resource ID's in switch statement

        IDEAS:
        - safe to draft
        - clear all user input
        - change / check email signature
        - some email header hacking / options / customization
        - mail server probing
        - sending address spoofer ??
    */
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.create_message_delete:
                Toast.makeText(getActivity(), "item 1 clicked", Toast.LENGTH_LONG).show();
                return true;
            case R.id.create_message_spam:
                Toast.makeText(getActivity(), "item 2 clicked", Toast.LENGTH_LONG).show();
                return true;
            default: /* this case should never occur */
                return false;
        }
    }
}