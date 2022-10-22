package com.noahvogt.snailmail.ui.mailboxes.archiveFolder;

import static com.noahvogt.snailmail.MainActivity.isDownloading;

import com.noahvogt.snailmail.MainActivity;
import com.noahvogt.snailmail.R;
import com.noahvogt.snailmail.data.CustomAdapter;
import com.noahvogt.snailmail.data.EmailViewModel;
import com.noahvogt.snailmail.database.Message;
import com.noahvogt.snailmail.ui.reader.ReaderFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ArchiveFragment extends Fragment implements CustomAdapter.SelectedMessage {

    private ArchiveViewModel archiveViewModel;
    EmailViewModel mEmailViewModel;
    RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        recyclerView = MainActivity.recyclerView.findViewById(R.id.recyclerView);

        final CustomAdapter adapter = new CustomAdapter(new CustomAdapter.EmailDiff(), this);

        /* Attach the adapter to the recyclerview to populate items */
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // mEmailViewModel = new ViewModelProvider(this).get(EmailViewModel.class);

        mEmailViewModel = new ViewModelProvider(this).get(EmailViewModel.class);
        mEmailViewModel.getArchiveMessage().observe(getViewLifecycleOwner(), messages -> {
            /* Update the cached copy of the messages in the adapter */
            adapter.submitList(messages);
            /* get List of Message to show them onClick */
            adapter.getList(messages);
            /* gives list of messages to EmailViewModel */
            MainActivity.mEmailViewModel.setListAll(messages, "Archive", isDownloading);
        });

        archiveViewModel = new ViewModelProvider(this).get(ArchiveViewModel.class);
        View root = inflater.inflate(R.layout.mailbox_archive_fragment, container, false);
        final TextView textView = root.findViewById(R.id.text_archive);
        return root;

    }

    @Override
    public void selectedMessage(Message messages, EmailViewModel emailViewModel) {

        AppCompatActivity activity = (AppCompatActivity) getContext();
        DialogFragment dialog = ReaderFragment.newInstance(messages, mEmailViewModel);
        dialog.show(activity.getSupportFragmentManager(), "tag");
    }
}
