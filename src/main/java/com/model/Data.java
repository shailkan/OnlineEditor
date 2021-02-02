package com.model;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

public class Data
{
	private int contentId;
	private Date dateCreated;
	private String Title;
	private String content;
	private String source;
	private Status status;
	@Column(unique = true)
	private String sessionId;
	private Category category;
	private Set<Comments> comments = new HashSet<Comments>(0);
	private Set<Logs> logs = new HashSet<Logs>(0);
	private Set<Image> image = new HashSet<Image>(0);
	
	public Data()
	{
	}

	public Data(int contentId, Date dateCreated, String title, String content,
			String source, Status status, Category category,String sessionId,
			Set<Comments> comments, Set<Logs> logs, Set<Image> image) 
	{
		super();
		this.contentId = contentId;
		this.dateCreated = dateCreated;
		Title = title;
		this.content = content;
		this.source = source;
		this.status = status;
		this.category = category;
		this.comments = comments;
		this.logs = logs;
		this.image = image;
		this.sessionId = sessionId;
	}

	public int getContentId() 
	{
		return contentId;
	}

	public void setContentId(int contentId) 
	{
		this.contentId = contentId;
	}

	public Date getDateCreated() 
	{
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) 
	{
		this.dateCreated = dateCreated;
	}

	public String getTitle() 
	{
		return Title;
	}

	public void setTitle(String title) 
	{
		Title = title;
	}

	public String getContent() 
	{
		return content;
	}

	public void setContent(String content) 
	{
		this.content = content;
	}

	public String getSource() 
	{
		return source;
	}

	public void setSource(String source) 
	{
		this.source = source;
	}
	
	@Enumerated(EnumType.ORDINAL)
	public Status getStatus() 
	{
		return status;
	}

	public void setStatus(Status status) 
	{
		this.status = status;
	}

	public Category getCategory() 
	{
		return category;
	}

	public void setCategory(Category category) 
	{
		this.category = category;
	}

	public Set<Comments> getComments() 
	{
		return comments;
	}

	public void setComments(Set<Comments> comments) 
	{
		this.comments = comments;
	}

	public Set<Logs> getLogs() 
	{
		return logs;
	}

	public void setLogs(Set<Logs> logs) 
	{
		this.logs = logs;
	}

	public Set<Image> getImage() 
	{
		return image;
	}

	public void setImage(Set<Image> image) 
	{
		this.image = image;
	}

	public String getSessionId() 
	{
		return sessionId;
	}

	public void setSessionId(String sessionId) 
	{
		this.sessionId = sessionId;
	}
	
	
}
