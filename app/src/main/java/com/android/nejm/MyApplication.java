package com.android.nejm;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.android.nejm.db.AnnouceRecordManager;
import com.android.nejm.db.DBManager;
import com.android.nejm.manage.LoginUserManager;
import com.android.nejm.manage.PushManager;
import com.android.nejm.utils.FileUtils1;
import com.android.nejm.utils.SPUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.store.PersistentCookieStore;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;

import java.io.File;
import java.util.List;
import java.util.logging.Level;

import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.ShareSDK;

public class MyApplication extends Application {
    private static MyApplication APPLICATION;
    public static String lat = "0.0"/*"123.3213"*/;
    public static String lng = "0.0"/*"30.2655"*/;
    public static String mToken = null;
    public static String client_id = null;
    public static String  uid;
//    public static User user = new User();
    public static String mStoreId;
    public static String address;
    public static String ori_address;
    public static String ori_lat;
    public static String ori_lng;

    //    public Handler uiHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            if (msg.what == ProximityEventHandler.MSG_NUM1) {
//                Bundle data = msg.getData();
//                for (String key : data.keySet()) {
//                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                    CAMagicProximityManager.getInstance().sendAdvancedNotification(R.mipmap.app_icon,"畅麦",data.getString(key),"",intent);
//                }
//            }else if (msg.what == ProximityEventHandler.MSG_NUM2) {
//                Attachment attachment = (Attachment) msg.obj;
//                displayAttachNotification(attachment);
//            }
//            super.handleMessage(msg);
//        }
//    };
//    CAMagicProximityManager caMagicProximityManager;
//    private ProximityEventHandler proximityEventHandler = new ProximityEventHandler(uiHandler);
    PackageInfo packageInfo = null;

    //static 代码段可以防止内存泄露
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);//全局设置主题颜色
                return new MaterialHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("minrui", "onCreate");
        String processName = getProcessName(this, android.os.Process.myPid());
        if (processName != null) {
            boolean defaultProcess = processName.equals("cn.com.nejm");
            if (defaultProcess) {
                APPLICATION = this;
                //获取apk版本号并与原来的做比较
                try {
                    packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                    int versionCode = packageInfo.versionCode;
                    int lastVersion = SPUtils.getSharedIntData(getApplicationContext(), "versionCode");
                    if (versionCode > lastVersion) {
                        SPUtils.setSharedBooleanData(getApplicationContext(), "first", true);
                        SPUtils.setSharedIntData(getApplicationContext(), "versionCode", versionCode);
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                Fresco.initialize(getApplicationContext());
                ShareSDK.initSDK(this);

                PushManager.getInstance().init();

                //JPushInterface.resumePush(getApplicationContext());
                MyApplication.mToken = SPUtils.getSharedStringData(getApplicationContext(), "access_token");
                MyApplication.client_id = SPUtils.getSharedStringData(getApplicationContext(), "client_id");
                MyApplication.mStoreId = SPUtils.getSharedStringData(getApplicationContext(), "store_id");
                initOkGo();

               // JPushInterface.setDebugMode(true);
//                JPushInterface.init(this);

//
//        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
//                .detectDiskReads()
//                .detectDiskWrites()
//                .detectNetwork()   // or .detectAll() for all detectable problems
//                .penaltyLog()
//                .build());
//        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
//                .detectActivityLeaks()
//                .detectLeakedRegistrationObjects()
//                .detectLeakedSqlLiteObjects()
////                .detectLeakedClosableObjects()
//                .penaltyLog()
//                .penaltyDeath()
//                .build());
            }
        }

        copyAssetsFiles();
    }

    private void copyAssetsFiles() {
        File filePath = new File(getExternalFilesDir(""), "/html");
        Log.e("dpp", "filePath.getAbsolutePath() = " + filePath.getAbsolutePath());
        FileUtils1.getInstance(this).copyAssetsToSD("", filePath.getAbsolutePath());
    }

    public static String getProcessName(Context cxt, int pid) {
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null || runningApps.isEmpty()) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return null;
    }


    public static MyApplication getApplication() {
        return APPLICATION;
    }

    private void initOkGo() {
        OkGo.init(this);

        //以下设置的所有参数是全局参数,同样的参数可以在请求的时候再设置一遍,那么对于该请求来讲,请求中的参数会覆盖全局参数
        //好处是全局参数统一,特定请求可以特别定制参数
        try {
            //以下都不是必须的，根据需要自行选择,一般来说只需要 debug,缓存相关,cookie相关的 就可以了
            OkGo.getInstance()

                    // 打开该调试开关,打印级别INFO,并不是异常,是为了显眼,不需要就不要加入该行
                    // 最后的true表示是否打印okgo的内部异常，一般打开方便调试错误
                    .debug("OkGo", Level.INFO, true)

                    //如果使用默认的 60秒,以下三行也不需要传
                    .setConnectTimeout(OkGo.DEFAULT_MILLISECONDS)  //全局的连接超时时间
                    .setReadTimeOut(OkGo.DEFAULT_MILLISECONDS)     //全局的读取超时时间
                    .setWriteTimeOut(OkGo.DEFAULT_MILLISECONDS)    //全局的写入超时时间

                    //可以全局统一设置缓存模式,默认是不使用缓存,可以不传,具体其他模式看 github 介绍 https://github.com/jeasonlzy/
                    .setCacheMode(CacheMode.NO_CACHE)

                    //可以全局统一设置缓存时间,默认永不过期,具体使用方法看 github 介绍
                    .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)

                    //可以全局统一设置超时重连次数,默认为三次,那么最差的情况会请求4次(一次原始请求,三次重连请求),不需要可以设置为0
                    .setRetryCount(3)

                    //如果不想让框架管理cookie（或者叫session的保持）,以下不需要
//              .setCookieStore(new MemoryCookieStore())            //cookie使用内存缓存（app退出后，cookie消失）
                    .setCookieStore(new PersistentCookieStore())        //cookie持久化存储，如果cookie不过期，则一直有效

                    //可以设置https的证书,以下几种方案根据需要自己设置
                    .setCertificates();                              //方法一：信任所有证书,不安全有风险
//              .setCertificates(new SafeTrustManager())            //方法二：自定义信任规则，校验服务端证书
//              .setCertificates(getAssets().open("srca.cer"))      //方法三：使用预埋证书，校验服务端证书（自签名证书）
//              //方法四：使用bks证书和密码管理客户端证书（双向认证），使用预埋证书，校验服务端证书（自签名证书）
//               .setCertificates(getAssets().open("xxx.bks"), "123456", getAssets().open("yyy.cer"))//

            //配置https的域名匹配规则，详细看demo的初始化介绍，不需要就不要加入，使用不当会导致https握手失败
//               .setHostnameVerifier(new SafeHostnameVerifier())

            //可以添加全局拦截器，不需要就不要加入，错误写法直接导致任何回调不执行
//                .addInterceptor(new Interceptor() {
//                    @Override
//                    public Response intercept(Chain chain) throws IOException {
//                        return chain.proceed(chain.request());
//                    }
//                })

            //这两行同上，不需要就不要加入
            //.addCommonHeaders(headers)  //设置全局公共头
            // .addCommonParams(params);   //设置全局公共参数

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}