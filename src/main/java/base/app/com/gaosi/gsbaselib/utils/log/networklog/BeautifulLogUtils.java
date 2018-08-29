package base.app.com.gaosi.gsbaselib.utils.log.networklog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zhengpeng on 2016/5/18.
 */
public class BeautifulLogUtils {
    /******************************Beautiful Json Format************************************************/
    public static void json(String jsonFormat) {//fixme  1.容易乱序，整块输出时容易被其他日志插入的问题 2 无法定位到方法请求行的问题
        printLog(null, jsonFormat);
    }

    public static void json(String tag, String jsonFormat) {
        printLog(tag, jsonFormat);
    }
    /******************************Beautiful Json Format private Util************************************************/
    private static void printLog(String tagStr, Object... objects) {
        String[] contents = wrapperContent(tagStr, objects);
        String tag = contents[0];
        String msg = contents[1];
        String headString = contents[2];
        printJson(tag, msg, headString);

    }

    private static void printJson(String tag, String msg, String headString) {

        String message;

        try {
            if (msg.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(msg);
                message = jsonObject.toString(JSON_INDENT);
            } else if (msg.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(msg);
                message = jsonArray.toString(JSON_INDENT);
            } else {
                message = msg;
            }
        } catch (JSONException e) {
            message = msg;
        }

        printLine(tag, true);
        message = headString + LINE_SEPARATOR + message;
        String[] lines = message.split(LINE_SEPARATOR);
        for (String line : lines) {
            LogUtil.i( "║ " + line);
        }
        printLine(tag, false);
    }
    private static void printLine(String tag, boolean isTop) {
        if (isTop) {
            LogUtil.i("╔═══════════════════════════════════════════════════════════════════════════════════════");
        } else {
            LogUtil.i("╚═══════════════════════════════════════════════════════════════════════════════════════");
        }
    }
    public static final String DEFAULT_MESSAGE = "execute";
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private static final String NULL_TIPS = "Log with null object";
    public static final String PARAM = "Param";
    public static final String NULL = "null";
    private static final String TAG_DEFAULT = "VisualJson";
    private static final String SUFFIX = ".java";
    private static final int STACK_TRACE_MIN = 6;
    private static final int STACK_TRACE_MAX = 10;
    private static final int JSON_INDENT = 4;


    private static String[] wrapperContent(String tagStr, Object... objects) {
        StringBuilder sb = new StringBuilder();
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (int i = STACK_TRACE_MIN; i < stackTrace.length && i < STACK_TRACE_MAX; i++) {
            StackTraceElement targetElement = stackTrace[i];
            String className = targetElement.getClassName();
            String[] classNameInfo = className.split("\\.");
            if (classNameInfo.length > 0) {
                className = classNameInfo[classNameInfo.length - 1] + SUFFIX;
            }
            String methodName = targetElement.getMethodName();
            int lineNumber = targetElement.getLineNumber();

            if (lineNumber < 0) {
                lineNumber = 0;
            }
            String methodNameShort = methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
            String headString = "[ (" + className + ":" + lineNumber + ")#" + methodNameShort + " ]\n";
            sb.append(headString);
        }
        String tag = (tagStr == null ? TAG_DEFAULT : tagStr);
        String msg = (objects == null) ? NULL_TIPS : getObjectsString(objects);

        return new String[]{tag, msg, sb.toString()};
    }
    private static String getObjectsString(Object... objects) {

        if (objects.length > 1) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("\n");
            for (int i = 0; i < objects.length; i++) {
                Object object = objects[i];
                if (object == null) {
                    stringBuilder.append(PARAM).append("[").append(i).append("]").append(" = ").append(NULL).append("\n");
                } else {
                    stringBuilder.append(PARAM).append("[").append(i).append("]").append(" = ").append(object.toString()).append("\n");
                }
            }
            return stringBuilder.toString();
        } else {
            Object object = objects[0];
            return object == null ? NULL : object.toString();
        }
    }
}
