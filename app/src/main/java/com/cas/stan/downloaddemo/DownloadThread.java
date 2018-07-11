package com.cas.stan.downloaddemo;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class DownloadThread implements Runnable {
    public static final String TAG = "DownloadThread";
    public String url = "195D0D?qbsrc=51&asr=4286";
    public String apkName = "demo.apk";
    public String savePath = "mnt/sdcard/Download/";
    public File file;
    public int start;
    public int end;

    public DownloadThread(File file, int start, int end) {
        this.file = file;
        this.start = start;
        this.end = end;
    }

    @Override
    public synchronized void run() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://surl.qq.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        DownloadFileService downloadFileService =
                retrofit.create(DownloadFileService.class);
        downloadFileService.downloadPartialFile(url,"bytes=" + start + "-" + end)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        Log.d(TAG, "onSubscribe");
                    }
                    @Override
                    public void onNext(ResponseBody responseBody) {
                        Log.d(TAG, "onNext");
                        try {
                            OutputStream os;
                            InputStream inputStream = responseBody.byteStream();

                                os = new FileOutputStream(file);
                                int len;
                                byte[] buff = new byte[1024*8];
                                while ((len = inputStream.read(buff)) != -1) {
                                    os.write(buff, 0, len);
                                }
                                os.close();
                        } catch (IOException io) {
                            io.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        Log.e(TAG, "ERROR");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
