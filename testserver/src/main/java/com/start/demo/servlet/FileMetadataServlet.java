package com.start.demo.servlet;

import com.start.demo.componentStart.StartDerby;
import com.start.demo.entity.FileBean;
import com.start.demo.service.FileService;
import com.start.demo.utils.FileEncryptionUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 获取文件元数据服务类
 */
public class FileMetadataServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp){
        System.out.println("doGet");
        resp.setContentType("text/html;charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        //从在Header中获取X-SID和X-Signature属性
        String str = req.getHeader("X-SID");
        String strEncryption = req.getHeader("X-Signature");
        if (str != null && strEncryption != null) {
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
                    String fileName = fileBean.getOriginalName();
                    String fileType = fileBean.getFileType();
                    String fileSize = fileBean.getFileSize();
                    String createTime = fileBean.getCreateTime();
                    String key = fileBean.getDigitalEnvelope();
                    PrintWriter printWriter = resp.getWriter();
                    printWriter.println(fileName);
                    printWriter.println(fileType);
                    printWriter.println(fileSize);
                    printWriter.println(createTime);
                    printWriter.println(key);
                } catch (Exception e) {
                    try {
                        resp.sendError(500, "获取元数据失败");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    e.printStackTrace();
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
