package com.model;
import java.sql.Timestamp;

public class Logs 
{
	private int logId;
	private Data data;
	private Employee employee;
	private String action;
	private Timestamp timeOfAction;
	
	public Logs()
	{
	}
	public Logs(int logId, Data data, Employee employee, String action,Timestamp timeOfAction) 
	{
		super();
		this.logId = logId;
		this.data = data;
		this.employee = employee;
		this.action = action;
		this.timeOfAction = timeOfAction;
	}
	public int getLogId() 
	{
		return logId;
	}
	public void setLogId(int logId) 
	{
		this.logId = logId;
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
	public String getAction() 
	{
		return action;
	}
	public void setAction(String action) 
	{
		this.action = action;
	}
	public Timestamp getTimeOfAction() 
	{
		return timeOfAction;
	}
	public void setTimeOfAction(Timestamp timeOfAction) 
	{
		this.timeOfAction = timeOfAction;
	}
	
}
