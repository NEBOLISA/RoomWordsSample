package com.hfad.roomwordssample;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class WordRepository {
    private WordDao mWordDao;
    private LiveData<List<Word>>mAllWords;
    WordRepository(Application application){
        WordRoomDatabase db =WordRoomDatabase.getDatabase(application);
        mWordDao=db.wordDao();
        mAllWords =mWordDao.getAllWords();
    }
    LiveData<List<Word>>getAllWords(){
        return mAllWords;
    }
   public void insert(Word word){
        new insertAsyncTask(mWordDao).execute(word);
   }
   private static class insertAsyncTask extends AsyncTask<Word,Void,Void>{
      private WordDao mAsyncTaskDao;
      insertAsyncTask(WordDao dao){
      mAsyncTaskDao = dao;
}
       @Override
       protected Void doInBackground(final Word... words) {
    mAsyncTaskDao.insert(words[0]);
           return null;
       }
   }
   private static class deleteAllWords extends AsyncTask<Void, Void ,Void>{
private WordDao mAsyncTaskDao;
       public deleteAllWords(WordDao dao) {
           mAsyncTaskDao = dao;
       }

       @Override
       protected Void doInBackground(Void... voids) {
           mAsyncTaskDao.deleteAll();
           return null;
       }
   }
   public void deleteAllWords(){
        new deleteAllWords(mWordDao).execute();
   }
   public void deleteWord(Word word){
        new deleteWord(mWordDao).execute(word);
   }
   public void updateWord(Word word){
        new updateWordAsyncTask(mWordDao).execute(word);
   }
   private static class deleteWord extends AsyncTask<Word, Void,Void>{
private WordDao mAsyncTaskDao;

       public deleteWord(WordDao Dao) {
           mAsyncTaskDao = Dao;
       }

       @Override
       protected Void doInBackground(Word... words) {
           mAsyncTaskDao.deleteWord(words[0]);
           return null;
       }
   }
private  static class updateWordAsyncTask extends AsyncTask<Word,Void,Void>{
private  WordDao mAsyncTaskDao;

    public updateWordAsyncTask(WordDao mAsyncTaskDao) {
        this.mAsyncTaskDao = mAsyncTaskDao;
    }

    @Override
    protected Void doInBackground(Word... words) {
        mAsyncTaskDao.update(words[0]);
        return null;
    }
}
}
