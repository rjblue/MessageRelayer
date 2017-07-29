package com.whf.messagerelayer.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.whf.messagerelayer.R;
import com.whf.messagerelayer.utils.NativeDataManager;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static WeakReference<MainActivity> myself;
    private RelativeLayout mSmsLayout, mEmailLayout, mRuleLayout;
    private NativeDataManager mNativeDataManager;
    private TextView logTextView;

    public static MainActivity getInstance() {
        return myself != null ? myself.get() : null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        myself = new WeakReference<MainActivity>(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logTextView = (TextView) findViewById(R.id.log_text);
        logTextView.setMovementMethod(new ScrollingMovementMethod());
        mNativeDataManager = new NativeDataManager(this);
        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Boolean isReceiver = mNativeDataManager.getReceiver();
        final MenuItem menuItem = menu.add("开关");
        if (isReceiver) {
            menuItem.setIcon(R.mipmap.ic_send_on);
        } else {
            menuItem.setIcon(R.mipmap.ic_send_off);
        }

        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Boolean receiver = mNativeDataManager.getReceiver();
                if (receiver) {
                    mNativeDataManager.setReceiver(false);
                    menuItem.setIcon(R.mipmap.ic_send_off);
                    Toast.makeText(MainActivity.this, "总闸已关闭", Toast.LENGTH_SHORT).show();
                } else {
                    checkAndGetPermission();
                    mNativeDataManager.setReceiver(true);
                    menuItem.setIcon(R.mipmap.ic_send_on);
                    Toast.makeText(MainActivity.this, "总闸已开启", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    private void initView() {
        mSmsLayout = (RelativeLayout) findViewById(R.id.sms_relay_layout);
        mEmailLayout = (RelativeLayout) findViewById(R.id.email_relay_layout);
        mRuleLayout = (RelativeLayout) findViewById(R.id.rule_layout);

        mSmsLayout.setOnClickListener(this);
        mEmailLayout.setOnClickListener(this);
        mRuleLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sms_relay_layout:
                startActivity(new Intent(this, SmsRelayerActivity.class));
                break;
            case R.id.email_relay_layout:
                startActivity(new Intent(this, EmailRelayerActivity.class));
                break;
            case R.id.rule_layout:
                startActivity(new Intent(this, RuleActivity.class));
        }
    }

    private void checkAndGetPermission() {
//        boolean hadPermission1 = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED;
//        log("是否拥有短信权限:" + hadPermission1);
//        boolean hadPermission2 = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
//        log("是否拥有读取通讯录权限:" + hadPermission2);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_CONTACTS, Manifest.permission.READ_CALL_LOG}, 0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults == null || grantResults.length == 0) {
            return;
        }
        for (int i = 0; i < grantResults.length; ++i) {
            if (grantResults[i] == 0) {
                log(permissions[i] + "权限获取成功");
            } else {
                log(permissions[i] + "权限获取失败,无法正常工作");
            }
        }

    }

    public void log(final String msg) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                logTextView.append(new SimpleDateFormat("HH-mm-ss:SSS").format(new Date()) + msg + "\n");
                int offset = logTextView.getLineCount() * logTextView.getLineHeight();
                logTextView.scrollTo(0, offset - logTextView.getHeight());
            }
        });
    }
}
