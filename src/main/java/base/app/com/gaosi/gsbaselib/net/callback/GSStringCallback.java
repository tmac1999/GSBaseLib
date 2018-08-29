package base.app.com.gaosi.gsbaselib.net.callback;

/**
 * 返回String结果的网络请求回调
 *
 * Created by pingfu on 2018/3/2.
 */
public abstract class GSStringCallback extends AbsGsCallback<String> {
    @Override
    protected String getResult() {
        return stringResponse;
    }
}
