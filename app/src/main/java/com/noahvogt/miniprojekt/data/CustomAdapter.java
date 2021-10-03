 package com.noahvogt.miniprojekt.data;


import android.os.Build;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.noahvogt.miniprojekt.DataBase.Message;


import java.util.List;
import java.util.Objects;

public class CustomAdapter extends ListAdapter<Message, EmailViewHolder> {

    public SelectedMessage selectedMessage;
    public List<Message> messageList;

    public CustomAdapter(@NonNull DiffUtil.ItemCallback<Message> diffCallback, SelectedMessage selectedMessage) {
        super(diffCallback);
        this.selectedMessage = selectedMessage;
    }

    @Override
    public EmailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return EmailViewHolder.create(parent,selectedMessage, messageList);
    }

    /* bind data to View*/
    @Override
    public void onBindViewHolder(EmailViewHolder holder, int position) {
        Message current = getItem(position);
        holder.bind(current.getFrom(),current.getSubject(), current.getDate() ,current.getTextContent()); }

    /*get List from adapter which is shown*/
    public void getList(List<Message> messageList){
        this.messageList = messageList;
    }

    public interface SelectedMessage{
        void selectedMessage(Message messages, EmailViewModel emailViewModel);
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

