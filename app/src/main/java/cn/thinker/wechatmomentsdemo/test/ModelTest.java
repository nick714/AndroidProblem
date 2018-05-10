package cn.thinker.wechatmomentsdemo.test;

import android.os.Bundle;
import android.os.Message;
import android.test.InstrumentationTestCase;
import android.util.Log;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import cn.thinker.wechatmomentsdemo.common.ParcelablePoolObject;
import cn.thinker.wechatmomentsdemo.controller.IViewController;
import cn.thinker.wechatmomentsdemo.model.IDataOperation;
import cn.thinker.wechatmomentsdemo.model.Model;
import cn.thinker.wechatmomentsdemo.model.data.ImageItem;
import cn.thinker.wechatmomentsdemo.model.data.TweetInfo;
import cn.thinker.wechatmomentsdemo.model.data.UserInfo;
import cn.thinker.wechatmomentsdemo.model.image.BitmapHolder;

public class ModelTest extends InstrumentationTestCase {

    private static final String TEST_IMAGE_URL = "http://i.ytimg.com/vi/rGWI7mjmnNk/hqdefault.jpg";

    private CountDownLatch signal;

    public static class MockViewController implements IViewController {

        private CountDownLatch internalSignal;

        public MockViewController(CountDownLatch signal) {
            internalSignal = signal;
        }
        @Override
        public boolean handleMessage(Message msg) {

            switch (msg.what) {
                case IDataOperation.REQUEST_TYPE_GET_USER_INFO: {
                    ParcelablePoolObject ppo = (ParcelablePoolObject) msg.obj;
                    Bundle result = ppo.getData();
                    if (msg.arg1 == IDataOperation.REQUEST_RESULT_SUCCESS) {
                        UserInfo user = result.getParcelable(IDataOperation.BUNDLE_KEY_RESULT_USER);
                        assertNotNull(user);
                        Log.v("ModelTest", "testRetriveUserInfo: " + user.toString());
                    }
                    Model.peekInstance().freePoolObject(ppo);
                    internalSignal.countDown();
                    break;
                }
                case IDataOperation.REQUEST_TYPE_GET_TWEETS_INFO: {
                    ParcelablePoolObject ppo = (ParcelablePoolObject) msg.obj;
                    Bundle result = ppo.getData();
                    if (msg.arg1 == IDataOperation.REQUEST_RESULT_SUCCESS) {
                        List<TweetInfo> tweets = result.getParcelableArrayList(IDataOperation.BUNDLE_KEY_RESULT_TWEETS);
                        assertNotNull(tweets);
                        dumpTweets(tweets);
                    }
                    Model.peekInstance().freePoolObject(ppo);
                    internalSignal.countDown();
                    break;
                }
                case IDataOperation.REQUEST_TYPE_DECODE_IMAGE_BITMAP:
                case IDataOperation.REQUEST_TYPE_GET_IMAGE_BITMAP: {
                    BitmapHolder holder = ((BitmapHolder) msg.obj);
                    if (msg.arg1 == IDataOperation.REQUEST_RESULT_SUCCESS) {
                        assertNotNull(holder);
                        assertNotNull(holder.getBitmap());
                        assertEquals(holder.getBitmap().getWidth(), 80);
                        assertEquals(holder.getBitmap().getHeight(), 80);
                    }
                    internalSignal.countDown();
                    Model.peekInstance().freePoolObject(holder.getParam());
                    break;
                }
            }
            return false;
        }

        private void dumpTweets(List<TweetInfo> tweets) {
            Log.v("ModelTest", "testRetriveTweets: \n");
            for (TweetInfo tweet: tweets) {
                Log.v("ModelTest", tweet.toString());
            }
        }
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Model.peekInstance();
    }

    public void testRetriveUserInfo() throws InterruptedException {
        signal = new CountDownLatch(1);
        ParcelablePoolObject ppo = Model.peekInstance().peekPoolObject();
        Message msg = Message.obtain(null, IDataOperation.REQUEST_TYPE_GET_USER_INFO, ppo);
        Model.peekInstance().requestDataOperation(new MockViewController(signal), msg);
        signal.await();
    }

    public void testRetriveTweets() throws InterruptedException {
        signal = new CountDownLatch(1);
        ParcelablePoolObject ppo = Model.peekInstance().peekPoolObject();
        Message msg = Message.obtain(null, IDataOperation.REQUEST_TYPE_GET_TWEETS_INFO, ppo);
        Model.peekInstance().requestDataOperation(new MockViewController(signal), msg);
        signal.await();
    }

    public void testRetriveImageWithDesiredSize() throws InterruptedException {
        signal = new CountDownLatch(1);
        ImageItem item = new ImageItem(80, 80, TEST_IMAGE_URL);
        ParcelablePoolObject poolObj = Model.peekInstance().peekPoolObject();
        Bundle param = poolObj.getData();
        param.putParcelable(IDataOperation.BUNDLE_KEY_PARAM_IMAGE_REQUEST_ITEM, item);
        Message msg = Message.obtain(null, IDataOperation.REQUEST_TYPE_GET_IMAGE_BITMAP, poolObj);
        msg.setData(param);
        Model.peekInstance().requestDataOperation(new MockViewController(signal), msg);
    }

    public void testDocodeImageWithDesiredSize() throws InterruptedException {
        signal = new CountDownLatch(1);
        ImageItem item = new ImageItem(80, 80, TEST_IMAGE_URL);
        ParcelablePoolObject poolObj = Model.peekInstance().peekPoolObject();
        Bundle param = poolObj.getData();
        param.putParcelable(IDataOperation.BUNDLE_KEY_PARAM_IMAGE_REQUEST_ITEM, item);
        Message msg = Message.obtain(null, IDataOperation.REQUEST_TYPE_DECODE_IMAGE_BITMAP, poolObj);
        msg.setData(param);
        Model.peekInstance().requestDataOperation(new MockViewController(signal), msg);
    }
 }
