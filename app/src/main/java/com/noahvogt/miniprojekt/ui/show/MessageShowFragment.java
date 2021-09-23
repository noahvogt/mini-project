package com.noahvogt.miniprojekt.ui.show;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.noahvogt.miniprojekt.R;
import com.noahvogt.miniprojekt.ui.DataBase.Message;
import com.noahvogt.miniprojekt.ui.home.CustomAdapter;
import com.noahvogt.miniprojekt.ui.slideshow.EmailViewModel;


public class MessageShowFragment extends DialogFragment implements PopupMenu.OnMenuItemClickListener {


    public TextView showTo;
    public TextView showFrom;
    public TextView showSubject;
    public TextView showMessage;

    private static Message mCurrent;
    private EmailViewModel mEmailViewModel;
    public static MessageShowFragment newInstance(com.noahvogt.miniprojekt.ui.DataBase.Message current) {
       mCurrent = current;
        return new MessageShowFragment();}

    /* set theming style */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.messageCreateTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // set and inflate layout
        View view = inflater.inflate(R.layout.message_show_fragment, container, false);

        // init vars

        MessageShowViewModel messageShowViewModel =
                new ViewModelProvider(this).get(MessageShowViewModel.class);
        View root = inflater.inflate(R.layout.message_show_fragment, container, false);
        final TextView textView = root.findViewById(R.id.show_To);
        messageShowViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });



        ImageButton closeButton = view.findViewById(R.id.show_message_close_button);
        ImageButton dotButton = view.findViewById(R.id.show_message_dots_button);
        ImageButton attachButton = view.findViewById(R.id.show_message_attach_button);

        showTo = (TextView) view.findViewById(R.id.show_To);
        showFrom = (TextView) view.findViewById(R.id.show_From);
        showSubject = (TextView) view.findViewById(R.id.show_Subject);
        showMessage = (TextView) view.findViewById(R.id.show_Message);

        showTo.setText(mCurrent.getFrom());
        showFrom.setText(mCurrent.getFrom());
        showSubject.setText(mCurrent.getSubject());
        showMessage.setText(mCurrent.getTextContent());


        // TODO: add cc + bcc functionality

        // button listeners

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                popupMenu.setOnMenuItemClickListener(MessageShowFragment.this::onMenuItemClick);
                popupMenu.inflate(R.menu.create_message_options_menu);
                popupMenu.show();
            }
        });

        return view;
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.create_message_delete:
                Toast.makeText(getActivity(), "item delete clicked", Toast.LENGTH_LONG).show();
                return true;
            case R.id.create_message_spam:
                Toast.makeText(getActivity(), "item spam clicked", Toast.LENGTH_LONG).show();
                return true;
            case R.id.create_message_sent_to:
                Toast.makeText(getActivity(), "item sent to clicked", Toast.LENGTH_LONG).show();
                return true;
            case R.id.create_message_move_to:
                Toast.makeText(getActivity(), "item move to clicked", Toast.LENGTH_LONG).show();
                return true;
            default: // this case should never occur
                return false;
        }
    }
}