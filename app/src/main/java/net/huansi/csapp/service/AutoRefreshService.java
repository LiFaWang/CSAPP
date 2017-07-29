package net.huansi.csapp.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import net.huansi.csapp.event.AutoEvent;

import org.greenrobot.eventbus.EventBus;

public class AutoRefreshService extends Service {

    private Thread timeThread;
    private int i=0;
    public AutoRefreshService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        timeThread=new Thread(new GUTimer());
        timeThread.start();
    }

    //开线程
    class GUTimer implements Runnable {
        @Override
        public void run() {
            while (true) {
                i++;
                AutoEvent event  = new AutoEvent();
                event.setId(i);
                EventBus.getDefault().post(event);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timeThread.interrupt();
    }
}
