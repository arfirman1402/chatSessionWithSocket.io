package io.arfirman1402.chatsessionwithsocketio.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.arfirman1402.chatsessionwithsocketio.R;
import io.arfirman1402.chatsessionwithsocketio.app.ChatSessionApp;
import io.arfirman1402.chatsessionwithsocketio.model.ChatMessage;
import io.arfirman1402.chatsessionwithsocketio.util.BaseConstant;
import io.arfirman1402.chatsessionwithsocketio.view.adapter.ChatAdapter;
import io.arfirman1402.chatsessionwithsocketio.view.adapter.IsTypingAdapter;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG = MainActivity.class.getSimpleName();
    private String userName;

    private RecyclerView mainChatList;
    private RecyclerView mainChatIsTyping;
    private EditText mainSendEdit;
    private Button mainSendButton;

    private Handler handlerTyping;
    private Runnable runTyping;
    private final int TIMEOUT_TYPING = 2000;
    private boolean isTyping = false;

    private Socket socket;

    private ChatAdapter chatAdapter;
    private List<ChatMessage> chatMessages;

    private List<String> isTypingList;
    private IsTypingAdapter isTypingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userName = getIntent().getStringExtra("userName");

        mainChatList = (RecyclerView) findViewById(R.id.main_chat_list);
        mainChatIsTyping = (RecyclerView) findViewById(R.id.main_chat_istyping);
        mainSendEdit = (EditText) findViewById(R.id.main_send_edit);
        mainSendButton = (Button) findViewById(R.id.main_send_button);

        LinearLayoutManager layoutManagerChatList = new LinearLayoutManager(this);
        mainChatList.setLayoutManager(layoutManagerChatList);
        mainChatList.setHasFixedSize(true);

        LinearLayoutManager layoutManagerIsTyping = new LinearLayoutManager(this);
        mainChatIsTyping.setLayoutManager(layoutManagerIsTyping);
        mainChatIsTyping.setHasFixedSize(true);

        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatMessages);
        mainChatList.setAdapter(chatAdapter);

        isTypingList = new ArrayList<>();
        isTypingAdapter = new IsTypingAdapter(isTypingList);
        mainChatList.setAdapter(chatAdapter);

        mainSendEdit.addTextChangedListener(new SendEditTextChanged());

        mainSendButton.setOnClickListener(this);

        handlerTyping = new Handler();

        runTyping = new Runnable() {
            @Override
            public void run() {
                isTyping = false;
            }
        };

        socket = ChatSessionApp.getInstance().getSocket();

        startSocket();
    }

    public static void startNewActivity(Context context, String username) {
        Intent i = new Intent(context, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("userName", username);
        context.startActivity(i);
        ((Activity) context).finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        disconnectSocket();
    }

    @Override
    protected void onResume() {
        super.onResume();
        connectSocket();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroySocket();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_send_button:
                sendChat();
                break;
            default:
                break;
        }
    }

    private void startSocket() {
        socket.on(Socket.EVENT_CONNECT, onSocketConnect);
        socket.on(Socket.EVENT_DISCONNECT, onSocketDisconnect);
        socket.on(Socket.EVENT_CONNECT_ERROR, onSocketConnectError);
        socket.on(Socket.EVENT_CONNECT_TIMEOUT, onSocketConnectTimeout);
        socket.on(BaseConstant.EVENT_NEW_MESSAGE, onSocketNewMessage);
        socket.on(BaseConstant.EVENT_IS_TYPING, onSocketIsTyping);
        socket.on(BaseConstant.EVENT_PAUSED, onSocketPaused);
        socket.on(BaseConstant.EVENT_JOIN_ROOM, onSocketJoinRoom);
        socket.on(BaseConstant.EVENT_LEAVE_ROOM, onSocketLeaveRoom);
    }

    private void destroySocket() {
        socket.off(Socket.EVENT_CONNECT, onSocketConnect);
        socket.off(Socket.EVENT_DISCONNECT, onSocketDisconnect);
        socket.off(Socket.EVENT_CONNECT_ERROR, onSocketConnectError);
        socket.off(Socket.EVENT_CONNECT_TIMEOUT, onSocketConnectTimeout);
        socket.off(BaseConstant.EVENT_NEW_MESSAGE, onSocketNewMessage);
        socket.off(BaseConstant.EVENT_IS_TYPING, onSocketIsTyping);
        socket.off(BaseConstant.EVENT_PAUSED, onSocketPaused);
        socket.off(BaseConstant.EVENT_JOIN_ROOM, onSocketJoinRoom);
        socket.off(BaseConstant.EVENT_LEAVE_ROOM, onSocketLeaveRoom);
    }

    private void connectSocket() {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setUsername("You");
        chatMessage.setMessage("has joined the room");
        addChatMessage(chatMessage);
        socket.connect();
    }

    private void disconnectSocket() {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setUsername("You");
        chatMessage.setMessage("has leaved the room");
        addChatMessage(chatMessage);
        socket.emit(BaseConstant.EVENT_LEAVE_ROOM, userName);
        socket.disconnect();
    }

    private Emitter.Listener onSocketConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d(TAG, "Socket Connected");
            Log.d(TAG, "Socket id = " + socket.id());
            socket.emit(BaseConstant.EVENT_JOIN_ROOM, userName);
        }
    };

    private Emitter.Listener onSocketDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d(TAG, "Socket Disconnected");
        }
    };

    private Emitter.Listener onSocketConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.e(TAG, "Socket Connect Error");
        }
    };

    private Emitter.Listener onSocketConnectTimeout = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.e(TAG, "Socket Connect Timeout");
        }
    };

    private Emitter.Listener onSocketNewMessage = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d(TAG, "Socket New Message");
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setUsername(args[0].toString());
            chatMessage.setMessage(args[1].toString());
            addChatMessage(chatMessage);
        }
    };

    private void addChatMessage(final ChatMessage chatMessage) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                chatMessages.add(chatMessage);
                chatAdapter.notifyDataSetChanged();
                mainChatList.scrollToPosition(chatMessages.size() - 1);

            }
        });
    }

    private Emitter.Listener onSocketIsTyping = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d(TAG, "Socket Is Typing");
            String username = args[0].toString();
            addChatIsTyping(username);
        }
    };

    private void addChatIsTyping(final String username) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                isTypingList.add(username);
                isTypingAdapter.notifyDataSetChanged();
                mainChatIsTyping.scrollToPosition(isTypingList.size() - 1);
            }
        });
    }

    private Emitter.Listener onSocketPaused = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d(TAG, "Socket Paused");
            String username = args[0].toString();
            removeChatIsTyping(username);
        }
    };

    private void removeChatIsTyping(final String username) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                isTypingList.remove(username);
                isTypingAdapter.notifyDataSetChanged();
                mainChatIsTyping.scrollToPosition(isTypingList.size() - 1);
            }
        });
    }

    private Emitter.Listener onSocketJoinRoom = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d(TAG, "Socket Join Room");
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setUsername(args[0].toString());
            chatMessage.setMessage("has joined the room");
            addChatMessage(chatMessage);
        }
    };

    private Emitter.Listener onSocketLeaveRoom = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d(TAG, "Socket Leave Room");
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setUsername(args[0].toString());
            chatMessage.setMessage("has leaved the room");
            addChatMessage(chatMessage);
        }
    };

    private void sendChat() {
        if (mainSendEdit.getText().toString().trim().length() > 0) {
            sendChatToRoom();
            mainSendEdit.setText("");
        } else {
            Toast.makeText(getApplicationContext(), "Mohon isi pesan sebelum dikirim.", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendChatToRoom() {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setUsername("You");
        chatMessage.setMessage(mainSendEdit.getText().toString());
        addChatMessage(chatMessage);
        socket.emit(BaseConstant.EVENT_NEW_MESSAGE, chatMessage.getMessage(), chatMessage.getMessage());
    }

    private class SendEditTextChanged implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!isTyping) {
                isTyping = true;
                handlerTyping.removeCallbacks(runTyping);
                handlerTyping.postDelayed(runTyping, TIMEOUT_TYPING);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
