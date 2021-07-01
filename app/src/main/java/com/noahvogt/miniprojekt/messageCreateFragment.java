package com.noahvogt.miniprojekt;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.snackbar.Snackbar;

public class messageCreateFragment extends DialogFragment implements PopupMenu.OnMenuItemClickListener {

    static messageCreateFragment newInstance() {
        return new messageCreateFragment();
    }
    private AlertDialog dialog;



    // set theming style
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.messageCreateTheme);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // set and inflate layout
        View view = inflater.inflate(R.layout.message_create_fragment, container, false);

        // init vars

        ImageButton closeButton = view.findViewById(R.id.create_message_close_button);
        ImageButton sendButton = view.findViewById(R.id.create_message_send_button);
        ImageButton dotButton = view.findViewById(R.id.create_message_dots_button);
        ImageButton attachButton = view.findViewById(R.id.create_message_attach_button);

        EditText sendingAddressObject = (EditText) view.findViewById(R.id.create_message_sending_address_text);
        EditText receivingAddressObject = (EditText) view.findViewById(R.id.create_message_receiving_address_text);
        EditText subjectObject = (EditText) view.findViewById(R.id.create_message_subject_text);
        EditText messageBodyObject = (EditText) view.findViewById(R.id.create_message_body_text);

        // get string vars, MAYBE NOT HERE
        String sendingAddress = sendingAddressObject.getText().toString();
        String receivingAddress = receivingAddressObject.getText().toString();
        String subject = subjectObject.getText().toString();
        String messageBody = messageBodyObject.getText().toString();

        // TODO: add cc + bcc functionality

        // button listeners

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subject = subjectObject.getText().toString();
                String messageBody = messageBodyObject.getText().toString();
                if (subject.isEmpty() && messageBody.isEmpty()) {
                    dismiss();
                }
                else {

                    final boolean[] wantsToCancel = new boolean[1]; // do we really need an array here? stupid java
                    /*
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                    final View cancelAlertPopupView = getLayoutInflater().inflate(R.layout.closing_alert, null);

                    // open View window
                    dialogBuilder.setView(cancelAlertPopupView);
                    dialog = dialogBuilder.create();
                    dialog.show();

                    Button yesButton = (Button) view.findViewById(R.id.closing_alert_yes_button);
                    Button noButton = (Button) view.findViewById(R.id.closing_alert_no_button);





                    yesButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //wantsToCancel[0] = true;
                            dialog.dismiss();
                        }
                    });
                    noButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //wantsToCancel[0] = false;
                            dialog.dismiss();
                        }
                    });

                    if (wantsToCancel[0]) {
                        dismiss();
                    }*/
                    // TODO: alert user when pressing this button in case input fields are not empty

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

                    // set title
                    alertDialogBuilder.setTitle("Warning");

                    // set dialog message
                    alertDialogBuilder
                            .setMessage("Do you really want to cancel?")
                            .setCancelable(false)
                            .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // if this button is clicked, close
                                    // current activity
                                    wantsToCancel[0] = true;
                                    dismiss();
                                }
                            })
                            .setNegativeButton("No",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    // if this button is clicked, just close
                                    // the dialog box and do nothing
                                    wantsToCancel[0] = false;
                                    dialog.cancel();
                                }
                            });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();
                }



            }
        });

        attachButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: add file attachment functionality
            }
        });

        dotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getActivity(), v);
                popupMenu.setOnMenuItemClickListener(messageCreateFragment.this::onMenuItemClick);
                popupMenu.inflate(R.menu.create_message_options_menu);
                popupMenu.show();
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: implement sending functionality
                dismiss();
            }
        });


        return view;
    }

    // TODO: add useful functionality to the menu + consider not using Resource ID's in switch statement
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.create_message_item_1:
                Toast.makeText(getActivity(), "item 1 clicked", Toast.LENGTH_LONG).show();
                return true;
            case R.id.create_message_item_2:
                Toast.makeText(getActivity(), "item 2 clicked", Toast.LENGTH_LONG).show();
                return true;
            case R.id.create_message_item_3:
                Toast.makeText(getActivity(), "item 3 clicked", Toast.LENGTH_LONG).show();
                return true;
            case R.id.create_message_item_4:
                Toast.makeText(getActivity(), "item 4 clicked", Toast.LENGTH_LONG).show();
                return true;
            default: // this case should never occur
                return false;
        }
    }
}