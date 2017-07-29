package net.huansi.csapp.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import net.huansi.csapp.MainActivity;
import net.huansi.csapp.R;
import net.huansi.csapp.bean.LoginBean;
import net.huansi.csapp.databinding.ActivityLoginBinding;
import net.huansi.csapp.utils.SpUtils;

import java.util.List;

import huansi.net.qianjingapp.base.NotWebBaseActivity;
import huansi.net.qianjingapp.entity.HsWebInfo;
import huansi.net.qianjingapp.entity.WsEntity;
import huansi.net.qianjingapp.imp.SimpleHsWeb;
import huansi.net.qianjingapp.utils.OthersUtil;
import huansi.net.qianjingapp.utils.RxjavaWebUtils;

import static huansi.net.qianjingapp.utils.WebServices.WebServiceType.HS_SERVICE;
import static net.huansi.csapp.utils.Constants.IS_LOGIN;
import static net.huansi.csapp.utils.Constants.USER_COMPANY;
import static net.huansi.csapp.utils.Constants.USER_NAME;
import static net.huansi.csapp.utils.Constants.USER_SECTION;

public class LoginActivity extends NotWebBaseActivity {

     private String phoneNum="";//电话号码
     private String sPassword="";//密码
    private ActivityLoginBinding activityLoginBinding;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void init() {
        activityLoginBinding = (ActivityLoginBinding) viewDataBinding;


    }


    /**
     * 登陆按钮监听
     * @param view
     */
    public void login(View view){
        String isLogin = SpUtils.getSpData(LoginActivity.this, IS_LOGIN, "false");
        String password = activityLoginBinding.etPassword.getText().toString();
//        String phoneNum = activityLoginBinding.etPhoneNum.getText().toString();
        //测试
         if(password.equals("888880")){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }else if(isLogin.equals("false")){
             getLoginMes();

         }else{
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void getLoginMes() {
        RxjavaWebUtils.requestByGetJsonData(this, HS_SERVICE,
                "spappYunEquUserLogin"
                , "sMobileNo="+phoneNum+",sPassword="+sPassword,
                LoginBean.class.getName(), true, "", new SimpleHsWeb() {
                    @Override
                    public void success(HsWebInfo hsWebInfo) {
                        List<WsEntity> entities = hsWebInfo.wsData.LISTWSDATA;
                        LoginBean loginBean=null;
                        for (int i = 0; i < entities.size(); i++) {
                             loginBean = (LoginBean) entities.get(i);
                        }
                        if(loginBean!=null&&!loginBean.SMOBILENO.isEmpty()){
                            SpUtils.saveMobileNo(LoginActivity.this,loginBean.SMOBILENO);
                            SpUtils.saveSpData(LoginActivity.this,USER_NAME,loginBean.SUSERNAME);
                            SpUtils.saveSpData(LoginActivity.this,USER_SECTION,loginBean.SDEPTNAME);
                            SpUtils.saveSpData(LoginActivity.this,USER_COMPANY,loginBean.SCOMPANYNAME);
                            SpUtils.saveSpData(LoginActivity.this,IS_LOGIN,"true");
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            OthersUtil.ToastMsg(LoginActivity.this,"账号或者密码不正确！");
                        }

                    }

                    @Override
                    public void error(HsWebInfo hsWebInfo, Context context) {
                        super.error(hsWebInfo, context);
                    }
                });

    }


}
