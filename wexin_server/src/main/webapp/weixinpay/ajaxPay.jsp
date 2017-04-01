<%@page import="org.rwl.pay.weixin.manager.WeixinJsbridgeManager"%>
<%@page import="org.rwl.pay.weixin.manager.WeixinJsbridgePojo"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%

String data = "";
double totalprice = Double.parseDouble(request.getParameter("totalprice"));
String openid = request.getParameter("openid");
String subject = "测试支付";
WeixinJsbridgePojo pojo = new WeixinJsbridgePojo();
pojo.appid = "wx64719d9b69c5714d";
pojo.clientaddr = request.getRemoteAddr();
pojo.clientsendurl = "http://wx.fsdigital.cn/weiixin/weixinpay/resultClient.jsp";
pojo.mchid = "1374938902";
pojo.notifyurl = "http://wx.fsdigital.cn/weiixin/weixinpay/weixinjsbridge_notify.do";
pojo.openid = openid;
pojo.setAPIKEY("F164CCB267B15E0C531ED6BE288E11B2");
pojo.useragent = request.getHeader("user-agent");
data = WeixinJsbridgeManager.getInstance().Gopay(pojo, subject, totalprice, ""+System.currentTimeMillis());
out.clear();
out.print(data);
%>
