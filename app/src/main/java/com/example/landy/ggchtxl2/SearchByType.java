package com.example.landy.ggchtxl2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import java.util.ArrayList;
import java.util.List;

public class SearchByType extends Activity {
    Spinner ChoicType;
    ListView listView;
    List<User> list;
    ImageView Back;
    DoubleMessageAdapter doubleMessageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_search_by_type);
        Intent intent =getIntent();
        Bundle bundle = intent.getExtras();
        final ArrayList<String> type = bundle.getStringArrayList("list");
        final ArrayList<User> AllUser = (ArrayList<User>) bundle.getSerializable("AllUser");
        ChoicType = (Spinner)findViewById(R.id.Spiner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.spinner_item);
        if (type!=null) {
            for (int i = 0; i < type.size(); i++) {
                adapter.add(type.get(i));
            }
            ChoicType.setAdapter(adapter);
            list = Handle.inserDate(AllUser, type.get(0));
        }
        listView = (ListView)findViewById(R.id.listView);
        doubleMessageAdapter = new DoubleMessageAdapter(getApplicationContext(),list);
        listView.setAdapter(doubleMessageAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User item = list.get(position);
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable("User",item);
                Intent i = new Intent(SearchByType.this, Massage.class);
                i.putExtras(bundle1);
                startActivity(i);
            }
        });
        Back = (ImageView) findViewById(R.id.back);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ChoicType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                list.removeAll(list);
                list.addAll(Handle.inserDate(AllUser,type.get(position)));
                Log.e("size",list.size()+"");
                doubleMessageAdapter.notifyDataSetChanged();
                listView.setAdapter(doubleMessageAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
