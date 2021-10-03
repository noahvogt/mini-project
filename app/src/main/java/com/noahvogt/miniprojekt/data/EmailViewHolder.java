package com.noahvogt.miniprojekt.data;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.noahvogt.miniprojekt.DataBase.Message;
import com.noahvogt.miniprojekt.R;

import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

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
        System.out.println("length of from " + from.length());
        System.out.println("length of message " + message.length());


        if (subject.length() > 30){
            subject = subject.substring(0,26) + "...";
        }
        if (from.length() > 12) {
            from = from.substring(0,12) + "...";
        }
        if (message.length() > 30){
            message = message.substring(0,30) + "...";
        }
        if (date.length() > 9){
            date = "INVALID";
        }
        if (!isDate(date)){
            date = "INVALID";
        }

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

    public static boolean isDate(String date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy", Locale.getDefault());
        //dateFormat.setLenient(false);
        try {
            dateFormat.parse(date);
        } catch (ParseException e){
            return false;
        }

        return true;
    }



    }


