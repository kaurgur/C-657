package com.example.prabhjot.project_cis657;


import android.*;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import org.joda.time.DateTime;

import java.security.Permission;
import java.util.List;

public class MainScreenActivity extends AppCompatActivity {
    public final static int PICK_CONTACT = 1;
    public static int Date_and_time_result = 11;
    public static int message_result = 111;
    private static final String TAG = MainScreenActivity.class.getSimpleName();
    private Uri contactData;
    private String contactID;
    private static final int READ_CONTACTS_PERMISSIONS_REQUEST = 1;
    private static final int SEND_SMS_PERMISSIONS_REQUEST = 2;
    public String contactNumber = null;
    public String contactName = null;
    public Scheduling_alarm schedule = new Scheduling_alarm();
    public int hour_value;
    public int min_value;
    private DateTime select_date;
    public String message;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView add_contact = (ImageView) findViewById(R.id.add_contact);
        ImageView add_date = (ImageView) findViewById(R.id.add_dateTime);
        ImageView add_message = (ImageView) findViewById(R.id.add_message);

        Button schedule_button = (Button) findViewById(R.id.schedule_button);

        add_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               Intent intent= new Intent(Intent.ACTION_PICK,  ContactsContract.Contacts.CONTENT_URI);


                    startActivityForResult(intent, PICK_CONTACT);
                }

        });


        add_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainScreenActivity.this, DateAndTime.class);
                startActivityForResult(intent, Date_and_time_result );
            }

        });

        add_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainScreenActivity.this, Message_content.class);
                startActivityForResult(intent, message_result );
            }

        });

        schedule_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                getPermission_tosendmessage();
                schedule.SetAlarm(getApplicationContext() , contactNumber,hour_value,min_value,message);



            }

        });


    }



    public void getPermission_tosendmessage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.SEND_SMS},
                        SEND_SMS_PERMISSIONS_REQUEST);
            }
        }


    }

    public void getPermissionToReadUserContacts() {

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.READ_CONTACTS},
                        READ_CONTACTS_PERMISSIONS_REQUEST);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        // Make sure it's our original READ_CONTACTS request
        if (requestCode == READ_CONTACTS_PERMISSIONS_REQUEST) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Read Contacts permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Read Contacts permission denied", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode ==SEND_SMS_PERMISSIONS_REQUEST)
         {
            if (grantResults.length < 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Send SMS permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Send SMS permission denied", Toast.LENGTH_SHORT).show();
            }
        }


        else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }




    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {
            case (PICK_CONTACT) :
                if (resultCode == MainScreenActivity.RESULT_OK) {

                    getPermissionToReadUserContacts();


                     contactData = data.getData();

                    retrieveContactName();
                    retrieveContactNumber();
                   /* Cursor c =  getContentResolver().query(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        String phone = c.getString(c.getColumnIndex(ContactsContract.Contacts));
                        // TODO Fetch other Contact details as you want to use

                    }
                    */

                }
                break;
        }

        if (resultCode == Date_and_time_result) {
            this.hour_value = Integer.parseInt(data.getStringExtra("hour_value"));
            this.min_value = Integer.parseInt(data.getStringExtra("min_value"));

        }

        if (resultCode == message_result) {
            this.message = data.getStringExtra("message");
            Log.d(TAG, ">>>>>>>>>>>>>>>>>>MESSAGE<<<<<<<<<<<<<<<<<<<<< " + message );
        }

    }




    public void retrieveContactNumber() {



        // getting contacts ID
        Cursor cursorID = getContentResolver().query(contactData, new String[]{ContactsContract.Contacts._ID},
                null, null, null);

        if (cursorID.moveToFirst()) {

            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }

        cursorID.close();

        Log.d(TAG, "Contact ID: " + contactID);

        // Using the contact ID now we will get contact phone number
        Cursor cursorPhone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},

                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,

                new String[]{contactID},
                null);

        if (cursorPhone.moveToFirst()) {
            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }



        cursorPhone.close();

        Log.d(TAG, "Contact Phone Number: " + contactNumber);
    }

    public void retrieveContactName() {
        // querying contact data store
        Cursor cursor = getContentResolver().query(contactData, null, null, null, null);

        if (cursor.moveToFirst()) {

            // DISPLAY_NAME = The display name for the contact.

            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        }

        cursor.close();

        Log.d(TAG, "Contact Name: " + contactName);

    }

}
