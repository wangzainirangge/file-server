package com.start.demo.servlet;

import com.start.demo.componentStart.StartDerby;
import com.start.demo.service.FileService;
import com.start.demo.utils.FileEncryptionUtil;
import com.start.demo.utils.TimeUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;
import java.util.HashMap;

/**
 * 文件上传服务类
 */
public class FileUploadServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("doGet");
        resp.setContentType("text/html;charset=UTF-8");

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp){
        System.out.println("doPost");
        resp.setContentType("text/html;charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String fileStr = null;
        String fileName = null;
        String fileSize = null;
        String fileType = null;
        String encryptionFile = null;
        String digitalEnvelope = null;
        BufferedOutputStream out = null;
        //从在Header中获取X-SID和X-Signature属性
        String str = req.getHeader("X-SID");
        String strEncryption = req.getHeader("X-Signature");
        if (str != null && strEncryption != null){
            //通过RSA公钥对字符串解密
            String strDecrypt = FileEncryptionUtil.StrDecrypt(strEncryption);
            //用公钥对 SID 和 Signature 验签，来验证是否是合法的客户端请求
            if (str.equals(strDecrypt)){
                try {
                    fileStr = (req.getParameter("mufile"));
                    fileName = (req.getParameter("fileName"));
                    fileSize = (req.getParameter("fileSize"));
                    fileType = (req.getParameter("fileType"));
                    //调用时间转换工具类获取到文件所在的文件夹
                    String TimeMillis = TimeUtil.dateConversion3(new Date());
                    File file = new File("D:/Test/上传文件/"+TimeMillis);
                    if(!file.exists()){
                        //如果文件夹不存在创建文件夹
                        file.mkdirs();
                    }
                    //调用文件加密工具类对文件加密
                    HashMap<String, String> hashMap = FileEncryptionUtil.fileEncryption(fileStr);
                    //加密后的文件
                    encryptionFile = hashMap.get("aesFile");
                    //数字信封
                    digitalEnvelope = hashMap.get("keyCipher");
                    //创建文件操作对象
                    FileService fileService = new FileService(StartDerby.getConnection());
                    //存储元数据并接收id
                    String uuid = fileService.addFile(fileSize,fileType,fileName,digitalEnvelope);
                    out = new BufferedOutputStream(new FileOutputStream(new File(file.getPath()+"/"+uuid+".txt")));
                    out.write(encryptionFile.getBytes());
                    out.flush();
                    PrintWriter printWriter = resp.getWriter();
                    printWriter.println(uuid);
                } catch (Exception e) {
                    try {
                        resp.sendError(500, "文件上传失败");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    e.printStackTrace();
                }finally {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }else {
                try {
                    resp.sendError(403, "校验接口权限失败");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
