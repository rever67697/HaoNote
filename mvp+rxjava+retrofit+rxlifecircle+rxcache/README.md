# Mvp+Retrofit+Rxjava+RxLifecycle的使用

[TOC]

先上效果图:

 ![1](1.gif)

### 添加依赖

```java
	compile 'com.squareup.retrofit2:retrofit:2.3.0'
    compile 'com.squareup.retrofit2:converter-gson:2.3.0'
    compile 'com.squareup.retrofit2:adapter-rxjava2:2.3.0'
    compile 'com.squareup.okhttp3:logging-interceptor:3.8.0'
    compile 'io.reactivex.rxjava2:rxandroid:2.0.1'
    compile 'com.trello.rxlifecycle2:rxlifecycle-components:2.1.0'
```

​	使用`RxLifeCycle`是因为在使用Rxjava的过程中,当发布一个订阅后,页面被finsh,此时订阅的逻辑还没完成,容易引发内存泄漏的问题.

### 准备base类

##### 1.在BaseActivity中继承RxAppCompatActivity

```java
public class BaseActivity extends RxAppCompatActivity {
}
```

##### 2.在BasePresenter中写拿到LifecycleProvider的方法,方便后边的RetrofitService设置手动关闭订阅.

```java
public class BasePresenter {
    
    private LifecycleProvider<ActivityEvent> provider;

    public BasePresenter(LifecycleProvider<ActivityEvent> provider) {
        this.provider = provider;
    }

    public LifecycleProvider<ActivityEvent> getProvider() {
        return provider;
    }
}
```

当我们在activity中初始化presenter的时候,由于activity继承的RxAppCompatActivity,只需要传this就可以把LifecycleProvider<ActivityEvent>传过来了.

##### 3.使用mvp,需要新建抽象类BaseView

```java
public interface BaseView {

    /**
     * 显示Loading
     */
    void showProgressDialog();

    /**
     * 隐藏Loading
     */
    void hideProgressDialog();

    /**
     * 显示错误信息
     *
     * @param msg 错误信息
     */
    void showError(String msg);
}
```



### 准备接口地址类和请求参数接口类

##### 1.新建接口地址类Constant

```java
public class Constant {

    /**
     * 服务器地址(基类地址)
     */
    public static final String SERVER_URL = "http://www.kuaidi100.com/";

    /**
     * 接口请求地址
     */
    public static class UrlOrigin {
      
      //--------------------------------------------------
      //拼接的尾部地址都写下边
        /**
         * 获取快递信息
         */
        public static final String get_express_info = "query";
    }
}
```

##### 2.新建请求参数接口类RetrofitService

```java
public interface RetrofitService {

    /**
     * 获取快递信息
     * Rx方式
     * @return Observable<ExpressInfo>
     */
    @GET(Constant.UrlOrigin.get_express_info)
    Observable<ExpressInfo> getExpressInfoRx(@QueryMap Map<String,String> map);
}
```

​	注意: `@GET(Constant.UrlOrigin.get_express_info)`括号中的参数为Constant的尾部地址"query".

### 准备Retrofit工具类

##### 1.新建RetrofitHelper

​	初始化Retrofit基础配置.

```java
public class RetrofitHelper {

    private static RetrofitHelper retrofitHelper;
    private RetrofitService retrofitService;

    public static RetrofitHelper getInstance() {
        return retrofitHelper == null ? retrofitHelper = new RetrofitHelper() : retrofitHelper;
    }

    private RetrofitHelper() {
        // 初始化Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create()) // json解析
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // 支持RxJava
                .client(RetrofitUtils.getOkHttpClient()) //打印请求参数
                .build();
        retrofitService = retrofit.create(RetrofitService.class);
    }

    public RetrofitService getRetrofitService() {
        return retrofitService;
    }
}
```

​	可以通过getRetrofitService()方法,拿到RetrofitService对象,进而调接口.

##### 2.新建DataManager

​	数据处理.

```java
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

  //---------------------------------------------------------
  //从下边开始,就是各个接口的请求
    /**
     * 获取快递信息
     * @return Observable<ExpressInfo>
     */
    public Observable<ExpressInfo> getExpressInfo(Map<String,String> map) {
        return retrofitService.getExpressInfoRx(map);
    }
}
```

​	在DataManager中初始化RetrofitHelper,并通过RetrofitHelper重的getRetrofitService()方法拿到RetrofitService. 

​	然后在DataManager中做网络请求,返回拿到的javabean,如上面代码中的getExpressInfo()方法.

### 开始使用

##### 1.要使用mvp,需要新建抽象类ExpressView

```java
public interface ExpressView extends BaseView {

    /**
     * 更新UI
     *
     * @param expressInfo 快递信息
     */
    void updateView(ExpressInfo expressInfo);
}
```

ExpressView在presenter中设置,然后在activity中实现ExpressView,在回调updateView()中拿到javabean数据,然后做数据绑定等操作.

##### 2.在MainActivity中

```java
public class MainActivity extends BaseActivity implements ExpressView {

    @BindView(R.id.tv_post_info)
    TextView tvPostInfo;

    private ProgressDialog progressDialog;
    private ExpressPresenter expressPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        expressPresenter = new ExpressPresenter(this, this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在获取快递信息...");
    }

    @OnClick(R.id.btn_get_post_info)
    public void onViewClicked() {
        Map<String,String> map = new HashMap<>();
        map.put("type","yuantong");
        map.put("postid","11111111111");
       //开始做数据请求
        expressPresenter.getExpressInfo(map);
    }

    /**
     * 拿到数据,做数据绑定操作
     *
     * @param expressInfo 快递信息
     */
    @Override
    public void updateView(ExpressInfo expressInfo) {
        tvPostInfo.setText(expressInfo.toString());
    }

    /**
     * 数据请求时的
     * 显示加载框
     */
    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    /**
     * 数据加载完成的隐藏加载框
     */
    @Override
    public void hideProgressDialog() {
        progressDialog.hide();
    }

    /**
     * 显示错误信息
     * @param msg 错误信息
     */
    @Override
    public void showError(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
```

##### 3.新建presenter类ExpressPresenter

```java
public class ExpressPresenter extends BasePresenter {

    private ExpressView expressView;
    private DataManager dataManager;

    public ExpressPresenter(ExpressView expressView, LifecycleProvider<ActivityEvent> provider) {
        super(provider);
        this.expressView = expressView;
        dataManager = DataManager.getInstance();
    }

    /**
     * 获取快递信息
     */
    public void getExpressInfo(Map<String,String> map) {
        expressView.showProgressDialog();

        dataManager.getExpressInfo(map)
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
```

**注意:RxLifeCircle的手动/自动关闭代码就是在compose()中,关于RxLifeCircle请关注我写的RxLifeXCircle详解**

### 最后

​	好了,以后只需要在Constant里新加接口地址,在RetrofitService新加请求参数,在DataManager中新建个方法,请求网络,返回Observable<javabean>.

​	然后在presenter中的getExpressInfo()方法中,dataManager.get    DataManger的请求网络的方法.



demo地址：https://github.com/chinachance/Demos



### 附加:RxCache 缓存库的使用

##### 1.添加依赖

在整个项目的build.gradle中添加:

~~~~~~xml
allprojects {
    repositories {
        jcenter()
        maven { url "https://jitpack.io" }
    }
}
~~~~~~

在moudle的build.gradle中添加

~~~xml
compile "com.github.VictorAlbertos.RxCache:runtime:1.8.1-2.x"
//如果已经添加 implementation 'io.reactivex.rxjava2:rxandroid:2.0.1' ,则没必要添加下边的rxjava
compile "io.reactivex.rxjava2:rxjava:2.0.6"
~~~

##### 2.新建接口CacheProvider

~~~java
public interface CacheProvider {
    /**
     * 获取快递信息
     * Rx方式
     * @return Observable<ExpressInfo>
     */
    @ProviderKey("mocks") //可不写,用于标志
    @LifeCache(duration = 1, timeUnit = TimeUnit.MINUTES)   //可不写 , 缓存有效期1分钟
    Observable<ExpressInfo> getExpressInfoRx(Observable<ExpressInfo> oMocks, EvictProvider evictProvider);
}
~~~

##### 3.新建RetrofitCacheHelper

~~~java
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
		
        //rxcache初始化
        //获取缓存的文件存放路径
        File cacheDir =mContext.getFilesDir();
        mCacheProvider = new RxCache.Builder()
                .persistence(cacheDir, new GsonSpeaker())//配置缓存的文件存放路径，以及数据的序列化和反序列化
                .using(CacheProvider.class);    //和Retrofit相似，传入缓存API的接口

    }

    public CacheProvider getRetrofitService() {
        return mCacheProvider;
    }
}
~~~

##### 4.DataManager

~~~java
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

~~~

然后,在getExpressCacheInfo调用getExpressCacheInfo方法就好了.

