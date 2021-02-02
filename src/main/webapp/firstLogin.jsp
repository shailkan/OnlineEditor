<%request.getSession().setAttribute("employee",session.getAttribute("employee")); %>
<% int employeeId = ((com.model.Employee)session.getAttribute("employee")).getEmpId(); %>
<% String password = ((com.model.Employee)session.getAttribute("employee")).getPassword(); %>
<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Online Editorial Application</title>
		<script type="text/javascript" src="Resources/JS/jquery-2.2.2.js"></script> 
		<link rel="stylesheet" href="Resources/assets/jquery-ui.css">
		<script src="Resources/JS/jquery-ui.js"></script>
		<script type="text/javascript" src="Resources/JS/jquery-confirm.js"></script>
		<link rel="stylesheet" href="Resources/assets/jquery-confirm.css">
		<link href="Resources/assets/font-awesome.css" rel="stylesheet"/>
		<script>
			$(document).ready(function()
			{
				$("#defPassword,#wrongPassword").hide();
				$("#firstLogin").dialog(
				{
					closeOnEscape: false,
					open: function(event, ui) 
				    {
				       $(".ui-dialog-titlebar-close").hide();
				    },
					modal: true,
					width: "400px",
				    dialogClass : "passwordDialog"
				});
				
				$("#changePassword").click(function()
				{
					if(($("#passwrd").val()).toLowerCase() == "dummy123")
					{
						$("<div title='Invalid Password!' id='invalidPassword'>Please choose a password other than the default password.</div>")
						.dialog(
								{
									open: function(event, ui) 
								    {
								       $(".ui-dialog-titlebar-close").hide();
								    },
								    buttons : 
									[{
								   		text: 'OK',
							               click: function() 
							               {
							               	$("#defPassword").show();
							                   $(this).dialog("close"); //closing on Ok click
							               },
							               'class': 'my-custom-button-class'
								    }],
								    dialogClass: 'invalidPassword',
								    width: '370px'
								});
					}
					else if($("#passwrd").val() != $("#confirmPassword").val())
					{
						$("#defPassword").hide();
						$("#passwordValidation").html("<p style='margin-top: 7px; color: #ff0000;'>Passwords don't match!</p><br/>");
						$("#wrongPassword").show();
					}
					else
					{
						$("#defPassword,#wrongPassword").hide();
						$("#passwordValidation").html("");
						$("#passwordUpdateForm")[0].submit();
					}
					
				});
			});
		</script>
		<style>
		.invalidPassword
		{
			box-shadow: 0 2px 6px rgba(0, 0, 0, 0.2);
			border-radius: 5px;
			padding: 15px 15px 0;
			color: #333;
		}
		.invalidPassword .ui-dialog-titlebar
		{
			background: transparent; 
     		border: none; 
     		display: inline-block;
   			vertical-align: middle;
    		padding-bottom: 15px;
    		line-height: 20px;
		}
		
		.ui-dialog .ui-dialog-title
		{
			width: 100%;	
		}
		
		.invalidPassword .my-custom-button-class
		{
			background-color: transparent; 
     		font-weight: bold;
		}
		
		.my-custom-button-class:hover
		{
			background-color: #d0d0e1; 
		}
		
		.invalidPassword .ui-widget-content,.invalidPassword .ui-state-default
		{
			border: none; 
		}
		
		#passwordUpdateForm input
		{
			margin-bottom: 15px;
			margin-top: 5px;
		}
		#passwordUpdateForm label
		{
			font-weight: bold;
		}
		.passwordDialog .ui-widget-header
		{
			text-align: justify;
			background: #f78e1e;	
		}
		.fa-exclamation-triangle
		{
			color: #ff0000;
		}
		</style>
	</head>
	<body>
		<div id="firstLogin" title="First Login Password Change">
			<p>For security concerns, it is recommended that you change your default password.</p>
			<div id="passwordUpdate">
				<form id="passwordUpdateForm" onsubmit="return false" action="updatePassword" method="post">
					<input type="hidden" name="emp" value='<%=employeeId%>' />
					<input type="hidden" name="roleUser" value="" />
					<label for="pwd">Old Password</label><br/>
					<input type="text" name="pwd" value='<%=password%>' style="background: #f2f2f2" readonly />
					<br/>
					<input type="password" name="password" id="passwrd" placeholder="New Password" required />
					<i class="fa fa-exclamation-triangle" aria-hidden="true" id="defPassword"></i>
					<input type="password" name="confirmPassword" id="confirmPassword" placeholder="Confirm New Password" required />
					<i class="fa fa-exclamation-triangle" aria-hidden="true" id="wrongPassword"></i>
					<br/>
					<div style="float:left"><input type="submit" value="Change" id="changePassword" /></div>
					<div id="passwordValidation" style="margin-left: 100px;"></div>
				</form>
			</div>
		</div>
	</body>
</html>
