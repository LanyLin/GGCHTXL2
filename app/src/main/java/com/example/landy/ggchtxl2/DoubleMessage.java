package com.example.landy.ggchtxl2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import java.util.List;

public class DoubleMessage extends Activity {
    List<User> list;
    ListView name_list;
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
