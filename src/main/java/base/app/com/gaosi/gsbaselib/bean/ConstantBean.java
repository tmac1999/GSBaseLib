package base.app.com.gaosi.gsbaselib.bean;

import android.os.Build;
import android.text.TextUtils;
import android.util.Base64;

/**
 * Created by mrz on 18/8/29.
 */

public class ConstantBean {
    public static int appId = 3;
    //备课模块重构后
    public static String BASEURL_BEIKE;

    //经度
    public static double Longitude = 0;
    //纬度
    public static double Latitude = 0;
    public static String titleName = "";


    public static int appVersionCode;

    /**
     * 当前版本信息
     */
    public static String appVersion;

    /**
     * 操作系统版本号
     */
    public final static int systemVersion = Build.VERSION.SDK_INT;

    /**
     * 操作系统类型
     */
    public final static String systemType = "android";

    /**
     * 设备ID
     */
    public final static String deviceId = initDeviceId();

    /**
     * 设备类型
     */
    public final static String deviceType = android.os.Build.MODEL;

    public static String userId;

    public static String channel = "";

    public static int screenHeight;

    public static int screenWidth;

    public static float density;

    public static float scaledDensity;
    public String tokenAndSessionId;
    public boolean IsDebug;

    private static String initDeviceId() {
        String serialId = "serial_";
        String serial = TextUtils.isEmpty(Build.SERIAL) ? "#" : Build.SERIAL;
        serialId += serial;

        String deviceId = TextUtils.isEmpty(Build.ID) ? "#" : Build.ID;
        serialId += "deviceId_" + deviceId;

        try {
            deviceId = encode(serialId);
        } catch (Exception e) {
            e.printStackTrace();
            deviceId = serialId;
        }

        return deviceId;
    }

    /**
     * @param str
     * @return
     * @throws Exception
     */
    public static String encode(String str) throws Exception {
        byte[] bytes = str.getBytes("utf-8");
        return encode(bytes);
    }

    /**
     * 二进制数据编码为BASE64字符串
     *
     * @param bytes
     * @return
     * @throws Exception
     */
    public static String encode(byte[] bytes) throws Exception {
        return new String(Base64.encode(bytes, Base64.NO_WRAP));
    }
}
