package com.thinker.dora.wechatmomentsdemo.control;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.thinker.dora.wechatmomentsdemo.R;
import com.thinker.dora.wechatmomentsdemo.adapter.TweetsItemAdapter;
import com.thinker.dora.wechatmomentsdemo.interfaces.OnResponseListener;
import com.thinker.dora.wechatmomentsdemo.interfaces.OnUserInfoResponse;
import com.thinker.dora.wechatmomentsdemo.model.TweetsItemBean;
import com.thinker.dora.wechatmomentsdemo.model.UserBean;
import com.thinker.dora.wechatmomentsdemo.tools.Constants;
import com.thinker.dora.wechatmomentsdemo.view.XListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnUserInfoResponse,OnResponseListener,XListView.IXListViewListener,AdapterView.OnItemClickListener{

    private ArrayList<TweetsItemBean> list;
    private ArrayList<TweetsItemBean> data;
    private XListView listView;
    private TweetsItemAdapter adapter;
    private ImageView emptyView;

    private UserBean userBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        PostRequest.getUserInfo(this);
        PostRequest.getData(MainActivity.this, this);
    }

    private void initViews() {
        listView = (XListView)findViewById(R.id.listview);
        list = new ArrayList<>();
        data = new ArrayList<>();
        userBean = new UserBean();
        listView.setXListViewListener(this);
        listView.setPullLoadEnable(true);
        adapter = new TweetsItemAdapter(list, this,userBean);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        emptyView = (ImageView)findViewById(R.id.empty_view);
        listView.setEmptyView(emptyView);
    }

    @Override
    public void onRefresh() {
        listView.setPullLoadEnable(true);
        PostRequest.getData(MainActivity.this, this);
    }

    @Override
    public void downPulling() {

    }

    @Override
    public void onLoadMore() {
        if (data != null && data.size() > list.size()) {
            for (int i = list.size(); i < data.size(); i++) {
                list.add(data.get(i));
            }
            adapter.notifyDataSetChanged();
            listView.setPullLoadEnable(false);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onSuccess(ArrayList<TweetsItemBean> data) {
        listView.stopRefresh();
        if (data != null && data.size() > 0) {
            if (list == null) {
                list = new ArrayList<>();
            }else{
                list.clear();
            }
            for (int i = 0; i < data.size() ; i++) {
                this.data.add(data.get(i));
                if (i < Constants.page){
                    list.add(data.get(i));
                }
            }
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSuccess(UserBean bean) {
        if (bean != null) {
            userBean.setAvatar(bean.getAvatar());
            userBean.setNick(bean.getNick());
            userBean.setProfileImage(bean.getProfileImage());
            userBean.setUsername(bean.getUsername());
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onFailed() {

    }
}
