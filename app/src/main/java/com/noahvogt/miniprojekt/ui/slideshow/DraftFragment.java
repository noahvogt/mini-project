package com.noahvogt.miniprojekt.ui.slideshow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
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

import com.noahvogt.miniprojekt.data.EmailViewModel;
import com.noahvogt.miniprojekt.MainActivity;
import com.noahvogt.miniprojekt.R;
import com.noahvogt.miniprojekt.DataBase.Message;
import com.noahvogt.miniprojekt.data.CustomAdapter;
import com.noahvogt.miniprojekt.MessageCreateFragment;

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
            /*gives list of messages to EmailViewModel */
            MainActivity.mEmailViewModel.setListAll(messages, "Drafts");

        });

        draftViewModel =
                new ViewModelProvider(this).get(DraftViewModel.class);
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        final TextView textView = root.findViewById(R.id.text_slideshow);
        draftViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });
        return root;
    }


    @Override
    public void selectedMessage(Message messages, EmailViewModel emailViewModel) {
        MessageCreateFragment messageCreateFragment = new MessageCreateFragment();

        //TODO: make this Fragment editable
        AppCompatActivity activity = (AppCompatActivity) getContext();
        DialogFragment dialog = messageCreateFragment.getMessage(messages, emailViewModel, messageCreateFragment);
        dialog.show(activity.getSupportFragmentManager(), "tag");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item, View headerView) {
        return false;
    }
}

