20190906更新
https://github.com/DimpleFeng/DimpleBlog


管理员：小西瓜

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


######################layer 提示######################
$.modal.alertSuccess(result.msg)
$.modal.alertWarning(result.msg)
$.modal.alertError(result.msg);

if (result.code == web_status.SUCCESS) {
                        $.modal.alertSuccess(result.msg);
                        window.parent.location.reload();
                    }else{
                        $.modal.alertWarning(result.msg);
                    }


$.modal.confirm("确认要删除该文件吗？", function () {
            $.operate.ajaxSend("/common/delFile", "post", "json", {"fileId":fileId}, function callBackFunc(result){
                console.info(result);
                $.modal.closeLoading();
                if(result.code == web_status.SUCCESS){
                    delAttachment(fileId)
                    $.modal.msgSuccess(result.msg);
                }else{
                    $.modal.alertWarning(result.msg)
                }
            });
        })

        
        
 右下角支付码鼠标不能停留在二维码上面
 博客里的打赏二维码没有居中
        
        