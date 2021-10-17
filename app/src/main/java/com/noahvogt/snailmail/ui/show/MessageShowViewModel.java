package com.noahvogt.snailmail.ui.show;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MessageShowViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MessageShowViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is from Simon");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
