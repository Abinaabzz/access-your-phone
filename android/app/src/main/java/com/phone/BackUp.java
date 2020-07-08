package com.example.phone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class BackUp extends AppCompatActivity {
    String fname;
    static String str = "";
    private static final int SERVERPORT = 7003;
    private static final String SERVER_IP = "192.168.43.85";
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_up);

        String ftyp=getIntent().getStringExtra("ftyp");

        Toast.makeText(this, ftyp, Toast.LENGTH_SHORT).show();
        if(ftyp.equals("Vb"))
        {
                getvideos();
        }
        else if(ftyp.equals("Ab"))
        {
            getaudios();

        }
        else if(ftyp.equals("Ib"))
        {
            getimages();
        }

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
