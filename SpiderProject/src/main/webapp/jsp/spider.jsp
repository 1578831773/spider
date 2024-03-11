<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored ="false" %>
<html>
<head>
    <meta charset="utf-8">
    <title>爬虫</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/bootstrap/css/bootstrap.css"/>
    <script src="${pageContext.request.contextPath}/static/js/jquery-1.11.1.js"></script>
    <script src="${pageContext.request.contextPath}/static/bootstrap/js/bootstrap.js"></script>
</head>
<body>
<div class="top col-sm-offset-1 col-sm-10 row" style="height: 100px; padding-top: 10px;">
    <div class="webname col-sm-3"><h3>分布式爬虫系统</h3></div>
    <div class="user_head col-sm-offset-9 row">
        <img class="img-circle col-sm-4" src="${pageContext.request.contextPath}/static/img/${headPic}" width="80px" height="80px" style="padding-bottom: 10px;"/>
        <div class="dropdown col-sm-2" style="padding-left: 0px;padding-top: 15px;">
            <button type="button" class="btn dropdown-toggle" id="dropdownMenu1" data-toggle="dropdown">我的
                <span class="caret"></span>
            </button>
            <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">
                <li role="presentation">
                    <a role="menuitem" tabindex="-1" href="/user/homepage">个人主页</a>
                </li>
                <li role="presentation">
                    <form action="/user/logout" method="post" style="padding-left: 15px">
                        <input type="submit" value="注销" style="border: 0px">
                    </form>
                </li>
                <li role="presentation" class="divider"></li>
                <li role="presentation">
                    <a role="menuitem" tabindex="-1">Hi~欢迎${userName}</a>
                </li>
            </ul>
        </div>
    </div>

</div>
<div class="main col-sm-offset-1 col-sm-10">
    <div class="nav col-sm-2">
        <p>导航菜单</p>
        <ul class="nav nav-pills nav-stacked">
            <li class="active"><a href="#">创建爬虫任务</a></li>
            <li><a href="/record/list">资源管理</a></li>
            <li><a href="/user/list">用户管理</a></li>
            <li><a href="/template/list">模板管理</a></li>
            <li><a href="/log/logList">日志管理</a></li>
        </ul>
    </div>
    <div class="content col-sm-10">
        <div class="navigation_bar">
            <ul class="breadcrumb">
                <li><a href="/spider/index">首页</a> </li>
                <li class="active">创建爬虫任务</li>
            </ul>
        </div>
        <div class="spiderForm col-sm-offset-1" >
            <form class="form-horizontal" role="form" action="/spider/start" method="get">
                <div class="form-group">
                    <label for="bootUrl" class="control-label col-sm-2">网址</label>
                    <div class="col-sm-8">
                        <input type="text" class="form-control" id="bootUrl" placeholder="请输入爬取的网址" name="bootUrl">
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label col-sm-2" >模板</label>
                    <div class="col-sm-4">
                        <select class="form-control" name="templateId">
                            <c:forEach items="${templateList}" var="template">
                                <option value="${template.templateId}">${template.templateName}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label col-sm-2">最大爬取数</label>
                    <div class="col-sm-4">
                    <select class="form-control" name="spiderNum">
                        <option value="20">20</option>
                        <option value="50">50</option>
                        <option value="100">100</option>
                        <option value="500">500</option>
                    </select>
                </div>
                </div>
                <div class="form-group row">
                    <div class="col-sm-offset-2 col-sm-3">
                        <button type="submit" class="btn btn-default">开爬</button>
                    </div>
                </div>
            </form>
        </div>
    </div>

</div>
<script>
    window.onload = function(){
        <%
             String msg = (String)request.getAttribute("message");
            if(msg != null){
        %>
        alert("<%=msg%>");
        <%
             }
        %>
    };
</script>

</body>
</html>