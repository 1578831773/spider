<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored ="false" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title></title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/bootstrap/css/bootstrap.css"/>
    <script src="${pageContext.request.contextPath}/static/js/jquery-1.11.1.js"></script>
    <script src="${pageContext.request.contextPath}/static/bootstrap/js/bootstrap.js"></script>
    <script>
        //绘制饼图
        function drawCircle(canvasId, data_arr, color_arr, text_arr)
        {
            var c = document.getElementById(canvasId);
            var ctx = c.getContext("2d");

            var radius = c.height / 2 - 20; //半径
            var ox = radius + 20, oy = radius + 20; //圆心

            var width = 30, height = 10; //图例宽和高
            var posX = ox * 2 + 20, posY = 30;   //
            var textX = posX + width + 5, textY = posY + 10;

            var startAngle = 0; //起始弧度
            var endAngle = 0;   //结束弧度
            for (var i = 0; i < data_arr.length; i++)
            {
                //绘制饼图
                endAngle = endAngle + data_arr[i] * Math.PI * 2; //结束弧度
                ctx.fillStyle = color_arr[i];
                ctx.beginPath();
                ctx.moveTo(ox, oy); //移动到到圆心
                ctx.arc(ox, oy, radius, startAngle, endAngle, false);
                ctx.closePath();
                ctx.fill();
                startAngle = endAngle; //设置起始弧度

                //绘制比例图及文字
                ctx.fillStyle = color_arr[i];
                ctx.fillRect(posX, posY + 20 * i, width, height);
                ctx.moveTo(posX, posY + 20 * i);
                ctx.font = 'bold 12px 微软雅黑';    //斜体 30像素 微软雅黑字体
                ctx.fillStyle = color_arr[i]; //"#000000";
                var percent = text_arr[i] + "：" + 100 * data_arr[i] + "%";
                ctx.fillText(percent, textX, textY + 20 * i);
            }
        }

        function init() {
            //绘制饼图
            //比例数据和颜色
            var data_arr = <%=request.getAttribute("templateUse")%>;
            var color_arr = ["#00FF21", "#FFAA00", "#00AABB", "#FFAAFF"];
            var text_arr = ["搜狐新闻", "通用", "微博热搜", "微博"];

            drawCircle("canvas_circle", data_arr, color_arr, text_arr);
        }

        //页面加载时执行init()函数
        window.onload = init;
    </script>
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



<div class="main col-sm-offset-1">
    <div class="nav col-sm-2">
        <p>导航菜单</p>
        <ul class="nav nav-pills nav-stacked">
            <li class="active"><a href="/spider/index">创建爬虫任务</a></li>
            <li><a href="/record/list">资源管理</a></li>
            <li><a href="/user/list">用户管理</a></li>
            <li><a href="/template/list">模板管理</a></li>
            <li><a href="/log/logList">日志管理</a></li>
        </ul>
    </div>
    <div class="content col-sm-8">
        <div class="navigation_bar">
            <ul class="breadcrumb">
                <li class="active">首页</li>
                <li><a href="/spider/task">创建爬虫任务</a></li>
            </ul>
        </div>
        <div class="container col-sm-10">
            <div class="jumbotron">
                <h4>模板使用频率</h4>
                <p>
                    <canvas id="canvas_circle" width="500" height="300" >
                        浏览器不支持canvas
                    </canvas>
                </p>
            </div>
        </div>
        <div class="col-sm-2">
            <a href="#" class="list-group-item active">
                用户使用信息
            </a>
            <a href="#" class="list-group-item">登录次数:${loginCount}</a>
            <a href="#" class="list-group-item">创建爬虫任务数:${recordCount}</a>
            <a href="#" class="list-group-item">爬取数据条数${retailCount}</a>
        </div>

    </div>

</body>
</html>
