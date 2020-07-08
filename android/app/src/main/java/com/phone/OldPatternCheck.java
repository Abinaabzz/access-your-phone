package com.example.phone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.andrognito.patternlockview.utils.ResourceUtils;

import java.util.List;

public class OldPatternCheck extends AppCompatActivity {
    PatternLockView patternLockView;
    DatabaseHelper db=new DatabaseHelper(this);
    private Object PatternLockViewListener;
    public  static String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_old_pattern_check);
        Toast.makeText(this, "OldPatterncheck", Toast.LENGTH_SHORT).show();
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

                id=db.login(p);
                if(!id.equals("error"))
                {

                    patternLockView.setViewMode(PatternLockView.PatternViewMode.CORRECT);
                    Intent i=new Intent(getApplicationContext(),ChangePattern.class);
                    startActivity(i);
                }
                else {
                    patternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);
                    Toast.makeText(OldPatternCheck.this, "incorrect password", Toast.LENGTH_SHORT).show();
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
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            finishAffinity();
            finish();
        }
    }
}
