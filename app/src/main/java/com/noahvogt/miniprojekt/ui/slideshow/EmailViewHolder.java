package com.noahvogt.miniprojekt.ui.slideshow;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.noahvogt.miniprojekt.MainActivity;
import com.noahvogt.miniprojekt.ui.show.MessageShowFragment;
import com.noahvogt.miniprojekt.R;

import androidx.appcompat.app.AlertDialog;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.RecyclerView;

/* adds the content to the View of RecyclerView*/
public class EmailViewHolder extends RecyclerView.ViewHolder {
    private final TextView fromItemView;
    private final TextView subjectItemView;
    private final TextView dateItemView;
    public final TextView messageItemView;

    private AppBarConfiguration mAppBarConfiguration;


    private EmailViewHolder(View itemView, ViewGroup parent) {
        super(itemView);
        fromItemView = itemView.findViewById(R.id.textView);
        subjectItemView = itemView.findViewById(R.id.subject);
        dateItemView = itemView.findViewById(R.id.date);
        messageItemView = itemView.findViewById(R.id.message);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "clicked ViewHolder ", Toast.LENGTH_LONG).show();
              /*  Fragment fragment = new MessageShowFragment();
                if (!fragment.isAdded()){
                    Toast.makeText(v.getContext(), "is not Added ", Toast.LENGTH_LONG).show();

                    FragmentManager fragmentManager = fragment.getParentFragmentManager();
                    fragmentManager.beginTransaction()
                        .add(R.id.nav_show, MessageShowFragment.class, null)
                        .commit();
                }

               */
            }
        });
    }

    public void bind(String from, String subject, String date, String message) {
        fromItemView.setText(from);
        subjectItemView.setText(subject);
        dateItemView.setText(date);
        messageItemView.setText(message);
    }

    public static EmailViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_home, parent, false);
        return new EmailViewHolder(view, parent);
    }



   /* public void createNewEmailDialog(){
        // define View window
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(itemView.getContext());

        // init text field variables
        newemail_name = emailPopupView.findViewById(R.id.popup_material_name_asking_text);
        newemail_email = emailPopupView.findViewById(R.id.popup_material_email_asking_text);
        newemail_password = emailPopupView.findViewById(R.id.popup_material_password_asking_text);

        // init button variables
        Button newemail_save_button = (Button) emailPopupView.findViewById(R.id.saveButton);
        // may not be private
        Button newemail_cancel_button = (Button) emailPopupView.findViewById(R.id.cancelButton);

        // open View window
        dialogBuilder.setView(emailPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        // store user input
        newemail_save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // store user input (only needed for DEBUGGING)
                String name = newemail_name.getText().toString();
                String email = newemail_email.getText().toString();
                String password = newemail_password.getText().toString();

                if (!mailFunctions.validateEmail(newemail_email) | !mailFunctions.validateName(newemail_name) | !mailFunctions.validatePassword(newemail_password)) {
                    return;
                }

                // show all strings the user gave, this will later be stored to a secure database and checked for validation
                showToast(name);
                showToast(email);
                showToast(password);


                showSnackbar(emailPopupView,"save button clicked");
            }
        });

    */

    }


