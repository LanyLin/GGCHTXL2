package com.example.landy.ggchtxl2.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.landy.ggchtxl2.Model.DoubleViewHolder;
import com.example.landy.ggchtxl2.Model.User;
import com.example.landy.ggchtxl2.R;
import com.squareup.picasso.Picasso;
import java.util.List;


public class DoubleMessageAdapter extends BaseAdapter {
    private Context context;
    private List<User> list;
    String temp_longnum;
    public DoubleMessageAdapter(Context context,List<User> list)
    {
        super();
        this.context=context;
        this.list=list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final User item = list.get(position);
        DoubleViewHolder doubleViewHolder;
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
            resetViewHolder(doubleViewHolder);
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
                i.setData(Uri.parse("tel:" + item.getMobilePhoneNumber()));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });
        return convertView;
    }

    private void resetViewHolder(DoubleViewHolder doubleViewHolder) {
        doubleViewHolder.call.setImageBitmap(null);
        doubleViewHolder.head.setImageBitmap(null);
        doubleViewHolder.namelist.setText(null);
        doubleViewHolder.longnum.setText(null);
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
