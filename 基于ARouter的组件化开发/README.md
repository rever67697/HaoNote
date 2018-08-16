## 基于ARouter的组件化开发

[TOC]

### 添加依赖

在整个项目的build.gradle中添加:

~~~~java
apply plugin: 'com.alibaba.arouter'
buildscript {
    dependencies {
        classpath "com.alibaba:arouter-register:1.0.2"
    }
}
~~~~

在需要跳转和跳转到的moudle的build.gradle中添加依赖:

~~~~java
android {
    defaultConfig {
        ...
        javaCompileOptions {
            annotationProcessorOptions {
                arguments = [AROUTER_MODULE_NAME: project.getName()]
            }
        }
    }
}

dependencies {
    // 替换成最新版本, 需要注意的是api
    // 要与compiler匹配使用，均使用最新版可以保证兼容
    compile 'com.alibaba:arouter-api:1.4.0'
    annotationProcessor 'com.alibaba:arouter-compiler:1.2.0'
}
~~~~

然后,需要在主项目的`moudle`的`application`的`onCreate()`中初始化ARouter.

~~~~java
public class MyApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        if (isDebug()) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this); // 尽可能早，推荐在Application中初始化
    }

    private boolean isDebug() {
        return true;
    }
}
~~~~

### 开始使用

#### 简单跳转(不带参数)

比如说,我要从这个页面跳转到其他`moudle`的页面`LibraryTwoActivity`中.

1.`LibraryTwoActivity`添加`@Route`注解

~~~~JAVA
@Route(path = "/test/libraryTwo")
public class LibraryTwoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_two);
    }
}
~~~~

2.跳转

~~~~java
ARouter.getInstance().build("/test/libraryTwo").navigation
~~~~

注意: path至少需要两个层级,如上所示, `/test`表示分组, 不同moudle中的组名不能相同.例如,有两个moudle中的path都是`/test/**` , 即第一个的组名相同,编译的时候会报错,所以不同moudle分成不同的组名.

#### 带参数跳转

~~~~java
ARouter.getInstance().build("/test/libraryTwo")
			.withLong("key1", 666L)
			.withString("key3", "888")
			.withObject("key4", new Test("Jack", "Rose"))
			.navigation();
~~~~

在接收时直接当做intent传值就可以了, 如: `getIntent().getStringExtra("key");`  

### 拦截器的使用

路由跳转的时候可以使用拦截器全局拦截.

~~~~java
// 比较经典的应用就是在跳转过程中处理登陆事件，这样就不需要在目标页重复做登陆检查
// 拦截器会在跳转之间执行，多个拦截器会按优先级顺序依次执行
@Interceptor(priority = 8, name = "测试用拦截器")
public class TestInterceptor implements IInterceptor {

    /**
     * @param postcard
     * @param callback
     */
    @Override
    public void process(Postcard postcard, InterceptorCallback callback) {

        if (postcard.getExtra() == 1) {
            //extra == 1的时候不拦截 , 其他时候拦截
            // 处理完成，交还控制权
            callback.onContinue(postcard);
        }else {
            // 觉得有问题，中断路由流程(回调中处理)
            callback.onInterrupt(new RuntimeException("我觉得有点异常"));

        }
    }

    @Override
    public void init(Context context) {
        // 拦截器的初始化，会在sdk初始化的时候调用该方法，仅会调用一次
    }
}
~~~~

如上述代码,我们可以在要跳转的activity的注解中设置extra.

~~~~java
@Route(path = "/test/activity", extras = 1)
~~~~

结合起来理解, 比如说当extras等于1的时候 , 不需要登录就可以进入界面,所以我们在拦截器中, `postcard.getExtra()`获得extras的值 , 当等于1的时候,就是不需要登录,拦截器不拦截, 最后执行`callback.onContinue(postcard);`方法 , 如果不等于1 ,拦截器就需要拦截 , 可以自定义提示信息

`callback.onInterrupt(new RuntimeException("我觉得有点异常"));`,这时候就需要我们自己去处理.



~~~~java
//@Route注解中,path命名,如"/test/activity",第一个test表示组,不同moudle不能分成一个组
                ARouter.getInstance().build("/test/libraryTwo").navigation(MainActivity.this, new NavigationCallback() {
                    @Override
                    public void onFound(Postcard postcard) {
                        //开始先调用次方法,不管能不能跳转
                    }

                    @Override
                    public void onLost(Postcard postcard) {
                    }

                    @Override
                    public void onArrival(Postcard postcard) {
                        //跳转成功调用此方法
                    }

                    @Override
                    public void onInterrupt(Postcard postcard) {
                        //被拦截器拦截调用此方法  拦截器传递的报错信息在 postcard.tag 里边.
                        //直接跳转到登录页
                        ARouter.getInstance().build("/app/login").navigation();
                    }
~~~~



