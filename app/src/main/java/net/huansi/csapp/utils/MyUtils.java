package net.huansi.csapp.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.text.SimpleDateFormat;
import java.util.Date;

import static huansi.net.qianjingapp.utils.TimeUtils.TIME_FORMATE_MINUS;
import static huansi.net.qianjingapp.utils.TimeUtils.TIME_FORMATE_MINUS2;
import static huansi.net.qianjingapp.utils.TimeUtils.TIME_FORMATE_MINUS3;
import static huansi.net.qianjingapp.utils.TimeUtils.TIME_FORMATE_SLASH;

/**
 * Created by Administrator on 2017/7/6 0006.
 */

public class MyUtils {
    /**
     * 获取屏幕宽度和高度
     * @param context
     * @return
     */
    public static int[] getScreenSize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return new int[]{outMetrics.widthPixels, outMetrics.heightPixels};
    }
    public static String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }
    public static String formatString(String string){
        String[] split = string.split(" ");
       if(split.length>1){
           String[] split1 = split[1].split(":");
           if(split1.length>1){
               return  split1[0]+":"+split1[1];
           }

       }
        return "";
    }


    //得到当前操作时间
    public static String getCurDate(String timeType) {
        Date now = new Date();
        SimpleDateFormat dateFormat = null;
        if (timeType.equals("-")) {
            dateFormat = new SimpleDateFormat(TIME_FORMATE_MINUS);
        }else if(timeType.equals("--")){
            dateFormat = new SimpleDateFormat(TIME_FORMATE_MINUS2);
        } else if(timeType.equals("mm")) {
            dateFormat = new SimpleDateFormat(TIME_FORMATE_MINUS3);
        } else
        {
            dateFormat = new SimpleDateFormat(TIME_FORMATE_SLASH);
        }
        String sCurDate = dateFormat.format(now);
        return sCurDate;
    }
   //获取版本号
    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

}
