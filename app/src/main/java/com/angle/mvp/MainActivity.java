package com.angle.mvp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.angle.mvp.test.VideoFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        getSupportFragmentManager().beginTransaction().add(R.id.fl,new VideoFragment()).commitNow();
    }
}
