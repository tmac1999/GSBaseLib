package base.app.com.gaosi.gsbaselib.statistic;

import com.aliyun.sls.android.sdk.model.LogGroup;

/**
 * 日志数据
 *
 * Created by pingfu on 2018/3/5.
 */
public class GSLogGroup extends LogGroup {
    public void clear() {
        if (mContent != null) {
            mContent.clear();
        }
    }
}
