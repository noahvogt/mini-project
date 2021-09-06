package com.noahvogt.miniprojekt.ui.slideshow;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.AndroidViewModel;

import com.noahvogt.miniprojekt.MainActivity;
import com.noahvogt.miniprojekt.ui.DataBase.EmailRepository;
import com.noahvogt.miniprojekt.ui.DataBase.Message;

import java.util.List;

public class EmailViewModel extends AndroidViewModel {

    private EmailRepository mEmailRepository;

    private LiveData<List<Message>> mDraftMessage;
    private LiveData<List<Message>> mInboxMessage;
    private LiveData<List<Message>> mSentMessage;
    private LiveData<List<Message>> mArchiveMassage;

    public EmailViewModel(Application application) {
        super(application);
        mEmailRepository = new EmailRepository(application);
       mDraftMessage = mEmailRepository.getDraftMessages();
    }

    public LiveData<List<Message>> getDraftMessage(){
      //  mDraftMessage = mEmailRepository.getDraftMessages();
        return mDraftMessage;
    }

    public void deleteNewMessage(){
        mEmailRepository.deleteNewMessage();
    }

    public LiveData<List<Message>> getInboxMessage(){
        mInboxMessage = mEmailRepository.getInboxMessages();
        return mDraftMessage;}

    public LiveData<List<Message>> getSentMessage(){
        mSentMessage = mEmailRepository.getSentMessages();
        return mDraftMessage;}

    public LiveData<List<Message>> getArchiveMessage(){
        mArchiveMassage = mEmailRepository.getArchiveMessages();
        return mDraftMessage;}



    public void insert(Message message){mEmailRepository.insert(message);}

}