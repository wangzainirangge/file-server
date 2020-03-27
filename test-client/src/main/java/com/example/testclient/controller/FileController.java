package com.example.testclient.controller;

import com.example.testclient.entity.FileBean;
import com.example.testclient.service.FileService;
import com.example.testclient.utils.ByteCastUtil;
import com.example.testclient.utils.FileEncryptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

@CrossOrigin
@Controller
@RequestMapping(value = "/file")
public class FileController {

    @Autowired
    private FileService fileService;

    /**
     * 上传文件接口
     * @param mufile 上传的文件
     * @param model 模板对象
     * @return 自动生成的文件id
     */
    @RequestMapping(value = {"/uploadFile"},method = RequestMethod.POST)
    public String uploadFile(@RequestParam("mufile") MultipartFile mufile,
                             Model model){
        BufferedOutputStream out = null;
        FileBean fileBean = null;
        String fileStr = null;
        String fileName = null;
        String fileSize = null;
        String fileType = null;
        if (!mufile.isEmpty()){
            String originalFilename = mufile.getOriginalFilename();
            int index = originalFilename.lastIndexOf(".");
            fileName = originalFilename.substring(0,index);
            fileSize = String.valueOf(mufile.getSize());
            fileType = originalFilename.substring(index+1);
            try {
                fileStr = ByteCastUtil.toHexString(mufile.getBytes());
                fileBean = fileService.uploadFile(fileStr,fileName,fileType,fileSize);
                model.addAttribute("status",true);
                model.addAttribute("uuid",fileBean.getId());
                model.addAttribute("fileName",fileBean.getOriginalName());
                model.addAttribute("fileType",fileBean.getFileType());
                model.addAttribute("fileSize",fileBean.getFileSize());
                model.addAttribute("createTime",fileBean.getCreateTime());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "index";
    }

    /**
     * 下载文件接口
     * @param uuid  文件id
     * @param response  下载响应对象
     */

    @RequestMapping(value = {"/downloadFile"},method = RequestMethod.GET)
    public void downloadFile(@RequestParam(value = "uuid") String uuid,
                             HttpServletResponse response,
                             Model model){
        OutputStream out = null;
        HashMap<String,String> hashMap = fileService.downloadFile(uuid);
        if (hashMap.size()!=0){
            String fileName = hashMap.get("name");
            String key = hashMap.get("key");
            String fileStr = hashMap.get("fileStr");
            String fileContent = FileEncryptionUtil.fileDecrypt(fileStr,key);
            //通过字节字符转换类把字符串转换为字节
            byte[] file = ByteCastUtil.toByteArray(fileContent);
            // 缓存区
            byte buffer[] = new byte[1024];
            int len = 0;
            try {
                //设置响应状态200
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("text/plain");
                //添加响应头 设置允许浏览器可尝试恢复中断的下载
                response.addHeader("Accept-Ranges", "bytes");
                response.addHeader("Cache-control", "private");
                //添加响应头 设置浏览器另存为对话框的默认文件名
                response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));
                out = response.getOutputStream();
                // 循环将输入流中的内容读取到缓冲区中
                out.write(file);
                out.flush();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                //关闭流
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else {
            response.setContentType("text/html; charset=UTF-8");
            try {
                response.getWriter().print("<html><body><script type='text/javascript'>alert('未获取到文件，请输入正确ID！');window.location='http://localhost:8080'</script></body></html>");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
