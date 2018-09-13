# 在build.gradle中根据打包方式不同切换测试和线上环境

[TOC]

在moudle的build.gradle的buildTypes中添加buildConfigField属性：

~~~~JAVA
android {
    .......
        
    buildTypes {
        release {
            buildConfigField("boolean", "LOG_DEBUG", "false")
            buildConfigField "String", "SERVER_HOST", "\"api.clife.cn/\""
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }

        debug {
            buildConfigField("boolean", "LOG_DEBUG", "true")
            buildConfigField "String", "SERVER_HOST", "\"200.200.200.50/\""
            minifyEnabled false//true：启用混淆,false:不启用
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }
    }
}
~~~~

在buildConfigField中设置线上环境和测试环境，测试环境打debug包，线上环境打release包。

`buildConfigField "String", "SERVER_HOST", "\"200.200.200.50/\""`中，第一个参数表示返回的数据类型，第二个表示定义的名称，第三个表示定义的值。

然后获取基类地址这样获取：

~~~~java
String url = BuildConfig.SERVER_HOST；
~~~~

