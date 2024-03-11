<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored ="false" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>爬取记录</title>
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
            <li><a href="/spider/index">创建爬虫任务</a></li>
            <li class="active"><a href="#">资源管理</a></li>
            <li><a href="/user/list">用户管理</a></li>
            <li><a href="/template/list">模板管理</a></li>
            <li><a href="/log/logList">日志管理</a></li>
        </ul>
    </div>
    <div class="content col-sm-10">
        <div class="navigation_bar">
            <ul class="breadcrumb">
                <li class="active">记录列表</li>
            </ul>
        </div>
        <div class="record_list">
            <form class="form-horizontal col-sm-8 row" role="form" action="/record/search" method="get">
                <div class="form-group col-sm-8">
                    <input type="text" class="form-control" placeholder="输入要搜索的源网址" name="bootUrl">
                </div>
                <div class="form-group row">
                    <div class="col-sm-2">
                        <button type="submit" class="btn btn-default">
                            <span class="glyphicon glyphicon-search	"></span>
                        </button>
                    </div>
                </div>

            </form>
            <table class="table">
                <caption>爬取记录</caption>
                <thead>
                <tr>
                    <th class="col-sm-3">源地址</th>
                    <th class="col-sm-1">用户编号</th>
                    <th class="col-sm-1">创建时间</th>
                    <th class="col-sm-1">更新时间</th>
                    <th class="col-sm-1">记录数</th>
                    <th class="col-sm-1">模板号</th>
                    <th class="col-sm-1">状态</th>
                    <th class="col-sm-1"></th>
                    <th class="col-sm-1"></th>
                    <th class="col-sm-1"></th>

                </tr>
                </thead>
                <tbody>
                <c:forEach var="record" items="${recordList}">
                    <tr>
                        <td>${record.bootUrl}</td>
                        <td>${record.userId}</td>
                        <td>${record.createTime}</td>
                        <td>${record.updateTime}</td>
                        <td>${record.count}</td>
                        <td>${record.templateId}</td>
                        <td>
                        <c:if test="${record.status == 0}">
                            爬取中
                        </c:if>
                        <c:if test="${record.status == 1}">
                            已完成
                        </c:if>
                        </td>
                        <td><form action="/record/details" method="get">
                            <input type="hidden" name="bootId" value="${record.bootId}">
                            <input type="hidden" name="templateId" value="${record.templateId}">
                            <button type="submit" class="btn btn-default">详情</button>
                        </form> </td>
                        <td><c:if test="${record.status == 1}">
                            <form action="/record/delete" method="post">
                                <input type="hidden" name="bootId" value="${record.bootId}">
                                <button type="submit" class="btn btn-default">删除</button>
                            </form>
                        </c:if>
                        </td>


                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

</body>
</html>
