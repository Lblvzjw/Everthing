package task;

import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Creat with IntelliJ IDEA.
 * Description：
 * User:LiuBen
 * Date:2020-01-09
 * Time:14:32
 */
public class FileScanTask {
    private final ExecutorService POOL = Executors.newFixedThreadPool(4);
    private final AtomicInteger COUNT = new AtomicInteger();
    private final CountDownLatch LATCH = new CountDownLatch(1);
    private FileScanCallback callback;

    public FileScanTask(FileScanCallback callback){
        this.callback = callback;
    }

    //启动根目录的扫描任务
    public  void startScan(File file) {
        COUNT.incrementAndGet();
        POOL.execute(new Runnable() {
            @Override
            public void run() {
                list(file);
            }
        });
    }

    //等待所有扫描任务执行完毕
    public void waitFinish() throws InterruptedException {
        try{
            LATCH.await();
        }finally {
            POOL.shutdown();
        }

    }

    public  void list(File dir){
        if(!Thread.interrupted()) {
            try {
                callback.execute(dir);
                if (dir.isDirectory()) {
                    File[] children = dir.listFiles();
                    if (children != null && children.length > 0) {
                        for (File child : children) {
                            if (child.isDirectory()) {
                                COUNT.incrementAndGet();
                                POOL.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        list(child);
                                    }
                                });
                            } else {
                                callback.execute(child);
                            }
                        }
                    }
                }
            } finally {
                if (COUNT.decrementAndGet() == 0) {
                    LATCH.countDown();
                }
            }
        }
    }

}
