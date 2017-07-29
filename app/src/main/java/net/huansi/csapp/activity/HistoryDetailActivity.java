package net.huansi.csapp.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.bigkoo.pickerview.TimePickerView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import net.huansi.csapp.R;
import net.huansi.csapp.bean.EquipmentListBean;
import net.huansi.csapp.bean.HistoryDetailBean;
import net.huansi.csapp.databinding.ActivityHistoryDetailBinding;
import net.huansi.csapp.utils.MyUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import huansi.net.qianjingapp.base.NotWebBaseActivity;
import huansi.net.qianjingapp.entity.HsWebInfo;
import huansi.net.qianjingapp.entity.WsEntity;
import huansi.net.qianjingapp.imp.SimpleHsWeb;
import huansi.net.qianjingapp.utils.OthersUtil;
import huansi.net.qianjingapp.utils.RxjavaWebUtils;
import huansi.net.qianjingapp.view.LoadProgressDialog;

import static huansi.net.qianjingapp.utils.WebServices.WebServiceType.HS_SERVICE;
import static net.huansi.csapp.utils.Constants.CHANNEL_ID;
import static net.huansi.csapp.utils.Constants.CHANNEL_NAME;

public class HistoryDetailActivity extends NotWebBaseActivity {
    private String channelName = "";//频道名字
    private String channelId="1";//频道id
    private String mStartDate="1900-1-1";//开始日期
    private String mEndDate="2020-1-1";//结束日期
    private String mMobileNo="1";//手机号码
    private ActivityHistoryDetailBinding detailBinding;
    private TimePickerView pvTime;
    private LineChart lineChart;
    private ArrayList<String> xValues;//x轴数据
    private ArrayList<Entry> yValues;//y轴数据
    private ArrayList<LineDataSet> lineDataSets;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_history_detail;
    }

    @Override
    public void init() {
        channelName = getIntent().getStringExtra(CHANNEL_NAME);
        channelId = getIntent().getStringExtra(CHANNEL_ID);
        detailBinding = (ActivityHistoryDetailBinding) viewDataBinding;
        String curDate = MyUtils.getCurDate("--");
        detailBinding.tvStart.setText(curDate);
        detailBinding.tvEnd.setText(curDate);
        lineChart = detailBinding.lineChart;
        detailBinding.tvChannelName.setText(channelName);
        detailBinding.lineChart.setNoDataText("没有数据");
        initTimePickerView();
        initLineChart();//初始化折线图
        detailBinding.tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        detailBinding.ivStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvTime.show(view);
            }
        });
        detailBinding.ivEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvTime.show(view);
            }
        });
        detailBinding.tvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvTime.show(view);
            }
        });
        detailBinding.tvEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvTime.show(view);
            }
        });
        setData(mStartDate,mEndDate);
        detailBinding.ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(detailBinding.tvStart.getText().toString().isEmpty()||
                       detailBinding.tvEnd.getText().toString().isEmpty()){
                    OthersUtil.ToastMsg(HistoryDetailActivity.this,"请选择日期");
               }else{
                   mStartDate = detailBinding.tvStart.getText().toString();
                   mEndDate = detailBinding.tvEnd.getText().toString();
                   setData(mStartDate,mEndDate);
               }

            }
        });
    }

    private void initLineChart() {
        xValues = new ArrayList<>();
        yValues = new ArrayList<>();
        lineDataSets = new ArrayList<>();
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getXAxis().setTextSize(5f);
        lineChart.getAxisLeft().setTextSize(5f);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.setNoDataText("没有数据");
        lineChart.setDescription(channelName);
    }

    private void initTimePickerView() {
        //时间选择器
        //选中事件回调
        pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                switch (v.getId()){
                    case R.id.ivStart:
                        detailBinding.tvStart.setText(MyUtils.getTime(date));
                        break;
                    case R.id.tvStart:
                        detailBinding.tvStart.setText(MyUtils.getTime(date));
                        break;
                    case R.id.ivEnd:
                        detailBinding.tvEnd.setText(MyUtils.getTime(date));
                        break;
                    case R.id.tvEnd:
                        detailBinding.tvEnd.setText(MyUtils.getTime(date));
                        break;
                }

            }
        }).setType(new boolean[]{true, true, true, false, false, false})
          .build();
    }

    private void setData(String mStartDate,String mEndDate) {
        OthersUtil.showLoadDialog(mDialog);
        //设备数据
        RxjavaWebUtils.requestByGetJsonData(this, HS_SERVICE,
                "spappYunEquTerminalModuleChannelLineData", "sMobileNo="+mMobileNo+",iUserModuleChannelId="
                +channelId+",dStartDate="+mStartDate+",dEndDate="+mEndDate,
                HistoryDetailBean.class.getName(), true, "", new SimpleHsWeb() {
                    @Override
                    public void success(HsWebInfo hsWebInfo) {
                        xValues.clear();
                        yValues.clear();
                        lineDataSets.clear();
                        List<WsEntity> entities = hsWebInfo.wsData.LISTWSDATA;
                        List<HistoryDetailBean> data =  new ArrayList<>();
                         for (int i = 0; i < entities.size(); i++) {
                          HistoryDetailBean historyDetailBean = (HistoryDetailBean) entities.get(i);
                             data.add(historyDetailBean);
                             xValues.add(historyDetailBean.COLLECTTIME);
                             yValues.add(new Entry(Float.valueOf(historyDetailBean.CHANNELVALUE),i));
                        }
                        LineDataSet lineDataSet = new LineDataSet(yValues, channelName);
                        lineDataSets.add(lineDataSet);
                        LineData lineData = new LineData(xValues, lineDataSets);
                        lineChart.setData(lineData);
                        lineChart.setVisibleXRangeMaximum(15);
                        OthersUtil.dismissLoadDialog(mDialog);
                        lineData.setValueTextSize(4f);
                        lineData.notifyDataChanged();
                        lineChart.notifyDataSetChanged();
                        lineChart.invalidate();
                        lineChart.animateXY(3000,2000);
                    }

                    @Override
                    public void error(HsWebInfo hsWebInfo, Context context) {
                        super.error(hsWebInfo, context);
                        detailBinding.lineChart.clear();
                        OthersUtil.dismissLoadDialog(mDialog);

                    }
                });

    }
}
