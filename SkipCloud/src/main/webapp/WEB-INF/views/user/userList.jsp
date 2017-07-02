<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html lang="en" class="no-js">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<div class="content">
	    <form class="forget-form" id="form-find-user" action="rest/user/findUser" method="get">
			<h3>用户列表</h3>
			<div  class="form-group" style="float:left">
				<div class="input-icon">
					<input class="form-control placeholder-no-fix" type="text" placeholder="请输入用户名" name="username" id="username"/>
				</div>
			</div>
			<div class="form-group" style="float:left">
				<button type="button" class="btn blue" id="btn-find-user">
					查询<i class="m-icon-swapright m-icon-white"></i>
				</button>
			</div>
			<div class="tmp-error">
				<span id="span-error"><font color="red" size="4">${error}</font></span>
			</div>
			<div style="clear:both"></div>
		</form>
		<div>
			<table class="table table-bordered table-hover table-striped table-condensed">
				<thead>
					<tr class="active">
						<th>用户名</th>
						<th>姓名</th>
						<th>邮箱</th>
						<th>国家</th>
						<th>城市</th>
						<th>地址</th>
						<th>状态</th>
						<th>创建时间</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${userList}" var="user">
						<tr>
							<td>${user.username}</td>
							<td>${user.fullname}</td>
							<td>${user.email}</td>
							<td>${user.country}</td>
							<td>${user.city}</td>
							<td>${user.address}</td>
							<td>${user.state}</td>
							<td><fmt:formatDate value="${user.createTime}" type="both" dateStyle="default"/></td>
							<td>
								<a class="a-ban-user" href="rest/user/banUser"><i class="fa fa-ban"></i>禁用</a>
                                <a href="extra_lock.html"><i class="fa fa-trash-o"></i>删除</a>
                            </td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
	<!--Begin Test -->
    <script type="text/javascript">
    $('#btn-find-user').click(function(e) {
        var url = $('#form-find-user').attr('action');
        $.get(url, { username: $('#username').val()}, function(data) {
            $('#main-content').html(data);
        });
    });

    $('#a-ban-user').click(function(e) {
    	e.preventDefault();
        var url = this.href;
        alert(this.parent());
        $.get(url, { username: $('#username').val()}, function(data) {
            $('#main-content').html(data);
        });
    });

    $('#username').focus(function(e) {
        $('#span-error').text('');
    });
    </script>
	<!--End Test -->
</body>
</html>