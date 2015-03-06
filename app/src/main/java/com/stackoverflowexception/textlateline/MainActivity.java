package com.stackoverflowexception.textlateline;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;


public class MainActivity extends ActionBarActivity {

    String lateLineNumber, storeNumber, tripID , ShortOrderNumber;
    String message, reasonText, arrivalTime, arrivalHour, arrivalMinute;
    Integer customerID;
    EditText editShortOrderNumber, editTripID;
    Spinner reasonSpinner;
    TimePicker arrivalTimePicker;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Text Late Line");
        editShortOrderNumber = (EditText) findViewById(R.id.orderText);
        editShortOrderNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("PREF_SHORTORDERNUMBER",s.toString());
                editor.commit();
            }
        });
        reasonSpinner = (Spinner) findViewById(R.id.reasonSpinner);
        editTripID = (EditText) findViewById(R.id.tripIDText);
        arrivalTimePicker = (TimePicker) findViewById(R.id.timePicker);
        arrivalTimePicker.setIs24HourView(true);
        arrivalTimePicker.setCurrentHour((Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + 1));
        arrivalTimePicker.setCurrentMinute(5);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        lateLineNumber = prefs.getString("PREF_NUMBER", "");
        storeNumber = prefs.getString("PREF_STORENUMBER", "");
        tripID = prefs.getString("PREF_TRIPID", "");
        ShortOrderNumber = prefs.getString("PREF_SHORTORDERNUMBER", "");
        editShortOrderNumber.setText(ShortOrderNumber);
        editTripID.setText(tripID);
        if(lateLineNumber.equals("") || storeNumber.equals("")) {
            Intent intent = new Intent(this, ShowSettings.class);
            this.startActivity(intent);
            Toast.makeText(this, getString(R.string.settings_prompt), Toast.LENGTH_LONG).show();
        }
    }

    public void prevClicked(View view) {
        if(!(editShortOrderNumber.getText().toString().equals(""))) {
            Integer customerID = Integer.parseInt(editShortOrderNumber.getText().toString());
            editShortOrderNumber.setText(Integer.toString(--customerID));
        }
    }

    public void nextClicked(View view) {
        if(!(editShortOrderNumber.getText().toString().equals(""))) {
            Integer customerID = Integer.parseInt(editShortOrderNumber.getText().toString());
            editShortOrderNumber.setText(Integer.toString(++customerID));
        }
    }

    public void sendSMS(View view){

        tripID = editTripID.getText().toString();
        lateLineNumber = prefs.getString("PREF_NUMBER", "");
        storeNumber = prefs.getString("PREF_STORENUMBER", "");
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("PREF_TRIPID",tripID);
        editor.commit();
        ShortOrderNumber = editShortOrderNumber.getText().toString();
        reasonText = (String) reasonSpinner.getSelectedItem();
        if(reasonText.equals("(No Reason)")) reasonText = "";
        arrivalHour   = Integer.toString(arrivalTimePicker.getCurrentHour());
        arrivalMinute = Integer.toString(arrivalTimePicker.getCurrentMinute());
        arrivalTime = String.format("%s:%s",arrivalHour,arrivalMinute);
        message = String.format("%s %s %s %s %s",storeNumber,tripID,ShortOrderNumber,arrivalTime,reasonText);

        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage(String.format("Send text message:\n%s", message));
        builder1.setCancelable(true);
        builder1.setPositiveButton("Send",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SmsManager sms = SmsManager.getDefault();
                        sms.sendTextMessage(lateLineNumber, null, message, null, null);
                        dialog.cancel();
                    }
                });
        builder1.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
            switch(item.getItemId()) {
                case R.id.action_settings:
                    Intent intent = new Intent(this, ShowSettings.class);
                    this.startActivity(intent);
                    break;
                default:
                    return super.onOptionsItemSelected(item);
            }

        return super.onOptionsItemSelected(item);
    }
}
