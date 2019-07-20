package com.jd.core.base;

import android.app.Application;
import android.content.Context;

import com.alibaba.android.arouter.launcher.ARouter;

import androidx.multidex.MultiDex;


public class BaseApplication extends Application {
    private static BaseApplication mBaseApplication;

    private boolean isDebugARouter = true;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        mBaseApplication = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //MultiDex分包方法 必须最先初始化
        MultiDex.install(this);


        if (isDebugARouter) {
            ARouter.openLog();
            ARouter.openDebug();
        }
        ARouter.init(this);
    }

    public static BaseApplication getAppContext(){
        return mBaseApplication;
    }


    /**
     * 退出应用
     */
    public void exitApp() {
        //用于杀掉当前进程
        android.os.Process.killProcess(android.os.Process.myPid());
        //参数0和1代表退出的状态，0表示正常退出，1表示异常退出
        System.exit(0);
    }


}
