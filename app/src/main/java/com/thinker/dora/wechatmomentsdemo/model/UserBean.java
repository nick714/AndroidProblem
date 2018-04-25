package com.thinker.dora.wechatmomentsdemo.model;

import com.thinker.dora.wechatmomentsdemo.tools.MyToolbox;

public class UserBean {
    private String profileImage;
    private String avatar;
    private String nick;
    private String username;

    public String getProfileImage() {
        return MyToolbox.paramIsNull(profileImage);
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getAvatar() {
        return MyToolbox.paramIsNull(avatar);
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNick() {
        return MyToolbox.paramIsNull(nick);
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getUsername() {
        return MyToolbox.paramIsNull(username);
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
