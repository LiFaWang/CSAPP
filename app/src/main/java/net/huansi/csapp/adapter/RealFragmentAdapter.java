package net.huansi.csapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.huansi.csapp.R;
import net.huansi.csapp.activity.EquRouletteActivity;
import net.huansi.csapp.bean.RealTimeMonitoringBean;

import java.util.List;

import huansi.net.qianjingapp.adapter.HsBaseAdapter;
import huansi.net.qianjingapp.utils.ViewHolder;

import static net.huansi.csapp.utils.Constants.FACTORY_NAME;
import static net.huansi.csapp.utils.Constants.ITEM_EQU_ID;
import static net.huansi.csapp.utils.Constants.ITEM_EQU_NAME;

/**
 * Created by WB on 2017/7/5 0005.
 */

public class RealFragmentAdapter extends HsBaseAdapter<RealTimeMonitoringBean>{

    public RealFragmentAdapter(List<RealTimeMonitoringBean> list, Context context) {
        super(list, context);
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if (view==null){
            view=mInflater.inflate(R.layout.item_home_adapter,viewGroup,false);
        }
        TextView factory = ViewHolder.get(view, R.id.listFactory);
        TextView area = ViewHolder.get(view, R.id.listArea);
        TextView dvNum = ViewHolder.get(view, R.id.listDVNum);
        TextView state = ViewHolder.get(view, R.id.listState);
        factory.setText(mList.get(i).STERMINALNAME);
        area.setText(mList.get(i).NCHANNELNUMBER);
        dvNum.setText(mList.get(i).NEXPNUMBER);
        state.setText(mList.get(i).SSTATUS);
        factory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, EquRouletteActivity.class);
                intent.putExtra(ITEM_EQU_ID,mList.get(i).ITERMINALID);
                intent.putExtra(ITEM_EQU_NAME,mList.get(i).STERMINALNAME);
                intent.putExtra(FACTORY_NAME,mList.get(i).SFACTORYNAME);
                mContext.startActivity(intent);
            }
        });
        return view;
    }
}
