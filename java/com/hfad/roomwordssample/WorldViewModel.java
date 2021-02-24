package com.hfad.roomwordssample;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class WorldViewModel extends AndroidViewModel {
    private  WordRepository mRepository;
    private LiveData<List<Word>> mAllWords;
    public WorldViewModel(@NonNull Application application) {
        super(application);
        mRepository = new WordRepository(application);
        mAllWords = mRepository.getAllWords();
    }
    LiveData<List<Word>> getAllWords(){
        return mAllWords;
    }
    public  void insert(Word word){
        mRepository.insert(word);
    }
    public void deleteAllWords(){
        mRepository.deleteAllWords();
    }
    public void deleteWord(Word word){
        mRepository.deleteWord(word);
    }
    public void update(Word word){mRepository.updateWord(word);
    }
}
