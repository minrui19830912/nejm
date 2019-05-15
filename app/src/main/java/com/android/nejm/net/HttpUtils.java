package com.android.nejm.net;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.nejm.MyApplication;
import com.android.nejm.utils.Util;
import com.android.nejm.widgets.LoadingDialog;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.PostRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

import static com.lzy.okgo.OkGo.post;


/**
 * Created by malone on 2017/1/19.
 */

public class HttpUtils {
    public static final String APP_KEY="gm8Pyx3sbuCdqsspYylv3rhh9Bt40vn7";
    public static final String CONTENT_TYPE="application/x-www-form-urlencoded";
//    public static final String BASE_URL="https://nejmqianyan.cn";//production
    public static final String BASE_URL="https://dev.nejmqianyan.com";//test

    public static final String MAIN_URL=BASE_URL+"/?c=app&m=index";//首页
    public static final String NEW_KNOWLEDGE_URL=BASE_URL+"/?c=app&m=activities";//新知列表
    public static final String YEAR_ARTICLE_URL=BASE_URL+"/?c=app&m=weeks";//期刊列表
    public static final String VIDEO_LIST_URL=BASE_URL+"/?c=app&m=videos";//视频列表
    public static final String DOWNLOAD_ARTICLE_THIS_WEEK_URL=BASE_URL+"/?c=app&m=get_zip_url";//下载本周文章

    public static final String PERIOD_ARTICLE_DETAIL_URL=BASE_URL+"/?c=app&m=week";//期刊详情
    public static final String ARTICLE_LIST_URL=BASE_URL+"/?c=app&m=article_filter";//文章列表
    public static final String ARTICLE_CLASS_URL=BASE_URL+"/?c=app&m=article_class";//专业领域
    public static final String READ_RECORD_URL=BASE_URL+"/?c=app&m=read";//阅读记录



    public static final String STORE_LIST=BASE_URL+"/api/stores?";//门店列表
    public static final String QUERY_STORE_LIST=BASE_URL+"/api/query_stores?";//门店列表（输入地址时调用）
    public static final String STORE_LIST_DETAIL=BASE_URL+"/api/store?";//门店详情
    public static final String FIND_LIST=BASE_URL+"/api/pages?";//发现
    public static final String REGISTER=BASE_URL+"/api/register?";//注册
    public static final String GET_VERIFY_CODE=BASE_URL+"/api/send_phone_code?";//发送短信验证码
    public static final String LOGIN=BASE_URL+"/api/login?";//登录
    public static final String USER_INFO=BASE_URL+"/api/account?";//个人中心首页
    public static final String MY_MESSAGE=BASE_URL+"/api/messages?";//我的消息
    public static final String DEL_MESSAGE=BASE_URL+"/api/delete_msg?";//消息删除
    public static final String MY_ACCOUNT=BASE_URL+"/api/scores?";//我的账户
    public static final String LOGOUT=BASE_URL+"/api/logout?";//退出登陆
    public static final String ADDRESS_LIST=BASE_URL+"/api/shippings?";//收货人管理
    public static final String ADD_ADDRESS=BASE_URL+"/api/deal_add_shipping?";//添加收货人
    public static final String SERACH=BASE_URL+"/api/search?";//搜索页面
    public static final String MY_COLLECTION=BASE_URL+"/api/collect?";//我的收藏
    public static final String COLLECT_GOOD=BASE_URL+"/api/deal_good_collect?";//处理商品收藏
    public static final String GOOD_DETAIL=BASE_URL+"/api/good?";//商品详情
    public static final String GOOD_LIST=BASE_URL+"/api/goods?";//商品列表
    public static final String EDIT_ADDRESS=BASE_URL+"/api/deal_update_shipping?";//编辑收货人
    public static final String CHANGE_DEFAULT=BASE_URL+"/api/change_shipping_default?";//设置默认收货人
    public static final String DEL_ADDRESS=BASE_URL+"/api/delete_shipping?";//删除收货人
    public static final String GET_SHOPCAR=BASE_URL+"/api/carts?";//购物车
    public static final String ADD_TO_SHOPCAR=BASE_URL+"/api/deal_good_cart?";//添加到购物车
    public static final String DEL_SHOPCAR=BASE_URL+"/api/delete_cart?";//删除购物车商品
    public static final String CONFIRM_ORDER=BASE_URL+"/api/confirm_order?";//确认订单
    public static final String CHANG_GOODNUM_IN_SHOPCAR=BASE_URL+"/api/change_num?";//购物车购买数量变更
    public static final String SUBMIT_ORDER=BASE_URL+"/api/deal_order?";//提交订单
    public static final String MY_ORDER=BASE_URL+"/api/orders?";//我的订单
    public static final String ACCEPT_CONFIRM=BASE_URL+"/api/deal_receipt_order?";//确认收货
    public static final String CANCEL_ORDER=BASE_URL+"/api/deal_cancel_order?";//取消订单
    public static final String EXPRESS_INFO=BASE_URL+"/api/express?";//快递信息
    public static final String DEL_ORDER=BASE_URL+"/api/deal_delete_order?";//删除订单
    public static final String ORDER_DETAIL=BASE_URL+"/api/order?";//订单详情
    public static final String MODIFY_SEX=BASE_URL+"/api/deal_edit_sex?";//修改性别
    public static final String MODIFY_NAME=BASE_URL+"/api/deal_edit_name?";//修改昵称
    public static final String MODIFY_HEAD=BASE_URL+"/api/deal_edit_avatar?";//修改头像
    public static final String CHARGE_FEE=BASE_URL+"/api/deal_recharge?";//充值
    public static final String CONTINUE_PAY=BASE_URL+"/api/order_pay?";//个人中心订单继续支付
    public static final String WEIXIN_LOGIN=BASE_URL+"/api/android_weixin_login?";//微信登录
    public static final String GET_COMMENT_LIST=BASE_URL+"/api/comments?";//商品评价列表
    public static final String ADD_COMMENT=BASE_URL+"/api/deal_add_comment?";//评价添加
    public static final String ADD_REFUND=BASE_URL+"/api/add_refund?";//添加退货退款页面
    public static final String DEL_REFUND=BASE_URL+"/api/deal_add_refund?";//处理退货退换
    public static final String FIND_PWD_SENDMSG=BASE_URL+"/password/send_phone_code?";//找回密码发送验证码
    public static final String DEAL_PWD=BASE_URL+"/password/deal_action?";//处理密码

    public static final String SEND_PWD_CODE = BASE_URL+"/api/send_password_phone_code?";//重置密码发送密码
    public static final String DEAL_PWD_RESET = BASE_URL+ "/api/deal_password_reset?";//重置密码
    public static final String DEAL_EXPORT = BASE_URL +"/api/deal_export?";//提现
    public static final String BIND_PHONE = BASE_URL +"/api/bind_phone?";//绑定手机号
    public static final String SEND_CUSTOMER_PHONE = BASE_URL +"/api/send_customer_phone_code?";//获取 messageNumber 接口
    public static final String QUERY_WATERFALL = BASE_URL +"/api/query_waterfall?";//获取 瀑布流数据 接口

    private static boolean isDebug=false;

    public static void getMainData(final Context context, final OnNetResponseListener listener){
       long timeStamp= System.currentTimeMillis();

       String sign= generateMd5Str("",timeStamp,APP_KEY,"");
        StringBuilder build=new StringBuilder("^");
//access_token^timestamp^clientid
        build.append("").append("^").append(timeStamp).append("^").append("");
        OkGo.get(MAIN_URL).headers("Authorization",sign+build.toString()).execute(new StringNetCallback(context) {
            @Override
            public void onSuccess(String s, Call call, Response response) {


                paraJson(context,s,listener);
            }


        });

    }

    public static void getNewKnowledge(final Context context, String id, int page, final OnNetResponseListener listener){
        long timeStamp= System.currentTimeMillis();

        String sign= generateMd5Str("",timeStamp,APP_KEY,"");
        StringBuilder build=new StringBuilder("^");
//access_token^timestamp^clientid
        build.append("").append("^").append(timeStamp).append("^").append("");

        StringBuilder url = new StringBuilder(NEW_KNOWLEDGE_URL);
        if(!TextUtils.isEmpty(id)){
            url.append("&id=").append(id);
        }
        url.append("&page=").append(page);

        OkGo.get(url.toString()).headers("Authorization",sign+build.toString()).execute(new StringNetCallback(context) {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                paraJson(context,s,listener);
            }
        });

    }

    public static void getYearArticles(final Context context, String year,final OnNetResponseListener listener){
        long timeStamp= System.currentTimeMillis();

        String sign= generateMd5Str("",timeStamp,APP_KEY,"");
        StringBuilder build=new StringBuilder("^");
//access_token^timestamp^clientid
        build.append("").append("^").append(timeStamp).append("^").append("");
        StringBuilder url = new StringBuilder(YEAR_ARTICLE_URL);
        if(!TextUtils.isEmpty(year)){
            url.append("&id=").append(year);
        }
        OkGo.get(url.toString()).headers("Authorization",sign+build.toString()).execute(new StringNetCallback(context) {
            @Override
            public void onSuccess(String s, Call call, Response response) {


                paraJson(context,s,listener);
            }


        });

    }



    public static void getVideoList(final Context context,String id,int page, final OnNetResponseListener listener){
        long timeStamp= System.currentTimeMillis();

        String sign= generateMd5Str("",timeStamp,APP_KEY,"");
        StringBuilder build=new StringBuilder("^");
//access_token^timestamp^clientid
        build.append("").append("^").append(timeStamp).append("^").append("");
        StringBuilder url = new StringBuilder(VIDEO_LIST_URL);
        if(!TextUtils.isEmpty(id)){
            url.append("&id=").append(id);
        }
        url.append("&page=").append(page);
        OkGo.get(url.toString()).headers("Authorization",sign+build.toString()).execute(new StringNetCallback(context) {
            @Override
            public void onSuccess(String s, Call call, Response response) {


                paraJson(context,s,listener);
            }


        });

    }

    public static void getArticleClassList(final Context context,String id,int page, final OnNetResponseListener listener){
        long timeStamp= System.currentTimeMillis();

        String sign= generateMd5Str("",timeStamp,APP_KEY,"");
        StringBuilder build=new StringBuilder("^");
//access_token^timestamp^clientid
        build.append("").append("^").append(timeStamp).append("^").append("");
        StringBuilder url = new StringBuilder(ARTICLE_CLASS_URL);
        if(!TextUtils.isEmpty(id)){
            url.append("&id=").append(id);
        }
        url.append("&page=").append(page);
        OkGo.get(url.toString()).headers("Authorization",sign+build.toString()).execute(new StringNetCallback(context) {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                paraJson(context,s,listener);
            }
        });
    }

    public static void getArticleList(final Context context,String id,int page, final OnNetResponseListener listener){
        long timeStamp= System.currentTimeMillis();

        String sign= generateMd5Str("",timeStamp,APP_KEY,"");
        StringBuilder build=new StringBuilder("^");
//access_token^timestamp^clientid
        build.append("").append("^").append(timeStamp).append("^").append("");
        StringBuilder url = new StringBuilder(ARTICLE_LIST_URL);
        if(!TextUtils.isEmpty(id)){
            url.append("&id=").append(id);
        }
        url.append("&page=").append(page);
        OkGo.get(url.toString()).headers("Authorization",sign+build.toString()).execute(new StringNetCallback(context) {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                paraJson(context,s,listener);
            }
        });
    }

    public static void getReadRecordList(final Context context, int page, final OnNetResponseListener listener){
        long timeStamp= System.currentTimeMillis();

        String sign= generateMd5Str("",timeStamp,APP_KEY,"");
        StringBuilder build=new StringBuilder("^");
//access_token^timestamp^clientid
        build.append("").append("^").append(timeStamp).append("^").append("");
        StringBuilder url = new StringBuilder(READ_RECORD_URL);
        url.append("&page=").append(page);
        OkGo.get(url.toString()).headers("Authorization",sign+build.toString()).execute(new StringNetCallback(context) {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                paraJson(context,s,listener);
            }
        });
    }

    public static void getPeriodArticleDetails(final Context context, String id, final OnNetResponseListener listener){
        long timeStamp= System.currentTimeMillis();

        String sign= generateMd5Str("",timeStamp,APP_KEY,"");
        StringBuilder build=new StringBuilder("^");
//access_token^timestamp^clientid
        build.append("").append("^").append(timeStamp).append("^").append("");

        StringBuilder url = new StringBuilder(PERIOD_ARTICLE_DETAIL_URL);
        if(!TextUtils.isEmpty(id)){
            url.append("&id=").append(id);
        }

        OkGo.get(url.toString()).headers("Authorization",sign+build.toString()).execute(new StringNetCallback(context) {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                paraJson(context,s,listener);
            }
        });

    }


    public static void getThisWeekArticle(final Context context, int  lastzipid,final OnNetResponseListener listener){
        long timeStamp= System.currentTimeMillis();

        String sign= generateMd5Str("",timeStamp,APP_KEY,"");
        StringBuilder build=new StringBuilder("^");
//access_token^timestamp^clientid
        build.append("").append("^").append(timeStamp).append("^").append("");
        String url = DOWNLOAD_ARTICLE_THIS_WEEK_URL+"&lastzipid="+lastzipid;
        OkGo.get(url).headers("Authorization",sign+build.toString()).execute(new StringNetCallback(context) {
            @Override
            public void onSuccess(String s, Call call, Response response) {


                paraJson(context,s,listener);
            }


        });

    }


    //瀑布流
    public static void getWaterFallData(final Context context, int page, final OnNetResponseListener listener){
        long timeStamp= System.currentTimeMillis();
        String sign= generateMd5Str("","page"+page,timeStamp,APP_KEY,"");
        StringBuilder build=new StringBuilder("access_token=");
        build.append("&timestamp=").append(timeStamp).append("&app_key="+APP_KEY)
                .append("&client=").append("&page=").append(page).append("&sign=").append(sign);

//        if (isDebug)
//            Log.e("changmai","jsonurl:"+QUERY_WATERFALL+build.toString()+">>>");

        OkGo.get(QUERY_WATERFALL+build.toString()).execute(new StringNetCallback(context) {
            @Override
            public void onSuccess(String s, Call call, Response response) {


                paraJson(context,s,listener);
            }


        });

    }

    public static void getCommentList(final Context context, String lat, String lng, int id, final OnNetResponseListener listener){
        long timeStamp= System.currentTimeMillis();
//    String lat="123.3213";
//    String lng="30.2655";
        String sign= generateMd5Str("","id"+id+"lat"+lat+"lng"+lng,timeStamp,APP_KEY,"");
//           Util.getMd5("lat"+lat+"lng"+lng+timeStamp+"gm8Pyx3sbuCdqsspYylv3rhh9Bt40vn7");
        StringBuilder build=new StringBuilder("access_token=");
        build.append("&timestamp=").append(timeStamp).append("&app_key="+APP_KEY)
                .append("&client=").append("&lat=").append(lat).append("&lng=").append(lng).append("&id=").append(id).append("&sign=").append(sign);
//        if (isDebug)
//            Log.e("changmai","jsonurl:"+GET_COMMENT_LIST+build.toString()+">>>");
        OkGo.get(GET_COMMENT_LIST+build.toString()).execute(new StringNetCallback(context) {
            @Override
            public void onSuccess(String s, Call call, Response response) {


                paraJson(context,s,listener);
            }


        });


    }


    public static void getStoreList(final Context context, String lat, String lng, final OnNetResponseListener listener){
        long timeStamp= System.currentTimeMillis();
        String sign= generateMd5Str("","lat"+lat+"lng"+lng,timeStamp,APP_KEY,"");
        StringBuilder build=new StringBuilder("access_token=");
        build.append("&timestamp=").append(timeStamp).append("&app_key="+APP_KEY)
                .append("&client=").append("&lat=").append(lat).append("&lng=").append(lng).append("&sign=").append(sign);
//        if (isDebug)
//            Log.e("changmai","jsonurl:"+STORE_LIST+build.toString()+">>>");
        OkGo.get(STORE_LIST+build.toString()).execute(new StringNetCallback(context) {
            @Override
            public void onSuccess(String s, Call call, Response response) {


                paraJson(context,s,listener);
            }

        });
    }

    public static void getQueryStoreList(final Context context, String lat, String lng, String address, final OnNetResponseListener listener){
        long timeStamp= System.currentTimeMillis();
        String sign= generateMd5Str("","address"+address+"lat"+lat+"lng"+lng,timeStamp,APP_KEY,"");
        StringBuilder build=new StringBuilder("access_token=");
        build.append("&timestamp=").append(timeStamp).append("&app_key="+APP_KEY)
                .append("&client=").append("&lat=").append(lat).append("&lng=").append(lng).append("&address=").append(address).append("&sign=").append(sign);
//        if (isDebug)
//            Log.e("changmai","jsonurl:"+QUERY_STORE_LIST+build.toString()+">>>");
        OkGo.get(QUERY_STORE_LIST+build.toString()).execute(new StringNetCallback(context) {
            @Override
            public void onSuccess(String s, Call call, Response response) {


                paraJson(context,s,listener);
            }

        });
    }

    public static void getStoreDetail(final Context context, String id, final OnNetResponseListener listener){
        long timeStamp= System.currentTimeMillis();
        String sign= generateMd5Str("","id"+id,timeStamp,APP_KEY,"");
        StringBuilder build=new StringBuilder("access_token=");
        build.append("&timestamp=").append(timeStamp).append("&app_key="+APP_KEY)
                .append("&client=").append("&id=").append(id).append("&sign=").append(sign);
//        if (isDebug)
//            Log.e("changmai","jsonurl:"+STORE_LIST_DETAIL+build.toString()+">>>");

        OkGo.get(STORE_LIST_DETAIL+build.toString()).execute(new StringNetCallback(context) {
            @Override
            public void onSuccess(String s, Call call, Response response) {


                paraJson(context,s,listener);
            }
        });
    }

    public static void getFindList(final Context context, final OnNetResponseListener listener){
        long timeStamp= System.currentTimeMillis();
        String sign= generateMd5Str("","",timeStamp,APP_KEY,"");
        StringBuilder build=new StringBuilder("access_token=");
        build.append("&timestamp=").append(timeStamp).append("&app_key="+APP_KEY)
                .append("&client=").append("&sign=").append(sign);
//        if (isDebug)
//            Log.e("changmai","jsonurl:"+FIND_LIST+build.toString()+">>>");

        OkGo.get(FIND_LIST+build.toString()).execute(new StringNetCallback(context) {
            @Override
            public void onSuccess(String s, Call call, Response response) {


                paraJson(context,s,listener);
            }
        });
    }

    public static void getVerifyCode(final Context context, String phone, final OnNetResponseListener listener){
        long timeStamp= System.currentTimeMillis();
        String sign= generateMd5Str("","phone"+phone,timeStamp,APP_KEY,"");
        StringBuilder build=new StringBuilder("access_token=");
        build.append("&timestamp=").append(timeStamp).append("&app_key="+APP_KEY)
                .append("&client=").append("&phone=").append(phone).append("&sign=").append(sign);
//        if (isDebug)
//            Log.e("changmai","jsonurl:"+GET_VERIFY_CODE+build.toString()+">>>");


        OkGo.get(GET_VERIFY_CODE+build.toString()).execute(new StringNetCallback(context) {
            @Override
            public void onSuccess(String s, Call call, Response response) {


                paraJson(context,s,listener);
            }
        });
    }

    public static void regist(final Context context, String phone, String password, String confirm_password, String messageNumber, final OnNetResponseListener listener){
        Map<String, String> params = new HashMap<String, String>();
        params.put("phone", phone);
        params.put("password", password);
        params.put("confirm_password", confirm_password);
        params.put("messageNumber", messageNumber);
        long timeStamp= System.currentTimeMillis();
        String sign= generateMd5Str("","",timeStamp,APP_KEY,"");
        StringBuilder build=new StringBuilder("access_token=");
        build.append("&timestamp=").append(timeStamp).append("&app_key="+APP_KEY)
                .append("&client=").append("&sign=").append(sign);

//        Log.e("changmai","jsonurl:"+REGISTER+build.toString()+">>>");

        post(REGISTER+build.toString()).params(params).execute(new StringNetCallback(context) {
            @Override
            public void onSuccess(String s, Call call, Response response) {


                paraJson(context,s,listener);
            }
        });
    }

    public static void login(final Context context, String phone, String password, final OnNetResponseListener listener){
        Map<String, String> params = new HashMap<String, String>();
        params.put("phone", phone);
        params.put("password", password);

        long timeStamp= System.currentTimeMillis();
        String sign= generateMd5Str("","",timeStamp,APP_KEY,"");
        StringBuilder build=new StringBuilder("access_token=");
        build.append("&timestamp=").append(timeStamp).append("&app_key="+APP_KEY)
                .append("&client=").append("&sign=").append(sign);
//        if (isDebug)
//            Log.e("changmai","jsonurl:"+LOGIN+build.toString()+">>>"+params.toString());

        post(LOGIN+build.toString()).params(params).execute(new StringNetCallback(context) {
            @Override
            public void onSuccess(String s, Call call, Response response) {


                paraJson(context,s,listener);
            }
        });
    }


    public static void getUserInfo(final Context context, String access_token, String client, final OnNetResponseListener listener){


        long timeStamp= System.currentTimeMillis();
        String sign= generateMd5Str(access_token,"",timeStamp,APP_KEY,client);
        StringBuilder build=new StringBuilder("access_token=");
        build.append(access_token).append("&timestamp=").append(timeStamp).append("&app_key="+APP_KEY)
                .append("&client=").append(client).append("&sign=").append(sign);
//        if (isDebug)
//            Log.e("changmai","jsonurl:"+USER_INFO+build.toString()+">>>");

        post(USER_INFO+build.toString()).execute(new StringNetCallback(context) {
            @Override
            public void onSuccess(String s, Call call, Response response) {


                paraJson(context,s,listener);
            }
        });
    }

    public static void addComment(final Context context, String access_token,
                                  String client, int id, String content, int star, ArrayList<File> mFileList,
                                  final OnNetResponseListener listener){
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", ""+id);
        params.put("content", content);
        params.put("star", ""+star);


        long timeStamp= System.currentTimeMillis();
        String sign= generateMd5Str(access_token,"",timeStamp,APP_KEY,client);
        StringBuilder build=new StringBuilder("access_token=");
        build.append(access_token).append("&timestamp=").append(timeStamp).append("&app_key="+APP_KEY)
                .append("&client=").append(client).append("&sign=").append(sign);
//        if (isDebug)
//            Log.e("changmai","jsonurl:"+ADD_COMMENT+build.toString()+">>>");

        PostRequest request = OkGo.post(ADD_COMMENT + build.toString()).params(params);
        if(mFileList!=null){
            for (int i = 0; i <mFileList.size(); i++) {
                request.params("file"+i, mFileList.get(i));
            }

        }
        request.execute(new StringNetCallback(context) {
            @Override
            public void onSuccess(String s, Call call, Response response) {


                paraJson(context,s,listener);
            }
        });
    }


    public static void getMyMessage(final Context context, String access_token, String client, final OnNetResponseListener listener){


        long timeStamp= System.currentTimeMillis();
        String sign= generateMd5Str(access_token,"",timeStamp,APP_KEY,client);
        StringBuilder build=new StringBuilder("access_token=");
        build.append(access_token).append("&timestamp=").append(timeStamp).append("&app_key="+APP_KEY)
                .append("&client=").append(client).append("&sign=").append(sign);
//        if (isDebug)
//            Log.e("changmai","jsonurl:"+MY_MESSAGE+build.toString()+">>>");

        post(MY_MESSAGE+build.toString()).execute(new StringNetCallback(context) {
            @Override
            public void onSuccess(String s, Call call, Response response) {


                paraJson(context,s,listener);
            }
        });
    }

    public static void getMyCollection(final Context context, String access_token, String client, final OnNetResponseListener listener){


        long timeStamp= System.currentTimeMillis();
        String sign= generateMd5Str(access_token,"",timeStamp,APP_KEY,client);
        StringBuilder build=new StringBuilder("access_token=");
        build.append(access_token).append("&timestamp=").append(timeStamp).append("&app_key="+APP_KEY)
                .append("&client=").append(client).append("&sign=").append(sign);
//        if (isDebug)
//            Log.e("changmai","jsonurl:"+MY_COLLECTION+build.toString()+">>>");

        post(MY_COLLECTION+build.toString()).execute(new StringNetCallback(context) {
            @Override
            public void onSuccess(String s, Call call, Response response) {


                paraJson(context,s,listener);
            }
        });
    }
    public static void collectGood(final Context context, String access_token, String client, int id, int type, final OnNetResponseListener listener){


        long timeStamp= System.currentTimeMillis();
        String sign= generateMd5Str(access_token,"id"+id+"type"+type,timeStamp,APP_KEY,client);
        StringBuilder build=new StringBuilder("access_token=");
        build.append(access_token).append("&timestamp=").append(timeStamp).append("&app_key="+APP_KEY)
                .append("&client=").append(client).append("&id=").append(id).append("&type=")
                .append(type).append("&sign=").append(sign);
//        if (isDebug)
//            Log.e("changmai","jsonurl:"+COLLECT_GOOD+build.toString()+">>>");

        OkGo.get(COLLECT_GOOD+build.toString()).execute(new StringNetCallback(context) {
            @Override
            public void onSuccess(String s, Call call, Response response) {


                paraJson(context,s,listener);
            }
        });
    }

    public static void addRefund(final Context context, String access_token,
                                 String client, String id, int type,
                                 final OnNetResponseListener listener){


        long timeStamp= System.currentTimeMillis();
        StringBuilder build=new StringBuilder("access_token=");
//        if (isDebug) {
//            Log.e("minrui", "access_token=" + access_token);
//            Log.e("minrui", "client=" + client);
//            Log.e("minrui", "id=" + id);
//            Log.e("minrui", "type=" + type);
//        }
        if(type==1){
        String sign= generateMd5Str(access_token,"id"+id+"type"+type,timeStamp,APP_KEY,client);

        build.append(access_token).append("&timestamp=").append(timeStamp).append("&app_key="+APP_KEY)
                .append("&client=").append(client).append("&id=").append(id).append("&type=")
                .append(type).append("&sign=").append(sign);
        }else if(type==2){

            String sign= generateMd5Str(access_token,"id"+id+"lat"+ MyApplication.lat+
                    "lng"+MyApplication.lng+"type"+type,timeStamp,APP_KEY,client);

            build.append(access_token).append("&timestamp=").append(timeStamp).append("&app_key="+APP_KEY)
                    .append("&client=").append(client).append("&id=").append(id).
                    append("&lat=").append(MyApplication.lat).append("&lng=").append(MyApplication.lng).
                    append("&type=").append(type).append("&sign=").append(sign);
        }
//        if (isDebug)
//            Log.e("changmai","jsonurl:"+ADD_REFUND+build.toString()+">>>");

        OkGo.get(ADD_REFUND+build.toString()).execute(new StringNetCallback(context) {
            @Override
            public void onSuccess(String s, Call call, Response response) {


                paraJson(context,s,listener);
            }
        });
    }


    public static void getGoodList(final Context context, String access_token, String client, String key, int is_best, int is_new,
                                   int is_hot, int is_member, String msubcate, int orderby, int order, int page, final OnNetResponseListener listener){

StringBuilder parm =new StringBuilder();
        if(is_best!=-1){
            parm.append("is_best").append(is_best);
        }

        if(is_hot!=-1){
            parm.append("is_hot").append(is_hot);
        }
        if(is_member!=-1){
            parm.append("is_member").append(is_member);
        }
        if(is_new!=-1){
            parm.append("is_new").append(is_new);
        }
        if(!TextUtils.isEmpty(key)){
            parm.append("key").append(key);
        }

        parm.append("order").append(order);
        parm.append("orderby").append(orderby);
        parm.append("page").append(page);
        if(!TextUtils.isEmpty(msubcate)){
            parm.append("subcate").append(msubcate);
        }
        long timeStamp= System.currentTimeMillis();
        String sign= generateMd5Str(access_token,parm.toString(),timeStamp,APP_KEY,client);
        StringBuilder build=new StringBuilder("access_token=");
        build.append(access_token).append("&timestamp=").append(timeStamp).append("&app_key="+APP_KEY)
                .append("&client=").append(client);

        if(is_best!=-1){
            build.append("&is_best=").append(is_best);
        }
        if(!TextUtils.isEmpty(key)){
            build.append("&key=").append(key);
        }
        if(is_hot!=-1){
            build.append("&is_hot=").append(is_hot);
        }
        if(is_member!=-1){
            build.append("&is_member=").append(is_member);
        }
        if(is_new!=-1){
            build.append("&is_new=").append(is_new);
        }
        if(!TextUtils.isEmpty(msubcate)){
            build.append("&subcate=").append(msubcate);
        }
        build.append("&order=").append(order);
        build.append("&orderby=").append(orderby);
        build.append("&page=").append(page);
        build.append("&sign=").append(sign);

//        if (isDebug)
//            Log.e("changmai","jsonurl:"+GOOD_LIST+build.toString()+">>>");

        OkGo.get(GOOD_LIST+build.toString()).execute(new StringNetCallback(context) {
            @Override
            public void onSuccess(String s, Call call, Response response) {


                paraJson(context,s,listener);
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                netError(context,e.getMessage(),listener);
            }
        });
    }



    public static void getGoodDetail(final Context context, String access_token, String client, int id, String store_id, final OnNetResponseListener listener){


        long timeStamp= System.currentTimeMillis();
        StringBuilder build=new StringBuilder("access_token=");
        if(TextUtils.isEmpty(access_token)){
        String sign= generateMd5Str("","id"+id+"store_id"+store_id,timeStamp,APP_KEY,"");

        build.append("&timestamp=").append(timeStamp).append("&app_key="+APP_KEY)
                .append("&client=").append("&id=").append(id).append("&store_id=").append(store_id)
                .append("&sign=").append(sign);
        }else{
            String sign= generateMd5Str(access_token,"id"+id+"store_id"+store_id,timeStamp,APP_KEY,client);

            build.append(access_token).append("&timestamp=").append(timeStamp).append("&app_key="+APP_KEY)
                    .append("&client=").append(client).append("&id=").append(id).append("&store_id=").append(store_id)
                    .append("&sign=").append(sign);
        }
//        if (isDebug)
//            Log.e("changmai","jsonurl:"+GOOD_DETAIL+build.toString()+">>>");

        OkGo.get(GOOD_DETAIL+build.toString()).execute(new StringNetCallback(context) {
            @Override
            public void onSuccess(String s, Call call, Response response) {


                paraJson(context,s,listener);
            }
        });
    }


    public static void delMessage(final Context context, String access_token, String client, String id, final OnNetResponseListener listener){
        long timeStamp= System.currentTimeMillis();
        String sign= generateMd5Str(access_token,"id"+id,timeStamp,APP_KEY,client);
        StringBuilder build=new StringBuilder("access_token=");
        build.append(access_token).append("&timestamp=").append(timeStamp).append("&app_key="+APP_KEY)
                .append("&client=").append(client).append("&id=").append(id).append("&sign=").append(sign);
//        if (isDebug)
//            Log.e("changmai","jsonurl:"+DEL_MESSAGE+build.toString()+">>>");

        OkGo.get(DEL_MESSAGE+build.toString()).execute(new StringNetCallback(context) {
            @Override
            public void onSuccess(String s, Call call, Response response) {


                paraJson(context,s,listener);
            }
        });
    }
    public static void getMyAccount(final Context context, String access_token, String client, final OnNetResponseListener listener){


        long timeStamp= System.currentTimeMillis();
        String sign= generateMd5Str(access_token,"",timeStamp,APP_KEY,client);
        StringBuilder build=new StringBuilder("access_token=");
        build.append(access_token).append("&timestamp=").append(timeStamp).append("&app_key="+APP_KEY)
                .append("&client=").append(client).append("&sign=").append(sign);
//        if (isDebug)
//            Log.e("changmai","jsonurl:"+MY_ACCOUNT+build.toString()+">>>");

        post(MY_ACCOUNT+build.toString()).execute(new StringNetCallback(context) {
            @Override
            public void onSuccess(String s, Call call, Response response) {


                paraJson(context,s,listener);
            }
        });
    }

    public static void logout(final Context context, String access_token, String client, final OnNetResponseListener listener){


        long timeStamp= System.currentTimeMillis();
        String sign= generateMd5Str(access_token,"",timeStamp,APP_KEY,client);
        StringBuilder build=new StringBuilder("access_token=");
        build.append(access_token).append("&timestamp=").append(timeStamp).append("&app_key="+APP_KEY)
                .append("&client=").append(client).append("&sign=").append(sign);
//        if (isDebug)
//            Log.e("changmai","jsonurl:"+LOGOUT+build.toString()+">>>");

        post(LOGOUT+build.toString()).execute(new StringNetCallback(context) {
            @Override
            public void onSuccess(String s, Call call, Response response) {


                paraJson(context,s,listener);
            }
        });
    }

    public static void getAddressList(final Context context, String access_token, String client, final OnNetResponseListener listener){


        long timeStamp= System.currentTimeMillis();
        String sign= generateMd5Str(access_token,"",timeStamp,APP_KEY,client);
        StringBuilder build=new StringBuilder("access_token=");
        build.append(access_token).append("&timestamp=").append(timeStamp).append("&app_key="+APP_KEY)
                .append("&client=").append(client).append("&sign=").append(sign);
//        if (isDebug)
//            Log.e("changmai","jsonurl:"+ADDRESS_LIST+build.toString()+">>>");

        post(ADDRESS_LIST+build.toString()).execute(new StringNetCallback(context) {
            @Override
            public void onSuccess(String s, Call call, Response response) {


                paraJson(context,s,listener);
            }
        });
    }

    public static void addAddress(final Context context, String access_token, String client, String phone, String name, String address,
                                  String area, String id_card, final OnNetResponseListener listener){
        Map<String, String> params = new HashMap<String, String>();
        params.put("phone", phone);
        params.put("name", name);
        params.put("address", address);
        params.put("area", area);
        params.put("id_card", id_card);
        long timeStamp= System.currentTimeMillis();
        String sign= generateMd5Str(access_token,"",timeStamp,APP_KEY,client);
        StringBuilder build=new StringBuilder("access_token=");
        build.append(access_token).append("&timestamp=").append(timeStamp).append("&app_key="+APP_KEY)
                .append("&client=").append(client).append("&sign=").append(sign);
//        if (isDebug)
//            Log.e("changmai","jsonurl:"+ADD_ADDRESS+build.toString()+">>>");

        post(ADD_ADDRESS+build.toString()).params(params).execute(new StringNetCallback(context) {
            @Override
            public void onSuccess(String s, Call call, Response response) {


                paraJson(context,s,listener) ;
            }
        });
    }

    public static void delRefund(final Context context, String access_token, String client,
                                 String id, String ids,
                                 String remark, ArrayList<File> files, int type, int refund_good_type,
                                 String store_id, final OnNetResponseListener listener){
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        params.put("type", ""+type);
        params.put("refund_good_type", ""+refund_good_type);
        params.put("store_id", store_id);
        params.put("ids",ids);
        params.put("remark",remark);

        long timeStamp= System.currentTimeMillis();
        String sign= generateMd5Str(access_token,"",timeStamp,APP_KEY,client);
        StringBuilder build=new StringBuilder("access_token=");
        build.append(access_token).append("&timestamp=").append(timeStamp).append("&app_key="+APP_KEY)
                .append("&client=").append(client).append("&sign=").append(sign);
//        if (isDebug)
//            Log.e("changmai","jsonurl:"+DEL_REFUND+build.toString()+">>>");

        PostRequest postRequest=  post(DEL_REFUND+build.toString()).params(params);
        for (int i = 0; i < files.size(); i++) {
            postRequest.params("file"+i,files.get(i));
        }
        postRequest.execute(new StringNetCallback(context) {
            @Override
            public void onSuccess(String s, Call call, Response response) {


                paraJson(context,s,listener);
            }
        });
    }


    public static void editAddress(final Context context, String access_token, String id, String client, String phone, String name, String address,
                                   String area, String id_card, final OnNetResponseListener listener){
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        params.put("phone", phone);
        params.put("name", name);
        params.put("address", address);
        params.put("area", area);
        params.put("id_card", id_card);
        long timeStamp= System.currentTimeMillis();
        String sign= generateMd5Str(access_token,"",timeStamp,APP_KEY,client);
        StringBuilder build=new StringBuilder("access_token=");
        build.append(access_token).append("&timestamp=").append(timeStamp).append("&app_key="+APP_KEY)
                .append("&client=").append(client).append("&sign=").append(sign);
//        if (isDebug)
//            Log.e("changmai","jsonurl:"+EDIT_ADDRESS+build.toString()+">>>");

        post(EDIT_ADDRESS+build.toString()).params(params).execute(new StringNetCallback(context) {
            @Override
            public void onSuccess(String s, Call call, Response response) {


                paraJson(context,s,listener);
            }
        });
    }

    public static void changeDefaultAddress(final Context context, String access_token, String id, String client,
                                            final OnNetResponseListener listener){
        long timeStamp= System.currentTimeMillis();
        String sign= generateMd5Str(access_token,"id"+id,timeStamp,APP_KEY,client);
        StringBuilder build=new StringBuilder("access_token=");
        build.append(access_token).append("&timestamp=").append(timeStamp).append("&app_key="+APP_KEY)
                .append("&client=").append(client).append("&id=").append(id).append("&sign=").append(sign);
//        if (isDebug)
//            Log.e("changmai","jsonurl:"+CHANGE_DEFAULT+build.toString()+">>>");

        OkGo.get(CHANGE_DEFAULT+build.toString()).execute(new StringNetCallback(context) {
            @Override
            public void onSuccess(String s, Call call, Response response) {


                paraJson(context,s,listener);
            }
        });
    }


    public static void delAddress(final Context context, String access_token, String id, String client,
                                  final OnNetResponseListener listener){
        long timeStamp= System.currentTimeMillis();
        String sign= generateMd5Str(access_token,"id"+id,timeStamp,APP_KEY,client);
        StringBuilder build=new StringBuilder("access_token=");
        build.append(access_token).append("&timestamp=").append(timeStamp).append("&app_key="+APP_KEY)
                .append("&client=").append(client).append("&id=").append(id).append("&sign=").append(sign);
//        if (isDebug)
//            Log.e("changmai","jsonurl:"+DEL_ADDRESS+build.toString()+">>>");


        OkGo.get(DEL_ADDRESS+build.toString()).execute(new StringNetCallback(context) {
            @Override
            public void onSuccess(String s, Call call, Response response) {


                paraJson(context,s,listener);
            }
        });
    }


    public static void search(final Context context, final OnNetResponseListener listener){
        long timeStamp= System.currentTimeMillis();
        String sign= generateMd5Str("","",timeStamp,APP_KEY,"");
        StringBuilder build=new StringBuilder("access_token=");
        build.append("&timestamp=").append(timeStamp).append("&app_key="+APP_KEY)
                .append("&client=").append("&sign=").append(sign);
//        if (isDebug)
//            Log.e("changmai","jsonurl:"+SERACH+build.toString()+">>>");

        OkGo.get(SERACH+build.toString()).execute(new StringNetCallback(context) {
            @Override
            public void onSuccess(String s, Call call, Response response) {


                paraJson(context,s,listener);
            }
        });
    }


    public static void getShopCarData(final Context context, String access_token, String id , String client,
                                      final OnNetResponseListener listener){
        long timeStamp= System.currentTimeMillis();
        String sign= generateMd5Str(access_token,"store_id"+id,timeStamp,APP_KEY,client);
        StringBuilder build=new StringBuilder("access_token=");
        build.append(access_token).append("&timestamp=").append(timeStamp).append("&app_key="+APP_KEY)
                .append("&client=").append(client).append("&store_id=").append(id)
                .append("&sign=").append(sign);
//        if (isDebug)
//            Log.e("changmai","jsonurl:"+GET_SHOPCAR+build.toString()+">>>");

        OkGo.get(GET_SHOPCAR+build.toString()).execute(new StringNetCallback(context) {
            @Override
            public void onSuccess(String s, Call call, Response response) {


                paraJson(context,s,listener);
            }
        });
    }


    public static void addToShopCar(final Context context, String access_token, int id, int num, String client,
                                    final OnNetResponseListener listener){
        long timeStamp= System.currentTimeMillis();
        String sign= generateMd5Str(access_token,"id"+id+"num"+num,timeStamp,APP_KEY,client);
        StringBuilder build=new StringBuilder("access_token=");
        build.append(access_token).append("&timestamp=").append(timeStamp).append("&app_key="+APP_KEY)
                .append("&client=").append(client).append("&id=").append(id)
                .append("&num=").append(num).append("&sign=").append(sign);
//        if (isDebug)
//            Log.e("changmai","jsonurl:"+ADD_TO_SHOPCAR+build.toString()+">>>");

        OkGo.get(ADD_TO_SHOPCAR+build.toString()).execute(new StringNetCallback(context) {
            @Override
            public void onSuccess(String s, Call call, Response response) {


                paraJson(context,s,listener);
            }
        });
    }


    public static void changeGoodNumInShopcar(final Context context, String access_token, String id, int num, String client,
                                              final OnNetResponseListener listener){
        long timeStamp= System.currentTimeMillis();
        String sign= generateMd5Str(access_token,"id"+id+"num"+num,timeStamp,APP_KEY,client);
        StringBuilder build=new StringBuilder("access_token=");
        build.append(access_token).append("&timestamp=").append(timeStamp).append("&app_key="+APP_KEY)
                .append("&client=").append(client).append("&id=").append(id)
                .append("&num=").append(num).append("&sign=").append(sign);
//        if (isDebug)
//            Log.e("changmai","jsonurl:"+CHANG_GOODNUM_IN_SHOPCAR+build.toString()+">>>");


        OkGo.get(CHANG_GOODNUM_IN_SHOPCAR+build.toString()).execute(new StringNetCallback(context) {
            @Override
            public void onSuccess(String s, Call call, Response response) {


                paraJson(context,s,listener);
            }
        });
    }

    public static void delGoodInShopcar(final Context context, String access_token, String client,
                                        String ids, final OnNetResponseListener listener){
        Map<String, String> params = new HashMap<String, String>();
        params.put("ids", ids);

        long timeStamp= System.currentTimeMillis();
        String sign= generateMd5Str(access_token,"",timeStamp,APP_KEY,client);
        StringBuilder build=new StringBuilder("access_token=");
        build.append(access_token).append("&timestamp=").append(timeStamp).append("&app_key="+APP_KEY)
                .append("&client=").append(client).append("&sign=").append(sign);
//        if (isDebug)
//            Log.e("changmai","jsonurl:"+DEL_SHOPCAR+build.toString()+">>>");

        post(DEL_SHOPCAR+build.toString()).params(params).execute(new StringNetCallback(context) {
            @Override
            public void onSuccess(String s, Call call, Response response) {


                paraJson(context,s,listener);
            }
        });
    }

    public static void confirmOrder(final Context context, String access_token, String ids, String store_id, String shipping_id, String client,
                                    final OnNetResponseListener listener){
        long timeStamp= System.currentTimeMillis();
        StringBuilder build = new StringBuilder("access_token=");
        if(!shipping_id.equals("-1")) {
            String sign = generateMd5Str(access_token, "ids" + ids +"shipping_id" + shipping_id +"store_id" + store_id, timeStamp, APP_KEY, client);

            build.append(access_token).append("&timestamp=").append(timeStamp).append("&app_key=" + APP_KEY)
                    .append("&client=").append(client).append("&ids=").append(ids)
                    .append("&shipping_id=").append(shipping_id).append("&store_id=")
                    .append(store_id).append("&sign=").append(sign);
        }else{
            String sign = generateMd5Str(access_token, "ids" + ids  +"store_id" + store_id, timeStamp, APP_KEY, client);

            build.append(access_token).append("&timestamp=").append(timeStamp).append("&app_key=" + APP_KEY)
                    .append("&client=").append(client).append("&ids=").append(ids)
                    .append("&store_id=").append(store_id).append("&sign=").append(sign);
        }
//        if (isDebug)
//            Log.e("changmai","jsonurl:"+CONFIRM_ORDER+build.toString()+">>>");

        OkGo.get(CONFIRM_ORDER+build.toString()).execute(new StringNetCallback(context) {
            @Override
            public void onSuccess(String s, Call call, Response response) {


                paraJson(context,s,listener);
            }
        });
    }


    public static void submitOrder(final Context context, String access_token, String client,
                                   String ids, String store_id, String shipping_id, String pay_amount,
                                   String score, int shipping_type, String remark, String rid, int type,
                                   final OnNetResponseListener listener){
        Map<String, String> params = new HashMap<String, String>();
        params.put("ids", ids);
        params.put("store_id", store_id);
        params.put("shipping_id", shipping_id);
//        params.put("pay_amount", pay_amount);
        params.put("type", ""+type);
        params.put("score", score);
        params.put("shipping_type", ""+shipping_type);
        params.put("remark", remark);
//        params.put("rid", rid);

        long timeStamp= System.currentTimeMillis();
        String sign= generateMd5Str(access_token,"",timeStamp,APP_KEY,client);
        StringBuilder build=new StringBuilder("access_token=");
        build.append(access_token).append("&timestamp=").append(timeStamp).append("&app_key="+APP_KEY)
                .append("&client=").append(client).append("&sign=").append(sign);
//        if (isDebug)
//            Log.e("changmai","jsonurl:"+SUBMIT_ORDER+build.toString()+">>>");

        post(SUBMIT_ORDER+build.toString()).params(params).execute(new StringNetCallback(context) {
            @Override
            public void onSuccess(String s, Call call, Response response) {


                paraJson(context,s,listener);
            }
            });
    }


    public static void getMyOrder(final Context context, String access_token, String client, int st, final OnNetResponseListener listener){
        long timeStamp= System.currentTimeMillis();
        StringBuilder build = new StringBuilder("access_token=");
        if(st==0) {
            String sign = generateMd5Str(access_token, "", timeStamp, APP_KEY, client);

            build.append(access_token).append("&timestamp=").append(timeStamp).append("&app_key=" + APP_KEY)
                    .append("&client=").append(client).append("&sign=").append(sign);
        }else{
            String sign = generateMd5Str(access_token, "st"+st, timeStamp, APP_KEY, client);

            build.append(access_token).append("&timestamp=").append(timeStamp).append("&app_key=" + APP_KEY)
                    .append("&client=").append(client).append("&st=").append(st).append("&sign=").append(sign);
        }
//        if (isDebug)
//            Log.e("changmai","jsonurl:"+MY_ORDER+build.toString()+">>>");

        OkGo.get(MY_ORDER+build.toString()).execute(new StringNetCallback(context) {
            @Override
            public void onSuccess(String s, Call call, Response response) {


                paraJson(context,s,listener);
            }
        });
    }

    public static void acceptConfirm(final Context context, String access_token, String client, String id, final OnNetResponseListener listener){
        long timeStamp= System.currentTimeMillis();
        StringBuilder build = new StringBuilder("access_token=");

            String sign = generateMd5Str(access_token, "id"+id, timeStamp, APP_KEY, client);

            build.append(access_token).append("&timestamp=").append(timeStamp).append("&app_key=" + APP_KEY)
                    .append("&client=").append(client).append("&id=").append(id).append("&sign=").append(sign);
//        if (isDebug)
//            Log.e("changmai","jsonurl:"+ACCEPT_CONFIRM+build.toString()+">>>");

        OkGo.get(ACCEPT_CONFIRM+build.toString()).execute(new StringNetCallback(context) {
            @Override
            public void onSuccess(String s, Call call, Response response) {


                paraJson(context,s,listener);
            }
        });
    }

    public static void cancelOrder(final Context context, String access_token, String client, String id, final OnNetResponseListener listener){
        long timeStamp= System.currentTimeMillis();
        StringBuilder build = new StringBuilder("access_token=");

        String sign = generateMd5Str(access_token, "id"+id, timeStamp, APP_KEY, client);

        build.append(access_token).append("&timestamp=").append(timeStamp).append("&app_key=" + APP_KEY)
                .append("&client=").append(client).append("&id=").append(id).append("&sign=").append(sign);
//        if (isDebug)
//            Log.e("changmai","jsonurl:"+CANCEL_ORDER+build.toString()+">>>");

        OkGo.get(CANCEL_ORDER+build.toString()).execute(new StringNetCallback(context) {
            @Override
            public void onSuccess(String s, Call call, Response response) {


                paraJson(context,s,listener);
            }
        });
    }


    public static void delOrder(final Context context, String access_token, String client, String id, final OnNetResponseListener listener){
        long timeStamp= System.currentTimeMillis();
        StringBuilder build = new StringBuilder("access_token=");

        String sign = generateMd5Str(access_token, "id"+id, timeStamp, APP_KEY, client);

        build.append(access_token).append("&timestamp=").append(timeStamp).append("&app_key=" + APP_KEY)
                .append("&client=").append(client).append("&id=").append(id).append("&sign=").append(sign);
//        if (isDebug)
//            Log.e("changmai","jsonurl:"+DEL_ORDER+build.toString()+">>>");

        OkGo.get(DEL_ORDER+build.toString()).execute(new StringNetCallback(context) {
            @Override
            public void onSuccess(String s, Call call, Response response) {


                paraJson(context,s,listener);
            }
        });
    }

    public static void getOrderDetail(final Context context, String access_token, String client, String id, final OnNetResponseListener listener){
        long timeStamp= System.currentTimeMillis();
        StringBuilder build = new StringBuilder("access_token=");

        String sign = generateMd5Str(access_token, "id"+id, timeStamp, APP_KEY, client);

        build.append(access_token).append("&timestamp=").append(timeStamp).append("&app_key=" + APP_KEY)
                .append("&client=").append(client).append("&id=").append(id).append("&sign=").append(sign);
//        if (isDebug)
//            Log.e("changmai","jsonurl:"+ORDER_DETAIL+build.toString()+">>>");

        OkGo.get(ORDER_DETAIL+build.toString()).execute(new StringNetCallback(context) {
            @Override
            public void onSuccess(String s, Call call, Response response) {


                paraJson(context,s,listener);
            }
        });
    }

    public static void getExpressInfo(final Context context, String access_token, String client, String id, final OnNetResponseListener listener){
        long timeStamp= System.currentTimeMillis();
        StringBuilder build = new StringBuilder("access_token=");

        String sign = generateMd5Str(access_token, "id"+id, timeStamp, APP_KEY, client);

        build.append(access_token).append("&timestamp=").append(timeStamp).append("&app_key=" + APP_KEY)
                .append("&client=").append(client).append("&id=").append(id).append("&sign=").append(sign);
//        if (isDebug)
//            Log.e("changmai","jsonurl:"+EXPRESS_INFO+build.toString()+">>>");

        OkGo.get(EXPRESS_INFO+build.toString()).execute(new StringNetCallback(context) {
            @Override
            public void onSuccess(String s, Call call, Response response) {


                paraJson(context,s,listener);
            }
        });
    }

    public static void updateNickName(final Context context, String access_token, String client,
                                      String name, final OnNetResponseListener listener){
        Map<String, String> params = new HashMap<String, String>();
        params.put("name", name);

        long timeStamp= System.currentTimeMillis();
        String sign= generateMd5Str(access_token,"",timeStamp,APP_KEY,client);
        StringBuilder build=new StringBuilder("access_token=");
        build.append(access_token).append("&timestamp=").append(timeStamp).append("&app_key="+APP_KEY)
                .append("&client=").append(client).append("&sign=").append(sign);
//        if (isDebug)
//            Log.e("changmai","jsonurl:"+MODIFY_NAME+build.toString()+">>>");

        post(MODIFY_NAME+build.toString()).params(params).execute(new StringNetCallback(context) {
            @Override
            public void onSuccess(String s, Call call, Response response) {


                paraJson(context,s,listener);
            }
        });
    }

    public static void updateSex(final Context context, String access_token, String client,
                                 int sex, final OnNetResponseListener listener){
        Map<String, String> params = new HashMap<String, String>();
        params.put("sex", ""+sex);

        long timeStamp= System.currentTimeMillis();
        String sign= generateMd5Str(access_token,"",timeStamp,APP_KEY,client);
        StringBuilder build=new StringBuilder("access_token=");
        build.append(access_token).append("&timestamp=").append(timeStamp).append("&app_key="+APP_KEY)
                .append("&client=").append(client).append("&sign=").append(sign);
//        if (isDebug)
//            Log.e("changmai","jsonurl:"+MODIFY_SEX+build.toString()+">>>");

        post(MODIFY_SEX+build.toString()).params(params).execute(new StringNetCallback(context) {
            @Override
            public void onSuccess(String s, Call call, Response response) {


                paraJson(context,s,listener);
            }
        });
    }

    public static void chargeFee(final Context context, String access_token, String client, float score, int type,
                                 final OnNetResponseListener listener){
        Map<String, String> params = new HashMap<String, String>();
        params.put("score", ""+score);
        params.put("type", ""+type);

        long timeStamp= System.currentTimeMillis();
        String sign= generateMd5Str(access_token,"",timeStamp,APP_KEY,client);
        StringBuilder build=new StringBuilder("access_token=");
        build.append(access_token).append("&timestamp=").append(timeStamp).append("&app_key="+APP_KEY)
                .append("&client=").append(client).append("&sign=").append(sign);
//        if (isDebug)
//            Log.e("changmai","jsonurl:"+CHARGE_FEE+build.toString()+">>>");

        post(CHARGE_FEE+build.toString()).params(params).execute(new StringNetCallback(context) {
            @Override
            public void onSuccess(String s, Call call, Response response) {


                paraJson(context,s,listener);
            }
        });
    }


    public static void continuePay(final Context context, String access_token, String client, String id, int type, final OnNetResponseListener listener){


        long timeStamp= System.currentTimeMillis();
        String sign= generateMd5Str(access_token,"",timeStamp,APP_KEY,client);
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        params.put("type", ""+type);



        StringBuilder build=new StringBuilder("access_token=");
        build.append(access_token).append("&timestamp=").append(timeStamp).append("&app_key="+APP_KEY)
                .append("&client=").append(client).append("&sign=").append(sign);
//        if (isDebug)
//            Log.e("changmai","jsonurl:"+CONTINUE_PAY+build.toString()+">>>");

        OkGo.post(CONTINUE_PAY+build.toString()).params(params).execute(new StringNetCallback(context) {
            @Override
            public void onSuccess(String s, Call call, Response response) {


                paraJson(context,s,listener);
            }
        });

    }

    public static void weixinLogin(final Context context, String headimgurl, String nickname,
                                   String openid, int sex, String unionid
            , final OnNetResponseListener listener){

        Map<String, String> params = new HashMap<String, String>();
        params.put("headimgurl", headimgurl);
        params.put("nickname", nickname);
        params.put("openid", openid);
        params.put("sex", ""+sex);
        params.put("unionid", unionid);
        long timeStamp= System.currentTimeMillis();
        String sign= generateMd5Str("","",timeStamp,APP_KEY,"");
        StringBuilder build=new StringBuilder("access_token=");
        build.append("&timestamp=").append(timeStamp).append("&app_key="+APP_KEY)
                .append("&client=").append("&sign=").append(sign);
//        if (isDebug)
//            Log.e("changmai","jsonurl:"+WEIXIN_LOGIN+build.toString()+">>>");

        post(WEIXIN_LOGIN+build.toString()).params(params).execute(new StringNetCallback(context) {
            @Override
            public void onSuccess(String s, Call call, Response response) {


                paraJson(context,s,listener);
            }
        });
    }

    public static void pwdSendMsg(final Context context, String phone, final OnNetResponseListener listener){
        Map<String, String> params = new HashMap<>();
        params.put("phone",phone);
        StringBuilder build=new StringBuilder("content-type="+CONTENT_TYPE);
        build.append("&phone=").append(phone);

//        Log.e("changmai","jsonurl:"+FIND_PWD_SENDMSG+build.toString()+">>>");
        post(FIND_PWD_SENDMSG+build.toString()).params(params).execute(new StringNetCallback(context) {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                paraJson(context,s,listener);
            }
        });
    }

    public static void dealPwd(final Context context, String password, String phone, String messageNumber, final OnNetResponseListener listener){
        Map<String, String> params = new HashMap<>();
        params.put("password",password);
        params.put("phone",phone);
        params.put("messageNumber",messageNumber);
        StringBuilder build=new StringBuilder("content-type="+CONTENT_TYPE);
        build.append("&password=").append(password).append("&phone="+phone)
                .append("&messageNumber=").append(messageNumber);

//        Log.e("changmai","jsonurl:"+DEAL_PWD+build.toString()+">>>");

        post(DEAL_PWD+build.toString()).params(params).execute(new StringNetCallback(context) {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                paraJson(context,s,listener);
            }
        });
    }

    public static void sendPwdCode(final Context context, String phone, final OnNetResponseListener listener){
        long timeStamp= System.currentTimeMillis();
        String sign= generateMd5Str("","phone"+phone,timeStamp,APP_KEY,"");
        StringBuilder build=new StringBuilder("access_token=");
        build.append("&timestamp=").append(timeStamp).append("&app_key="+APP_KEY)
                .append("&client=").append("&phone=").append(phone).append("&sign=").append(sign);
//        if (isDebug)
//            Log.e("changmai","jsonurl:"+SEND_PWD_CODE+build.toString()+">>>");


        post(SEND_PWD_CODE+build.toString()).execute(new StringNetCallback(context) {
            @Override
            public void onSuccess(String s, Call call, Response response) {


                paraJson(context,s,listener);
            }
        });
    }
    public static void dealPwdReset(final Context context, String password, String phone, String messageNumber, final OnNetResponseListener listener){
        Map<String, String> params = new HashMap<String, String>();
        params.put("phone", phone);
        params.put("password", password);
        params.put("messageNumber", messageNumber);
        long timeStamp= System.currentTimeMillis();
        String sign= generateMd5Str("","",timeStamp,APP_KEY,"");
        StringBuilder build=new StringBuilder("access_token=");
        build.append("&timestamp=").append(timeStamp).append("&app_key="+APP_KEY)
                .append("&client=").append("&sign=").append(sign);

//        Log.e("changmai","jsonurl:"+DEAL_PWD_RESET+build.toString()+">>>");

        post(DEAL_PWD_RESET+build.toString()).params(params).execute(new StringNetCallback(context) {
            @Override
            public void onSuccess(String s, Call call, Response response) {


                paraJson(context,s,listener);
            }
        });
    }

    /**
     * score						提现金额
     type_name				提现账号
     message					备注
     bank_name       银行名称
     bank_address    银行地址
     bank_username   开户人姓名
     phone   				预留手机号
     type
     1: 微信
     2: 支付宝
     3: 银联卡
     * @param context
     * @param access_token
     * @param client
     * @param listener
     */
    public static void dealExport(final Context context, String access_token, String client, String score, String type_name,
                                  String message, String bank_name, String bank_address, String bank_username,
                                  String phone, String type, final OnNetResponseListener listener){
        Map<String, String> params = new HashMap<String, String>();
        params.put("score", score);
        params.put("type_name", type_name);
        if (!TextUtils.isEmpty(message))
            params.put("message", message);
        if (!TextUtils.isEmpty(bank_name))
            params.put("bank_name", bank_name);
        if (!TextUtils.isEmpty(bank_address))
            params.put("bank_address", bank_address);
        if (!TextUtils.isEmpty(bank_username))
            params.put("bank_username", bank_username);
        if (!TextUtils.isEmpty(phone))
            params.put("phone", phone);
        params.put("type", type);

        long timeStamp= System.currentTimeMillis();
        String sign= generateMd5Str(access_token,"",timeStamp,APP_KEY,client);
        StringBuilder build=new StringBuilder("access_token=");
        build.append(access_token).append("&timestamp=").append(timeStamp).append("&app_key="+APP_KEY)
                .append("&client=").append(client).append("&sign=").append(sign);
//        if (isDebug)
//            Log.e("changmai","jsonurl:"+DEAL_EXPORT+build.toString()+">>>");

        post(DEAL_EXPORT+build.toString()).params(params).execute(new StringNetCallback(context) {
            @Override
            public void onSuccess(String s, Call call, Response response) {


                paraJson(context,s,listener);
            }
        });
    }

    //绑定手机号
    public static void bindPhone(final Context context, String access_token, String client, String bindPhone, String messageNumber,
                                 final OnNetResponseListener listener){
        Map<String, String> params = new HashMap<String, String>();
        params.put("bindPhone", bindPhone);
        params.put("messageNumber", messageNumber);

        long timeStamp= System.currentTimeMillis();
        String sign= generateMd5Str(access_token,"",timeStamp,APP_KEY,client);
        StringBuilder build=new StringBuilder("access_token=");
        build.append(access_token).append("&timestamp=").append(timeStamp).append("&app_key="+APP_KEY)
                .append("&client=").append(client).append("&sign=").append(sign);
//        if (isDebug)
//            Log.e("changmai","jsonurl:"+BIND_PHONE+build.toString()+">>>");

        post(BIND_PHONE+build.toString()).params(params).execute(new StringNetCallback(context) {
            @Override
            public void onSuccess(String s, Call call, Response response) {


                paraJson(context,s,listener);
            }
        });
    }

    //绑定手机号获取验证码
    public static void getMessageNumber(final Context context, String access_token, String client, String phone, String type, final OnNetResponseListener listener){
        long timeStamp= System.currentTimeMillis();
        StringBuilder build = new StringBuilder("access_token=");

        String sign = generateMd5Str(access_token, "phone"+phone+"type"+type, timeStamp, APP_KEY, client);

        build.append(access_token).append("&timestamp=").append(timeStamp).append("&app_key=" + APP_KEY)
                .append("&client=").append(client).append("&phone=").append(phone)
                .append("&type=").append(type).append("&sign=").append(sign);
//        if (isDebug)
//            Log.e("changmai","jsonurl:"+SEND_CUSTOMER_PHONE+build.toString()+">>>");

        OkGo.get(SEND_CUSTOMER_PHONE+build.toString()).execute(new StringNetCallback(context) {
            @Override
            public void onSuccess(String s, Call call, Response response) {


                paraJson(context,s,listener);
            }
        });
    }
    private static String generateMd5Str(String access_token, long  timeStamp, String appkey, String client){
        StringBuilder builder = new StringBuilder(access_token);
        builder.append(timeStamp).append(appkey).append(client);
//        Log.e("json","str:"+builder.toString()+">>>");
//        Log.e("json","md5Str:"+Util.getMd5(builder.toString()));
        return Util.getMd5(builder.toString());
    }

    private static String generateMd5Str(String access_token, String parms, long  timeStamp, String appkey, String client){
        StringBuilder builder = new StringBuilder(access_token);
        builder.append(parms).append(timeStamp).append(appkey).append(client);
//        Log.e("json","str:"+builder.toString()+">>>");
//        Log.e("json","md5Str:"+Util.getMd5(builder.toString()));
       return Util.getMd5(builder.toString());
    }

    private static void paraJson(Context context, String json, OnNetResponseListener listener){
        Log.e("minrui","json="+json);
        JSONObject object = null;
        try {
            object = new JSONObject(json);
          int result=  object.optInt("result");
            if(listener!=null){
                if(result==1){
                    listener.onNetDataResponse(object);
                }else{
                    listener.onNetFailResponse(context,object.optString("msg"),object.optString("errcode"));
                }
            }
        } catch (JSONException e) {
            LoadingDialog.cancelDialogForLoading();
            e.printStackTrace();
            Log.e("minrui","JSONException="+e.getMessage());
        }
    }

    private static void netError(Context context, String error, OnNetResponseListener listener){
        if (listener!=null){
            listener.onNetErrorResponse(context,error);
        }
    }

    public static void uploadImg(final Context context, File file, String access_token, String client
            , final OnNetResponseListener listener){
        long timeStamp= System.currentTimeMillis();
        String sign= generateMd5Str(access_token,"",timeStamp,APP_KEY,client);
        StringBuilder build=new StringBuilder("access_token=");
        build.append(access_token).append("&timestamp=").append(timeStamp).append("&app_key="+APP_KEY)
                .append("&client=").append(client).append("&sign=").append(sign);
//        Log.e("changmai","jsonurl:"+MODIFY_HEAD+build.toString()+">>>");

        post(MODIFY_HEAD+build.toString())//
        .tag(context)//

        .params("cover", file)           // 这种方式为同一个key，上传多个文件
        .execute(new StringNetCallback(context) {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                paraJson(context,s,listener);
            }
        });

    }


}



//OkGo.post(Urls.URL_FORM_UPLOAD)//
//        .tag(this)//
//        .headers("header1", "headerValue1")//
//        .headers("header2", "headerValue2")//
//        .params("param1", "paramValue1")//
//        .params("param2", "paramValue2")//
////                .params("file1",new File("文件路径"))   //这种方式为一个key，对应一个文件
////                .params("file2",new File("文件路径"))
////                .params("file3",new File("文件路径"))
//        .addFileParams("file", files)           // 这种方式为同一个key，上传多个文件
//        .execute(new JsonCallback<LzyResponse<ServerModel>>() {
//@Override
//public void onBefore(BaseRequest request) {
//        super.onBefore(request);
//        btnFormUpload.setText("正在上传中...");
//        }
//
//@Override
//public void onSuccess(LzyResponse<ServerModel> responseData, Call call, Response response) {
//        handleResponse(responseData.data, call, response);
//        btnFormUpload.setText("上传完成");
//        }
//
//@Override
//public void onError(Call call, Response response, Exception e) {
//        super.onError(call, response, e);
//        handleError(call, response);
//        btnFormUpload.setText("上传出错");
//        }
//
//@Override
//public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
//        System.out.println("upProgress -- " + totalSize + "  " + currentSize + "  " + progress + "  " + networkSpeed);
//
//        String downloadLength = Formatter.formatFileSize(getApplicationContext(), currentSize);
//        String totalLength = Formatter.formatFileSize(getApplicationContext(), totalSize);
//        tvDownloadSize.setText(downloadLength + "/" + totalLength);
//        String netSpeed = Formatter.formatFileSize(getApplicationContext(), networkSpeed);
//        tvNetSpeed.setText(netSpeed + "/S");
//        tvProgress.setText((Math.round(progress * 10000) * 1.0f / 100) + "%");
//        pbProgress.setMax(100);
//        pbProgress.setProgress((int) (progress * 100));
//        }
//        });