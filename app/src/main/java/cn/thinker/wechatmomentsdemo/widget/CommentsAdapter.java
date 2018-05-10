package cn.thinker.wechatmomentsdemo.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.thinker.wechatmomentsdemo.R;
import cn.thinker.wechatmomentsdemo.model.data.CommentInfo;

public class CommentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater mInflater;
    private List<CommentInfo> mComments;

    public CommentsAdapter (Context context) {
        mInflater = LayoutInflater.from(context);
    }

    public static class CommentsViewHolder extends  RecyclerView.ViewHolder {
        public TextView senderText;
        public TextView contentText;

        public CommentsViewHolder(View itemView) {
            super(itemView);
            senderText = (TextView) itemView.findViewById(R.id.comments_nick_name_text);
            contentText = (TextView) itemView.findViewById(R.id.comments_content_text);
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mInflater.inflate(R.layout.tweet_comments_item_layout, parent, false);
        return new CommentsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CommentInfo ci = mComments.get(position);
        CommentsViewHolder cv = (CommentsViewHolder) holder;
        if (ci != null) {
            if (ci.sender != null && !TextUtils.isEmpty(ci.sender.nick)) {
                cv.senderText.setText(ci.sender.nick + ": ");
            }
            if (!TextUtils.isEmpty(ci.content)) {
                cv.contentText.setText(ci.content);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mComments == null ? 0 : mComments.size();
    }

    public void setComments(List<CommentInfo> comments) {
        mComments = comments;
        notifyDataSetChanged();
    }

}
