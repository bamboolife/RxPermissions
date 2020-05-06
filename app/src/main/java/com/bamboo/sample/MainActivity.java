package com.bamboo.sample;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bamboo.permissions.RxPermissions;
import com.jakewharton.rxbinding3.view.RxView;

public class MainActivity extends AppCompatActivity {

    RxPermissions rxPermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rxPermissions = new RxPermissions(this);

        initTest1();
        initTest3();
    }
    @SuppressLint("CheckResult")
    private void initTest1() {
        rxPermissions
                .request(Manifest.permission.CAMERA)
                .subscribe(granted -> {
                    if (granted) { // Always true pre-M
                        // I can control the camera now
                    } else {
                        // Oups permission denied
                    }
                });
    }
    @SuppressLint("CheckResult")
    private void initTest2() {
        rxPermissions
                .request(Manifest.permission.CAMERA,
                        Manifest.permission.READ_PHONE_STATE)
                .subscribe(granted -> {
                    if (granted) {
                        // All requested permissions are granted
                    } else {
                        // At least one permission is denied
                    }
                });
    }
    @SuppressLint("CheckResult")
    private void initTest3() {
        RxView.clicks(findViewById(R.id.btn))
                .compose(rxPermissions.ensure(Manifest.permission.CAMERA))
                .subscribe(granted -> {
                    if (granted){
                        Toast.makeText(MainActivity.this,"获取了权限",Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(MainActivity.this,"没有获取了权限",Toast.LENGTH_LONG).show();
                    }
                });

    }
    @SuppressLint("CheckResult")
    private void initTest4() {
        rxPermissions
                .requestEach(Manifest.permission.CAMERA,
                        Manifest.permission.READ_PHONE_STATE)
                .subscribe(permission -> { // will emit 2 Permission objects
                    if (permission.granted) {
                        // `permission.name` is granted !
                    } else if (permission.shouldShowRequestPermissionRationale) {
                        // Denied permission without ask never again
                    } else {
                        // Denied permission with ask never again
                        // Need to go to the settings
                    }
                });


    }

    @SuppressLint("CheckResult")
    private void initTest5() {

        rxPermissions
                .requestEachCombined(Manifest.permission.CAMERA,
                        Manifest.permission.READ_PHONE_STATE)
                .subscribe(permission -> {
                    if (permission.granted) {
                      // All permissions are granted !

                    } else if (permission.shouldShowRequestPermissionRationale) {
                        // At least one denied permission without ask never again

                    } else {

                        // At least one denied permission with ask never again
                        // Need to go to the settings
                    }
                });

    }

}

