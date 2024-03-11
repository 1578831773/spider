<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored ="false" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>用户登录</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/bootstrap/css/bootstrap.css"/>
    <script src="${pageContext.request.contextPath}/static/js/jquery-1.11.1.js"></script>

</head>
<body>
<div class="top col-md-offset-1 col-md-10">
    <div class="webname"><h3>分布式爬虫系统</h3></div>
    <div class="navigation_bar">
        <ul class="breadcrumb">
            <li class="active">用户登录</li>
        </ul>
    </div>
</div>
<div class="loginform col-sm-offset-4 col-sm-4">
    <form class="form-horizontal" role="form" action="/user/login" method="post">
        <div class="form-group">
            <label for="firstname" class="control-label col-sm-2">账号</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" id="firstname" placeholder="请输入账号" name="account">
            </div>
        </div>
        <div class="form-group">
            <label for="lastname" class=" control-label col-sm-2">密码</label>
            <div class="col-sm-10">
                <input type="password" class="form-control" id="lastname" placeholder="请输入密码" name="password">
            </div>
        </div>
        <div class="form-group">
            <div>
                <div class="checkbox col-md-offset-2">
                    <label>
                        <input type="checkbox">请记住我
                    </label>
                </div>
                <div class="col-sm-offset-8"><a href="register.jsp">没有账号?点击注册</a></div>
            </div>

        </div>
        <div class="form-group row">
            <div class="col-sm-offset-2 col-sm-3">
                <button type="submit" class="btn btn-default";>登录</button>
            </div>
            <div class="col-sm-offset-4 col-sm-3">
                <button type="reset" class="btn btn-default";>重置</button>
            </div>
        </div>
    </form>
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
