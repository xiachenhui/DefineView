package com.xch.im.imtest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    OvalToCircle mButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButton = (OvalToCircle) findViewById(R.id.animation_btn);
        mButton.setAnimationButtonListener(new OvalToCircle.AnimationButtonListener() {
            @Override
            public void onClickListener() {
                mButton.start();
            }

            @Override
            public void animationFinish() {
                mButton.reset();
            }
        });
    }
}


