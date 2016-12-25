package io.arfirman1402.chatsessionwithsocketio.model;

/**
 * Created by arfirman1402 on 24/12/16.
 */

public class ChatMessage {
    private String username;
    private String message;
    private boolean isTyping;

    public ChatMessage() {
    }

    public ChatMessage(String username, String message, boolean isTyping) {
        this.username = username;
        this.message = message;
        this.isTyping = isTyping;
    }

    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTyping(boolean typing) {
        isTyping = typing;
    }
}
