package base.app.com.gaosi.gsbaselib.webresource_uploader;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.avos.avoscloud.AVOSCloud;

import java.util.List;

import base.app.com.gaosi.gsbaselib.R;
import base.app.com.gaosi.gsbaselib.bean.ConstantBean;
import base.app.com.gaosi.gsbaselib.net.callback.GSJsonCallback;
import base.app.com.gaosi.gsbaselib.request.Request;

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

    /**
     * @param c        application
     * @param isDebug
     * @param filePath web资源最终保存的路径（调用h5时读取的路径 e.g. data/data/包名/files/JsBundle...）
     * @param appid    如果要使用LeadCloud ，传入的leadcloud appid  待移除
     * @param AppKey   如果要使用LeadCloud ，传入的leadcloud appkey 待移除
     *                 <p>
     *                 <p>(1)init之前需先调用{@link base.app.com.gaosi.gsbaselib.net.callback.GSRequest#initRequest(Application, ConstantBean)}
     *                 <p>(2)然后在合适的地方调用{@link WebResourceUploader#setLongClickTriggerView(View, Activity)}</p>
     */
    public static void init(Context c, boolean isDebug, String filePath, String appid, String AppKey) {
        WebResourceUploader.isDebug = isDebug;
        // 初始化参数依次为 this, AppId, AppKey
        if (isDebug) {
            AVOSCloud.initialize(c, appid, AppKey);
            WebResourceUploader.filePath = filePath;
        }

    }

    private static void showDialog(final Activity activity) {


        String appType = "teacher";
        Request.getH5ResourceList(new GSJsonCallback<List<H5ResourceBean>>() {
            @Override
            public void onSuccess(@NonNull List<H5ResourceBean> list) {
                new WebResourceListDialog(activity, R.style.WinDialog, list).show();

            }
        }, appType);
//        Request.getH5ResourceList(new GSJsonCallback<List<H5ResourceBean>>() {
//            @Override
//            public void onSuccess(@NonNull List<H5ResourceBean> list) {
//                new WebResourceListDialog(activity, R.style.WinDialog, list).show();
//
//            }
//        });
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
