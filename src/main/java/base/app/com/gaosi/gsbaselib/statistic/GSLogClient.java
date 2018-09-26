package base.app.com.gaosi.gsbaselib.statistic;

import com.aliyun.sls.android.sdk.ClientConfiguration;
import com.aliyun.sls.android.sdk.LOGClient;
import com.aliyun.sls.android.sdk.LogException;
import com.aliyun.sls.android.sdk.core.auth.StsTokenCredentialProvider;
import com.aliyun.sls.android.sdk.core.callback.CompletedCallback;
import com.aliyun.sls.android.sdk.request.PostLogRequest;
import com.aliyun.sls.android.sdk.result.PostLogResult;


/**
 * 阿里云客户端
 */
public class GSLogClient {

    private static final String endPoint = "cn-beijing.log.aliyuncs.com";

    private LOGClient mClient;
    private GSLogGroup mLogGroup = new GSLogGroup();
    private String projectName;
    private String logStoreName;

    public GSLogClient(String accessKeyId,String secretKeyId) {
        // 配置信息
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000);     // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5);      // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2);             // 失败后最大重试次数，默认2次

        conf.setCachable(true);               // 设置日志发送失败时，是否支持本地缓存。

        // 设置缓存日志发送的网络策略。
        conf.setConnectType(ClientConfiguration.NetworkPolicy.WWAN_OR_WIFI);

        StsTokenCredentialProvider provider = new StsTokenCredentialProvider(accessKeyId, secretKeyId, "");
        mClient = new LOGClient(GSStatisticUtil.application, endPoint, provider, conf);
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setLogStoreName(String logStoreName) {
        this.logStoreName = logStoreName;
    }

    public void sendLog(GSLog... logs) {
        if (logs == null || logs.length == 0) {
            return;
        }

        mLogGroup.clear();
        for (GSLog log : logs) {
            mLogGroup.PutLog(log);
        }

        PostLogRequest request = new PostLogRequest(projectName, logStoreName, mLogGroup);
        try {
            mClient.asyncPostLog(request, new CompletedCallback<PostLogRequest, PostLogResult>() {
                @Override
                public void onSuccess(PostLogRequest postLogRequest, PostLogResult postLogResult) {

                }

                @Override
                public void onFailure(PostLogRequest postLogRequest, LogException e) {

                }
            });
        } catch (LogException e) {
            e.printStackTrace();
        }
    }
}
