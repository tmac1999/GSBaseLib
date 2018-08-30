package base.app.com.gaosi.gsbaselib.net.callback;

import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lzy.okgo.model.Response;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import base.app.com.gaosi.gsbaselib.net.GSHttpResponse;
import base.app.com.gaosi.gsbaselib.webresource_uploader.H5ResourceBean;

/**
 * Created by pingfu on 2018/3/2.
 */
public abstract class GSJsonCallback<E> extends AbsGsCallback<GSHttpResponse<E>> {
    private GSHttpResponse<E> result;

    @Override
    protected void parseResult() {
        super.parseResult();
        result = new GSHttpResponse<>();
        if (mJsonObjectResponse == null) {
            return;
        }

        try {
            result.errorCode = mJsonObjectResponse.optString("errorCode", "");
            result.errorMessage = mJsonObjectResponse.optString("errorMessage", "");
            result.status = mJsonObjectResponse.optInt("status", 0);
            if (result.status == 0) {
                onGSError(result.errorMessage);
                return;
            }
            String body = mJsonObjectResponse.optString("body");
            if (body != null) {
                Gson gson = new Gson();
                result.body = gson.fromJson(body, getBodyType());

                if (result.body == null) {
                    //result.body = getBodyInstance();
                    result.body = (E) new H5ResourceBean();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onGSError(String errorMessage) {
        Toast.makeText(GSRequest.application, errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected GSHttpResponse<E> getResult() {
        return result;
    }

    /**
     * 得到泛型的具体类型，通过FastJSON解析，获取body对象
     *
     * @return 泛型的具体类型
     */
    private Type getBodyType() {
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        if (params != null && params.length > 0) {
            return params[0];
        }

        return null;
    }

    private E getBodyInstance() {
        try {
            return (E) ((Class) ((ParameterizedType) this.getClass().
                    getGenericSuperclass()).getActualTypeArguments()[0]).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void onResponseSuccess(Response response, int code, @NonNull GSHttpResponse<E> result) {
        if (result.body == null) {
            onResponseError(response, code, "解析错误" + getBodyInstance() + "=null");
            //onResponseError(response, code, "解析错误" );
            return;
        }
        onSuccess(result.body);
    }

    public void onSuccess(@NonNull E body) {

    }

    @Override
    public void onResponseError(Response response, int code, String message) {
        Toast.makeText(GSRequest.application, message, Toast.LENGTH_SHORT).show();
    }
}
