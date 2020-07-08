package com.example.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class IncomingSms  extends BroadcastReceiver {

	public static String msg="",msg_from="", msg1="", num="",num1="";
	final SmsManager sms = SmsManager.getDefault();

	public void onReceive(Context context, Intent intent) {
		if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
            SmsMessage[] msgs = null;
         //Toast.makeText(context, "gggnnggggg", Toast.LENGTH_LONG).show();
            if (bundle != null){
                //---retrieve the SMS message received---
                try{


                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for(int i=0; i<msgs.length; i++){
                        msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                        msg_from = msgs[i].getOriginatingAddress();
                        String msgBody = msgs[i].getMessageBody();
                        msg=msgBody;
                       // Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                        //sendMessage(msg,msg_from);
                        //capture(context);
                    }
                }catch(Exception e){
                           Log.d("Exception caught",e.getMessage());
                    Toast.makeText(context, "gggggggg", Toast.LENGTH_LONG).show();
                }
            }
        }
	}
}