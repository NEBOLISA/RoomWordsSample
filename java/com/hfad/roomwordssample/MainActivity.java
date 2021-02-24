package com.hfad.roomwordssample;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceDataStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
static WorldViewModel mWordViewModel;
    WordListAdapter adapter;
    RecyclerView recyclerView;
    MyBroadcastService myBroadcastService = new MyBroadcastService();
private List<Word>mWords;
    private static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;
    public static final int UPDATE_WORD_ACTIVITY_REQUEST_CODE =2;
    public static final String EXTRA_DATA_UPDATE_WORD ="extra_word_to_be_updated";
    public static final String EXTRA_DATA_ID = "extra_data_id";
    Intent intent;
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(myBroadcastService);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // intent = getIntent();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        this.registerReceiver(myBroadcastService,intentFilter);
        recyclerView = findViewById(R.id.recyclerview);
        adapter = new WordListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FloatingActionButton fab = findViewById(R.id.fab);
        coordinatorLayout =(CoordinatorLayout)findViewById(R.id.snack_container);
       // SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.class);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewWordActivity.class);
                startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);

            }
            
        });
        mWordViewModel = ViewModelProviders.of(this).get(WorldViewModel.class);

        mWordViewModel.getAllWords().observe(this, new Observer<List<Word>>() {
            @Override
            public void onChanged(@Nullable final List<Word> words) {
                // Update the cached copy of the words in the adapter.

                // mWords=words;
                adapter.setWords(words);
            }
        });
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    int position = viewHolder.getAdapterPosition();
                    Word myWord = adapter.getWordAtPosition(position);
                    showSnackbar("Deleted " + myWord.getWord());
                    //makeToast(myWord.getWord());

                    //Toast.makeText(MainActivity.this, "Deleting " + myWord.getWord(),Toast.LENGTH_LONG).show();
                    mWordViewModel.deleteWord(myWord);
            }
        });
        helper.attachToRecyclerView(recyclerView);
adapter.setOnItemClickListener(new WordListAdapter.ClickListener() {
    @Override
    public void onItemClick(View v, int position) {
        Word word = adapter.getWordAtPosition(position);
        launchUpdateWordActivty(word);
    }
});

    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
intent =data;
        if (requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {

                Word word = new Word(data.getStringExtra(NewWordActivity.EXTRA_REPLY));
                mWordViewModel.insert(word);

        } else if(requestCode == UPDATE_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            String word_data = data.getStringExtra(NewWordActivity.EXTRA_REPLY);
            int id = data.getIntExtra(NewWordActivity.EXTRA_REPLY_ID, -1);
            if (id != -1) {
                mWordViewModel.update(new Word(id, word_data));
            } else {
                Toast.makeText(this, "Unable to update", Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
        }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this,SettingsActivity.class));
            return true;
        }
        if (id == R.id.action_delete) {
            Toast.makeText(this,"Clearing the Data .....", Toast.LENGTH_LONG).show();
            mWordViewModel.deleteAllWords();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
public void launchUpdateWordActivty(Word word){
        Intent intent = new Intent(this, NewWordActivity.class);
        intent.putExtra(EXTRA_DATA_UPDATE_WORD,word.getWord());
        intent.putExtra(EXTRA_DATA_ID,word.getId());
        startActivityForResult(intent,UPDATE_WORD_ACTIVITY_REQUEST_CODE);
}
void makeToast(String word){
    LayoutInflater inflater = getLayoutInflater();
    View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup)findViewById(R.id.custom_toast));
    TextView text = (TextView)layout.findViewById(R.id.textView2);
    text.setText("Deleting " + word);
    Toast toast = new Toast(getApplicationContext());
    toast.setGravity(Gravity.BOTTOM,Gravity.LEFT,0);
    toast.setDuration(Toast.LENGTH_LONG);
    toast.setView(layout);
    toast.show();
}
    public void showSnackbar(String text){

        Snackbar snackbar  = Snackbar.make(coordinatorLayout,text,Snackbar.LENGTH_INDEFINITE)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //snackbar.dismiss();

                    }
                }).setActionTextColor(Color.BLUE);
        View snackView = snackbar.getView();
        TextView textView = snackView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();
    }
    Snackbar snackbar;

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        //.addFilter("Include all the broadcasts you wish to listen to")
        registerReceiver(myBroadcastService,intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(myBroadcastService);
    }
}