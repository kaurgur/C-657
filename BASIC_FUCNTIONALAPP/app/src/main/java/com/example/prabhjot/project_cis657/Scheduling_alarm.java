package com.example.prabhjot.project_cis657;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.SystemClock;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;
import org.joda.time.DateTime;



import java.util.Calendar;

/**
 * Created by prabhjot on 6/18/2017.
 */

public class Scheduling_alarm extends BroadcastReceiver {
    private static String TAG = "TIMERSAMPLE";

     public String sendtime;
    private DateTime system_time;
   // public String contactNumber ;
    PendingIntent pi = null;


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG ,String.format("Alarm received Broadcast message - %s", intent.getAction()));





        Log.i(TAG, "-----********************************On RECEIVE ENTERED**************************------------------//////////.");
            String contactNumber = intent.getStringExtra("contactNumber");
            String sendmessage = intent.getStringExtra("message");


            try {

                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(contactNumber, null, sendmessage, null, null);
                Log.d(TAG, "##################Message sent to########### " + contactNumber);
            } catch (Exception ex) {
                ex.printStackTrace();
                Log.d(TAG, "^^^^^^^^^^^$^#%#@%#@@^#@^$#^%&%&%$^&%&%&" + ex);
            }




        //Toast.makeText(context, String.format("Timer Broadcast occured at %s",), Toast.LENGTH_LONG).show();
        //sendsms(contactNumber);

    }
    public void SetAlarm(Context context , String contactNumber, int hour_value, int min_value, String message) {
        //this.contactNumber = contactNumber;
       // Log.i(TAG, "----------/////////////-----------//// this class contact number =  " + this.contactNumber  );

        AlarmManager alarm =(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent set = new Intent("com.example.prabhjot.project_cis657_Scheduling_alarm");
        set.putExtra("contactNumber" , contactNumber);
        set.putExtra("message",message);


        pi = PendingIntent.getBroadcast(context, 0, set, 0);



        alarm.cancel(pi);

 /* Set the alarm to start at chosen time */
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());

        c.set(Calendar.HOUR_OF_DAY, hour_value);
        c.set(Calendar.MINUTE, min_value);


        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),0, pi);

        Log.i(TAG, "Alarm Setting Done.");
        Toast.makeText(context, String.format("Scheduled!!!"), Toast.LENGTH_LONG).show();






    }

    public void CancelAlarm(Context context)
    {
        Intent intent = new Intent("com.example.prabhjot.project_cis657.START_ALARM");
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
        Log.i(TAG, "Alarm Cancelled.");
    }










}



