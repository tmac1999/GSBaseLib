package base.app.com.gaosi.gsbaselib.net.callback;

/**
 * WebView中网络请求模块
 *
 * Created by pingfu on 2018/3/2.
 */
public abstract class GSWebViewCallback extends AbsGsCallback<String> {
    @Override
    protected String getResult() {
        return stringResponse;
    }
}
