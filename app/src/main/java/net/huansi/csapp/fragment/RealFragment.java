package net.huansi.csapp.fragment;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import net.huansi.csapp.R;
import net.huansi.csapp.adapter.PopAreaAdapter;
import net.huansi.csapp.adapter.PopFactoryAdapter;
import net.huansi.csapp.adapter.RealFragmentAdapter;
import net.huansi.csapp.bean.CountryListBean;
import net.huansi.csapp.bean.FactoryListBean;
import net.huansi.csapp.bean.RealTimeMonitoringBean;
import net.huansi.csapp.databinding.FragmentRealBinding;
import net.huansi.csapp.event.HomeToRealEvent;
import net.huansi.csapp.utils.MyUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import huansi.net.qianjingapp.entity.HsWebInfo;
import huansi.net.qianjingapp.entity.WsEntity;
import huansi.net.qianjingapp.fragment.BaseFragment;
import huansi.net.qianjingapp.imp.SimpleHsWeb;
import huansi.net.qianjingapp.utils.OthersUtil;
import huansi.net.qianjingapp.utils.RxjavaWebUtils;
import huansi.net.qianjingapp.view.LoadProgressDialog;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

import static huansi.net.qianjingapp.utils.WebServices.WebServiceType.HS_SERVICE;


public class RealFragment extends BaseFragment {


    private FragmentRealBinding fragmentRealBinding;
    private int[] length;//屏幕长宽;
    private List<FactoryListBean> factoryData;//工厂数据
    private List<CountryListBean> countryData;//区域数据
    private XAxis xAxis;//x轴
    private YAxis yAxis;//y轴
    private BarDataSet allPointsName, errorPointsName;//所有点数，异常点数名字
    private ArrayList<BarEntry> allPoints, errorPoints;//所有点数，异常点数
    private String xData[];//x轴描述
    private Map<String,String> mapCountry;//存储区域的id和名字
    private Map<String,List<FactoryListBean>> mapFactory;//存储区域的id，工厂的id和名字
    private Map<String,String> itemMapFactory;//存储工厂的id和名字
    private List<String> popNameData;//填充popwindow的工厂或者区域的名字
    private PopAreaAdapter adapterArea;
    private PopFactoryAdapter adapterFactory;
    private String iCountryId="";//国家id
    private String iFactoryId="";//工厂id
    private String iFactoryName="";//工厂名字
    private List<BarDataSet> barChart ;//存储BarDataSet
    private List<FactoryListBean> factoryDataMemory;//存储FactoryListBean的输出进行筛选
    private List<RealTimeMonitoringBean> data;//实时监控设备数据
    private RealFragmentAdapter adapter;
    private LoadProgressDialog dialog;


    @Override
    public int getLayout() {
        return R.layout.fragment_real;

    }


    @Override
    public void init() {
        dialog = new LoadProgressDialog(getActivity());
        allPoints = new ArrayList<>();
        errorPoints = new ArrayList<>();
        mapCountry = new HashMap<>();
        mapFactory = new HashMap<>();
        itemMapFactory = new HashMap<>();
        popNameData = new ArrayList<>();
        barChart = new ArrayList<>();
        factoryData = new ArrayList<>();
        factoryDataMemory = new ArrayList<>();
        data = new ArrayList<>();
        length = MyUtils.getScreenSize(getActivity());
        EventBus.getDefault().register(this);
        fragmentRealBinding = (FragmentRealBinding) viewDataBinding;
        OthersUtil.initRefresh(fragmentRealBinding.prtReal,getActivity());
        adapter = new RealFragmentAdapter(data, getContext());
        fragmentRealBinding.realListView.setAdapter(adapter);
        initChart();
        setData();
        fragmentRealBinding.prtReal.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                setEquData(iCountryId,iFactoryId);
            }
        });
    }

    /**
     * 初始化柱状图信息
     */
    private void initChart() {
        fragmentRealBinding.barChart.getAxisRight().setEnabled(false);
        fragmentRealBinding.barChart.setDescription("");
        xAxis = fragmentRealBinding.barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        yAxis = fragmentRealBinding.barChart.getAxisLeft();
        yAxis.setDrawGridLines(false);
        fragmentRealBinding.barChart.setNoDataText("没有数据");
        fragmentRealBinding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String factory = fragmentRealBinding.tvFactory.getText().toString();
                if(factory.equals("无")){
                    OthersUtil.ToastMsg(getContext(),"该区域无数据");
                }else if(factory.equals("(区域)")){
                    OthersUtil.ToastMsg(getContext(),"请选择区域");
                }else if(factory.equals("(工厂)")){
                    OthersUtil.ToastMsg(getContext(),"请选择工厂");
                }else{
//                    OthersUtil.ToastMsg(getContext(),"查询完成");
                    setEquData(iCountryId,iFactoryId);

                }
            }
        });

    }

    private void setData() {
             OthersUtil.showLoadDialog(dialog);
        //区域数据
        RxjavaWebUtils.requestByGetJsonData((RxAppCompatActivity) this.getActivity(), HS_SERVICE,
                "spappYunEquCountryList", "sMobileNo="+mMobileNo,
                CountryListBean.class.getName(), true, "", new SimpleHsWeb() {

                    @Override
                    public void success(HsWebInfo hsWebInfo) {
                        List<WsEntity> entities = hsWebInfo.wsData.LISTWSDATA;
                        countryData = new ArrayList<>();
                        countryData.clear();
                        List<CountryListBean> data = new ArrayList<>();
                        for (int i = 0; i < entities.size(); i++) {
                            CountryListBean countryListBean = (CountryListBean) entities.get(i);
                            countryData.add(countryListBean);
                            data.add(countryListBean);
                            mapCountry.put(countryListBean.ICOUNTRYID,countryListBean.SCOUNTRYNAME);
                        }
                        iCountryId = countryData.get(0).ICOUNTRYID;
                        setFactory();
                    }

                    @Override
                    public void error(HsWebInfo hsWebInfo, Context context) {
                        super.error(hsWebInfo, context);

                    }
                });

        fragmentRealBinding.tvArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapterArea = new PopAreaAdapter(countryData,getContext());
                showPop(view);
            }
        });
        fragmentRealBinding.tvFactory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapterFactory = new PopFactoryAdapter(factoryData,getContext());
                if(fragmentRealBinding.tvArea.getText().equals("(区域)")){
                    OthersUtil.ToastMsg(getContext(),"请选择区域");
                }else{
                    showPop(view);
                }


            }
        });

    }

    /**
     * 工厂数据
     */
    private void setFactory() {
        //工厂数据
        RxjavaWebUtils.requestByGetJsonData((RxAppCompatActivity) this.getActivity(), HS_SERVICE,
                "spappYunEquFactoryList", "sMobileNo="+mMobileNo,
                FactoryListBean.class.getName(), true, "", new SimpleHsWeb() {
                    @Override
                    public void success(HsWebInfo hsWebInfo) {
                        List<WsEntity> entities = hsWebInfo.wsData.LISTWSDATA;
                        factoryDataMemory.clear();
                        for (int i = 0; i < entities.size(); i++) {
                            FactoryListBean factory = (FactoryListBean) entities.get(i);
//                            factoryData.add(factory);
                            factoryDataMemory.add(factory);
                            itemMapFactory.put(factory.IFACTORYID,factory.SFACTORYNAME);
                            mapFactory.put(factory.ICOUNTRYID,factoryDataMemory);

                        }
                        setEquData("","");
                    }


                    @Override
                    public void error(HsWebInfo hsWebInfo, Context context) {
                        super.error(hsWebInfo, context);


                    }
                });
    }

    /**实时监控设备数据
     *
     */
    private void setEquData(String iCountryId,String iFactoryId) {
        OthersUtil.showLoadDialog(dialog);
        //实时监控设备数据
        RxjavaWebUtils.requestByGetJsonData((RxAppCompatActivity) this.getActivity(), HS_SERVICE,
                "spappYunEquRealtimeMonitoring", "sMobileNo=''" +mMobileNo+
                        ",iCountryId=" +iCountryId+
                        ",iFactoryId="+iFactoryId,
                RealTimeMonitoringBean.class.getName(), true, "", new SimpleHsWeb() {
                    @Override
                    public void success(HsWebInfo hsWebInfo) {
                        List<WsEntity> entities = hsWebInfo.wsData.LISTWSDATA;
                        data.clear();
                        xData=null;
                        barChart.clear();
                        allPoints.clear();
                        errorPoints.clear();
                        for (int i = 0; i < entities.size(); i++) {
                            RealTimeMonitoringBean real = (RealTimeMonitoringBean) entities.get(i);

                            data.add(real);
                        }

                        adapter.notifyDataSetChanged();
                        xData = new String[data.size()];
                        for (int i = 0; i < data.size(); i++) {
                            allPoints.add(new BarEntry(Integer.valueOf(data.get(i).NCHANNELNUMBER),i));
                            errorPoints.add(new BarEntry(Integer.valueOf(data.get(i).NEXPNUMBER),i));
                            xData[i] = data.get(i).STERMINALNAME;
                            System.out.println("0000111111:"+xData);
                        }

                        allPointsName = new BarDataSet(allPoints, "采集总点数");
                        allPointsName.setColor(Color.rgb(104, 241, 175));
                        errorPointsName = new BarDataSet(errorPoints, "异常点数");
                        errorPointsName.setColor(Color.rgb(255, 102, 0));
                        barChart.add(allPointsName);
                        barChart.add(errorPointsName);
                        BarData barData = new BarData(xData, barChart);
                        barData.setDrawValues(true);
                        fragmentRealBinding.barChart.setData(barData);
                        OthersUtil.dismissLoadDialog(dialog);
                        fragmentRealBinding.barChart.animateXY(0,2000);
                        fragmentRealBinding.barChart.invalidate();
                        barData.notifyDataChanged();
                        fragmentRealBinding.prtReal.refreshComplete();
                    }

                    @Override
                    public void error(HsWebInfo hsWebInfo, Context context) {
                        super.error(hsWebInfo, context);
                        OthersUtil.dismissLoadDialog(dialog);
                        fragmentRealBinding.barChart.clear();
                        fragmentRealBinding.barChart.invalidate();
                        data.clear();
                        adapter.notifyDataSetChanged();
                        fragmentRealBinding.prtReal.refreshComplete();

                    }
                });
    }

    /**
     * 区域和工厂pop
     *
     * @param view
     */
    List<FactoryListBean> listFactoryItem = new ArrayList<>();
    public void showPop(final View view) {
        popNameData.clear();
        View contentView = LayoutInflater.from(getContext()).inflate(
                R.layout.pop_list, null);
        final PopupWindow popupWindow = new PopupWindow(contentView,
                length[0] / 3 - 5, length[1] / 4, true);
        final ListView popListView = (ListView) contentView.findViewById(R.id.lv_pop_clothlist);
       switch (view.getId()){
           case R.id.tvArea:
               popListView.setAdapter(adapterArea);
               break;
           case R.id.tvFactory:
               popListView.setAdapter(adapterFactory);
               break;
       }
        popupWindow.setTouchable(true);
        ColorDrawable dw = new ColorDrawable(0x000);
        popupWindow.setBackgroundDrawable(dw);
        popupWindow.showAsDropDown(view);
        popListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view2, int position, long id) {

                switch (view.getId()) {
                    case R.id.tvArea:
                        CountryListBean itemCountry = (CountryListBean) parent.getItemAtPosition(position);
                        fragmentRealBinding.tvArea.setText(itemCountry.SCOUNTRYNAME);
                        listFactoryItem.clear();
                        for (int i = 0; i < factoryDataMemory.size(); i++) {
                            FactoryListBean factoryListBean = factoryDataMemory.get(i);
                            if(itemCountry.ICOUNTRYID.equals(factoryListBean.ICOUNTRYID)){
                                listFactoryItem.add(factoryListBean);
                            }
                        }
                        if(listFactoryItem.isEmpty()){
                            fragmentRealBinding.tvFactory.setText("无");
                            factoryData.clear();
                        }else{
                            factoryData.clear();
                            factoryData.addAll(listFactoryItem);
                            fragmentRealBinding.tvFactory.setText(listFactoryItem.get(0).SFACTORYNAME);
                            iFactoryId = listFactoryItem.get(0).IFACTORYID;
                            iFactoryName=  listFactoryItem.get(0).SFACTORYNAME;
                        }
                        iCountryId = itemCountry.ICOUNTRYID;
                        break;
                    case R.id.tvFactory:
                        FactoryListBean itemFactory = (FactoryListBean) parent.getItemAtPosition(position);
                        fragmentRealBinding.tvFactory.setText(itemFactory.SFACTORYNAME);
                        iFactoryId = itemFactory.IFACTORYID;
                        iFactoryName= itemFactory.SFACTORYNAME;
                        break;
                }
                popupWindow.dismiss();
            }
        });
    }

    /**
     * 接收首页点击工厂的数据
     *
     * @param
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveHomeInfo(HomeToRealEvent event) {
        fragmentRealBinding.tvArea.setText(event.getArea());
        fragmentRealBinding.tvFactory.setText(event.getFactory());
        iCountryId = event.getCountryID();
        iFactoryId= event.getFactoryID();
        String countryID = event.getCountryID();
        factoryData.clear();
        for (int i = 0; i < factoryDataMemory.size(); i++) {
            FactoryListBean factoryListBean = factoryDataMemory.get(i);
            if(countryID.equals(factoryListBean.ICOUNTRYID)){
                factoryData.add(factoryListBean);
            }
        }
        setEquData(iCountryId,iFactoryId);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
