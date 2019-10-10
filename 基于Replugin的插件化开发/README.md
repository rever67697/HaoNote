#基于Replugin的插件化开发

[TOC]

##添加依赖

###添加宿主项目的依赖

在**项目根目录**的 build.gradle 中添加:

~~~~java
buildscript {
    dependencies {
        //添加此依赖库
        classpath 'com.qihoo360.replugin:replugin-host-gradle:2.2.4'
    }
}
~~~~

在moudle的build.gradle中添加依赖:

~~~~java
//插件放在android{}后边
apply plugin: 'replugin-host-gradle'

dependencies {
    implementation 'com.qihoo360.replugin:replugin-host-lib:2.2.4'
}

/**
 * 配置项均为可选配置，默认无需添加
 * 更多可选配置项参见replugin-host-gradle的RepluginConfig类
 * 可更改配置项参见 自动生成RePluginHostConfig.java
 */
repluginHostConfig {
    /**
     * 是否使用 AppCompat 库
     * 不需要个性化配置时，无需添加
     */
    useAppCompat = true
    /**
     * 背景不透明的坑的数量
     * 不需要个性化配置时，无需添加
     */
    countNotTranslucentStandard = 6
    countNotTranslucentSingleTop = 2
    countNotTranslucentSingleTask = 3
    countNotTranslucentSingleInstance = 2
}
~~~~

注意: `apply plugin`要写在android{}的后边 , 其实就是写在applicationId的后边,而且applicationId要明确申明包名.如: 

~~~~java
 applicationId "com.example.administrator.replugindemo"
~~~~

在`baseApplication`中继承:

~~~~java
public class MainApplication extends RePluginApplication {
}
~~~~



### 添加插件项目的依赖

在**项目根目录**的 build.gradle 中添加:

~~~~java
repositories {
    jcenter()
}

dependencies {
    classpath 'com.qihoo360.replugin:replugin-plugin-gradle:2.2.4'
}
~~~~

在moudle的build.gradle中添加依赖:

~~~~java
//插件放在android{}后边
apply plugin: 'replugin-plugin-gradle'

dependencies {
    implementation 'com.qihoo360.replugin:replugin-plugin-lib:2.2.4'
}
~~~~

然后就可以把插件打包使用了.

​	

## 简单使用

在assets文件夹下新建文件夹external,放入打包好的apk,随意命名为app.apk.

~~~~java
private void initView() {
        tv = (TextView) findViewById(R.id.tv);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simulateInstallExternalPlugin();
                Log.v("hao", "MainActivity onClick(): " + RePlugin.isPluginInstalled("app"));
            }
        });
    }


    /**
     * 模拟安装或升级（覆盖安装）外置插件
     * 注意：为方便演示，外置插件临时放置到Host的assets/external目录下，具体说明见README</p>
     */
    private void simulateInstallExternalPlugin() {
        String demo3Apk = "app.apk";
        String demo3apkPath = "external" + File.separator + demo3Apk;

        // 文件是否已经存在？直接删除重来
        String pluginFilePath = getFilesDir().getAbsolutePath() + File.separator + demo3Apk;
        File pluginFile = new File(pluginFilePath);
        if (pluginFile.exists()) {
            FileUtils.deleteQuietly(pluginFile);
        }

        // 开始复制
        copyAssetsFileToAppFiles(demo3apkPath, demo3Apk);
        PluginInfo info = null;
        if (pluginFile.exists()) {
            info = RePlugin.install(pluginFilePath);
        }

        if (info != null) {
            Intent intent = RePlugin.createIntent(info.getName(), "com.example.administrator.replugintwodemo.MainActivity");
            intent.putExtra("text","这是传过来的值");
            RePlugin.startActivity(MainActivity.this, intent);
        } else {
            Toast.makeText(MainActivity.this, "install external plugin failed", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 从assets目录中复制某文件内容
     *
     * @param assetFileName assets目录下的Apk源文件路径
     * @param newFileName   复制到/data/data/package_name/files/目录下文件名
     */
    private void copyAssetsFileToAppFiles(String assetFileName, String newFileName) {
        InputStream is = null;
        FileOutputStream fos = null;
        int buffsize = 1024;

        try {
            is = this.getAssets().open(assetFileName);
            fos = this.openFileOutput(newFileName, Context.MODE_PRIVATE);
            int byteCount = 0;
            byte[] buffer = new byte[buffsize];
            while ((byteCount = is.read(buffer)) != -1) {
                fos.write(buffer, 0, byteCount);
            }
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
~~~~



跳转到插件我们只需要这些代码:

~~~~java
Intent intent = RePlugin.createIntent("com.example.administrator.replugintwodemo","com.example.administrator.replugintwodemo.MainActivity");
intent.putExtra("text","这是传过来的值");
RePlugin.startActivity(MainActivity.this, intent);
~~~~

其中createIntent()方法中,第一个参数表示要跳转的插件的包名,第二个参数表示要跳转的类名(包名+类名).