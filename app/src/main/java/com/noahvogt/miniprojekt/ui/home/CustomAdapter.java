package com.noahvogt.miniprojekt.ui.home;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.noahvogt.miniprojekt.R;

import org.w3c.dom.Text;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private List<Data> localDataSet;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private TextView Betreff;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            textView = (TextView) view.findViewById(R.id.textView);
            Betreff = (TextView) view.findViewById(R.id.Betreff);
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
    public CustomAdapter(List<Data> dataSet) {
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
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
        Data contact = localDataSet.get(position);

        // Set item views based on your views and data model
        TextView textView = viewHolder.textView;
        textView.setText(contact.getName());
        TextView betreffView = viewHolder.Betreff;
        betreffView.setText(contact.getDate());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}

