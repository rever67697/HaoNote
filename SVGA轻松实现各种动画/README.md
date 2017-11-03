# SVGA轻松实现各种动画

github地址:https://github.com/yyued/SVGAPlayer-Android

先看下效果图:

 ![1](1.gif)

### 添加依赖:

~~~~JAVA
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
~~~~

~~~~java
compile 'com.github.yyued:SVGAPlayer-Android:2.0.0'
~~~~

### 添加本地动画只需要在布局引用即可:

~~~~html
 <com.opensource.svgaplayer.SVGAImageView
        android:layout_height="match_parent"
        android:layout_width="match_parent"
        app:source="posche.svga"
        app:autoPlay="true"
        android:background="#000" />
~~~~

`app:source`指定相对应的.svga文件即可.

### 添加网络加载完整代码:

~~~~JAVA
/**
 * Created by cuiminghui on 2017/3/30.
 * 这是最复杂的一个 Sample， 演示了从网络加载动画，并播放动画。
 * 更多的 Sample 可以在这里找到 https://github.com/yyued/SVGA-Samples
 */

public class MainActivity extends AppCompatActivity {

    SVGAImageView testView = null;
    SVGADynamicEntity dynamicItem = new SVGADynamicEntity();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        testView = new SVGAImageView(this);
        testView.setBackgroundColor(Color.GRAY);
        loadAnimation();
        setContentView(testView);
    }

    private void loadAnimation() {
        SVGAParser parser = new SVGAParser(this);
        resetDownloader(parser);
        try {
            parser.parse(new URL("https://github.com/yyued/SVGA-Samples/blob/master/angel.svga?raw=true"), new SVGAParser.ParseCompletion() {
                @Override
                public void onComplete(@NotNull SVGAVideoEntity videoItem) {
                    SVGADrawable drawable = new SVGADrawable(videoItem);
                    testView.setImageDrawable(drawable);
                    testView.startAnimation();
                }
                @Override
                public void onError() {

                }
            });
        } catch (Exception e) {
            System.out.print(true);
        }
    }

    /**
     * 设置下载器，这是一个可选的配置项。
     * @param parser
     */
    private void resetDownloader(SVGAParser parser) {
        parser.setFileDownloader(new SVGAParser.FileDownloader() {
            @Override
            public void resume(final URL url, final Function1<? super InputStream, Unit> complete, final Function1<? super Exception, Unit> failure) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder().url(url).get().build();
                        try {
                            Response response = client.newCall(request).execute();
                            complete.invoke(response.body().byteStream());
                        } catch (IOException e) {
                            e.printStackTrace();
                            failure.invoke(e);
                        }
                    }
                }).start();
            }
        });
    }

}
~~~~



