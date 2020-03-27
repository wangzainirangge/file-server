package com.start.demo.servlet;

import com.start.demo.componentStart.StartDerby;
import com.start.demo.entity.FileBean;
import com.start.demo.service.FileService;
import com.start.demo.utils.FileEncryptionUtil;
import com.start.demo.utils.TimeUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;

/**
 * 文件加载服务类
 */
public class FileDownloadServlet extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp){
        System.out.println("doGet");
        resp.setContentType("text/html;charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String line;
        BufferedReader bufferedReader = null;
        //从在Header中获取X-SID和X-Signature属性
        String str = req.getHeader("X-SID");
        String strEncryption = req.getHeader("X-Signature");
        if (str != null && strEncryption != null){
            //通过RSA公钥对字符串解密
            String strDecrypt = FileEncryptionUtil.StrDecrypt(strEncryption);
            //用公钥对 SID 和 Signature 验签，来验证是否是合法的客户端请求
            if (str.equals(strDecrypt)) {
                try {
                    String uuid = (req.getParameter("uuid"));
                    //创建文件操作对象
                    FileService fileService = new FileService(StartDerby.getConnection());
                    //通过id获取元数据
                    FileBean fileBean = fileService.getFileById(uuid);
                    if (fileBean!=null){
                        String fileName = fileBean.getOriginalName();
                        String fileType = fileBean.getFileType();
                        String name = fileName + "." + fileType;
                        String key = fileBean.getDigitalEnvelope();
                        System.out.println(fileBean.getCreateTime());
                        long lt = new Long(fileBean.getCreateTime());
                        String filePath = TimeUtil.dateConversion3(new Date(lt));
                        File file = new File("D:/Test/上传文件/"+filePath+"/"+uuid+".txt");
                        bufferedReader = new BufferedReader(new FileReader(file));
                        // 用来存储文件内容
                        StringBuilder sb = new StringBuilder();
                        // 循环读取流,若不到结尾处
                        while ((line = bufferedReader.readLine()) != null) {
                            sb.append(line);
                        }
                        PrintWriter printWriter = resp.getWriter();
                        printWriter.println(String.valueOf(sb));
                        printWriter.println(name);
                        printWriter.println(key);
                    }
                } catch (Exception e) {
                    try {
                        resp.sendError(410, "文件下载失败");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    e.printStackTrace();
                } finally {
                    try {
                        bufferedReader.close();
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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("doPost");


    }


}
