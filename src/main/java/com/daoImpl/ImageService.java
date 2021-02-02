package com.daoImpl;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import org.hibernate.Session;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

import com.model.Data;
import com.model.Image;
import com.dao.ImageDao;

public class ImageService implements ImageDao
{
	Image image = new Image();
	ArrayList<Image> imageList = new ArrayList<Image>(); //clear it when submit or save
	private SessionFactory sessionFactory;
	public ImageService(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
		
	}
	
	@Override
	public Image getImage(HttpServletRequest request)
	{
		File file;
		int maxFileSize = 5000 * 1024;
		int maxMemSize = 5000 * 1024;
		String contentType = request.getContentType();
		String imageDescp = "";
		String imgName = "";
		if((contentType.indexOf("multipart/form-data") >=0 ))
		{
			DiskFileItemFactory factory = new DiskFileItemFactory();
			//maximum size that will be  stored in memory
			factory.setSizeThreshold(maxMemSize);
			//Location to save data that is larger than maxMemSize
			factory.setRepository(new File("temp.jpg"));
			//Create a new file upload handler
			ServletFileUpload upload  = new ServletFileUpload(factory);
			//maximum file size to be uploaded
			upload.setSizeMax(maxFileSize);
			try
			{
				//Parse the request to get file items
				List fileItems = upload.parseRequest(request);
				//Process the uploaded file items
				Iterator i = fileItems.iterator();
				while(i.hasNext())
				{
					FileItem fi = (FileItem)i.next();
					if(fi.isFormField())
					{
						String  fieldName = fi.getFieldName();
						switch(fieldName)
						{
							case "imgName":
								imgName = fi.getString();
								break;
							case "imgDescp":
								imageDescp = fi.getString();
								break;
						}
					}
					else
					{
						//write the file
						file = new File("temp.jpg");
						fi.write(file);
					}
				}
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
			try
			{
				RandomAccessFile f = new RandomAccessFile("temp.jpg", "r");
				byte[] byteArr = new byte[(int)f.length()];
				f.read(byteArr);
				image = setImageAttr(imageDescp,imgName,byteArr);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			return image;
		}
		return null;
	}
	
	public Image setImageAttr(String description, String name, byte[] img)
	{
		image.setDescription(description);
		image.setName(name);
		image.setImg(img);
		return image;
	}
	
	@Override @Transactional
	public void addImages(ArrayList<Image> images,Data data)
	{
		if(!images.isEmpty())
		{
			Session session = this.sessionFactory.getCurrentSession();
			for(Image image: images)
			{
				image.setImgId(0);
				image.setData(data);
				session.persist(image);
			}
		}
	}
	
	@Override @Transactional
	public void updateImage(Data data)
	{
		Session session = this.sessionFactory.getCurrentSession();
		String deleteQuery = "delete from Image where contentId=:contentId";
		Query query = session.createQuery(deleteQuery);
		query.setInteger("contentId",data.getContentId());
		query.executeUpdate();
	}
	
	@Override @Transactional
	public ArrayList<Image> getImageList(String contentId)
	{
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createQuery("from Image where contentId=:contentId");
		query.setParameter("contentId",Integer.parseInt(contentId));
		List<Image> images = query.list();
		ArrayList<Image> imageList = new ArrayList<Image>(images);
		return imageList;
	}
}
