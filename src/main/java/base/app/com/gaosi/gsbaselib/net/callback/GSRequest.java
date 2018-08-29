package base.app.com.gaosi.gsbaselib.net.callback;

import android.app.Application;
import android.text.TextUtils;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.https.HttpsUtils;
import com.lzy.okgo.model.Response;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import base.app.com.gaosi.gsbaselib.net.IService;
import okhttp3.OkHttpClient;

/**
 * 网络请求帮助类
 * Created by pingfu on 2018/2/4.
 */
public class GSRequest {
    public static final int GET = 0;
    public static final int POST = 1;
    static final int TIME_OUT_COUNT = 15000;

    public static int getHttpMethod(String methodStr) {
        int method = GET;
        if ("0".equalsIgnoreCase(methodStr) || "get".equalsIgnoreCase(methodStr)) {
            method = GET;
        } else if ("1".equalsIgnoreCase(methodStr) || "post".equalsIgnoreCase(methodStr)) {
            method = POST;
        }

        return method;
    }
    /**
     * 初始化OkGO，添加HTTPS支持
     *
     * @param application 当前用用的Application
     */
    public static void initRequest(Application application) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory();
        builder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        builder.hostnameVerifier(new SafeHostnameVerifier());
        builder.readTimeout(TIME_OUT_COUNT, TimeUnit.MILLISECONDS);
        builder.writeTimeout(TIME_OUT_COUNT, TimeUnit.MILLISECONDS);
        builder.connectTimeout(TIME_OUT_COUNT, TimeUnit.MILLISECONDS);
        if (BuildConfig.DEBUG) {
            GSHttpLoggingInterceptor loggingInterceptor = new GSHttpLoggingInterceptor("Work");
            loggingInterceptor.setPrintLevel(GSHttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);
        }
        OkGo.getInstance().init(application).setOkHttpClient(builder.build());
    }

    /**
     * 发送网络请求
     *
     * @param url      网络请求URL
     * @param params   请求参数
     * @param callback 请求参数返回值
     */
    public static void startRequest(String url, Map<String, String> params, AbsGsCallback callback) {
        startRequest(url, POST, params, callback);
    }
    /**
     * 发送网络请求
     *
     * @param url      网络请求URL
     * @param params   请求参数
     * @param callback 请求参数返回值
     */
    public static void startRequest(IService url, Map<String, String> params, GSBusinessRequest callback) {
        callback.service=url;
        callback.requestParams=params;
        startRequest(url.getUrl(), POST, params, callback);
    }

    public static void startRequest(String url, int method, Map<String, String> params, AbsGsCallback callback) {
        startRequest(url, method, params, "", callback);
    }
    public static void startRequest(String url, int method, Map<String, String> params, String tag, AbsGsCallback callback) {
        if (TextUtils.isEmpty(url)) {
            return;
        }

        if (!url.startsWith("http")) {
            url = Constants.BASEURL_BEIKE + url;
        }

        Map<String, String> requestParam = new HashMap<>();

        if (!TextUtils.isEmpty(Constants.userId)) {
            requestParam.put("userId", Constants.userId);
        }

        if (params != null) {
            requestParam.putAll(params);
        }

        if (callback == null) {
            callback = buildDefaultCallback();
        }

        if (method == GET) {
            OkGo.get(url).tag(tag).params(requestParam).execute(callback);
        } else {
            OkGo.post(url).tag(tag).params(requestParam).execute(callback);
        }
    }

    public static void startRequest(String url, int method, Map<String, String> params,boolean isUpJson ,AbsGsCallback callback) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        String tag = "";
        if (!url.startsWith("http")) {
            url = Constants.BASEURL_BEIKE + url;
        }

        Map<String, String> requestParam = new HashMap<>();

        if (!TextUtils.isEmpty(Constants.userId)) {
            requestParam.put("userId", Constants.userId);
        }

        if (params != null) {
            requestParam.putAll(params);
        }

        if (callback == null) {
            callback = buildDefaultCallback();
        }

        if (method == GET) {
            OkGo.get(url).tag(tag).params(requestParam).execute(callback);
        } else if(isUpJson){
            OkGo.post(url).tag(tag).upJson(new JSONObject(requestParam)).execute(callback);
        } else{
            OkGo.post(url).tag(tag).params(requestParam).execute(callback);
        }
    }

    public static void download(String url, String path, FileDownloadCallback callback) {
        if (callback == null) {
            callback = new FileDownloadCallback() {
                @Override
                public void onDownloadProcess(float process) {

                }

                @Override
                public void onResponseSuccess(Response response, int code, File result) {

                }

                @Override
                public void onResponseError(Response response, int code, String message) {

                }
            };
        }

        if (!TextUtils.isEmpty(path)) {
            callback.setSavePath(path);
        }
        OkGo.<File>post(url).isMultipart(false).execute(callback);
    }

    private static AbsGsCallback buildDefaultCallback() {
        return new AbsGsCallback<Object>() {
            @Override
            public void onResponseSuccess(Response response, int code, Object result) {

            }

            @Override
            public void onResponseError(Response response, int code, String message) {

            }
        };
    }

    /**
     * HTTPS认证，暂时没有认证，所有的HTTPS都能发送
     */
    private static class SafeHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }
}
