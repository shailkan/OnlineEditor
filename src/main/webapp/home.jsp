<% if(session.getAttribute("employee") == null) {%>
	<jsp:forward page="login.jsp" />
<% } %>
<%request.getSession().setAttribute("employee",session.getAttribute("employee")); %>
<%String password = ((com.model.Employee)(request.getSession().getAttribute("employee"))).getPassword();
	if(password.equalsIgnoreCase("dummy123")) { %>
		<jsp:forward page="firstLogin.jsp" />
<% } %>
<%String[] roles = com.model.Role.names(); %>
<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Online Editorial Application</title>
		<script type="text/javascript" src="Resources/JS/jquery-2.2.2.js"></script>
		<link rel="stylesheet" href="Resources/assets/bootstrap.min.css" />
		<script type="text/javascript" src="Resources/JS/bootstrap.min.js"></script>
		<link href="Resources/assets/summernote.css" rel="stylesheet"/>
		<script src="Resources/JS/summernote.js"></script>
		<link rel="stylesheet" href="Resources/assets/jquery-ui.css">
		<script src="Resources/JS/jquery-ui.js"></script>
		<script type="text/javascript" src="Resources/JS/jquery-confirm.js"></script>
		<link rel="stylesheet" href="Resources/assets/jquery-confirm.css">
		<script type="text/javascript" src="Resources/JS/admin.js"></script>
		<script type="text/javascript" src="Resources/JS/main.js"></script>
		<link href="Resources/assets/font-awesome.css" rel="stylesheet"/>
		<link href="Resources/assets/main.css" rel="stylesheet"/>
		<script>
			let empId = "<%=((com.model.Employee)session.getAttribute("employee")).getEmpId() %>";
			let username = "<%=((com.model.Employee)session.getAttribute("employee")).getUsername() %>";
			empRole = "<%=((com.model.Employee)session.getAttribute("employee")).getRole() %>";
			status = "<%=com.model.Status.NEW%>";
		</script>
	</head>
	<body>
		<!-- Header -->
		<jsp:include page="header.jsp" />
		<!-- verfiy role of user as received from servlet -->
		<%com.model.Role empRole = ((com.model.Employee)session.getAttribute("employee")).getRole(); %>
		<%com.model.Status status = com.model.Status.NEW; %>
		<% if(empRole == com.model.Role.EDITOR) {%>
			<jsp:include page="editor.jsp">
				<jsp:param name='role' value='<%=empRole %>' />
			</jsp:include>
		<%}
		else if(empRole == com.model.Role.ADMIN)
		{%>
			<jsp:include page="admin.jsp" />
		<%}
		else {%>
			<script>fillData();</script>
			<jsp:include page="view.jsp">
				<jsp:param name='status' value='<%=status %>' />
				<jsp:param name='role' value='<%=empRole %>' />
			</jsp:include>
		<% } %>
	</body>
</html>
