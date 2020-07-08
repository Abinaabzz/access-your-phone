package com.example.phone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.ksoap2.serialization.SoapObject;

public class ChangeCommandPassword extends AppCompatActivity implements View.OnClickListener {
    EditText e1,e2,e3;
    Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_command_password);
        e1=(EditText)findViewById(R.id.editText2);
        e2=(EditText)findViewById(R.id.editText3);
        e3=(EditText)findViewById(R.id.editText4);
        b1=(Button)findViewById(R.id.button2);
        b1.setOnClickListener(this);



        try
        {
            if (android.os.Build.VERSION.SDK_INT > 9)
            {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
        }
        catch(Exception e)
        {

        }


    }
    @Override
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            finishAffinity();
            finish();
        }
    }
    @Override
    public void onClick(View view) {

        SoapObject obj=new SoapObject(soapclass.NAMESPACE,"commadcheck");
        obj.addProperty("id",DrawPattern.id);
        obj.addProperty("cmndpass",e1.getText().toString());
        soapclass sc=new soapclass();
        String ou=sc.Callsoap(obj,"http://tempuri.org/commadcheck");
        if(!ou.equals("")&&!ou.equals("error"))
        {
            if(e2.getText().toString().equals(e3.getText().toString()))
            {
            SoapObject obj1=new SoapObject(soapclass.NAMESPACE,"commandchange");
            obj1.addProperty("id",DrawPattern.id);
            obj1.addProperty("cmndpass",e2.getText().toString());
            soapclass sc1=new soapclass();
            String ou1=sc1.Callsoap(obj1,"http://tempuri.org/commandchange");
            if(!ou1.equals("")&&!ou1.equals("error"))
            {

                Toast.makeText(getApplicationContext(),"Password Changed",Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"unsuccessupdate",Toast.LENGTH_SHORT).show();
            }
        }}
        else
        {
            Toast.makeText(getApplicationContext(),"unsuccesscheck",Toast.LENGTH_SHORT).show();
        }
    }

    }

