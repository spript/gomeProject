uploadVideo();
function uploadVideo(){
	let flashContent = document.getElementById('flashContent');
	let swfVersionStr = "11.4.0";
	let xiSwfUrlStr = "expressInstall.swf";
	var flashvars = {
		// env : "dev",
		// appname : "h5",
		// user_id : "1",
		// description : "测试数据",
		// tag : "视频,新闻,体育"
		// icon : 'http://10.69.205.18:8000/static/img/videoIcon.png'
	};
	var params = {};
	params.quality = "high";
	params.bgcolor = "#fff";
	params.allowscriptaccess = "always";
	params.allowfullscreen = "true";
	var attributes = {};
	attributes.id = "video-upload";
	attributes.name = "video-upload";
	attributes.align = "middle";
	attributes.bgcolor="#fff";
	swfobject.embedSWF(
		"https://js.meixincdn.com/gvs-player/dist/utils/video-upload.swf?t=" + new Date().getTime(),
		"flashContent",
		"100", "35",
		swfVersionStr, xiSwfUrlStr,
		flashvars, params, attributes);
	swfobject.createCSS(flashContent, "display:block;text-align:left;");

};
function uploadInited(){
	console.log("uploadInited");
}
//打开文件选择器
function fileBrowse(){
	console.log("open");
	ajax({
		type:"get",
		url:"/api/video/token",
		dataType:"json",
		data:{},
		success:function(msg){
			console.log(msg);
			thisFlash("video-upload").setData({
				env: msg.data.env,
				appname: msg.data.appname,
				token: msg.data.token,
				is_published:1,
				// env:"dev",
				// appkey: "bf461da5418a6200",
				// appname: advert,
				user_id: "11",
				description: "",
				tag: ""
			});
		},
		error:function(){
			console.log("error")
		}
	})
}
//准备上传
function uploadPreparing() {
	console.log('uploadPreparing');
	var tip = document.getElementById('videoIdsTip');
	tip.innerHTML='正在上传，请等待！';
}
//上传视频开始
function startUpload(data) {
	console.log("startUpload");
	console.log(data);
}

//上传视频进度
function progress(data) {
	console.log(data);
	var progressBox = document.getElementById('progressBox');
	var progress = document.getElementById('progress');
	progressBox.style.display='block';
	progress.value = Math.round(data.bytesLoaded/data.bytesTotal*100);
	if(progress.value == 100){
		setTimeout(function(){
			progressBox.style.display='none';
		},5000)
	}
}

//上传视频结束
function uploadComplete(data) {
	console.log("uploadComplete videoId : " + data.videoId);
	window.UploadVideoId = data.videoId;
	var tip = document.getElementById('videoIdsTip');
	tip.innerHTML='上传成功！';
	setTimeout(function(){
		tip.innerHTML = '';
	},5000)
}

function error(data) {
	console.log("error : " + data.message + " , id : " + data.data);
	var tip = document.getElementById('videoIdsTip');
	tip.innerHTML='上传失败，请重新上传！'
	setTimeout(function(){
		tip.innerHTML = '';
	},5000)

}
function thisFlash(flashName) {
	console.log(flashName);
	if (navigator.appName.indexOf("Microsoft") != -1) {
		console.log(window[flashName]);
		return window[flashName];
	} else {
		return document[flashName];
	}
}



function ajax(){
	var ajaxData = {
		type:arguments[0].type || "GET",
		url:arguments[0].url || "",
		async:arguments[0].async || "true",
		data:arguments[0].data || null,
		dataType:arguments[0].dataType || "text",
		contentType:arguments[0].contentType || "application/x-www-form-urlencoded",
		beforeSend:arguments[0].beforeSend || function(){},
		success:arguments[0].success || function(){},
		error:arguments[0].error || function(){}
	}
	ajaxData.beforeSend()
	var xhr = createxmlHttpRequest();
	xhr.responseType=ajaxData.dataType;
	xhr.open(ajaxData.type,ajaxData.url,ajaxData.async);
	xhr.setRequestHeader("Content-Type",ajaxData.contentType);
	xhr.send(convertData(ajaxData.data));
	xhr.onreadystatechange = function() {
		if (xhr.readyState == 4) {
			if(xhr.status == 200){
				ajaxData.success(xhr.response)
			}else{
				ajaxData.error()
			}
		}
	}
}

function createxmlHttpRequest() {
	if (window.ActiveXObject) {
		return new ActiveXObject("Microsoft.XMLHTTP");
	} else if (window.XMLHttpRequest) {
		return new XMLHttpRequest();
	}
}

function convertData(data){
	if( typeof data === 'object' ){
		var convertResult = "" ;
		for(var c in data){
			convertResult+= c + "=" + data[c] + "&";
		}
		convertResult=convertResult.substring(0,convertResult.length-1)
		return convertResult;
	}else{
		return data;
	}
}
