package com.dao;

import java.util.ArrayList;

import com.model.Employee;
import com.model.Data;
import com.model.Comments;

public interface CommentsDao {
    ArrayList<Comments> getComments(String contentId);

    void addComments(Data data, Employee employee, String commentText);

    void updateComment(int commentId, Data data, Employee employee, String commentText);
}
