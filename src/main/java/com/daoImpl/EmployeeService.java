package com.daoImpl;
import org.hibernate.SessionFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import com.model.Employee;

import com.model.Role;
import com.dao.EmployeeDao;

public class EmployeeService implements EmployeeDao
{
	private SessionFactory sessionFactory;
	private Query query;
	public EmployeeService(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	@Transactional
	public Employee getEmployee(int empId)
	{
		Session session = this.sessionFactory.getCurrentSession();
		query = session.createQuery("from Employee E where E.empId =:employeeId");
		query.setParameter("employeeId",empId);
		Employee employee = (Employee)query.uniqueResult();
		return employee;
	}
	
	@Override
	@Transactional
	public Employee getEmployee(HttpServletRequest request)
	{
		String username = (request.getParameter("username")).toLowerCase();
		String password = request.getParameter("password");
		Session session = this.sessionFactory.getCurrentSession();
		query = session.createQuery("from Employee E where lower(E.username) =:username and E.password =:password");
		query.setParameter("username",username);
		query.setParameter("password",password);
		Employee employee = (Employee)query.uniqueResult();
		return employee;
	}
	
	@Override
	@Transactional
	public void addEmployee(HttpServletRequest request)
	{
		Session session = this.sessionFactory.getCurrentSession();
		Employee employee = new Employee();
		employee.setEmpId(Integer.parseInt(request.getParameter("eId")));
		employee.setRole(Role.valueOf(request.getParameter("roleUser")));
		employee.setUsername(request.getParameter("username"));
		employee.setPassword(request.getParameter("password"));
		session.persist(employee);
	}
	
	@Override
	@Transactional
	public boolean isUsernameValid(String username)
	{
		Session session = this.sessionFactory.getCurrentSession();
		query = session.createQuery("from Employee E where lower(E.username) =:username");
		query.setParameter("username",username.toLowerCase());
		return(query.uniqueResult() == null);
	}
	
	@Override
	@Transactional
	public ArrayList<Employee> getEmployeeList(String searchString)
	{
		searchString = "%" + searchString + "%";
		Session session = this.sessionFactory.getCurrentSession();
		ArrayList<Employee> empList = new ArrayList<Employee>(session.createCriteria(Employee.class).add(Restrictions.ilike("username",searchString)).list());
		return empList;
	}

	@Override
	@Transactional
	public void deleteEmployee(int empId)
	{
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createQuery("from Employee where empId = :empid");
		query.setParameter("empId", empId);
		Employee employee = (Employee)query.list().get(0);
		session.delete(employee);
	}
	
	@Override
	@Transactional
	public void updateEmployee(HttpServletRequest request)
	{
		Session session = this.sessionFactory.getCurrentSession();
		Employee employee = (Employee)session.load(Employee.class,Integer.parseInt(request.getParameter("emp")));
		if(request.getParameter("roleUser").equals(""))
			employee.setPassword(request.getParameter("password"));
		else
			employee.setRole(Role.valueOf(request.getParameter("roleUser").replace(" ","_").toUpperCase()));
		session.persist(employee);
	}
	
	@Override
	@Transactional
	public boolean employeeIdAlreadyExists(int empId)
	{
		Session session = this.sessionFactory.getCurrentSession();
		query = session.createQuery("from Employee E where empId = :empId");
		query.setParameter("empId",empId);
		return(query.uniqueResult() == null);
	}
}
