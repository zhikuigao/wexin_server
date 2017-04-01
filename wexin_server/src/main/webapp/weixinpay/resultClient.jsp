<%@page import="java.util.Enumeration"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<h3>客户端支付跳转结果</h3>
<%
//头信息
String result = "";
{
    result += "<h4>Header</h4>";
    Enumeration eNames = request.getHeaderNames();
    while (eNames.hasMoreElements()) {
        String name = (String) eNames.nextElement();
        String value = request.getHeader(name);
        result += name + ":" + value + "<br>";
    }
}
//参数信息
{
    result += "<h4>Parameter</h4>";
    Enumeration eNames = request.getParameterNames();
    while (eNames.hasMoreElements()) {
        String name = (String) eNames.nextElement();
        String value = request.getParameter(name);
        
        result += name + ":" + value + "<br>";
    }
}
//Session信息
{
    result += "<h4>Session</h4>";
    Enumeration eNames = request.getSession().getAttributeNames();
    while (eNames.hasMoreElements()) {
        Object name = eNames.nextElement();
        Object value = request.getSession().getAttribute(name.toString());
        result += name + ":" + value + "<br>";
    }
}
%>
<%=result%>
