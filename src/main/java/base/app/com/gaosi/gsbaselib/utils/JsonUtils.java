package base.app.com.gaosi.gsbaselib.utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * <li>description: json相关的工具类
 * <li>author: zhengpeng
 * <li>date: 18/5/7 上午11:37
*/
public class JsonUtils {
    /**
     * <li>description: 两个json object 合并  浅合并
     * <li>author: zhengpeng
     * <li>date: 18/5/7 上午11:38
    */
    public static JSONObject jsonMerge(JSONObject o1, JSONObject o2) throws JSONException {
        if (o1 == null) {
            return o2;
        } else if (o2 == null) {
            return o1;
        } else {
            Iterator i1 = o1.keys();
            Iterator i2 = o2.keys();
            JSONObject mergedObj = new JSONObject();
            String tmp_key;
            while (i1.hasNext()) {
                tmp_key = (String) i1.next();
                mergedObj.put(tmp_key, o1.get(tmp_key));
            }
            while (i2.hasNext()) {
                tmp_key = (String) i2.next();
                mergedObj.put(tmp_key, o2.get(tmp_key));
            }
            return mergedObj;
        }
    }

    //fixme 深合并  此方法有问题 to be fixed
    public static JSONObject deepMerge(JSONObject source, JSONObject target) throws JSONException {

        while (source.keys().hasNext()) {
            String key = source.keys().next();
            Object value = source.get(key);
            if (!target.has(key)) {
                // new value for "key":
                target.put(key, value);
            } else {
                // existing value for "key" - recursively deep merge:
                if (value instanceof JSONObject) {
                    JSONObject valueJson = (JSONObject) value;
                    deepMerge(valueJson, target.getJSONObject(key));
                } else {
                    target.put(key, value);
                }
            }
        }
        return target;
    }
}
