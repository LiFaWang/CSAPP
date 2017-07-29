package net.huansi.csapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.huansi.csapp.R;
import net.huansi.csapp.bean.FactoryListBean;

import java.util.List;

import huansi.net.qianjingapp.adapter.HsBaseAdapter;
import huansi.net.qianjingapp.utils.ViewHolder;

/**
 * Created by Administrator on 2017/7/10 0010.
 */

public class PopFactoryAdapter extends HsBaseAdapter<FactoryListBean>{
    public PopFactoryAdapter(List<FactoryListBean> list, Context context) {
        super(list, context);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null){
            view = mInflater.inflate(R.layout.item_pop_list,viewGroup,false);
        }
        TextView tv  = ViewHolder.get(view,R.id.text);
        tv.setText(mList.get(i).SFACTORYNAME);
        return view;
    }
}
