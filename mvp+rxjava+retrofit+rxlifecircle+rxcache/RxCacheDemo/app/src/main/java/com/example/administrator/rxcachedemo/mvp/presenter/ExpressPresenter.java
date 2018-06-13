package com.example.administrator.rxcachedemo.mvp.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.administrator.rxcachedemo.mvp.bean.ExpressInfo;
import com.example.administrator.rxcachedemo.mvp.manager.DataManager;
import com.example.administrator.rxcachedemo.mvp.view.ExpressView;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

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
 * @description 例子presenter
 */

public class ExpressPresenter extends BasePresenter {

    private ExpressView expressView;
    private DataManager dataManager;

    public ExpressPresenter(Context context, ExpressView expressView, LifecycleProvider<ActivityEvent> provider) {
        super(provider);
        this.expressView = expressView;
        dataManager = DataManager.getInstance(context);
    }

    /**
     * 获取快递信息
     * @param map
     * @param update 是否加载最新数据(是否清除缓存)
     */
    public void getExpressInfo(Map<String, String> map,boolean update) {
        expressView.showProgressDialog();

        //调用DataManger中的getExpressInfo方法,进行网络请求,拿到Observable<ExpressInfo>
        dataManager.getExpressCacheInfo(map,update)
                .subscribeOn(Schedulers.io()) // 在子线程中进行Http访问
                .observeOn(AndroidSchedulers.mainThread()) // UI线程处理返回接口
                .compose(getProvider().<ExpressInfo>bindUntilEvent(ActivityEvent.DESTROY)) // onDestroy取消订阅
                .subscribe(new DefaultObserver<ExpressInfo>() {  // 订阅
                    @Override
                    public void onNext(@NonNull ExpressInfo expressInfo) {
                        expressView.updateView(expressInfo);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        expressView.showError(e.getMessage());
                        expressView.hideProgressDialog();
                    }

                    @Override
                    public void onComplete() {
                        expressView.hideProgressDialog();
                    }
                });
    }

}
