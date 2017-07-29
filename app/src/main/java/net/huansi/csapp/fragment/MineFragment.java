package net.huansi.csapp.fragment;



import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import net.huansi.csapp.R;
import net.huansi.csapp.activity.LoginActivity;
import net.huansi.csapp.adapter.MineFragmentAdapter;
import net.huansi.csapp.bean.MineBean;
import net.huansi.csapp.databinding.FragmentMineBinding;
import net.huansi.csapp.utils.MyUtils;
import net.huansi.csapp.utils.SpUtils;

import java.util.ArrayList;
import java.util.List;

import huansi.net.qianjingapp.fragment.BaseFragment;

import static net.huansi.csapp.utils.Constants.IS_LOGIN;
import static net.huansi.csapp.utils.Constants.USER_COMPANY;
import static net.huansi.csapp.utils.Constants.USER_NAME;
import static net.huansi.csapp.utils.Constants.USER_SECTION;


public class MineFragment extends BaseFragment {

    private List<MineBean> data;

    @Override
    public int getLayout() {
        return R.layout.fragment_mine;
    }


    @Override
    public void init() {
        String mobileNo = SpUtils.getMobileNo(getActivity());
        String userName = SpUtils.getSpData(getActivity(), USER_NAME, "张三");
        String userSection = SpUtils.getSpData(getActivity(), USER_SECTION, "运营部");
        String userCompany = SpUtils.getSpData(getActivity(), USER_COMPANY, "环思智慧");
        String versionNum = MyUtils.getVersionName(getActivity());
        int ima[] = {R.drawable.account,R.drawable.user,R.drawable.state,R.drawable.company,
        R.drawable.section,R.drawable.updata,R.drawable.end};
        String title[]={"账号","用户名","状态","公司","部门","版本更新","注销"};
        String content[]={mobileNo,userName,"有效",userCompany,userSection,"V"+versionNum,""};
        data = new ArrayList<>();
        for (int i = 0; i <ima.length; i++) {
            data.add(new MineBean(ima[i],title[i],content[i]));
        }
        FragmentMineBinding fragmentMineBinding = (FragmentMineBinding) viewDataBinding;
        MineFragmentAdapter adapter  = new MineFragmentAdapter(data,getContext());
        fragmentMineBinding.lvMine.setAdapter(adapter);
       fragmentMineBinding.lvMine.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
              MineBean mine = (MineBean) adapterView.getItemAtPosition(i);
               if(mine.name.equals("注销")){
                   SpUtils.saveSpData(getActivity(),IS_LOGIN,"false");
                   startActivity(new Intent(getActivity(), LoginActivity.class));
                   getActivity().finish();
               }
           }
       });
    }
}
