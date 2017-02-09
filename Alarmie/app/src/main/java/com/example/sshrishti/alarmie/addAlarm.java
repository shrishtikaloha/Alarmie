package com.example.sshrishti.alarmie;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.system.ErrnoException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class addAlarm extends AppCompatActivity {
    NotificationManager notificationManager;
    int notifID = 33;
    boolean isNotificActive = false;
    Context context = addAlarm.this;
    TimePicker alarmTimePicker;
    AlarmManager alarmManager;
    private PendingIntent pending_intent;
    private static int id = 0;
    public int temperature;

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);

        Button homeButton = (Button)findViewById(R.id.homebutton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addAlarm.this.startActivity(new Intent(addAlarm.this, MainActivity.class));

            }
        });

        Button setButton = (Button)findViewById(R.id.setbutton);
        setButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addAlarm.this.startActivity(new Intent(addAlarm.this, addAlarm.class));

            }
        });

        Button addButton = (Button)findViewById(R.id.addbutton);
        final Intent myIntent = new Intent(context, AlarmReceiver.class);


        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);




        alarmTimePicker = (TimePicker) findViewById(R.id.timePicker);
        addButton.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.N)
            public void onClick(View v) {

                final Calendar calendar = Calendar.getInstance();
                final int init_hour=alarmTimePicker.getHour();

                calendar.setTimeInMillis(System.currentTimeMillis());

                //calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getHour());
                calendar.set(Calendar.MINUTE, alarmTimePicker.getMinute());
                calendar.set(Calendar.SECOND , 0);

                System.out.println("time : " + calendar.getTimeInMillis());

                final int hour = alarmTimePicker.getHour();
                final int minute = alarmTimePicker.getMinute();

                pending_intent = PendingIntent.getBroadcast(context, 1, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);



             //   long time  = calendar.getTimeInMillis();

                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() , pending_intent);
                Toast.makeText(context, "Alarm is set for : " + hour + " : " + minute, Toast.LENGTH_LONG).show();
                setAlarm();
            }
        });
    }

    public void showNotification(View view) {

        // Builds a notification
        NotificationCompat.Builder notificBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setContentTitle("Message")
                .setContentText("New Message")
                .setTicker("Alert New Message")
                .setSmallIcon(R.drawable.alarmclockicon);

        // Define that we have the intention of opening MoreInfoNotification
        Intent moreInfoIntent = new Intent(this, MoreInfoNotification.class);

        // Used to stack tasks across activites so we go to the proper place when back is clicked
        TaskStackBuilder tStackBuilder = TaskStackBuilder.create(this);

        // Add all parents of this activity to the stack
        tStackBuilder.addParentStack(MoreInfoNotification.class);

        // Add our new Intent to the stack
        tStackBuilder.addNextIntent(moreInfoIntent);

        // Define an Intent and an action to perform with it by another application
        // FLAG_UPDATE_CURRENT : If the intent exists keep it but update it if needed
        PendingIntent pendingIntent = tStackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);

        // Defines the Intent to fire when the notification is clicked
        notificBuilder.setContentIntent(pendingIntent);

        // Gets a NotificationManager which is used to notify the user of the background event
        notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Post the notification
        notificationManager.notify(notifID, notificBuilder.build());

        // Used so that we can't stop a notification that has already been stopped
        isNotificActive = true;
    }

    public void stopNotification(View view) {

        // If the notification is still active close it
        if (isNotificActive) {
            notificationManager.cancel(notifID);
        }

    }


    public void setAlarm(){
        TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);

        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();

        MyDbHelper dbHelper = new MyDbHelper(this);
        SQLiteDatabase db=dbHelper.getWritableDatabase();

        ContentValues newRow=new ContentValues();
        newRow.put(MyDataEntry.HOUR_COLUMN, hour);
        newRow.put(MyDataEntry.MIN_COLUMN, minute);
     //   newRow.put(MyDataEntry._ID, id);

        System.out.println("Writing to database: " + hour +": "+ minute);
        long newRowId = db.insert(MyDataEntry.TABLE_NAME, null, newRow);
        System.out.println("Result: "+ newRowId);
       // System.out.println("Result2: "+ id++);

        Intent intent = new Intent(this, MainActivity.class);

      //  createAlarm(hour , minute);

        startActivity(intent);

    }






}
