package com.noahvogt.miniprojekt;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.noahvogt.miniprojekt.ui.DataBase.Message;
import com.noahvogt.miniprojekt.ui.home.CustomAdapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.google.android.material.snackbar.Snackbar;
import com.noahvogt.miniprojekt.ui.home.SettingsActivity;
import com.noahvogt.miniprojekt.ui.show.MessageShowFragment;
import com.noahvogt.miniprojekt.ui.slideshow.EmailViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.noahvogt.miniprojekt.R.id.drawer_layout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, CustomAdapter.SelectedMessage {

    private AppBarConfiguration mAppBarConfiguration;

    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;
    public static EmailViewModel mEmailViewModel;
    public static RecyclerView recyclerView;

    private AlertDialog dialog;
    private EditText newemail_name, newemail_email, newemail_password; /* may not be private */

    SharedPreferences preferences;

    /* empty descriptor */
    public MainActivity() {}


    public String getVisibleFragment(){
        FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if(fragments != null){
            showToast("not null");
            for(Fragment fragment : fragments){
                showToast(fragment.toString());
                if(fragment.isVisible())
                    showToast("found visible fragment");
                    return "is gallery";
            }
        } else {
            showToast("null");}
            return null;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* define button listeners */

        Button add_email_button = (Button) findViewById(R.id.addEmailButton);
        add_email_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewEmailDialog();
            }
        });

        /*creates accountmanager by clicking on profil */
        View accountView = findViewById(R.id.accountView);
        accountView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewEmailDialog();
            }
        });

        /* invoke preferences */
        preferences = (SharedPreferences) getSharedPreferences("UserPrefrences", Context.MODE_PRIVATE);

        /* invoke toolbar */
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /* invoke drawer */
        DrawerLayout drawer = findViewById(drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        /*
         Passing each menu ID as a set of Ids because each
         menu should be considered as top level destinations.
        */
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_archive, R.id.nav_spam)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        /* Lookup the recyclerview in activity layout */
        recyclerView = findViewById(R.id.recyclerView);

        final CustomAdapter adapter = new CustomAdapter(new CustomAdapter.EmailDiff(),this);



        /* Attach the adapter to the recyclerview to populate items */
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        /* get Inbox Messages in Recyclerviewer at begining is overwritten by Fragments but has to stay*/
        mEmailViewModel = new ViewModelProvider(this).get(EmailViewModel.class);
        mEmailViewModel.getInboxMessage().observe(this, messages -> {
            /* Update the cached copy of the messages in the adapter*/
            adapter.submitList(messages);
        });


        Button settingButton = findViewById(R.id.settingsButton);
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(i);
            }
        });



        /* Start email Writer*/
        FloatingActionButton message_create_button = findViewById(R.id.messageButton);
        message_create_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment dialog = messageCreateFragment.newInstance();
                dialog.show(getSupportFragmentManager(), "tag");

            }
        });
    }


        /* gets the data from the Email writer and adds it to the Database */
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, messageCreateFragment.replyIntent);

            /* Creates class for the Date when Email is written */
            Date dNow = new Date();
            SimpleDateFormat ft =
                    new SimpleDateFormat("dd.MM.yy");

         //   if (requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
                Message word = new Message(
                        messageCreateFragment.replyIntent.getStringExtra(messageCreateFragment.EXTRA_TO),
                        null,
                        null,
                        messageCreateFragment.replyIntent.getStringExtra(messageCreateFragment.EXTRA_FROM),
                        ft.format(dNow),
                        messageCreateFragment.replyIntent.getStringExtra(messageCreateFragment.EXTRA_SUBJECT),
                        messageCreateFragment.replyIntent.getStringExtra(messageCreateFragment.EXTRA_MESSAGE),
                        "Draft",false);
                mEmailViewModel.insert(word);
          //  } else {
                Toast.makeText(
                        getApplicationContext(),
                        R.string.empty_not_saved,
                        Toast.LENGTH_LONG).show();

            Toast.makeText(
                    getApplicationContext(),
                    messageCreateFragment.replyIntent.getStringExtra(messageCreateFragment.EXTRA_FROM),
                    Toast.LENGTH_LONG).show();
          //  }


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
    public void onClick(View view) { }



    public void createNewEmailDialog(){
        /* define View window */
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final View emailPopupView = getLayoutInflater().inflate(R.layout.popup, null);

        /* init text field variables */
        newemail_name = emailPopupView.findViewById(R.id.popup_material_name_asking_text);
        newemail_email = emailPopupView.findViewById(R.id.popup_material_email_asking_text);
        newemail_password = emailPopupView.findViewById(R.id.popup_material_password_asking_text);

        /* init button variables */
        Button newemail_save_button = (Button) emailPopupView.findViewById(R.id.saveButton);
        /* may not be private */
        Button newemail_cancel_button = (Button) emailPopupView.findViewById(R.id.cancelButton);


        /* open View window */
            dialogBuilder.setView(emailPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        SharedPreferences.Editor preferencesEditor = preferences.edit();

        if (! Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }

        /* store user input */
        newemail_save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* store user input (only needed for DEBUGGING) */
                String name = newemail_name.getText().toString();
                String email = newemail_email.getText().toString();
                String password = newemail_password.getText().toString();

                if (!MailFunctions.validateEmail(newemail_email) | !MailFunctions.validateName(newemail_name) | !MailFunctions.validatePassword(newemail_password)) {
                    return;
                }

                /* connect to mail server and print various debugging output */
                showToast("Probe Connection ...");
                if (MailFunctions.canConnect(name, email, password) == Boolean.TRUE) {
                    showToast("was able to connect");
                    List l =  MailFunctions.listMailboxes(MailFunctions.getIMAPConnection(name, email, password));
                    for (int i = 0; i < l.size(); i++) {
                        showToast(l.get(i).toString());
                        /*gives list of Message Objects/dictionaries */
                        //List p = MailFunctions.fetchMailsFromBox(MailFunctions.getIMAPConnection(name, email, password), "INBOX");
                        System.out.println(l.get(i).toString());
                    }

                    /*Message word = new Message(
                            messageCreateFragment.replyIntent.getStringExtra(messageCreateFragment.EXTRA_TO),
                            null,
                            null,
                            messageCreateFragment.replyIntent.getStringExtra(messageCreateFragment.EXTRA_FROM),
                            ft.format(dNow),
                            messageCreateFragment.replyIntent.getStringExtra(messageCreateFragment.EXTRA_SUBJECT),
                            messageCreateFragment.replyIntent.getStringExtra(messageCreateFragment.EXTRA_MESSAGE),
                            "Draft",false);
                    mEmailViewModel.insert(word);

                     */

                    preferencesEditor.putString("name", name);
                    preferencesEditor.putString("email", email);
                    preferencesEditor.putString("password", password);
                    preferencesEditor.apply();
                } else {
                    showToast("failed to connect");

                    /* show all strings the user gave, this will later be stored to a secure database and checked for validation */
                    showToast(name);
                    showToast(email);
                    showToast(password);
                }


            }
        });

        newemail_cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    /* show debug output in  specific view */
    private void showSnackbar(View View, String text) {
        Snackbar.make(View, text, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    /* like showSnackbar(), but global and uglier */
    private void showToast(String text) {
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void selectedMessage(Message messages, EmailViewModel emailViewModel) {

        DialogFragment dialog = MessageShowFragment.newInstance(messages, mEmailViewModel);
        dialog.show(getSupportFragmentManager(), "tag");

    }
}

