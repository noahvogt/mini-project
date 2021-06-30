package com.noahvogt.miniprojekt;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.noahvogt.miniprojekt.debuggingFeatures;

public class messageCreateFragment extends DialogFragment {

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
        ImageButton close = view.findViewById(R.id.create_message_close_button);
        TextView action = view.findViewById(R.id.create_message_send_button);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: implement sending functionality
                dismiss();
            }
        });

        return view;
    }

    public interface Callback {
        void onActionClick(String name);
    }

}