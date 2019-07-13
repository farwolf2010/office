package com.office.plugin.component;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.farwolf.perssion.Perssion;
import com.farwolf.perssion.PerssionCallback;
import com.farwolf.util.StringUtil;
import com.farwolf.weex.annotation.WeexComponent;
import com.farwolf.weex.util.Const;
import com.farwolf.weex.util.Weex;
import com.office.plugin.util.LoadFileModel;
import com.office.plugin.util.Md5Tool;
import com.office.plugin.util.SuperFileView2;
import com.office.plugin.util.TLog;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXVContainer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//此office演示了如何制作一个界面组件
//可以改名后直接在这上面改，也可以删除了自己写
//name:在vue中标签的名字
//此注解将自动注册组件到weex
@WeexComponent(name="office")
public class WXOfficeComponent extends WXComponent<LinearLayout> {

    private String TAG = "WXOfficeComponent";

    String filePath;
    public WXOfficeComponent(WXSDKInstance instance, WXVContainer parent, BasicComponentData basicComponentData) {
        super(instance, parent, basicComponentData);
    }

    //**1.在这个方法里返回目标view
    @Override
    protected LinearLayout initComponentHostView(@NonNull Context context) {
        return new LinearLayout(context);
    }

    //2.此函数用来初始化
    @Override
    protected void onHostViewInitialized(LinearLayout host) {
        super.onHostViewInitialized(host);

    }



   //**android 是通过注解定义属性的
    @WXComponentProp(name = "src")
    public void setSrc(String path){
        if(StringUtil.isNullOrEmpty(path))
            return;
        this.filePath=path;
        Perssion.check((Activity) mInstance.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE,new PerssionCallback(){


            @Override
            public void onGranted() {


                Perssion.check((Activity) mInstance.getContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE , new PerssionCallback() {
                    @Override
                    public void onGranted() {
                        resetOffice(filePath);

                    }
                });



            }
        });
        //setSeekBarColor(getHostView(), Color.parseColor(color));

    }

    public void resetOffice(String url){
        this.filePath=url;
        SuperFileView2 sv=new SuperFileView2(getContext());
        ViewGroup.LayoutParams lp=new  ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        sv.setLayoutParams(lp);
        if(getHostView().getChildCount()>0){
            SuperFileView2 tp= (SuperFileView2)getHostView().getChildAt(0);
            tp.onStopDisplay();
            getHostView().removeAllViews();
        }
        getHostView().removeAllViews();
        getHostView().addView(sv);
        getFilePathAndShowFile(sv);
    }

    @Override
    public void onActivityDestroy() {
        super.onActivityDestroy();
        SuperFileView2 tp= (SuperFileView2)getHostView().getChildAt(0);
        tp.onStopDisplay();
    }

    public void setFilePath(String fileUrl) {
        this.filePath = fileUrl;
    }

    private String getFilePath() {
        if(filePath!=null&&filePath.startsWith(Const.PREFIX_SDCARD)){
            filePath=filePath.replace(Const.PREFIX_SDCARD,"");
        }
        filePath= Weex.getRootPath(filePath,getInstance());
        return filePath;
    }
    private void getFilePathAndShowFile(SuperFileView2 mSuperFileView2) {


        if (getFilePath().startsWith("http")) {//网络地址要先下载

            downLoadFromNet(getFilePath(),mSuperFileView2);

        } else {
            mSuperFileView2.displayFile(new File(getFilePath()));
        }
    }

    private void downLoadFromNet(final String url,final SuperFileView2 mSuperFileView2) {

        //1.网络下载、存储路径、
        File cacheFile = getCacheFile(url);
        if (cacheFile.exists()) {
            if (cacheFile.length() <= 0) {
                TLog.d(TAG, "删除空文件！！");
                cacheFile.delete();
                return;
            }
        }



        LoadFileModel.loadPdfFile(url, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                TLog.d(TAG, "下载文件-->onResponse");
                boolean flag;
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    ResponseBody responseBody = response.body();
                    is = responseBody.byteStream();
                    long total = responseBody.contentLength();

                    File file1 = getCacheDir(url);
                    if (!file1.exists()) {
                        file1.mkdirs();
                        TLog.d(TAG, "创建缓存目录： " + file1.toString());
                    }


                    //fileN : /storage/emulated/0/pdf/kauibao20170821040512.pdf
                    File fileN = getCacheFile(url);//new File(getCacheDir(url), getFileName(url))

                    TLog.d(TAG, "创建缓存文件： " + fileN.toString());
                    if (!fileN.exists()) {
                        boolean mkdir = fileN.createNewFile();
                    }
                    fos = new FileOutputStream(fileN);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        TLog.d(TAG, "写入缓存文件" + fileN.getName() + "进度: " + progress);
                    }
                    fos.flush();
                    TLog.d(TAG, "文件下载成功,准备展示文件。");
                    //2.ACache记录文件的有效期
                    mSuperFileView2.displayFile(fileN);
                } catch (Exception e) {
                    TLog.d(TAG, "文件下载异常 = " + e.toString());
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                TLog.d(TAG, "文件下载失败");
                File file = getCacheFile(url);
                if (!file.exists()) {
                    TLog.d(TAG, "删除下载失败文件");
                    file.delete();
                }
            }
        });


    }

    /***
     * 获取缓存目录
     *
     * @param url
     * @return
     */
    private File getCacheDir(String url) {

        return new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/007/");

    }
    /***
     * 绝对路径获取缓存文件
     *
     * @param url
     * @return
     */
    private File getCacheFile(String url) {
        File cacheFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/007/"
                + getFileName(url));
        TLog.d(TAG, "缓存文件 = " + cacheFile.toString());
        return cacheFile;
    }

    /***
     * 根据链接获取文件名（带类型的），具有唯一性
     *
     * @param url
     * @return
     */
    private String getFileName(String url) {
        String fileName = Md5Tool.hashKey(url) + "." + getFileType(url);
        return fileName;
    }

    /***
     * 获取文件类型
     *
     * @param paramString
     * @return
     */
    private String getFileType(String paramString) {
        String str = "";

        if (TextUtils.isEmpty(paramString)) {
            TLog.d(TAG, "paramString---->null");
            return str;
        }
        TLog.d(TAG,"paramString:"+paramString);
        int i = paramString.lastIndexOf('.');
        if (i <= -1) {
            TLog.d(TAG,"i <= -1");
            return str;
        }


        str = paramString.substring(i + 1);
        TLog.d(TAG,"paramString.substring(i + 1)------>"+str);
        return str;
    }

}
