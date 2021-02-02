package com.action;

import com.dao.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AEditorController implements HttpSessionListener
{
	@Autowired
	private EmployeeDao employeeDao;
	@Autowired
	private ImageDao imageDao;
	@Autowired
	private CategoryDao categoryDao;
	@Autowired
	private CommentsDao commentsDao;
	@Autowired
	private DataDao dataDao;
	
	//String redirectView = "http://localhost:8180/AEditor"
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String editor(HttpServletRequest request)
	{
		String view = "";
		request.getSession().setAttribute("error","none");
		if(request.getSession().getAttribute("employee") == null)
			view = "login";
		else
			view = "home";
		return view;
	}
	
	@RequestMapping(value = "/getEmployee", method = {RequestMethod.GET,RequestMethod.POST})
	public String getEmployee(HttpServletRequest request) throws IOException
	{
		if(request.getMethod() == "GET")
			return "editor";
		Employee employee = employeeDao.getEmployee(request);
		if(employee == null)
		{
			request.getSession().setAttribute("error","error");
			return "login";
		}
		request.getSession().setAttribute("employee",employee);
		if(employee.getPassword().equals("dummy123"))
			return "firstLogin";
		return "home";
	}
	
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public void uploadImage(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		Image image = new Image();
		image = imageDao.getImage(request);
		//Jackson class that handles JSON marshalling
		ObjectMapper objectMapper = new ObjectMapper();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		//marshalling the data of your loginResult in JSON format
		String json = objectMapper.writeValueAsString(image);
		response.getWriter().write(json);
	}
	
	@RequestMapping(value = "/getCategories", method = RequestMethod.GET)
	public @ResponseBody Map<Integer,String>getCategories() throws IOException
	{
		Map<Integer,String> categoryMap = new LinkedHashMap<Integer,String>();
		ArrayList<Category> categoryList = categoryDao.getCategories();
		for(Category category:categoryList)
			categoryMap.put(category.getCatId(),category.getCatName());
		return categoryMap;
	}
	
	@RequestMapping(value = "/addCategory", method = RequestMethod.POST)
	public @ResponseBody String addCategory(HttpServletRequest request)
	{
		categoryDao.addNewCategory(request.getParameter("newCategory"));
		return "success";
	}

	@RequestMapping(value = "/getComments", method = RequestMethod.GET)
	public @ResponseBody String[][] getComments(String contentId) throws IOException
	{
		ArrayList<Comments> commentList = commentsDao.getComments(contentId);
		String[][] commentsMap = new String[commentList.size()][2];
		for(int i=0;i<commentList.size();i++)
		{
			Comments comment = commentList.get(i);
			commentsMap[i][0] = comment.getEmployee().getUsername();
			commentsMap[i][1] = comment.getCommentText();
		}
		return commentsMap;
	}
	
	@RequestMapping(value = "/saveOrSubmit", method = RequestMethod.POST)
	public String saveOrSubmit(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
		String json = "";
		if(br != null)
			json = br.readLine();
		ObjectMapper mapper = new ObjectMapper();
		DataModel dataModel = mapper.readValue(json,DataModel.class);
		ArrayList<Image> images = new ArrayList<Image>(dataModel.getImageList());
		Data data;
		Employee employee = employeeDao.getEmployee(Integer.parseInt(dataModel.getEmpId()));
		Status status = dataModel.getStatus();

		if(dataModel.getSaveSubmit().equals("SAVE"))
		{
			if(employee.getRole() == Role.EDITOR)
				status = status.SAVED_EDITOR;
			else if(employee.getRole() == Role.SENIOR_EDITOR)
				status = Status.SAVED_SENIOR_EDITOR;
			else
				status = Status.SAVED_REVIEWER;
		}
		else if(dataModel.getSaveSubmit().equals("REVERT"))
		{
			if(employee.getRole() == Role.SENIOR_EDITOR)
				status = Status.REQUIRES_EDITOR_ATTENTION;
			else //Reviewer only
				status = Status.REQUIRES_SENIOR_EDITOR_ATTENTION;
		}
		else if(dataModel.getSaveSubmit().equals("PUBLISH"))
			status = Status.PUBLISHED;
		else //SUBMIT or FREEZE
		{
			if(employee.getRole() == Role.EDITOR)
				status = status.SUBMITTED_TO_SENIOR_EDITOR;
			else if(employee.getRole() == Role.SENIOR_EDITOR)
				status = Status.SUBMITTED_TO_REVIEWER;
			else
				status = Status.FROZEN;
		}
		Category category = categoryDao.getCategory(Integer.parseInt(dataModel.getCategory()));
		if(dataModel.getStatus() == Status.NEW)
		{
			data = dataDao.addData(dataModel.getTitle(),dataModel.getContent(),dataModel.getSource(),category,status);
			if(dataModel.getComment().length()>0)
				commentsDao.addComments(data,employee,dataModel.getComment());
			imageDao.addImages(images,data);
		}
		else
		{
			int commentId = 0;
			data = dataDao.getData(dataModel.getContentId());
			List<Comments> comments = (commentsDao.getComments(Integer.toString(dataModel.getContentId())));
			int lastUserCommenting;
			if(comments.size() > 0)
			{
				commentId = (comments.get(comments.size()-1)).getCommentId();
				lastUserCommenting = comments.get(comments.size()-1).getEmployee().getEmpId();
				if(lastUserCommenting != employee.getEmpId())
					commentId = 0;
			}
			data = dataDao.updateData(data,dataModel.getTitle(),dataModel.getContent(),dataModel.getSource(),category,status);
			if(employee.getRole() != Role.DEVELOPER && dataModel.getComment().length() > 0)
			{
				if(commentId > 0 && (dataModel.getStatus() == Status.SAVED_EDITOR || dataModel.getStatus() == Status.SAVED_SENIOR_EDITOR))
					commentsDao.updateComment(commentId,data,employee,dataModel.getComment());
				else
					commentsDao.addComments(data,employee,dataModel.getComment());
			}

			imageDao.updateImage(data);
			imageDao.addImages(images,data);
		}
		request.getSession().setAttribute("employee",employee);
		if(employee.getRole() == Role.EDITOR)
			return "home";
		return "view";
	}
		
	@RequestMapping(value = "/getView", method = RequestMethod.GET)
	public @ResponseBody ArrayList<DataModel> getView(HttpServletRequest request)
	{
		Employee employee = employeeDao.getEmployee(Integer.parseInt(request.getParameter("empId")));
		ArrayList<DataModel> dataList = dataDao.getData(employee);
		return dataList;
	}
	
	@RequestMapping(value = "/getImages", method = RequestMethod.GET)
	public 	@ResponseBody ArrayList<Image> getImages(HttpServletRequest request)
	{
		ArrayList<Image> imageList = imageDao.getImageList(request.getParameter("contentId"));
		return imageList;
	}
	
	@RequestMapping(value = "/createUser", method = RequestMethod.POST)
	public String createUser(HttpServletRequest request)
	{
		employeeDao.addEmployee(request);
		request.getSession().setAttribute("employee",employeeDao.getEmployee(Integer.parseInt(request.getParameter("empId"))));
		return "home";
	}
	
	@RequestMapping(value = "/validateUsername", method = RequestMethod.GET)
	public @ResponseBody boolean validateUsername(HttpServletRequest request)
	{
		return (employeeDao.isUsernameValid(request.getParameter("username")));
	}
	
	@RequestMapping(value = "/validateEmpId", method = RequestMethod.GET)
	public @ResponseBody boolean validateEmpId(HttpServletRequest request)
	{
		return(employeeDao.employeeIdAlreadyExists(Integer.parseInt(request.getParameter("empId"))));
	}
	
	@RequestMapping(value = "/getUsers", method = RequestMethod.GET)
	public @ResponseBody ArrayList<Employee> getUsers(HttpServletRequest request)
	{
		ArrayList<Employee> empList = employeeDao.getEmployeeList(request.getParameter("searchString"));
		return empList;
	}
	
	@RequestMapping(value = "/deleteUser", method = RequestMethod.GET)
	public @ResponseBody void deleteUser(HttpServletRequest request)
	{
		employeeDao.deleteEmployee(Integer.parseInt(request.getParameter("employeeId")));
	}
	
	@RequestMapping(value = "/updateUser", method = RequestMethod.GET)
	public @ResponseBody void updateUser(HttpServletRequest request)
	{
		employeeDao.updateEmployee(request);
	}
	
	@RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
	public String updatePassword(HttpServletRequest request) throws IOException
	{
		employeeDao.updateEmployee(request);
		request.getSession().setAttribute("employee",employeeDao.getEmployee(Integer.parseInt(request.getParameter("emp"))));
		return "home";
	}
	
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request)
	{
		request.getSession().removeAttribute("employee");
		dataDao.removeLocks(request.getSession().getId());
		return editor(request);
	}
	
	@RequestMapping(value = "/lockDocument", method = RequestMethod.GET)
	public @ResponseBody void updateSession(HttpServletRequest request)
	{
		dataDao.lockObject(request);
	}
	
	@RequestMapping(value = "/unlockDocument", method = RequestMethod.GET)
	public @ResponseBody void removeSessionId(HttpServletRequest request)
	{
		dataDao.unlockObject(request);
	}
	
	@RequestMapping(value = "/removeLocks", method = RequestMethod.GET)
	public @ResponseBody void removeLocks(HttpServletRequest request)
	{
		dataDao.removeLocks(request.getSession().getId());
	}
	
	@Override
	public void sessionCreated(HttpSessionEvent sessionEvent)
	{
		//T0000 Auto-generated method stub
	}
	
	@Override
	public void sessionDestroyed(HttpSessionEvent sessionEvent)
	{
		//T0000 Auto-generated method stub
		dataDao.removeLocks(sessionEvent.getSession().getId());
	}
}
