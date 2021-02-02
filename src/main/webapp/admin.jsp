<% 
	com.model.Employee employee = (com.model.Employee)session.getAttribute("employee"); 
	request.getSession().setAttribute("employee", employee);
	String empId = Integer.toString(employee.getEmpId());
	String[] roles = com.model.Role.names();
%>
<link href="Resources/assets/admin.css" rel="stylesheet"/>
<br/>
<div id="tabsAdmin" class="tabs">
    <ul>
        <li style = "width : 250px;"><a href="#newUser">Create New User</a></li>
        <li style = "width : 250px;"><a href="#modifyUser">Modify Existing User</a></li>
        <li style = "width : 250px;"><a href="#deleteUser">Delete Existing User</a></li>
    </ul>
    <div id="newUser" class="tabContent">
    	<form id="formUser" onsubmit = "return false" action="createUser">
    		<div id="employeeId-validation-text"></div>
    		<input type=number name='eId' id='eId' placeholder='Employee Id(Only numerics 0 to 9)' required />
    		<i class="fa fa-refresh fa-spin fa-2x fa-fw" id="employeeId-validation-icon"></i>
    		<br/>
    		<select name='roleUser' id='roleUser' required>
    			<option value=''>Please select a Role</option>
    			<% for(String userRole:roles) 
    			{
    				String userRoleShow = "";
    				if(userRole.equals("DEVELOPER"))
    					userRoleShow = "Publisher";
    				else
    					userRoleShow = org.apache.commons.lang3.text.WordUtils.capitalizeFully(userRole.replaceAll("_", " "));
    				%>
    				<option value = '<%=userRole%>'><%=userRoleShow%></option>
    				<%
    			}
    			%>
    		</select> 
    		<br/>
    		<div id="username-validation-text"></div>
    		<input type=text name="username" id="username" placeholder="Username" required />
    		<i class="fa fa-refresh fa-spin fa-2x fa-fw" id="username-validation-icon"></i>
    		<br/>
    		<label for="password">Password</label><br/>
				<input type="text" name="password" id="password" value ="dummy123" readonly="readonly" style="background: #f2f2f2"/>
			<br/>
			<input type="hidden" name="empId" value="<%=empId%>"/>
			<input type="submit" value="Create" id="createUser"/>
    	</form>
    </div>
    <div id="modifyUser" class="tabContent">
    	<jsp:include page="search.jsp">
    		<jsp:param name='mode' value='MODIFY'/>
    	</jsp:include>
    </div>
    <div id="deleteUser" class="tabContent">
    	<jsp:include page="search.jsp">
    		<jsp:param name='mode' value='DELETE'/>
    	</jsp:include>
    </div>
</div>