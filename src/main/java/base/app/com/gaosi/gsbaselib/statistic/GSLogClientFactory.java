package base.app.com.gaosi.gsbaselib.statistic;

import android.text.TextUtils;

public class GSLogClientFactory {
    private static final String LOG_PROJECT = "axx-logs";

    private static GSLogClient mPageLogClient;
    private static GSLogClient mClickLogClient;
    private static GSLogClient mPerformanceClient;

    static GSLogClient buildLogClient(String storeName) {
        if (TextUtils.isEmpty(storeName)) {
            return createPageLogClient();
        }

        GSLogClient client;
        if (storeName.contains(GSLog.PERFORMANCE_LOG_STORE)) {
            client = createPerformanceClient();
        } else if (storeName.contains(GSLog.CLICK_LOG_STORE)) {
            client = createClickClient();
        } else {
            client = createPageLogClient();
        }

        return client;
    }

    private static GSLogClient createPageLogClient() {
        if (mPageLogClient == null) {
            String storeName = GSLog.PAGE_LOG_STORE;
            if (!GSStatisticUtil.statisticInfoBean.isRelease) {
                storeName = storeName + "-test";
            }
            mPageLogClient = createGSLogClient(LOG_PROJECT, storeName);
        }

        return mPageLogClient;
    }

    private static GSLogClient createClickClient() {
        if (mClickLogClient == null) {
            String storeName = GSLog.CLICK_LOG_STORE;
            if (!GSStatisticUtil.statisticInfoBean.isRelease) {
                storeName = storeName + "-test";
            }
            mClickLogClient = createGSLogClient(LOG_PROJECT, storeName);
        }

        return mClickLogClient;
    }

    private static GSLogClient createPerformanceClient() {
        if (mPerformanceClient == null) {
            String storeName = GSLog.PERFORMANCE_LOG_STORE;
            if (!GSStatisticUtil.statisticInfoBean.isRelease) {
                storeName = storeName + "-test";
            }
            mPerformanceClient = createGSLogClient(LOG_PROJECT, storeName);
        }

        return mClickLogClient;
    }

    static String accessKeyId = "";
    static String secretKeyId = "";

    private static GSLogClient createGSLogClient(String projectName, String storeName) {
        GSLogClient client = new GSLogClient(accessKeyId, secretKeyId);
        client.setProjectName(projectName);
        client.setLogStoreName(storeName);

        return client;
    }
}
