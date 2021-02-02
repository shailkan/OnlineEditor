package com.daoImpl;

import org.hibernate.SessionFactory;
import org.hibernate.Query;
import org.springframework.transaction.annotation.Transactional;
import org.hibernate.Session;
import java.util.List;
import java.util.ArrayList;

import com.model.Category;
import com.dao.CategoryDao;

public class CategoryService implements CategoryDao 
{
	private SessionFactory sessionFactory;
	private Query query;
	
	public CategoryService(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}
	
	@Override @Transactional
	public ArrayList<Category> getCategories()
	{
		Session session = this.sessionFactory.getCurrentSession();
		query = session.createQuery("from Category");
		List<Category> categories = query.list();
		ArrayList<Category> catList = new ArrayList<Category>(categories);
		return catList;
	}

	@Override @Transactional
	public void addNewCategory(String newCategory)
	{
		Session session = this.sessionFactory.getCurrentSession();
		Category category = new Category();
		category.setCatName(newCategory);
		session.persist(category);
	}
	
	@Override @Transactional
	public Category getCategory(int catId)
	{
		Session session = this.sessionFactory.getCurrentSession();
		query = session.createQuery("from Category C where C.catId =:catId");
		query.setParameter("catId",catId);
		Category category = (Category)query.uniqueResult();
		return category;
	}
}
