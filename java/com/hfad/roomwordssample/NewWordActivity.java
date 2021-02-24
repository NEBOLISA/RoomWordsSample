package com.hfad.roomwordssample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;

import java.util.zip.Inflater;

import static com.hfad.roomwordssample.MainActivity.EXTRA_DATA_ID;
import static com.hfad.roomwordssample.MainActivity.EXTRA_DATA_UPDATE_WORD;

public class NewWordActivity extends AppCompatActivity {
    public static final String EXTRA_REPLY =
            "com.hfad.roomwordssample.REPLY";
    public static final String EXTRA_REPLY_ID =
            "com.hfad.roomwordssample.REPLY_ID";
private CoordinatorLayout coordinatorLayout;
private Button button;

    private EditText mEditWordView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_word);
        mEditWordView = findViewById(R.id.edit_word);

        //coordinatorLayout = (CoordinatorLayout)findViewById(R.id.snack_container);

int id =-1;
final  Bundle extras = getIntent().getExtras();
if(extras != null){
    String word = extras.getString(EXTRA_DATA_UPDATE_WORD,"");
    if(!word.isEmpty()){
        mEditWordView.setText(word);
        mEditWordView.setSelection(word.length());
        mEditWordView.requestFocus();
    }
}
        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(mEditWordView.getText())) {
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    String word = mEditWordView.getText().toString();
                    replyIntent.putExtra(EXTRA_REPLY, word);
                    if(extras != null && extras.containsKey(EXTRA_DATA_ID)){
                        int id = extras.getInt(EXTRA_DATA_ID, -1);
                        if(id !=-1){
                            replyIntent.putExtra(EXTRA_REPLY_ID,id);
                        }
                    }
                    setResult(RESULT_OK, replyIntent);
                }

                finish();
            }
        });
    }

}