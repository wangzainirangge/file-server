package com.example.testclient.service.Impl;

import com.example.testclient.dao.HttpURLConnectionTest;
import com.example.testclient.entity.FileBean;
import com.example.testclient.service.FileService;
import com.example.testclient.utils.TimeUtil;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * 文件接口服务层
 */
@Service
public class FileServiceImpl implements FileService {



    /**
     * 上传文件
     * @param fileStr   文件内容
     * @param fileName  文件名称
     * @param fileType  文件类型
     * @param fileSize  文件大小
     * @return
     */
    @Override
    public FileBean uploadFile(String fileStr,String fileName,String fileType,String fileSize){
        String uuid = null;
        String url = "http://localhost:9999/FileUploadServlet";
        ArrayList<String> arrayList;
        FileBean fileBean = new FileBean();
        try {
            String parm = "mufile="+ URLEncoder.encode(fileStr, "utf-8") +
                    "&fileName=" + URLEncoder.encode(fileName, "utf-8") +
                    "&fileType=" + URLEncoder.encode(fileType, "utf-8") +
                    "&fileSize=" + URLEncoder.encode(fileSize, "utf-8");
            arrayList = HttpURLConnectionTest.httpURLConnectionPOST(url,parm);
            uuid = arrayList.get(0);
            FileService fileService = new FileServiceImpl();
            arrayList = fileService.getFileBean(uuid);
            fileBean.setId(uuid);
            fileBean.setOriginalName(arrayList.get(0));
            fileBean.setFileType(arrayList.get(1));
            fileBean.setFileSize(arrayList.get(2));
            long lt = new Long(arrayList.get(3));
            fileBean.setCreateTime(TimeUtil.dateConversion2(new Date(lt)));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }finally {
            return fileBean;
        }
    }

    /**
     * 下载文件
     * @param uuid  文件id
     * @return  文件数据
     */
    @Override
    public HashMap<String,String> downloadFile(String uuid) {
        //服务端下载文件路径
        String url = "http://localhost:9999/FileDownloadServlet?uuid="+uuid;
        HashMap<String,String> hashMap = new HashMap<>();
        ArrayList<String> arrayList;
        String fileStr = "";
        arrayList = HttpURLConnectionTest.httpURLConectionGET(url);
        if (arrayList.size()!=0){
            fileStr = arrayList.get(0);
            String name = arrayList.get(1);
            String key = arrayList.get(2);
            hashMap.put("fileStr",fileStr);
            hashMap.put("name",name);
            hashMap.put("key",key);
        }
        return hashMap;
    }

    /**
     * 通过文件id获取文件元数据
     * @param uuid 文件id
     * @return 文件元数据
     */
    @Override
    public ArrayList<String> getFileBean(String uuid){
        //服务端获取文件元数据
        String url = "http://localhost:9999/FileMetadataServlet?uuid="+uuid;
        ArrayList<String> arrayList = HttpURLConnectionTest.httpURLConectionGET(url);
        return arrayList;
    }

}
