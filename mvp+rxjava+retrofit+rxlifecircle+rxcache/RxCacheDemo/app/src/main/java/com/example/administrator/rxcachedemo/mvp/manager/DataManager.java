package com.example.administrator.rxcachedemo.mvp.manager;

import android.content.Context;

import com.example.administrator.rxcachedemo.mvp.bean.ExpressInfo;
import com.example.administrator.rxcachedemo.mvp.net.CacheProvider;
import com.example.administrator.rxcachedemo.mvp.net.RetrofitCacheHelper;
import com.example.administrator.rxcachedemo.mvp.net.RetrofitHelper;
import com.example.administrator.rxcachedemo.mvp.net.RetrofitService;

import java.util.Map;

import io.reactivex.Observable;
import io.rx_cache2.EvictProvider;

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
 * @description 数据处理(初始化Retrofit,拿到RetrofitService,拿到接口地址,进行网络请求,返回Observable<javabean>)
 */

public class DataManager {

    private static DataManager dataManager;
    /**
     * 未进行缓存的数据的接口
     */
    private RetrofitService retrofitService;
    /**
     * 进行缓存的数据的接口
     */
    private CacheProvider mCacheProvider;
    private static Context mContext;

    public static DataManager getInstance(Context context) {
        mContext = context;
        return dataManager == null ? dataManager = new DataManager() : dataManager;
    }

    /**
     * 初始化Retrofit,拿到RetrofitService和CacheProvider
     */
    private DataManager() {
        //未缓存数据的RetrofitService的实例化
        retrofitService = RetrofitHelper.getInstance().getRetrofitService();
        //缓存数据的CacheProvider的实例化
        mCacheProvider = RetrofitCacheHelper.getInstance(mContext).getRetrofitService();
    }


    /************下边开始进行网络请求,在各自的presenter中调用各自的下边的网络请求的方法,在presenter中拿到Observable<javabean>*****************/


    /**
     * 获取快递信息(无缓存)
     * @return Observable<ExpressInfo>
     */
    public Observable<ExpressInfo> getExpressInfo(Map<String,String> map) {
        return retrofitService.getExpressInfoRx(map);
    }

    /**
     * 获取快递信息(有缓存)
     * @param map
     * @param update 是否清除所有缓存
     * @return Observable<ExpressInfo>
     */
    public Observable<ExpressInfo> getExpressCacheInfo(Map<String,String> map , boolean update) {
        return mCacheProvider.getExpressInfoRx(retrofitService.getExpressInfoRx(map),new EvictProvider(update));
    }
}
