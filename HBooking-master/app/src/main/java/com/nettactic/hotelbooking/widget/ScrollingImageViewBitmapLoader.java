package com.nettactic.hotelbooking.widget;

import android.content.Context;
import android.graphics.Bitmap;

import com.bumptech.glide.Glide;
import com.nettactic.hotelbooking.common.MyApplication;

/**
 * Created by thijs on 22-03-16.
 */
public interface ScrollingImageViewBitmapLoader {

    Bitmap loadBitmap(Context context, int resourceId);
}
