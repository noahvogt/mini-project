package com.noahvogt.miniprojekt.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import com.noahvogt.miniprojekt.DataBase.Message;
import com.noahvogt.miniprojekt.data.CustomAdapter;
import com.noahvogt.miniprojekt.ui.show.MessageShowFragment;
import com.noahvogt.miniprojekt.data.EmailViewModel;

import org.jetbrains.annotations.NotNull;

public class GalleryFragment extends Fragment implements CustomAdapter.SelectedMessage{

    EmailViewModel mEmailViewModel;
    RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {



       // mEmailViewModel.deleteNewMessage();

        GalleryViewModel galleryViewModel = new ViewModelProvider(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);
        galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        recyclerView = MainActivity.recyclerView.findViewById(R.id.recyclerView);

        final CustomAdapter adapter = new CustomAdapter(new CustomAdapter.EmailDiff(), this);


        /* Attach the adapter to the recyclerview to populate items */
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mEmailViewModel = new ViewModelProvider(this).get(EmailViewModel.class);
        mEmailViewModel.getSentMessage().observe(getViewLifecycleOwner(), messages -> {
            /* Update the cached copy of the messages in the adapter*/
            adapter.submitList(messages);
            /*get List of Message to show them onClick */
            adapter.getList(messages);
            /*gives list of messages to EmailViewModel */
            MainActivity.mEmailViewModel.setListAll(messages, "Sent");
        });

        return root;

    }


    /*starts Dialog of clicked message*/
    @Override
    public void selectedMessage(Message messages, EmailViewModel emailViewModel) {
        AppCompatActivity activity = (AppCompatActivity) getContext();
        DialogFragment dialog = MessageShowFragment.newInstance(messages, mEmailViewModel);
        dialog.show(activity.getSupportFragmentManager(), "tag");

    }
}