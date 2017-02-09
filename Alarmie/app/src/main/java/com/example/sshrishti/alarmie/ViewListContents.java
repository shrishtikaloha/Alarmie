package com.example.sshrishti.alarmie;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by sshrishti on 11/30/2016.
 */
public class ViewListContents extends AppCompatActivity {



    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ListView listView=(ListView)findViewById(R.id.listView);


        ArrayList<String> Items= new ArrayList<>();

        MyDbHelper dbHelper=new MyDbHelper(this);
        Cursor data = dbHelper.getListContents();

        if(data.getCount()==0){
            Toast.makeText(this, "There are no contents in this list!", Toast.LENGTH_LONG).show();
        }
        else{

            while(data.moveToNext()){

                // Item.add(data.getString(1), data.getString(2));
                String text = new String();
                text =  data.getString(1) + "  : " + data.getString(2);
                Items.add(text);
                MyCustomAdapter adapter = new MyCustomAdapter(Items, this);
                ListAdapter listAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,Items);


                listView.setAdapter(listAdapter);
                listView.setAdapter(adapter);
            }
        }

    }



}
