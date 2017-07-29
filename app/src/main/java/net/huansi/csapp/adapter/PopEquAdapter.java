package net.huansi.csapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.huansi.csapp.R;
import net.huansi.csapp.bean.EquipmentListBean;

import java.util.List;

import huansi.net.qianjingapp.adapter.HsBaseAdapter;
import huansi.net.qianjingapp.utils.ViewHolder;

/**
 * Created by Administrator on 2017/7/11 0011.
 */

public class PopEquAdapter extends HsBaseAdapter<EquipmentListBean>{
    public PopEquAdapter(List<EquipmentListBean> list, Context context) {
        super(list, context);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null){
            view = mInflater.inflate(R.layout.item_pop_list,viewGroup,false);
        }
        TextView tv  = ViewHolder.get(view,R.id.text);
        tv.setText(mList.get(i).STERMINALNAME);
        return view;
    }
}
