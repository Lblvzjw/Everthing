package dao;

import app.FileMeta;
import org.sqlite.core.DB;
import util.DButil;
import util.Pinyin4jUtil;

import java.awt.image.DataBufferUShort;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Creat with IntelliJ IDEA.
 * Description：
 * User:LiuBen
 * Date:2020-01-12
 * Time:9:20
 */
public class FileOperatorDAO {

    public static List<FileMeta> query(String dirPath) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        List<FileMeta> metas = new ArrayList<>();

        try{
            connection = DButil.getConnection();
            String sql = "select name,path,size,last_modified,is_directory"+
                    " from file_meta where path=?";
            statement = connection.prepareStatement(sql);
            statement.setString(1,dirPath);
            resultSet = statement.executeQuery();
            System.out.println("query");
            while (resultSet.next()){
                String name = resultSet.getString("name");
                String path = resultSet.getString("path");
                Long size = resultSet.getLong("size");
                Long last_modified = resultSet.getLong("last_modified");
                boolean is_directory = resultSet.getBoolean("is_directory");
                FileMeta meta = new FileMeta(name,path,size,last_modified,is_directory);
                metas.add(meta);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            DButil.closedb(connection,statement,resultSet);
        }
        return metas;
    }


    public static void insert(FileMeta localMeta) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DButil.getConnection();
            String sql = "insert into file_meta" +
                    "(name,path,size,last_modified," +
                    "pinyin,pinyin_first,is_directory)" +
                    "values (?,?,?,?,?,?,?)";
            statement = connection.prepareStatement(sql);
            statement.setString(1,localMeta.getName());
            statement.setString(2,localMeta.getPath());
            statement.setLong(3,localMeta.getSize());
            statement.setTimestamp(4,new Timestamp(localMeta.getLastModified()));
            statement.setBoolean(7,localMeta.getDirectory());
            String pinyin = null;
            String pinyin_first = null;
            if(Pinyin4jUtil.containsChinese(localMeta.getName())){
                String[] pinyins = Pinyin4jUtil.getpinyin(localMeta.getName());
                pinyin = pinyins[0];
                pinyin_first= pinyins[1];
            }
            statement.setString(5,pinyin);
            statement.setString(6,pinyin_first);
            statement.executeUpdate();
            System.out.println("insert:" + localMeta.getName() + localMeta.getPath() + localMeta.getSize());
            }catch (SQLException e) {
                e.printStackTrace();
            }finally {
                DButil.closedb(connection,statement);
        }
    }

    public static void delete(FileMeta meta) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DButil.getConnection();
            connection.setAutoCommit(false);
            String sql = "delete from file_meta where name = ? " +
                    "and path = ? and is_directory=?";
            statement = connection.prepareStatement(sql);
            statement.setString(1,meta.getName());
            statement.setString(2,meta.getPath());
            statement.setBoolean(3,meta.getDirectory());
            statement.executeUpdate();

            if(meta.getDirectory()){
                sql = "delete from file_meta where path like ? or path = ?";
                statement = connection.prepareStatement(sql);
                String path = meta.getPath() + File.separator + meta.getName();
                statement.setString(1,path + File.separator + "%");
                statement.setString(2,path);
                statement.executeUpdate();
                System.out.println("dele");
            }
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            if(connection != null){
                try {
                    connection.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }finally {
            DButil.closedb(connection,statement);
        }
    }

//    public static void main(String[] args) {
        //        FileMeta fileMeta = new FileMeta("jobspider","D:\\shaziben",24431L,1562118328713L,true);
        //        delete(fileMeta);
//   }


    public static List<FileMeta> search(String srcpath,String text){
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        List<FileMeta> metas = new ArrayList<>();

        try{
            connection = DButil.getConnection();
            boolean pathIsEmpty = (srcpath == null || srcpath.trim().length() == 0);
            String sql = "select name,path,size,last_modified,is_directory"+
                    " from file_meta where (name like ? or pinyin like ? or pinyin_first like ?)"+
                    (pathIsEmpty ? "" : "and(path = ? or path like ?)");
            statement = connection.prepareStatement(sql);
            statement.setString(1,"%" + text + "%");
            statement.setString(2,"%" + text + "%");
            statement.setString(3,"%" + text + "%");
            if(!pathIsEmpty){
                statement.setString(4,srcpath);
                statement.setString(5,srcpath + File.separator + "%");
            }

            resultSet = statement.executeQuery();
            while (resultSet.next()){
                String name = resultSet.getString("name");
                String path = resultSet.getString("path");
                Long size = resultSet.getLong("size");
                Long last_modified = resultSet.getLong("last_modified");
                boolean is_directory = resultSet.getBoolean("is_directory");
                FileMeta meta = new FileMeta(name,path,size,last_modified,is_directory);
                metas.add(meta);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            DButil.closedb(connection,statement,resultSet);
        }
        return metas;
    }
}
