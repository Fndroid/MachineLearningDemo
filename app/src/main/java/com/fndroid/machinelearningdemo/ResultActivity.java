package com.fndroid.machinelearningdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {
    private static final String TAG = "ResultActivity";
    private TextView mTextView;
    private String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        data = getIntent().getStringExtra("result");
        initViews();
    }

    private void initViews() {
        mTextView = (TextView) findViewById(R.id.result_tv_show);
        StringBuilder sb = new StringBuilder();
        String[] items = data.split("；");
        Log.d(TAG, "initViews: " + items.length);
        for (int i = 0; i < items.length; i++) {
            sb.append(items[i]);
            sb.append("\n");
        }
        mTextView.setText(sb.toString());
    }
}
