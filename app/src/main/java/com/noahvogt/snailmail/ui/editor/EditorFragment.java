package com.noahvogt.snailmail.ui.editor;

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

import com.noahvogt.snailmail.DataBase.Message;
import com.noahvogt.snailmail.MainActivity;
import com.noahvogt.snailmail.NewDraftMessageActivity;
import com.noahvogt.snailmail.R;
import com.noahvogt.snailmail.data.EmailViewModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EditorFragment extends DialogFragment implements PopupMenu.OnMenuItemClickListener {

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

    public static com.noahvogt.snailmail.ui.editor.EditorFragment newInstance() {
        return new com.noahvogt.snailmail.ui.editor.EditorFragment();
    }

    public com.noahvogt.snailmail.ui.editor.EditorFragment getMessage(Message message, EmailViewModel emailViewModel, 
                                                                      com.noahvogt.snailmail.ui.editor.EditorFragment editorFragment) {
        this.mEmailViewModel = emailViewModel;
        this.mMessage = message;
        return editorFragment;
    }

    private AlertDialog dialog;

    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    /* set theming style */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.editorTheme);
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

        /* button listeners */
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subject = subjectObject.getText().toString();
                String messageBody = messageBodyObject.getText().toString();

                /* give alert dialog box to user in case input fields are not empty */
                if (subject.isEmpty() && messageBody.isEmpty()) {
                    dismiss();
                } else {
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
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ButtonHandler buttonHandler = new ButtonHandler();
                buttonHandler.handleSendButton(getActivity(), mailServerCredentials, getDialog(), currentMailUser, sendingAddressObject,
                                               receivingAddressObject, subjectObject, messageBodyObject, ccObject, bccObject);
            }
        });

        return view;
    }

    /* TODO: add useful functionality to the menu + consider not using Resource ID's in switch statement

        IDEAS:
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
                Toast.makeText(getActivity(), "Nothing will happen", Toast.LENGTH_LONG).show();
                return true;
            case R.id.create_message_spam:
                Toast.makeText(getActivity(), "Nothing wil happen", Toast.LENGTH_LONG).show();
                return true;
            default: /* this case should never occur */
                return false;
        }
    }
}