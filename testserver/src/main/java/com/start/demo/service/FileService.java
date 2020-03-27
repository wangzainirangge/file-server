package com.start.demo.service;

import com.start.demo.componentStart.StartDerby;
import com.start.demo.entity.FileBean;
import com.start.demo.utils.TimeUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class FileService {

    Connection connection;

    public FileService(Connection connection){
        this.connection = connection;
    }

    public void createTable(){
        Statement stat = null;
        String createTable = "CREATE TABLE file_information(" +
                "    id  VARCHAR(100) PRIMARY KEY ," +
                "    file_size VARCHAR(50) NOT NULL default ''," +
                "    file_type VARCHAR(20) NOT NULL default ''," +
                "    original_name VARCHAR(50) NOT NULL default ''," +
                "    create_time VARCHAR(50) NOT NULL default ''," +
                "    digital_envelope VARCHAR(255) NOT NULL default ''" +
                ")";
        try {
            stat = connection.createStatement();
            //创建数据表
            stat.executeUpdate(createTable);
            System.out.println("创建数据表成功");
        } catch (SQLException e) {
            //e.printStackTrace();
        }
    }

    public String addFile(String fileSize,String fileType,String originalName,String digitalEnvelope) throws SQLException {
        Statement stat = null;
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        String nowDate = String.valueOf(System.currentTimeMillis());
        stat = connection.createStatement();
        stat.executeUpdate("INSERT INTO file_information" +
                "(id,file_size,file_type,original_name,create_time,digital_envelope)" +
                " VALUES('"+uuid+"','"+fileSize+"','"+fileType+"','"+originalName+"','"+nowDate+"','"+digitalEnvelope+"')");
        System.out.println("添加数据成功！");
        FileService fileService = new FileService(StartDerby.getConnection());
        return uuid;

    }


    public FileBean getFileById(String id) throws SQLException {
        Statement stat = null;
        FileBean fileBean = null;
        stat = connection.createStatement();
        ResultSet result = stat.executeQuery("SELECT id,file_size,file_type,original_name,create_time,digital_envelope FROM file_information where id = '"+id+"' ");
        //将光标移动到下一行，初始在第一行之前
        if (result.next()){
            fileBean = new FileBean();
            fileBean.setId(result.getString("id"));
            fileBean.setFileSize(result.getString("file_size"));
            fileBean.setFileType(result.getString("file_type"));
            fileBean.setOriginalName(result.getString("original_name"));
            fileBean.setCreateTime(result.getString("create_time"));
            fileBean.setDigitalEnvelope(result.getString("digital_envelope"));
        }
        return fileBean;
    }

    public ArrayList<FileBean> FileList(){
        Statement stat = null;
        ArrayList<FileBean> arrayList = new ArrayList<>();
        FileBean fileBean = new FileBean();
        try {
            stat = connection.createStatement();
            ResultSet result = stat.executeQuery("SELECT id,file_size,file_type,original_name,create_time,digital_envelope FROM file_information");
            //将光标移动到下一行，初始在第一行之前
            while(result.next()){
                fileBean.setId(result.getString("id"));
                fileBean.setFileSize(result.getString("file_size"));
                fileBean.setFileType(result.getString("file_type"));
                fileBean.setOriginalName(result.getString("original_name"));
                long lt = new Long(result.getString("create_time"));
                fileBean.setCreateTime(TimeUtil.dateConversion2(new Date(lt)));
                fileBean.setDigitalEnvelope(result.getString("digital_envelope"));
                arrayList.add(fileBean);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            return arrayList;
        }
    }


    public void deleteTable(){
        Statement stat = null;
        try {
            stat = connection.createStatement();
            stat.executeUpdate("drop table file_information");
            System.out.println("删除数据表成功！");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
