package com.android.asynctaskpractice;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Button btnTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnTask = (Button) findViewById(R.id.btnTask);
        btnTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MyAsyncTask(MainActivity.this).execute();
            }
        });

    }


    static final class MyAsyncTask extends AsyncTask<Void, Integer, Boolean> {

        private static final String TAG = "myAsyncTask";

        //如上三个泛型参数从左到右含义依次为：
        //1. 在执行AsyncTask时需要传入的参数，可用于在后台任务中使用。
        //2. 后台任务执行时，如果需要在界面上显示当前的进度，则使用这个。
        //3. 当任务执行完毕后，如果需要对结果进行返回，则使用这个。

        private Context mContext = null;
        private ProgressDialog mDialog = null;
        private int mCount = 0;


        public MyAsyncTask(Context context) {
            mContext = context;
        }

        /**
         * 初始化操作
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new ProgressDialog(mContext);
            mDialog.setMax(100);
            mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mDialog.show();
        }


        /***
         * 任务执行完成后 做一些资源回收操作
         *
         * @param aBoolean
         */
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean && mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
                mDialog = null;
            }
        }

        //当在后台任务中调用了publishProgress(Progress...)方法后，这个方法就很快会被调用 更新数据
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            mDialog.setProgress(values[0]);
        }

        // 在后台执行任务
        @Override
        protected Boolean doInBackground(Void... params) {
            while (mCount < 100) {
                publishProgress(mCount);
                mCount += 10;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return true;
        }
    }
}
