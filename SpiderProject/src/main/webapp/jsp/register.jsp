<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored ="false" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>用户注册</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/bootstrap/css/bootstrap.css"/>
    <script src="${pageContext.request.contextPath}/static/js/jquery-1.11.1.js"></script>
    <script type="text/javascript">
        function ProcessFile(e) {
            var file = document.getElementById('headPic').files[0];
            if (file) {
                var reader = new FileReader();
                reader.onload = function (event) {
                    var txt = event.target.result;
                    var img = document.createElement("img");
                    img.height = 200;
                    img.width = 180;
                    img.src = txt;//将图片base64字符串赋值给img的src
                    // console.log(txt);
                    document.getElementById("result").appendChild(img);
                };
            }
            reader.readAsDataURL(file);
        }
        function contentLoaded() {
            document.getElementById('headPic').addEventListener('change',
                ProcessFile, false);
        }
        window.addEventListener("DOMContentLoaded", contentLoaded, false);
    </script>
</head>
<body>
<div class="top col-md-offset-1 col-md-10">
    <div class="webname"><h3>分布式爬虫系统</h3></div>
    <div class="navigation_bar">
        <ul class="breadcrumb">
            <li><a href="register.jsp">用户登陆</a></li>
            <li class="active">用户注册</li>
        </ul>
    </div>
</div>
<div class="loginform col-sm-offset-3 col-sm-5">
    <form class="form-horizontal" role="form" action="/user/register" method="post" enctype="multipart/form-data">
        <div class="form-group">
            <label for="headPic" class="control-label col-sm-2">头像</label>
            <div class="col-sm-5">
                <div id="result">
                </div>
                <input type="file" id="headPic" name="headPicFile">
            </div>
        </div>
        <div class="form-group">
            <label for="firstname" class="control-label col-sm-2">账号</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" id="firstname" placeholder="请输入手机号" name="account">
            </div>
        </div>
        <div class="form-group">
            <label for="name" class="control-label col-sm-2">昵称</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" id="name" placeholder="请输入昵称" name="userName">
            </div>
        </div>
        <div class="form-group">
            <label for="firstname" class="control-label col-sm-2">性别</label>
            <div class="radio col-sm-10">
                <label>
                    <input type="radio" name="sex" id="optionsRadios1" value="0" checked>女
                </label>
                <label>
                    <input type="radio" name="sex" id="optionsRadios2" value="1">男
                </label>
            </div>
        </div>
        <div class="form-group">
            <label for="password" class=" control-label col-sm-2">密码</label>
            <div class="col-sm-10">
                <input type="password" class="form-control" id="password" placeholder="请输入密码" name="password">
            </div>
        </div>
        <div class="form-group">
            <label for="rePassword" class=" control-label col-sm-2">重复密码</label>
            <div class="col-sm-10">
                <input type="password" class="form-control" id="rePassword" placeholder="请再次输入密码" name="rePassword">
            </div>
        </div>
        <div class="form-group row">
            <div class="col-sm-offset-2 col-sm-3">
                <button type="submit" class="btn btn-default";>注册</button>
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
