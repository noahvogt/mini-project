package com.noahvogt.miniprojekt.ui.home;


import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.noahvogt.miniprojekt.MainActivity;
import com.noahvogt.miniprojekt.ui.DataBase.Message;
import com.noahvogt.miniprojekt.ui.slideshow.EmailViewHolder;


import java.util.Objects;

public class CustomAdapter extends ListAdapter<Message, EmailViewHolder> {

    public CustomAdapter(@NonNull DiffUtil.ItemCallback<Message> diffCallback) {
        super(diffCallback);
    }

    @Override
    public EmailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return EmailViewHolder.create(parent);
    }

    /* bind data to View*/
    @Override
    public void onBindViewHolder(EmailViewHolder holder, int position) {
        Message current = getItem(position);
        holder.bind(current.getFrom(),current.getSubject(), current.getDate() ,current.getTextContent());
        EmailViewHolder.putCurrent(current);
    }

    public static class EmailDiff extends DiffUtil.ItemCallback<Message> {

        @Override
        public boolean areItemsTheSame(@NonNull Message oldItem, @NonNull Message newItem) {
            return oldItem == newItem;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public boolean areContentsTheSame(@NonNull Message oldItem, @NonNull Message newItem) {
            return Objects.equals(oldItem.getId(), newItem.getId());
        }
    }
}

