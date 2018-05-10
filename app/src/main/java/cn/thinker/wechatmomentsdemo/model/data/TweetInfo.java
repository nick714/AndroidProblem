package cn.thinker.wechatmomentsdemo.model.data;

import android.os.Parcel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import cn.thinker.wechatmomentsdemo.common.Constants;

public class TweetInfo extends BaseInfo {

    public List<String> images;
    public UserInfo sender;
    public String content;
    public List<CommentInfo> comments;

    private boolean mErrorOccurs;

    @Override
    protected void readFromParcel(Parcel source) {
        images = source.createStringArrayList();
        sender = source.readParcelable(UserInfo.class.getClassLoader());
        content = source.readString();
        comments = source.createTypedArrayList(CommentInfo.CREATOR);
    }

    @Override
    public void fromJSONData(JSONObject obj) throws JSONException {
        if (obj.has(Constants.JSON_KEY_TWEET_ERROR)) {
            setErrorOccurs(true);
            return;
        }
        if (obj.has(Constants.JSON_KEY_TWEET_SENDER)) {
            sender = new UserInfo();
            sender.fromJSONData(obj.getJSONObject(Constants.JSON_KEY_TWEET_SENDER));
        }
        if (obj.has(Constants.JSON_KEY_TWEET_CONTENT)) {
            content = obj.getString(Constants.JSON_KEY_TWEET_CONTENT);
        }
        if (obj.has(Constants.JSON_KEY_TWEET_IMAGES)) {
            JSONArray imageJsonArray = obj.getJSONArray(Constants.JSON_KEY_TWEET_IMAGES);
            int count = imageJsonArray == null ? 0 : imageJsonArray.length();
            images = new ArrayList<>(count);
            for (int i = 0; i < count; i++) {
                images.add(imageJsonArray.getJSONObject(i).getString(Constants.JSON_KEY_TWEET_IMAGE_URL));
            }
        }
        if (obj.has(Constants.JSON_KEY_TWEET_COMMENTS)) {
            JSONArray commentJsonArray = obj.getJSONArray(Constants.JSON_KEY_TWEET_COMMENTS);
            int count = commentJsonArray == null ? 0 : commentJsonArray.length();
            comments = new ArrayList<>(count);
            for (int i = 0; i < count; i++) {
                CommentInfo comment = new CommentInfo();
                comment.fromJSONData(commentJsonArray.getJSONObject(i));
                comments.add(comment);
            }
        }
    }

    @Override
    public ByteBuffer toByteBuffer(ByteBuffer buffer) {
        return null;
    }

    @Override
    public void fromByteBuffer(ByteBuffer buffer) {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(images);
        dest.writeParcelable(sender, flags);
        dest.writeString(content);
    }

    @Override
    public String toString() {
        String str = "[TweetInfo]: ";
        str += ("images: ");
        str += (images == null ? "[unspecified]" : dumpImages());
        str += (" sender: ");
        str += (sender == null ? "[unspecified]" : sender.toString());
        str += (" content: ");
        str += (content == null ? "[unspecified]" : content);
        str += (" comments: ");
        str += (comments == null ? "[unspecified]" : dumpComments());
        return str;
    }

    private String dumpComments() {
        StringBuilder sb = new StringBuilder();
        for (CommentInfo ci : comments) {
            sb.append(ci.toString()).append("\n");
        }
        return sb.toString();
    }

    private String dumpImages() {
        StringBuilder sb = new StringBuilder();
        for (String imageUrl : images) {
            sb.append(imageUrl).append("\n");
        }
        return sb.toString();
    }

    public static final Creator<TweetInfo> CREATOR = new Creator<TweetInfo>() {

        @Override
        public TweetInfo createFromParcel(Parcel source) {
            TweetInfo info = new TweetInfo();
            info.readFromParcel(source);
            return info;
        }

        @Override
        public TweetInfo[] newArray(int size) {
            return new TweetInfo[size];
        }
    };

    public boolean isErrorOccurs() {
        return mErrorOccurs;
    }

    public void setErrorOccurs(boolean mErrorOccurs) {
        this.mErrorOccurs = mErrorOccurs;
    }
}
