package com.noahvogt.miniprojekt.data;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.AndroidViewModel;

import com.noahvogt.miniprojekt.DataBase.EmailRepository;
import com.noahvogt.miniprojekt.DataBase.Message;

import java.util.List;

public class EmailViewModel extends AndroidViewModel {

    private EmailRepository mEmailRepository;

    private LiveData<List<Message>> mDraftMessage;
    private LiveData<List<Message>> mInboxMessage;
    private LiveData<List<Message>> mSentMessage;
    private LiveData<List<Message>> mArchiveMessage;
    private LiveData<List<Message>> mSpamMessage;

    public EmailViewModel(Application application) {
        super(application);
        mEmailRepository = new EmailRepository(application);
        mInboxMessage = mEmailRepository.getInboxMessages();
        mDraftMessage = mEmailRepository.getDraftMessages();
        mSentMessage = mEmailRepository.getSentMessages();
        mArchiveMessage = mEmailRepository.getArchiveMessages();
        mSpamMessage = mEmailRepository.getSpamMessage();
    }

    public LiveData<List<Message>> getDraftMessage(){
        return mDraftMessage;
    }

    public LiveData<List<Message>> getSpamMessage(){return mSpamMessage;}

    public LiveData<List<Message>> getInboxMessage(){ return mInboxMessage;}

    public LiveData<List<Message>> getSentMessage(){ return mSentMessage;}

    public LiveData<List<Message>> getArchiveMessage(){ return mArchiveMessage;}

    public void insert(Message message){mEmailRepository.insert(message);}

    public void deleteMessage(Message message){mEmailRepository.deleteMessage(message);}

    public void updateMessage(Message message){mEmailRepository.updateMessage(message);}
}