/* Note: this file is derived from a template located in the following repository:
 * https://github.com/googlecodelabs/android-room-with-a-view
 * 
 * This means was originally licensed under the Apache License 2.0, which can be found
 * in the text file 'APL' in the root directory of this repository. Any changes made to
 * this file however are licensed under the GNU General Public License Version 3.0,
 * which can be found in the file 'LICENSE' in the root directory of this repository.
 */

package com.noahvogt.snailmail.data;


import android.os.Build;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.noahvogt.snailmail.DataBase.Message;


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

