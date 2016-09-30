package com.example.landy.ggchtxl2.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.landy.ggchtxl2.R;

public class SendSuggest extends Activity {
    EditText Suggest;
    ImageView back;
    Button send;
    String tempsuggest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_send_suggest);
        Suggest = (EditText) findViewById(R.id.Suggest);
        back = (ImageView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        send = (Button)findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempsuggest = Suggest.getText().toString();
                if (tempsuggest.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"输入建议不能为空",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Intent i  = new Intent();
                    i.putExtra("suggest",Suggest.getText().toString());
                    setResult(RESULT_OK,i);
                    finish();
                }
            }
        });
    }
}
