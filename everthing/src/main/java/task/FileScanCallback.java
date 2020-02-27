package task;

import java.io.File;

/**
 * Creat with IntelliJ IDEA.
 * Description：文件扫描任务的回调方法
 * User:LiuBen
 * Date:2020-01-10
 * Time:17:10
 */
public interface FileScanCallback {
    void execute(File dir);
}
