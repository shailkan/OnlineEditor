let mode = '';
let form = null;
function startupAdmin()
{
	const welcome = $("#welcome");
	welcome.html("Welcome " + username);
	$("#tabsAdmin").tabs(
		{
			collapsible : true,
			active : false,
			activate: function()
			{
				if ($('#tabsAdmin .ui-state-active').attr('aria-controls') ===  "newUser")
				{
					$("#formUser")[0].reset();
					mode = 'Create';
					bindEvents();
				}
				else  if($('#tabsAdmin .ui-state-active').attr('aria-controls') ===  "modifyUser")
				{
					form = $("#modifyDelete").detach();
					$("#modifyUser").html("");
					form.appendTo("#modifyUser");
					form = null;
					mode = 'Modify';
					const uname = $("#searchString").val();
					if(uname !== "")
						searchUsers();
					bindEvents();
					$("#searchString").val(uname);
				}
				else
				{
					$("#search").show();
					form = $("#modifyDelete").detach();
					$("#deleteUser").html("");
					form.appendTo("#deleteUser");
					mode = 'Delete';
					bindEvents();
				}
			}
		});
	
	//Disable/Enable create button 
	$('#username,#eId,#roleUser').on('input change',function() 
	{
		if ($("#eId").val() !== '' && $("#roleUser").val() !== '' && $("#username").val() !== '')
		{
			$("#createUser").addClass("createEnabled");
			$('#createUser').prop('disabled', false);
		}
		else
		{
			$("#createUser").removeClass("createEnabled");
			$("#createUser").addClass("createDisbaled");
			$('#createUser').prop('disabled', true);
		}
	});
}
function validateEmpId()
{
	const empId = $("#eId").val();
	$("#username-validation-text").hide();
	$("#username-validation-icon").hide();
	$("#employeeId-validation-icon").show();
	$.ajax({
		type : "GET",
		url : "validateEmpId",
		data : "empId=" + empId,
		success : function(data) {
			if(data !== true)
			{
				$("#employeeId-validation-text").html("<p style='margin-left: 10px; color: #0047b3'>Employee Id already exists. Please choose a different one</p>");
				$("#employeeId-validation-text").show();
				$("#employeeId-validation-icon").hide();
			}
			else
			{
				validateUsername();
			}
		},
		error : function() {
			alert("error");
		}
	});
}
function validateUsername()
{
	const uname = $("#username").val();
	$("#username-validation-icon").show();
	$("#employeeId-validation-text").hide();
	$("#employeeId-validation-icon").hide();
	$.ajax({
		type : "GET",
		url : "validateUsername",
		data : "username=" + uname,
		success : function(data) {
			if(data === true)
			{
				$("#employeeId-validation-text").hide();
				$("#username-validation-text").hide();
				const $formData = $("#formUser");
		       const url = $formData.attr( 'action' );
		       $.post( url, $formData.serialize()).done(function() 
		           {
		       		bindEvents();
		           	$.alert({
						   title: 'Success!',
						   animation: 'none',
						   content: 'Data updated successfully.',
						   confirmButton: 'Ok',
						   confirm: function()
						   {
						   	$("#searchString").val(uname);
						   	$("#tabsAdmin").tabs("option","active",1);
						   }
		           	});
		       });
			}
			else
			{
				$("#username-validation-text").html("<p style='margin-left: 10px; color: #0047b3'>Username already exists. Please choose a different one</p>");
				$("#username-validation-text").show();
				$("#username-validation-icon").hide();
			}
		},
		error : function() {
			alert("error");
		}
	});
}

function searchUsers()
{
	$("#modifyUserForm").hide();
	$("#search").show();
	$("#noUsers").html("");
	const userSearchString = $("#searchString").val();
	$("#userDetailsContent").html("");
	$.ajax({
		type : "GET",
		url : "getUsers",
		data : "searchString=" + userSearchString,
		success : function(employees) 
		{
			if(employees.length ===  0)
			{
				$("#userDetails").hide();
				$("#noUsers").append("<p>There are no users matching the searched username</p>");
				$("#noUsers").show();
			}
			else
			{
				$("#noUsers").hide();
				employees.forEach(function(employee) 
				{
					let action= "<td></td>";
					if(employee.empId !== empId)
						action = "<td><a>"+mode+"</a></td>";
					let eRole = employee.role;
					if(eRole === "DEVELOPER")
						eRole = "Publisher";
					else
						eRole = (eRole.replace(/_/g," ")).toLowerCase();
					const a = $("<tr>" +
							"<td>" + employee.username +"</td>" +
							"<td>" + employee.empId + "</td>" +
							"<td class='statusString'>" + eRole + "</td>" +
							action +"</tr>");
					if(mode === "Delete")
						a.on('click','a',{param1 : employee.empId}, deleteUser);
					else
						a.on('click','a',{param1 : employee.empId, param2: employee.role, param3: employee.username}, modifyUser);
					$("#userDetailsContent").append(a);
				});
				$("#userDetails").show();
			}
		},
		error : function(e) {
			console.log(e.responseText);
		}
	});
}

function bindEvents()
{
	unbindEvents();
	$("#username-validaton-text,#username-validation-icon,#employeeId-validaton-text,#employeeId-validation-icon").hide();
	$("#userDetails,#openUserDetails,#noUsers").hide();
	$("#modifyUserForm").hide();
	$("#createUser").removeClass("createEnabled");
	$("#createUser").addClass("createDisabled");
	$('#createUser').prop('disabled', true);
	$("#createUser").on('click',validateEmpId);
	$("input#searchUsers").on('click',searchUsers);
}

function unbindEvents()
{
	$("#createUser").off('click',validateEmpId);
	$("#searchUsers").off('click',searchUsers);
	$("#searchString").val("");
}

function deleteUser(event)
{
	const employeeId = event.data.param1;
	$.confirm({
	   title: 'Delete User!',
	   content: 'Are you sure you want to delete the user?',
	   confirmButton: 'Yes',
	   cancelButton: 'No',
	   animation: 'none',
	   confirm: function()
		   {
			$.ajax({
				type : "GET",
				url : "deleteUser",
				data : "employeeId=" + employeeId,
				mimeType : "text/html",
				success : function() 
				{
					$.alert({
					   title: 'Success!',
					   animation: 'none',
					   content: 'User deleted successfully.',
					   confirmButton: 'Ok',
					   confirm: function()
					   {
					   	searchUsers();
					   }
					});
				},
				error : function(e) {
					console.log(e);
				}
			});
		}
	});
}

function modifyUser(event)
{
	const employeeId = event.data.param1;
	const employeeRole = event.data.param2;
	const empUsername = event.data.param3;
	$("#search,#noUsers,#openUserDetails,#userDetails").hide();
	$("#modifyUserForm").show();
	$("#emp").val(employeeId);
	$("#employeeRole").val(employeeRole);
	$("#empUsername").val(empUsername);
}

function updateUser()
{
	$.confirm({
	   title: 'Update User!',
	   content: 'Are you sure you want to modify the user?',
	   confirmButton: 'Yes',
	   cancelButton: 'No',
	   animation: 'none',
	   confirm: function()
	   {
			$.ajax({
				type: "GET",
				url: "updateUser",
				processData: false,
			   contentType: false,
				data: $("#modifyForm").serialize(),
				success: function() 
				{
					$.alert({
					   title: 'Success!',
					   animation: 'none',
					   content: 'User role updated successfully.',
					   confirmButton: 'Ok',
					   confirm: function()
					   {
					   	searchUsers();
					   }
					});
				},
				error : function(e) {
					console.log(e);
				}
			});
	   }
	});
}
