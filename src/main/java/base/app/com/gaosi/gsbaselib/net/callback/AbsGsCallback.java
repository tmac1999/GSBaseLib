package base.app.com.gaosi.gsbaselib.net.callback;

import com.gaosi.application.Constants;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 网络请求回调
 * <p/>
 * Created by pingfu on 2018/3/2.
 */
public abstract class AbsGsCallback<E> extends AbsCallback<String> {
    //请求返回结果
    protected String stringResponse;

    protected JSONObject mJsonObjectResponse;

    //请求返回code
    public int code;

    @Override
    public void onStart(Request<String, ? extends Request> request) {
        super.onStart(request);
        //添加请求头部
        Request headers = request.headers("User-Agent", "WeAppPlusPlayground/1.0")
                .headers("appId", "" + Constants.appId)
                .headers("X-Requested-With", "X-Requested-With")
                .headers("Content-Type", "application/json;charset=UTF-8")
                .headers("appVersion", Constants.appVersion)
                .headers("systemType", Constants.systemType)
                .headers("systemVersion", "" + Constants.systemVersion)
                .headers("deviceId", Constants.deviceId)
                .headers("deviceType", Constants.deviceType)
                .headers("channel", Constants.channel);
        if (Constants.teacherInfo != null && Constants.teacherInfo.getTokenAndSessionId() != null) {
            headers.headers("token", Constants.teacherInfo.getTokenAndSessionId());
            //headers.headers("token", "1bebb9c4c2355f0330a76dbaae25313b,742,0");
        }

    }

    @Override
    public void onSuccess(Response<String> response) {
        if (response == null) {
            onResponseError(null, -1, "net work error");
        } else {
            if (code >= 20 && code < 300) {
                onResponseSuccess(response, code, getResult());
            } else {
                onResponseError(response, code, response.message());
            }
        }
    }

    @Override
    public void onError(Response<String> response) {
        super.onError(response);
        int code = -1;
        String message = "net work error";
        if (response != null) {
            code = response.code();
            if (response.message() != null)
                message = response.message();
        }
        onResponseError(response, code, message);
    }

    @Override
    public String convertResponse(okhttp3.Response response) throws Throwable {
        if (response == null) {
            stringResponse = "";
            code = -1;
        } else {
            code = response.code();
            if (response.body() != null) {
                try {
                    stringResponse = response.body().string();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        parseResult();
        return stringResponse;
    }

    protected void parseResult() {
        try {
            mJsonObjectResponse = new JSONObject(stringResponse);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected E getResult() {
        return null;
    }

    public abstract void onResponseSuccess(Response response, int code, E result);

    public abstract void onResponseError(Response response, int code, String message);
}
