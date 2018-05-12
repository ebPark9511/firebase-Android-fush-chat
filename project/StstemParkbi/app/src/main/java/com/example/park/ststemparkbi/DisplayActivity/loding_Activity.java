package com.example.park.ststemparkbi.DisplayActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.park.ststemparkbi.*;
import com.example.park.ststemparkbi.R;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.ssomai.android.scalablelayout.ScalableLayout;

import java.util.ArrayList;

/**
 * Created by Park on 2016-11-14.
 * BCFF51
 */

public class loding_Activity extends Activity {
    ScalableLayout loding_img;
    SharedPreferences setting;
    SharedPreferences.Editor editor;
    DBHelper dbHelper;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.park.ststemparkbi.R.layout.loding);
        loding_img = (ScalableLayout) findViewById(R.id.loding_img);
        dbHelper = new DBHelper(getApplicationContext(), "SystemMsg.db", null, 1);

        AppManager.getInstance().setActivity(this);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_twits);
        loding_img.startAnimation(animation);
        setting = getSharedPreferences("setting", 0);
        editor = setting.edit();

        ConnectivityManager manager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mobile.isConnected() || wifi.isConnected()) {//인터넷 연결하라는 소스

            PermissionListener permissionlistener = new PermissionListener() {
                @Override
                public void onPermissionGranted() {
                    Handler mHandler = new Handler();
                    mHandler.postDelayed(new Runnable() {
                        public void run() {
                                if(! setting.getString("NICK","").toString().equals("")) {
                                    startActivity(new Intent(loding_Activity.this, MapsActivity.class));
                                    finish();
                                }else{
                                    showDialog();
                                }
                        }
                    }, 800);
                }

                @Override
                public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                    finish();
                }

            };
            new TedPermission(this)
                    .setPermissionListener(permissionlistener)
                    .setDeniedMessage("아직 허용되지 않은 권한이 있기 때문에 앱을 실행할 수 없습니다.\n\n[설정] > [권한] 에서 권한을 허용으로 바꿔주세요.")
                    .setPermissions(android.Manifest.permission.ACCESS_FINE_LOCATION)
                    .check();
        } else

        {
            AlertDialog.Builder aDialog = new AlertDialog.Builder(loding_Activity.this);
            aDialog.setMessage("인터넷 연결 뒤 다시 이용해 주세요.");
            aDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            moveTaskToBack(true);
                            finish();
                        }
                    }
            );
            AlertDialog ad = aDialog.create();  // 위 내용의 팝업창 생성
            ad.show();  //팝업창(액티비티)을 보여준다.
        }
    }


    public void showDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("입력");
        alert.setMessage("닉네임을 정해주세요");

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                value.toString();
                startActivity(new Intent(loding_Activity.this, MapsActivity.class));

                editor.putString("NICK",  value.toString());
                editor.commit();

                finish();
            }
        });


        alert.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                        finish();
                    }
                });

        alert.show();
    }

}