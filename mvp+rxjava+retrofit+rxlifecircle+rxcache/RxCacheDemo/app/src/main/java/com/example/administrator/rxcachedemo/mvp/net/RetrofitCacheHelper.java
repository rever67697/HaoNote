package com.example.administrator.rxcachedemo.mvp.net;

import android.content.Context;

import java.io.File;

import io.rx_cache2.internal.RxCache;
import io.victoralbertos.jolyglot.GsonSpeaker;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * ━━━━━━神兽出没━━━━━━
 * 　　　┏┓　　　┏┓
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　　　　　┃
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　　　　　┃
 * 　　┃　　　┻　　　┃
 * 　　┃　　　　　　　┃
 * 　　┗━┓　　　┏━┛
 * 　　　　┃　　　┃  神兽保佑
 * 　　　　┃　　　┃  代码无bug
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * ━━━━━━感觉萌萌哒━━━━━━
 *
 * @author hao
 * @date 2017/10/30
 * @description retrofit缓存帮助类
 */

public class RetrofitCacheHelper {

    private static RetrofitCacheHelper retrofitHelper;
    /**
     * 进行缓存的数据的接口
     */
    private CacheProvider mCacheProvider;
    private static Context mContext;

    public static RetrofitCacheHelper getInstance(Context context) {
        mContext = context;
        return retrofitHelper == null ? retrofitHelper = new RetrofitCacheHelper() : retrofitHelper;
    }

    private RetrofitCacheHelper() {
        // 初始化Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create()) // json解析
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // 支持RxJava
                .client(RetrofitUtils.getOkHttpClient()) //打印请求参数
                .build();
        this.mCacheProvider = retrofit.create(CacheProvider.class);

        //获取缓存的文件存放路径
        File cacheDir =mContext.getFilesDir();
        mCacheProvider = new RxCache.Builder().useExpiredDataIfLoaderNotAvailable(true)
                .persistence(cacheDir, new GsonSpeaker())//配置缓存的文件存放路径，以及数据的序列化和反序列化
                .using(CacheProvider.class);    //和Retrofit相似，传入缓存API的接口

    }

    public CacheProvider getRetrofitService() {
        return mCacheProvider;
    }
}
