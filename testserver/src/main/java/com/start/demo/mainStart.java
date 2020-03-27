package com.start.demo;

import com.start.demo.componentStart.StartDerby;
import com.start.demo.componentStart.StartJetty;
import com.start.demo.service.FileService;

/**
 * 服务端主启动类
 */
public class mainStart {

    public static void main(String[] args) {
        //启动jetty内嵌服务
        StartJetty.startJetty();
        //创建文件操作对象
        FileService fileService = new FileService(StartDerby.getConnection());
        //创建数据表
        fileService.createTable();
    }
}
