package base.app.com.gaosi.gsbaselib.net;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Type;

/**
 * 网络请求返回结果基类
 * <p/>
 * Created by pingfu on 2018/2/4.
 */
public class GSHttpResponse<T> implements Serializable {
    private static final long serialVersionUID = 11111L;

    //返回结果的code
    public String errorCode;

    //返回结果的状态，这个和code有什么区别呢？
    public int status;

    //网络请求的提示
    public String errorMessage;

    //网络请求返回数据
    public T body;


    public void parseJsonArray(JSONArray jsonArray) {

    }

    public void parseJsonObject(JSONObject jsonObject) {
    }

    public void parseObject(String json, Type type) {
        if (TextUtils.isEmpty(json)) {
            try {
                body = JSON.parseObject(json, type);
            } catch (Exception e) {

            }
        }
    }

    public boolean isSuccess() {
        return status > 0;
    }
}
