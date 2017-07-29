package net.huansi.csapp;

import android.support.annotation.IdRes;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioGroup;
import net.huansi.csapp.databinding.ActivityMainBinding;
import net.huansi.csapp.event.HomeToRealEvent;
import net.huansi.csapp.fragment.HistoryFragment;
import net.huansi.csapp.fragment.HomeFragment;
import net.huansi.csapp.fragment.MineFragment;
import net.huansi.csapp.fragment.RealFragment;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import huansi.net.qianjingapp.base.NotWebBaseActivity;

public class MainActivity extends NotWebBaseActivity implements RadioGroup.OnCheckedChangeListener{

    private ActivityMainBinding activityMainBinding;
    private HomeFragment homeFragment;
    private RealFragment realFragment;
    private HistoryFragment historyFragment;
    private MineFragment mineFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void init() {
        activityMainBinding = (ActivityMainBinding) viewDataBinding;
        activityMainBinding.mainRG.setOnCheckedChangeListener(this);
        initFragments();
    }


    /**
     * 初始化fragment
     */
    private void initFragments() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        homeFragment = new HomeFragment();
        realFragment = new RealFragment();
        historyFragment = new HistoryFragment();
        mineFragment = new MineFragment();
        ft.add(R.id.mainFrameLayout, homeFragment);
        ft.add(R.id.mainFrameLayout, realFragment);
        ft.add(R.id.mainFrameLayout, historyFragment);
        ft.add(R.id.mainFrameLayout, mineFragment);
        ft.commitAllowingStateLoss();
        hideFragments(ft);
        ft.show(homeFragment);
    }

    /**
     * 隐藏fragment
     */
    private void hideFragments(FragmentTransaction ft) {
        ft.hide(homeFragment);
        ft.hide(realFragment);
        ft.hide(historyFragment);
        ft.hide(mineFragment);
    }



    @Override
    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        hideFragments(ft);
        switch (i){
            case R.id.mainHome:
                ft.show(homeFragment);
                break;
            case R.id.mainReal:
                ft.show(realFragment);
                break;
            case R.id.mainHistory:
                ft.show(historyFragment);
                break;
            case R.id.mainMy:
                ft.show(mineFragment);
                break;
        }
       ft.commitAllowingStateLoss();
    }

    /**
     * 点击工厂按钮跳转实时监控界面
     */
     @Subscribe(threadMode = ThreadMode.MAIN)
     public void jumpRealFragment(HomeToRealEvent event){
         FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
         hideFragments(ft);
         ft.show(realFragment);
         activityMainBinding.mainReal.setChecked(true);
    }

}
