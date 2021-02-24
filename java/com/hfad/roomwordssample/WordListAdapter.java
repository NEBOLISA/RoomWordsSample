package com.hfad.roomwordssample;

import android.content.Context;
import android.graphics.drawable.LayerDrawable;
import android.icu.text.Transliterator;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.WordViewHolder> {
    private final LayoutInflater mInflater;
    private List<Word> mWord;
    private static ClickListener clickListener;
    WordListAdapter(Context context){
       // this.mWord = mWords;
        mInflater=LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public WordListAdapter.WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item,parent,false);
        return new WordViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WordListAdapter.WordViewHolder holder, int position) {
if(mWord!=null){
    Word current =mWord.get(position);
    holder.wordItemView.setText(current.getWord());
   // holder.wordItemView.setOnTouchListener(new OnSwipeTouchListener(MainActivity.class));
}else {
    holder.wordItemView.setText("No word");
}
    }
    void setWords(List<Word> words){
        mWord=words;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(mWord != null)
            return mWord.size();
        else return 0;
    }


    class WordViewHolder extends RecyclerView.ViewHolder{
private final TextView wordItemView;
        public WordViewHolder(@NonNull View itemView) {
            super(itemView);
            wordItemView = itemView.findViewById(R.id.textView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
clickListener.onItemClick(v,getAdapterPosition());
                }
            });

        }
    }
    public void setOnItemClickListener(ClickListener clickListener) {
        WordListAdapter.clickListener = clickListener;
    }
    public interface ClickListener {
        void onItemClick(View v, int position);
    }
    public Word getWordAtPosition(int position){
        return mWord.get(position);
    }
}
