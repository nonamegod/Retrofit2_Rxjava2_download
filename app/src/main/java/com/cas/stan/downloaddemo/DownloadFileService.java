package com.cas.stan.downloaddemo;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface DownloadFileService {
    String url ="http://surl.qq.com/";
    @Streaming //大文件时要加不然会OOM
    @GET
    Observable<ResponseBody> downloadFile(@Url String fileUrl);

    @Streaming
    @GET
    Observable<ResponseBody> downloadPartialFile(@Url String url , @Header("Range")String range);
}
