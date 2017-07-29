package net.huansi.csapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import net.huansi.csapp.R;
import net.huansi.csapp.bean.HomeInfoBean;
import net.huansi.csapp.event.HomeToRealEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import huansi.net.qianjingapp.adapter.HsBaseAdapter;
import huansi.net.qianjingapp.utils.ViewHolder;

/**
 * Created by WB on 2017/7/5 0005.
 */

public class HomeFragmentAdapter extends HsBaseAdapter<HomeInfoBean>{

    public HomeFragmentAdapter(List<HomeInfoBean> list, Context context) {
        super(list, context);
    }

    @Override
    public View getView(int i, View view, final ViewGroup viewGroup) {


        if (view==null){
            view=mInflater.inflate(R.layout.item_home_adapter,viewGroup,false);
        }
        TextView factory = ViewHolder.get(view, R.id.listFactory);
        TextView area = ViewHolder.get(view, R.id.listArea);
        TextView dvNum = ViewHolder.get(view, R.id.listDVNum);
        TextView state = ViewHolder.get(view, R.id.listState);
        final HomeInfoBean home = mList.get(i);
        factory.setText(home.SFACTORYNAME);
        area.setText(home.SCOUNTRYNAME);
        dvNum.setText(home.NEQUICOUNT);
        state.setText(home.SSTATUS);
        factory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeToRealEvent event=null;
                if(event==null){
                    event  = new HomeToRealEvent();
                }
                event.setArea(home.SCOUNTRYNAME);
                event.setFactory(home.SFACTORYNAME);
                event.setCountryID(home.ICOUNTRYID);
                event.setFactoryID(home.IFACTORYID);
                EventBus.getDefault().post(event);
            }
        });
        return view;
    }
}
