package com.example.phone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.andrognito.patternlockview.utils.ResourceUtils;

import java.util.List;

public class SetPattern extends AppCompatActivity {
    PatternLockView patternLockView;
    DatabaseHelper db=new DatabaseHelper(this);
    private Object PatternLockViewListener;
    EditText e;
public static  String phno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setpattern);
        e=(EditText)findViewById(R.id.editText5);
        Toast.makeText(this, "Set", Toast.LENGTH_SHORT).show();

        String check=db.checkuser();
        Toast.makeText(this, check, Toast.LENGTH_SHORT).show();

        if(!check.equals("error")) {
        Intent in=new Intent(getApplicationContext(),DrawPattern.class);
            startActivity(in);

        }
//        else
//        {
//            Intent inte=new Intent(SetPattern.this,SetPattern.class);
//            startActivity(inte);
//
//        }






            final PatternLockView patternLockView;




            patternLockView = findViewById(R.id.patternView);


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


        patternLockView.addPatternLockListener(new PatternLockViewListener() {
                @Override
                public void onStarted() {


                }

                @Override
                public void onProgress(List progressPattern) {
                                   }

                @Override
                public void onComplete(List pattern) {
                    Toast.makeText(SetPattern.this, "ffff", Toast.LENGTH_SHORT).show();
                    Log.d(getClass().getName(), "Pattern complete: " +
                            PatternLockUtils.patternToString(patternLockView, pattern));
                  String p=  PatternLockUtils.patternToString(patternLockView, pattern);

                    boolean res=db.reg(p,e.getText().toString());

                    if(res==true) {

                        phno=e.getText().toString();
                        Toast.makeText(SetPattern.this, "success", Toast.LENGTH_SHORT).show();
                        patternLockView.setViewMode(PatternLockView.PatternViewMode.CORRECT);
                        Intent i=new Intent(getApplicationContext(),CommandPassword.class);
                        startActivity(i);
                    }
                        else
                        Toast.makeText(SetPattern.this, "unsuccessful", Toast.LENGTH_SHORT).show();

                    Toast.makeText(SetPattern.this, PatternLockUtils.patternToString(patternLockView, pattern), Toast.LENGTH_SHORT).show();
//                    if (PatternLockUtils.patternToString(patternLockView, pattern).equalsIgnoreCase("123")) {
//                        patternLockView.setViewMode(PatternLockView.PatternViewMode.CORRECT);
//                        Toast.makeText(SetPattern.this, "Welcome back", Toast.LENGTH_LONG).show();
//                    } else {
//                        patternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);
//                        Toast.makeText(SetPattern.this, "Incorrect password", Toast.LENGTH_LONG).show();
//                    }
                }

                @Override
                public void onCleared() {



                }
            });
        }

    @Override
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            finishAffinity();
            finish();
        }
    }
    }
