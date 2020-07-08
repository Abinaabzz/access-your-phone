package com.example.phone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.ksoap2.serialization.SoapObject;

public class CommandPassword extends  AppCompatActivity implements View.OnClickListener {
    EditText e;
    Button b;
    public static String msg = "";
    TelephonyManager telManager;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_command_password);
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
        } catch (Exception e) {

        }
        SoapObject obj2=new SoapObject(soapclass.NAMESPACE,"commadsetcheck");
        obj2.addProperty("phno",SetPattern.phno);

        soapclass sc1=new soapclass();
        String ou2=sc1.Callsoap(obj2,"http://tempuri.org/commadsetcheck");
        if(!ou2.equals("")&&!ou2.equals("error"))
        {
            Intent inte=new Intent(CommandPassword.this,ViewHelp.class);
            startActivity(inte);
            Toast.makeText(getApplicationContext(),"sSuccess",Toast.LENGTH_SHORT).show();
        }


        e = (EditText) findViewById(R.id.editText);
        b = (Button) findViewById(R.id.button);
        b.setOnClickListener(this);



    }
    @Override
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            finishAffinity();
            finish();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu m) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menufile, m);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.item1) {
            Intent intent = new Intent(CommandPassword.this, OldPatternCheck.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.item2) {
            Intent in = new Intent(CommandPassword.this, ChangeCommandPassword.class);
            startActivity(in);

            return true;
        }
        if (id == R.id.item3) {
            Intent intent = new Intent(CommandPassword.this, ViewHelp.class);
            startActivity(intent);

            return true;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        SoapObject obj = new SoapObject(soapclass.NAMESPACE, "commandinsert");
        obj.addProperty("id", DrawPattern.id);
        obj.addProperty("cmndpass", e.getText().toString());
        obj.addProperty("phno", SetPattern.phno);
        soapclass sc = new soapclass();
        String ou = sc.Callsoap(obj, "http://tempuri.org/commandinsert");

        if (!ou.equals("") && !ou.equals("error")) {

            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "unsuccess", Toast.LENGTH_SHORT).show();
        }

    }

    public class AppCompatActivity extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //for message
            if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
                Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
                SmsMessage[] msgs = null;
                String msg_from;

                if (bundle != null) {
                    //---retrieve the SMS message received---
                    try {

                        Object[] pdusObj = (Object[]) bundle.get("pdus");
                        for (int i = 0; i < pdusObj.length; i++) {
                            SmsMessage currentMessage = SmsMessage
                                    .createFromPdu((byte[]) pdusObj[i]);
                            String phoneNumber = currentMessage
                                    .getDisplayOriginatingAddress();
                            String senderNum = phoneNumber;
                            String message = currentMessage.getDisplayMessageBody();
                            //Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                            msg = message;
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
//                            Log.d("Exception caught",e.getMessage());
                    }

                }

                //end message



            }
        }
    }
}
