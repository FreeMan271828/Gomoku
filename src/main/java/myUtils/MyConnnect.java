package myUtils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *方法类，获取数据库连接实例
 */
public class MyConnnect { // 创建类Connect

    private static final MyLog LOG = MyLog.getInstance();
    /**
     * @return 数据库连接实例
     */
    public static Connection getConnection() {

        String jsonString = MyJson.get("DbConfig.json");
        if (jsonString == null) { LOG.error("未找到此文件"); return null; }

        JSONObject jsonObject = JSON.parseObject(jsonString);
        JSONObject dbConnection = jsonObject.getJSONObject("DbConnection1");
        String url=dbConnection.getString("url");
        String name=dbConnection.getString("name");
        String password=dbConnection.getString("password");
        if(url.isEmpty()||name.isEmpty()||password.isEmpty()) {LOG.error("未找到全部字段"); return null;}

        // 建立返回值为Connection的方法
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

