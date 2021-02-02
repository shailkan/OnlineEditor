<%if(request.getSession().getAttribute("employee")!= null) 
{ 
	String password = ((com.model.Employee)(request.getSession().getAttribute("employee"))).getPassword();
	if(password.equalsIgnoreCase("dummy123")) { %>
		<jsp:forward page="firstLogin.jsp" />
<%} else {%>
		<jsp:forward page="home.jsp" />
<%}}%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Online Editorial Application</title>
		<script>
			history.pushState(null, null, 'login.jsp');
			window.addEventListener('popstate', function(event) 
			{
				history.pushState(null, null, 'login.jsp');
			});
		</script>
		<link href="Resources/assets/login.css" rel="stylesheet"/>
	</head>
	<body>
	<div id="logo"></div>
	<div id="loginForm">
		<h2>Online Editorial Application</h2>
			<form id="formContents" action="getEmployee" method="post">
				<div id="login" style="padding-left : 50px; padding-top: 30px;">
					<label for="username">Username</label><br>
					<input id="username" type=text name="username" required/>
					<br>
					<label for="password">Password</label><br>
					<input id= "password" type=password name="password" required/>
					<% if(session.getAttribute("error").equals("error")) {%>
					<p>Invalid Username/Password</p>
				<% } %>
				<input type="submit" value="Submit" style="width : 150px; height : 40px; font-size : 14px; font-weight : bold; background : #999999; margin-left : 20px; margin-top: 2px; margin-bottom: 20px;"/>
				</div>
			</form>
		</div>
	</body>
</html>
