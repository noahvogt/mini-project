package com.noahvogt.miniprojekt;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


public class messageCreateFragment extends DialogFragment implements PopupMenu.OnMenuItemClickListener {

    static messageCreateFragment newInstance() {
        return new messageCreateFragment();
    }

    // set theming style
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.messageCreateTheme);
    }

    // set layout
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.message_create_fragment, container, false);
        ImageButton closeButton = view.findViewById(R.id.create_message_close_button);
        ImageButton sendButton = view.findViewById(R.id.create_message_send_button);
        ImageButton dotButton = view.findViewById(R.id.create_message_dots_button);
        ImageButton attachButton = view.findViewById(R.id.create_message_attach_button);

        // TODO: add cc + bcc functionality

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: alert user when pressing this button in case input fields are not empty
                dismiss();
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