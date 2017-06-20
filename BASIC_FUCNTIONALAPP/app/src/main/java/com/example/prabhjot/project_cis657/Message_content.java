package com.example.prabhjot.project_cis657;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import static com.example.prabhjot.project_cis657.R.id.radio1;

public class Message_content extends AppCompatActivity {
    private static final String TAG = Message_content.class.getSimpleName();
    public String message_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_content);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        EditText textbox = (EditText) findViewById(R.id.textbox);
       RadioGroup r = (RadioGroup) findViewById(R.id.rg);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "&&&&&&&&&&&&&&&& MESSAGE<<<<<<<<<<<<<<<<<<<<< " + message_content);
                Intent intent = new Intent();
                intent.putExtra("message", message_content);
                setResult(MainScreenActivity.message_result,intent);
                finish();

            }
        });
      // int sel_id = r.getCheckedRadioButtonId();
        RadioButton radio_1 = (RadioButton) findViewById(R.id.radio1);
        RadioButton radio_2 = (RadioButton) findViewById(R.id.radio2);
        RadioButton radio_3 = (RadioButton) findViewById(R.id.radio3);
        RadioButton radio_4 = (RadioButton) findViewById(R.id.radio4);

        r.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.radio1:
                        message_content = radio_1.getText().toString();
                        break;
                    case R.id.radio2:
                        message_content = radio_2.getText().toString();
                        break;
                    case R.id.radio3:
                        message_content = radio_3.getText().toString();
                        break; case R.id.radio4:
                        message_content = textbox.getText().toString();
                        break;

                }
            }
        });


    }




}
