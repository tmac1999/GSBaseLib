package base.app.com.gaosi.gsbaselib.statistic;

/**
 * Created by mrz on 18/8/23.
 */

public class StatisticInfoBean {
    public String uid;
    public String app_ver;//appVersion
    public int w;////设备分辨率-宽度
    public int h;//设备分辨率-高度
    public String lang;//语言
    public double lng;//经度
    public double lat;//纬度
    public String u2;//IMEI移动为设备id，web为session_id有效期三年
    public String net_type;//网络类型


    public boolean isRelease;//控制storeName 是否要加-test?
}
