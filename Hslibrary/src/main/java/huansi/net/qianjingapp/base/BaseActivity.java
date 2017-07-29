package huansi.net.qianjingapp.base;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import huansi.net.qianjingapp.R;
import huansi.net.qianjingapp.listener.InitListener;
import huansi.net.qianjingapp.utils.DrawableCache;
import huansi.net.qianjingapp.utils.SPUtils;
import huansi.net.qianjingapp.view.LoadProgressDialog;


/**
 * Created by 单中年 on 2016/12/23.
 */

public abstract class BaseActivity extends RxAppCompatActivity implements InitListener {

    private TextView mToolbarTitle;
    private TextView mToolbarSubTitle;
    private Toolbar mToolbar;
    protected ViewDataBinding viewDataBinding;
    protected boolean isWeb;
    protected String mMobileNo;
    protected LoadProgressDialog mDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int layoutId=getLayoutId();
        viewDataBinding=DataBindingUtil.setContentView(this,layoutId);
        mMobileNo= SPUtils.getMobileNo(this);
        mDialog  = new LoadProgressDialog(this);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
       /*
        toolbar.setLogo(R.mipmap.gh);
        toolbar.setTitle("Title");
        toolbar.setSubtitle("Sub Title");
        */

        mToolbarTitle = (TextView) findViewById(R.id.tooltitle);
        mToolbarSubTitle = (TextView) findViewById(R.id.subtitle);

        if (mToolbar != null) {
            //将Toolbar显示到界面
            setSupportActionBar(mToolbar);
        }
        if (mToolbarTitle != null) {
            //getTitle()的值是activity的android:lable属性值
            mToolbarTitle.setText(getTitle());
            //设置默认的标题不显示
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        /**
         * 判断是否有Toolbar,并默认显示返回按钮
         */
        if(null != getToolbar() && isShowBacking()){
            showBack();
        }
    }

    /**
     * 获取头部标题的TextView
     * @return
     */
    public TextView getToolbarTitle(){
        return mToolbarTitle;
    }
    /**
     * 获取头部标题的TextView
     * @return
     */
    public TextView getSubTitle(){
        return mToolbarSubTitle;
    }

    /**
     * 设置头部标题
     * @param title
     */
    public void setToolBarTitle(CharSequence title) {
        if(mToolbarTitle != null){
            mToolbarTitle.setText(title);
        }else{
            getToolbar().setTitle(title);
            setSupportActionBar(getToolbar());
        }
    }

    /**
     * this Activity of tool bar.
     * 获取头部.
     * @return support.v7.widget.Toolbar.
     */
    public Toolbar getToolbar() {
        return (Toolbar) findViewById(R.id.toolbar);
    }

    /**
     * 版本号小于21的后退按钮图片
     */
    private void showBack(){
        //setNavigationIcon必须在setSupportActionBar(toolbar);方法后面加入
        getToolbar().setNavigationIcon(DrawableCache.getInstance()
                .getDrawable(isWeb? R.drawable.icon_top_close: R.drawable.icon_return,getApplicationContext()));
        getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isWeb) webClose();
                else onBackPressed();
            }
        });
    }

    public abstract void webClose();

    /**
     * 是否显示后退按钮,默认显示,可在子类重写该方法.
     * @return
     */
    protected boolean isShowBacking(){
        return true;
    }

    /**
     * this activity layout res
     * 设置layout布局,在子类重写该方法.
     * @return res layout xml id
     */
    protected abstract int getLayoutId();


    /**
     * 防止字体变大或者变小导致面目全非
     * @return
     */
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config=new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config,res.getDisplayMetrics() );
        return res;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

}
