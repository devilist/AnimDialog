package com.devilist.animd;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.devilist.animd.animmediaplayer.MediaPlayerDialog;
import com.devilist.animd.circle_anim_dialog.CircleDialog;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.tv_player).setOnClickListener(this);
        findViewById(R.id.tv_circle).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_player:
                String url = "android.resource://" + getPackageName() + "/" + R.raw.video;
                MediaPlayerDialog.newInstance(url, url)
                        .show(getSupportFragmentManager());
                break;
            case R.id.tv_circle:
                CircleDialog.newInstance().show(getSupportFragmentManager());
                break;
        }
    }
}
