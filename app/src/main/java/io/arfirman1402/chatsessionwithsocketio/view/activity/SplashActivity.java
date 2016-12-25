package io.arfirman1402.chatsessionwithsocketio.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import io.arfirman1402.chatsessionwithsocketio.R;

/**
 * Created by arfirman1402 on 24/12/16.
 */

public class SplashActivity extends AppCompatActivity {
    private long TIMEOUT_HANDLER = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        doSplash();
    }

    private void doSplash() {
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                LoginActivity.startNewActivity(SplashActivity.this);
            }
        };
        handler.postDelayed(runnable,TIMEOUT_HANDLER);
    }
}
