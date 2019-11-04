package com.xcy.fzb;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.xcy.fzb.all.api.APKVersionCodeUtils;
import com.xcy.fzb.all.api.FinalContents;
import com.xcy.fzb.Login.LoginActivity;
import com.xcy.fzb.all.persente.CleanDataUtils;

public class BaseUrl extends AppCompatActivity {
    private TextView ensure;
    private TextView cancle;
    private EditText baseUrl;
    private SharedPreferences.Editor editor;
    private SharedPreferences pref;

    private  final int SPLASH_DISPLAY_LENGHT = 2000;//两秒后进入系统，时间可自行调整

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.baseurl);

        editor = getSharedPreferences("data", MODE_PRIVATE).edit();
        pref = getSharedPreferences("data", MODE_PRIVATE);


        if (pref.getString("introduction", "").equals("")) {
            Intent mainIntent = new Intent(BaseUrl.this,Introduction.class);
            BaseUrl.this.startActivity(mainIntent);
            BaseUrl.this.finish();
            editor.putString("introduction", "1");
            editor.commit();
            CleanDataUtils.cleanSharedPreference(this);
        } else if (pref.getString("introduction", "").equals("1")) {
            initLogin();
        }

        String versionName = APKVersionCodeUtils.getVerName(this);
        FinalContents.setVersionNumBer(versionName);
//        init();


    }

    private void init(){
        ensure = findViewById(R.id.login_ensure);
        cancle = findViewById(R.id.login_cancle);
        baseUrl = findViewById(R.id.baseUrl);
        ensure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FinalContents.setBaseUrl(baseUrl.getText().toString()+"/fangfang/app/v1/");
                Intent intent = new Intent(BaseUrl.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FinalContents.setBaseUrl("http://39.98.173.250:8080/fangfang/app/v1/");
                Intent intent = new Intent(BaseUrl.this,LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initLogin(){
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(BaseUrl.this,LoginActivity.class);
                BaseUrl.this.startActivity(mainIntent);
                BaseUrl.this.finish();
            }
        },SPLASH_DISPLAY_LENGHT);
    }
}
