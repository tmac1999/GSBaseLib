package base.app.com.gaosi.gsbaselib.net.callback;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.lzy.okgo.model.Response;

import java.util.Map;

import base.app.com.gaosi.gsbaselib.net.GSHttpResponse;
import base.app.com.gaosi.gsbaselib.net.IService;

/**
 * 网络请求的回调
 * 为了简单好用，这个回调只能适合如下返回结果的回调：
 * {
 * "errorCode":"",
 * "message":"",
 * "status":1,
 * "data":{}
 * }
 * 由于历史原因，当前项目中网络请求返回结果并不统一，对于那些格式千奇百怪的返回结果，用GSJsonCallback处理
 * Created by pingfu on 2018/4/26.
 */
public abstract class GSBusinessRequest extends AbsGsCallback<String> {
    public IService service;

    /**
     * 请求序列号，用来区分同一个URL的不同Http请求
     */
    public int requestCode;

    /**
     * 请求返回值通过FastJson解析后的Object
     */
    public GSHttpResponse result;

    /**
     * 请求返回值
     */
    public Response httpResponse;

    /**
     * 请求参数
     */
    public Map<String, String> requestParams;
    private GSBusinessRequest mRequest;

    public GSBusinessRequest() {
        mRequest = this;
        result = new GSHttpResponse();
    }

    @Override
    public void onResponseSuccess(Response response, int code, String result) {
        httpResponse = response;
        onSuccess(mRequest, service, code, this.result);
    }

    @Override
    public void onResponseError(Response response, int code, String message) {
        httpResponse = response;
        onError(mRequest, service, code, message);
    }

    @Override
    protected void parseResult() {
        super.parseResult();
        if (mJsonObjectResponse == null) {
            result.status = code;
            result.errorCode = "G_10001";
            result.body = null;
            return;
        }

        result.errorCode = mJsonObjectResponse.optString("errorCode", "");
        result.errorMessage = mJsonObjectResponse.optString("errorMessage", "");
        result.status = mJsonObjectResponse.optInt("status");
        String dataStr = mJsonObjectResponse.optString("body");
        if (!TextUtils.isEmpty(dataStr)) {
            try {
                result.body = JSON.parseObject(dataStr, service.getResponseClass());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (result.body == null) {
            try {
                result.body = service.getResponseClass().newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

//            if (result.body != null && result.body instanceof BaseBody) {
//                result.body.parseJson(dataStr);
//            }
        }
    }

    @Override
    protected String getResult() {
        return stringResponse;
    }

    public abstract void onSuccess(GSBusinessRequest request, IService service, int code, @NonNull GSHttpResponse data);

    public abstract void onError(GSBusinessRequest request, IService service, int code, String message);
}
