## Bugly热修复集成注意事项

1.**1.3.1及以上版本** 不用再配置**AndroidManifest** 。

2.打包执行命令：![1](C:\Users\Administrator\Desktop\HaoNote\Bugly热修复集成注意事项\1.png)

打补丁执行命令：

![2](C:\Users\Administrator\Desktop\HaoNote\Bugly热修复集成注意事项\2.png)



3.打补丁的时候，

~~~~java
/**
 * 此处填写每次构建生成的基准包目录
 */
def baseApkDir = "app-0926-17-51-24"
~~~~

这个包名要和之前的打的包名所在文件夹一样，这样才知道要打那个安装包的补丁。

~~~~java
tinkerId = "bd1-1.0.1"
~~~~

tinkerId每次打补丁都要不一样，而且后边版本号要一样，和安卓包版本号一致。

4.注意：打包之前一定要在app的build.gradle中加上签名配置信息，签名文件要放到默认位置，即app的文件夹下边，就是和`tinker-support.gradle`的文件在同一级目录，签名配置如下：

~~~~java
android {
 	......
    //签名
    signingConfigs {
        release {
            storeFile file('wallet.jks')
            storePassword '123456'
            keyAlias "wallet"
            keyPassword "123456"
        }

        debug {
            storeFile file('wallet.jks')
            storePassword '123456'
            keyAlias "wallet"
            keyPassword "123456"
        }
    }

    buildTypes {

        //设置打debug包和release包时的基类链接地址
        debug {
            buildConfigField("boolean", "LOG_DEBUG", "true")
            buildConfigField "String", "SERVER_HOST", "\"http://192.168.0.3:9100/\""
            minifyEnabled false
            //注意，这要引用签名配置信息
            signingConfig signingConfigs.debug
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }

        release {
            buildConfigField("boolean", "LOG_DEBUG", "false")
            buildConfigField "String", "SERVER_HOST", "\"http://192.168.0.3:9100/\""
            minifyEnabled false
            //注意，这要引用签名配置信息
            signingConfig signingConfigs.release
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }
    }
  }
~~~~

5.安装包生成目录在`app\build\bakApk`中, 补丁的生成目录在`app\build\outputs\patch\release`中,补丁上传选择`patch_signed_7zip.apk`.