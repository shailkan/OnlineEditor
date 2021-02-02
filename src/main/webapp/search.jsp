<%@page import="com.model.Role"%>
<%request.getSession().setAttribute("employee",session.getAttribute("employee")); %>
<%String[] roles = Role.names(); %>
<%String mode = request.getParameter("mode");%>
<div id="modifyDelete">
	<div id="search">
		<form id="searchForm" onsubmit = "return false">
			<input type=text name="searchString" id="searchString" placeholder="&#xf002; Username" style="font-family:Arial, FontAwesome"/>
			<input type="submit" value="Search" id = "searchUsers" class="createEnabled" />
		</form>
	</div>
	<div id="noUsers">
	</div>
	<div id="userDetails">
		<table id="userDetailsHeading">
			<tr>
				<th>Username</th>
				<th>Employee Id</th>
				<th>Role</th>
				<th>Action</th>
			</tr>
		</table>
		<table id = "userDetailsContent">
		</table>
	</div>
	<div id="openUserDetails">
	</div>
	<div id="modifyUserForm">
		<a id='backUser' style='margin-left : 15px;' onclick='searchUsers()'>Back</a><br/>
		<form id="modifyForm" onsubmit = "return false">
			<label for="emp">Employee Id</label><br/>
			<input type=text name="emp" id= "emp" style="background-color: #f2f2f2;" readonly>
			<br/>
			<label for="roleUser">Role</label><br/>
    		<select id = "roleUser" name='roleUser' id='employeeRole' required>
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
    		<label for="empUsername">Username</label><br/>
    		<input type=text name="username" id="empUsername" placeholder="Username" style= "background-color: #f2f2f2;" readonly />
    		<br/>
			<input type="submit" value="Modify" id="modifyUserButton" class="createEnabled" onclick="updateUser()"/>
		</form>
	</div>
</div>
