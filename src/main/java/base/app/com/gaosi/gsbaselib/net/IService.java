package base.app.com.gaosi.gsbaselib.net;

/**
 * 网络请求注册的接口
 * 当前的网络请求返回值的格式不是统一的，这样就会导致我们很难用FastJSON统一解析网络请求返回结果
 *
 * Created by pingfu on 2018/4/26.
 * */
public interface IService {
    /**
     * 网络请求的URL，默认是一些URI后缀
     */
    String getUrl();

    /**
     * 网络请求返回结果对应的Java对象类型，因为返回结果不唯一，所以这个没办法统一
     * 如果后面再定义接口，我会严格要求请求结果的返回格式
     */
    Class<? extends BaseBody> getResponseClass();

    /**
     * 网络请求的类型，有get和post两种，以后也不要定义其他的类型
     */
    int getMethod();
}
