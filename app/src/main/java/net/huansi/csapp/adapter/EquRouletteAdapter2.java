package net.huansi.csapp.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.huansi.csapp.R;
import net.huansi.csapp.activity.HistoryDetailActivity;
import net.huansi.csapp.bean.EquRealTimeDataBean;
import net.huansi.csapp.view.DashboardView1;
import java.util.List;
import static net.huansi.csapp.utils.Constants.CHANNEL_ID;
import static net.huansi.csapp.utils.Constants.CHANNEL_NAME;

/**
 * Created by Administrator on 2017/7/12 0012.
 */

public class EquRouletteAdapter2 extends RecyclerView.Adapter<EquRouletteAdapter2.EquViewHolder> {
    private List<EquRealTimeDataBean> list;
    private Context context;
    private int type;

    public EquRouletteAdapter2(List<EquRealTimeDataBean> list, Context context,int type) {
        this.context = context;
        this.list = list;
        this.type = type;
    }


    @Override
    public EquViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (type){
            case 1:
                view=View.inflate(context,R.layout.item_roulette_adapter,null);
                break;
            case 2:
                view =View.inflate(context,R.layout.item_text_adapter,null);
                break;
            default:
                view =View.inflate(context,R.layout.item_text_adapter,null);
                break;
        }

        return new EquViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EquViewHolder holder, final int position) {
        EquRealTimeDataBean equRealTimeDataBean = list.get(position);
        switch (type){
            case 1:
                holder.dash.mHeaderText=equRealTimeDataBean.SCHANNELNAME;
                String sValue = equRealTimeDataBean.NCHANNELREALTIMEVALUE;
                if(sValue.isEmpty()){
                    sValue="0";
                }
                int value = (int)Double.parseDouble(sValue);

                if(equRealTimeDataBean.NUPLIMITVALUE.isEmpty()&&value>100&&value<=900){
                    holder.dash.mMax=value*2;
                    holder.dash.mMin=0;
                    holder.dash.mPortion=10;
                    holder.dash.mSection=10;
                    holder.dash.mTexts = new String[11];
                    for (int i = 0; i < holder.dash.mTexts.length; i++) {
                        int n = (holder.dash.mMax - holder.dash.mMin) / holder.dash.mSection;
                        holder.dash.mTexts[i] = String.valueOf(holder.dash.mMin + i * n);
                    }
                    holder.dash.postInvalidate();
                }else if(value>900&&value<500000){
                    holder.dash.mMax=value*2;
                    holder.dash.mMin=0;
                    holder.dash.mPortion=4;
                    holder.dash.mSection=4;
                    holder.dash.mTexts = new String[5];
                    for (int i = 0; i < holder.dash.mTexts.length; i++) {
                        int n = (holder.dash.mMax - holder.dash.mMin) / holder.dash.mSection;
                        holder.dash.mTexts[i] = String.valueOf(holder.dash.mMin + i * n);
                    }
                    holder.dash.postInvalidate();
                }else{
                    holder.dash.mMax=100;
                    holder.dash.mMin=0;
                    holder.dash.mPortion=10;
                    holder.dash.mSection=10;
                    holder.dash.mTexts = new String[11];
                    for (int i = 0; i < holder.dash.mTexts.length; i++) {
                        int n = (holder.dash.mMax - holder.dash.mMin) / holder.dash.mSection;
                        holder.dash.mTexts[i] = String.valueOf(holder.dash.mMin + i * n);
                    }
                    holder.dash.postInvalidate();
                }
                holder.dash.setRealTimeValue(value);
                holder.dash.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, HistoryDetailActivity.class);
                        EquRealTimeDataBean equRealTimeDataBean1 = list.get(position);
                        intent.putExtra(CHANNEL_NAME,equRealTimeDataBean1.SCHANNELNAME);
                        intent.putExtra(CHANNEL_ID,equRealTimeDataBean1.IUSERMODULECHANNELID);
                        context.startActivity(intent);
                    }
                });

                break;
            case 2:
                setData(equRealTimeDataBean,holder);
                break;
            default:
                setData(equRealTimeDataBean,holder);
                break;
        }


    }

    private void setData(EquRealTimeDataBean equRealTimeDataBean, EquViewHolder holder) {

        String sValue = equRealTimeDataBean.NCHANNELREALTIMEVALUE;
        if(sValue.isEmpty()){
            sValue="0";
        }
        double content = Double.parseDouble(sValue);
        holder.title.setText(equRealTimeDataBean.SCHANNELNAME+"："+content+equRealTimeDataBean.SUNITNAME);
//        holder.content.setText();
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    class EquViewHolder extends RecyclerView.ViewHolder{
        private DashboardView1 dash;
        private TextView title;
        private TextView content;
        public EquViewHolder(View itemView) {
            super(itemView);
            switch (type){
                case 1:
                    dash  = (DashboardView1) itemView.findViewById(R.id.dashBoard);
                    break;
                case 2:
                    title = (TextView) itemView.findViewById(R.id.title);
//                    content = (TextView) itemView.findViewById(R.id.content);
                    break;
                default:

                    break;
            }

        }
    }
}
