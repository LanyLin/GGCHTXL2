package com.example.landy.ggchtxl2;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImportAll extends ActionBarActivity {
    private static final String G_CB = "G_CB";
    private static final String C_CB = "C_CB";
    private List<Map<String, Boolean>> groupCheckBox;
    private List<List<Map<String, Boolean>>> childCheckBox;
    ArrayList<String> AcademyList;
    ArrayList<ArrayList<User>> ChildList;
    ArrayList<User> AllUsers;
    ExpandableListView UserList;
    ProgressDialog ImprotProgress;
    private ArrayList<User> ImportMessage;
    int hasfinish = 0;
    int Mustfinish;
    int progressStatus;
    private static final String[] PHONES_PROJECT=new String[]{
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_all);
        setView();
        Intent intent =getIntent();
        Bundle bundle = intent.getExtras();
        AcademyList = bundle.getStringArrayList("list");
        AllUsers = (ArrayList<User>) bundle.getSerializable("AllUser");
        UserList = (ExpandableListView) findViewById(R.id.ImportList);
        ChildList = Handle.returnData(AllUsers,AcademyList);
        for (int i =0;i<ChildList.size();i++)
        {
            Map<String,Boolean> curGB = new HashMap<>();
            curGB.put(G_CB,false);
            groupCheckBox.add(curGB);
            List<Map<String,Boolean>> childCB = new ArrayList<>();
            for (int j=0;j<ChildList.get(i).size();j++)
            {
                Map<String, Boolean> curCB = new HashMap<>();
                curCB.put(C_CB, false);
                childCB.add(curCB);
            }
            childCheckBox.add(childCB);
        }
        final MyAdapter adpter = new MyAdapter(ImportAll.this);
        UserList.setAdapter(adpter);
        UserList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                CheckBox checkbox = (CheckBox) v.findViewById(R.id.checkbox);
                checkbox.toggle();
                if (childCheckBox.get(groupPosition).get(childPosition).get(C_CB)) {
                    childCheckBox.get(groupPosition).get(childPosition).put(C_CB, false);
                    if (groupCheckBox.get(groupPosition).get(G_CB)) {
                        groupCheckBox.get(groupPosition).put(G_CB, false);
                    }
                } else {
                    int count = 0;
                    boolean check = ifexist(ChildList.get(groupPosition).get(childPosition).getUsername().toString());
                    if (check)
                    {
                        Toast.makeText(getApplicationContext(),"该联系人已存在通讯录中",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        childCheckBox.get(groupPosition).get(childPosition).put(C_CB, true);
                        for (int i = 0; i < ChildList.get(groupPosition).size(); i++) {
                            if (childCheckBox.get(groupPosition).get(i).get(C_CB))
                                count++;
                        }
                        if (childCheckBox.get(groupPosition).size() == count)
                            groupCheckBox.get(groupPosition).put(G_CB, true);
                    }

                }

                adpter.notifyDataSetChanged();
                return false;
            }
        });


    }
    Handler handle = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==0x11)
            {
                ImprotProgress.setProgress(progressStatus);
            }
        }
    };
    public void showProgress(View v)
    {
        Mustfinish=0;
        hasfinish =0;
        ImportMessage = new ArrayList<>();
        for (int i =0;i<ChildList.size();i++)
        {
            for(int j=0;j<ChildList.get(i).size();j++)
            {
                if(childCheckBox.get(i).get(j).get(C_CB))
                    ImportMessage.add(ChildList.get(i).get(j));
            }
        }
        Mustfinish = ImportMessage.size();
        ImprotProgress = new ProgressDialog(ImportAll.this);
        ImprotProgress.setMax(Mustfinish);

        ImprotProgress.setTitle("正在导入");
        ImprotProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        ImprotProgress.setCancelable(true);
        ImprotProgress.setIndeterminate(false);
        ImprotProgress.setCanceledOnTouchOutside(false);
        ImprotProgress.show();
        //ImprotProgress.create();
        new Thread(){
            @Override
            public void run() {
                while(hasfinish<Mustfinish)
                {
                    progressStatus =doWork();
                    handle.sendEmptyMessage(0x111);
                }
                if(hasfinish>=Mustfinish){

                    ImprotProgress.dismiss();
                    Looper.prepare();
                    Toast.makeText(ImportAll.this,"导入完成",Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }
        }.start();
    }
    public int doWork(){
        User itme = ImportMessage.get(hasfinish);
        ContentResolver resolver = getContentResolver();
        Uri url = Uri.parse("content://com.android.contacts/raw_contacts");
        Uri dataUri = Uri.parse("content://com.android.contacts/data");
        ContentValues values = new ContentValues();
        Cursor cursor =resolver.query(url,new String[]{"_id"},null,null,null );
        cursor.moveToLast();
        int last = cursor.getInt(0);
        int next = last+1;
        values.put("contact_id", next);
        resolver.insert(url, values);

        ContentValues longnum = new ContentValues();
        longnum.put("data1", itme.getMobilePhoneNumber());
        longnum.put("mimetype", "vnd.android.cursor.item/phone_v2");
        longnum.put("raw_contact_id", next);
        resolver.insert(dataUri, longnum);

        ContentValues shoutnum = new ContentValues();
        shoutnum.put("data1",itme.getShoutnum());
        shoutnum.put("mimetype","vnd.android.cursor.item/phone_v2");

        shoutnum.put("raw_contact_id", next);
        resolver.insert(dataUri, shoutnum);

        ContentValues name = new ContentValues();
        name.put("data1",itme.getUsername());
        name.put("mimetype","vnd.android.cursor.item/name");
        name.put("raw_contact_id", next);
        resolver.insert(dataUri, name);

        ContentValues doritomy = new ContentValues();
        doritomy.put("data1",itme.getDormitory());
        doritomy.put("mimetype","vnd.android.cursor.item/postal-address_v2");
        doritomy.put("raw_contact_id",next);
        resolver.insert(dataUri,doritomy);

        return hasfinish++;
    }
    private class MyAdapter extends BaseExpandableListAdapter{
        private Context context;
        ImportViewHolder holder;
        private MyAdapter(Context context){
            this.context =context;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return ChildList.get(groupPosition).size();
        }

        @Override
        public int getGroupCount() {
            return AcademyList.size();
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return ChildList.get(groupPosition).get(childPosition);
        }

        @Override
        public Object getGroup(int groupPosition) {
            return AcademyList.get(groupPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }
        private void changeChildStatesFalse(int groupPosition, Boolean isCheck) {
            for (int i =0;i<childCheckBox.get(groupPosition).size();i++)
            {

                childCheckBox.get(groupPosition).get(i).put(C_CB,false);
            }
        }
        private void changeChildStatesTrue(int groupPosition, Boolean isCheck) {
            for(int i =0;i<childCheckBox.get(groupPosition).size();i++)
            {
                boolean check = ifexist(ChildList.get(groupPosition).get(i).getUsername().toString());
                if (!check)
                    childCheckBox.get(groupPosition).get(i).put(C_CB,true);
            }
        }
        @Override
        public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            View v= convertView;
            if (convertView==null)
            {
                LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v= inflater.inflate(R.layout.grouplayout,null);
            }
            TextView groupText = (TextView)v.findViewById(R.id.acadamyname_list);
            final CheckBox checkbox = (CheckBox)v.findViewById(R.id.grdoucheckBox);
            final String academy = AcademyList.get(groupPosition);
            groupText.setText(academy);
            checkbox.setChecked(groupCheckBox.get(groupPosition).get(G_CB));
            checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkbox.isChecked()&&groupCheckBox.get(groupPosition).get(G_CB)==false)
                    {
                        Boolean isCheck =checkbox.isChecked();
                        groupCheckBox.get(groupPosition).put(G_CB,isCheck);
                        changeChildStatesTrue(groupPosition,isCheck);
                    }
                    else if (!checkbox.isChecked()&& groupCheckBox.get(groupPosition).get(G_CB))
                    {
                        Boolean isCheck = checkbox.isChecked();
                        groupCheckBox.get(groupPosition).put(G_CB, isCheck);
                        changeChildStatesFalse(groupPosition, isCheck);
                    }
                    notifyDataSetChanged();
                }
            });
            return v;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            if (convertView==null)
            {
                LayoutInflater inflate = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView =inflate.inflate(R.layout.importlist,null);
                holder = new ImportViewHolder();
                holder.namelist = (TextView)convertView.findViewById(R.id.namelist);
                holder.checkBox = (CheckBox)convertView.findViewById(R.id.checkBoxlist);
                holder.head = (ImageView)convertView.findViewById(R.id.headlist);
                holder.longnum = (TextView)convertView.findViewById(R.id.longnumlist);
                convertView.setTag(holder);
            }
            else
            {
                holder = (ImportViewHolder)convertView.getTag();
            }
            if (ChildList.get(groupPosition).get(childPosition).getPic()!=null)
            {
                Picasso.with(context).load(ChildList.get(groupPosition).get(childPosition).getPic().getFileUrl()).into(holder.head);
            }
            holder.namelist.setText(ChildList.get(groupPosition).get(childPosition).getUsername());
            holder.longnum.setText(ChildList.get(groupPosition).get(childPosition).getMobilePhoneNumber());
            holder.checkBox.setChecked(childCheckBox.get(groupPosition).get(childPosition).get(C_CB));
            return convertView;

        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
    public boolean ifexist(String name)
    {
        boolean check = false;
        ContentResolver resolver = ImportAll.this.getContentResolver();
        Cursor cursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,PHONES_PROJECT,null,null,null);
        if (cursor!=null)
        {
            while (cursor.moveToNext())
            {
                String temp_name = cursor.getString(0);
                if (temp_name.equals(name))
                {
                    check=true;
                    break;
                }

            }
        }

        return check;
    }
    private void setView() {
        groupCheckBox = new ArrayList<Map<String,Boolean>>();
        childCheckBox = new ArrayList<List<Map<String,Boolean>>>();
    }
}
