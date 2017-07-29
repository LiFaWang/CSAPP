package net.huansi.csapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import net.huansi.csapp.bean.HistoryDataMapBean;

import java.util.List;
import java.util.Map;

/**
 * Created by shanz on 2017/4/6.
 */

public abstract class HsBaseAdapter2<T> extends BaseAdapter{
    protected List<T> mList;
    protected LayoutInflater mInflater;
    protected Context mContext;
    protected Map<String,List<HistoryDataMapBean>> mMap;

    public HsBaseAdapter2(List<T> list, Context context, Map<String,List<HistoryDataMapBean>> map) {
        this.mList = list;
        this.mContext = context;
        mInflater=LayoutInflater.from(context);
        this.mMap = map;
    }


    @Override
    public int getCount() {
        return mList==null?0:mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
