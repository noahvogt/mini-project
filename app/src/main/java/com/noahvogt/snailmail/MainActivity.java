package com.noahvogt.snailmail;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.reflect.TypeToken;
import com.noahvogt.snailmail.database.Message;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.noahvogt.snailmail.data.CustomAdapter;
import com.noahvogt.snailmail.data.EmailViewModel;
import com.noahvogt.snailmail.data.MailFunctions;
import com.noahvogt.snailmail.data.MailServerCredentials;
import com.noahvogt.snailmail.ui.settings.SettingsActivity;
import com.noahvogt.snailmail.workers.DownloadWorker;
import com.noahvogt.snailmail.ui.reader.ReaderFragment;
import com.noahvogt.snailmail.ui.editor.EditorFragment;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.google.gson.Gson;



import static com.noahvogt.snailmail.R.id.drawer_layout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, CustomAdapter.SelectedMessage {

    private AppBarConfiguration mAppBarConfiguration;

    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;

    public static EmailViewModel mEmailViewModel;
    public static RecyclerView recyclerView;

    public static String userGlobal;
    public static boolean isDownloading = false;

    private AlertDialog dialog;
    private EditText newEmailName, newEmailAddress, newEmailPassword; /* may not be private */

    SharedPreferences mailServerCredentials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);


        /* invoke toolbar */
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /* invoke drawer */
        DrawerLayout drawer = findViewById(drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);

        /*
         Passing each menu ID as a set of Ids because each
         menu should be considered as top level destinations.
        */
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_archive, R.id.nav_spam)
                .setDrawerLayout(drawer)
                .build();
        final NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        final NavController navController = navHostFragment.getNavController();
        //NavController navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        /* show account manager when clicking on profile */
        View accountView = findViewById(R.id.accountView);
        accountView.setOnClickListener(onClickView -> {
            /* define dialog */
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
            final View accountManagerView = getLayoutInflater().inflate(R.layout.account_manager, null);

            AutoCompleteTextView accountSelectorObject = (AutoCompleteTextView) accountManagerView.findViewById(R.id.accountSelectorTextView);

            /* get string data for drop down menu */
            SharedPreferences credReader = getSharedPreferences("Credentials", Context.MODE_PRIVATE);
            String currentUser = credReader.getString("currentUser", "");

            TextView showCurrentUserObject = (TextView) accountManagerView.findViewById(R.id.showCurrentUser);
            Button switchAccountObject = (Button) accountManagerView.findViewById(R.id.switchToAccountButton);
            Button deleteAccountObject = (Button) accountManagerView.findViewById(R.id.deleteAccountButton);
            Button changeServerSettingsObject = (Button) accountManagerView.findViewById(R.id.changeServerSettingsButton);
            Button exit = (Button) accountManagerView.findViewById(R.id.exitButton);

            if (currentUser == null) {
                showCurrentUserObject.setText("current user:\nNone");
            } else {
                showCurrentUserObject.setText(String.format("current user:\n%s", currentUser));
                userGlobal = currentUser;
            }

            SharedPreferences.Editor credEditor = credReader.edit();

            Gson gson = new Gson();

            String jsonCredData = credReader.getString("data", "");
            Type credentialsType = new TypeToken<ArrayList<MailServerCredentials>>() {
            }.getType();
            ArrayList<MailServerCredentials> currentUsersCredentials = gson.fromJson(jsonCredData, credentialsType);
            String[] userArray = new String[0];
            try {
                if (!currentUsersCredentials.isEmpty()) {
                    userArray = new String[currentUsersCredentials.size()];
                    for (int i = 0; i < currentUsersCredentials.size(); i++) {
                        userArray[i] = currentUsersCredentials.get(i).getUsername();
                    }
                }
            } catch (NullPointerException ignored) {}

            ArrayAdapter<String> dropDownAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.dropdown_item,
                    R.id.textViewDropDownItem, userArray);
            accountSelectorObject.setAdapter(dropDownAdapter);

            /* open dialog */
            dialogBuilder.setView(accountManagerView);
            AlertDialog rootAccountManagerDialog = dialogBuilder.create();
            rootAccountManagerDialog.show();

            switchAccountObject.setOnClickListener(view -> {
                String userInput = accountSelectorObject.getText().toString();
                String jsonCredData1 = credReader.getString("data", "");
                Type credentialsType1 = new TypeToken<ArrayList<MailServerCredentials>>(){}.getType();
                ArrayList<MailServerCredentials> currentUsersCredentials1 = gson.fromJson(jsonCredData1, credentialsType1);


                try {
                    for (int i = 0; i < currentUsersCredentials1.size(); i++) {
                        if (currentUsersCredentials1.get(i).getUsername().equals(userInput)) {
                            credEditor.putString("currentUser", userInput).apply();
                            userGlobal = userInput;
                            showCurrentUserObject.setText(String.format("current user:\n%s", userInput));
                            showToast("Switched account");
                            updateNavHeaderText(headerView);
                            break;
                        }
                    }
                } catch (NullPointerException ignored) {}
            });

            /* needed to use array in inner method later */
            String[] finalUserArray = userArray;

            deleteAccountObject.setOnClickListener(view -> {
                String userInput = accountSelectorObject.getText().toString();
                String jsonCredData12 = credReader.getString("data", "");
                Type credentialsType12 = new TypeToken<ArrayList<MailServerCredentials>>() {
                }.getType();
                ArrayList<MailServerCredentials> currentUserCredentials = gson.fromJson(jsonCredData12, credentialsType12);

                try {
                    for (int i = 0; i < currentUserCredentials.size(); i++) {
                        if (currentUserCredentials.get(i).getUsername().equals(userInput)) {
                            currentUserCredentials.remove(i);

                            /* live update adapter for dropdown menu */
                            int k = 0;
                            String[] newUserArray = new String[finalUserArray.length - 1];
                            for (String s : finalUserArray) {
                                if (!s.contains(userInput)) {
                                    newUserArray[k] = s;
                                    k++;
                                }
                            }

                            ArrayAdapter<String> newDropDownAdapter = new ArrayAdapter<>(MainActivity.this.getApplicationContext(),
                                    R.layout.dropdown_item, R.id.textViewDropDownItem, newUserArray);
                            accountSelectorObject.setAdapter(newDropDownAdapter);

                            showCurrentUserObject.setText(R.string.NoEmailsInDropDownMenuAvailable);

                            /* update credentials strings */
                            credEditor.putString("data", gson.toJson(currentUserCredentials, credentialsType12)).apply();
                            if (!currentUserCredentials.isEmpty()) {
                                String usernameZero = currentUserCredentials.get(0).getUsername();
                                credEditor.putString("currentUser", usernameZero).apply();
                                showCurrentUserObject.setText(String.format("current user:\n%s", usernameZero));
                                userGlobal = usernameZero;
                            } else {
                                credEditor.putString("currentUser", "").apply();
                                showCurrentUserObject.setText("current user:\n None");
                                userGlobal = null;
                            }
                            MainActivity.this.showToast("Account removed");
                            MainActivity.this.updateNavHeaderText(headerView);
                            break;
                        }
                    }
                } catch (NullPointerException ignored) {
                }
            });

            changeServerSettingsObject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String userInput = accountSelectorObject.getText().toString();
                    String jsonCredData = credReader.getString("data", "");
                    Type credentialsType = new TypeToken<ArrayList<MailServerCredentials>>() {
                    }.getType();
                    ArrayList<MailServerCredentials> currentUserCredentials = gson.fromJson(jsonCredData, credentialsType);

                    String email, name, password, smtpHost, imapHost = null;
                    int smtpPort, imapPort = 0;
                    try {
                        for (int i = 0; i < currentUserCredentials.size(); i++) {
                            if (currentUserCredentials.get(i).getUsername().equals(userInput)) {
                                email = currentUserCredentials.get(i).getUsername();
                                name = currentUserCredentials.get(i).getName();
                                password = currentUserCredentials.get(i).getPassword();
                                smtpHost = currentUserCredentials.get(i).getSmtpHost();
                                imapHost = currentUserCredentials.get(i).getImapHost();
                                smtpPort = currentUserCredentials.get(i).getSmtpPort();
                                imapPort = currentUserCredentials.get(i).getImapPort();
                                changeMailServerSettingsDialog(name, email, password, headerView, imapHost, smtpHost, imapPort,
                                        smtpPort, false);
                                break;
                            }
                        }
                    } catch (NullPointerException ignored) {}
                }
            });

            exit.setOnClickListener(v -> rootAccountManagerDialog.dismiss());
        });


        /* Lookup the recyclerview in activity layout */
        recyclerView = findViewById(R.id.recyclerView);

        final CustomAdapter adapter = new CustomAdapter(new CustomAdapter.EmailDiff(),this);

        /* Attach the adapter to the recyclerview to populate items */
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        /* get Inbox Messages in RecycleViewer at beginning is overwritten by Fragments but has to stay*/
        mEmailViewModel = new ViewModelProvider(this).get(EmailViewModel.class);
        mEmailViewModel.getInboxMessage().observe(this, messages -> {
            /* Update the cached copy of the messages in the adapter*/
            adapter.submitList(messages);
        });

        updateNavHeaderText(headerView);

        Button settingButton = findViewById(R.id.settingsButton);
        settingButton.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(i);
        });

        final Boolean[] clicked = {false};
        Button add_email_button = (Button) findViewById(R.id.addEmailButton);
        add_email_button.setOnClickListener(v -> {
            if (!clicked[0]){

                createInformation(true,headerView);
                clicked[0] = true;


            } else {
                createNewEmailDialog(headerView);
            }
        });


        /* Start email Writer*/
        FloatingActionButton message_create_button = findViewById(R.id.messageButton);
        message_create_button.setOnClickListener(v -> {
            DialogFragment dialogFragment = EditorFragment.newInstance();
            dialogFragment.show(getSupportFragmentManager(), "tag");
        });

        /* start python instance right on startup */
        if (! Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }
    }

        /* gets the data from the Email writer and adds it to the Database */
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, EditorFragment.replyIntent);

            /* Creates class for the Date when Email is written */
            Date dNow = new Date();
            SimpleDateFormat ft =
                    new SimpleDateFormat("dd.MM.yy");

                Message word = new Message(userGlobal,
                        EditorFragment.replyIntent.getStringExtra(EditorFragment.EXTRA_TO),
                        EditorFragment.replyIntent.getStringExtra(EditorFragment.EXTRA_CC),
                        EditorFragment.replyIntent.getStringExtra(EditorFragment.EXTRA_BCC),
                        EditorFragment.replyIntent.getStringExtra(EditorFragment.EXTRA_FROM),
                        ft.format(dNow),
                        EditorFragment.replyIntent.getStringExtra(EditorFragment.EXTRA_SUBJECT),
                        EditorFragment.replyIntent.getStringExtra(EditorFragment.EXTRA_MESSAGE),
                        "Draft",false);
                mEmailViewModel.insert(word);
        }

    /* set nav header name + email to current user data */
    public void updateNavHeaderText(View headerView) {
        mailServerCredentials = getSharedPreferences("Credentials", Context.MODE_PRIVATE);

        TextView navHeaderNameObject = (TextView) headerView.findViewById(R.id.navHeaderName);
        TextView navHeaderEmailObject = (TextView) headerView.findViewById(R.id.navHeaderEmail);

        String startupUser = mailServerCredentials.getString("currentUser","");

        if (!startupUser.isEmpty()) {
            Gson gson = new Gson();

            String startupJsonData = mailServerCredentials.getString("data", "");
            Type credentialsType = new TypeToken<ArrayList<MailServerCredentials>>(){}.getType();
            ArrayList<MailServerCredentials> startupUsersCredentials = gson.fromJson(startupJsonData, credentialsType);

            for (int i = 0; i < startupUsersCredentials.size(); i++) {
                if (startupUsersCredentials.get(i).getUsername().equals(startupUser)) {
                    navHeaderNameObject.setText(startupUser);
                    navHeaderEmailObject.setText(startupUsersCredentials.get(i).getName());
                    break;
                }
            }
        } else {
            navHeaderEmailObject.setText(R.string.noAccountsAddedNavHeaderMessageEmail);
            navHeaderNameObject.setText(R.string.noAccountsAddedNavHeaderMessageName);
        }
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
    public void onClick(View view) {}

    public void changeMailServerSettingsDialog(String name, String email, String password, View headerView, String imapHost,
                                               String smtpHost, int imapPort, int smtpPort, boolean wantToAddNew) {
        /* define View window */
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final View changeMailServerSettingsView = getLayoutInflater().inflate(R.layout.mail_credentials_customizer, null);

        /* access objects */
        EditText incomingServerObject = (EditText) changeMailServerSettingsView.findViewById(R.id.custom_mail_server_incoming_server_text);
        EditText outgoingServerObject = (EditText) changeMailServerSettingsView.findViewById(R.id.custom_mail_server_outgoing_server_text);
        EditText incomingPortObject = (EditText) changeMailServerSettingsView.findViewById(R.id.custom_mail_server_incoming_port_text);
        EditText outgoingPortObject = (EditText) changeMailServerSettingsView.findViewById(R.id.custom_mail_server_outgoing_port_text);
        EditText serverUsernameObject = (EditText) changeMailServerSettingsView.findViewById(R.id.custom_mail_server_username_text);
        EditText passwordObject = (EditText) changeMailServerSettingsView.findViewById(R.id.custom_mail_server_password_text);
        Button saveButton = (Button) changeMailServerSettingsView.findViewById(R.id.saveCustomizeButton);
        Button cancelButton = (Button) changeMailServerSettingsView.findViewById(R.id.cancelCustomizeButton);

        /* set assumed input in corresponding fields */
        incomingServerObject.setText(imapHost);
        outgoingServerObject.setText(smtpHost);
        incomingPortObject.setText(String.valueOf(imapPort));
        outgoingPortObject.setText(String.valueOf(smtpPort));
        serverUsernameObject.setText(email);
        passwordObject.setText(password);

        /* open View window */
        dialogBuilder.setView(changeMailServerSettingsView);
        AlertDialog rootChangeServerSettingsDialog = dialogBuilder.create();
        rootChangeServerSettingsDialog.show();

        saveButton.setOnClickListener(view -> {
            if (wantToAddNew) {
                addNewAccountCredentials(name, serverUsernameObject.getText().toString(), passwordObject.getText().toString(),
                        Integer.parseInt(incomingPortObject.getText().toString()), Integer.parseInt(outgoingPortObject.getText().toString()),
                        incomingServerObject.getText().toString(), outgoingServerObject.getText().toString(), rootChangeServerSettingsDialog, false,
                        headerView);
            } else {
                changeAccountCredentials(name, serverUsernameObject.getText().toString(),
                        passwordObject.getText().toString(), Integer.parseInt(incomingPortObject.getText().toString()),
                        Integer.parseInt(outgoingPortObject.getText().toString()), incomingServerObject.getText().toString(),
                        outgoingServerObject.getText().toString(), rootChangeServerSettingsDialog, headerView, email);
            }
        });

        cancelButton.setOnClickListener(v -> rootChangeServerSettingsDialog.dismiss());
    }

    public void askForChangeMailServerSettingsDialog(String name, String email, String password, View headerView) {
        /* define View window */
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        /* open View window */
        dialogBuilder.setTitle("failed to connect :(");
        dialogBuilder
                .setMessage("Do you want to further customize your mail server settings?")
                .setPositiveButton("Yes", (dialog, id) -> {
                    /*if this button is clicked, close the whole fragment */
                    changeMailServerSettingsDialog(name, email, password, headerView,
                            MailFunctions.getImapHostFromEmail(email), MailFunctions.getSmtpHostFromEmail(email), 993, 587,
                            true);
                })
                .setNegativeButton("No", (dialogInput, id) -> {
                    /* if this button is clicked, close the hole fragment */
                    dialogInput.dismiss();
                });
        AlertDialog rootAskForChangeServerDialog = dialogBuilder.create();
        rootAskForChangeServerDialog.show();
    }

    public void createInformation(boolean button, View view){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        final View emailPopupView = getLayoutInflater().inflate(R.layout.new_user_welcome_message, null);

        /* init text field variables */
        TextView shedText = emailPopupView.findViewById(R.id.background);
        Button okayButton = emailPopupView.findViewById(R.id.okay_button);

        /* open View window */
        dialogBuilder.setView(emailPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        okayButton.setOnClickListener(v -> {
            dialog.dismiss();
            if (view!=null) {
                if (button) {
                    createNewEmailDialog(view);
                }
            }
        });
    }

    public static MailServerCredentials newMailServerCredentials;
    public static SharedPreferences.Editor credentialsEditor;

    public boolean addNewAccountCredentials(String name, String email, String password, int imapPort,
                                         int smtpPort, String imapHost, String smtpHost, DialogInterface dialogContext,
                                         boolean wantConnectionFailedDialog, View headerView) {
        credentialsEditor = mailServerCredentials.edit();

        /* connect to mail server */
        showToast("Probe Connection ...");
        if (MailFunctions.canConnect(imapHost, email, password) == Boolean.TRUE) {
            showToast("Was able to connect");

            Gson gson = new Gson();

            /* read login credentials from SharedPreferences */
            SharedPreferences initialCredentialsReader = getSharedPreferences(
                    "Credentials", Context.MODE_PRIVATE);
            String initialReadJsonData = initialCredentialsReader.getString("data", "");
            Type credentialsType = new TypeToken<ArrayList<MailServerCredentials>>(){}.getType();
            ArrayList<MailServerCredentials> allUsersCredentials = gson.fromJson(initialReadJsonData, credentialsType);

            /* check for unique email */
            boolean newEmailIsUnique = true;
            try {
                for (int i = 0; i < allUsersCredentials.size(); i++) {
                    if (allUsersCredentials.get(i).getUsername().equals(email)) {
                        newEmailIsUnique = false;
                        break;
                    }
                }
            } catch (NullPointerException e) {
                System.out.println("creating new arraylist for user credentials, as it seems to be empty");
                allUsersCredentials = new ArrayList<>();
            }

            /* add new email account if the email hasn't been entered before */
            if (newEmailIsUnique) {
                allUsersCredentials.add(new MailServerCredentials(name, email, password, imapHost, smtpHost, imapPort,
                        smtpPort, ""));
                credentialsEditor.putString("data", gson.toJson(allUsersCredentials, credentialsType));
                credentialsEditor.putString("currentUser", email);
                credentialsEditor.apply();
                updateNavHeaderText(headerView);
                dialogContext.dismiss();
                return true;
            } else {
                showToast("You cannot add the same email twice");
                return false;
            }
        } else {
            if (wantConnectionFailedDialog) {
                askForChangeMailServerSettingsDialog(name, email, password, headerView);
            } else {
                showToast("Failed to get connection");
                return false;
            }
        }
        return false;
    }

    /* use 'initialMail' variable so that the program knows which email entry is has to change */
    public void changeAccountCredentials(String name, String email, String password, int imapPort,
                                         int smtpPort, String imapHost, String smtpHost, DialogInterface dialogContext,
                                         View headerView, String initialEmail) {
        credentialsEditor = mailServerCredentials.edit();

        /* connect to mail server */
        showToast("Probe Connection ...");
        if (MailFunctions.canConnect(imapHost, email, password) == Boolean.TRUE) {
            showToast("Was able to connect");

            Gson gson = new Gson();

            /* read login credentials from SharedPreferences */
            SharedPreferences initialCredentialsReader = getSharedPreferences(
                    "Credentials", Context.MODE_PRIVATE);
            String initialReadJsonData = initialCredentialsReader.getString("data", "");
            Type credentialsType = new TypeToken<ArrayList<MailServerCredentials>>(){}.getType();
            ArrayList<MailServerCredentials> allUsersCredentials = gson.fromJson(initialReadJsonData, credentialsType);

            for (int i = 0; i < allUsersCredentials.size(); i++) {
                if (allUsersCredentials.get(i).getUsername().equals(initialEmail)) {
                    String signature = allUsersCredentials.get(i).getSignature();
                    allUsersCredentials.set(i, new MailServerCredentials(name, email, password, imapHost, smtpHost, imapPort,
                            smtpPort, signature));
                    credentialsEditor.putString("data", gson.toJson(allUsersCredentials, credentialsType)).apply();
                    dialogContext.dismiss();
                    break;
                }
            }
        } else {
                showToast("Failed to get connection");
        }
    }

    public void createNewEmailDialog(View headerView){
        /* define View window */
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final View emailPopupView = getLayoutInflater().inflate(R.layout.add_email_popup, null);

        /* init text field variables */
        newEmailName = emailPopupView.findViewById(R.id.popup_material_name_asking_text);
        newEmailAddress = emailPopupView.findViewById(R.id.popup_material_email_asking_text);
        newEmailPassword = emailPopupView.findViewById(R.id.popup_material_password_asking_text);

        /* init button variables */
        Button newEmailSaveButton = (Button) emailPopupView.findViewById(R.id.saveButton);
        /* may not be private */
        Button newEmailCancelButton = (Button) emailPopupView.findViewById(R.id.cancelButton);


        /* open View window */
        dialogBuilder.setView(emailPopupView);
        AlertDialog rootCreateNewEmailPopupDialog = dialogBuilder.create();
        rootCreateNewEmailPopupDialog.show();

        credentialsEditor = mailServerCredentials.edit();

        /* store user input */
        newEmailSaveButton.setOnClickListener(v -> {
            /* store user input */
            String name = newEmailName.getText().toString();
            String email = newEmailAddress.getText().toString();
            String password = newEmailPassword.getText().toString();

            if (!MailFunctions.validateEmail(newEmailAddress) | !MailFunctions.validateName(newEmailName) | !MailFunctions.validatePassword(newEmailPassword)) {
                return;
            }

            boolean connection = addNewAccountCredentials(name, email, password, 993, 587, MailFunctions.getImapHostFromEmail(email),
                    MailFunctions.getSmtpHostFromEmail(email), rootCreateNewEmailPopupDialog, true, headerView);
            if (connection) {
                showToast("Downloading messages");
                mEmailViewModel.applyDownload();
                userGlobal = email;
            }
            dialog.dismiss();
    });

        newEmailCancelButton.setOnClickListener(v -> rootCreateNewEmailPopupDialog.dismiss());
    }

    /* print relatively globally messages at the bottom of the screen */
    private void showToast(String text) {
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void selectedMessage(Message messages, EmailViewModel emailViewModel) {
        DialogFragment dialog = ReaderFragment.newInstance(messages, mEmailViewModel);
        dialog.show(getSupportFragmentManager(), "tag");
    }

    private static final String TAG = DownloadWorker.class.getSimpleName();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_information:
                createInformation(false,null);
                return true;
            case R.id.action_refresh:
                showToast("Refreshing");
                mEmailViewModel.applyDownload();
                return true;
            case R.id.action_deletefolder:
                showToast("Deleting all folders");
                for (int delete = 0; delete < mEmailViewModel.getAll(false).size(); delete++){
                    mEmailViewModel.deleteMessage(mEmailViewModel.getAll(false).get(delete));
                }
                mEmailViewModel.getAll(true);

                return true;
        }
        return false;
    }
}