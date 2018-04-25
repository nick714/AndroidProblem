package com.thinker.dora.wechatmomentsdemo.model;

import com.thinker.dora.wechatmomentsdemo.tools.MyToolbox;


public class TweetsItemBean {
    private String content;
    private ImagesBean[] images;
    private UserBean sender;
    private CommentBean[] comments;


    public String getContent() {
        return MyToolbox.paramIsNull(content);
    }

    public void setContent(String content) {
        this.content = content;
    }

    public CommentBean[] getComments() {
        return comments;
    }

    public void setComments(CommentBean[] comments) {
        this.comments = comments;
    }

    public UserBean getSender() {
        return sender;
    }

    public void setSender(UserBean sender) {
        this.sender = sender;
    }

    public ImagesBean[] getImages() {
        return images;
    }

    public void setImages(ImagesBean[] images) {
        this.images = images;
    }
}
