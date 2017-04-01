<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script type="text/javascript" src="/weiixin/js/jquery.min.js"></script>
<script type="text/javascript" src="/weiixin/js/jquery-form.js"></script>

</head>
<body>
    <form id="gp" method="post" enctype="multipart/form-data">  
    <input type="text" name="openid" id="openid" />  openid：
    <input type="file" name="file" id="" accept="image/*"/>  
    <input type="button" id="ok" value="保存" />  
    </form>  
	<script type="text/javascript">
    $("#ok").click(function() {  
        $("#gp").ajaxSubmit({  
            url : "http://127.0.0.1/weiixin/entry/interfaceEntryForStream.do",  
            type : "post",  
            dataType : 'json',  
            success : function(data) {  
                alert("设置成功！");  
            },  
            error : function(data) {  
                alert("error:" + data.responseText);  
            }  
        });  
    });  
    </script>
</body>
</html>