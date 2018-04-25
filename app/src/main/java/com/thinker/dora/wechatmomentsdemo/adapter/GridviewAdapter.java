package com.thinker.dora.wechatmomentsdemo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.thinker.dora.wechatmomentsdemo.R;
import com.thinker.dora.wechatmomentsdemo.model.ImagesBean;
import com.thinker.dora.wechatmomentsdemo.tools.NetLoad;

import java.util.ArrayList;

public class GridviewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ImagesBean> list;

    public GridviewAdapter(Context context,ArrayList<ImagesBean> data){
        this.context = context;
        this.list = data;
    }
    public int getCount() {
        return list.size();
    }

    public Object getItem(int item) {
        return list.get(item);
    }

    public long getItemId(int id) {
        return id;
    }

    //创建View方法
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(context);
        imageView.setLayoutParams(new GridView.LayoutParams(200,200));//设置ImageView对象布局
        imageView.setAdjustViewBounds(false);//设置边界对齐
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);//设置刻度的类型
        imageView.setPadding(8, 8, 8, 8);//设置间距
        NetLoad.loadImage(imageView, list.get(position).getUrl(), R.drawable.ic_launcher, R.drawable.ic_launcher);
        return imageView;
    }
}