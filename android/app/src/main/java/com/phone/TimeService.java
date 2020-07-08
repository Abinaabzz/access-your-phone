package com.example.phone;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.StrictMode;
import android.provider.Contacts;
import android.provider.ContactsContract;
//import android.support.v4.app.NotificationCompat;
import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressLint("NewApi")
public class TimeService extends Service {


	static final int RESULT_ENABLE = 1;
	static final int PICK_CONTACT = 1;


	DevicePolicyManager deviceManger;
	ActivityManager activityManager;
	ComponentName compName;

	String fname,cpas;
	static String str = "";
	private static final int SERVERPORT = 7003;
	private static final String SERVER_IP = "192.168.43.85";
	DatabaseHelper dh=new DatabaseHelper(this);

	// constant
	public static final long NOTIFY_INTERVAL = 60 * 1000; // 10 seconds
	SQLiteDatabase db;
	String uid;
	public static String rid = "", hid = "";
	private NotificationManager alarmNotificationManager;
	// run on another Thread to avoid crash
	private Handler mHandler = new Handler();
	// timer handling
	private Timer mTimer = null;

	//public final static String URL = "http://192.168.1.12:3000/Ambulance/WebService.asmx";
	//public final static String URL = "http://"+ MainActivity.ipset +"/Ambulance/WebService.asmx";
	public static String URL = "http://192.168.1.30/Ambulance/WebService.asmx";
	private final String NAMESPACE = "http://tempuri.org/";
	private final String SOAP_ACTION = "http://tempuri.org/Setvalues";
	private final String METHOD_NAME = "Setvalues";

	// AudioManager audioManager;

	int cflag = 0;
	double dlat, dlon;
public static int bflag=0,sflag=0,lfag=0;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("NewApi")
	@Override
	public void onCreate() {


		deviceManger = (DevicePolicyManager) getSystemService(
				Context.DEVICE_POLICY_SERVICE);
		activityManager = (ActivityManager) getSystemService(
				Context.ACTIVITY_SERVICE);
		compName = new ComponentName(this, AdminReceiver.class);




		// cancel if already existed
		if (mTimer != null) {
			mTimer.cancel();
		} else {
			// recreate new
			mTimer = new Timer();
		}
		// schedule task
		mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0,
				NOTIFY_INTERVAL);
		try {
			if (Build.VERSION.SDK_INT > 9) {
				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
						.permitAll().build();
				StrictMode.setThreadPolicy(policy);
			}
		} catch (Exception e) {

		}





	}

	class TimeDisplayTimerTask extends TimerTask {

		@Override
		public void run() {
			// run on another thread
			mHandler.post(new Runnable() {
				@Override
				public void run() {

//					AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
					try {
						//Toast.makeText(getApplicationContext(),"wwwww",Toast.LENGTH_SHORT).show();
						changeVol(IncomingSms.msg, IncomingSms.msg_from);
//						Toast.makeText(getApplicationContext(),IncomingSms.msg_from , Toast.LENGTH_LONG).show();
//					Toast.makeText(getApplicationContext(),IncomingSms.msg , Toast.LENGTH_LONG).show();
//					if(IncomingSms.msg.contains("AT+CG"))
//					{
//						//Toast.makeText(getApplicationContext(), "dfdfdfdf", 5).show();


						SoapObject obj2=new SoapObject(soapclass.NAMESPACE,"take_cmndpass");
						obj2.addProperty("phno",DrawPattern.ph);

						soapclass sc1=new soapclass();
						String ou2=sc1.Callsoap(obj2,"http://tempuri.org/take_cmndpass");
						cpas=ou2;
						Toast.makeText(getApplicationContext(),cpas,Toast.LENGTH_LONG).show();





						Gpstracker gps = new Gpstracker(getApplicationContext());

						dlat = gps.getLatitude();
						dlon = gps.getLongitude();

						//################################################

						//soap object creation using namespace and method name
//						SoapObject soapobj =new SoapObject(NAMESPACE,METHOD_NAME);
//						//add parameter to soap object
//						soapobj.addProperty("lat",Double.toString(dlat));
//						soapobj.addProperty("lon",Double.toString(dlon));
//
//
//						//Toast.makeText(getApplicationContext(), URL, 5).show();
//						//create xml envelop
//						SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
//						//add soap object to xml doc
//					    envelope.setOutputSoapObject(soapobj);
//			            //dotnet envelop
//					    envelope.dotNet = true;
//					    //for http call
//					    HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
//			            try {
//			            	//http call
//							androidHttpTransport.call(SOAP_ACTION, envelope);
//							 // Get the SoapResult from the envelope body.
//				            SoapObject result = (SoapObject)envelope.bodyIn;
//				            String ou  =result.getProperty(0).toString();
//				            //Toast.makeText(getApplicationContext(), ou, 5).show();
//
//						} catch (IOException e) {
//							Toast.makeText(getApplicationContext(),e.toString(), Toast.LENGTH_SHORT).show();
//						}
//			           
//			            catch (XmlPullParserException e) {
//							// TODO Auto-generated catch block
//			            	//Toast.makeText(getApplicationContext(), "hhh1", 5).show();
//							e.printStackTrace();
//						}

						//####################################################

						//Toast.makeText(getApplicationContext(), Double.toString(dlon), 5).show();
//						String [] temp=IncomingSms.msg.split(":");
//						String t=temp[1].substring(0, 3);
//						String m=temp[2].substring(0, 3);
//						///insert service here
//						
//						Toast.makeText(getApplicationContext(), t, 5).show();
//						Toast.makeText(getApplicationContext(), m, 5).show();
						//	}
						//	getcontact(IncomingSms.msg,IncomingSms.msg_from);

						//Toast.makeText(getApplicationContext(), IncomingSms.msg, Toast.LENGTH_SHORT).show();
						//   Toast.makeText(getApplicationContext(), IncomingSms.msg_from, Toast.LENGTH_SHORT).show();
						//  Toast.makeText(getApplicationContext(), android.R.attr.mode+"",Toast.LENGTH_LONG).show();
//						IncomingSms.msg="";
//						IncomingSms.msg_from="";
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			});
		}
	}


	public void lockme() {


		System.out.println("inside button");
		PowerManager pManager = (PowerManager)
				getSystemService(Context.POWER_SERVICE);
		@SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl = pManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "lock screen");
		wl.acquire();
		wl.release();


	}


	public void getcontact(String msg, String adr) {

		if (msg.contains("Cont")) {
			Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
			Toast.makeText(getApplicationContext(), adr, Toast.LENGTH_LONG).show();
			String[] ms = msg.split("2");
			if (ms.length > 1) {

				ContentResolver cr = getContentResolver();
				Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
						null, null, null, null);

				if (cur.getCount() > 0) {
					while (cur.moveToNext()) {
						String id = cur.getString(
								cur.getColumnIndex(ContactsContract.Contacts._ID));
						String name = cur.getString(cur.getColumnIndex(
								ContactsContract.Contacts.DISPLAY_NAME));
						Toast.makeText(getApplicationContext(), name, Toast.LENGTH_LONG).show();

						if (cur.getInt(cur.getColumnIndex(
								ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
							Cursor pCur = cr.query(
									ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
									null,
									ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
									new String[]{id}, null);
							while (pCur.moveToNext()) {
								String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
								//Toast.makeText(getApplicationContext(), "Name: " + name  + ", Phone No: " + phoneNo, Toast.LENGTH_SHORT).show();

								Toast.makeText(getApplicationContext(), phoneNo, Toast.LENGTH_SHORT).show();

								//Toast.makeText(getApplicationContext(), ms[1], Toast.LENGTH_SHORT).show();
								if (name.contains(ms[1])) {
									//Toast.makeText(getApplicationContext(), adr, Toast.LENGTH_SHORT).show();
									Toast.makeText(getApplicationContext(), phoneNo, Toast.LENGTH_SHORT).show();
//			                	SmsManager sendsms=SmsManager.getDefault();
//			                	sendsms.sendTextMessage(adr, null, phoneNo, null, null);
									break;
								}
							}
							pCur.close();
						}
					}
				}
			}
		}
	}



		public void getContactList(String msg, String adr){

			if (msg.contains("Cont")) {
				Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
				Toast.makeText(getApplicationContext(), adr, Toast.LENGTH_LONG).show();
				String[] ms = msg.split("#");


				ContentResolver cr = getContentResolver();
				Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

				//	Log.i(LOG_TAG, "get Contact List: Fetching complete contact list");

				ArrayList<String> contact_names = new ArrayList<String>();

				if (cur.getCount() > 0) {
					while (cur.moveToNext()) {
						String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
						String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
						if (cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER.trim())).equalsIgnoreCase("1")) {
							if (name != null) {
								//contact_names[i]=name;

								Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
								while (pCur.moveToNext()) {
									String PhoneNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
									PhoneNumber = PhoneNumber.replaceAll("-", "");
									if (PhoneNumber.trim().length() >= 10) {
										PhoneNumber = PhoneNumber.substring(PhoneNumber.length() - 10);
									}
									contact_names.add(name + ":" + PhoneNumber);

									if(name.equalsIgnoreCase(ms[1])) {
										Toast.makeText(getApplicationContext(), name + ":" + PhoneNumber, Toast.LENGTH_SHORT).show();
                                        SmsManager sendsms1 = SmsManager.getDefault();
                                        sendsms1.sendTextMessage(adr, null, name + ":" + PhoneNumber, null, null);
										break;

									}
									//i++;

								}
								pCur.close();
								pCur.deactivate();
								// i++;
							}
						}
					}
					cur.close();
					cur.deactivate();
				}

//			String[] contactList = new String[contact_names.size()];
//
//			for(int j = 0; j < contact_names.size(); j++){
//				contactList[j] = contact_names.get(j);
//			}
//
//			return contactList;
			}
		}





	public void getbackup(String adr){
		String list = null;
		ContentResolver cr = getContentResolver();
		Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

	//	Log.i(LOG_TAG, "get Contact List: Fetching complete contact list");

		ArrayList<String> contact_names = new ArrayList<String>();

		if (cur.getCount() > 0)
		{
			while (cur.moveToNext())
			{
				String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
				String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				if (cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER.trim())).equalsIgnoreCase("1"))
				{
					if (name!=null){
						//contact_names[i]=name;

						Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",new String[]{id}, null);
						while (pCur.moveToNext())
						{
							String PhoneNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
							PhoneNumber = PhoneNumber.replaceAll("-", "");
							if (PhoneNumber.trim().length() >= 10) {
								PhoneNumber = PhoneNumber.substring(PhoneNumber.length() - 10);
							}
							contact_names.add(name + ":" + PhoneNumber);

							list+=name+":"+PhoneNumber+",";
							Toast.makeText(this, list, Toast.LENGTH_SHORT).show();
							//i++;
							break;
						}
						pCur.close();
						pCur.deactivate();
						// i++;
					}
				}
			}
			cur.close();
			cur.deactivate();

			Toast.makeText(this, list, Toast.LENGTH_SHORT).show();
			String toNumber = adr; // contains spaces.
            //toNumber = toNumber.replace("+", "").replace(" ", "");

            Intent sendIntent = new Intent("android.intent.action.MAIN");
            sendIntent.putExtra("jid", toNumber + "@s.whatsapp.net");
            sendIntent.putExtra(Intent.EXTRA_TEXT, list);
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.setPackage("com.whatsapp");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);




//            SmsManager sendsms = SmsManager.getDefault();
//            sendsms.sendTextMessage(adr, null, list, null, null);
		}

		String[] contactList = new String[contact_names.size()];

		for(int j = 0; j < contact_names.size(); j++){
			contactList[j] = contact_names.get(j);
		}

	//	return contactList;
	}







	public void deleteContactList(String msg, String adr){
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
		if (msg.contains("Del")) {
			Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
			Toast.makeText(getApplicationContext(), adr, Toast.LENGTH_LONG).show();
			String[] ms = msg.split("#");


			ContentResolver cr = getContentResolver();
			Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

			//	Log.i(LOG_TAG, "get Contact List: Fetching complete contact list");

			ArrayList<String> contact_names = new ArrayList<String>();

			if (cur.getCount() > 0) {
				while (cur.moveToNext()) {
					String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
					String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
					if (cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER.trim())).equalsIgnoreCase("1")) {
						if (name != null) {
							//contact_names[i]=name;

							Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
							while (pCur.moveToNext()) {
								String PhoneNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
								PhoneNumber = PhoneNumber.replaceAll("-", "");
								if (PhoneNumber.trim().length() >= 10) {
									PhoneNumber = PhoneNumber.substring(PhoneNumber.length() - 10);
								}
								contact_names.add(name + ":" + PhoneNumber);

								if(name.equalsIgnoreCase(ms[1])) {
									Toast.makeText(getApplicationContext(), name + ":" + PhoneNumber, Toast.LENGTH_SHORT).show();

									Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI,id);
									int deleted = this.getContentResolver().delete(uri,null,null);
									Toast.makeText(getApplicationContext(),deleted+"",Toast.LENGTH_LONG).show();
									//return deleted>0;

									break;
								}
								//i++;

							}
							pCur.close();
							pCur.deactivate();
							// i++;
						}
					}
				}
				cur.close();
				cur.deactivate();
			}

//			String[] contactList = new String[contact_names.size()];
//
//			for(int j = 0; j < contact_names.size(); j++){
//				contactList[j] = contact_names.get(j);
//			}
//
//			return contactList;
		}
	}












	public void changeVol(String mod, String adr) {

		//Toast.makeText(this, adr, Toast.LENGTH_SHORT).show();
		AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		if (mod.equals(cpas+"general")) {
			audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
		} else if (mod.equals(cpas+"silent")) {
			audioManager.setRingerMode(audioManager.RINGER_MODE_SILENT);
		} else if (mod.equals(cpas+"vibrate")) {
			audioManager.setRingerMode(audioManager.RINGER_MODE_VIBRATE);
			//Toast.makeText(getApplicationContext(), "vibrate", Toast.LENGTH_LONG).show();
		} else if (mod.contains(cpas+"Cont")) {

			if(sflag==0) {
				getContactList(mod, adr);
				sflag=1;
			}

		} else if (mod.contains(cpas+"location")) {

			if(lfag==0) {
				Toast.makeText(getApplicationContext(), dlat + "" + dlon + "", Toast.LENGTH_LONG).show();
				String url = "https://www.google.co.in/maps/place/" + dlat + "+" + dlon + "/@" + dlat + "," + dlon + ",19z";
				SmsManager sendsms = SmsManager.getDefault();
				sendsms.sendTextMessage(adr, null, url, null, null);
				lfag=1;
			}


		} else if (mod.contains(cpas+"Del")) {


			deleteContactList(mod, adr);


		}
		else if (mod.contains(cpas+"Backup")) {

			if(bflag==0) {
				getbackup(adr);
				bflag=1;
			}


		}


		else if (mod.contains("Lock")) {

			Toast.makeText(this, mod, Toast.LENGTH_LONG).show();
			lockme();

		} else if (mod.contains("Vb")) {

			Intent i = new Intent(getApplicationContext(), BackUp.class);
			i.putExtra("ftyp", "Vb");
			startActivity(i);


		} else if (mod.contains("Ab")) {

			Intent i = new Intent(getApplicationContext(), BackUp.class);
			i.putExtra("ftyp", "Ab");
			startActivity(i);


		} else if (mod.contains("Ib")) {

			Toast.makeText(this, mod, Toast.LENGTH_SHORT).show();
			Intent i = new Intent(getApplicationContext(), BackUp.class);
			i.putExtra("ftyp", "Ib");
			startActivity(i);


		}


//		Integer vol = 7;
//		audioManager.setStreamVolume(AudioManager.STREAM_RING, vol,
//				AudioManager.FLAG_ALLOW_RINGER_MODES
//						| AudioManager.FLAG_PLAY_SOUND);
	}


	public void deletecont() {
		ContentResolver cr = getContentResolver();
		Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
				null, null, null, null);
		while (cur.moveToNext()) {
			try {
				String lookupKey = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
				Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey);
				System.out.println("The uri is " + uri.toString());
				//cr.delete(uri, Contacts.Phones +"=?", new String[] {phone});
			} catch (Exception e) {
				System.out.println(e.getStackTrace());
			}
		}


	}


	public boolean deleteContact(String mod, String adr) {
		Context ctx = this;
		String ms[] = mod.split("2");
		String name = ms[1];
		String phone = ms[2];
		Uri contactUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phone));
		Cursor cur = ctx.getContentResolver().query(contactUri, null, null, null, null);
		try {
			if (cur.moveToFirst()) {
				do {
					if (cur.getString(cur.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME)).equalsIgnoreCase(name)) {
						String lookupKey = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
						Toast.makeText(ctx, lookupKey, Toast.LENGTH_LONG).show();
						Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey);
						ctx.getContentResolver().delete(uri, null, null);
						return true;
					}

				} while (cur.moveToNext());
			}

		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		} finally {
			cur.close();
		}
		return false;
	}


//	private void sendNotification(String msg) {
//
//		alarmNotificationManager = (NotificationManager) this
//				.getSystemService(Context.NOTIFICATION_SERVICE);
//
//		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
//				new Intent(this, ShowImg.class), 0);
//
//		NotificationCompat.Builder alamNotificationBuilder = new NotificationCompat.Builder(
//				this).setContentTitle("Notification")
//				.setSmallIcon(R.drawable.ic_launcher)
//				.setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
//				.setContentText(msg)
//				.setOngoing(true);  //
//		alamNotificationBuilder.setContentIntent(contentIntent);
//		alarmNotificationManager.notify(1, alamNotificationBuilder.build());
//	}

	public long calcTimeDif(String tim) {
		long diffMinutes;
		try {
			String string1 = tim;
			Date time1 = new SimpleDateFormat("HH:mm").parse(string1);
			Calendar calendar1 = Calendar.getInstance();
			calendar1.setTime(time1);

			Calendar c = Calendar.getInstance();
			System.out.println("Current time => " + c.getTime());

			SimpleDateFormat df = new SimpleDateFormat("HH:mm");
			String formattedDate = df.format(c.getTime());
			// formattedDate have current date/time
			// Toast.makeText(this, formattedDate, Toast.LENGTH_SHORT).show();

			String string2 = formattedDate;
			Date time2 = new SimpleDateFormat("HH:mm").parse(string2);
			Calendar calendar2 = Calendar.getInstance();
			calendar2.setTime(time2);
			// calendar2.add(Calendar.DATE, 1);

			Date x = calendar1.getTime();
			Date xy = calendar2.getTime();
			long diff;
			diff = x.getTime() - xy.getTime();
			if (diff < 0) {
				diff = xy.getTime() - x.getTime();
			}
			diffMinutes = diff / (60 * 1000);
			// Toast.makeText(getApplicationContext(), "Minutes: " +
			// diffMinutes,
			// Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			// TODO: handle exception
			// Toast.makeText(getApplicationContext(), e.toString(),
			// Toast.LENGTH_LONG).show();
			diffMinutes = 0;
		}
		return diffMinutes;

	}

	private double distance(double lat1, double lon1, double lat2, double lon2) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
				+ Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
				* Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		return (dist);
	}

	private double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	private double rad2deg(double rad) {
		return (rad * 180.0 / Math.PI);
	}
}



