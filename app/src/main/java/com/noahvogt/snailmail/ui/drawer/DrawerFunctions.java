package com.noahvogt.snailmail.ui.drawer;

import static com.noahvogt.snailmail.MainActivity.mEmailViewModel;
import static com.noahvogt.snailmail.MainActivity.userGlobal;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.noahvogt.snailmail.R;
import com.noahvogt.snailmail.data.MailFunctions;
import com.noahvogt.snailmail.data.MailServerCredentials;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class DrawerFunctions {

    public void changeMailServerSettingsDialog(String name, String email, String password, View headerView, String imapHost, String smtpHost,
                                               int imapPort, int smtpPort, boolean wantToAddNew, LayoutInflater layoutInflater, Context context,
                                               SharedPreferences mailServerCredentials) {
        /* define View window */
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        final View changeMailServerSettingsView = layoutInflater.inflate(R.layout.mail_credentials_customizer, null);

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
                        headerView, mailServerCredentials, context, layoutInflater);
            } else {
                changeAccountCredentials(name, serverUsernameObject.getText().toString(), passwordObject.getText().toString(),
                        Integer.parseInt(incomingPortObject.getText().toString()), Integer.parseInt(outgoingPortObject.getText().toString()),
                        incomingServerObject.getText().toString(), outgoingServerObject.getText().toString(), rootChangeServerSettingsDialog,
                        headerView, email, mailServerCredentials, context);
            }
        });

        cancelButton.setOnClickListener(v -> rootChangeServerSettingsDialog.dismiss());
    }

    public boolean addNewAccountCredentials(String name, String email, String password, int imapPort, int smtpPort, String imapHost, String smtpHost,
                                            DialogInterface dialogContext, boolean wantConnectionFailedDialog, View headerView,
                                            SharedPreferences mailServerCredentials, Context context, LayoutInflater layoutInflater) {

        SharedPreferences.Editor credentialsEditor = mailServerCredentials.edit();

        /* connect to mail server */
        Toast.makeText(context, "Probe Connection", Toast.LENGTH_SHORT).show();
        if (MailFunctions.canConnect(imapHost, email, password) == Boolean.TRUE) {
            Toast.makeText(context, "Was able to connect", Toast.LENGTH_LONG).show();

            Gson gson = new Gson();

            /* read login credentials from SharedPreferences */
            SharedPreferences initialCredentialsReader = context.getSharedPreferences("Credentials", Context.MODE_PRIVATE);
            String initialReadJsonData = initialCredentialsReader.getString("data", "");
            Type credentialsType = new TypeToken<ArrayList<MailServerCredentials>>() {
            }.getType();
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
                updateNavHeaderText(headerView, context);
                dialogContext.dismiss();
                return true;
            } else {
                Toast.makeText(context, "You cannot add the same email twice", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            if (wantConnectionFailedDialog) {
                askForChangeMailServerSettingsDialog(name, email, password, headerView, layoutInflater, context, mailServerCredentials);
            } else {
                Toast.makeText(context, "Failed to get connection", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return false;
    }

    public void askForChangeMailServerSettingsDialog(String name, String email, String password, View headerView, LayoutInflater layoutInflater,
                                                     Context context, SharedPreferences mailServerCredentials) {
        /* define View window */
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);

        /* open View window */
        dialogBuilder.setTitle("failed to connect :(");
        dialogBuilder
                .setMessage("Do you want to further customize your mail server settings?")
                .setPositiveButton("Yes", (dialog, id) -> {
                    /*if this button is clicked, close the whole fragment */
                    changeMailServerSettingsDialog(name, email, password, headerView,
                            MailFunctions.getImapHostFromEmail(email), MailFunctions.getSmtpHostFromEmail(email), 993, 587,
                            true, layoutInflater, context, mailServerCredentials);
                })
                .setNegativeButton("No", (dialogInput, id) -> {
                    /* if this button is clicked, close the hole fragment */
                    dialogInput.dismiss();
                });
        AlertDialog rootAskForChangeServerDialog = dialogBuilder.create();
        rootAskForChangeServerDialog.show();
    }

    /* set nav header name + email to current user data */
    public void updateNavHeaderText(View headerView, Context context) {
        SharedPreferences mailServerCredentials = context.getSharedPreferences("Credentials", Context.MODE_PRIVATE);

        TextView navHeaderNameObject = (TextView) headerView.findViewById(R.id.navHeaderName);
        TextView navHeaderEmailObject = (TextView) headerView.findViewById(R.id.navHeaderEmail);

        String startupUser = mailServerCredentials.getString("currentUser", "");

        if (!startupUser.isEmpty()) {
            Gson gson = new Gson();

            String startupJsonData = mailServerCredentials.getString("data", "");
            Type credentialsType = new TypeToken<ArrayList<MailServerCredentials>>() {
            }.getType();
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

    /* use 'initialMail' variable so that the program knows which email entry is has to change */
    public void changeAccountCredentials(String name, String email, String password, int imapPort, int smtpPort, String imapHost,
                                         String smtpHost, DialogInterface dialogContext, View headerView, String initialEmail,
                                         SharedPreferences mailServerCredentials, Context context) {
        SharedPreferences.Editor credentialsEditor = mailServerCredentials.edit();

        /* connect to mail server */
        Toast.makeText(context, "Probe Connection ...", Toast.LENGTH_SHORT).show();
        if (MailFunctions.canConnect(imapHost, email, password) == Boolean.TRUE) {
            Toast.makeText(context, "Was able to connect", Toast.LENGTH_SHORT).show();

            Gson gson = new Gson();

            /* read login credentials from SharedPreferences */
            SharedPreferences initialCredentialsReader = context.getSharedPreferences("Credentials", Context.MODE_PRIVATE);
            String initialReadJsonData = initialCredentialsReader.getString("data", "");
            Type credentialsType = new TypeToken<ArrayList<MailServerCredentials>>() {
            }.getType();
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
            Toast.makeText(context, "Failed to get connection", Toast.LENGTH_SHORT).show();
        }
    }

    public void createNewEmailDialog(View headerView, LayoutInflater layoutInflater, Context context) {
        /* define View window */
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        final View emailPopupView = layoutInflater.inflate(R.layout.add_email_popup, null);

        /* init text field variables */
        EditText newEmailName = emailPopupView.findViewById(R.id.popup_material_name_asking_text);
        EditText newEmailAddress = emailPopupView.findViewById(R.id.popup_material_email_asking_text);
        EditText newEmailPassword = emailPopupView.findViewById(R.id.popup_material_password_asking_text);

        /* init button variables */
        Button newEmailSaveButton = (Button) emailPopupView.findViewById(R.id.saveButton);
        /* may not be private */
        Button newEmailCancelButton = (Button) emailPopupView.findViewById(R.id.cancelButton);


        /* open View window */
        dialogBuilder.setView(emailPopupView);
        AlertDialog rootCreateNewEmailPopupDialog = dialogBuilder.create();
        rootCreateNewEmailPopupDialog.show();

        SharedPreferences mailServerCredentials = context.getSharedPreferences("Credentials", Context.MODE_PRIVATE);

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
                    MailFunctions.getSmtpHostFromEmail(email), rootCreateNewEmailPopupDialog, true, headerView, mailServerCredentials,
                    context, layoutInflater);
            if (connection) {
                Toast.makeText(context, "Downloading messages ...", Toast.LENGTH_SHORT).show();
                mEmailViewModel.applyDownload();
                userGlobal = email;
            }
        });

        newEmailCancelButton.setOnClickListener(v -> rootCreateNewEmailPopupDialog.dismiss());
    }

    public void accountManagerViewHandler(Context context, LayoutInflater layoutInflater, View headerView, SharedPreferences mailServerCredentials) {
        /* define dialog */
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        final View accountManagerView = layoutInflater.inflate(R.layout.account_manager, null);

        AutoCompleteTextView accountSelectorObject = (AutoCompleteTextView) accountManagerView.findViewById(R.id.accountSelectorTextView);

        /* get string data for drop down menu */
        SharedPreferences credReader = context.getSharedPreferences("Credentials", Context.MODE_PRIVATE);
        String currentUser = credReader.getString("currentUser", "");

        TextView showCurrentUserObject = (TextView) accountManagerView.findViewById(R.id.showCurrentUser);
        Button switchAccountObject = (Button) accountManagerView.findViewById(R.id.switchToAccountButton);
        Button deleteAccountObject = (Button) accountManagerView.findViewById(R.id.deleteAccountButton);
        Button changeServerSettingsObject = (Button) accountManagerView.findViewById(R.id.changeServerSettingsButton);
        Button exit = (Button) accountManagerView.findViewById(R.id.exitButton);

        if (currentUser == null)
            showCurrentUserObject.setText("current user:\nNone");
        else {
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
        } catch(NullPointerException ignored) {}

        ArrayAdapter<String> dropDownAdapter = new ArrayAdapter<>(context, R.layout.dropdown_item,
                R.id.textViewDropDownItem, userArray);
                accountSelectorObject.setAdapter(dropDownAdapter);

        /* open dialog */
        dialogBuilder.setView(accountManagerView);
        AlertDialog rootAccountManagerDialog = dialogBuilder.create();
        rootAccountManagerDialog.show();

       switchAccountObject.setOnClickListener(view -> {
            String userInput = accountSelectorObject.getText().toString();
            String jsonCredData1 = credReader.getString("data", "");
            Type credentialsType1 = new TypeToken<ArrayList<MailServerCredentials>>() {}.getType();
            ArrayList<MailServerCredentials> currentUsersCredentials1 = gson.fromJson(jsonCredData1, credentialsType1);

            try {
                for (int i = 0; i < currentUsersCredentials1.size(); i++) {
                    if (currentUsersCredentials1.get(i).getUsername().equals(userInput)) {
                        credEditor.putString("currentUser", userInput).apply();
                        userGlobal = userInput;
                        showCurrentUserObject.setText(String.format("current user:\n%s", userInput));
                        Toast.makeText(context,"Switched account", Toast.LENGTH_SHORT).show();
                        updateNavHeaderText(headerView, context);
                        break;
                    }
                }
            } catch (NullPointerException ignored) {
            }
        });

        /* needed to use array in inner method later */
        String[] finalUserArray = userArray;

        deleteAccountObject.setOnClickListener(view -> {
            String userInput = accountSelectorObject.getText().toString();
            String jsonCredData12 = credReader.getString("data", "");
            Type credentialsType12 = new TypeToken<ArrayList<MailServerCredentials>>() {}.getType();
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

                        ArrayAdapter<String> newDropDownAdapter = new ArrayAdapter<>(context.getApplicationContext(), R.layout.dropdown_item,
                                                                                     R.id.textViewDropDownItem, newUserArray);
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
                        Toast.makeText(context,"Account removed", Toast.LENGTH_SHORT).show();
                        updateNavHeaderText(headerView, context);
                        break;
                    }
                }
            } catch (NullPointerException ignored) {}
        });

        changeServerSettingsObject.setOnClickListener(view -> {
            String userInput = accountSelectorObject.getText().toString();
            String jsonCredData13 = credReader.getString("data", "");
            Type credentialsType13 = new TypeToken<ArrayList<MailServerCredentials>>() {}.getType();
            ArrayList<MailServerCredentials> currentUserCredentials = gson.fromJson(jsonCredData13, credentialsType13);

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
                                smtpPort, false, layoutInflater, context, mailServerCredentials);
                        break;
                    }
                }
            } catch (NullPointerException ignored) {}
        });

        exit.setOnClickListener(v ->rootAccountManagerDialog.dismiss());
    }

    public void newUserAlphaSoftwareWarningBeforeAddingEmail(boolean button, View view, Context context, LayoutInflater layoutInflater) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        final View emailPopupView = layoutInflater.inflate(R.layout.new_user_welcome_message, null);

        DrawerFunctions drawerFunctions = new DrawerFunctions();

        /* init text field variables */
        TextView shedText = emailPopupView.findViewById(R.id.background);
        Button okayButton = emailPopupView.findViewById(R.id.okay_button);

        /* open View window */
        dialogBuilder.setView(emailPopupView);
        AlertDialog alphaWarningDialog = dialogBuilder.create();
        alphaWarningDialog.show();

        okayButton.setOnClickListener(v -> {
            alphaWarningDialog.dismiss();
            if (view!=null) {
                if (button) {
                    drawerFunctions.createNewEmailDialog(view, layoutInflater, context);
                }
            }
        });
    }

    public boolean handleMasterDotsMenu(MenuItem menuItem, Context context, LayoutInflater layoutInflater) {
        switch (menuItem.getItemId()){
            case R.id.action_information:
                newUserAlphaSoftwareWarningBeforeAddingEmail(false, null, context, layoutInflater);
                return true;
            case R.id.action_refresh:
                Toast.makeText(context, "Refreshing", Toast.LENGTH_SHORT).show();
                mEmailViewModel.applyDownload();
                return true;
            case R.id.action_deletefolder:
                Toast.makeText(context, "Deleting all folders", Toast.LENGTH_SHORT).show();
                for (int delete = 0; delete < mEmailViewModel.getAll(false).size(); delete++) {
                    mEmailViewModel.deleteMessage(mEmailViewModel.getAll(false).get(delete));
                }
                mEmailViewModel.getAll(true);
                return true;
        }
        return false;
    }
}
