package base.app.com.gaosi.gsbaselib.webresource_uploader;

import android.app.Dialog;
import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.GetDataCallback;
import com.avos.avoscloud.ProgressCallback;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import base.app.com.gaosi.gsbaselib.R;

/**
 * Created by mrz on 18/8/23.
 */

public class WebResourceListDialog extends Dialog {
    private static final String TAG = "WebResourceListDialog";
    private static final int DEFAULT_BUFF_SIZE = 1024 * 4;
    private TextView tvCopyProgress;

    public WebResourceListDialog(@NonNull Context context) {
        super(context);

    }

    Context c;

    private void init(final Context c, final List<AVObject> list) {

        setContentView(R.layout.gsbaselib_dialog_web_resource_list);
        this.c = c;
        ListView lvWebResourceList = findViewById(R.id.lv_web_resource_list);
        tvCopyProgress = findViewById(R.id.tv_copy_progress);
        findViewById(R.id.btn_remove_resource).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDir(new File(WebResourceUploader.filePath));
                Toast.makeText(c, "删除成功", Toast.LENGTH_SHORT).show();
            }
        });
        lvWebResourceList.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = View.inflate(c, R.layout.gsbaselib_item_web_resource_list, null);
                TextView tv_name = view.findViewById(R.id.tv_name);
                final ProgressBar progressBar = findViewById(R.id.pb);
                TextView tv_upload_time = view.findViewById(R.id.tv_upload_time);
                final AVObject avObject = list.get(position);
                Date date = (Date) avObject.get("updatedAt");
                String updatedAt = date.toLocaleString();
                tv_upload_time.setText(updatedAt);
                final String name = avObject.get("name").toString();
                tv_name.setText(name);
                view.findViewById(R.id.btn_replace).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //下载 并替换资源
                        String url = avObject.get("url").toString();
                        AVFile file = new AVFile("aaa.zip", url, null);
                        file.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(final byte[] bytes, AVException e) {
                                new Thread() {
                                    @Override
                                    public void run() {
                                        File zipFile = new File(Environment.getExternalStorageDirectory() + "/" + name);

                                        OutputStream output = null;
                                        try {
                                            output = new FileOutputStream(zipFile);
                                            BufferedOutputStream bufferedOutput = new BufferedOutputStream(output);
                                            bufferedOutput.write(bytes);
                                            unzip(zipFile.toString(), WebResourceUploader.filePath);
                                        } catch (FileNotFoundException e1) {
                                            e1.printStackTrace();
                                        } catch (IOException e1) {
                                            e1.printStackTrace();
                                        }

                                        progressBar.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                tvCopyProgress.setText("替换完成");
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        });
                                    }
                                }.start();

                            }

                        }, new ProgressCallback() {
                            @Override
                            public void done(Integer integer) {
                                // 下载进度数据，integer 介于 0 和 100。


                                progressBar.setMax(100);
                                progressBar.setProgress(integer);
                                progressBar.setVisibility(View.VISIBLE);


                            }
                        });
                    }
                });
                return view;
            }
        });

    }

    public WebResourceListDialog(@NonNull Context context, int themeResId, List<AVObject> list) {
        super(context, themeResId);
        if (list != null && list.size() != 0)
            init(context, list);
    }

    protected WebResourceListDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public void show() {
        super.show();
        /**
         * 设置宽度全屏，要设置在show的后面
         */
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        getWindow().getDecorView().setPadding(0, 0, 0, 0);

        getWindow().setAttributes(layoutParams);
    }


    public void unzip(String zipFile, String location) throws IOException {
        try {
            File f = new File(location);
            if (!f.isDirectory()) {
                f.mkdirs();
            }
            ZipInputStream zin = new ZipInputStream(new FileInputStream(zipFile));
            try {
                ZipEntry ze = null;
                while ((ze = zin.getNextEntry()) != null) {
                    final String name = ze.getName();
                    String pathBefore = location + name;
                    String path = pathBefore.replace("prod/", "");//去掉prod路径
                    tvCopyProgress.post(new Runnable() {
                        @Override
                        public void run() {
                            if (tvCopyProgress.getVisibility() == View.GONE) {
                                tvCopyProgress.setVisibility(View.VISIBLE);
                            }
                            tvCopyProgress.setText("正在拷贝：" + name);
                        }
                    });

                    if (ze.isDirectory()) {
                        File unzipFile = new File(path);
                        if (!unzipFile.isDirectory()) {
                            unzipFile.mkdirs();
                        }
                    } else {
//                        FileOutputStream fout = new FileOutputStream(path, false);
//                        try {
//                            for (int c = zin.read(); c != -1; c = zin.read()) {
//                                fout.write(c);
//                            }
//                            zin.closeEntry();
//                        } finally {
//                            fout.close();
//                        }
                        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(path, false));
                        int len;
                        byte[] buff = new byte[DEFAULT_BUFF_SIZE];
                        while ((len = zin.read(buff, 0, DEFAULT_BUFF_SIZE)) != -1) {
                            bos.write(buff, 0, len);
                        }
                        bos.close();

                    }
                }
            } finally {
                zin.close();
            }
        } catch (Exception e) {
            Log.e(TAG, "Unzip exception", e);
        }
    }


    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     *
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     * If a deletion fails, the method stops attempting to
     * delete and returns "false".
     */
    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
//递归删除目录中的子目录下
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }
}
