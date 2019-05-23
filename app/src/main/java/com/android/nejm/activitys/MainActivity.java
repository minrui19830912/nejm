package com.android.nejm.activitys;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;

import com.android.nejm.Fragments.BaseFragment;
import com.android.nejm.Fragments.HomeFragment;
import com.android.nejm.Fragments.MyFragment;
import com.android.nejm.Fragments.MyUnloginFragment;
import com.android.nejm.Fragments.NewKnowledgeFragment;
import com.android.nejm.Fragments.PeriodArticleFragment;
import com.android.nejm.Fragments.VideoListFragment;
import com.android.nejm.R;
import com.android.nejm.manage.LoginUserManager;
import com.android.nejm.manage.UploadManager;
import com.android.nejm.utils.ToastUtil;
import com.android.nejm.widgets.LoadingDialog;

import java.io.File;
import java.io.FileNotFoundException;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private HomeFragment mHomeFragment;
    private NewKnowledgeFragment mNewKnowledgeFragment;
    private PeriodArticleFragment mPeriodArticleFragment;
    private VideoListFragment mVideoListFragment;
    private BaseFragment mMyFragment;
    private int mIndex = -1;
    private int[]mTabArray={R.id.indicator_one,R.id.indicator_two,R.id.indicator_three,R.id.indicator_four,R.id.indicator_five};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RadioGroup navView = findViewById(R.id.indicator_group);

        for(int i=0;i<mTabArray.length;i++){
            findViewById(mTabArray[i]).setOnClickListener(this);
        }
        findViewById(mTabArray[0]).performClick();

        UploadManager.getInstance().checkUpdate(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(mIndex == 4) {
            showFragment(4);
        }
    }

    private void showFragment(int index) {

        mIndex = index;
        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();

        switch (index) {
            case 0:
//                if (mMainFragment == null) {
//                    mMainFragment = new MainFragment();
//                    trans.add(R.id.content, mMainFragment);
//                } else {
//                    trans.show(mMainFragment);
//                }
                if (mHomeFragment == null) {
                    mHomeFragment = new HomeFragment();
                    trans.add(R.id.content, mHomeFragment);
                } else {
                    trans.show(mHomeFragment);
                }
                break;
            case 1:
//                AppUtil.shareToFriend(mContext, "abc", "fdasfd", "www.baidu.com", new PlatformActionListener() {
//                    @Override
//                    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
//                        ToastUtil.showShort(mContext,"onComplete");
//                    }
//
//                    @Override
//                    public void onError(Platform platform, int i, Throwable throwable) {
//                        ToastUtil.showShort(mContext,"onError");
//                    }
//
//                    @Override
//                    public void onCancel(Platform platform, int i) {
//                        ToastUtil.showShort(mContext,"onCancel");
//                    }
//                });
                if (mNewKnowledgeFragment == null) {
                    mNewKnowledgeFragment = new NewKnowledgeFragment();
                    trans.add(R.id.content, mNewKnowledgeFragment);
                } else {
                    trans.show(mNewKnowledgeFragment);
                }
                break;
            case 2:
                if (mPeriodArticleFragment == null) {
                    mPeriodArticleFragment = new PeriodArticleFragment();
                    trans.add(R.id.content, mPeriodArticleFragment);
                } else {
                    trans.show(mPeriodArticleFragment);
                }
                break;
            case 3:

                if (mVideoListFragment == null) {
                    mVideoListFragment = new VideoListFragment();
                    trans.add(R.id.content, mVideoListFragment);
                } else {
                    trans.show(mVideoListFragment);
                }
                break;
            case 4:
                if (mMyFragment == null) {
                  if(LoginUserManager.getInstance().isLogin) {
                      mMyFragment = new MyFragment();
                  } else {
                      mMyFragment = new MyUnloginFragment();
                  }

                    trans.add(R.id.content, mMyFragment);
                } else {
                    if(mMyFragment instanceof MyUnloginFragment
                            && LoginUserManager.getInstance().isLogin) {
                        trans.remove(mMyFragment);
                        mMyFragment = new MyFragment();
                        trans.add(R.id.content, mMyFragment);
                    } else if(mMyFragment instanceof MyFragment
                            && !LoginUserManager.getInstance().isLogin) {
                        trans.remove(mMyFragment);
                        mMyFragment = new MyUnloginFragment();
                        trans.add(R.id.content, mMyFragment);
                    }

                    trans.show(mMyFragment);
                }
                break;
        }

        trans.commitAllowingStateLoss();
    }

    private void hideAllFragment() {
        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
//        if (mMainFragment != null) {
//            trans.hide(mMainFragment);
//        }
        if (mHomeFragment != null) {
            trans.hide(mHomeFragment);
        }
        if (mNewKnowledgeFragment != null) {
            trans.hide(mNewKnowledgeFragment);
        }
        if (mPeriodArticleFragment != null) {
            trans.hide(mPeriodArticleFragment);
        }
        if (mVideoListFragment != null) {
            trans.hide(mVideoListFragment);
        }

        if (mMyFragment != null) {
            trans.hide(mMyFragment);
        }

        trans.commitAllowingStateLoss();
    }

    public void showTab(int index){
        findViewById(mTabArray[index]).performClick();
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id) {

            case R.id.indicator_one:
                hideAllFragment();
                showFragment(0);

                break;
            case R.id.indicator_two:

                    hideAllFragment();
                    showFragment(1);

                break;
            case R.id.indicator_three:
                hideAllFragment();
                showFragment(2);
                break;
            case R.id.indicator_four:

                    hideAllFragment();
                    showFragment(3);
//                startActivity(new Intent(mContext,LoginActivity.class));
                break;
            case R.id.indicator_five:
                hideAllFragment();
                showFragment(4);
                break;

        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
//        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }

    public void getPicFromAlbm() {
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.e("minrui","onRequestPermissionsResult=");
        if (requestCode == 1008) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, ALBUM_REQUEST_CODE);
            }
        } else if (requestCode == 1009) {
            Log.e("minrui","grantResults.length="+grantResults.length);
            Log.e("minrui","grantResults[0]="+grantResults[0]);
            if (grantResults.length == 0||grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            }
        }


        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void openCamera(){
        Log.e("minrui","openCamera");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

            if (checkSelfPermission(
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED ||checkSelfPermission(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                    Log.e("minrui","requestPermissions");
                requestPermissions( new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1009);
            }
            else{
                getPicFromCamera();
            }}else {
            getPicFromCamera();
        }

    }

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

    //相册请求码
    private static final int ALBUM_REQUEST_CODE = 1;
    //相机请求码
    private static final int CAMERA_REQUEST_CODE = 2;
    //剪裁请求码
    private static final int CROP_REQUEST_CODE = 3;

    //调用照相机返回图片文件
    private File tempFile;
    private Uri uritempFile;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Log.e("minrui","requestCode="+requestCode);
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
                        //ToastUtil.showShort(mContext,"dfs");
                        //设置到ImageView上
//                    imageViewHead.setImageBitmap(image);
                        //也可以进行一些保存、压缩等操作后上传
                        //uploadImage(image);
                        if(mMyFragment!=null&&mMyFragment instanceof MyFragment){
                            ((MyFragment)mMyFragment).uploadImage(image);
                        }
                    } else {
                        try {
                            Bitmap bitmap = BitmapFactory.decodeStream(mContext.getContentResolver().openInputStream(uritempFile));
                            //ToastUtil.showShort(mContext,"dfs123");
                            if(mMyFragment!=null&&mMyFragment instanceof MyFragment){
                                ((MyFragment)mMyFragment).uploadImage(bitmap);
                            }
                            //uploadImage(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                    }
                }
                break;
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
}
