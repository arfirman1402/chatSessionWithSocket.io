package io.arfirman1402.chatsessionwithsocketio.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import io.arfirman1402.chatsessionwithsocketio.R;
import io.arfirman1402.chatsessionwithsocketio.model.ChatMessage;

/**
 * Created by arfirman1402 on 25/12/16.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ListHolder> {
    private List<ChatMessage> chatMessages;

    public ChatAdapter(List<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }

    @Override
    public ChatAdapter.ListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_chat, null);
        return new ChatAdapter.ListHolder(view);
    }

    @Override
    public void onBindViewHolder(final ChatAdapter.ListHolder holder, int position) {
        holder.chatUserName.setText(chatMessages.get(position).getUsername());
        holder.chatMessage.setText(chatMessages.get(position).getMessage());
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    class ListHolder extends RecyclerView.ViewHolder {
        TextView chatUserName;
        TextView chatMessage;

        ListHolder(View itemView) {
            super(itemView);
            chatUserName = (TextView) itemView.findViewById(R.id.chat_username);
            chatMessage = (TextView) itemView.findViewById(R.id.chat_message);
        }
    }
}