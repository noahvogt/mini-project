package com.noahvogt.snailmail.ui.reader;


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
import com.noahvogt.snailmail.database.Message;
import com.noahvogt.snailmail.data.EmailViewModel;


public class ReaderFragment extends DialogFragment implements PopupMenu.OnMenuItemClickListener {
    public TextView showTo;
    public TextView showFrom;
    public TextView showSubject;
    public TextView showMessage;
    public TextView showDate;

    public Button showBccButton;
    public Button showCCButton;

    private static Message mCurrent;
    private static EmailViewModel mEmailViewModel;

    public static ReaderFragment newInstance(Message current, EmailViewModel emailViewModel) {
        mEmailViewModel = emailViewModel;
        mCurrent = current;
        return new ReaderFragment();}

    /* set theming style */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.editorTheme);
    }

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /* set and inflate layout */
        View view = inflater.inflate(R.layout.reader_fragment, container, false);

        ReaderViewModel readerViewModel =
                new ViewModelProvider(this).get(ReaderViewModel.class);
        View root = inflater.inflate(R.layout.reader_fragment, container, false);
        final TextView textView = root.findViewById(R.id.show_To);
        readerViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
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
        showBccButton.setOnClickListener(v -> createNewPopup(true));

        showCCButton.setOnClickListener(v -> createNewPopup(false));

        closeButton.setOnClickListener(v -> dismiss());

        attachButton.setOnClickListener(v -> {
            // TODO: add file attachment functionality
        });

        dotButton.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(getActivity(), v);
            popupMenu.setOnMenuItemClickListener(ReaderFragment.this::onMenuItemClick);
            popupMenu.inflate(R.menu.create_message_options_menu);
            popupMenu.show();
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
        final View bccPopupView = getLayoutInflater().inflate(R.layout.reader_cc_bcc_popup, null);
        TextView mBcc = bccPopupView.findViewById(R.id.show_listBcc);

        alertDialog.setView(bccPopupView);
        dialog = alertDialog.create();
        dialog.show();

        if (bcc){
            mBcc.setText(mCurrent.getBcc());
        } else {
            mBcc.setText(mCurrent.getCc());
        }
    }
}