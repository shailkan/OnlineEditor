package com.model;
import java.util.HashSet;
import java.util.Set;

public class Category 
{
	private int catId;
	private String catName;
	private Set<Data> data = new HashSet<Data>(0);
	
	public Category()
	{
	}
	public Category(int catId, String catName, Set<Data> data) 
	{
		super();
		this.catId = catId;
		this.catName = catName;
		this.data = data;
	}
	public int getCatId() 
	{
		return catId;
	}
	public void setCatId(int catId) 
	{
		this.catId = catId;
	}
	public String getCatName() 
	{
		return catName;
	}
	public void setCatName(String catName) 
	{
		this.catName = catName;
	}
	public Set<Data> getData() 
	{
		return data;
	}
	public void setData(Set<Data> data) 
	{
		this.data = data;
	}
}
