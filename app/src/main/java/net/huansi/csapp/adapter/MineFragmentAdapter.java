package net.huansi.csapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.huansi.csapp.R;
import net.huansi.csapp.bean.MineBean;

import java.util.List;

import huansi.net.qianjingapp.adapter.HsBaseAdapter;
import huansi.net.qianjingapp.utils.ViewHolder;

/**
 * Created by Administrator on 2017/7/13 0013.
 */

public class MineFragmentAdapter extends HsBaseAdapter<MineBean> {


    public MineFragmentAdapter(List<MineBean> list, Context context) {
        super(list, context);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null){
            view=mInflater.inflate(R.layout.item_mine_adapter,viewGroup,false);
        }
        TextView title = ViewHolder.get(view,R.id.tvTitle);
        ImageView iv = ViewHolder.get(view,R.id.ivIcon);
        TextView content = ViewHolder.get(view,R.id.tvContent);
        MineBean mineBean = mList.get(i);
        title.setText(mineBean.name);
        iv.setImageResource(mineBean.imageId);
        if(mineBean.name.equals("注销")){
            title.setTextColor(Color.parseColor("#1296db"));
        }
//        else{
//            title.setTextColor(Color.parseColor("#000000"));
//        }
        content.setText(mineBean.data);
        return view;
    }
}
