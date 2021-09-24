package com.noahvogt.miniprojekt.ui.slideshow;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.noahvogt.miniprojekt.ui.DataBase.Message;
import com.noahvogt.miniprojekt.ui.home.CustomAdapter;
import com.noahvogt.miniprojekt.R;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/* adds the content to the View of RecyclerView*/
public class EmailViewHolder extends RecyclerView.ViewHolder {
    private final TextView fromItemView;
    private final TextView subjectItemView;
    private final TextView dateItemView;
    private final TextView messageItemView;


    private EmailViewHolder(View itemView,
                            CustomAdapter.SelectedMessage selectedMessage,
                            List<Message> messageList) {
        super(itemView);
        fromItemView = itemView.findViewById(R.id.textView);
        subjectItemView = itemView.findViewById(R.id.subject);
        dateItemView = itemView.findViewById(R.id.date);
        messageItemView = itemView.findViewById(R.id.message);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedMessage.selectedMessage(messageList.get(getBindingAdapterPosition()), null );


            }
        });
    }

    public void bind(String from, String subject, String date, String message) {
        fromItemView.setText(from);
        subjectItemView.setText(subject);
        dateItemView.setText(date);
        messageItemView.setText(message);
    }

    public static EmailViewHolder create(ViewGroup parent,
                                         CustomAdapter.SelectedMessage selectedMessage,
                                         List<Message> messageList) {
         View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_home, parent, false);
        return new EmailViewHolder(view, selectedMessage, messageList);
    }



    }


