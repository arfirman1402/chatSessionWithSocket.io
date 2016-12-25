package io.arfirman1402.chatsessionwithsocketio.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
        setTitle("Set Username");

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
        loginUsername.setText(loginUsername.getText().toString().trim());

        if (!loginUsername.getText().toString().isEmpty()) {
            if (!(loginUsername.getText().toString().length() > 20)) {
                MainActivity.startNewActivity(LoginActivity.this, loginUsername.getText().toString());
            } else {
                Toast.makeText(getApplicationContext(), "Nama anda terlalu panjang (max.20 karakter).", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Nama anda tidak boleh kosong.", Toast.LENGTH_SHORT).show();
        }
    }
}
