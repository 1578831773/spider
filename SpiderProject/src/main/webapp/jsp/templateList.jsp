<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored ="false" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>模板列表</title>
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
            <li><a href="/record/list">资源管理</a></li>
            <li><a href="/user/list">用户管理</a></li>
            <li class="active"><a href="#">模板管理</a></li>
            <li><a href="/log/logList">日志管理</a></li>
        </ul>
    </div>
    <div class="content col-sm-10">
        <div class="navigation_bar">
            <ul class="breadcrumb">
                <li class="active">模板列表</li>
            </ul>
        </div>
        <div class="template_list">
            <!-- 按钮触发模态框 -->
            <button  style="margin-top: 10px;" class="btn btn-default" data-toggle="modal" data-target="#headPicture">添加模板</button>
            <!-- 模态框（Modal） -->
            <div class="modal fade" id="headPicture" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                            <h4 class="modal-title" id="myModalLabel">添加模板</h4>
                        </div>
                        <form action="/template/insert" method="post" enctype="multipart/form-data">
                            <div class="modal-body" style="height: 100px;">
                                <div class="form-group">
                                    <label for="templateName" class=" control-label col-sm-2">模板名</label>
                                    <div class="col-sm-10">
                                        <input type="text" class="form-control" id="templateName" name="templateName" placeholder="请输入模板名">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="desc" class=" control-label col-sm-2">描述</label>
                                    <div class="col-sm-10">
                                        <input type="text" class="form-control" id="desc" name="desc" placeholder="请输入模板描述">
                                    </div>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                                <button type="submit" class="btn btn-primary">添加</button>
                            </div>
                        </form>

                    </div><!-- /.modal-content -->
                </div><!-- /.modal -->
            </div>
            <table class="table">
                <caption>模板列表</caption>
                <thead>
                <tr>
                    <th>模板编号</th>
                    <th>模板名</th>
                    <th>描述</th>
                    <th>添加时间</th>
                    <th>使用次数</th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="template" items="${templateList}">
                    <tr>
                        <td>${template.templateId}</td>
                        <td>${template.templateName}</td>
                        <td>${template.desc}</td>
                        <td>${template.addTime}</td>
                        <td>${template.useCount}</td>
                        <td><form action="/template/update" method="post">
                            <input type="hidden" name="templateId" value="${template.templateId}">
                            <input type="hidden" name="status" value="${template.status}">
                            <button type="submit" class="btn btn-default">
                                <c:if test="${template.status == 0}">
                                    启用
                                </c:if>
                                <c:if test="${template.status == 1}">
                                    禁用
                                </c:if>
                            </button>
                        </form> </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

</body>
</html>
