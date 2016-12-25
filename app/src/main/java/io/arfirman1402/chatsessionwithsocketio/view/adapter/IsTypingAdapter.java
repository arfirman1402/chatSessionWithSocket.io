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

public class IsTypingAdapter extends RecyclerView.Adapter<IsTypingAdapter.ListHolder> {
    private List<String> isTypingList;

    public IsTypingAdapter(List<String> isTypingList) {
        this.isTypingList = isTypingList;
    }

    @Override
    public IsTypingAdapter.ListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_istyping, null);
        return new IsTypingAdapter.ListHolder(view);
    }

    @Override
    public void onBindViewHolder(final IsTypingAdapter.ListHolder holder, int position) {
        holder.isTypingUserName.setText(isTypingList.get(position));
    }

    @Override
    public int getItemCount() {
        return isTypingList.size();
    }

    class ListHolder extends RecyclerView.ViewHolder {
        TextView isTypingUserName;

        ListHolder(View itemView) {
            super(itemView);
            isTypingUserName = (TextView) itemView.findViewById(R.id.istyping_username);
        }
    }
}