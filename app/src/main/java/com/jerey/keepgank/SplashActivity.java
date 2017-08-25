package com.jerey.keepgank;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.cn.jerey.permissiontools.Callback.PermissionCallbacks;
import com.cn.jerey.permissiontools.PermissionTools;

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
                        //    Toast.makeText(MainActivity.this, "权限申请通过", Toast.LENGTH_SHORT)
                        // .show();
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    }

                    @Override
                    public void onPermissionsDenied(int requestCode, List<String> perms) {
                        Toast.makeText(SplashActivity.this, "权限申请被拒绝", Toast.LENGTH_SHORT).show();
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
