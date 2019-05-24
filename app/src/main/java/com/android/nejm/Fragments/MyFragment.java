package com.android.nejm.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.nejm.BuildConfig;
import com.android.nejm.R;
import com.android.nejm.activitys.EditPersonalInfoActivity;
import com.android.nejm.activitys.FavoriteActivity;
import com.android.nejm.activitys.FeedbackActivity;
import com.android.nejm.activitys.MainActivity;
import com.android.nejm.activitys.NotificationActivity;
import com.android.nejm.activitys.OfflineArticleListActivity;
import com.android.nejm.activitys.ReadHistoryActivity;
import com.android.nejm.activitys.SettingActivity;
import com.android.nejm.activitys.WebViewActivity;
import com.android.nejm.bean.DownloadRecord;
import com.android.nejm.data.AccountInfo;
import com.android.nejm.data.RelatedArticle;
import com.android.nejm.db.DownloadRecordManager;
import com.android.nejm.manage.LoginUserManager;
import com.android.nejm.net.HttpUtils;
import com.android.nejm.net.OnNetResponseListener;
import com.android.nejm.utils.MyDownloadManager;
import com.android.nejm.utils.ToastUtil;
import com.android.nejm.widgets.LoadingDialog;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MyFragment extends BaseFragment {
    @BindView(R.id.textViewUserName)
    TextView textViewUserName;
    @BindView(R.id.textViewReadCount)
    TextView textViewReadCount;
    @BindView(R.id.textViewFavoriteCount)
    TextView textViewFavoriteCount;
    @BindView(R.id.textViewDownloadCount)
    TextView textViewDownloadCount;
    @BindView(R.id.textViewNotifyCount)
    TextView textViewNotifyCount;
    @BindView(R.id.textViewVersion)
    TextView textViewVersion;
    @BindView(R.id.imageViewHead)
    SimpleDraweeView imageViewHead;
    @BindView(R.id.textViewDownload)
    TextView textViewDownload;
    @BindView(R.id.textViewDownloadComplete)
    TextView textViewDownloadComplete;

    //相册请求码
    private static final int ALBUM_REQUEST_CODE = 1;
    //相机请求码
    private static final int CAMERA_REQUEST_CODE = 2;
    //剪裁请求码
    private static final int CROP_REQUEST_CODE = 3;

    //调用照相机返回图片文件
    private File tempFile;
    private Uri uritempFile;

    private AccountInfo accountInfo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.my_fragment, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        textViewVersion.setText("V" + BuildConfig.VERSION_NAME);
    }

    private void updateDownloadCount() {
        Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(ObservableEmitter<Long> emitter) throws Exception {
                long count = DownloadRecordManager.getRecordCount();
                Log.e("dpp", "subscribe, count = " + count);
                emitter.onNext(count);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long count) throws Exception {
                        Log.e("dpp", "accept, count = " + count);
                        textViewDownloadCount.setText(String.valueOf(count));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
        AccountInfo accountInfo = LoginUserManager.getInstance().getAccountInfo();
        if (accountInfo != null && !TextUtils.isEmpty(accountInfo.avatar)) {
            imageViewHead.setImageURI(accountInfo.avatar);
        }

        long lastDownloadTime = LoginUserManager.getInstance().lastDownloadtime;
        if(lastDownloadTime != 0) {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);

            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTimeInMillis(lastDownloadTime);
            int year2 = calendar1.get(Calendar.YEAR);
            int weekOfYear2 = calendar1.get(Calendar.WEEK_OF_YEAR);

            if(year == year2 && weekOfYear == weekOfYear2) {
                textViewDownloadComplete.setVisibility(View.VISIBLE);
                textViewDownload.setVisibility(View.INVISIBLE);
            } else {
                textViewDownloadComplete.setVisibility(View.INVISIBLE);
                textViewDownload.setVisibility(View.VISIBLE);
            }
        } else {
            textViewDownloadComplete.setVisibility(View.INVISIBLE);
            textViewDownload.setVisibility(View.VISIBLE);
        }

        updateDownloadCount();

        Log.e("TAG", "MyFragment, onResume");
    }

    private void getData() {
        LoadingDialog.showDialogForLoading(mContext);
        HttpUtils.getPersonalInfo(mContext, new OnNetResponseListener() {
            @Override
            public void onNetDataResponse(JSONObject json) {
                LoadingDialog.cancelDialogForLoading();
                accountInfo = new Gson().fromJson(json.toString(), AccountInfo.class);
                LoginUserManager.getInstance().setAccountInfo(accountInfo);
                imageViewHead.setImageURI(accountInfo.avatar);
                textViewUserName.setText(accountInfo.membername);
                if (accountInfo.read_count >= 100) {
                    textViewReadCount.setText("99+");
                } else {
                    textViewReadCount.setText(String.valueOf(accountInfo.read_count));
                }

                if (accountInfo.fav_count >= 100) {
                    textViewFavoriteCount.setText("99+");
                } else {
                    textViewFavoriteCount.setText(String.valueOf(accountInfo.fav_count));
                }
                if(accountInfo.ids!=null){
                    textViewNotifyCount.setText(""+accountInfo.ids.size());
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @OnClick(R.id.imageViewHead)
    public void onClickHead() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setItems(new String[]{"从相册选择", "从相机选择"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    ((MainActivity)mContext).getPicFromAlbm();
                } else {
                    ((MainActivity)mContext).openCamera();
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void openCamera(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ContextCompat.checkSelfPermission((Activity)mContext,
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED ||ContextCompat.checkSelfPermission((Activity)mContext,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity)mContext, new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1009);
            }
            else{
                getPicFromCamera();
            }}else {
            getPicFromCamera();
        }
    }


    @OnClick(R.id.textViewEmail)
    public void onClickEmail() {
        if (TextUtils.equals(accountInfo.email_unsubscribe, "0")) {
            SpannableString msg = new SpannableString("您已开启邮件订阅，是否要关闭邮件订阅？");
            msg.setSpan(new ForegroundColorSpan(Color.parseColor("#C92700")), 12, 14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            new AlertDialog.Builder(getActivity())
                    .setMessage(msg)
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).setPositiveButton("关闭订阅", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    HttpUtils.subscribeEmail(mContext, new OnNetResponseListener() {
                        @Override
                        public void onNetDataResponse(JSONObject json) {
                            ToastUtil.showShort(mContext, "关闭订阅成功");
                            LoginUserManager.getInstance().accountInfo.email_unsubscribe = "1";
                        }
                    });
                }
            }).create().show();
        } else {
            SpannableString msg = new SpannableString("您已关闭邮件订阅，是否要打开邮件订阅？");
            msg.setSpan(new ForegroundColorSpan(Color.parseColor("#C92700")), 12, 14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            new AlertDialog.Builder(getActivity())
                    .setMessage(msg)
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).setPositiveButton("打开订阅", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (TextUtils.isEmpty(accountInfo.email)) {
                        new AlertDialog.Builder(getActivity())
                                .setMessage("您没有邮箱信息，在设置邮箱后，才能订阅邮件。")
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).setPositiveButton("设置邮箱", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(mContext, EditPersonalInfoActivity.class);
                                startActivity(intent);
                            }
                        }).create().show();
                    } else {
                        HttpUtils.subscribeEmail(mContext, new OnNetResponseListener() {
                            @Override
                            public void onNetDataResponse(JSONObject json) {
                                ToastUtil.showShort(mContext, "打开订阅成功");
                                LoginUserManager.getInstance().accountInfo.email_unsubscribe = "0";
                            }
                        });
                    }
                }
            }).create().show();
        }
    }

    @OnClick(R.id.textViewMicroMsg)
    public void onClickMicroMsg() {
        new AlertDialog.Builder(getActivity())
                .setMessage("立刻前往NEJM医学前沿官方微信。")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("立刻前往", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                WebViewActivity.launchActivity(getActivity(), "NEJM医学前沿", "http://edm.nejmqianyan.cn/wechat_info/wechat.html");
            }
        }).create().show();
    }

    @OnClick(R.id.textViewEditPersonalInfo)
    public void onClickEditPersonalInfo() {
        Intent intent = new Intent(getActivity(), EditPersonalInfoActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.textViewFeedback)
    public void onClickFeedback() {
        Intent intent = new Intent(getActivity(), FeedbackActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.textViewSetting)
    public void onClickSetting() {
        Intent intent = new Intent(getActivity(), SettingActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.textViewContactUs)
    public void onClickContactUs() {
        WebViewActivity.launchActivity(getActivity(), "联系我们", "http://www.nejmqianyan.cn/index.php?c=singlepage&m=contactus");
    }

    @OnClick(R.id.readLayout)
    public void onClickReadLayout() {
        startActivity(new Intent(getActivity(), ReadHistoryActivity.class));
    }

    @OnClick(R.id.favoriteLayout)
    public void onClickFavoriteLayout() {
        startActivity(new Intent(getActivity(), FavoriteActivity.class));
    }

    @OnClick(R.id.downloadLayout)
    public void onClickDownloadLayout() {
        OfflineArticleListActivity.launchActivity(mContext);
    }

    @OnClick(R.id.notifyLayout)
    public void onClickNotifyLayout() {
        startActivity(new Intent(getActivity(), NotificationActivity.class));
    }

    @OnClick(R.id.textViewDownload)
    public void onDownload() {
        LoadingDialog.showDialogForLoading(mContext);
        String lastzipid = LoginUserManager.getInstance().lastzipid;
        HttpUtils.getThisWeekArticle(mContext, lastzipid, new OnNetResponseListener() {
            @Override
            public void onNetDataResponse(JSONObject json) {
                String zipId = json.optString("lastzipid");
                LoginUserManager.getInstance().setLastzipid(zipId);
                List<String> ids = new Gson().fromJson(json.optJSONArray("ids").toString(),
                        new TypeToken<List<String>>(){}.getType());
                List<String> urlList = new ArrayList<>();
                if(ids != null) {
                    List<String> filePathList = new ArrayList<>();
                    for(String id : ids) {
                        urlList.add(HttpUtils.ARTICLE_DETAIL_URL + id + "&uid=" + LoginUserManager.getInstance().uid);
                        String filePath = String.format(Locale.CHINA, "/html/%s.html", id);
                        filePathList.add(filePath);
                    }

                    List<RelatedArticle> articles = new Gson().fromJson(json.optJSONArray("items").toString(),
                            new TypeToken<List<RelatedArticle>>(){}.getType());

                    List<String> imgUrlList = new ArrayList<>();
                    List<String> imgFilePathList = new ArrayList<>();

                    for(RelatedArticle article : articles) {
                        int lastIndex = article.thumb.lastIndexOf('/');
                        if(lastIndex >= 0) {
                            String fileName = article.thumb.substring(lastIndex);
                            String filePath = String.format(Locale.CHINA, "/html/%s", fileName);
                            imgFilePathList.add(filePath);
                            imgUrlList.add(article.thumb);
                        }

                        DownloadRecord downloadRecord = new DownloadRecord(article);
                        File file = new File(mContext.getExternalFilesDir(null), String.format(Locale.CHINA, "/html/%s.html", article.id));
                        downloadRecord.filePath = file.getAbsolutePath();
                        DownloadRecordManager.insert(downloadRecord);
                    }

                    MyDownloadManager.download(mContext, urlList, filePathList, imgUrlList, imgFilePathList,
                            new MyDownloadManager.DownloadCompleteListener() {
                        @Override
                        public void downloadComplete() {
                            Log.e("TAG", "downloadComplete");
                            LoadingDialog.cancelDialogForLoading();
                            textViewDownloadComplete.setVisibility(View.VISIBLE);
                            textViewDownload.setVisibility(View.INVISIBLE);
                            LoginUserManager.getInstance().setLastDownloadtime(Calendar.getInstance().getTimeInMillis());
                            updateDownloadCount();
                        }
                    });
                }
            }
        });
    }

    @OnClick(R.id.textViewDownloadComplete)
    public void onClickDownloadComplete() {

    }

    /**
     * 从相机获取图片
     */
    private void getPicFromCamera() {
        //用于保存调用相机拍照后所生成的文件
        Log.e("minrui","getPicFromCamera");
        tempFile = new File(Environment.getExternalStorageDirectory().getPath(), System.currentTimeMillis() + ".jpg");
        //跳转到调用系统相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //判断版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {   //如果在Android7.0以上,使用FileProvider获取Uri
            intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(mContext, "com.android.nejm", tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
        } else {    //否则使用Uri.fromFile(file)方法获取Uri
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        }
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    /**
     * 从相册获取图片
     */
    private void getPicFromAlbm() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission((Activity) mContext,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1008);
            } else {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, ALBUM_REQUEST_CODE);
            }
        } else {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, ALBUM_REQUEST_CODE);
        }
    }


    /**
     * 裁剪图片
     */
    private void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);

        uritempFile = Uri.parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + "small.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        startActivityForResult(intent, CROP_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case CAMERA_REQUEST_CODE:   //调用相机后返回
                if (resultCode == Activity.RESULT_OK) {
                    //用相机返回的照片去调用剪裁也需要对Uri进行处理
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Uri contentUri = FileProvider.getUriForFile(mContext, "com.android.nejm", tempFile);
                        cropPhoto(contentUri);
                    } else {
                        cropPhoto(Uri.fromFile(tempFile));
                    }
                }
                break;
            case ALBUM_REQUEST_CODE:    //调用相册后返回
                if (resultCode == Activity.RESULT_OK) {
                    Uri uri = intent.getData();
                    cropPhoto(uri);
                }
                break;
            case CROP_REQUEST_CODE:     //调用剪裁后返回
                LoadingDialog.showDialogForLoading(mContext);
                if (intent != null) {
                    Bundle bundle = intent.getExtras();
                    if (bundle != null) {
                        //在这里获得了剪裁后的Bitmap对象，可以用于上传
                        Bitmap image = bundle.getParcelable("data");
                        //设置到ImageView上
//                    imageViewHead.setImageBitmap(image);
                        //也可以进行一些保存、压缩等操作后上传
                        uploadImage(image);
                    } else {
                        try {
                            Bitmap bitmap = BitmapFactory.decodeStream(mContext.getContentResolver().openInputStream(uritempFile));
                            uploadImage(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                    }
                }
                break;
        }
    }

    public void uploadImage(Bitmap image) {
        Log.e("minrui", "image=" + image);
        String path = saveImage("crop", image);
        HttpUtils.uploadImg(mContext, new File(path), new OnNetResponseListener() {
            @Override
            public void onNetDataResponse(JSONObject json) {
                LoadingDialog.cancelDialogForLoading();
                ToastUtil.showShort(mContext, "头像上传成功");
                String headUrl = json.optString("avatar");
                imageViewHead.setImageURI(headUrl);
                LoginUserManager.getInstance().accountInfo.avatar = headUrl;
            }

            @Override
            public void onNetFailResponse(Context context, String msg, String msgCode) {
                super.onNetFailResponse(context, msg, msgCode);
                ToastUtil.showShort(mContext, "头像上传失败");
            }
        });
    }

    public String saveImage(String name, Bitmap bmp) {
        File appDir = new File(Environment.getExternalStorageDirectory().getPath());
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = name + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == 1008) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, ALBUM_REQUEST_CODE);
            }
        } else if (requestCode == 1009) {
            if (grantResults.length == 0||grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            }
        }


        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
