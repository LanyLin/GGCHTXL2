package com.example.landy.ggchtxl2.UI;

import android.app.Activity;
import android.os.Looper;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.landy.ggchtxl2.R;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SendSuggest extends Activity {
    private final String URL = "http://120.24.212.93/createwebsite/admin.php/member/android_remark";
    private static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
    EditText Suggest;
    ImageView back;
    Button send;
    String tempsuggest;
    Runnable networkTask = new Runnable() {
        @Override
        public void run() {
            try {

                OkHttpClient client = new OkHttpClient();
                String strutf= URLEncoder.encode(tempsuggest,"utf-8");
                Log.e("text",strutf);
                RequestBody requestBody=RequestBody.create(MEDIA_TYPE_MARKDOWN,"remark="+strutf+"&");
                Request request = new Request.Builder().url(URL).post(requestBody)
                        .addHeader("cache-control","no-cache")
                        .addHeader("content-type","application/x-www-form-urlencoded; charset=utf-8")
                        .build();
                Response response = client.newCall(request).execute();
                if (response.isSuccessful())
                {
                    String result = response.body().string();
                    Log.e("text",result);
                    if (result!=null)
                    {
                        result = result.substring(1);
                    }
                    JSONObject obj = new JSONObject(result);
                    String resultcode = obj.getString("ResultCode");
                    Looper.prepare();
                    if (resultcode.equals("0"))
                    {
                        Toast.makeText(getApplicationContext(),"反馈成功",Toast.LENGTH_LONG).show();

                    }else
                    {
                        Toast.makeText(getApplicationContext(),"反馈失败",Toast.LENGTH_LONG).show();
                    }
                    Looper.loop();
                }
                else {
                    throw  new IOException(response+"eeee");
                }




            }catch (Exception e)
            {
                Log.e("error",e.toString());
            }
        }
    };
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
                    new Thread(networkTask).start();
                }
            }
        });
    }
}
