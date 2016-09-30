package com.example.landy.ggchtxl2.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.landy.ggchtxl2.Adapter.DoubleMessageAdapter;
import com.example.landy.ggchtxl2.Model.User;
import com.example.landy.ggchtxl2.R;

import java.util.List;

@SuppressWarnings("unchecked")
public class DoubleMessage extends Activity {
    List<User> list;
    ListView name_list;
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_double_message);
        Intent i =getIntent();
        list =(List<User>) i.getSerializableExtra("data");
        name_list = (ListView)this.findViewById(R.id.listView);
        DoubleMessageAdapter doubleMessageAdapter = new DoubleMessageAdapter(DoubleMessage.this,list);
        name_list.setAdapter(doubleMessageAdapter);
        name_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User temp = list.get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("User",temp);
                Intent i = new Intent(DoubleMessage.this, Massage.class);
                i.putExtras(bundle);
                startActivity(i);
            }
        });
        back =(ImageView)this.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }



}
