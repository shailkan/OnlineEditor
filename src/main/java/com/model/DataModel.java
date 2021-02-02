package com.model;
import java.util.ArrayList;

public class DataModel 
{
	private String empId;
	private String category;
	private String title;
	private ArrayList<Image> imageList;
	private String comment;
	private String source;
	private String content;
	private int contentId;
	private String saveSubmit;
	private Role role;
	private ArrayList<Comments> comments = new ArrayList<Comments>();
	private boolean editable = false;
	private Status status;
	
	public DataModel()
	{
	}

	public DataModel(String empId, String category, String title,
			ArrayList<Image> imageList, String comment, String source,
			String content, int contentId, String saveSubmit, Role role,
			ArrayList<Comments> comments, boolean editable, Status status) 
	{
		super();
		this.empId = empId;
		this.category = category;
		this.title = title;
		this.imageList = imageList;
		this.comment = comment;
		this.source = source;
		this.content = content;
		this.contentId = contentId;
		this.saveSubmit = saveSubmit;
		this.role = role;
		this.comments = comments;
		this.editable = editable;
		this.status = status;
	}

	public String getEmpId() 
	{
		return empId;
	}

	public void setEmpId(String empId) 
	{
		this.empId = empId;
	}

	public String getCategory() 
	{
		return category;
	}

	public void setCategory(String category) 
	{
		this.category = category;
	}

	public String getTitle() 
	{
		return title;
	}

	public void setTitle(String title) 
	{
		this.title = title;
	}

	public ArrayList<Image> getImageList() 
	{
		return imageList;
	}

	public void setImageList(ArrayList<Image> imageList) 
	{
		this.imageList = imageList;
	}

	public String getComment() 
	{
		return comment;
	}

	public void setComment(String comment) 
	{
		this.comment = comment;
	}

	public String getSource() 
	{
		return source;
	}

	public void setSource(String source) 
	{
		this.source = source;
	}

	public String getContent() 
	{
		return content;
	}

	public void setContent(String content) 
	{
		this.content = content;
	}

	public int getContentId() 
	{
		return contentId;
	}

	public void setContentId(int contentId) 
	{
		this.contentId = contentId;
	}

	public String getSaveSubmit() 
	{
		return saveSubmit;
	}

	public void setSaveSubmit(String saveSubmit) 
	{
		this.saveSubmit = saveSubmit;
	}

	public Role getRole() 
	{
		return role;
	}

	public void setRole(Role role) 
	{
		this.role = role;
	}

	public ArrayList<Comments> getComments() 
	{
		return comments;
	}

	public void setComments(ArrayList<Comments> comments) 
	{
		this.comments = new ArrayList<Comments>();
		(this.comments).addAll(comments);
	}

	public boolean isEditable() 
	{
		return editable;
	}

	public void setEditable(boolean editable) 
	{
		this.editable = editable;
	}

	public Status getStatus()
	{
		return status;
	}

	public void setStatus(Status status)
	{
		this.status = status;
	}
	
	
}
