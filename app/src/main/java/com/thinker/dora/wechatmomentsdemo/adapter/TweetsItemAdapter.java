package com.thinker.dora.wechatmomentsdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.thinker.dora.wechatmomentsdemo.R;
import com.thinker.dora.wechatmomentsdemo.model.ImagesBean;
import com.thinker.dora.wechatmomentsdemo.model.TweetsItemBean;
import com.thinker.dora.wechatmomentsdemo.model.UserBean;
import com.thinker.dora.wechatmomentsdemo.tools.NetLoad;
import com.thinker.dora.wechatmomentsdemo.view.MyTextView;

import java.util.ArrayList;


public class TweetsItemAdapter extends BaseAdapter{

    private ArrayList<TweetsItemBean> list;
    private UserBean user;
    private Context mContext;
    
    private GridviewAdapter adapter;
    private ArrayList<ImagesBean> imgList;

    public TweetsItemAdapter(ArrayList<TweetsItemBean> list, Context mContext,UserBean userbean) {
        this.list = list;
        user = userbean;
        this.mContext = mContext;
        imgList = new ArrayList<>();
        adapter = new GridviewAdapter(mContext,imgList);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TweetsItemBean bean = list.get(i);
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            view = layoutInflater.inflate(R.layout.list_item, null, false);
            viewHolder.top = (RelativeLayout)view.findViewById(R.id.top);
            viewHolder.profile = (ImageView)view.findViewById(R.id.top_img);
            viewHolder.myAvatar = (ImageView)view.findViewById(R.id.avatar);
            viewHolder.avatar = (ImageView) view.findViewById(R.id.sender_avatar);
            viewHolder.myName = (MyTextView) view.findViewById(R.id.myName);
            viewHolder.username = (MyTextView) view.findViewById(R.id.username);
            viewHolder.content = (MyTextView) view.findViewById(R.id.content);
            viewHolder.imagesLay = (RelativeLayout)view.findViewById(R.id.image_layout);
            viewHolder.bigImgview = (ImageView)viewHolder.imagesLay.findViewById(R.id.big_img);
            viewHolder.gridview = (GridView)viewHolder.imagesLay.findViewById(R.id.gridview);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        if (i == 0) {
            viewHolder.top.setVisibility(View.VISIBLE);
            viewHolder.myName.setText(user.getNick());
            NetLoad.loadImage(viewHolder.profile,user.getProfileImage(), R.drawable.ic_launcher, R.drawable.ic_launcher);
            NetLoad.loadImage(viewHolder.myAvatar,user.getAvatar(), R.drawable.ic_launcher, R.drawable.ic_launcher);
        }else{
            viewHolder.top.setVisibility(View.GONE);
        }
        //set avatar
        NetLoad.loadImage(viewHolder.avatar, bean.getSender().getAvatar(), R.drawable.ic_launcher, R.drawable.ic_launcher);
        viewHolder.username.setText(bean.getSender().getNick());
        //verify content
        if (bean.getContent() != null && !bean.getContent().equals("")) {
            viewHolder.content.setVisibility(View.VISIBLE);
            viewHolder.content.setText(bean.getContent());
        }else{
            viewHolder.content.setVisibility(View.GONE);
        }
        //verify images
        if (bean.getImages() != null && bean.getImages().length > 0) {
            viewHolder.imagesLay.setVisibility(View.VISIBLE);
            if (bean.getImages().length == 1) {
                viewHolder.bigImgview.setVisibility(View.VISIBLE);
                viewHolder.gridview.setVisibility(View.GONE);
                NetLoad.loadImage(viewHolder.bigImgview, bean.getImages()[0].getUrl(), R.drawable.ic_launcher, R.drawable.ic_launcher);

            }else{
                viewHolder.bigImgview.setVisibility(View.GONE);
                viewHolder.gridview.setVisibility(View.VISIBLE);
                for (int j = 0; j < bean.getImages().length; j++) {
                    imgList.add(bean.getImages()[j]);
                }
                viewHolder.gridview.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        }else{
            viewHolder.imagesLay.setVisibility(View.GONE);
        }
        //verify comment（fragment）
        return view;
    }

    public class ViewHolder{
        private RelativeLayout top;
        private ImageView profile,myAvatar;
        private MyTextView myName;
        private ImageView avatar;
        private MyTextView username,content;
        private RelativeLayout imagesLay;
        private ImageView bigImgview;
        private GridView gridview;
    }

}
