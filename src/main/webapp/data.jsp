<%com.model.Status status = com.model.Status.valueOf(request.getParameter("status")); %>
<%String empRole = request.getParameter("role"); %>
<%String statusString = org.apache.commons.lang3.text.WordUtils.capitalizeFully(request.getParameter("status")).replace('_', ' '); %>
<form id="form" onsubmit = "return false" enctype="multipart/form-data" >
	<label for= "status" class="no-grid">Status</label>
	<select id="status">
		<option value='<%=status%>'><%=statusString%></option> 
	</select>
	<br>
	<label for="create-category" class="no-grid">Category</label>
	<!-- Categories from database -->	
	<select id="create-category"></select> 
	<a id="new-category">Add new category</a>
	<div id='category-validation'></div>
	<div id='category-dialog' title = 'New Category'>
		<input type=text name = 'newCategory' id = 'newCategory'/>
		<br>
		<input type=button value='Add' id='addCategory'/> 
	</div>
	<br>
	<!-- validation according to jsp param, new or not -->
	<div id="title-display-create">
		<label for="title-create" class="no-grid">Title</label>
		<input type="text" id="title-create" />
		<!-- title validation div -->
	</div>
	<br>
	<div class="container-fluid" id="grid-create">
		<div class="row">
			<div class="col-sm-9">
				<div id="content"></div>
				<label for="summernote" >Content</label>
					<div id='content-validation'></div>
					<p contenteditable="true" spellcheck="true" id="summernote-content">
						<div id="summernote" class="summernote"></div>
					</p>
					<br>
					<div id="images">
						<a><i class="fa fa-plus-circle"></i> Add images/graphics</a>
					</div>
					<jsp:include page="createDialog.jsp" />
					<label for="images" >Images/graphics</label>
				<!-- for displaying images -->
				<div id="dvPreview"></div>
			</div>
			<div class="col-sm-3" id = "comments-view">
				<label for="comments">Comments</label>
				<div id="comments">
					<div id="oldComments">
					</div>
					<div id="postedComment">
					</div>	
					<div id="newComment">
						<textarea id = "commentText" placeholder="Enter your comment here" style = "margin-left: 10px; width: 96%;"></textarea>
						<input type="button" value="Post" class="post"/>
					</div>
				</div>
			</div>
		</div>
	</div>
	<br>
	<div id='source-display-create'>
		<label for="source-create" class="no-grid"> Source</label>
		<input type="text" id="source-create"/>
	</div>
	<br>
	<div id='btn-display-create' class="no-grid">
		<% if(!empRole.equals("EDITOR")) {%>
			<input type="button" value="Revert" class="buttons" id = "revert" onclick = "submitData(empId,'revert')" />
		<% } %>
		<input type="button" value="Save" class="buttons" id = "save" onclick = "saveData(empId,'save')" />
		<% if(!empRole.equals("REVIEWER")) {%>
			<input type="button" value="Submit" class="buttons" id = "submit" onclick="submitData(empId,'submit')"/>
		<% } else { %>
			<input type="button" value="Freeze" class="buttons" id = "freeze" onclick = "submitData(empId,'freeze')" />
		<% } %>
		<% if(empRole.equals("DEVELOPER") && !(status.equals(com.model.Status.PUBLISHED))) { %>
			<input type="button" value="Publish" class="buttons" id = "publish" onclick = "saveData(empId,'publish')" />
		<%	} %>
		<% if(status.equals(com.model.Status.FROZEN) && !(empRole.equals("DEVELOPER"))) { %>
			<input type="button" value="Unfreeze" class="buttons" id = "unfreeze" onclick = "unfreezeContent()" />
		<% } %>
	</div>
</form>
