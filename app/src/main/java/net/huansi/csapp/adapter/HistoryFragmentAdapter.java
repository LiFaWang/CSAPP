package net.huansi.csapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import net.huansi.csapp.R;
import net.huansi.csapp.activity.HistoryDetailActivity;
import net.huansi.csapp.bean.HistoryDataMapBean;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import huansi.net.qianjingapp.adapter.HsBaseAdapter;
import huansi.net.qianjingapp.utils.ViewHolder;

import static net.huansi.csapp.utils.Constants.CHANNEL_ID;
import static net.huansi.csapp.utils.Constants.CHANNEL_NAME;


/**
 * Created by Administrator on 2017/7/7 0007.
 */

public class HistoryFragmentAdapter extends HsBaseAdapter<Map<String,List<HistoryDataMapBean>>> {

    private String channelName = "";
    public HistoryFragmentAdapter(List<Map<String, List<HistoryDataMapBean>>> list, Context context) {
        super(list, context);
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        if(view==null){
            view=mInflater.inflate(R.layout.item_history_adapter,viewGroup,false);
        }
        LineChart chart= ViewHolder.get(view,R.id.lineChart);
        chart.setNoDataText("没有数据！");
//        if(!mList.get(i).isEmpty()||1==1){
            ArrayList<String> xValues = new ArrayList<>();
            ArrayList<Entry> yValues = new ArrayList<>();
            ArrayList<LineDataSet> lineDataSets = new ArrayList<>();
            xValues.clear();
            yValues.clear();
            lineDataSets.clear();
            chart.setDescription("");
            chart.setPinchZoom(false);
            chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
            chart.getAxisRight().setEnabled(false);
            chart.getAxisLeft().setValueFormatter(new YAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, YAxis yAxis) {
                    DecimalFormat df = new DecimalFormat("0.0");

                    return df.format(value)+"";
                }
            });

            Map<String, List<HistoryDataMapBean>> map = mList.get(i);

            final List<HistoryDataMapBean> historyDataMapBeen = new ArrayList<>();
            Set<String> strings = map.keySet();
            for (String string : strings) {
                historyDataMapBeen.addAll(map.get(string));
                channelName = string;
            }

            if(mList.get(i).isEmpty()){
                for(int k =0;k<10;k++){
                    xValues.add("");
                    yValues.add(new Entry(0,k));
                }
            }else{
                for (int j = 0; j < historyDataMapBeen.size(); j++) {
                    xValues.add("");
                    String value = historyDataMapBeen.get(j).NCHANNELVALUE;
                    float v;
                    if(value.isEmpty()||value==null){
                        v=0;
                    }else{
                        v= Float.parseFloat(value);
                    }
                    yValues.add(new Entry(v,j));
                }
            }
            LineDataSet lineDataSet = new LineDataSet(yValues, channelName);
            lineDataSet.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    return "";
                }
            });
            lineDataSets.add(lineDataSet); // add the datasets
            LineData lineData = new LineData(xValues, lineDataSets);
            chart.setData(lineData);
            chart.setVisibleXRangeMaximum(6);
            chart.animateX(2000);
            lineData.notifyDataChanged();
            chart.invalidate();
//        }

        chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, List<HistoryDataMapBean>> stringListMap = mList.get(i);
                Set<String> strings1 = stringListMap.keySet();
                String name ="";
                String id ="";
                for (String s : strings1) {
                    name= s;
                    List<HistoryDataMapBean> historyDataMapBeen1 = stringListMap.get(s);
                    historyDataMapBeen1.size();
                    id=historyDataMapBeen1.get(0).IUSERMODULECHANNELID;
                }
                Intent intent = new Intent(mContext, HistoryDetailActivity.class);
                intent.putExtra(CHANNEL_NAME,name);
                intent.putExtra(CHANNEL_ID,id);
                mContext.startActivity(intent);
            }
        });

        return view;
    }
}
