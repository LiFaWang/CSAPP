package net.huansi.csapp.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import net.huansi.csapp.R;
import net.huansi.csapp.adapter.EquRouletteAdapter;
import net.huansi.csapp.bean.EquRealTimeDataBean;
import net.huansi.csapp.databinding.ActivityEquRouletteBinding;
import net.huansi.csapp.event.AutoEvent;
import net.huansi.csapp.service.AutoRefreshService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import huansi.net.qianjingapp.base.NotWebBaseActivity;
import huansi.net.qianjingapp.entity.HsWebInfo;
import huansi.net.qianjingapp.entity.WsEntity;
import huansi.net.qianjingapp.imp.SimpleHsWeb;
import huansi.net.qianjingapp.utils.OthersUtil;
import huansi.net.qianjingapp.utils.RxjavaWebUtils;
import huansi.net.qianjingapp.view.LoadProgressDialog;

import static huansi.net.qianjingapp.utils.WebServices.WebServiceType.HS_SERVICE;
import static net.huansi.csapp.utils.Constants.FACTORY_NAME;
import static net.huansi.csapp.utils.Constants.ITEM_EQU_ID;
import static net.huansi.csapp.utils.Constants.ITEM_EQU_NAME;

public class EquRouletteActivity extends NotWebBaseActivity {

    private ActivityEquRouletteBinding activityEquRouletteBinding;
    private List<EquRealTimeDataBean> data;//仪表盘数据
    private EquRouletteAdapter adapter;
    private String iTerminalId = "0";
    private String name;//设备名字
    private String factoryName;//工厂名字
    private LoadProgressDialog dialog;
    private int type=2;//文本2和仪表盘1显示
    private int k=0;//文本和仪盘表切换
    @Override
    protected int getLayoutId() {
        return R.layout.activity_equ_roulette;
    }

    @Override
    public void init() {
        Intent intent = new Intent(this, AutoRefreshService.class);
        startService(intent);
        EventBus.getDefault().register(this);
        data = new ArrayList<>();
        dialog = new LoadProgressDialog(this);
        iTerminalId = getIntent().getStringExtra(ITEM_EQU_ID);
        name = getIntent().getStringExtra(ITEM_EQU_NAME);
        factoryName = getIntent().getStringExtra(FACTORY_NAME);
        activityEquRouletteBinding = (ActivityEquRouletteBinding) viewDataBinding;
        activityEquRouletteBinding.recycleView.setAdapter(adapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        activityEquRouletteBinding.recycleView.setLayoutManager(gridLayoutManager);
        activityEquRouletteBinding.tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        activityEquRouletteBinding.tvModule.setText(name);
        activityEquRouletteBinding.tvTitle.setText(factoryName);
        setData(0);
        activityEquRouletteBinding.ivToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(k++%2==0){
                    type=1;
                }else{
                    type=2;
                }
                adapter = new EquRouletteAdapter(data,EquRouletteActivity.this,type);
                activityEquRouletteBinding.recycleView.setAdapter(adapter);
            }
        });

    }

    private void setData(int i) {
        if(i==0){
            OthersUtil.showLoadDialog(dialog);
        }

        RxjavaWebUtils.requestByGetJsonData(this, HS_SERVICE,
                "spappYunEquRealtimeData"
                , "sMobileNo=" + mMobileNo + ",iTerminalId=" + iTerminalId,
                EquRealTimeDataBean.class.getName(), true, "", new SimpleHsWeb() {
                    @Override
                    public void success(HsWebInfo hsWebInfo) {
                        data.clear();
                        List<WsEntity> entities = hsWebInfo.wsData.LISTWSDATA;
                        for (int i = 0; i < entities.size(); i++) {
                            EquRealTimeDataBean realTimeBean = (EquRealTimeDataBean) entities.get(i);
                            data.add(realTimeBean);
                        }
                        if(adapter==null){
                            adapter = new EquRouletteAdapter(data,EquRouletteActivity.this,type);
                            activityEquRouletteBinding.recycleView.setAdapter(adapter);
                        }else{
                            adapter.notifyDataSetChanged();
                        }
                        OthersUtil.dismissLoadDialog(dialog);
                    }

                    @Override
                    public void error(HsWebInfo hsWebInfo, Context context) {
                        super.error(hsWebInfo, context);
                        OthersUtil.dismissLoadDialog(dialog);
                    }
                });


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void autRefresh(AutoEvent a) {
            if (a.id % 6 == 0) {
                setData(1);
            }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
