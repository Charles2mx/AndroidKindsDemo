package com.xiaoming.acrossendweex.adapter;


import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.taobao.weex.adapter.IWXImgLoaderAdapter;
import com.taobao.weex.common.WXImageStrategy;
import com.taobao.weex.dom.WXImageQuality;
import com.xiaoming.acrossendweex.MyApplication;


//https://blog.csdn.net/weixin_33742618/article/details/91380769
public class ImageAdapter implements IWXImgLoaderAdapter {
    @Override
    public void setImage(String s, ImageView imageView, WXImageQuality wxImageQuality, WXImageStrategy wxImageStrategy) {
        //实现你自己的图片下载，否则图片无法显示。这里不一定要用glide，可以使用其他任意的图片加载框架
        Glide.with(MyApplication.mInstance).load(s).into(imageView);
    }
}
