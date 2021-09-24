package com.noahvogt.miniprojekt.ui.slideshow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.noahvogt.miniprojekt.MainActivity;
import com.noahvogt.miniprojekt.R;
import com.noahvogt.miniprojekt.ui.DataBase.Message;
import com.noahvogt.miniprojekt.ui.home.CustomAdapter;
import com.noahvogt.miniprojekt.ui.show.MessageShowFragment;

public class  DraftFragment extends Fragment implements CustomAdapter.SelectedMessage{

   private DraftViewModel draftViewModel;
   EmailViewModel mEmailViewModel;
   RecyclerView recyclerView;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {



        recyclerView = MainActivity.recyclerView.findViewById(R.id.recyclerView);

        final CustomAdapter adapter = new CustomAdapter(new CustomAdapter.EmailDiff(), this);

        /* Attach the adapter to the recyclerview to populate items */
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //mEmailViewModel = new ViewModelProvider(this).get(EmailViewModel.class);

        mEmailViewModel = new ViewModelProvider(this).get(EmailViewModel.class);
        mEmailViewModel.getDraftMessage().observe(getViewLifecycleOwner(), messages -> {
            /* Update the cached copy of the messages in the adapter*/
            adapter.submitList(messages);
            /*get List of Message to show them onClick */
            adapter.getList(messages);

        });

        draftViewModel =
                new ViewModelProvider(this).get(DraftViewModel.class);
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        final TextView textView = root.findViewById(R.id.text_slideshow);
        draftViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }


    @Override
    public void selectedMessage(Message messages, EmailViewModel emailViewModel) {

        AppCompatActivity activity = (AppCompatActivity) getContext();
        DialogFragment dialog = MessageShowFragment.newInstance(messages, mEmailViewModel);
        dialog.show(activity.getSupportFragmentManager(), "tag");
    }
}

