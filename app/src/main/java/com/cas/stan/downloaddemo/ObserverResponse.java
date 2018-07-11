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
import okhttp3.ResponseBody;

public class ObserverResponse implements Observer<ResponseBody> {
    private static final String TAG = "ObserverResponse";

    @Override
    public void onSubscribe(Disposable disposable) {
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onNext(ResponseBody responseBody) {
        try {
            long currentLength = 0;
            OutputStream os;
            InputStream inputStream = responseBody.byteStream();
            long total = responseBody.contentLength();
            File file = new File(Constants.SAVE_PATH + Constants.filaName);
            if (!file.exists()) {
                if (file.createNewFile()) {
                    os = new FileOutputStream(file);
                    int len;
                    byte[] buff = new byte[1024*10];
                    while ((len = inputStream.read(buff)) != -1) {
                        os.write(buff, 0, len);
                        currentLength += len;
                        EventBus.getDefault().post(new TestEvent((double) 100*currentLength / total));
                    }
//                    Log.i(TAG,currentLength+"");

                }

            }
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    @Override
    public void onError(Throwable throwable) {
    }
}
