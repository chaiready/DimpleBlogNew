20190906更新
https://github.com/DimpleFeng/DimpleBlog


######################host文件######################
127.0.0.1 51it


######################nginx配置######################
 upstream 51it{ 
	#这里指定多个源服务器，ip:端口,80端口的话可写可不写 
	server 127.0.0.1:8080; 
	server 127.0.0.1:8081; 
    }

    server {
        listen       80;
        server_name  127.0.0.1;
        #charset koi8-r;
        #access_log  logs/host.access.log  main;
        location / {
        proxy_pass   http://51it;
        }
    }


