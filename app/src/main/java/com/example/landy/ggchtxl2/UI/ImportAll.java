package com.example.landy.ggchtxl2.UI;

import android.app.Activity;
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
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.landy.ggchtxl2.Dao.DatabaseHelper;
import com.example.landy.ggchtxl2.Dao.Handle;
import com.example.landy.ggchtxl2.Model.ImportViewHolder;
import com.example.landy.ggchtxl2.Model.User;
import com.example.landy.ggchtxl2.Model.UserExist;
import com.example.landy.ggchtxl2.PicTransFormation.CircleImageTransformation;
import com.example.landy.ggchtxl2.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImportAll extends Activity {
    private static final String G_CB = "G_CB";
    private static final String C_CB = "C_CB";
    private List<Map<String, Boolean>> groupCheckBox;
    private List<List<Map<String, Boolean>>> childCheckBox;
    ArrayList<String> AcademyList;
    ArrayList<ArrayList<User>> ChildList;
    ArrayList<User> AllUsers;
    ExpandableListView UserList;
    ProgressDialog ImprotProgress;
    ImageView back;
    private ArrayList<User> ImportMessage;
    int hasfinish = 0;
    int Mustfinish;
    int progressStatus;
    DatabaseHelper databaseHelper;
    UserExist userExist;
    View v;
    private static final String[] PHONES_PROJECT=new String[]{
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};
    Handler handle = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==0x11)
            {
                ImprotProgress.setProgress(progressStatus);
            }
            else if (msg.what==0x12)
            {
                ImportViewHolder holder = (ImportViewHolder) msg.obj;
                Bundle bundle = msg.getData();
                String name = bundle.getString("name");
                userExist = databaseHelper.getUserExist(name);
                if (userExist!=null)
                {
                    if (userExist.getExist()==1)
                    {
                        holder.checkBox.setVisibility(View.INVISIBLE);
                    }
                }
            }

        }
    };
    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_import_all);
        setView();
        Intent intent =getIntent();
        Bundle bundle = intent.getExtras();
        databaseHelper = new DatabaseHelper(getApplicationContext());
        AcademyList = bundle.getStringArrayList("list");
        AllUsers = (ArrayList<User>) bundle.getSerializable("AllUser");
        ChildList = Handle.returnData(AllUsers,AcademyList);
        UserList = (ExpandableListView) findViewById(R.id.ImportList);
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
        back = (ImageView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    public void showProgress(View v)
    {
        Mustfinish=0;
        hasfinish =0;
        ImportMessage = new ArrayList<>();
        for (int i =0;i<ChildList.size();i++)
        {
            for(int j=0;j<ChildList.get(i).size();j++)
            {
                if(childCheckBox.get(i).get(j).get(C_CB)&&!ifexist(ChildList.get(i).get(j).getUsername()))
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
                        handle.sendEmptyMessage(0x11);
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
        if (cursor!=null) {
            cursor.moveToLast();
            int last = cursor.getInt(0);
            int next = last + 1;
            values.put("contact_id", next);
            resolver.insert(url, values);

            ContentValues longnum = new ContentValues();
            longnum.put("data1", itme.getMobilePhoneNumber());
            longnum.put("mimetype", "vnd.android.cursor.item/phone_v2");
            longnum.put("raw_contact_id", next);
            resolver.insert(dataUri, longnum);

            ContentValues shoutnum = new ContentValues();
            shoutnum.put("data1", itme.getShoutnum());
            shoutnum.put("mimetype", "vnd.android.cursor.item/phone_v2");

            shoutnum.put("raw_contact_id", next);
            resolver.insert(dataUri, shoutnum);

            ContentValues name = new ContentValues();
            name.put("data1", itme.getUsername());
            name.put("mimetype", "vnd.android.cursor.item/name");
            name.put("raw_contact_id", next);
            resolver.insert(dataUri, name);

            ContentValues doritomy = new ContentValues();
            doritomy.put("data1", itme.getDormitory());
            doritomy.put("mimetype", "vnd.android.cursor.item/postal-address_v2");
            doritomy.put("raw_contact_id", next);
            resolver.insert(dataUri, doritomy);

            cursor.close();
        }

        return hasfinish++;
    }

    @SuppressWarnings("WrongConstant")
    private class MyAdapter extends BaseExpandableListAdapter{
        ImportViewHolder holder;
        private Context context;
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
        private void changeChildStatesFalse(int groupPosition) {
            for (int i =0;i<childCheckBox.get(groupPosition).size();i++)
            {

                childCheckBox.get(groupPosition).get(i).put(C_CB,false);
            }
        }
        private void changeChildStatesTrue(int groupPosition) {
            for(int i =0;i<childCheckBox.get(groupPosition).size();i++)
            {

                childCheckBox.get(groupPosition).get(i).put(C_CB,true);
            }
        }
        @Override
        public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            v= convertView;
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

                            if (checkbox.isChecked()&&!groupCheckBox.get(groupPosition).get(G_CB))
                            {
                                Boolean isCheck =checkbox.isChecked();
                                groupCheckBox.get(groupPosition).put(G_CB,isCheck);
                                changeChildStatesTrue(groupPosition);
                            }
                            else if (!checkbox.isChecked()&& groupCheckBox.get(groupPosition).get(G_CB))
                            {
                                Boolean isCheck = checkbox.isChecked();
                                groupCheckBox.get(groupPosition).put(G_CB, isCheck);
                                changeChildStatesFalse(groupPosition);
                            }
                    notifyDataSetChanged();
                        }




            });
            return v;
        }

        @Override
        public View getChildView(final int groupPosition,final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

            Long startTime = System.currentTimeMillis();
            if (convertView==null)
            {
                LayoutInflater inflate = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView =inflate.inflate(R.layout.importlist,null);
                TextView namelist = (TextView)convertView.findViewById(R.id.namelist);
                CheckBox checkBox = (CheckBox)convertView.findViewById(R.id.checkBoxlist);
                ImageView head = (ImageView)convertView.findViewById(R.id.headlist);
                TextView longnum = (TextView)convertView.findViewById(R.id.longnumlist);
                holder  =new ImportViewHolder(head,checkBox,namelist,longnum);
                convertView.setTag(holder);
            }
            else
            {
                holder = (ImportViewHolder)convertView.getTag();
                resetViewHolder(holder);
            }
            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("name",ChildList.get(groupPosition).get(childPosition).getUsername());
            message.setData(bundle);
            message.obj=holder;
            message.what=0x12;
            handle.sendMessage(message);
            if (ChildList.get(groupPosition).get(childPosition).getPic()!=null)
            {

                Picasso.with(context).load(ChildList.get(groupPosition).get(childPosition).getPic().getFileUrl()).transform(new CircleImageTransformation()).into(holder.head);
            }
            holder.namelist.setText(ChildList.get(groupPosition).get(childPosition).getUsername());
            holder.longnum.setText(ChildList.get(groupPosition).get(childPosition).getMobilePhoneNumber());
            holder.checkBox.setChecked(childCheckBox.get(groupPosition).get(childPosition).get(C_CB));
            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                            if (childCheckBox.get(groupPosition).get(childPosition).get(C_CB)) {
                                childCheckBox.get(groupPosition).get(childPosition).put(C_CB, false);
                                if (groupCheckBox.get(groupPosition).get(G_CB)) {
                                    groupCheckBox.get(groupPosition).put(G_CB, false);
                                }
                            } else {
                                int count = 0;
                                    childCheckBox.get(groupPosition).get(childPosition).put(C_CB, true);
                                    for (int i = 0; i < ChildList.get(groupPosition).size(); i++) {
                                        if (childCheckBox.get(groupPosition).get(i).get(C_CB))
                                            count++;
                                    }
                                    if (childCheckBox.get(groupPosition).size() == count)
                                        groupCheckBox.get(groupPosition).put(G_CB, true);


                            }
                    notifyDataSetChanged();
                        }




            });
            long endtime = System.currentTimeMillis();
            Log.e("time",endtime-startTime+"eeee");
            return convertView;

        }
        private void resetViewHolder(ImportViewHolder holder) {
            holder.checkBox.setChecked(false);
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.head.setImageBitmap(null);
            holder.longnum.setText(null);
            holder.namelist.setText(null);
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
        if (cursor!=null)
            cursor.close();
        return check;
    }
    private void setView() {
        groupCheckBox = new ArrayList<>();
        childCheckBox = new ArrayList<>();
    }
}
