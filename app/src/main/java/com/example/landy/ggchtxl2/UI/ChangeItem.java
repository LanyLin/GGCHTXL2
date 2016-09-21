package com.example.landy.ggchtxl2.UI;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.landy.ggchtxl2.R;

public class ChangeItem extends Activity {
    TextView Type,Save;
    ImageView back;
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_change_item);
        Intent i =getIntent();
        final String type = i.getStringExtra("Type");
        final String content = i.getStringExtra("content");
        Type = (TextView) findViewById(R.id.Type);
        Save = (TextView) findViewById(R.id.save);
        back = (ImageView) findViewById(R.id.back);
        editText = (EditText) findViewById(R.id.content);
        Type.setText(type);
        editText.setText(content);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editText.getText().toString().equals(""))
                {
                    Save.setVisibility(View.INVISIBLE);
                }
                else
                {
                    Save.setVisibility(View.VISIBLE);
                }
            }
        });
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = editText.getText().toString();
                Intent intent = new Intent();
                intent.putExtra("Type",type);
                intent.putExtra("content",temp);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }
}
