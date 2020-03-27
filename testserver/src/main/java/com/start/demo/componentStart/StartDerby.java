package com.start.demo.componentStart;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *Derby嵌入式数据库启动类
 */
public class StartDerby {

    //derby驱动程序自动注册驱动类
    static String driver = "org.apache.derby.jdbc.EmbeddedDriver";
    //数据库名称
    static String dbName = "testDerby";
    //static String password = "testDerby";
    //数据库连接路径
    static String url = "jdbc:derby:" + dbName + ";create=true";


    public static Connection getConnection(){
        Connection conn = null;
        try {
            /*
             * 载入derby驱动.嵌入式驱动使用这个方式启动derby数据库引擎
             * 检查初始化动作,检查CLASSPATH路径设置问题
             */
            Class.forName(driver);
            System.out.println(driver + " loaded. ");
            //Connection conn = DriverManager.getConnection(url,dbName,password);]
            //启动嵌入式数据库
            conn = DriverManager.getConnection(url);
        } catch (java.lang.ClassNotFoundException e) {
            System.err.print("ClassNotFoundException: ");
            System.err.println(e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            return conn;
        }

    }

}
