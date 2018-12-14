

# 域名解析以及配置服务器Nginx环境

[TOC]

买过域名解析好域名，还需要我们的服务器安装Nginx才可以访问。

## 安装好Nginx

根据链接安装就行：https://www.cnblogs.com/AlanLee/p/9044644.html

其中，安装c++环境是linux系统安装，如果使用ununtu系统，请自行搜索命令安装。

##配置Nginx

照着链接博客操作到配置nginx.conf ，如果按照博客上边的配置，如果你的项目有设置端口号，访问的时候还是要输入`域名：端口号`。

所以，我们如果想直接域名访问，不需要显示端口号，需进行以下配置：

1.进入Nginx安装目录，`/usr/local/webserver/nginx/conf`进入config文件夹，修改`nginx.conf`文件。

~~~~java
#博客上一步设置的，创建 Nginx 运行使用的用户
user www www;
#服务器cpu数
worker_processes  1;

#error_log  logs/error.log;
#error_log  logs/error.log  notice;
#error_log  logs/error.log  info;

#pid        logs/nginx.pid;


events {
    worker_connections  1024;
}


http {
    #路径修改为Nginx安装目录的路径（也就是nginx.conf这个文件的路径）
    include       /usr/local/webserver/nginx/conf/mime.types;
	#include vhost;
    default_type  application/octet-stream;

    #log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
    #                  '$status $body_bytes_sent "$http_referer" '
    #                  '"$http_user_agent" "$http_x_forwarded_for"';

    #access_log  logs/access.log  main;
	
    #路径修改为Nginx安装目录的路径,这个路径就是下一步操作的创建vhost的路径。
	include /usr/local/webserver/nginx/conf/vhost/*.conf;

    sendfile        on;
    #tcp_nopush     on;

    #keepalive_timeout  0;
    keepalive_timeout  65;

    #gzip  on;



    # another virtual host using mix of IP-, name-, and port-based configuration
    #
    #server {
    #    listen       8000;
    #    listen       somename:8080;
    #    server_name  somename  alias  another.alias;

    #    location / {
    #        root   html;
    #        index  index.html index.htm;
    #    }
    #}


    # HTTPS server
    #
    #server {
    #    listen       443 ssl;
    #    server_name  localhost;

    #    ssl_certificate      cert.pem;
    #    ssl_certificate_key  cert.key;

    #    ssl_session_cache    shared:SSL:1m;
    #    ssl_session_timeout  5m;

    #    ssl_ciphers  HIGH:!aNULL:!MD5;
    #    ssl_prefer_server_ciphers  on;

    #    location / {
    #        root   html;
    #        index  index.html index.htm;
    #    }
    #}

}
~~~~



2.然后在Nginx的安装目录的conf文件夹中创建一个文件，命名为`vhost`,在vhost文件夹中创建文件，hao.conf。

~~~~java
server {
    #监听端口,为80端口表示80下边的端口都可以监听，如：8080,8081
    listen 80;
    #域名
    server_name www.xiezihao.我爱你;
	
	location / {
        #项目的端口号，前边的ip地址不需要变，只改端口号即可
		proxy_pass  http://127.0.0.1:8081;
		proxy_redirect     off;
        proxy_set_header   Host             $host:$server_port;
        proxy_set_header   X-Real-IP        $remote_addr;
        proxy_set_header   X-Forwarded-For  $proxy_add_x_forwarded_for;
        proxy_next_upstream error timeout invalid_header http_500 http_502 http_503 http_504;
        proxy_max_temp_file_size 0;
        proxy_connect_timeout      100;
        proxy_send_timeout         100;
        proxy_read_timeout         100;
        proxy_buffer_size          32k;
        proxy_buffers              16 128k;
        proxy_busy_buffers_size    256k;
        proxy_temp_file_write_size 512k;
    }
}
~~~~

配置过之后，启动Nginx就可以使用啦。

