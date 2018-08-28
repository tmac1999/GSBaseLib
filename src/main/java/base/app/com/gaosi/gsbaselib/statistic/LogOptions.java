package base.app.com.gaosi.gsbaselib.statistic;

/**
 * 日志收集策略
 * <p/>
 * Created by pingfu on 2018/3/21.
 */
public enum LogOptions {
    /** 立刻收集 */
    AT_ONCE("once", 0),

    /** 延迟收集 */
    LAZY_COLLECTED("lazy", 1),

    /** 下次启动收集 */
    NEST_START("next", 2)
    ;
    private final String mPath;
    private final int mPriority;
    LogOptions(String path, int priority) {
        mPath = path;
        mPriority = priority;
    }

    public String getPath() {
        return mPath;
    }

    public int getPriority() {
        return mPriority;
    }
}
