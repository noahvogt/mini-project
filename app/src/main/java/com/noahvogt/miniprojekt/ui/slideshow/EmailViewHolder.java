package com.noahvogt.miniprojekt.ui.slideshow;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.noahvogt.miniprojekt.ui.DataBase.Message;
import com.noahvogt.miniprojekt.ui.show.MessageShowFragment;
import com.noahvogt.miniprojekt.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

/* adds the content to the View of RecyclerView*/
public class EmailViewHolder extends RecyclerView.ViewHolder {
    private final TextView fromItemView;
    private final TextView subjectItemView;
    private final TextView dateItemView;
    private final TextView messageItemView;

    public static Message curent;

    private EmailViewHolder(View itemView) {
        super(itemView);
        fromItemView = itemView.findViewById(R.id.textView);
        subjectItemView = itemView.findViewById(R.id.subject);
        dateItemView = itemView.findViewById(R.id.date);
        messageItemView = itemView.findViewById(R.id.message);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), curent.getFrom(), Toast.LENGTH_LONG).show();

                AppCompatActivity activity = (AppCompatActivity) v.getContext();

                DialogFragment dialog = MessageShowFragment.newInstance(curent);
                dialog.show(activity.getSupportFragmentManager(), "tag");





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
        return new EmailViewHolder(view );
    }

    public static void putCurrent(Message current){
        curent = current;
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


