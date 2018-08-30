package base.app.com.gaosi.gsbaselib.request;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetDataCallback;
import com.avos.avoscloud.ProgressCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import base.app.com.gaosi.gsbaselib.net.callback.FileDownloadCallback;
import base.app.com.gaosi.gsbaselib.net.callback.GSJsonCallback;
import base.app.com.gaosi.gsbaselib.net.callback.GSRequest;
import base.app.com.gaosi.gsbaselib.webresource_uploader.H5ResourceBean;

/**
 * Created by mrz on 18/8/29.
 */

public class Request {
    public static String GET_H5_RESOURCE_LIST = "http://10.39.2.203:8080/mock/getFiles";

    public static void getH5ResourceList(GSJsonCallback<List<H5ResourceBean>> callback, String appType) {
        Map<String, String> param = new HashMap<>();
        param.put("group", appType);

        GSRequest.startRequest(GET_H5_RESOURCE_LIST,
                param, callback);
    }

    public static void getH5ResourceList(final GSJsonCallback<List<H5ResourceBean>> callback) {
        AVQuery<AVObject> avQuery = new AVQuery<>("_File");
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {

                if (list != null && list.size() != 0) {
                    ArrayList<H5ResourceBean> h5ResourceBeans = new ArrayList<>();
                    for (AVObject avObject : list) {
                        H5ResourceBean h5ResourceBean = new H5ResourceBean();
                        h5ResourceBean.name = avObject.get("name").toString();
                        Date updatedAt = (Date) avObject.get("updatedAt");
                        h5ResourceBean.updatedAt = updatedAt.toLocaleString();
                        h5ResourceBean.url = avObject.get("url").toString();

                        h5ResourceBeans.add(h5ResourceBean);
                    }
                    callback.onSuccess(h5ResourceBeans);
                }


            }
        });
    }

    public static void getH5ResourceZipFile(String url, final GetDataCallback getDataCallback,final ProgressCallback progressCallback) {
        AVFile file = new AVFile("aaa.zip", url, null);
        file.getDataInBackground(getDataCallback,progressCallback );
    }

    public static void getH5ResourceZipFile(String url,String path,FileDownloadCallback fileDownloadCallback) {
        GSRequest.download(url, path, fileDownloadCallback);
    }
}
