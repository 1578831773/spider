<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored ="false" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>用户列表</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/bootstrap/css/bootstrap.css"/>
    <script src="${pageContext.request.contextPath}/static/jquery-1.11.1.js"></script>
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
            <li><a href="/spider/index">创建爬虫任务</a></li>
            <li><a href="/record/list">资源管理</a></li>
            <li class="active"><a href="#">用户管理</a></li>
            <li><a href="/template/list">模板管理</a></li>
            <li><a href="/log/logList">日志管理</a></li>
        </ul>
    </div>
    <div class="content col-sm-10">
        <div class="navigation_bar">
            <ul class="breadcrumb">
                <li><a href="/user/list">用户列表</a></li>
                <li class="active">用户信息详情</li>
            </ul>
        </div>
        <div class="user_info panel-default">
            <h4>基本信息</h4>
            <table class="table-bordered col-sm-12">
                <tbody>
                <tr>
                    <td class="col-sm-2">
                        <div class="head_pic">
                            <img src="${pageContext.request.contextPath}/static/img/${userInfo.headPic}" class="img-thumbnail" width="120px" height="120px"/>
                        </div>
                    </td>
                    <td>
                        <table class="col-sm-12">
                            <tbody>
                            <tr>
                                <td class="col-sm-4">
                                    <h5>用户编号:${userInfo.userId}</h5>
                                </td>
                                <td>
                                    <h5>用户账号:${userInfo.account}</h5>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-sm-4">
                                    <h5>用户名:${userInfo.userName}</h5>
                                </td>
                                <td>
                                    <h5>性别:
                                    <c:if test="${userInfo.sex==0}">
                                        女
                                    </c:if>
                                    <c:if test="${userInfo.sex==1}">
                                        男
                                    </c:if>
                                    </h5>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </td>
                </tr>
                </tbody>
            </table>
            <div class="col-sm-3" style="padding-top: 10px">
                <form action="/user/setManager" method="post">
                    <input type="hidden" name="userId" value="${userInfo.userId}">
                    <input type="hidden" name="authority" value="${userInfo.authority}">
                    <button type="submit" class="btn btn-default">
                        <c:if test="${userInfo.authority == 2 || userInfo.authority == -2}">
                            取消管理员
                        </c:if>
                        <c:if test="${userInfo.authority == 1 || userInfo.authority == -1}">
                            设为管理员
                        </c:if>
                    </button>
                </form>
            </div>

        </div>
    </div>
</div>
<script>
    window.onload = function(){
        <%
             String msg =request.getParameter("message");
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
