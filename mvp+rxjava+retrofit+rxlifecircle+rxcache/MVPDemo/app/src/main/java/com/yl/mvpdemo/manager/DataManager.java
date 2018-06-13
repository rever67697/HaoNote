package com.yl.mvpdemo.manager;

import com.yl.mvpdemo.bean.ExpressInfo;
import com.yl.mvpdemo.net.RetrofitHelper;
import com.yl.mvpdemo.net.RetrofitService;

import java.util.Map;

import io.reactivex.Observable;

/**
 * 数据处理(初始化Retrofit,拿到RetrofitService,拿到接口地址,进行网络请求,返回Observable<ExpressInfo>)
 * Created by yangle on 2017/6/27.
 */

public class DataManager {

    private static DataManager dataManager;
    private RetrofitService retrofitService;

    public static DataManager getInstance() {
        return dataManager == null ? dataManager = new DataManager() : dataManager;
    }

    /**
     * 初始化Retrofit,拿到RetrofitService
     */
    private DataManager() {
        retrofitService = RetrofitHelper.getInstance().getRetrofitService();
    }

    /**
     * 获取快递信息
     * @return Observable<ExpressInfo>
     */
    public Observable<ExpressInfo> getExpressInfo(Map<String,String> map) {
        return retrofitService.getExpressInfoRx(map);
    }
}
