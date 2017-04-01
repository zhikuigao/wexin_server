// JavaScript Document
var root_ = "http://127.0.0.1"; //全局变量域名
//var root_ = "http://10.0.0.12";

(function storageopenid(){
	if(sessionStorage.openid){
		window.openid = sessionStorage.openid;
		return;
	}
	var param = GetRequest(),getId;
	getId = param.openid; //全局变量openid
	sessionStorage.openid = getId;

	window.openid = sessionStorage.openid;
})();

String.prototype.Format = function(args) {
	/*扩展字符串的格式化方法*/
    if (arguments.length > 0) {
        var result = this;
        if (arguments.length == 1 && typeof(args) == "object") {
            for (var key in args) {
                var reg = new RegExp("{" + key + "}", "g");
                result = result.replace(reg, args[key]);
            }
        } else {
            for (var i = 0; i < arguments.length; i++) {
                if (arguments[i] == undefined) {
                    return "";
                } else {
                    var reg = new RegExp("({[" + i + "]})", "g");
                    result = result.replace(reg, arguments[i]);
                }
            }
        }
        return result;
    } else {
        return this;
    }
}

function getQuestDetail(questId){
	var questId = questId;
	/*获取问题详情*/
	var url = root_+"/weixin/entry/interfaceEntry.do?paramObject";
	var data = {
		busiCode:"queryProblemDesc",
		id:questId
	}
	var result;
	new SelfAjax({
		url:url,
		data:data,
		async:false,
		success:function(data){
			if(data.result.code!=1){
				console.log(data.result.desc);
			}else{
				result = data.resultDesc;
				//console.log(data.resultDesc);
			}
		},
		error:function(){

		}
	});
	return result;
}	


function addZero(msg){
	return ("0"+msg).slice(-2);
}

function parserDate(date) { 
	/*IOS不支持直接解析，会出NAN*/ 
    var t = Date.parse(date);  
    return new Date(Date.parse(date.replace(/-/g, "/"))); 
}
function dateFormat(datestring){
	if(!datestring){
		return datestring;
	}
	var parseTime = Date.parse(parserDate(datestring));
	var now = new Date().getTime();
	var t = parseTime-now;
	
	var days      = Math.floor(  t/(1000*60*60*24) );
	var hours     = Math.floor( (t/(1000*60*60)) % 24 );
	var minutes   = Math.floor( (t/1000/60) % 60 );
	var seconds   = Math.floor( (t/1000) % 60 );
	var d = {
		't':t,
		'days': days,
		'h': hours,
		'm': minutes,
		's': seconds
	}
	return d;
}

function getRegisterStatus(){
	/*判断是否注册*/
	var userInfo = getUserInfo(openid);
	if(!userInfo.mobPhone||userInfo.mobPhone==''){
		if($(".body-wrap").hasClass("publish")){
			window.location.href = "../login.html";
		}else{
			window.location.href = "login.html";
		}
		return false;
	}
	return true;
}

function getUserInfo(openid){
	var url = root_+"/weixin/entry/interfaceEntry.do?paramObject";
	var data = {
		busiCode:"queryPersonInfo",
		openid:openid,
	}
	var result;

	new SelfAjax({
		url:url,
		data:data,
		async:false,
		success:function(data){
			if(data.result.code==1){
				result = data.resultDesc;
				
			}else{
				console.log(data.result.desc);
				result = false;
			}
		},
		error:function(errorMsg){
			log(errorMsg);
		}
	});
	return result;
}
function addHelpRecord(questId,openidBy){
	var url = root_+"/weixin/entry/interfaceEntry.do?paramObject";

	var data = {
		"busiCode":"addShareRecord",
		"problemId":questId,
		"shareOpenid":openid,
		"byShareOpenid":openidBy
	}
	new SelfAjax({
		url:url,
		data:data,
		success:function(data,status){
			if(data.result.code==1){
				console.log("帮忙记录添加成功");
			}
		}
	});
}
function showInfoDialog(status,word,tips){
	var temp = 	'<div class="info-dialog">'+
					'<div class="status-box {status}"></div>'+
					'<div class="word">{word}</div>'+
					'<div class="tips">{tips}</div>'+
				'</div>';
	var status = status?"":"fail";
	var data = {
		status:status,
		word:word?word:"成功",
		tips:tips?"<span class='num'>3</span>秒后"+tips+"":"<span class='num'>3</span>秒后进入详情页"
	}
	var html = temp.Format(data);
	var layerId = layer.open({
		type: 1,
		content: html,
		success:function(){
			var timer = setInterval(function(){
				var curText = $(".info-dialog .num").text();
				if(curText==0){clearInterval(timer);return false;}
				curText--;
				$(".info-dialog .num").text(curText);
			},1000);
		}
	});
	return layerId;
}

function firstClassWordHandle(obj){
	var selfObj = obj;
	var first = selfObj.firstClassification;
	var second = selfObj.secondClassification;
	switch(first){
		case "找人":
			second = "找"+second;
			break;
		case "问事":
			second = "问"+second;
			break;
		case "任务":
			second = "做"+second;
			break;
		default:
			second = second;
	}
	selfObj.secondClassification = second;
	return selfObj;
}

var layerIndex;
function globalBeforeSend(){
	/*全局的ajax  loading*/
	layerIndex = layer.open({
		type: 2,
		shade:'background-color: rgba(0,0,0,.3)'
	});
	return layerIndex;
}

function SelfAjax(param){

	var options = {
		url:param.url,
		data:param.data,
		async:param.async?true:param.async,
		success:param.success,
		error:param.error,
		ticket:"ceshi"
	}
	$.ajax({
		url:options.url,
		type:"post",
		async:options.async,
		data:JSON.stringify(options.data),
		dataType:"json",
		contentType:"application/json; charset=utf-8",
		beforeSend:function(){globalBeforeSend()},
		success:function(result){
			layer.close(layerIndex);
			options.success(result);
		},
		complete:function(){
			layer.close(layerIndex);
		},
		error:function(result){
			layer.close(layerIndex);
			if(options.error){
				options.error(result);
			}
			
		}
	});	
	return this;
}

function GetRequest(p) { 
	//获取url中所附带的参数
	var url = decodeURI(location.search);
	var theRequest = p ? "" : new Object(); 

	if (url.indexOf("?") != -1) { 
		var str = url.substr(1); 
		if(p){
			return str;
		}
		strs = str.split("&"); 
		for(var i = 0; i < strs.length; i ++) { 
			theRequest[strs[i].split("=")[0]]=unescape(strs[i].split("=")[1]); 
		} 
	} 

	return theRequest; 
} 

function log(msg){
	return console.log(msg);
}



var oldAlert = window.alert;
window.alert = function(msg,selfcallback){
	layer.open({
		content: msg,
		btn: '我知道了',
		style:"font-size:.24rem;",
		yes:function(index){
			selfcallback&&selfcallback();
			layer.close(index);
		}
	});
}

function showMsg(msg,callback){
	var callback = callback;
	layer.open({
		content: msg,
		skin:"msg",
		time: 2, //2秒后自动关闭,
		success: function(elem){
		  	setTimeout(function(){
				callback&&callback();
			},2000);
		}
	});
}

var noDataHtml = '<div class="no-data">当前栏目无数据</div>';


$(document).ready(function(e) {
	(function(){
		/*FastClick*/
		if(typeof(FastClick) !=="undefined"){
			FastClick.attach(document.body);
		}
	})();


	/*判断设备*/
	var u = navigator.userAgent, app = navigator.appVersion;
	var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Linux') > -1; //android终端或者uc浏览器
	var isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
	(function(){
		/*wx_share*/
		var is_weixin = (function isWeiXin(){
			var ua = window.navigator.userAgent.toLowerCase();
			if(ua.match(/MicroMessenger/i) == 'micromessenger'){
				return true;
			}else{
				return false;
			}
		})();
		


		$(".show-share-tip").on("click",function(e){
			//if(!is_weixin)return false;
			e.preventDefault();
			
			var $browserTips = $("<div></div>")
					.attr("id", "browserTips")
					.addClass("browser-tips")
					.html("<div class='browser-tips-content'>"
									+ "</div>"
									+ "<div class='browser-tips-mask'></div>");
			

			var classToggle = isiOS ? "IOS-noscroll":"noscroll";
			$("body").append($browserTips).addClass(classToggle);
			
			$browserTips.find(".browser-tips-mask").on("click",function(e){
				$browserTips.remove();
				$("body").removeClass(classToggle);
			});
		});
		document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {
			WeixinJSBridge.call('showOptionMenu');
		});
	})();

	(function(){
		/*g-checkbox*/
		$(".g-checkbox input").on("change",function(){
			if($(this).is(":checked")){
				$(this).parents(".g-checkbox").addClass("on");
			}else{
				$(this).parents(".g-checkbox").removeClass("on");
			}
		});
	})();

	(function(){
		/*footer publish btn*/
		var switchClassName = isiOS?"IOS-noscroll":"noscroll";

		$(".g-publish").on("click",function(){
			$(".menu-box").show();
			$("body").addClass(switchClassName);
		});
		$(".menu-box").on("click",".fix-close,.fix-shade",function(){
			$(this).parents(".menu-box").hide();
			$("body").removeClass(switchClassName);
		});
	})();

	$(".g-progress").on("click",".progress-item",function(){
		var that = $(this),
			thatProgress = that.parents(".g-progress");

		if(thatProgress.hasClass("disEditAbled")) return;

		var thatIndex = $(this).index();
		var toggleClassName = thatProgress.hasClass("no-last")?"active":"last active";

		$(this).addClass(toggleClassName).siblings(".progress-item").removeClass(toggleClassName);
		thatProgress.find(".progress-item:lt("+thatIndex+")").addClass("active");

		if(!$(this).hasClass("progress-other")){
			$(this).parents(".g-progress").siblings(".hidden-val").val($(this).attr("data-val"));
		}
		
	});
	$(document).on("touchstart",".btn-highlight",function(){
		$(this).addClass("hover");
	}).on("touchmove",".btn-highlight",function(event){
		event.preventDefault();
	}).on("touchcancel touchend",".btn-highlight",function(event){
		$(this).removeClass("hover");
	});

	$(document).on("touchstart",".btn-highlight1",function(){
		$(this).addClass("hover");
	}).on("touchmove",".btn-highlight1",function(event){
		event.preventDefault();
	}).on("touchcancel touchend",".btn-highlight1",function(event){
		$(this).removeClass("hover");
	});


	$(document).on("click",".head-location .head-wrap",function(e){
		e.preventDefault();
		var thisOpenid  = $(this).parents(".g-box-item").attr("data-open");
		if(thisOpenid == openid){
			var url = "mine.html"
		}else{
			var url = "mine.html?openid="+thisOpenid+"&isMySelf=false"
		}
		window.location.href=url;
	});

	$(document).on("click",".g-dialog-close,.g-dialog-mask",function(e){
		$(this).parents(".g-dialog").hide();
	});
});
/*部分特殊处理需要等页面包括资源文件完全加载完才允许执行，否则出错，如处理图片等*/
$(window).on("load",function(){
	
});