package com.model;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Image 
{
	private int imgId;
	@JsonIgnore
	private Data data;
	private String name;
	private String description;
	private byte[] img;
	
	public Image()
	{
	}

	public Image(int imgId, Data data, String name, String description,byte[] img) 
	{
		super();
		this.imgId = imgId;
		this.data = data;
		this.name = name;
		this.description = description;
		this.img = img;
	}

	public int getImgId() 
	{
		return imgId;
	}

	public void setImgId(int imgId) 
	{
		this.imgId = imgId;
	}

	public Data getData() 
	{
		return data;
	}

	public void setData(Data data) 
	{
		this.data = data;
	}

	public String getName() 
	{
		return name;
	}

	public void setName(String name) 
	{
		this.name = name;
	}

	public String getDescription() 
	{
		return description;
	}

	public void setDescription(String description) 
	{
		this.description = description;
	}

	public byte[] getImg() 
	{
		return img;
	}

	public void setImg(byte[] img) 
	{
		this.img = img;
	}
	
}
