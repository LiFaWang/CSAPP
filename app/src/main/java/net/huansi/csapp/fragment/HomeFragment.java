package net.huansi.csapp.fragment;




import android.content.Context;
import android.util.Log;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import net.huansi.csapp.R;
import net.huansi.csapp.adapter.HomeFragmentAdapter;
import net.huansi.csapp.bean.HomeInfoBean;
import net.huansi.csapp.databinding.FragmentHomeBinding;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import huansi.net.qianjingapp.entity.HsWebInfo;
import huansi.net.qianjingapp.entity.WsEntity;
import huansi.net.qianjingapp.fragment.BaseFragment;
import huansi.net.qianjingapp.imp.SimpleHsWeb;
import huansi.net.qianjingapp.utils.OthersUtil;
import huansi.net.qianjingapp.utils.RxjavaWebUtils;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

import static huansi.net.qianjingapp.utils.WebServices.WebServiceType.HS_SERVICE;

public class HomeFragment extends BaseFragment {


    private FragmentHomeBinding fragmentHomeBinding;


    @Override
    public int getLayout() {
        return R.layout.fragment_home;

    }

    @Override
    public void init() {
        EventBus.getDefault().register(getContext());
        fragmentHomeBinding = (FragmentHomeBinding) viewDataBinding;
        OthersUtil.initRefresh(fragmentHomeBinding.prtHome,getActivity());
        fragmentHomeBinding.prtHome.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                setData();
            }
        });
        setData();

    }

    public void setData(){
        //首页数据
        RxjavaWebUtils.requestByGetJsonData((RxAppCompatActivity)this.getActivity(), HS_SERVICE,
                "spappYunEquDistributionMap", "sMobileNo="+mMobileNo,
                HomeInfoBean.class.getName(), true, "", new SimpleHsWeb() {
                    @Override
                    public void success(HsWebInfo hsWebInfo) {
                        List<WsEntity> entities = hsWebInfo.wsData.LISTWSDATA;
                        List<HomeInfoBean> data = new ArrayList<>();
                        for (int i = 0; i < entities.size(); i++) {
                            HomeInfoBean homeInfoBean = (HomeInfoBean) entities.get(i);
                            data.add(homeInfoBean);
                        }
                        HomeFragmentAdapter adapter = new HomeFragmentAdapter(data,getContext());
                        fragmentHomeBinding.homeListView.setAdapter(adapter);
                        fragmentHomeBinding.prtHome.refreshComplete();
                    }

                    @Override
                    public void error(HsWebInfo hsWebInfo, Context context) {
                        super.error(hsWebInfo, context);
                        fragmentHomeBinding.prtHome.refreshComplete();
                    }
                });
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        if(EventBus.getDefault().isRegistered(getContext())){
            EventBus.getDefault().unregister(getContext());
        };
    }
}
