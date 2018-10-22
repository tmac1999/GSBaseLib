package base.app.com.gaosi.gsbaselib.statistic;

import android.app.Application;
import android.os.Build;
import android.support.v4.util.Pools;
import android.text.TextUtils;
import android.util.Log;

import com.aliyun.sls.android.sdk.SLSDatabaseManager;

import java.util.HashMap;
import java.util.Map;

import base.app.com.gaosi.gsbaselib.utils.log.networklog.LogUtil;

/**
 * 埋点管理
 * Created by pingfu on 2018/2/2.
 */
public class GSStatisticUtil {

    //页面跳转临时变量，在页面
    public static String previousPageId;

    /**
     * 页面埋点
     */
    public static void statisticCollect(String currentPageId) {
        // String previousPageId = GSStatisticUtil.getPreviousPageId();
        Log.i("collectPageLog", "pad = " + currentPageId);

        if (TextUtils.equals(currentPageId, previousPageId)) {
            LogUtil.i("collectPageLog===相同页面埋点，是否埋点方法执行了两次？");
            return;
        }


        GSStatisticUtil.collectPageLog(currentPageId, previousPageId);
        previousPageId = currentPageId;
    }


    static Pools.SimplePool<GSLog> mLogPool = new Pools.SimplePool<>(GSLogCollector.MAX_LOG / 2 + 1);

    public static void initLog(Application application, StatisticInfoBean statisticInfoBean, String accessKeyId, String secretKeyId) {
        SLSDatabaseManager.getInstance().setupDB(application);
        postOldLogs();
        GSStatisticUtil.statisticInfoBean = statisticInfoBean;
        GSStatisticUtil.application = application;
        GSLogClientFactory.accessKeyId = accessKeyId;
        GSLogClientFactory.secretKeyId = secretKeyId;
    }

    static StatisticInfoBean statisticInfoBean;
    static Application application;

    /**
     * 记录日志，这段代码应该有优化的空间，单单记录一条日志，不需要传递太多参数
     *
     * @param log 日志信息
     */
    public static void collectLog(GSLog log) {
        GSLogCollector.addLog(log, LogOptions.AT_ONCE);
    }

    /**
     * 记录日志，这段代码应该有优化的空间，单单记录一条日志，不需要传递太多参数
     *
     * @param params 日志信息
     */
    public static void collectLog(Map<String, String> params) {
        if (params == null || params.size() == 0) {
            return;
        }

        GSLog log = createGSLog(GSLog.PAGE_LOG_STORE, params);
        if (log != null) {
            GSLogCollector.addLog(log, null);
        }
    }

    /**
     * 提交历史log
     */
    public static void postOldLogs() {
        GSLogCollector.postOldLogs();
    }

    public static void collectClickLog(String pad, String clickKey, String value) {
        Map<String, String> params = new HashMap<>();
        params.put("evt", "clk");
        params.put("aad", clickKey);
        params.put("introduction", value);
        params.put("pad", pad);
        collectLog(createGSLog(GSLog.PAGE_LOG_STORE, params));
    }

    private static void collectPageLog(String pad, String rPad) {
        Map<String, String> params = new HashMap<>();
        params.put("pad", pad);
        params.put("rpad", rPad);
        Log.i("collectPageLog_rpad", "pad = " + pad + "    previousPad=" + rPad);
        collectLog(createGSLog(GSLog.PAGE_LOG_STORE, params));
    }

    /**
     * 收集性能信息
     * Wiki：https://shimo.im/sheet/NHiPEqCQYUYyyo4r/OLrji/
     *
     * @param type  类型，详情见Wiki
     * @param url   url信息，详情见Wiki
     * @param value 性能的value，详情见Wiki
     */
    public static void collectPerformanceLog(String type, String url, String value) {
        Map<String, String> params = new HashMap<>();
        params.put("type", type);
        params.put("url", url);
        params.put("value", value);

        collectLog(createGSLog(GSLog.PAGE_LOG_STORE, params));
    }

    /**
     * 创建log信息
     *
     * @param storeName log存储名称
     * @param params    log数据，详见Wiki
     * @return log
     */
    private static GSLog createGSLog(String storeName, Map<String, String> params) {
        GSLog log = mLogPool.acquire();
        if (log == null) {
            log = new GSLog();
        }

        if (!TextUtils.isEmpty(statisticInfoBean.uid)) {
            log.PutContent("uid", statisticInfoBean.uid);
        } else {
            log.PutContent("uid", "");
        }
        log.PutContent("app_ver", statisticInfoBean.app_ver);
        log.PutContent("pd", "asa");

        //填入系统信息
        log.PutContent("et", "" + System.currentTimeMillis());//客户端时间，精确到毫秒
        log.PutContent("w", "" + statisticInfoBean.w);//设备分辨率-宽度
        log.PutContent("h", "" + statisticInfoBean.h);//设备分辨率-高度
        log.PutContent("model", Build.MODEL);//设备型号
        log.PutContent("brand", Build.BRAND);//手机品牌
        log.PutContent("os", Build.VERSION.RELEASE);//设备系统版本
        log.PutContent("lang", statisticInfoBean.lang);//语言
        log.PutContent("lng", String.valueOf(statisticInfoBean.lng));//经度
        log.PutContent("lat", String.valueOf(statisticInfoBean.lat));//纬度
        log.PutContent("u2", statisticInfoBean.u2);//IMEI移动为设备id，web为session_id有效期三年
        log.PutContent("net_type", statisticInfoBean.net_type);//网络类型

        log.putContent(params);
        log.storeName = storeName;

        return log;
    }


}
