package base.app.com.gaosi.gsbaselib.statistic;

import com.alibaba.fastjson.JSON;
import com.aliyun.sls.android.sdk.LogEntity;
import com.aliyun.sls.android.sdk.SLSDatabaseManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 使用文件缓存log
 * <p/>
 * Created by pingfu on 2018/2/2.
 */
class GSCollectLogUtil {
    /**
     * 每个文件最多存储10个log
     * 阿里云使用数据库缓存log，每次取数据的数量是30条，所以超过30条可以批量发送日志
     */
    static final int MAX_LOG = 10;

    //记录Log的数量，每次log存储到本地数据库中
    private static volatile int mLogNum = 0;

    private static Executor mExecutor;

    /**
     * 添加一个Log
     * 如果当前缓存中有MAX_NUM个log，批量上传log，初始化新的缓存
     * 使用文件系统作为Log缓存，是因为H5页面退出APP的回调实现是System.exist()，会直接杀死进程，不能使用内存作为缓存
     *
     * @param log 需要添加的Log
     */
    public synchronized static void addLog(GSLog log, LogOptions options) {
        if (log == null) {
            return;
        }

        if (options == LogOptions.AT_ONCE) {
            postLog(log);
        } else {
            SLSDatabaseManager.getInstance().insertRecordIntoDB(createLogEntry(log));
            mLogNum ++;
            if (mLogNum >= MAX_LOG) {
                postLogs();
            }
        }
        log.recycle();
    }

    private synchronized static void postLogs() {
        final List<LogEntity> logEntities = SLSDatabaseManager.getInstance().queryRecordFromDB();
        mLogNum = 0;

        if (mExecutor == null) {
            mExecutor = Executors.newSingleThreadExecutor();
        }

        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                postLogEntities(logEntities);
            }
        });
    }

    static void postLog(GSLog log) {
        if (log == null) {
            return;
        }

        GSLogClientFactory.buildLogClient(log.storeName).sendLog(log);
    }

    /**
     * 上传历史log，可能系统强制退出，上次启动的log没有上传完成
     */
    static void postOldLogs() {
        if (mExecutor == null) {
            mExecutor = Executors.newSingleThreadExecutor();
        }

        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                List<LogEntity> logEntities = SLSDatabaseManager.getInstance().queryRecordFromDB();
                int i = 0;
                while (logEntities != null && logEntities.size() > 0 && i < 3) {
                    postLogEntities(logEntities);
                    logEntities = SLSDatabaseManager.getInstance().queryRecordFromDB();
                    i ++;
                }
                SLSDatabaseManager.getInstance().deleteTwoThousandRecords();
            }
        });
    }

    private static void postLogEntities(List<LogEntity> logEntities) {
        if (logEntities == null || logEntities.size() == 0) {
            return;
        }
        List<GSLog> pageLogs = new ArrayList<>();
        List<GSLog> clickLogs = new ArrayList<>();
        List<GSLog> performanceLogs = new ArrayList<>();
        for (LogEntity logEntity : logEntities) {
            if (logEntity != null) {
                SLSDatabaseManager.getInstance().deleteRecordFromDB(logEntity);
                GSLog log = parseLog(logEntity);
                if (log != null && logEntity.getStore() != null) {
                    if (logEntity.getStore().contains(GSLog.PERFORMANCE_LOG_STORE)) {
                        performanceLogs.add(log);
                    } else if (logEntity.getStore().contains(GSLog.CLICK_LOG_STORE)) {
                        clickLogs.add(log);
                    } else {
                        pageLogs.add(log);
                    }
                }
            }
        }
        GSLog[] logs = new GSLog[5];

        GSLogClientFactory.buildLogClient(GSLog.PAGE_LOG_STORE).sendLog(pageLogs.toArray(logs));
        GSLogClientFactory.buildLogClient(GSLog.CLICK_LOG_STORE).sendLog(clickLogs.toArray(logs));
        GSLogClientFactory.buildLogClient(GSLog.PERFORMANCE_LOG_STORE).sendLog(performanceLogs.toArray(logs));
    }

    private static GSLog parseLog(LogEntity logEntity) {
        if (logEntity == null) {
            return null;
        }
        GSLog log = null;
        try {
            log = JSON.parseObject(logEntity.getJsonString(), GSLog.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return log;
    }

    private static LogEntity createLogEntry(GSLog log) {
        LogEntity entity = new LogEntity();
        entity.setStore(log.storeName);
        entity.setJsonString(JSON.toJSONString(log));

        return entity;
    }
}
