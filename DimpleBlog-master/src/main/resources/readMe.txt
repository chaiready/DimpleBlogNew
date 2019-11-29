20190906更新
https://github.com/DimpleFeng/DimpleBlog

www.u311.com
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


[[${'当前共有 '+comments.size()+' 条评论'}]]


<li th:each="func,funcStat:${funcList}"  th:class="${funcStat.index==0}? 'active' : ''">
    <a th:href="@{#func_}+${func.funcId}" data-toggle="tab" th:attr="aria-expanded=${funcStat.first}" th:text="${func.funcName}"></a>
</li>


<a class="btn btn-success btn-xs " href="javascript:void(0)" th:onclick="openModal('/blog/blog/edit/[[${blog.blogId}]]');"><i class="fa fa-edit"></i> 编辑</a>




左右翻页
zzsc.css