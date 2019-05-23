package com.android.nejm.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.nejm.R;
import com.android.nejm.activitys.NotifyDetailActivity;
import com.android.nejm.bean.AnnounceRecord;
import com.android.nejm.data.AnnounceMessage;
import com.android.nejm.db.AnnouceRecordManager;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotifyMessageAdapter extends RecyclerView.Adapter<NotifyMessageAdapter.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private List<AnnounceMessage.MessageItem> messageItemList;
    OnItemClickListener onItemClickListener;

    public NotifyMessageAdapter(Context context, OnItemClickListener listener) {
       this.context = context;
       inflater = LayoutInflater.from(context);
       this.onItemClickListener = listener;
    }

    public void setData(List<AnnounceMessage.MessageItem> itemList) {
        this.messageItemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.notify_message_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        AnnounceMessage.MessageItem messageItem = messageItemList.get(i);
        viewHolder.textViewUnread.setVisibility(messageItem.read ? View.GONE : View.VISIBLE);
        viewHolder.textViewTitle.setText(messageItem.title);
        viewHolder.textViewPostDate.setText(String.format(Locale.CHINA, "系统通知 %s", messageItem.postdate));
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null) {
                    onItemClickListener.onItemClicked(i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return messageItemList != null ? messageItemList.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.textViewUnread)
        TextView textViewUnread;
        @BindView(R.id.textViewTitle)
        TextView textViewTitle;
        @BindView(R.id.textViewPostDate)
        TextView textViewPostDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClickListener {
        void onItemClicked(int index);
    }
}
