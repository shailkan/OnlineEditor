package com.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

public class Employee 
{
	private int empId;
	private Role role;
	private String username;
	private String password;
	@JsonIgnore
	private Set<Comments> comments = new HashSet<Comments>(0);
	@JsonIgnore
	private Set<Logs> logs = new HashSet<Logs>(0);
	
	public Employee()
	{
	}
	public Employee(int empId, Role role, String username, String password,
			Set<Comments> comments, Set<Logs> logs) 
	{
		super();
		this.empId = empId;
		this.role = role;
		this.username = username;
		this.password = password;
		this.comments = comments;
		this.logs = logs;
	}
	public int getEmpId() 
	{
		return empId;
	}
	public void setEmpId(int empId) 
	{
		this.empId = empId;
	}
	public Role getRole() 
	{
		return role;
	}
	public void setRole(Role role) 
	{
		this.role = role;
	}
	public String getUsername() 
	{
		return username;
	}
	public void setUsername(String username) 
	{
		this.username = username;
	}
	public String getPassword() 
	{
		return password;
	}
	public void setPassword(String password) 
	{
		this.password = password;
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
}
