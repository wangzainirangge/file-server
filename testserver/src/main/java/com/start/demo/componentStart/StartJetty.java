package com.start.demo.componentStart;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.webapp.WebAppContext;

/**
 * Jetty嵌入式Web服务启动类
 */
public class StartJetty {


    public static void startJetty(){
        // 服务器的监听端口
        Server server = new Server(9999);
        // 关联一个已经存在的上下文
        WebAppContext context = new WebAppContext();
        // 设置描述符位置
        context.setDescriptor("./src/main/webapp/WEB-INF/web.xml");
        // 设置Web内容上下文路径
        context.setResourceBase("./src/main/webapp");
        // 设置上下文路径
        context.setContextPath("/");
        context.setParentLoaderPriority(true);
        server.setHandler(context);

        try {
            server.start();
            // server.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.err.println("请访问http://localhost:9999");
        System.err.println("server is start");
    }


}
