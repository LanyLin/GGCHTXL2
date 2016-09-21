package com.example.landy.ggchtxl2.Model;

import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;


public class ImportViewHolder {
    public ImageView head;
    public CheckBox checkBox;
    public TextView namelist;
    public TextView longnum;
    public ImportViewHolder(ImageView head,CheckBox checkBox,TextView namelist,TextView longnum)
    {
        this.checkBox =checkBox;
        this.namelist=namelist;
        this.head = head;
        this.longnum =longnum;
    }
}
