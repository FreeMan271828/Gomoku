package org.freeman.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *方法类，获取数据库连接实例
 */
public class Connnect { // 创建类Connect
    /**
     * @param url 数据库地址
     * @param name 登录姓名
     * @param password 登录名称
     * @return 数据库连接实例
     */
    public static Connection getConnection(String url, String name, String password) { // 建立返回值为Connection的方法
        Connection coon = null;
        try { // 加载数据库驱动类
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("数据库驱动加载成功");
        } catch (ClassNotFoundException e) {
            e.fillInStackTrace();
        }
        try { // 通过访问数据库的URL获取数据库连接对象
            coon  = DriverManager.getConnection(url, name, password);
            System.out.println("数据库连接成功");
        } catch (SQLException e) {
            e.fillInStackTrace();
        }
        return coon; // 按方法要求返回一个Connection对象
    }

}

