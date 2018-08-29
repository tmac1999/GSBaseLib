package base.app.com.gaosi.gsbaselib.net.callback;

import android.os.Environment;
import android.text.TextUtils;

import com.gaosi.application.Constants;
import com.gaosi.util.LogUtil;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.convert.FileConvert;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import java.io.File;

import static com.lzy.okgo.convert.FileConvert.DM_TARGET_FOLDER;

/**
 *
 * Created by pingfu on 2018/3/7.
 */
public abstract class FileDownloadCallback extends FileCallback {
    private File mFile;
    private String mSavePath;
    private FileConvert convert;    //文件转换类

    public void setSavePath(String path) {
        mSavePath = path;
    }

    public FileDownloadCallback() {
        this(null, null);
    }

    public FileDownloadCallback(String destFileDir, String destFileName) {

    }

    @Override
    public void onStart(Request<File, ? extends Request> request) {
        super.onStart(request);
        //添加请求头部
        request.headers("User-Agent", "WeAppPlusPlayground/1.0")
                .headers("appId", "" + Constants.appId)
                .headers("X-Requested-With","X-Requested-With")
                .headers("Content-Type", "application/json;charset=UTF-8");
    }

    @Override
    public File convertResponse(okhttp3.Response response) throws Throwable {
        if (convert == null) {
            if (TextUtils.isEmpty(mSavePath)) {
                mSavePath = "temp";
            }
            File file = new File(Environment.getExternalStorageDirectory() + DM_TARGET_FOLDER + File.separator + mSavePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            convert = new FileConvert(mSavePath);
            convert.setCallback(this);
        }

        File file = convert.convertResponse(response);
        response.close();
        return file;
    }

    @Override
    public void downloadProgress(Progress progress) {
        LogUtil.e("download progress = " + progress.currentSize + ", totalSize" + progress.totalSize);
        onDownloadProcess((float) progress.currentSize / (float) progress.totalSize);
    }

    @Override
    public void onSuccess(Response<File> response) {
        mFile = response.body();
        onResponseSuccess(response, response.code(), mFile);
    }

    @Override
    public void onError(Response<File> response) {
        super.onError(response);
        onResponseError(response, response.code(), response.message());
    }

    public String getSavePath() {
        return mSavePath;
    }

    public abstract void onDownloadProcess(float process);

    public abstract void onResponseSuccess(Response<File> response, int code, File file);

    public abstract void onResponseError(Response<File> response, int code, String message);
}
