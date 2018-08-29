package base.app.com.gaosi.gsbaselib.utils.log.networklog;

import android.util.Log;

import com.gaosi.application.Constants;


/**
 * Created by 张旭 on 2017/4/10.
 * 邮箱：zhangxu0@gaosiedu.com
 */

public class LogUtil {
    public static void v(String msg) {
        if (Constants.IsDebug) {
            Log.v("LogUtil", "LogUtil msg:" + msg);
        }
    }

    public static void d(String msg) {
        if (Constants.IsDebug) {
            Log.d("LogUtil", "LogUtil msg:" + msg);
        }
    }

    public static void i(String msg) {
        if (Constants.IsDebug) {
            Log.i("LogUtil", "LogUtil msg:" + msg);
        }
    }

    public static void w(String msg) {
        if (Constants.IsDebug) {
            Log.w("LogUtil", "LogUtil msg:" + msg);
        }
    }

    public static void e(String msg) {
        if (Constants.IsDebug) {
            Log.e("LogUtil", "LogUtil msg:" + msg);
        }
    }
}
