package com.model;

import java.util.Arrays;

public enum Role 
{
	ADMIN,EDITOR,SENIOR_EDITOR,REVIEWER,DEVELOPER;
	
	public static String[] names()
	{
		String text = Arrays.toString(values());
		return text.substring(1,text.length()-1).split(", ");
	}
}
