<%request.getSession().setAttribute("employee",session.getAttribute("employee")); %>
<%String empRole = request.getParameter("role"); %>
<script>
	const empRole = "<%=empRole%>";

	function pulseChoice()
	{
		hide();
		if(empRole === "EDITOR")
		{
			
			$("#editorChoice").show();
		}
		else
			$("#reviewerChoice").show();
			
	}
	function faqChoice()
	{
		alert("Hi");
	}
	function hide()
	{
		$("#choice").hide();
		$("#home").show();
	}
	function show()
	{
		$("#choice").show();
		$("#home").hide();
		$("#editorChoice").hide();
		$("#reviewerChoice").hide();
	}
</script>
<style>
	#choice,#home
	{
		margin-left: 40px;
	}
	#choice
	{
		margin-top: 40px;
	}
	#choice a
	{
		font-weight: bold;
		font-size: 16px;
	}
	br
	{
		line-height: 20px;
	}
</style>
<br/>
<a onclick = show()><i class="fa fa-home" aria-hidden="true" id="home"></i>Home</a>
<div id="choice">
	<a id='pulse' class="pulseFaq" onclick=pulseChoice()>Finacle Pulse</a><br/>
	<a id='FAQ' class="pulseFaq" onclick=faqChoice()>Finacle Answer</a>
</div>

<div id="editorChoice" style='display: none;'>
	<jsp:include page="editor.jsp">
				<jsp:param name='role' value='<%=empRole %>' />
	</jsp:include>
</div>
<%com.model.Status status = com.model.Status.NEW; %>
<div id="reviewerChoice" style='display: none;'>
	<script>fillData();</script>
	<jsp:include page="view.jsp">
		<jsp:param name='status' value='<%=status %>' />
		<jsp:param name='role' value='<%=empRole %>' />
	</jsp:include>
</div>

