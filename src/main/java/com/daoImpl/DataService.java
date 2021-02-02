package com.daoImpl;

import org.hibernate.SessionFactory;
import org.hibernate.Query;
import org.springframework.transaction.annotation.Transactional;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import com.model.Employee;
import com.model.Data;
import com.dao.DataDao;
import com.model.DataModel;
import com.model.Role;
import com.model.Category;
import com.model.Status;

public class DataService implements DataDao 
{
	private SessionFactory sessionFactory;
	private Query query;
	
	public DataService(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}
	
	@Override @Transactional
	public Data getData(int contentId)
	{
		Session session = this.sessionFactory.getCurrentSession();
		query = session.createQuery("from Data D where D.contentId =:contentId");
		query.setParameter("contentId",contentId);
		Data data = new Data();
		data = (Data)query.uniqueResult();
		return data;
	}	
	
	@Override @Transactional
	public Data addData(String title, String content, String source, Category category, Status status)
	{
		Session session = this.sessionFactory.getCurrentSession();
		Data data = new Data();
		data.setCategory(category);
		data.setContent(content);
		data.setDateCreated(new java.sql.Date(Calendar.getInstance().getTime().getTime()));
		data.setSource(source);
		data.setStatus(status);
		data.setTitle(title);
		session.persist(data);
		return data;
	}
	
	@Override @Transactional
	public Data updateData(Data oldData,String title,String content,String source,Category category,Status status)
	{
		Session session = this.sessionFactory.getCurrentSession();
		Data data = (Data)session.load(Data.class,oldData.getContentId());
		data.setCategory(category);
		data.setContent(content);
		data.setDateCreated(new java.sql.Date(Calendar.getInstance().getTime().getTime()));
		data.setSource(source);
		data.setStatus(status);
		data.setTitle(title);
		session.persist(data);
		return data;
	}
	
	@Override @Transactional
	public ArrayList<DataModel> getData(Employee employee)
	{
		Role role = employee.getRole();
		Session session = this.sessionFactory.getCurrentSession();
		query = session.createQuery("from Data as data order by dateCreated desc");
		ArrayList<Data> dataList = new ArrayList<Data>(query.list());
		ArrayList<DataModel> dataModelList = new ArrayList<DataModel>();
		//get comment id of last comment
		for(Data data:dataList)
		{
			DataModel dataModel = new DataModel();
			dataModel.setCategory(data.getCategory().getCatName());
			dataModel.setContent(data.getContent());
			dataModel.setContentId(data.getContentId());
			dataModel.setTitle(data.getTitle());
			dataModel.setStatus(data.getStatus());
			dataModel.setEmpId(Integer.toString(employee.getEmpId()));
			dataModel.setRole(employee.getRole());
			if((role == Role.EDITOR && (data.getStatus() == Status.NEW || data.getStatus() == Status.SAVED_EDITOR || data.getStatus() == Status.REQUIRES_EDITOR_ATTENTION)) 
				|| 
				(role == Role.SENIOR_EDITOR && (data.getStatus() == Status.SAVED_SENIOR_EDITOR || data.getStatus() == Status.REQUIRES_SENIOR_EDITOR_ATTENTION || data.getStatus() == Status.SUBMITTED_TO_SENIOR_EDITOR))
				||
				(role == Role.REVIEWER && (data.getStatus() == Status.SAVED_REVIEWER || data.getStatus() == Status.SUBMITTED_TO_REVIEWER)))
				dataModel.setEditable(true);
			dataModel.setSource(data.getSource());
			if(role != Role.DEVELOPER || (data.getStatus() == Status.FROZEN || data.getStatus() == Status.PUBLISHED))
				dataModelList.add(dataModel);
		}
		return dataModelList;
	}
	
	@Override
	@Transactional
	public void lockObject(HttpServletRequest request)
	{
		Session session = this.sessionFactory.getCurrentSession();
		Data data = (Data) session.load(Data.class,Integer.parseInt(request.getParameter("contentId")));
		data.setSessionId(request.getSession().getId());
		session.persist(data);
	}
	
	@Override
	@Transactional
	public void removeLocks(String sessionId)
	{
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createQuery("update Data set sessionId = '' where sessionId = :sessionId");
		query.setParameter("sessionId",sessionId);
		query.executeUpdate();
	}
	
	@Override
	@Transactional
	public void unlockObject(HttpServletRequest request)
	{
		Session session = this.sessionFactory.getCurrentSession();
		Data data = (Data) session.load(Data.class,Integer.parseInt(request.getParameter("contentId")));
		data.setSessionId("");
		session.persist(data);
	}
}
