package com.example.phone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setpattern);
        Toast.makeText(this, "Set", Toast.LENGTH_SHORT).show();

        String check=db.checkuser();

        if(!check.equals("error")) {
        Intent in=new Intent(getApplicationContext(),DrawPattern.class);
            startActivity(in);

        }
        else
        {
            Intent inte=new Intent(getApplicationContext(),SetPattern.class);
            startActivity(inte);

        }






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

                    Log.d(getClass().getName(), "Pattern complete: " +
                            PatternLockUtils.patternToString(patternLockView, pattern));
                  String p=  PatternLockUtils.patternToString(patternLockView, pattern);

                    boolean res=db.reg(p);
                    if(res==true) {
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
    }
