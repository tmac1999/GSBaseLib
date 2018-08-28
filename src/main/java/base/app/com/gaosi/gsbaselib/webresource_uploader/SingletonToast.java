package base.app.com.gaosi.gsbaselib.webresource_uploader;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by mrz on 18/8/27.
 */

public class SingletonToast {
    private static  SingletonToast mToastUtils;
    private static Toast mToast;

    private SingletonToast(Context context){
        if (null == mToast){
            mToast = Toast.makeText(context.getApplicationContext(),"",Toast.LENGTH_LONG);
        }
    }

    public static SingletonToast getInstance(Context context) {
        if (mToastUtils == null){
            mToastUtils = new SingletonToast(context.getApplicationContext());
        }
        return mToastUtils;
    }

    public void showShortToast(String mString){
        if (mToast == null){
            return;
        }
        mToast.setText(mString);
        mToast.setDuration(Toast.LENGTH_SHORT);
        // mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.show();
    }

    public void showLongToast(String mString){
        if (mToast == null){
            return;
        }
        mToast.setText(mString);
        mToast.setDuration(Toast.LENGTH_LONG);
        // mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.show();
    }


}
