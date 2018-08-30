package base.app.com.gaosi.gsbaselib.request;

import android.support.annotation.NonNull;

import org.junit.Test;

import java.util.List;

import base.app.com.gaosi.gsbaselib.net.callback.GSJsonCallback;
import base.app.com.gaosi.gsbaselib.webresource_uploader.H5ResourceBean;

/**
 * Created by mrz on 18/8/29.
 */
public class RequestTest {
    @Test
    public void getH5ResourceList() throws Exception {

        Request.getH5ResourceList(new GSJsonCallback<List<H5ResourceBean>>() {
            @Override
            public void onSuccess(@NonNull List<H5ResourceBean> body) {
                super.onSuccess(body);
                body.size();
            }
        },"teacher");
    }

}