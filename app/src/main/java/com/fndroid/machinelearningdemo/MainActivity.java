package com.fndroid.machinelearningdemo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.hanuor.onyx.Onyx;
import com.hanuor.onyx.hub.OnTaskCompletion;
import com.hanuor.onyx.toolbox.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Random;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Button mButton;
    private ImageView mImageView;
    private TextView mTextView;
    private RequestQueue mRequestQueue;

    public static final String URL = "http://cdn-img.instyle" +
            ".com/sites/default/files/styles/622x350/public/images/2016/04/040116-emma-roberts" +
            "-lead.jpg?itok=OHMonJFa";
    public static final String TRANSLATION_URL = "http://api.fanyi.baidu" +
            ".com/api/trans/vip/translate";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mRequestQueue = Volley.newRequestQueue(this);
        initViews();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    private void initViews() {
        mButton = (Button) findViewById(R.id.main_btn_analyse);
        mImageView = (ImageView) findViewById(R.id.main_iv_pic);
        mTextView = (TextView) findViewById(R.id.main_tv_result);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Onyx.with(MainActivity.this).fromURL(URL).getTagsfromApi(new OnTaskCompletion() {
                    @Override
                    public void onComplete(ArrayList<String> response) {
                        try {
                            StringBuilder sb = new StringBuilder();
                            for (int i = 0; i < response.size(); i++) {
                                String str = response.get(i);
                                sb.append(str);
                                sb.append(";");
                            }
                            sb.deleteCharAt(sb.length() - 1);
                            translation(sb.toString());
                        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        Glide.with(this).load(URL).into(mImageView);
    }

    private void translation(String input) throws NoSuchAlgorithmException,
            UnsupportedEncodingException {
        String appid = "20160920000028985";
        String key = "qy53qCdsTFBRCGwNTsxo";
        int salt = new Random().nextInt(10000);
        String result = getMD5(input, appid, key, salt);
        appid = urlEncode(appid);
        input = urlEncode(input);
        String str_salt = urlEncode(Integer.toString(salt));
        String from = urlEncode("en");
        String to = urlEncode("zh");
        String url = TRANSLATION_URL + "?q=" + input + "&from=" + from + "&to=" + to + "&appid="
                + appid + "&salt=" + str_salt + "&sign=" + result;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                EventBus.getDefault().post(new MessageEvent(response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        mRequestQueue.add(stringRequest);
    }

    @NonNull
    private String getMD5(String input, String appid, String key, int salt) throws
            NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.reset();
        String s = appid + input + salt + key;
        messageDigest.update(s.getBytes());
        byte[] digest = messageDigest.digest();
        StringBuilder md5StrBuilder = new StringBuilder();

        //将加密后的byte数组转换为十六进制的字符串,否则的话生成的字符串会乱码
        for (int i = 0; i < digest.length; i++) {
            if (Integer.toHexString(0xFF & digest[i]).length() == 1) {
                md5StrBuilder.append("0").append(
                        Integer.toHexString(0xFF & digest[i]));
            } else {
                md5StrBuilder.append(Integer.toHexString(0xFF & digest[i]));
            }
        }
        return md5StrBuilder.toString();
    }

    private String urlEncode(String s) throws UnsupportedEncodingException {
        return URLEncoder.encode(s, "UTF-8");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        Gson gson = new Gson();
        Result result = gson.fromJson(event.msg, Result.class);
        String dst = result.getTrans_result().get(0).getDst();
        mTextView.setText(dst);
    }
}
