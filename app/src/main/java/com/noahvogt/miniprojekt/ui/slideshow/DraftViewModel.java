package com.noahvogt.miniprojekt.ui.slideshow;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.AndroidViewModel;

import com.noahvogt.miniprojekt.MainActivity;
import com.noahvogt.miniprojekt.ui.DataBase.EmailRepository;
import com.noahvogt.miniprojekt.ui.DataBase.Message;

import java.util.List;

public class DraftViewModel extends AndroidViewModel {

    private EmailRepository mEmailRepository;

    private final LiveData<List<Message>> mDraftMessage;

    public DraftViewModel(Application application) {
        super(application);
        mEmailRepository = new EmailRepository(application);
        mDraftMessage = mEmailRepository.getDraftMessages();
    }

    public LiveData<List<Message>> getDraftMessage(){return mDraftMessage;}

    public void insert(Message message){mEmailRepository.insert(message);}

}