package com.example.bird.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;

import java.util.concurrent.ExecutionException;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class GlideUtil {
    /***
     * Url ile internet üzerinden resim getirme.
     */
    public static synchronized void glide_use(Context context , String url , final ImageView imageView ){
        Glide.with(context)
                .load(url)
                .fitCenter()
                .crossFade()
                .into(imageView);
    }

    public static synchronized void glide_use_Circle (Context context , String url , final ImageView imageView ){
        Glide.with(context)
                .load(url)
                .fitCenter()
                .bitmapTransform(new CropCircleTransformation(context))
                .crossFade()
                .into(imageView);
    }

    public static synchronized void glide_use2(Context context , String url , final ImageView imageView,int w , int h ){
        try {
            Glide.with(context)
                    .load(url)
                    .fitCenter()
                    .bitmapTransform(new CropCircleTransformation(context))
                    .crossFade()
                    .priority(Priority.LOW)
                    .override(w,h)
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static synchronized void glide_use3(Context context , Bitmap url , final ImageView imageView ){
        Glide.with(context)
                .load(url)
                .fitCenter()
                .bitmapTransform(new CropCircleTransformation(context))
                .crossFade()
                .into(imageView);
    }
    /***
     * Drawable dosyasından resim getirme.
     */

    public static synchronized void glide_use(Context context , int resource , ImageView imageView){
        Glide.with(context)
                .load(resource)
                .fitCenter()
                .crossFade()
                .into(imageView);
    }

    /***
     * Resmi daire şeklinde kırpma.
     */

    public static synchronized void glideTransform(Context context , int resource , ImageView imageView){
        Glide.with(context)
                .load(resource)
                .fitCenter()
                .bitmapTransform(new CropCircleTransformation(context))
                .crossFade()
                .into(imageView);
    }

    /***
     * Resmi boyutlandırma
     */

    public static Bitmap glideResize(Context context , String url , int widht , int height){
        try {
            return Glide.with(context)
                    .load(url)
                    .asBitmap()
                    .dontAnimate()
                    .fitCenter()
                    .priority(Priority.LOW)
                    .override(widht,height)
                    .into(widht,height).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }
}
