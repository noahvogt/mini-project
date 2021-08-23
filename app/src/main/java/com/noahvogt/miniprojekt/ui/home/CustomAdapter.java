package com.noahvogt.miniprojekt.ui.home;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.appcompat.view.menu.MenuWrapperICS;
import androidx.recyclerview.widget.RecyclerView;

import com.noahvogt.miniprojekt.R;
import com.noahvogt.miniprojekt.ui.DataBase.Data;
import com.noahvogt.miniprojekt.ui.DataBase.Message;
import com.noahvogt.miniprojekt.ui.gallery.GalleryFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    public static Message message = new Message();

    private static ArrayList<Message> current = new ArrayList<Message>();

    private List<Message> localDataSet;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView betreff;
        private TextView date;
        private TextView begin;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            name = (TextView) view.findViewById(R.id.textView);
            betreff = (TextView) view.findViewById(R.id.betreff);
            date = (TextView) view.findViewById(R.id.date);
            begin = (TextView) view.findViewById(R.id.begin);
        }

        //public TextView getTextView() {
            //return textView;
        //}
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public CustomAdapter(List<Message> dataSet) {
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        //View view = LayoutInflater.from(viewGroup.getContext())
                //.inflate(R.layout.text_row_item, viewGroup, false);

        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.fragment_home, viewGroup, false); //fragment_home is just for no errors idk if it is the right file

        // Return a new holder instance
        ViewHolder view = new ViewHolder(contactView);
        return view;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        //viewHolder.getTextView().setText(localDataSet[position]);
        // Get the data model based on position
        Message contact = localDataSet.get(position);

        // Set item views based on your views and data model
        TextView nameView = viewHolder.name;
        TextView betreffView = viewHolder.betreff;
        TextView dateView = viewHolder.date;
        TextView beginView = viewHolder.begin;

        nameView.setText(contact.getFrom());
        betreffView.setText(contact.getBetreff());
        dateView.setText(contact.getDate());

           

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }



    //to set the content of the Sentfolder by changing the adapter
    public static void setSent(int numItems){

            message.setId(1);
            message.setBetreff("Hi");
            message.setFrom("jeffry");
            message.setDate("Today");

            current.add(message.getId(),message);

            message.setId(2);
            message.setBetreff("test");
            message.setFrom("Simon");
            message.setDate("23.8.august");

            current.add(message.getId(), message);


    }

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
}

