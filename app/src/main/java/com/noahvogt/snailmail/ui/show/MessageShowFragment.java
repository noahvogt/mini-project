package com.noahvogt.snailmail.ui.show;


import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.noahvogt.snailmail.R;
import com.noahvogt.snailmail.DataBase.Message;
import com.noahvogt.snailmail.data.EmailViewModel;


public class MessageShowFragment extends DialogFragment implements PopupMenu.OnMenuItemClickListener {


    public TextView showTo;
    public TextView showFrom;
    public TextView showSubject;
    public TextView showMessage;
    public TextView showDate;

    public Button showBccButton;
    public Button showCCButton;

    private static Message mCurrent;
    private static EmailViewModel mEmailViewModel;

    public static MessageShowFragment newInstance(Message current, EmailViewModel emailViewModel) {
        mEmailViewModel = emailViewModel;
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
        /* set and inflate layout */
        View view = inflater.inflate(R.layout.message_show_fragment, container, false);

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

        showBccButton = view.findViewById(R.id.show_bcc);
        showCCButton = view.findViewById(R.id.show_cc);

        showTo = (TextView) view.findViewById(R.id.show_To);
        showFrom = (TextView) view.findViewById(R.id.show_From);
        showSubject = (TextView) view.findViewById(R.id.show_subject);
        showMessage = (TextView) view.findViewById(R.id.show_Message);
        showDate = (TextView) view.findViewById(R.id.show_date);

        showTo.setText(mCurrent.getTo());
        showFrom.setText(mCurrent.getFrom());
        showSubject.setText(mCurrent.getSubject());
        showMessage.setText(mCurrent.getTextContent());
        showDate.setText(mCurrent.getDate());


        // TODO: add cc + bcc functionality
        showBccButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewPopup(true);
            }
        });

        showCCButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewPopup(false);
            }
        });

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
            }
        });

        return view;
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.create_message_delete:
                Toast.makeText(getActivity(), "Deleting message", Toast.LENGTH_LONG).show();
                mEmailViewModel.deleteMessage(mCurrent);
                return true;
            case R.id.create_message_spam:
                Toast.makeText(getActivity(), "Moving message to spam", Toast.LENGTH_LONG).show();
                mEmailViewModel.updateFolder(mCurrent.getId(), "Spam");
                return true;
            case R.id.create_message_move_archive:
                Toast.makeText(getActivity(), "Moving message to archive", Toast.LENGTH_LONG).show();
                mEmailViewModel.updateFolder(mCurrent.getId(), "Archive");


            default: /* this case should never occur */
                return false;
        }
    }

    public void createNewPopup(boolean bcc){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        AlertDialog dialog;
        final View bccPopupView = getLayoutInflater().inflate(R.layout.cc_bcc_popup, null);
        TextView mBcc = bccPopupView.findViewById(R.id.show_listBcc);

        alertDialog.setView(bccPopupView);
        dialog = alertDialog.create();
        dialog.show();

        if (bcc){
            mBcc.setText(mCurrent.getBcc());
        }else {
            mBcc.setText(mCurrent.getCc());
        }



    }
}