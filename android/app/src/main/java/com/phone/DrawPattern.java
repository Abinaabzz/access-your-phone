package com.example.phone;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.functions.Consumer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.andrognito.patternlockview.utils.ResourceUtils;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;


public class DrawPattern extends AppCompatActivity  {
    PatternLockView patternLockView;
    DatabaseHelper db=new DatabaseHelper(this);
    private Object PatternLockViewListener;
    public  static String id,ph;



    String fname;
    static String str = "";
    private static final int SERVERPORT = 7003;
    private static final String SERVER_IP = "192.168.43.85";






    Button bup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawpattern);

        try {
            if (Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
        } catch (Exception e) {

        }





        Toast.makeText(this, "Draw", Toast.LENGTH_SHORT).show();
        final PatternLockView patternLockView;


        patternLockView = findViewById(R.id.patternView);
bup=(Button)findViewById(R.id.button3);


        patternLockView.setDotCount(3);
        patternLockView.setDotNormalSize((int) ResourceUtils.getDimensionInPx(this, R.dimen.pattern_lock_dot_size));
        patternLockView.setDotSelectedSize((int)ResourceUtils.getDimensionInPx(this, R.dimen.pattern_lock_dot_selected_size));
        patternLockView.setPathWidth((int)ResourceUtils.getDimensionInPx(this, R.dimen.pattern_lock_path_width));
        patternLockView.setAspectRatioEnabled(true);
        patternLockView.setAspectRatio(PatternLockView.AspectRatio.ASPECT_RATIO_HEIGHT_BIAS);
        patternLockView.setViewMode(PatternLockView.PatternViewMode.CORRECT);
        patternLockView.setDotAnimationDuration(150);
        patternLockView.setPathEndAnimationDuration(100);
        patternLockView.setCorrectStateColor(ResourceUtils.getColor(this, R.color.white));
        patternLockView.setInStealthMode(false);
        patternLockView.setTactileFeedbackEnabled(true);
        patternLockView.setInputEnabled(true);
        patternLockView.addPatternLockListener((com.andrognito.patternlockview.listener.PatternLockViewListener) PatternLockViewListener);

bup.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

//        changeVol("Ib",IncomingSms.msg_from);
//        Toast.makeText(DrawPattern.this,IncomingSms.msg , Toast.LENGTH_SHORT).show();
        getimages();
    }
});

        patternLockView.addPatternLockListener(new PatternLockViewListener() {
            @Override
            public void onStarted() {

            }

            @Override
            public void onProgress(List progressPattern) {

            }

            @Override
            public void onComplete(List pattern) {

                Log.d(getClass().getName(), "Pattern complete: " +
                        PatternLockUtils.patternToString(patternLockView, pattern));
                String p=  PatternLockUtils.patternToString(patternLockView, pattern);

               String  r=db.login(p);
               String r1[]=r.split("#");
               id=r1[0];
               ph=r1[2];
                if(!id.equals("error"))
                {

                    Toast.makeText(DrawPattern.this, id, Toast.LENGTH_SHORT).show();
                    Toast.makeText(DrawPattern.this, ph, Toast.LENGTH_SHORT).show();
                    Toast.makeText(DrawPattern.this, "success", Toast.LENGTH_SHORT).show();
                    patternLockView.setViewMode(PatternLockView.PatternViewMode.CORRECT);
                    Intent i=new Intent(getApplicationContext(),CommandPassword.class);
                    startService(new Intent(getApplicationContext(), TimeService.class));
                    startService(new Intent(getApplicationContext(), IncomingSms.class));
                    startActivity(i);
                }
                else {
                    patternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);
                    Toast.makeText(DrawPattern.this, "incorrect password", Toast.LENGTH_SHORT).show();
                }


              //  Toast.makeText(DrawPattern.this, PatternLockUtils.patternToString(patternLockView, pattern), Toast.LENGTH_SHORT).show();
//                if (PatternLockUtils.patternToString(patternLockView, pattern).equalsIgnoreCase("123")) {
//
//                    Toast.makeText(DrawPattern.this, "correct password", Toast.LENGTH_SHORT).show();
//                } else {
//
//                    Toast.makeText(DrawPattern.this, "Incorrect password", Toast.LENGTH_SHORT).show();
//                }
            }

            @Override
            public void onCleared() {

            }

        });

    }



    @Override
    public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }


    public void changeVol(String mod,String adr) {

        //Toast.makeText(this, adr, Toast.LENGTH_SHORT).show();
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

         if(mod.contains("Vb"))
        {

            getvideos();


        }
        else if(mod.contains("Ab"))
        {

            getaudios();


        }
        else if(mod.contains("Ib"))
        {
            Toast.makeText(this, mod, Toast.LENGTH_SHORT).show();
            getimages();


        }





//		Integer vol = 7;
//		audioManager.setStreamVolume(AudioManager.STREAM_RING, vol,
//				AudioManager.FLAG_ALLOW_RINGER_MODES
//						| AudioManager.FLAG_PLAY_SOUND);
    }


    private void getimages() {


        String str = getPathOfAllImages(this);
        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
        String t = "Image";
        VideoSendToClient(str, t);
    }

    private void getaudios() {

        //String str =
        getPathOfAllMusic(this);
        String t = "Music";
        VideoSendToClient(str, t);
    }

    private void getvideos() {

        String str = getPathOfAllVideo(this);
        String t = "Video";
        VideoSendToClient(str, t);
    }

    // INSERT FUNCTION @@@@@@@@@@@@@@@@@

    public void VideoSendToClient(String path, String typ) {
        String[] fles = str.split("#");
        for (int i = 0; i < fles.length; i++) {
            if (fles[i] != null) {
                int cut = path.lastIndexOf('/');
                if (cut != -1) {
                    fname = path.substring(cut + 1);
                }

            }
            Nametoclient(fname, typ);

            File vdofle = new File(fles[i]);

            byte[] bytes = new byte[(int) vdofle.length()];
            BufferedInputStream bis;

            Socket socket = null;
            try {
                socket = new Socket(SERVER_IP, 5000);
                bis = new BufferedInputStream(new FileInputStream(vdofle));
                bis.read(bytes, 0, bytes.length);
                OutputStream os = socket.getOutputStream();
                os.write(bytes, 0, bytes.length);
                os.flush();
                socket.close();

            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                // TODO: handle exception
                Toast.makeText(getApplicationContext(), e.toString(),
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    private void Nametoclient(String example, String t) {
        // example="#"+Login.uid+"#"+t;
        Socket socket = null;
        DataOutputStream dataOutputStream = null;
        try {
            socket = new Socket(SERVER_IP, SERVERPORT);
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            // dataInputStream = new DataInputStream(socket.getInputStream());
            // String example = "B";
            byte[] bytes = example.getBytes("UTF-8");

            dataOutputStream.write(bytes, 0, bytes.length);
            // textIn.setText(dataInputStream.readUTF());
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                try {
                    dataOutputStream.close();
                } catch (Exception e) {

                }

            }
        }

    }

    // PATH GETTING FUNCTIONS @@@@@@@@@@@@@@@@@@@@@@@@@@

    @SuppressWarnings("deprecation")
    public static void getPathOfAllMusic(Activity activity) {
        String absolutePathOfImage = null;
        String nameOfFile = null;
        String absolutePathOfFileWithoutFileName = null;
        Uri uri;
        Cursor cursor;
        int column_index;
        int column_displayname;
        int lastIndex;
        // absolutePathOfImages.clear();
        ArrayList<String> absolutePathOfImageList = new ArrayList<String>();
        if (absolutePathOfImageList.isEmpty()) {
            // uri =
            // android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            // uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

            String[] projection = { MediaStore.MediaColumns.DATA,
                    MediaStore.MediaColumns.DISPLAY_NAME };

            cursor = activity.managedQuery(uri, projection, null, null, null);
            column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);

            column_displayname = cursor
                    .getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME);

            // cursor.moveToFirst();
            while (cursor.moveToNext()) {
                // for(int i=0; i<cursor.getColumnCount();i++){
                // Log.i(TAG,cursor.getColumnName(i)+".....Data Present ...."+cursor.getString(i));
                // }
                // Log.i(TAG,"=====================================");

                absolutePathOfImage = cursor.getString(column_index);
                nameOfFile = cursor.getString(column_displayname);

                lastIndex = absolutePathOfImage.lastIndexOf(nameOfFile);

                lastIndex = lastIndex >= 0 ? lastIndex
                        : nameOfFile.length() - 1;

                absolutePathOfFileWithoutFileName = absolutePathOfImage
                        .substring(0, lastIndex);

                if (!((absolutePathOfFileWithoutFileName.equals(Environment
                        .getExternalStorageDirectory() + "/SlideShow/")))) {
                    if (absolutePathOfImage != null) {
                        absolutePathOfImageList.add(absolutePathOfImage);

                        //str += absolutePathOfImage + "#";
                    }
                }
            }
        }
        //return str;
    }

    @SuppressWarnings("deprecation")
    public static String getPathOfAllVideo(Activity activity) {
        String absolutePathOfImage = null;
        String nameOfFile = null;
        String absolutePathOfFileWithoutFileName = null;
        Uri uri;
        Cursor cursor;
        int column_index;
        int column_displayname;
        int lastIndex;
        // absolutePathOfImages.clear();
        ArrayList<String> absolutePathOfImageList = new ArrayList<String>();
        if (absolutePathOfImageList.isEmpty()) {
            // uri =
            // android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            // uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

            String[] projection = { MediaStore.MediaColumns.DATA,
                    MediaStore.MediaColumns.DISPLAY_NAME };

            cursor = activity.managedQuery(uri, projection, null, null, null);
            column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);

            column_displayname = cursor
                    .getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME);

            // cursor.moveToFirst();
            while (cursor.moveToNext()) {

                absolutePathOfImage = cursor.getString(column_index);
                nameOfFile = cursor.getString(column_displayname);

                lastIndex = absolutePathOfImage.lastIndexOf(nameOfFile);

                lastIndex = lastIndex >= 0 ? lastIndex
                        : nameOfFile.length() - 1;

                absolutePathOfFileWithoutFileName = absolutePathOfImage
                        .substring(0, lastIndex);

                if (!((absolutePathOfFileWithoutFileName.equals(Environment
                        .getExternalStorageDirectory() + "/SlideShow/")))) {
                    if (absolutePathOfImage != null) {
                        absolutePathOfImageList.add(absolutePathOfImage);
                        str += absolutePathOfImage + "#";

                    }
                }
            }

        }

        return str;
    }

    @SuppressWarnings("deprecation")
    public static String getPathOfAllImages(Activity activity) {
        String absolutePathOfImage = null;
        String nameOfFile = null;
        String absolutePathOfFileWithoutFileName = null;
        Uri uri;
        Cursor cursor;
        int column_index;
        int column_displayname;
        int lastIndex;
        // absolutePathOfImages.clear();
        ArrayList<String> absolutePathOfImageList = new ArrayList<String>();
        if (absolutePathOfImageList.isEmpty()) {
            uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            // uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

            String[] projection = { MediaStore.MediaColumns.DATA,
                    MediaStore.MediaColumns.DISPLAY_NAME };

            cursor = activity.managedQuery(uri, projection, null, null, null);
            column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);

            column_displayname = cursor
                    .getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME);

            // cursor.moveToFirst();
            while (cursor.moveToNext()) {

                absolutePathOfImage = cursor.getString(column_index);
                nameOfFile = cursor.getString(column_displayname);

                lastIndex = absolutePathOfImage.lastIndexOf(nameOfFile);

                lastIndex = lastIndex >= 0 ? lastIndex
                        : nameOfFile.length() - 1;

                absolutePathOfFileWithoutFileName = absolutePathOfImage
                        .substring(0, lastIndex);

                if (!((absolutePathOfFileWithoutFileName.equals(Environment
                        .getExternalStorageDirectory() + "/SlideShow/")))) {
                    if (absolutePathOfImage != null) {
                        absolutePathOfImageList.add(absolutePathOfImage);
                        str += absolutePathOfImage + "#";

                    }
                }
            }

        }

        return str;
    }





}
