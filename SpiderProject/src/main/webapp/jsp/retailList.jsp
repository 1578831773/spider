<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored ="false" %>
<html>
<head>
    <meta charset="utf-8">
    <title>详情</title>
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
<%--    <div class="nav col-sm-2">--%>
<%--        <p>导航菜单</p>--%>
<%--        <ul class="nav nav-pills nav-stacked">--%>
<%--            <li><a href="/jsp/spider.jsp">创建爬虫任务</a></li>--%>
<%--            <li class="active"><a href="#">资源管理</a></li>--%>
<%--            <li><a href="#">用户管理</a></li>--%>
<%--            <li><a href="#">模板管理</a></li>--%>
<%--            <li><a href="#">日志管理</a></li>--%>
<%--        </ul>--%>
<%--    </div>--%>
    <div class="content">
        <div class="navigation_bar">
            <ul class="breadcrumb">
                <li><a href="/record/list">记录列表</a></li>
                <li class="active">详情</li>
            </ul>
        </div>
        <div class="recordList">
            <form class="form-horizontal col-sm-8 row" role="form" action="/record/retailSearch" method="get">
                <input class="hidden" type="text" name="bootId" value="${bootId}">
                <div class="form-group col-sm-6">
                    <input type="text" class="form-control" placeholder="输入要搜索内容" name="content">
                </div>
                <div class="radio col-sm-2">
                    <label>
                        <input type="radio" name="searchInd" id="optionsRadios1" value="1" checked> 网址
                    </label>
                </div>
                <div class="radio col-sm-2">
                    <label>
                        <input type="radio" name="searchInd" id="optionsRadios2" value="2"> 内容
                    </label>
                </div>
                <input name="templateId" type="hidden" value="${templateId}">
                <div class="form-group col-sm-2 row">
                    <div>
                        <button type="submit" class="btn btn-default">
                            <span class="glyphicon glyphicon-search	"></span>
                        </button>
                    </div>
                </div>

            </form>
            <div>
                <p>待下载:${downloadNum}, 待解析:${analyzeNum}</p>
            </div>
            <table class="table col-sm-10">
                <tbody>
                <%
                    if(((String)(request.getAttribute("templateId"))).equals("1")){
                %>
                <c:forEach var="detail" items="${details}">
                    <tr>
                        <th>url</th>
                        <td>${detail.url}</td>
                    </tr>
                    <tr>
                        <th class="col-sm-2">标题</th>
                        <td>${detail.title}</td>
                    </tr>
                    <tr>
                        <th class="col-sm-2">发布时间</th>
                        <td>${detail.publishTime}</td>
                    </tr>
                    <tr>
                        <th class="col-sm-2">标签</th>
                        <td>${detail.tags}</td>
                    </tr>
                    <tr>
                        <th class="col-sm-2">内容</th>
                        <td>${detail.content}</td>
                    </tr>
                    <tr>
                        <th class="col-sm-2">点击数</th>
                        <td>${detail.count}</td>
                    </tr>
                </c:forEach>
                <%
                    }else if(((String)(request.getAttribute("templateId"))).equals("2")){
                %>
                <c:forEach var="detail" items="${details}">
                    <tr>
                        <th>url</th>
                        <td>${detail.url}</td>
                    </tr>
                    <tr>
                        <th class="col-sm-2">内容</th>
                        <td>${detail.content}</td>
                    </tr>
                    <tr>
                        <th class="col-sm-2">爬取时间</th>
                        <td>${detail.spiderTime}</td>
                    </tr>
                </c:forEach>
                <%
                    }else if(((String)(request.getAttribute("templateId"))).equals("3")){
                %>
                <c:forEach var="detail" items="${details}">
                    <tr class="col-sm-12">
                        <th class="col-sm-1">url</th>
                        <td class="col-sm-10" style="word-wrap:break-word;word-break:break-all;overflow:hidden;">${detail.url}</td>
                    </tr>
                    <tr class="col-sm-12">
                        <th class="col-sm-1">标题</th>
                        <td class="col-sm-10">${detail.title}</td>
                    </tr>
                    <tr class="col-sm-12">
                        <th class="col-sm-1">发布时间</th>
                        <td class="col-sm-10">${detail.weiboArticle.publishTime}</td>
                    </tr>
                    <tr class="col-sm-12">
                        <th class="col-sm-1">导语</th>
                        <td class="col-sm-10">${detail.intro}</td>
                    </tr>
                    <tr class="col-sm-12">
                        <th class="col-sm-1">阅读量</th>
                        <td class="col-sm-10">${detail.readNum}</td>
                    </tr>
                    <tr class="col-sm-12">
                        <th class="col-sm-1">讨论量</th>
                        <td class="col-sm-10">${detail.discussNum}</td>
                    </tr>
                    <tr class="col-sm-12">
                        <th class="col-sm-1">用户名</th>
                        <td class="col-sm-10">${detail.weiboArticle.nickName}</td>
                    </tr>
                    <tr class="col-sm-12">
                        <th class="col-sm-1">内容</th>
                        <td class="col-sm-10">${detail.weiboArticle.content}</td>
                    </tr>
                    <tr class="col-sm-12">
                        <th class="col-sm-1">评论数</th>
                        <td class="col-sm-10">${detail.weiboArticle.commentNum}</td>
                    </tr>
                    <tr class="col-sm-12">
                        <th class="col-sm-1">点赞数</th>
                        <td class="col-sm-10">${detail.weiboArticle.likeNum}</td>
                    </tr>
                    <tr class="col-sm-12">
                        <th class="col-sm-1">爬取时间</th>
                        <td class="col-sm-10">${detail.weiboArticle.spiderTime}</td>
                    </tr>
                </c:forEach>
                <%
                    }else if(((String)(request.getAttribute("templateId"))).equals("4")){
                %>
                <c:forEach var="detail" items="${details}">
                    <tr>
                        <th class="col-sm-2">用户名</th>
                        <td>${detail.nickName}</td>
                    </tr>
                    <tr>
                        <th class="col-sm-2">内容</th>
                        <td>${detail.content}</td>
                    </tr>
                    <tr>
                        <th class="col-sm-2">评论数</th>
                        <td>${detail.commentNum}</td>
                    </tr>
                    <tr>
                        <th class="col-sm-2">点赞数</th>
                        <td>${detail.likeNum}</td>
                    </tr>
                    <tr>
                        <th class="col-sm-2">爬取时间</th>
                        <td>${detail.spiderTime}</td>
                    </tr>
                </c:forEach>
                <%
                    }
                %>
                </tbody>
            </table>
        </div>
    </div>
</div>

<script>
    <%
    if(request.getAttribute("redContent") != null){
    %>
    find("${redContent}","orange")

    <%
    }
    %>

    function find(searchVal, bgColor) {
        var oDiv = document.getElementsByTagName("body")[0];
        var sText = oDiv.innerHTML;
        var reg1 = /<script[^>]*>(.|\n)*<\/script>/gi; //去掉script标签
        sText = sText.replace(reg1, "");
        var bgColor = bgColor || "orange";
        var sKey = "<span name='addSpan' style='background-color: " + bgColor + ";'>" + searchVal + "</span>";
        var num = -1;
        var rStr = new RegExp(searchVal, "g");
        var rHtml = new RegExp("\<.*?\>", "ig");//匹配html元素
        var aHtml = sText.match(rHtml); //存放html元素的数组
        sText = sText.replace(rHtml, '{~}');  //替换html标签
        sText = sText.replace(rStr, sKey); //替换key
        sText = sText.replace(/{~}/g, function () {  //恢复html标签
            num++;
            return aHtml[num];
        });
        oDiv.innerHTML = sText;
    }
</script>

</body>
</html>