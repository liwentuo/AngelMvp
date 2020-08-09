package com.angle.mcmvp;


import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.angle.mcmvp.vedio.VideoFragment;

public class MainActivity extends AppCompatActivity {

    private FrameLayout fl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        fl = (FrameLayout) findViewById(R.id.fl);
        getSupportFragmentManager().beginTransaction().add(R.id.fl, new VideoFragment()).commit();
    }
}
