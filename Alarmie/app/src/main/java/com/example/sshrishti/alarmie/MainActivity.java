package com.example.sshrishti.alarmie;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private TimePicker alarmTimePicker;

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button setAlarm = (Button) findViewById(R.id.setbutton);
        setAlarm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, addAlarm.class));

            }
        });

        Button homeButton = (Button) findViewById(R.id.homebutton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, MainActivity.class));

            }
        });

        new getWeather().execute();
        populateList();

    }


    public void populateList() {
        ListView listView = (ListView) findViewById(R.id.listView);


        ArrayList<String> Items = new ArrayList<>();

        MyDbHelper dbHelper = new MyDbHelper(this);
        Cursor data = dbHelper.getListContents();

        if (data.getCount() == 0) {
            Toast.makeText(this, "There are no contents in this list!", Toast.LENGTH_LONG).show();
        } else {
            String text = new String();

            do {

                // Item.add(data.getString(1), data.getString(2));

                text = data.getString(1) + ":" + data.getString(2);
                Items.add(text);
                MyCustomAdapter adapter = new MyCustomAdapter(Items, this);
                ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Items);


                listView.setAdapter(listAdapter);
                listView.setAdapter(adapter);
            } while (data.moveToNext());
        }

    }

    //Loading Database
    public void loadDatabase() {
        MyDbHelper dbHelper = new MyDbHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] query_columns = {MyDataEntry._ID, MyDataEntry.HOUR_COLUMN, MyDataEntry.MIN_COLUMN};
        String selectQuery = MyDataEntry.HOUR_COLUMN + " =?";
        String[] selectionArgs = {" Filter String"};
        System.out.println("Executing select query!");
        String sortOrder = MyDataEntry.HOUR_COLUMN + " DESC";

        //form a select query-get a cursor object...
        Cursor cursor = db.query(
                MyDataEntry.TABLE_NAME,
                query_columns,
                null,//provide a where clause
                null,//arguments for where clause
                null, // griuping
                null, //filter
                sortOrder //sorting order
        );

        boolean hasMoreData = cursor.moveToFirst();
        while (hasMoreData) {
            //get value of each column...
            long recordID = cursor.getLong(cursor.getColumnIndexOrThrow(MyDataEntry._ID));
            String hour = cursor.getString(cursor.getColumnIndexOrThrow(MyDataEntry.HOUR_COLUMN));
            String minute = cursor.getString(cursor.getColumnIndexOrThrow(MyDataEntry.MIN_COLUMN));

            System.out.println("RECORD KEY: " + recordID + "Hour:" + hour + "Minute:" + minute);

            hasMoreData = cursor.moveToNext();
        }

    }

    private void saveAlarm(int hour, int minute) {
        MyDbHelper dbHelper = new MyDbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues newRow = new ContentValues();
        //final int hour = alarmTimePicker.getHour();
        //final int minute = alarmTimePicker.getMinute();
        newRow.put(MyDataEntry.HOUR_COLUMN, hour);
        newRow.put(MyDataEntry.MIN_COLUMN, minute);

        System.out.println("Writing to database: " + hour + ": " + minute);
        long newRowId = db.insert(MyDataEntry.TABLE_NAME, null, newRow);
        System.out.println("Result: " + newRowId);

    }

    public void setTemp(String temp) {
        TextView tw = (TextView) findViewById(R.id.tempText);

        Double tempInCelcius = Double.parseDouble(temp) - 273.15;

        tw.setText("Temperature now is : " + tempInCelcius.intValue());
    }

    private static String url = "http://api.openweathermap.org/data/2.5/weather?q=Toronto,ca&appid=3ec44456254286753e8a540ba84a43b1";
    private static String apiId = "3ec44456254286753e8a540ba84a43b1";


    private class getWeather extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            String jsonString = new HttpHandler().makeCall(url);
            Log.e("inbackground", "Response from url: " + jsonString);

            if (jsonString != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonString);
                    JSONObject weather = jsonObj.getJSONObject("main");
                    String temp = weather.getString("temp");

                    setTemp(temp);

                    System.out.println(temp);

                    System.out.println("Executing everything!");


                } catch (final JSONException e) {
                    Log.e("inbackground", "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e("inbackground", "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
