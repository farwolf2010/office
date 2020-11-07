package com.office.plugin.init;

import android.content.Context;
import android.util.Log;

import com.farwolf.weex.annotation.ModuleEntry;
import com.office.plugin.util.ExceptionHandler;
import com.tencent.smtt.sdk.QbSdk;


//此注解会被框架扫描到并执行类的init方法
@ModuleEntry
public class PluginInit {


    //**方法明和参数固定，框架会根据反射执行此函数，context 为application类型
    //**如果有第三方需要初始化，请在此处执行
    public  void init(Context context)
    {

        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("app", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(context,  cb);

    }
}
