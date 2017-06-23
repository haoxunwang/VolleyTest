package com.nelson.volleytest;

import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final String REQUEST_TAG = "request_tag";

    private static final String GET_URL = "https://api.github.com/users/";
    private static final String POST_URL = "http://www.baidu.com";
    private static final String NORMAL_IMAGE_URL = "https://image1.guazistatic.com/qn1706021141581b9526b6f78283b3bcc8aafd14fade1d.jpg";
    private static final String VOLLEY_IMAGE_URL = "https://image1.guazistatic.com/qn170602115500fe8a90dabfba5f08b88adcbe7e899b12.jpg";

    private Button mBtnGet;
    private Button mBtnPost;
    private Button mBtnGetImg;
    private Button mBtnCancel;
    private ImageView mIvImg;
    private TextView mTvTxt;
    private NetworkImageView mNetImg;
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mIvImg = getViewById(R.id.iv_img);
        mTvTxt = getViewById(R.id.tv_txt);
        mNetImg = getViewById(R.id.net_img);

        mBtnGet = getViewById(R.id.btn_get);
        mBtnPost = getViewById(R.id.btn_post);
        mBtnGetImg = getViewById(R.id.btn_get_img);
        mBtnCancel = getViewById(R.id.btn_cancel);

        mBtnGet.setOnClickListener(mOnclickListener);
        mBtnPost.setOnClickListener(mOnclickListener);
        mBtnGetImg.setOnClickListener(mOnclickListener);
        mBtnCancel.setOnClickListener(mOnclickListener);

        initVolley();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRequestQueue.cancelAll(REQUEST_TAG);
    }

    private void initVolley() {
        mRequestQueue = Volley.newRequestQueue(this);
    }


    private View.OnClickListener mOnclickListener = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onClick(View view) {

            mTvTxt.setText("");
            mIvImg.setImageResource(R.mipmap.ic_launcher);

            if (Objects.equals(mBtnGet, view)) {

                StringBuilder builder = new StringBuilder();
                try {
                    builder.append(GET_URL).append(URLEncoder.encode("haoxunwang", "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                // Normal Get Request
                StringRequest getRequest = new StringRequest(builder.toString(), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mTvTxt.setText(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mTvTxt.setText(error.getMessage());
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("Content-Type", "application/json");
                        return headers;
                    }
                };

                getRequest.setTag(REQUEST_TAG);
                mRequestQueue.add(getRequest);
                mRequestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<String>() {
                    @Override
                    public void onRequestFinished(Request<String> request) {
                        Log.e("test", request.getUrl());
                    }
                });

            } else if (Objects.equals(mBtnPost, view)) {

                // Normal Post Request
                StringRequest postRequest = new StringRequest(Request.Method.POST, POST_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mTvTxt.setText(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mTvTxt.setText(error.getMessage());
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<>();
                        map.put("params1", "value1");
                        map.put("params2", "value2");
                        return map;
                    }
                };

                postRequest.setTag(REQUEST_TAG);
                mRequestQueue.add(postRequest);
                mRequestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Bitmap>() {
                    @Override
                    public void onRequestFinished(Request<Bitmap> request) {
                        Log.e("test", request.getUrl());
                    }
                });


            } else if (Objects.equals(mBtnGetImg, view)) {

                // Normal Image Request

                // One way
                ImageRequest imageRequest = new ImageRequest(NORMAL_IMAGE_URL, new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        mIvImg.setImageBitmap(response);
                    }
                }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mIvImg.setImageResource(R.mipmap.ic_launcher);
                    }
                });

                mRequestQueue.add(imageRequest);
                mRequestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Bitmap>() {
                    @Override
                    public void onRequestFinished(Request<Bitmap> request) {
                        Log.e("test", request.getUrl());
                    }
                });

                //                // Two way
                //                ImageLoader imageLoader = new ImageLoader(mRequestQueue, new BitmapCache());
                //
                //                ImageLoader.ImageListener imageListener = imageLoader.getImageListener(mIvImg, R.mipmap.ic_launcher, R.mipmap.ic_launcher);
                //                //imageLoader.get(IMAGE_URL, imageListener);
                //                imageLoader.get(VOLLEY_IMAGE_URL, imageListener, 50, 50);
                //
                //                // Three way
                //                mNetImg.setDefaultImageResId(R.mipmap.ic_launcher);
                //                mNetImg.setErrorImageResId(R.mipmap.ic_launcher);
                //                mNetImg.setImageUrl(VOLLEY_IMAGE_URL, imageLoader);

            } else if (Objects.equals(view, mBtnCancel)) {
                mRequestQueue.cancelAll(REQUEST_TAG);
            }
        }
    };

    static class BitmapCache implements ImageLoader.ImageCache {

        private final LruCache<String, Bitmap> mCache;

        public BitmapCache() {
            int maxSize = 10 * 1024 * 1024;
            mCache = new LruCache<String, Bitmap>(maxSize) {
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    return bitmap.getRowBytes() * bitmap.getHeight();
                }
            };
        }

        @Override
        public Bitmap getBitmap(String url) {
            return mCache.get(url);
        }

        @Override
        public void putBitmap(String url, Bitmap bitmap) {
            mCache.put(url, bitmap);
        }
    }


    protected <VT extends View> VT getViewById(@IdRes int id) {
        return (VT) findViewById(id);
    }
}
