package io.arfirman1402.chatsessionwithsocketio.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by arfirman1402 on 24/12/16.
 */

public class ChatMessage {
    @SerializedName("username")
    private String username;

    @SerializedName("message")
    private String message;

    public ChatMessage() {
    }

    public ChatMessage(String username, String message) {
        this.username = username;
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }
}
