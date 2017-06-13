package cis.gvsu.edu.geocalculator;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;
import android.location.Location;
import java.text.DecimalFormat;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import android.view.inputmethod.InputMethodManager;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.parceler.Parcels;




import static cis.gvsu.edu.geocalculator.R.id.toolbar;

public class MainActivity extends AppCompatActivity {

    public static int SETTINGS_RESULT = 1;
    public static int HISTORY_RESULT = 2;
    private final static int l = 111;
    public DatabaseReference topRef = null;
    public static List<LocationLookup> allHistory;

    private String bearingUnits = "degrees";
    private String distanceUnits = "kilometers";


    private EditText p1Lat = null;
    private EditText p1Lng = null;
    private EditText p2Lat = null;
    private EditText p2Lng = null;
    private TextView distance = null;
    private TextView bearing = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        allHistory = new ArrayList<LocationLookup>();
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        Button clearButton = (Button)this.findViewById(R.id.clear);
        Button calcButton = (Button)this.findViewById(R.id.calc);
        Button  search =  (Button) this.findViewById(R.id.search);
//        Button settingsButton = (Button) this.findViewById(R.id.settings);

        p1Lat = (EditText) this.findViewById(R.id.p1Lat);
        p1Lng = (EditText) this.findViewById(R.id.p1Lng);
        p2Lat = (EditText) this.findViewById(R.id.p2Lat);
        p2Lng = (EditText) this.findViewById(R.id.p2Lng);
        distance = (TextView) this.findViewById(R.id.distance);
        bearing = (TextView) this.findViewById(R.id.bearing);





        search.setOnClickListener(v -> {
            Intent look = new Intent(MainActivity.this, LookupActivity.class);
            startActivityForResult(look, l);

        });




        clearButton.setOnClickListener(v -> {
            hideKeyboard();
            p1Lat.getText().clear();
            p1Lng.getText().clear();
            p2Lat.getText().clear();
            p2Lng.getText().clear();
            distance.setText("Distance:");
            bearing.setText("Bearing:");
        });



        calcButton.setOnClickListener(v -> {
            updateScreen();
        });
    }

    private void updateScreen()
    {
        try {
            Double lat1 = Double.parseDouble(p1Lat.getText().toString());
            Double lng1 = Double.parseDouble(p1Lng.getText().toString());
            Double lat2 = Double.parseDouble(p2Lat.getText().toString());
            Double lng2 = Double.parseDouble(p2Lng.getText().toString());


            Location p1 = new Location("");//provider name is unecessary
            p1.setLatitude(lat1);//your coords of course
            p1.setLongitude(lng1);

            Location p2 = new Location("");
            p2.setLatitude(lat2);
            p2.setLongitude(lng2);

            double b = p1.bearingTo(p2);
            double d = p1.distanceTo(p2) / 1000.0d;

            if (this.distanceUnits.equals("Miles")) {
                d *= 0.621371;
            }

            if (this.bearingUnits.equals("Mils")) {
                b *= 17.777777778;
            }

            DecimalFormat df = new DecimalFormat("#.##");
            df.setRoundingMode(RoundingMode.CEILING);


            String dStr = "Distance: " + df.format(d) + " " + this.distanceUnits;
            distance.setText(dStr);

            String bStr = "Bearing: " + df.format(b) + " " + this.bearingUnits;
            bearing.setText(bStr);

            // remember the calculation.
            LocationLookup entry = new LocationLookup();
            entry.setOrigLat(lat1);
            entry.setOrigLng(lng1);
            entry.setEndLat(lat2);
            entry.setEndLng(lng2);
            DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
            entry.setTimestamp(fmt.print(DateTime.now()));
           // entry.setTimestamp(DateTime.now());
            topRef.push().setValue(entry);

            hideKeyboard();
        } catch (Exception e) {
            return;
        }

    }

    private void hideKeyboard()
    {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            //this.getSystemService(Context.INPUT_METHOD_SERVICE);
            InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == SETTINGS_RESULT) {
            this.bearingUnits = data.getStringExtra("bearingUnits");
            this.distanceUnits = data.getStringExtra("distanceUnits");
            updateScreen();
        } else if (resultCode == HISTORY_RESULT) {

            if (data != null && data.hasExtra("locs")) {
                Parcelable par = data.getParcelableExtra("locs");
                LocationLookup l = Parcels.unwrap(par);
                this.p1Lat.setText(String.valueOf(l.origLat));
                this.p1Lng.setText(String.valueOf(l.origLng));
                this.p2Lat.setText(String.valueOf(l.endLat));
                this.p2Lng.setText(String.valueOf(l.endLng));
                this.updateScreen();

            }


        } else if (requestCode == l) {
            if (data != null && data.hasExtra("Locations")) {
                Parcelable par = data.getParcelableExtra("Locations");
                LocationLookup l = Parcels.unwrap(par);
                this.p1Lat.setText(String.valueOf(l.origLat));
                this.p1Lng.setText(String.valueOf(l.origLng));
                this.p2Lat.setText(String.valueOf(l.endLat));
                this.p2Lng.setText(String.valueOf(l.endLng));
                this.updateScreen();
                // topRef.push().setValue(l);

            }
        }
        else
            super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, MySettingsActivity.class);
            startActivityForResult(intent, SETTINGS_RESULT );
            return true;
        } else if(item.getItemId() == R.id.action_history) {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivityForResult(intent, HISTORY_RESULT );
            return true;
        }
        return false;
    }
    @Override
    public void onResume(){
        super.onResume();
        allHistory.clear();
        topRef = FirebaseDatabase.getInstance().getReference("history");
        topRef.addChildEventListener (chEvListener);
//topRef.addValueEventListener(valEvListener);
    }

    @Override
    public void onPause(){
        super.onPause();
        topRef.removeEventListener(chEvListener);
    }

    private ChildEventListener chEvListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            LocationLookup entry = (LocationLookup)
                    dataSnapshot.getValue(LocationLookup.class);
            entry._key = dataSnapshot.getKey();
            allHistory.add(entry);
        }
        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        }
        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            LocationLookup entry = (LocationLookup)
                    dataSnapshot.getValue(LocationLookup.class);
            List<LocationLookup> newHistory = new ArrayList<LocationLookup>();
            for (LocationLookup t : allHistory) {
                if (!t._key.equals(dataSnapshot.getKey())) {
                    newHistory.add(t);
                }
            }
            allHistory = newHistory;
        }
        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };



}
