package cn.thinker.wechatmomentsdemo.common;

public final class Constants {
    public static final String DEFAULT_ENCODING = "UTF-8";

    public static final int CACHE_IMAGES_TYPE = 0;

    public static final String CACHE_IMAGES_DIR = "images";

    public static final String JSON_KEY_USER_PROFILE_NAME = "profile-image";
    public static final String JSON_KEY_USER_AVATAR = "avatar";
    public static final String JSON_KEY_USER_NICK = "nick";
    public static final String JSON_KEY_USER_USERNAME = "username";

    public static final String JSON_KEY_TWEET_ERROR = "error";
    public static final String JSON_KEY_TWEET_SENDER = "sender";
    public static final String JSON_KEY_TWEET_CONTENT = "content";
    public static final String JSON_KEY_TWEET_IMAGES = "images";
    public static final String JSON_KEY_TWEET_IMAGE_URL = "url";
    public static final String JSON_KEY_TWEET_COMMENTS = "comments";

    public static final String BASE_URL = "http://thoughtworks-ios.herokuapp.com/";

    public static final String USER_URL = BASE_URL + "user/jsmith";
    public static final String TWEETS_URL = BASE_URL + "user/jsmith/tweets";

    public static final int TWEET_SENDER_AVATAR_HEIGHT_INDEX = 0;
    public static final int TWEET_SENDER_AVATAR_WIDTH_INDEX = 1;
    public static final int TWEET_SENDER_SINGLE_IMAGE_HEIGHT_INDEX = 2;
    public static final int TWEET_SENDER_SINGLE_IMAGE_WIDTH_INDEX = 3;
    public static final int TWEET_SENDER_MORE_IMAGE_HEIGHT_INDEX = 4;
    public static final int TWEET_SENDER_MORE_IMAGE_WIDTH_INDEX = 5;
    public static final int USER_PROFILE_IMAGE_HEIGHT_INDEX= 5;
    public static final int USER_PROFILE_IMAGE_WIDTH_INDEX = 6;
    public static final int USER_AVATAR_IMAGE_HEIGHT_INDEX= 7;
    public static final int USER_AVATAR_IMAGE_WIDTH_INDEX = 8;
}
