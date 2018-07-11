package com.cas.stan.downloaddemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.reactivestreams.Subscriber;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public Context context;
    public Activity activity;
    public ProgressBar progressBar = null;
    private Button button;
    public String url = "195D0D?qbsrc=51&asr=4286";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        activity = MainActivity.this;
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "HELLO");
                Intent intent = new Intent(MainActivity.this,MyIntentService.class);
                intent.putExtra("url",url);
                startService(intent);
            }
        });
        progressBar = findViewById(R.id.progressbar);
        EventBus.getDefault().register(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(TestEvent event) {
        progressBar.setProgress(((int) event.getMsg()));
        if (progressBar.getProgress() >= 100) {
            button.setText("已下载");
            button.setClickable(false);
            Toast.makeText(context, "下载完成", Toast.LENGTH_LONG).show();
        }
        if (progressBar.getProgress() > 0 && progressBar.getProgress() < 100) {
            button.setText("正在下载");
            Log.i(TAG,progressBar.getProgress()+"");
        }
    }

}
