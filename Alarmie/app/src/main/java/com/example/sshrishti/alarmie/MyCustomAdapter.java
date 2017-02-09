package com.example.sshrishti.alarmie;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by sshrishti on 11/30/2016.
 */
public class MyCustomAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String> list = new ArrayList<String>();
    private Context context;





    public MyCustomAdapter(ArrayList<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
       return pos;
        //just return 0 if your list items do not have an Id variable.
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.custom, null);
        }

        //Handle TextView and display string from your list
        TextView listItemText = (TextView)view.findViewById(R.id.list_item_string);
        listItemText.setText(list.get(position));

        //Handle button, Switch and add onClickListeners

      //  Switch mySwitch = (Switch)view.findViewById(R.id.switch2);
        Button deleteBtn = (Button)view.findViewById(R.id.delete_btn);



     /*   mySwitch.setChecked(true);
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {


                if(isChecked){
                    Toast.makeText(context, "Alarm On : ", Toast.LENGTH_SHORT).show();

                }else{
                    delRow(position);
                    Toast.makeText(context, "Alarm Off : ", Toast.LENGTH_SHORT).show();
                }

            }
        });*/

        final MyDbHelper dbHelper=new MyDbHelper(context);
        Cursor data;
        final String text;
        text =list.get(position);
        final String[] h = text.split(":");

        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                //do something
                Toast.makeText(context, "Deleting : " + h[0] + " : " + h[1] , Toast.LENGTH_SHORT).show();
                dbHelper.deleteRow(Integer.parseInt(h[0]) , Integer.parseInt(h[1]) , context);
                list.remove(position); //or some other task
                notifyDataSetChanged();

            }
        });


        return view;
    }

    public void delRow(int position){
        MyDbHelper dbHelper=new MyDbHelper(context);
        dbHelper.deleteEntry(position , context);
    }


}
