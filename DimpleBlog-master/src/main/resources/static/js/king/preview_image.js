//js本地图片预览，兼容ie[6-9]、火狐、Chrome17+、Opera11+、Maxthon3
 function PreviewImage(fileObj,imgPreviewId,divPreviewId){
	 var allowExtention=".jpg,.bmp,.gif,.png,.tif,.tiff";//允许上传文件的后缀名document.getElementById("hfAllowPicSuffix").value;
	 var extention=fileObj.value.substring(fileObj.value.lastIndexOf(".")+1).toLowerCase();            
	 var browserVersion= window.navigator.userAgent.toUpperCase();
	 if(allowExtention.indexOf(extention)>-1){ 
		 if(fileObj.files){//HTML5实现预览，兼容chrome、火狐7+等
			 if(window.FileReader){
				 var reader = new FileReader(); 
				 reader.onload = function(e){
					 document.getElementById(imgPreviewId).setAttribute("src",e.target.result);
				 }  
				 reader.readAsDataURL(fileObj.files[0]);
			 }else if(browserVersion.indexOf("SAFARI")>-1){
				 alert("不支持Safari6.0以下浏览器的图片预览!");
			 }
		 }else if (browserVersion.indexOf("MSIE")>-1){
			 if(browserVersion.indexOf("MSIE 6")>-1){//ie6
				 document.getElementById(imgPreviewId).setAttribute("src",fileObj.value);
			 }else{//ie[7-9]
				 fileObj.select();
				 if(browserVersion.indexOf("MSIE 9")>-1)
					 fileObj.blur();//不加上document.selection.createRange().text在ie9会拒绝访问
				 var newPreview =document.getElementById(divPreviewId+"New");
				 if(newPreview==null){
					 newPreview =document.createElement("div");
					 newPreview.setAttribute("id",divPreviewId+"New");
					 newPreview.style.width = document.getElementById(imgPreviewId).width+"px";
					 newPreview.style.height = document.getElementById(imgPreviewId).height+"px";
					 newPreview.style.border="solid 1px #d2e2e2";
				 }
				 newPreview.style.filter="progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod='scale',src='" + document.selection.createRange().text + "')";                            
				 var tempDivPreview=document.getElementById(divPreviewId);
				 tempDivPreview.parentNode.insertBefore(newPreview,tempDivPreview);
				 tempDivPreview.style.display="none";                    
			 }
		 }else if(browserVersion.indexOf("FIREFOX")>-1){//firefox
			 var firefoxVersion= parseFloat(browserVersion.toLowerCase().match(/firefox\/([\d.]+)/)[1]);
			 if(firefoxVersion<7){//firefox7以下版本
				 document.getElementById(imgPreviewId).setAttribute("src",fileObj.files[0].getAsDataURL());
			 }else{//firefox7.0+                    
				 document.getElementById(imgPreviewId).setAttribute("src",window.URL.createObjectURL(fileObj.files[0]));
			 }
		 }else{
			 document.getElementById(imgPreviewId).setAttribute("src",fileObj.value);
		 }         
	 }else{
		 alert("请上传"+allowExtention+"格式的文件!");
		 fileObj.value="";//清空选中文件
		 if(browserVersion.indexOf("MSIE")>-1){                        
			 fileObj.select();
			 document.selection.clear();
		 }                
		 fileObj.outerHTML=fileObj.outerHTML;
	 }
 }
 
	//查看大图
	function imgShow(outerdiv, innerdiv, bigimg, _this){
		var src = _this.attr("src");//获取当前点击的pimg元素中的src属性
		$(bigimg).attr("src", src);//设置#bigimg元素的src属性

			/*获取当前点击图片的真实大小，并显示弹出层及大图*/
		$("<img/>").attr("src", src).load(function(){
			var windowW = $(window).width();//获取当前窗口宽度
			var windowH = $(window).height();//获取当前窗口高度
			var realWidth = this.width;//获取图片真实宽度
			var realHeight = this.height;//获取图片真实高度
			var imgWidth, imgHeight;
			var scale = 0.8;//缩放尺寸，当图片真实宽度和高度大于窗口宽度和高度时进行缩放
			
			if(realHeight>windowH*scale) {//判断图片高度
				imgHeight = windowH*scale;//如大于窗口高度，图片高度进行缩放
				imgWidth = imgHeight/realHeight*realWidth;//等比例缩放宽度
				if(imgWidth>windowW*scale) {//如宽度扔大于窗口宽度
					imgWidth = windowW*scale;//再对宽度进行缩放
				}
			} else if(realWidth>windowW*scale) {//如图片高度合适，判断图片宽度
				imgWidth = windowW*scale;//如大于窗口宽度，图片宽度进行缩放
							imgHeight = imgWidth/realWidth*realHeight;//等比例缩放高度
			} else {//如果图片真实高度和宽度都符合要求，高宽不变
				//imgWidth = realWidth;
				//imgHeight = realHeight;
				imgHeight = windowH*scale;//如大于窗口高度，图片高度进行缩放
				imgWidth = imgHeight/realHeight*realWidth;//等比例缩放宽度
			}
					$(bigimg).css("width",imgWidth);//以最终的宽度对图片缩放
			
			var w = (windowW-imgWidth)/2;//计算图片与窗口左边距
			var h = (windowH-imgHeight)/2;//计算图片与窗口上边距
			$(innerdiv).css({"top":h, "left":w});//设置#innerdiv的top和left属性
			$(outerdiv).fadeIn("fast");//淡入显示#outerdiv及.pimg
		});
		
		$(outerdiv).click(function(){//再次点击淡出消失弹出层
			$(this).fadeOut("fast");
		});
	}