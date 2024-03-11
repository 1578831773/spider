<%@ page import="com.example.demo.bean.UserInfo" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored ="false" %>
<html>
<head>
    <title>个人中心</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/bootstrap/css/bootstrap.css"/>
    <script src="${pageContext.request.contextPath}/static/js/jquery-1.11.1.js"></script>
    <script src="${pageContext.request.contextPath}/static/bootstrap/js/bootstrap.js"></script>
</head>
<body>
<div class="top col-sm-offset-1 col-sm-10">
    <div class="webName"><h3>分布式爬虫系统</h3></div>
    <div class="navigation_bar">
        <ul class="breadcrumb">
            <li><a href="/spider/index">首页</a></li>
            <li class="active">个人中心</li>
        </ul>
    </div>
</div>
<div class="col-sm-10 col-sm-offset-1">
    <div class="col-sm-4">
        <div>
            <img class="img-circle" src="${pageContext.request.contextPath}/static/img/${headPic}" width="80px" height="85px" style="padding-bottom: 10px;"/>
            <p class="col-sm-6">欢迎来到个人中心~${userName}</p>
        </div>
        <div class="menu panel-group" id="accordion">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h4 class="panel-title">
                        <a data-toggle="collapse" data-parent="#accordion"
                           href="#collapseOne">
                            个人信息
                        </a>
                    </h4>
                </div>
                <div id="collapseOne" class="panel-collapse collapse in">
                    <div class="panel-body">
                        <ul>
                            <li class="active">我的信息</li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="panel panel-default col-sm-6 col-sm-offset-1">
        <div>
            <h4>基本信息</h4>
            <div style="padding-top:50px">
                <div>
                    <div class="headPic row">
                        <div class="col-sm-4">
                            头像:
                            <img class="img-thumbnail" src="${pageContext.request.contextPath}/static/img/${userInfo.headPic}" width="80px" height="80px"/>
                        </div>
                        <div class="col-sm-3">

                            <!-- 按钮触发模态框 -->
                            <button class="btn btn-default" data-toggle="modal" data-target="#pwd">修改密码</button>
                            <!-- 模态框（Modal） -->
                            <div class="modal fade" id="pwd" tabindex="-1" role="dialog" aria-labelledby="updatePassword" aria-hidden="true">
                                <div class="modal-dialog">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                                            <h4 class="modal-title" id="updatePassword">修改密码</h4>
                                        </div>
                                        <form action="/user/updatePassword" method="post">
                                            <div class="modal-body" style="height: 100px;">
                                                <div class="form-group">
                                                    <label for="password" class=" control-label col-sm-2">密码</label>
                                                    <div class="col-sm-10">
                                                        <input type="password" class="form-control" id="password" name="password" placeholder="请输入密码">
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label for="rePassword" class=" control-label col-sm-2">重复密码</label>
                                                    <div class="col-sm-10">
                                                        <input type="password" class="form-control" id="rePassword" name="rePassword" placeholder="请再次输入密码">
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="modal-footer">
                                                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                                                <button type="submit" class="btn btn-primary">提交更改</button>
                                            </div>
                                        </form>

                                    </div><!-- /.modal-content -->
                                </div><!-- /.modal -->
                            </div>
                        </div>
                        <div class="col-sm-4">
                            账号:${userInfo.account}
                            <br><br>
                            用户身份:
                            <c:if test="${userInfo.authority == 3}">
                                超级管理员
                            </c:if>
                            <c:if test="${userInfo.authority == 1 || userInfo.authority == -1}">
                                普通用户
                            </c:if>
                            <c:if test="${userInfo.authority == 2 || userInfo.authority == -2}">
                                管理员
                            </c:if>
                        </div>
                    </div>
                </div>
                <div style="padding-left: 35px;">
                    <!-- 按钮触发模态框 -->
                    <button  style="margin-top: 10px;" class="btn btn-default" data-toggle="modal" data-target="#headPicture">修改头像</button>
                    <!-- 模态框（Modal） -->
                    <div class="modal fade" id="headPicture" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                                    <h4 class="modal-title" id="myModalLabel">修改头像</h4>
                                </div>
                                <form action="/user/updateHeadPic" method="post" enctype="multipart/form-data">
                                    <div class="modal-body" style="height: 100px;">
                                        <div class="form-group">
                                            <label for="password" class=" control-label col-sm-2">头像</label>
                                            <div class="col-sm-10">
                                                <input type="file" class="form-control" id="headPic" name="headPic" placeholder="请输入选择图片">
                                            </div>
                                        </div>

                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                                        <button type="submit" class="btn btn-primary">提交更改</button>
                                    </div>
                                </form>

                            </div><!-- /.modal-content -->
                        </div><!-- /.modal -->
                    </div>
                </div>
            </div>
            <div class="col-sm-12" style="padding-top:30px">
                <form class="form-horizontal" role="form" action="/user/updateUserInfo" method="post" enctype="multipart/form-data">
                    <div class="form-group">
                        <label for="name" class="control-label col-sm-2">用户名</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="name" name="userName" value="${userInfo.userName}">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="sex1" class="control-label col-sm-2">性别</label>
                        <div class="radio col-sm-10">
                            <label>
                                <%
                                    if(((UserInfo)request.getAttribute("userInfo")).getSex().equals("0")){
                                %>
                                <input type="radio" name="sex" id="sex1" value="0" checked>女
                                <%
                                }else{
                                %>
                                <input type="radio" name="sex" id="sex1" value="0">女
                                <%
                                    }
                                %>
                            </label>
                            <label>
                                <%
                                    if(((UserInfo)request.getAttribute("userInfo")).getSex().equals("1")){
                                %>
                                <input type="radio" name="sex" id="sex2" value="1" checked>男
                                <%
                                }else{
                                %>
                                <input type="radio" name="sex" id="sex2" value="1">男
                                <%
                                    }
                                %>

                            </label>
                        </div>
                    </div>
                    <div class="col-sm-offset-2" style="padding-bottom: 50px">
                        <button type="submit" class="btn btn-default";>修改信息</button>
                    </div>
                </form>
            </div>
        </div>


    </div>
</div>
<script>
    window.onload = function(){
        <%

             String msg = request.getParameter("message");
            if(msg != null){
        %>
        alert("<%=msg%>");
        <%
                 request.removeAttribute("message");
             }
        %>
    };
</script>
</body>
</html>
