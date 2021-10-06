package com.noahvogt.miniprojekt.data;

import android.os.AsyncTask;

import static com.noahvogt.miniprojekt.MainActivity.mEmailViewModel;

public class DeleteThread extends AsyncTask<Boolean, Void, Void> implements Runnable {

    @Override
    public void run() {
       /* for (int delete = 0; delete <= mEmailViewModel.getAllMessages().size(); delete++){
            mEmailViewModel.deleteMessage(mEmailViewModel.getAllMessages().get(delete));
        }

        */
    }

    @Override
    protected Void doInBackground(Boolean... booleans) {

        return null;
    }
}
