package com.android.nejm.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.android.nejm.R;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

public class EditPersonalInfoActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {
    @BindView(R.id.textViewIdentityOne)
    TextView textViewIdentityOne;
    @BindView(R.id.textViewIdentityTwo)
    TextView textViewIdentityTwo;
    @BindView(R.id.textViewIdentityThree)
    TextView textViewIdentityThree;
    @BindView(R.id.textViewIdentityFour)
    TextView textViewIdentityFour;

    public static final int RC_CAMERA = 0;
    public static final int RC_STORAGE = 1;
    private static final int REQUEST_CODE_GET_IMAGE = 1;
    private static final int REQUEST_CODE_TAKE_PHOTO = 2;
    public static final int REQUEST_CODE_CHOOSE = 2;

    String mPicPath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_personal_info);
        ButterKnife.bind(this);
        setCommonTitle("编辑个人信息");
        showBack();
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

    @OnClick(R.id.textViewHead)
    public void onClickHead() {
        String[] strArr = new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
        if (EasyPermissions.hasPermissions(this, strArr)) {
            chooseImage();
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.request_update_storage), 1, strArr);
        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != Activity.RESULT_OK) {
            return;
        }
    }
}
