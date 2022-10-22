package com.noahvogt.snailmail.ui.mailboxes.draftFolder;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DraftViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public DraftViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
