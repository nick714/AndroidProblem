package cn.thinker.wechatmomentsdemo;

import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.List;

import cn.thinker.wechatmomentsdemo.common.Constants;
import cn.thinker.wechatmomentsdemo.common.ParcelablePoolObject;
import cn.thinker.wechatmomentsdemo.controller.IViewController;
import cn.thinker.wechatmomentsdemo.model.IDataOperation;
import cn.thinker.wechatmomentsdemo.model.Model;
import cn.thinker.wechatmomentsdemo.model.data.TweetInfo;
import cn.thinker.wechatmomentsdemo.model.data.UserInfo;
import cn.thinker.wechatmomentsdemo.widget.IView;
import cn.thinker.wechatmomentsdemo.widget.TweetsAdapter;


public class MainActivity extends AppCompatActivity implements IView, IViewController {

    private static final int PULL_TO_REFRESH_INCREAMENT = 5;

    private SwipyRefreshLayout mPullToRefreshLayout;
    private TweetsAdapter mAdapter;

    private List<TweetInfo> mTweets;

    private int mPullToRefreshStart = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        initActionBar();
        initView();
        setStatusBarColor(findViewById(R.id.statusBarBackground), getResources().getColor(R.color.main_actionbar_color));
        refreshUserProfile();
    }

    private void initView() {
        final Resources res = getResources();
        int[] imageSize = new int[Constants.USER_AVATAR_IMAGE_WIDTH_INDEX + 1];
        imageSize[Constants.TWEET_SENDER_AVATAR_HEIGHT_INDEX] = res.getDimensionPixelSize(R.dimen.tweet_sender_avatar_image_height);
        imageSize[Constants.TWEET_SENDER_AVATAR_WIDTH_INDEX] = res.getDimensionPixelSize(R.dimen.tweet_sender_avatar_image_height);
        imageSize[Constants.TWEET_SENDER_SINGLE_IMAGE_HEIGHT_INDEX] = res.getDimensionPixelSize(R.dimen.tweet_single_image_fixed_height);
        imageSize[Constants.TWEET_SENDER_SINGLE_IMAGE_WIDTH_INDEX] = res.getDimensionPixelSize(R.dimen.tweet_single_image_fixed_width);
        imageSize[Constants.TWEET_SENDER_MORE_IMAGE_HEIGHT_INDEX] = res.getDimensionPixelSize(R.dimen.tweet_more_image_height);
        imageSize[Constants.TWEET_SENDER_MORE_IMAGE_WIDTH_INDEX] = res.getDimensionPixelSize(R.dimen.tweet_more_image_width);
        imageSize[Constants.USER_PROFILE_IMAGE_HEIGHT_INDEX] = res.getDimensionPixelSize(R.dimen.user_profile_image_height);
        imageSize[Constants.USER_PROFILE_IMAGE_WIDTH_INDEX] = res.getDimensionPixelSize(R.dimen.user_profile_image_width);
        imageSize[Constants.USER_AVATAR_IMAGE_HEIGHT_INDEX] = res.getDimensionPixelSize(R.dimen.user_avatar_image_height);
        imageSize[Constants.USER_AVATAR_IMAGE_WIDTH_INDEX] = res.getDimensionPixelSize(R.dimen.user_avatar_image_width);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.moments_list);
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(llm);
        mAdapter = new TweetsAdapter(llm, this, imageSize);
        recyclerView.setAdapter(mAdapter);

        mPullToRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.pull_to_refresh_layout);
        mPullToRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection swipyRefreshLayoutDirection) {
                if (SwipyRefreshLayoutDirection.TOP.equals(swipyRefreshLayoutDirection)) {
                    mPullToRefreshStart = 0;
                    refreshContent();
                } else if (SwipyRefreshLayoutDirection.BOTTOM.equals(swipyRefreshLayoutDirection)) {
                    loadMore(true);
                }
            }
        });
        mPullToRefreshLayout.setRefreshing(true);
        refreshContent();
    }

    private void loadMore(boolean isAppend) {
        if (mTweets == null || mTweets.size() <= mPullToRefreshStart) {
            mPullToRefreshLayout.setRefreshing(false);
            return;
        }
        int desiredSize = mPullToRefreshStart + PULL_TO_REFRESH_INCREAMENT;
        int increament = desiredSize >= mTweets.size() ? mTweets.size() - mPullToRefreshStart
                : PULL_TO_REFRESH_INCREAMENT;
        if (isAppend) {
            mAdapter.appendTweets(mTweets.subList(mPullToRefreshStart, mPullToRefreshStart + increament));
        } else {
            mAdapter.setTweets(mTweets.subList(mPullToRefreshStart, mPullToRefreshStart + increament));
        }
        mPullToRefreshStart += increament;
        mPullToRefreshLayout.setRefreshing(false);
    }

    private void refreshContent() {
        final Model model = Model.peekInstance();
        ParcelablePoolObject ppo = model.peekPoolObject();
        Message msg = Message.obtain(null, IDataOperation.REQUEST_TYPE_GET_TWEETS_INFO, ppo);
        model.requestDataOperation(this, msg);
    }

    private void refreshUserProfile() {
        final Model model = Model.peekInstance();
        ParcelablePoolObject ppo = model.peekPoolObject();
        Message msg = Message.obtain(null, IDataOperation.REQUEST_TYPE_GET_USER_INFO, ppo);
        model.requestDataOperation(this, msg);
    }

    private void initActionBar() {
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setHomeButtonEnabled(true);
            ab.setDisplayShowTitleEnabled(true);
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        ParcelablePoolObject ppo = (ParcelablePoolObject) msg.obj;
        Bundle result = ppo.getData();
        switch (msg.what) {
            case IDataOperation.REQUEST_TYPE_GET_USER_INFO:
                if (msg.arg1 == IDataOperation.REQUEST_RESULT_SUCCESS) {
                    UserInfo user = result.getParcelable(IDataOperation.BUNDLE_KEY_RESULT_USER);
                    mAdapter.updateProfile(user);
                }
                break;
            case IDataOperation.REQUEST_TYPE_GET_TWEETS_INFO:
                mPullToRefreshLayout.setRefreshing(false);
                if (msg.arg1 == IDataOperation.REQUEST_RESULT_SUCCESS) {
                    mTweets = result.getParcelableArrayList(IDataOperation.BUNDLE_KEY_RESULT_TWEETS);
                    loadMore(false);
                }
                break;
        }
        Model.peekInstance().freePoolObject(ppo);
        return false;
    }

    private void setStatusBarColor(View statusBar, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // action bar height
            statusBar.getLayoutParams().height = getStatusBarHeight();
            statusBar.setBackgroundColor(color);
        } else {
            findViewById(R.id.statusBarBackground).setVisibility(View.GONE);
        }
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
