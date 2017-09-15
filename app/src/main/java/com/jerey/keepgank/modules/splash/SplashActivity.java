package com.jerey.keepgank.modules.splash;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.cn.jerey.permissiontools.Callback.PermissionCallbacks;
import com.cn.jerey.permissiontools.PermissionTools;
import com.jerey.keepgank.R;
import com.jerey.keepgank.data.constant.AppConstant;
import com.jerey.keepgank.modules.MainActivity;
import com.jerey.keepgank.modules.welcome.WelcomeActivity;
import com.jerey.keepgank.utils.SPUtils;

import java.util.List;

public class SplashActivity extends AppCompatActivity {
    PermissionTools permissionTools;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        permissionTools = new PermissionTools.Builder(this)
                .setOnPermissionCallbacks(new PermissionCallbacks() {
                    @Override
                    public void onPermissionsGranted(int requestCode, List<String> perms) {
                        if (SPUtils.get(SplashActivity.this, AppConstant.isFirstEnter, true)) {
                            SPUtils.put(SplashActivity.this, AppConstant.isFirstEnter, false);
                            startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));
                            finish();
                        } else {
                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
                            finish();
                        }
                    }

                    @Override
                    public void onPermissionsDenied(int requestCode, List<String> perms) {
                        Toast.makeText(SplashActivity.this, R.string.perm_request_refused, Toast.LENGTH_SHORT).show();
                    }
                })
                .setRequestCode(99)
                .build();
        permissionTools.requestPermissions(Manifest.permission.INTERNET
                , Manifest.permission.READ_EXTERNAL_STORAGE
                , Manifest.permission.ACCESS_NETWORK_STATE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionTools.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
