package task;

import app.FileMeta;
import dao.FileOperatorDAO;
import task.FileScanCallback;
import util.DButil;
import util.Pinyin4jUtil;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Creat with IntelliJ IDEA.
 * Description：
 * User:LiuBen
 * Date:2020-01-10
 * Time:17:22
 */
public class FileOperateTask implements FileScanCallback {
    @Override
    public void execute(File dir) {
        if(dir.isDirectory()){

            File[] children = dir.listFiles();

            List<FileMeta> localMetas = compose(children);

            List<FileMeta> metas = FileOperatorDAO.query(dir.getPath());
            for(FileMeta meta : metas){
                if(!localMetas.contains(meta)){
                    FileOperatorDAO.delete(meta);
                }
            }
            for (FileMeta localMeta : localMetas){
                if(!metas.contains(localMeta)){
                    FileOperatorDAO.insert(localMeta);
                }
            }
        }
    }

    private List<FileMeta> compose(File[] children) {
        List<FileMeta> metas = new ArrayList<>();

        if(children != null){
            for(File child : children){
                //metas.add(new FileMeta(child.getName(),child.getPath(),child.length(),child.lastModified(),child.isDirectory()));
                metas.add(new FileMeta(child));
            }
        }
        return metas;
    }
}
