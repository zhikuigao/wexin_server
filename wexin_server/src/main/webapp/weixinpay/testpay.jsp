<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="initial-scale=1, maximum-scale=1, user-scalable=no">
<meta name="format-detection" content="telephone=no">
</head>

<body leftmargin="1" topmargin="0" marginwidth="0" marginheight="0" style="align: center;">
<script src="../js/zepto.min.js"></script>
<h3>测试微信支付</h3>

<div style="padding: 20px; border: 1px solid #aaa; height: 200px;">
	费用:<input name="price" id="price" value="0.01"/><br>
	openid:<input name="openid" id="openid" value="oKz74wH-OgZoxsb4NSYRKtOtq-YI"/><br>
	<button id="btnpay">支付</button>
</div>

<script>
(function (){
    $("#btnpay").click(function() {
    	var params = {
           	action : 'pay',
           	totalprice : $("#price").val(),
           	openid : $("#openid").val()
        };
    	var url = "http://wx.fsdigital.cn"+"/weiixin/weixinpay/interface.do";
    	$.ajax({
			url:url,
			type:"post",
			data: JSON.stringify({ 'openid': params.openid,'money':params.totalprice}),
			contentType: "application/json; charset=utf-8",
			dataType:"json",
			success:function(data){
				if(data.success && data.success == false) {
	      			alert("提交失败");
	      			return;
	      		} 
      			if (parseInt(data[0].agent) < 5) {
                    alert("您的微信版本低于5.0无法使用微信支付。");
                    return;
                }
                WeixinJSBridge.invoke('getBrandWCPayRequest',{
                    "appId" : data[0].appId, //公众号名称，由商户传入  
                    "timeStamp" : data[0].timeStamp, //时间戳，自 1970 年以来的秒数  
                    "nonceStr" : data[0].nonceStr, //随机串  
                    "package" : data[0].packageValue, //商品包信息
                    "signType" : data[0].signType, //微信签名方式:  
                    "paySign" : data[0].paySign,//微信签名  
                },function(res) {
                    /* 对于支付结果，res对象的err_msg值主要有3种，含义如下：(当然，err_msg的值不只这3种)
                    1、get_brand_wcpay_request:ok   支付成功后，微信服务器返回的值
                    2、get_brand_wcpay_request:cancel   用户手动关闭支付控件，取消支付，微信服务器返回的值
                    3、get_brand_wcpay_request:fail   支付失败，微信服务器返回的值

                    -可以根据返回的值，来判断支付的结果。
                    -注意：res对象的err_msg属性名称，是有下划线的，与chooseWXPay支付里面的errMsg是不一样的。而且，值也是不同的。
                    */
                  //  alert(res.err_msg);
                    if (res.err_msg == 'get_brand_wcpay_request:ok') {
                        alert("支付成功！");
                        //window.location.href = data[0].sendUrl;
                    } else if (res.err_msg == "get_brand_wcpay_request:cancel") {
                        alert("您已手动取消该订单支付。");
                    } else {
                        alert("订单支付失败。");
                    }
                });
      		}
      		
   	    });
    });
    
})();
</script>

</body>
</html>
