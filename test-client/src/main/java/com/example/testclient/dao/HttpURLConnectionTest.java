package com.example.testclient.dao;

import com.example.testclient.utils.FileEncryptionUtil;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.UUID;

/**
 * 调用服务端get post接口
 */
public class HttpURLConnectionTest {


    /**
     * 接口调用 GET
     */
    public static ArrayList<String> httpURLConectionGET(String GET_URL) {
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            // 把字符串转换为URL请求地址
            URL url = new URL(GET_URL);
            //随机获取uuid
            String uuid = UUID.randomUUID().toString().replaceAll("-","");
            //调用加密工具类通过RSA私钥加密
            String uuidEncryption = FileEncryptionUtil.StrEncryption(uuid);
            // 打开连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // 设置连接输出流为true,默认false
            connection.setDoOutput(true);
            //在Header中添加X-SID和X-Signature 两个属性进行接口权限验证
            connection.setRequestProperty("X-SID",uuid);
            connection.setRequestProperty("X-Signature",uuidEncryption);
            // 设置连接输入流为true
            connection.setDoInput(true);
            // 连接会话
            connection.connect();
            // 获取输入流
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {// 循环读取流
                arrayList.add(line);
            }
            // 关闭流
            br.close();
            connection.disconnect();// 断开连接
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("失败!");
        }finally {
            return arrayList;
        }
    }

    /**
     * 接口调用  POST
     */
    public static ArrayList<String> httpURLConnectionPOST (String POST_URL, String parm) {
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            URL url = new URL(POST_URL);
            //随机获取uuid
            String uuid = UUID.randomUUID().toString().replaceAll("-","");
            //调用加密工具类通过RSA私钥加密
            String uuidEncryption = FileEncryptionUtil.StrEncryption(uuid);
            // 将url以open方法返回的urlConnection连接强转为HttpURLConnection连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //在Header中添加X-SID和X-Signature 两个属性进行接口权限验证
            connection.setRequestProperty("X-SID",uuid);
            connection.setRequestProperty("X-Signature",uuidEncryption);
            // 设置连接输出流为true,默认false
            connection.setDoOutput(true);
            // 设置连接输入流为true
            connection.setDoInput(true);
            // 设置请求方式为post
            connection.setRequestMethod("POST");
            // post请求缓存设为false
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            // 设置请求头里面的各个属性
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            // 建立连接
            connection.connect();
            //创建输入输出流,用于往连接里面输出携带的参数
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            // 将参数输出到连接
            out.writeBytes(parm);
            // 输出完成后刷新并关闭流
            out.flush();
            out.close();
            System.out.println(connection.getResponseCode());
            // 连接发起请求,处理服务器响应  (从连接获取到输入流并包装为bufferedReader)
            BufferedReader bf = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder sb = new StringBuilder(); // 用来存储响应数据
            // 循环读取流,若不到结尾处
            while ((line = bf.readLine()) != null) {
                arrayList.add(line);
            }
            //关闭流
            bf.close();
            // 销毁连接
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            return arrayList;
        }
    }

}
