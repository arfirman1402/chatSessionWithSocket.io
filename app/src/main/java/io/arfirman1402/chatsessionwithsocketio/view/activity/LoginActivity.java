package io.arfirman1402.chatsessionwithsocketio.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import io.arfirman1402.chatsessionwithsocketio.R;

/**
 * Created by arfirman1402 on 24/12/16.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private TextInputEditText loginUsername;
    private Button loginStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setView();
    }

    private void setView() {
        loginUsername = (TextInputEditText) findViewById(R.id.login_username);
        loginStart = (Button) findViewById(R.id.login_start);

        loginStart.setOnClickListener(this);
    }

    public static void startNewActivity(Context context) {
        Intent i = new Intent(context, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
        ((Activity) context).finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_start:
                startChat();
                break;
            default:
                break;
        }
    }

    private void startChat() {
        MainActivity.startNewActivity(LoginActivity.this, loginUsername.getText().toString());
    }
}
