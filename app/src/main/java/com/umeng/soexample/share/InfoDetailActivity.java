package com.umeng.soexample.share;

import java.io.Serializable;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.soexample.BaseActivity;
import com.umeng.soexample.HomeActivity;
import com.umeng.soexample.R;
import com.umeng.soexample.share.utils.SerializableMap;

import org.greenrobot.greendao.AbstractDaoMaster;

/**
 * Created by wangfei on 2018/1/23.
 */

public class InfoDetailActivity extends BaseActivity {
    private SHARE_MEDIA share_media;
    private TextView result;

    private static final int mediadata = 1;
    private static final int EXIT = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        share_media = (SHARE_MEDIA) getIntent().getSerializableExtra("platform");
        setTitle("第三方登录");
        setBackVisibily();
        result = findViewById(R.id.result);
        result.setMovementMethod(new ScrollingMovementMethod());
        findViewById(R.id.ok).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                UMShareAPI.get(InfoDetailActivity.this).getPlatformInfo(InfoDetailActivity.this, share_media, new UMAuthListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {

                    }

                    @Override
                    public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                        StringBuilder sb = new StringBuilder();
                        for (String key : map.keySet()) {
                            sb.append(key).append(" : ").append(map.get(key)).append("\n");
                        }

                        Log.i("第三方获取授权数据-->","data---->"+sb.toString()+"--->第三方类型--->"+share_media);
                        result.setText(sb.toString());

                        Message msg = new Message();
                        msg.what = mediadata;
                        msg.obj =  map;
                        mHandler.sendMessage(msg);
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                        result.setText("错误" + throwable.getMessage());
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media, int i) {
                        result.setText("用户已取消");
                    }
                });
            }
        });
        findViewById(R.id.clear).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                result.setText("");
            }
        });
    }
    @Override
    public int getLayout() {
        return R.layout.activity_infodetail;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode,resultCode,data);
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case mediadata:



                    Map<String, String> map = (Map<String, String>) msg.obj;
                    SerializableMap myMap=new SerializableMap();
                    myMap.setMap(map);//将map数据添加到封装的myMap中
                    Log.i("传递mymap-->",myMap.toString());
                    Intent intent = new Intent();
                    ComponentName comp = new ComponentName("com.bjfu.mcs","com.bjfu.mcs.loginSign.SocialOauthActivity");
                    intent.setComponent(comp);
                    intent.setAction("android.intent.action.MAIN");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                    finish();
                    break;

                case EXIT:
                    Intent intent2 = new Intent(InfoDetailActivity.this,HomeActivity.class);
                    intent2.putExtra(HomeActivity.TAG_EXIT, true);
                    startActivity(intent2);
                    break;
                default:
                    break;
            }
        }
    };
}
