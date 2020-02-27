package task;

import util.DButil;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Creat with IntelliJ IDEA.
 * Description：
 * User:LiuBen
 * Date:2020-01-09
 * Time:10:30
 */
public class DBInit {

    public static void init(){

        try {
            //获取数据库初始化任务
            InputStream is = DButil.class.getClassLoader()
                    .getResourceAsStream("init.sql");
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(is,"UTF-8"));

            String line;
            StringBuffer sb = new StringBuffer();
            while((line = in.readLine()) != null){
                int index = line.indexOf("--");
                if(index >= 0){
                    line = line.substring(0,index);
                }
                sb.append(line);
            }
            String[] sqls = sb.toString().split(";");
            Connection connection = null;
            PreparedStatement statement = null;
            try{
                for (String sql : sqls) {
                    connection = DButil.getConnection();
                    statement = connection.prepareStatement(sql);
                    statement.executeUpdate();
                }
            } finally {
                DButil.closedb(connection,statement);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("数据库初始化任务错误");
        }

    }

   // public static void main(String[] args) {
    //    init();
   // }

}
