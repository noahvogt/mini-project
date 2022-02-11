package com.noahvogt.snailmail.ui.reader;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ReaderViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ReaderViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is from Simon");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
