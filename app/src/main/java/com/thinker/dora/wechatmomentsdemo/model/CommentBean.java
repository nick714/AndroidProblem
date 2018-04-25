package com.thinker.dora.wechatmomentsdemo.model;

import com.thinker.dora.wechatmomentsdemo.tools.MyToolbox;


public class CommentBean {
    private String content;
    private UserBean sender;

    public String getContent() {
        return MyToolbox.paramIsNull(content);
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UserBean getSender() {
        return sender;
    }

    public void setSender(UserBean sender) {
        this.sender = sender;
    }
}
