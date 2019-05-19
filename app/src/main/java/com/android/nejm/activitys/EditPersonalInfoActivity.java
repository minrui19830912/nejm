package com.android.nejm.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.widget.TextView;

import com.android.nejm.Fragments.IdentityDoctorFragment;
import com.android.nejm.Fragments.IdentityStudentFragment;
import com.android.nejm.Fragments.IdentiyOtherFragment;
import com.android.nejm.R;
import com.android.nejm.data.AccountInfo;
import com.android.nejm.manage.LoginUserManager;
import com.android.nejm.net.HttpUtils;
import com.android.nejm.net.OnNetResponseListener;
import com.android.nejm.utils.ToastUtil;
import com.android.nejm.widgets.LoadingDialog;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

public class EditPersonalInfoActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {
    @BindView(R.id.draweeViewHead)
    SimpleDraweeView draweeViewHead;
    @BindView(R.id.textViewName)
    TextView textViewName;
    @BindView(R.id.textViewPhone)
    TextView textViewPhone;
    @BindView(R.id.textViewEmail)
    TextView textViewEmail;
    @BindView(R.id.textViewIdentityOne)
    TextView textViewIdentityOne;
    @BindView(R.id.textViewIdentityTwo)
    TextView textViewIdentityTwo;
    @BindView(R.id.textViewIdentityThree)
    TextView textViewIdentityThree;
    @BindView(R.id.textViewIdentityFour)
    TextView textViewIdentityFour;

    //相册请求码
    private static final int ALBUM_REQUEST_CODE = 1;
    //相机请求码
    private static final int CAMERA_REQUEST_CODE = 2;
    //剪裁请求码
    private static final int CROP_REQUEST_CODE = 3;

    //调用照相机返回图片文件
    private File tempFile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_personal_info);
        ButterKnife.bind(this);
        setCommonTitle("编辑个人信息");
        showBack();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPersonalInfo();
    }

    private void loadPersonalInfo() {
        AccountInfo accountInfo = LoginUserManager.getInstance().getAccountInfo();
        if(accountInfo != null) {
            draweeViewHead.setImageURI(accountInfo.avatar);
            textViewName.setText(accountInfo.truename);
            textViewPhone.setText(accountInfo.mobile);
            textViewEmail.setText(accountInfo.email);
            if(TextUtils.isEmpty(accountInfo.roleid)
                || TextUtils.equals(accountInfo.roleid, "0")) {
                textViewIdentityOne.setText("");
                textViewIdentityTwo.setText("");
                textViewIdentityThree.setText("");
                textViewIdentityFour.setText("");
            } else {
                textViewIdentityOne.setText(accountInfo.role_name);
                if(TextUtils.equals(accountInfo.roleid, "1")
                        || TextUtils.equals(accountInfo.roleid, "2")
                        || TextUtils.equals(accountInfo.roleid, "4")) {
                    textViewIdentityTwo.setText(accountInfo.division);
                    textViewIdentityThree.setText(accountInfo.hospital);
                    textViewIdentityFour.setText(accountInfo.jobname);
                } else if(TextUtils.equals(accountInfo.roleid, "5")) {
                    textViewIdentityTwo.setText(accountInfo.identity);
                    textViewIdentityThree.setText(accountInfo.hospital);
                    textViewIdentityFour.setText(accountInfo.jobname);
                } else {
                    textViewIdentityTwo.setText(accountInfo.identity);
                    textViewIdentityThree.setText(accountInfo.jobname);
                    textViewIdentityFour.setText(accountInfo.company);
                }
            }
        }
    }

    @OnClick(R.id.textViewName)
    public void onClickName() {
        EditNameActivity.launchActivity(this);
    }

    @OnClick(R.id.textViewPhone)
    public void onClickPhone() {
        ModifyPhoneStep1Activity.launchActivity(this);
    }

    @OnClick(R.id.textViewEmail)
    public void onClickEmail() {
        EditEmailActivity.launchActivity(this);
    }

    @OnClick(R.id.textViewEdit)
    public void onClickEdit() {
        startActivity(new Intent(this, IdentityInfoActivity.class));
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @OnClick(R.id.textViewHead)
    public void onClickHead() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setItems(new String[]{"从相册选择","从相机选择"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which==0){
                    getPicFromAlbm();
                } else {
                    getPicFromCamera();
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void chooseImage() {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        /*switch (requestCode) {
            case RC_CAMERA:
                break;
            case RC_STORAGE:
                break;
        }*/
        chooseImage();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }

    /**
     * 从相机获取图片
     */
    private void getPicFromCamera() {
        //用于保存调用相机拍照后所生成的文件
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
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, ALBUM_REQUEST_CODE);
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
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    //在这里获得了剪裁后的Bitmap对象，可以用于上传
                    Bitmap image = bundle.getParcelable("data");
                    //设置到ImageView上
//                    imageViewHead.setImageBitmap(image);
                    //也可以进行一些保存、压缩等操作后上传
                    String path = saveImage("crop", image);
                    HttpUtils.uploadImg(mContext, new File(path), new OnNetResponseListener() {
                        @Override
                        public void onNetDataResponse(JSONObject json) {
                            LoadingDialog.cancelDialogForLoading();
                            ToastUtil.showShort(mContext,"头像上传成功");
                            String headUrl = json.optString("avatar");
                            draweeViewHead.setImageURI(headUrl);
                            LoginUserManager.getInstance().accountInfo.avatar = headUrl;
                        }

                        @Override
                        public void onNetFailResponse(Context context, String msg, String msgCode) {
                            super.onNetFailResponse(context, msg, msgCode);
                            ToastUtil.showShort(mContext,"头像上传失败");
                        }
                    });
                } else {
                    LoadingDialog.cancelDialogForLoading();
                }
                break;
        }
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
}
