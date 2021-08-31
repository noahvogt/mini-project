package com.noahvogt.miniprojekt.ui.slideshow;

import android.provider.Telephony;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.noahvogt.miniprojekt.MainActivity;
import com.noahvogt.miniprojekt.R;
import androidx.recyclerview.widget.RecyclerView;

/* adds the content to the View of RecyclerView*/
public class EmailViewHolder extends RecyclerView.ViewHolder {
    private final TextView fromItemView;
    private final TextView subjectItemView;
    private final TextView dateItemView;
    private final TextView messageItemView;

    private EmailViewHolder(View itemView) {
        super(itemView);
        fromItemView = itemView.findViewById(R.id.textView);
        subjectItemView = itemView.findViewById(R.id.subject);
        dateItemView = itemView.findViewById(R.id.date);
        messageItemView = itemView.findViewById(R.id.message);
    }

    public void bind(String from, String subject, String date, String message) {
        fromItemView.setText(from);
        subjectItemView.setText(subject);
        dateItemView.setText(date);
        messageItemView.setText(message);
    }

    public static EmailViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_home, parent, false);
        return new EmailViewHolder(view);
    }
}


