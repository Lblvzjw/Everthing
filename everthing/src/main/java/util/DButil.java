package util;

import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;

import javax.sql.DataSource;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Creat with IntelliJ IDEA.
 * Description：
 * User:LiuBen
 * Date:2020-01-09
 * Time:9:12
 */
public class DButil {

    //初始化数据库
    private static volatile DataSource data_Source;

    //获取SQLite数据库本地文件路径(target目录下)
    private static String getUrl() throws URISyntaxException {
        String dbName = "xiaotianquan.db";
        URL url = DButil.class.getClassLoader().getResource(".");
        assert url != null;
        return "jdbc:sqlite://" + new File(url.toURI()).getParent() + File.separator + dbName;
    }

    //获取数据库连接池
    private static DataSource getData_Source() throws URISyntaxException {
        if(data_Source == null){
            synchronized (DButil.class){
                if(data_Source == null){
                    SQLiteConfig config = new SQLiteConfig();
                    config.setDateStringFormat(Util.DATE_PATTERN);
                    data_Source = new SQLiteDataSource(config);
                    ((SQLiteDataSource)data_Source).setUrl(getUrl());
                }
            }
        }
        return data_Source;
    }

    //获取数据库连接
    public static Connection getConnection(){
        try {
            return  getData_Source().getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("获取数据库连接失败");
        }
    }

    //释放数据库资源
    public static void closedb(Connection connection, Statement statement, ResultSet resultSet){
        try {
            if(resultSet != null){
                resultSet.close();
            }

            if(statement != null){
                statement.close();
            }

            if(connection != null){
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("释放数据库资源错误");
        }
    }

    public static void closedb(Connection connection, Statement statement){
        closedb(connection,statement,null);
    }



}
