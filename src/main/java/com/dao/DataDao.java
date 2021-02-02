package com.dao;

import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;

import com.model.Employee;
import com.model.Data;
import com.model.DataModel;
import com.model.Category;
import com.model.Status;

public interface DataDao {
    Data getData(int contentId);

    Data addData(String title, String content, String source, Category category, Status status);

    Data updateData(Data oldData, String title, String content, String source, Category category, Status status);

    ArrayList<DataModel> getData(Employee employee);

    void lockObject(HttpServletRequest request);

    void removeLocks(String sessionId);

    void unlockObject(HttpServletRequest request);
}
