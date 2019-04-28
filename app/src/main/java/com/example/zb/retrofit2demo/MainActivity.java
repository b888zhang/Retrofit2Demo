package com.example.zb.retrofit2demo;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import com.example.zb.retrofit2demo.bean.BaseResult;
import com.example.zb.retrofit2demo.bean.WeatherResult;
import com.example.zb.retrofit2demo.bean.WeatherResult2;
import com.example.zb.retrofit2demo.retrofit.RetrofitRequest;
import com.example.zb.retrofit2demo.utils.Constant;
import com.example.zb.retrofit2demo.utils.MyLog;
import com.example.zb.retrofit2demo.utils.StorageUtil;
import com.google.gson.Gson;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
public class MainActivity extends AppCompatActivity {
    private static final int DOWNLOAD_ING    = 1;// 下载中
    private static final int DOWNLOAD_FINISH = 2;// 下载结束
    private static final int DOWNLOAD_ERROR  = -1;// 下载出错
    @BindView(R.id.bt_get)
    Button mBtGet;
    @BindView(R.id.bt_post)
    Button mBtPost;
    @BindView(R.id.bt_top)
    Button mBtTop;
    @BindView(R.id.bt_xia)
    Button mBtXia;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case DOWNLOAD_ING:
                    // 正在下载
                    showProgress();
                    break;
                case DOWNLOAD_FINISH:
                    // 下载完成
                    onDownloadFinish();
                    break;
                case DOWNLOAD_ERROR:
                    // 出错
                    onDownloadError();
                    break;

            }
        }
    };

    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

         progressDialog = new ProgressDialog(this);
         progressDialog.setCancelable(false);
        progressDialog.setTitle("正在加载中。。。。");


    }
    /**
     * 显示下载进度
     */
    private void showProgress() {
        String text = progress + "%  |  " + downByte + "Kb / " + totalByte + "Kb";
        MyLog.i("下载进度----------", text);


    }
    /**
     * 下载出错处理
     */
    private void onDownloadError() {
        MyLog.i("下载进度----------", "出错了。。。。。。。。。");
    }

    /**
     * 下载完成
     */
    private void onDownloadFinish() {
        MyLog.i("下载进度----------", "下载完成了。。。。。。。");
    }


    @OnClick({R.id.bt_get, R.id.bt_post, R.id.bt_top, R.id.bt_xia})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_get:
                //get
                onGet();
                break;
            case R.id.bt_post:
                //Post
                onPost();
                break;
            case R.id.bt_top:
                //上传
                onFileUpload();
                break;
            case R.id.bt_xia:
                //下载
                onFileDownload();

                break;
        }
    }
    private void onGet() {
        // 这里放显示loading
        String url = Constant.URL_WEATHER;
        url += "?city=北京";
        RetrofitRequest.sendGetRequest(url, WeatherResult2.class, new RetrofitRequest.ResultHandler<WeatherResult2>(this) {
            @Override
            public void onBeforeResult() {
                // 这里可以放关闭loading
            }
            @Override
            public void onResult(WeatherResult2 weatherResult2) {
                String s = new Gson().toJson(weatherResult2);
                MyLog.i("get------", s);
            }

            @Override
            public void onAfterFailure() {
                // 这里可以放关闭loading
            }
        });
    }


    private void onPost() {
        // 这里放显示loading
        progressDialog.show();
        String url = Constant.URL_BASE1;
        Map<String, String> paramMap = new HashMap<>(1);
        paramMap.put("code", "admin");
        paramMap.put("password", "123456");
        RetrofitRequest.sendPostRequest(url, paramMap, WeatherResult.class, new RetrofitRequest.ResultHandler<WeatherResult>(this) {
            @Override
            public void onBeforeResult() {
                // 这里可以放关闭loading
                progressDialog.dismiss();
            }
            @Override
            public void onResult(WeatherResult weatherResult) {
                progressDialog.dismiss();
                String weather = new Gson().toJson(weatherResult);
                MyLog.i("retrofit------", weather);
                MyLog.i("errMsg------", weatherResult.getErrMsg());
                MyLog.i("Name------", weatherResult.getResult().getName());
                MyLog.i("password------", weatherResult.getResult().getPassword());
            }

            @Override
            public void onAfterFailure() {
                // 这里可以放关闭loading
                progressDialog.dismiss();
            }
        });

    }

    /**
     * 文件下载
     * <p>用百度手机助手APK文件作为下载示例</p>
     */
    private void onFileDownload() {
        MyLog.i("文件下载------", "下载中。。。。。。。。。。。。。");
        RetrofitRequest.fileDownload(Constant.URL_DOWNLOAD, new RetrofitRequest.DownloadHandler() {

            @Override
            public void onBody(ResponseBody body) {
                if (!writeResponseBodyToDisk(body)) {
                    mHandler.sendEmptyMessage(DOWNLOAD_ERROR);
                }

            }

            @Override
            public void onError() {
                mHandler.sendEmptyMessage(DOWNLOAD_ERROR);
            }
        });


    }

    private void onFileUpload() {
        MyLog.i("上传文件------", "上传中。。。。。。。。。。。。。");
        File file = null;
        try {
            // 通过新建文件替代文件寻址
            file = File.createTempFile("abc", "txt");
        } catch (IOException e) {
        }
        String url = Constant.URL_LOGIN;
        RetrofitRequest.fileUpload(url, file, BaseResult.class, new RetrofitRequest.ResultHandler<BaseResult>(this) {
            @Override
            public void onBeforeResult() {
                // 这里可以放关闭loading
            }

            @Override
            public void onResult(BaseResult baseResult) {
                MyLog.i("上传文件------", "上传成功");
            }

            @Override
            public void onAfterFailure() {
                // 这里可以放关闭loading
            }
        });
    }


    private String savePath;// 下载保存路径
    private int    progress;// 记录进度条数量
    private int    totalByte; // 总大小
    private int    downByte; // 已下载
    private boolean cancelUpdate = false;// 是否取消更新
    private String  fileName     = "news.apk"; // 下载存储的文件名

    /**
     * 写文件入磁盘
     *
     * @param body 请求结果
     * @return boolean 是否下载写入成功
     */
    private boolean writeResponseBodyToDisk(ResponseBody body) {
        savePath = StorageUtil.getDownloadPath();
        File apkFile = new File(savePath, fileName);
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            byte[] fileReader = new byte[4096];
            // 获取文件大小
            long fileSize = body.contentLength();
            long fileSizeDownloaded = 0;
            inputStream = body.byteStream();
            outputStream = new FileOutputStream(apkFile);
            // byte转Kbyte
            BigDecimal bd1024 = new BigDecimal(1024);
            totalByte = new BigDecimal(fileSize).divide(bd1024, BigDecimal.ROUND_HALF_UP).setScale(0).intValue();
            // 只要没有取消就一直下载数据
            while (!cancelUpdate) {
                int read = inputStream.read(fileReader);
                if (read == -1) {
                    // 下载完成
                    mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
                    break;
                }
                outputStream.write(fileReader, 0, read);
                fileSizeDownloaded += read;
                // 计算进度
                progress = (int) (((float) (fileSizeDownloaded * 100.0 / fileSize)));
                downByte = new BigDecimal(fileSizeDownloaded).divide(bd1024, BigDecimal.ROUND_HALF_UP).setScale(0).intValue();
                // 子线程中，借助handler更新界面
                mHandler.sendEmptyMessage(DOWNLOAD_ING);
            }
            outputStream.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
