package com.noahvogt.miniprojekt.ui.home;


import android.os.Build;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

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

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     */


    // Create new views (invoked by the layout manager)


    // Replace the contents of a view (invoked by the layout manager)


    // Return the size of your dataset (invoked by the layout manager)


    /*

    //to set the content of the Inboxfolder
    public static void setInbox(int numItems){

        for (int i =1 ; i <= numItems; i++){

            message.setFrom("Hello");
            message.setDate("Tomorrow");
            message.setBetreff("Bye");
        }


    }


    //to set the content of the Draftfolder
    public static void setDraft(int numItems){

        for (int i = 1; i <= numItems; i++){
            message.setFrom("You");
            message.setDate("noDay");
            message.setBetreff("i want to die");
        }


    }

    public static ArrayList<Message> createEmailList(int numItems){
        ArrayList<Message> current = new ArrayList<Message>();


            //for (int i = 1; i <= numItems; i++) {
             //   current.add(CustomAdapter.message);
           // }




            for (int i = 1; i <= numItems; i++) {
                CustomAdapter.current.add(CustomAdapter.message);
            }




            for (int i = 1; i <= numItems; i++) {
                CustomAdapter.current.add(CustomAdapter.message);
            }


        return CustomAdapter.current;

    }

     */
}

