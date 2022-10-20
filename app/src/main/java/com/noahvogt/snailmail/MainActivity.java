package com.noahvogt.snailmail;

import static com.noahvogt.snailmail.R.id.drawer_layout;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.noahvogt.snailmail.data.CustomAdapter;
import com.noahvogt.snailmail.data.EmailViewModel;
import com.noahvogt.snailmail.database.Message;
import com.noahvogt.snailmail.ui.drawer.DrawerFunctions;
import com.noahvogt.snailmail.ui.editor.EditorFragment;
import com.noahvogt.snailmail.ui.reader.ReaderFragment;
import com.noahvogt.snailmail.ui.settings.SettingsActivity;
import com.noahvogt.snailmail.workers.DownloadWorker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, CustomAdapter.SelectedMessage {

    private AppBarConfiguration mAppBarConfiguration;

    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;

    public static EmailViewModel mEmailViewModel;
    public static RecyclerView recyclerView;

    public static String userGlobal;
    public static boolean isDownloading = false;

    SharedPreferences mailServerCredentials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        DrawerFunctions drawerFunctions = new DrawerFunctions();

        /* invoke toolbar */
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /* invoke drawer */
        DrawerLayout drawer = findViewById(drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);

        /*
         * Passing each menu ID as a set of Ids because each menu should be considered
         * as top level destinations.
         */
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_home, R.id.nav_gallery,
                R.id.nav_slideshow, R.id.nav_archive, R.id.nav_spam).setDrawerLayout(drawer)
                        .build();
        final NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        final NavController navController = navHostFragment.getNavController();
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        View accountViewOnProfileClick = findViewById(R.id.accountView);
        accountViewOnProfileClick.setOnClickListener(onClickView -> {
            drawerFunctions.accountManagerViewHandler(MainActivity.this, getLayoutInflater(),
                    headerView, mailServerCredentials);
        });

        /* Lookup the recyclerview in activity layout */
        recyclerView = findViewById(R.id.recyclerView);

        final CustomAdapter adapter = new CustomAdapter(new CustomAdapter.EmailDiff(), this);

        /* Attach the adapter to the recyclerview to populate items */
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        /*
         * get Inbox Messages in RecycleViewer at beginning is overwritten by Fragments
         * but has to stay
         */
        mEmailViewModel = new ViewModelProvider(this).get(EmailViewModel.class);
        mEmailViewModel.getInboxMessage().observe(this, messages -> {
            /* Update the cached copy of the messages in the adapter */
            adapter.submitList(messages);
        });

        drawerFunctions.updateNavHeaderText(headerView, getApplicationContext());

        Button settingButton = findViewById(R.id.settingsButton);
        settingButton.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(i);
        });

        final Boolean[] clicked = { false };
        Button add_email_button = (Button) findViewById(R.id.addEmailButton);
        add_email_button.setOnClickListener(v -> {
            if (!clicked[0]) {
                drawerFunctions.newUserAlphaSoftwareWarningBeforeAddingEmail(true, headerView,
                        MainActivity.this, getLayoutInflater());
                clicked[0] = true;
            } else {
                drawerFunctions.createNewEmailDialog(headerView, getLayoutInflater(),
                        MainActivity.this);
            }
        });

        FloatingActionButton messageCreateButton = findViewById(R.id.messageButton);
        messageCreateButton.setOnClickListener(v -> {
            DialogFragment dialogFragment = EditorFragment.newInstance();
            dialogFragment.show(getSupportFragmentManager(), "tag");
        });
    }

    /* gets the data from the Email writer and adds it to the Database */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, EditorFragment.replyIntent);

        /* Creates class for the Date when Email is written */
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("dd.MM.yy");

        Message word = new Message(userGlobal,
                EditorFragment.replyIntent.getStringExtra(EditorFragment.EXTRA_TO),
                EditorFragment.replyIntent.getStringExtra(EditorFragment.EXTRA_CC),
                EditorFragment.replyIntent.getStringExtra(EditorFragment.EXTRA_BCC),
                EditorFragment.replyIntent.getStringExtra(EditorFragment.EXTRA_FROM),
                ft.format(dNow),
                EditorFragment.replyIntent.getStringExtra(EditorFragment.EXTRA_SUBJECT),
                EditorFragment.replyIntent.getStringExtra(EditorFragment.EXTRA_MESSAGE), "Draft",
                false);
        mEmailViewModel.insert(word);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Inflate the menu; this adds items to the action bar if it is present. */
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    /* better leave empty to avoid any listener disambiguity */
    public void onClick(View view) {
    }

    @Override
    public void selectedMessage(Message messages, EmailViewModel emailViewModel) {
        DialogFragment dialog = ReaderFragment.newInstance(messages, mEmailViewModel);
        dialog.show(getSupportFragmentManager(), "tag");
    }

    private static final String TAG = DownloadWorker.class.getSimpleName();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        DrawerFunctions drawerFunctions = new DrawerFunctions();
        return drawerFunctions.handleMasterDotsMenu(item, MainActivity.this, getLayoutInflater());
    }
}
