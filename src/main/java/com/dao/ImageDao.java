package com.dao;

import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;

import com.model.Data;
import com.model.Image;

public interface ImageDao {
    Image getImage(HttpServletRequest request);

    Image setImageAttr(String description, String name, byte[] img);

    void addImages(ArrayList<Image> images, Data data);

    void updateImage(Data data);

    ArrayList<Image> getImageList(String contentId);
}
