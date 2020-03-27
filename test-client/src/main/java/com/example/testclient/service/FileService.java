package com.example.testclient.service;

import com.example.testclient.entity.FileBean;

import java.util.ArrayList;
import java.util.HashMap;

public interface FileService {

    public FileBean uploadFile(String fileStr, String fileName, String fileType, String fileSize);


    public HashMap<String,String> downloadFile(String uuid);

    public ArrayList<String> getFileBean(String uuid);


}
