/* Note: this file is derived from a template located in the following repository:
 * https://github.com/googlecodelabs/android-room-with-a-view
 * 
 * This means was originally licensed under the Apache License 2.0, which can be found
 * in the text file 'APL' in the root directory of this repository. Any changes made to
 * this file however are licensed under the GNU General Public License Version 3.0,
 * which can be found in the file 'LICENSE' in the root directory of this repository.
 */

package com.noahvogt.snailmail.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.noahvogt.snailmail.DataBase.Message;
import com.noahvogt.snailmail.MainActivity;
import com.noahvogt.snailmail.R;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/* adds the content to the View of RecyclerView*/
public class EmailViewHolder extends RecyclerView.ViewHolder {
    private final TextView fromItemView;
    private final TextView subjectItemView;
    private final TextView dateItemView;
    private final TextView messageItemView;
    private final String TAG = EmailViewHolder.class.getSimpleName();


    private EmailViewHolder(View itemView,
                            CustomAdapter.SelectedMessage selectedMessage,
                            List<Message> messageList) {
        super(itemView);
        fromItemView = itemView.findViewById(R.id.textView);
        subjectItemView = itemView.findViewById(R.id.subject);
        dateItemView = itemView.findViewById(R.id.date);
        messageItemView = itemView.findViewById(R.id.message);
        System.out.println("Called EmailViewHolder");
        
        itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    selectedMessage.selectedMessage(messageList.get(getBindingAdapterPosition()), null);
                } catch (Throwable throwable){
                    Toast.makeText(itemView.getContext(), "Could not open this Message", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error by opening Message", throwable);
                }
            }
        });
    }

    public void bind(String from, String subject, String date, String message) {
        System.out.println("length of from " + from.length());
        System.out.println("length of message " + message.length());


        if (subject.length() > 30){
            subject = subject.substring(0,26) + "...";
        }
        if (from.length() > 12) {
            from = from.substring(0,12) + "...";
        }
        if (message.length() > 30){
            message = message.substring(0,30) + "...";
        }
        /*if (date.length() > 9){
            date = "INVALID";
        }
        if (!isDate(date)){
            date = "INVALID";
        }

         */

        fromItemView.setText(from);
        subjectItemView.setText(subject);
        dateItemView.setText(date);
        messageItemView.setText(message);
    }

    public static EmailViewHolder create(ViewGroup parent,
                                         CustomAdapter.SelectedMessage selectedMessage,
                                         List<Message> messageList) {
         View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_home, parent, false);
        return new EmailViewHolder(view, selectedMessage, messageList);
    }

    public static boolean isDate(String date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy", Locale.getDefault());
        //dateFormat.setLenient(false);
        try {
            dateFormat.parse(date);
        } catch (ParseException e){
            return false;
        }

        return true;
    }



    }


