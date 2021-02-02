package com.model;
import java.sql.Timestamp;

public class Comments 
{
	private int commentId;
	private String commentText;
	private Timestamp timeOfComment;
	private Data data;
	private Employee employee;
	
	public Comments()
	{
	}
	public Comments(int commentId, String commentText, Timestamp timeOfComment,Data data, Employee employee) 
	{
		super();
		this.commentId = commentId;
		this.commentText = commentText;
		this.timeOfComment = timeOfComment;
		this.data = data;
		this.employee = employee;
	}

	public int getCommentId() 
	{
		return commentId;
	}

	public void setCommentId(int commentId) 
	{
		this.commentId = commentId;
	}

	public String getCommentText() 
	{
		return commentText;
	}

	public void setCommentText(String commentText) 
	{
		this.commentText = commentText;
	}

	public Timestamp getTimeOfComment() 
	{
		return timeOfComment;
	}

	public void setTimeOfComment(Timestamp timeOfComment) 
	{
		this.timeOfComment = timeOfComment;
	}

	public Data getData() 
	{
		return data;
	}

	public void setData(Data data) 
	{
		this.data = data;
	}

	public Employee getEmployee() 
	{
		return employee;
	}

	public void setEmployee(Employee employee) 
	{
		this.employee = employee;
	}
	
}
