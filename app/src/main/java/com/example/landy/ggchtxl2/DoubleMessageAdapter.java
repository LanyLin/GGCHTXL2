package com.example.landy.ggchtxl2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * Created by Landy on 2015/11/3.
 */
public class DoubleMessageAdapter extends BaseAdapter {
    private Context context;
    private List<User> list;
    String temp_name,temp_longnum;
    public DoubleMessageAdapter(Context context,List<User> list)
    {
        super();
        this.context=context;
        this.list=list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User item = list.get(position);
        DoubleViewHolder doubleViewHolder = null;
        if (convertView==null)
        {
            doubleViewHolder = new DoubleViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.name_list,null);
            doubleViewHolder.head = (ImageView)convertView.findViewById(R.id.headlist);
            doubleViewHolder.namelist = (TextView)convertView.findViewById(R.id.namelist);
            doubleViewHolder.longnum = (TextView)convertView.findViewById(R.id.longnumlist);
            doubleViewHolder.call = (ImageView)convertView.findViewById(R.id.callbtn);
           convertView.setTag(doubleViewHolder);

        }else {
            doubleViewHolder = (DoubleViewHolder)convertView.getTag();
        }
        if (item.getPic()!=null)
        {
            Picasso.with(context).load(item.getPic().getFileUrl()).into(doubleViewHolder.head);
        }
        doubleViewHolder.namelist.setText(item.getUsername());
        doubleViewHolder.longnum.setText(item.getMobilePhoneNumber());
        temp_longnum=doubleViewHolder.longnum.getText().toString();
        doubleViewHolder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i  =new Intent();
                i.setAction(Intent.ACTION_CALL);
                i.setData(Uri.parse("tel:" + temp_longnum));
                context.startActivity(i);
            }
        });
        return convertView;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
