package com.example.phone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.ksoap2.serialization.SoapObject;

public class CommandPassword extends AppCompatActivity implements View.OnClickListener {
    EditText e;
    Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_command_password);

//        SoapObject obj2=new SoapObject(soapclass.NAMESPACE,"commadsetcheck");
//        obj2.addProperty("id",DrawPattern.id);
//        soapclass sc1=new soapclass();
//        String ou2=sc1.Callsoap(obj2,"http://tempuri.org/commadsetcheck");
//        if(!ou2.equals("")&&!ou2.equals("error"))
//        {
//            Intent inte=new Intent(CommandPassword.this,ViewHelp.class);
//            startActivity(inte);
//            Toast.makeText(getApplicationContext(),"sSuccess",Toast.LENGTH_SHORT).show();
//        }
//        else
//        {
//            Intent inten=new Intent(CommandPassword.this,CommandPassword.class);
//            startActivity(inten);
//
//            Toast.makeText(getApplicationContext(),"unsuccesstocheckcommd",Toast.LENGTH_SHORT).show();
//        }


        e=(EditText)findViewById(R.id.editText);
        b=(Button)findViewById(R.id.button);
        b.setOnClickListener(this);





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
    public boolean onCreateOptionsMenu(Menu m) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menufile,m);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.item1) {
            Intent intent=new Intent(CommandPassword.this,ChangePattern.class);
            startActivity(intent);
            return true;
        }
        if(id==R.id.item2){
            Intent in=new Intent(CommandPassword.this,ChangeCommandPassword.class);
            startActivity(in);

            return true;
        }
        if(id==R.id.item3){
            Intent intent=new Intent(CommandPassword.this,ViewHelp.class);
            startActivity(intent);

            return true;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        SoapObject obj=new SoapObject(soapclass.NAMESPACE,"commandinsert");
        obj.addProperty("id",DrawPattern.id);
        obj.addProperty("cmndpass",e.getText().toString());
        soapclass sc=new soapclass();
        String ou=sc.Callsoap(obj,"http://tempuri.org/commandinsert");
        if(!ou.equals("")&&!ou.equals("error"))
        {
            Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"unsuccess",Toast.LENGTH_SHORT).show();
        }
    }
}
