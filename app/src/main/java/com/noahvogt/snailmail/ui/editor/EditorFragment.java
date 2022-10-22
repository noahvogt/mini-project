package com.noahvogt.snailmail.ui.editor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.noahvogt.snailmail.R;
import com.noahvogt.snailmail.data.EmailViewModel;
import com.noahvogt.snailmail.database.Message;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import androidx.fragment.app.DialogFragment;

public class EditorFragment extends DialogFragment implements PopupMenu.OnMenuItemClickListener {

    public static final String EXTRA_TO = "com.example.android.namelistsql.NAME";
    public static final String EXTRA_FROM = "com.example.android.namelistsql.FROM";
    public static final String EXTRA_SUBJECT = "com.example.android.namelistsql.SUBJECT";
    public static final String EXTRA_MESSAGE = "com.example.android.namelistsql.MESSAGE";
    public static final String EXTRA_CC = "com.example.android.namelistsql.CC";
    public static final String EXTRA_BCC = "com.example.android.namelistsql.BCC";
    public static final String EXTRA_DATE = "com.example.android.namelistsql.DATE";

    public EditText sendingAddressObject, receivingAddressObject, subjectObject, messageBodyObject,
            ccObject, bccObject;
    public ImageButton sendButton, closeButton, dotButton, attachButton;
    public SharedPreferences mailServerCredentials;
    public String currentMailUser;

    public Message mMessage = null;
    public EmailViewModel mEmailViewModel = null;

    public static Intent replyIntent = new Intent();

    public static com.noahvogt.snailmail.ui.editor.EditorFragment newInstance() {
        return new com.noahvogt.snailmail.ui.editor.EditorFragment();
    }

    public com.noahvogt.snailmail.ui.editor.EditorFragment getMessage(Message message,
            EmailViewModel emailViewModel,
            com.noahvogt.snailmail.ui.editor.EditorFragment editorFragment) {
        this.mEmailViewModel = emailViewModel;
        this.mMessage = message;
        return editorFragment;
    }

    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors
            .newFixedThreadPool(NUMBER_OF_THREADS);

    /* set theming style */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.editorTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.editor_fragment, container, false);

        getPreferences(requireContext());
        getCurrentUser();

        initButtonObjects(view);
        initUserInputObjects(view);

        setFromAddressToLoggedInUser();
        applyMessageObjectToUI();

        closeButton.setOnClickListener(v -> {
            EditorButtonHandler editorButtonHandler = new EditorButtonHandler();
            editorButtonHandler.handleCloseButton(getActivity(), mailServerCredentials, getDialog(),
                    currentMailUser, sendingAddressObject, receivingAddressObject, subjectObject,
                    messageBodyObject, ccObject, bccObject);
        });

        attachButton.setOnClickListener(v -> {
        });

        dotButton.setOnClickListener(v -> {
        });

        sendButton.setOnClickListener(v -> {
            EditorButtonHandler editorButtonHandler = new EditorButtonHandler();
            editorButtonHandler.handleSendButton(getActivity(), mailServerCredentials, getDialog(),
                    currentMailUser, sendingAddressObject, receivingAddressObject, subjectObject,
                    messageBodyObject, ccObject, bccObject, getContext());
        });

        return view;
    }

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

    public void initUserInputObjects(View view) {
        sendingAddressObject = (EditText) view
                .findViewById(R.id.create_message_sending_address_text);
        receivingAddressObject = (EditText) view
                .findViewById(R.id.create_message_receiving_address_text);
        ccObject = (EditText) view.findViewById(R.id.create_message_cc_text);
        bccObject = (EditText) view.findViewById(R.id.create_message_bcc_text);
        subjectObject = (EditText) view.findViewById(R.id.create_message_subject_text);
        messageBodyObject = (EditText) view.findViewById(R.id.create_message_body_text);
    }

    public void initButtonObjects(View view) {
        closeButton = view.findViewById(R.id.create_message_close_button);
        sendButton = view.findViewById(R.id.create_message_send_button);
        dotButton = view.findViewById(R.id.create_message_dots_button);
        attachButton = view.findViewById(R.id.create_message_attach_button);
    }

    public void setFromAddressToLoggedInUser() {
        sendingAddressObject.setText(currentMailUser);
    }

    public void getCurrentUser() {
        currentMailUser = mailServerCredentials.getString("currentUser", "");
    }

    public void getPreferences(Context context) {
        mailServerCredentials = context.getSharedPreferences("Credentials", Context.MODE_PRIVATE);
    }

    public void applyMessageObjectToUI() {
        if (mMessage != null) {
            sendingAddressObject.setText(mMessage.getFrom());
            receivingAddressObject.setText(mMessage.getTo());
            subjectObject.setText(mMessage.getSubject());
            messageBodyObject.setText(mMessage.getTextContent());
            bccObject.setText(mMessage.getBcc());
            ccObject.setText(mMessage.getCc());
        }
    }
}
