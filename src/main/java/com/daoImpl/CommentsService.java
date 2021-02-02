package com.daoImpl;

import org.hibernate.SessionFactory;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.hibernate.Session;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import com.model.Employee;
import com.model.Data;
import com.model.Comments;
import com.dao.EmployeeDao;
import com.dao.DataDao;
import com.dao.CommentsDao;

public class CommentsService implements CommentsDao {
    private SessionFactory sessionFactory;
    private Query query;

    @Autowired
    EmployeeDao employeeDao;
    @Autowired
    DataDao dataDao;

    public CommentsService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    @Transactional
    public ArrayList<Comments> getComments(String contentId) {
        Session session = this.sessionFactory.getCurrentSession();
        query = session.createQuery("from Comments as cmnts left outer join fetch cmnts.employee where contentId = :contentId order by timeOfComment");
        query.setParameter("contentId", Integer.parseInt(contentId));
        List<Comments> comments = query.list();
        ArrayList<Comments> commentsList = new ArrayList<Comments>(comments);
        return commentsList;
    }

    @Override
    @Transactional
    public void addComments(Data data, Employee employee, String commentText) {
        if (!commentText.isEmpty()) {
            Session session = this.sessionFactory.getCurrentSession();
            Comments comment = new Comments();
            comment.setCommentText(commentText);
            comment.setEmployee(employee);
            comment.setData(data);
            comment.setTimeOfComment(new Timestamp(new Date().getTime()));
            session.persist(comment);
        }
    }

    @Override
    @Transactional
    public void updateComment(int commentId, Data data, Employee employee, String commentText) {
        Session session = this.sessionFactory.getCurrentSession();
        Comments comment = (Comments) session.load(Comments.class, commentId);
        comment.setCommentText(commentText);
        comment.setEmployee(employee);
        comment.setData(data);
        comment.setTimeOfComment(new Timestamp(new Date().getTime()));
        session.persist(comment);
    }
}
