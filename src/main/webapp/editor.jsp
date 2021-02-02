<%String empRole = request.getParameter("role"); %>
<%com.model.Status status = com.model.Status.NEW; %>
<br>
<div id="tabs" class="tabs">
    <ul>
        <li style = "width: 225px;"><a href="#New">Create New Document</a></li>
        <li><a href="#viewTab">View</a></li>
    </ul>
    <div id="New" class="tabContent">
        <jsp:include page="data.jsp">
			<jsp:param name="mode" value="create" />
			<jsp:param name="status" value = '<%=com.model.Status.NEW %>' />
			<jsp:param name='role' value='<%=empRole %>' />
		</jsp:include> 
    </div>
    <div id="viewTab" class="tabContent">
        <jsp:include page="view.jsp">
			<jsp:param name='status' value='<%=status %>' />
			<jsp:param name='role' value='<%=empRole %>' />
		</jsp:include>
    </div>
</div>

