package base.app.com.gaosi.gsbaselib.webresource_uploader;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;

import java.util.List;

import base.app.com.gaosi.gsbaselib.R;

/**
 * Created by mrz on 18/8/23.
 */

public class WebResourceUploader {


    public static String filePath;

    /**
     * @param c
     * @param filePath
     */
    private static boolean isDebug;

    public static void init(Context c, boolean isDebug, String filePath, String appid, String AppKey) {
        WebResourceUploader.isDebug = isDebug;
        // 初始化参数依次为 this, AppId, AppKey
        if (isDebug) {
            AVOSCloud.initialize(c, appid, AppKey);
            WebResourceUploader.filePath = filePath;
        }

    }

    private static void showDialog(final Activity activity) {

        AVQuery<AVObject> avQuery = new AVQuery<>("_File");
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                new WebResourceListDialog(activity, R.style.WinDialog, list).show();
            }
        });
    }

    public static void setLongClickTriggerView(View view, final Activity activity) {
        if (isDebug) {
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showDialog(activity);
                    return false;
                }
            });
        }

    }
}
