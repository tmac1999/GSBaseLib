package base.app.com.gaosi.gsbaselib.statistic;


import com.aliyun.sls.android.sdk.model.Log;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 重写com.aliyun.logsdk.Log，实现序列化方法
 * Created by pingfu on 2018/2/2.
 */
class GSLog extends Log implements Serializable{
    public static final String PAGE_LOG_STORE = "user-log";
    public static final String CLICK_LOG_STORE = "user-log-action";
    public static final String PERFORMANCE_LOG_STORE = "performance";

    public Map<String, Object> mContent = new HashMap<>();

    public String storeName;

    public GSLog(){
        mContent.put("__time__", Long.valueOf(System.currentTimeMillis() / 1000).intValue());
    }

    @Override
    public void PutTime(int time){
        mContent.put("__time__", time);
    }

    @Override
    public void PutContent(String key, String value) {
        if (key == null || key.isEmpty()) {
            return;
        }

        if (value == null) {
            mContent.put(key, "");
        } else {
            mContent.put(key, value);
        }
    }

    public void putContent(String key, String value) {
        if (key == null || key.isEmpty()) {
            return;
        }

        if (value == null) {
            mContent.put(key, "");
        } else {
            mContent.put(key, value);
        }
    }

    public void putContent(Map<String, String> params) {
        if (params != null && params.size() > 0) {
            mContent.putAll(params);
        }
    }

    public void recycle() {
        if (mContent != null && mContent.size() > 0) {
            mContent.clear();
        }
    }

    public Map<String,Object> getContent(){
        return mContent;
    }

    @Override
    public Map<String,Object> GetContent(){
        return mContent;
    }
}
