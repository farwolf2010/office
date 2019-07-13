package com.office.plugin.module;


import android.util.Log;

import com.farwolf.weex.annotation.WeexModule;
import com.farwolf.weex.base.WXModuleBase;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;

import java.util.HashMap;

//此注解将自动注册module到weex
@WeexModule(name="demo")
public class WXOfficeModule extends WXModuleBase {


    //**
    //异步返回
    @JSMethod
    public void log(JSCallback callback){
        Log.e("error","");
        HashMap m=new HashMap();
        m.put("code",0);
        callback.invoke(m);
    }



    //**
    //同步返回
    @JSMethod(uiThread = false)
    public int getData(HashMap param){
        int code=0;
        if(param.containsKey("code"))
            code=Integer.valueOf(param.get("code")+"");
       return 0;
    }




}
