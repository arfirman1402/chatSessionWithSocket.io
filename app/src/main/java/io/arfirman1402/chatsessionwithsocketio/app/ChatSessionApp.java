package io.arfirman1402.chatsessionwithsocketio.app;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.URISyntaxException;

import io.arfirman1402.chatsessionwithsocketio.util.BaseConstant;
import io.socket.client.IO;
import io.socket.client.Socket;

/**
 * Created by arfirman1402 on 24/12/16.
 */

public class ChatSessionApp extends Application implements Application.ActivityLifecycleCallbacks {
    private static ChatSessionApp instance;
    private Gson gson;
    private String TAG = this.getApplicationContext().getClass().getSimpleName();
    private Socket socket;

    @Override
    public void onCreate() {
        super.onCreate();
        if (instance == null) {
            instance = new ChatSessionApp();
        }

        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();

        generateSocket();

        registerActivityLifecycleCallbacks(this);
    }

    private void generateSocket() {
        try {
            socket = IO.socket(BaseConstant.HOST_SERVER_SOCKET_IO);
        } catch (URISyntaxException e) {
            Log.e(TAG, "Error = " + e.toString());
        }
    }

    public static ChatSessionApp getInstance() {
        return instance;
    }

    public Gson getGson() {
        return gson;
    }

    public Socket getSocket() {
        if (socket == null) {
            generateSocket();
        }
        return socket;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        String simpleNameClass = activity.getClass().getSimpleName();
        Log.d(TAG, simpleNameClass + " is Created");

    }

    @Override
    public void onActivityStarted(Activity activity) {
        String simpleNameClass = activity.getClass().getSimpleName();
        Log.d(TAG, simpleNameClass + " is Started");
    }

    @Override
    public void onActivityResumed(Activity activity) {
        String simpleNameClass = activity.getClass().getSimpleName();
        Log.d(TAG, simpleNameClass + " is Resumed");
    }

    @Override
    public void onActivityPaused(Activity activity) {
        String simpleNameClass = activity.getClass().getSimpleName();
        Log.d(TAG, simpleNameClass + " is Paused");
    }

    @Override
    public void onActivityStopped(Activity activity) {
        String simpleNameClass = activity.getClass().getSimpleName();
        Log.d(TAG, simpleNameClass + " is Stopped");
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        String simpleNameClass = activity.getClass().getSimpleName();
        Log.d(TAG, simpleNameClass + " save the Instance");
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        String simpleNameClass = activity.getClass().getSimpleName();
        Log.d(TAG, simpleNameClass + " is Destroyed");
    }

    @Override
    public void onTrimMemory(int level) {
        if (level == TRIM_MEMORY_UI_HIDDEN) {
            Log.d(TAG, "App is Background now");
        } else {
            Log.d(TAG, "Unknown trim memory level. Level = " + level);
        }
        super.onTrimMemory(level);
    }
}
