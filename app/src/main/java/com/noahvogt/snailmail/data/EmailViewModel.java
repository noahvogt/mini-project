/* Note: this file is derived from a template located in the following repository:
 * https://github.com/googlecodelabs/android-room-with-a-view
 * 
 * This means was originally licensed under the Apache License 2.0, which can be found
 * in the text file 'APL' in the root directory of this repository. Any changes made to
 * this file however are licensed under the GNU General Public License Version 3.0,
 * which can be found in the file 'LICENSE' in the root directory of this repository.
 */

package com.noahvogt.snailmail.data;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.AndroidViewModel;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.noahvogt.snailmail.DataBase.EmailRepository;
import com.noahvogt.snailmail.DataBase.Message;
import com.noahvogt.snailmail.workers.DownloadWorker;

import java.util.ArrayList;
import java.util.List;

public class EmailViewModel extends AndroidViewModel {

    private EmailRepository mEmailRepository;
    private WorkManager mWorkManager;

    public List<Message> all =  new ArrayList<>();
    private LiveData<List<Message>> mDraftMessage;
    private LiveData<List<Message>> mInboxMessage;
    private LiveData<List<Message>> mSentMessage;
    private LiveData<List<Message>> mArchiveMessage;
    private LiveData<List<Message>> mSpamMessage;
    private List<Message> mAllMessages;

    String sent;
    String spam;
    String archive;
    String inbox;
    String drafts;

    public EmailViewModel(Application application) {
        super(application);
        mWorkManager = WorkManager.getInstance(application);
        mEmailRepository = new EmailRepository(application);
        mInboxMessage = mEmailRepository.getInboxMessages();
        mDraftMessage = mEmailRepository.getDraftMessages();
        mSentMessage = mEmailRepository.getSentMessages();
        mArchiveMessage = mEmailRepository.getArchiveMessages();
        mSpamMessage = mEmailRepository.getSpamMessage();
        //mAllMessages = mEmailRepository.getAllMessages();
    }

    /*requests Worker and applies password, email to it */
    public void applyDownload(){
        OneTimeWorkRequest downloadRequest =
                new OneTimeWorkRequest.Builder(DownloadWorker.class)
                        .build();

        mWorkManager.enqueue(downloadRequest);
    }

    public void setListAll(List<Message> messageListAll, String fragment){
        System.out.println("setListAll: size messageListAll input: " + messageListAll.size());
        System.out.println("setListAll: fragment: " + fragment);
        System.out.println("setListAll: variable inbox: " + inbox);
        if (sent == null && fragment.equals("Sent")){
            for (int i = 0; i < messageListAll.size(); i++) {
                this.all.add(messageListAll.get(i));
            }
            sent = fragment;
        }

        if (drafts == null && fragment.equals("Drafts")){
            for (int i = 0; i < messageListAll.size(); i++) {
                this.all.add(messageListAll.get(i));
            }
            drafts = fragment;
        }

        if (archive == null && fragment.equals("Archive")){
            for (int i = 0; i < messageListAll.size(); i++) {
                this.all.add(messageListAll.get(i));
            }
            archive = fragment;
        }
        if (inbox == null && fragment.equals("Inbox")){
            for (int i = 0; i < messageListAll.size(); i++) {
                this.all.add(messageListAll.get(i));
            }
            inbox = fragment;
        }
        if (spam == null && fragment.equals("Spam")){
            for (int i = 0; i < messageListAll.size(); i++) {
                this.all.add(messageListAll.get(i));
            }
            spam = fragment;
        }
        System.out.println("setListAll: size all output: " + all.size());
    }

    public List<Message> getAll(boolean status){
        System.out.println("getAll: Size of all Lists " + all.size());
        for (int i = 0; i < all.size(); i++) {
            System.out.println("EmailModel all list " + all.get(i) + "\n Size: " + all.size());
        }
        if (status){
            List<Message> emptyAll;
            emptyAll = all;
            all.clear();
            System.out.println("Size cleared list:" + all.size());
            return emptyAll;
        }
        return all;
    }

    public LiveData<List<Message>> getDraftMessage(){ return mDraftMessage; }

    public LiveData<List<Message>> getSpamMessage(){return mSpamMessage;}

    public LiveData<List<Message>> getInboxMessage(){ return mInboxMessage;}

    public LiveData<List<Message>> getSentMessage(){ return mSentMessage;}

    public LiveData<List<Message>> getArchiveMessage(){ return mArchiveMessage;}

    public List<Message> getAllMessages(){
        mAllMessages = mEmailRepository.getAllMessages();
        return mAllMessages;}

    public void insert(Message message){mEmailRepository.insert(message);}

    public void deleteMessage(Message message){mEmailRepository.deleteMessage(message);}

    public void updateFolder(int id, String folder){mEmailRepository.updateFolder(id, folder);}

    public void updateDate(int id, String date){ mEmailRepository.updateDate(id, date); }


}
