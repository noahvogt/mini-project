package com.noahvogt.miniprojekt;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageButton;

public class NewDraftMessageActivity extends AppCompatActivity {

    public static final String EXTRA_TO = "com.example.android.namelistsql.NAME";
    public static final String EXTRA_FROM = "com.example.android.namelistsql.FROM";
    public static final String EXTRA_SUBJECT = "com.example.android.namelistsql.SUBJECT";
    public static final String EXTRA_MESSAGE = "com.example.android.namelistsql.MESSAGE";
    public static final String EXTRA_DATE = "com.example.android.namelistsql.DATE";

    private EditText sendingAddressObject;
    private EditText receivingAddressObject;
    private EditText subjectObject;
    private EditText messageBodyObject;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_create_fragment);

        sendingAddressObject = findViewById(R.id.create_message_sending_address_text);
        receivingAddressObject = findViewById(R.id.create_message_receiving_address_text);
        subjectObject = findViewById(R.id.create_message_subject_text);
        messageBodyObject = findViewById(R.id.create_message_body_text);

        final ImageButton button = findViewById(R.id.create_message_close_button);
        button.setOnClickListener(view -> {
            Intent replyIntent = new Intent();
            if (TextUtils.isEmpty(sendingAddressObject.getText())) {
                setResult(RESULT_CANCELED, replyIntent);
            } else {
                String from = sendingAddressObject.getText().toString();
                String to = receivingAddressObject.getText().toString();
                String subject = subjectObject.getText().toString();
                String message = messageBodyObject.getText().toString();
                replyIntent.putExtra(EXTRA_FROM, from);
                replyIntent.putExtra(EXTRA_TO, to);
                replyIntent.putExtra(EXTRA_SUBJECT, subject);
                replyIntent.putExtra(EXTRA_MESSAGE, message);
                setResult(RESULT_OK, replyIntent);
            }
            finish();
        });
    }
}
