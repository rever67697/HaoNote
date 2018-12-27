# SpringBoot部署项目到服务器

[TOC]

1.在`porm.xml`修改打包方式：

~~~~
<packaging>jar</packaging>
~~~~

2.在`application.properties`中添加端口：

~~~~
server.port=8081
~~~~



然后，右侧运行 --> Lifecycle-->package,打成jar包。

然后再target目录中找到jar包，上传到服务器，服务器需要集成java环境。

最后，到jar包所在目录，运行命令：

~~~~
$ nohup java -jar test.jar >temp.txt &
~~~~

其中test.jar是jar包名。

然后访问 ：  服务器地址：在`application.properties`中配置的端口号/... 就可以了。



**注意：服务器重新启动，需要重新部署一下项目，即重新执行一下命令：**

~~~~
$ nohup java -jar test.jar >temp.txt &
~~~~

