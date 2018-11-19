package com.crimbear.voi.sabianmcelroy.catanchornews;

import android.content.Context;
import android.media.Image;
import android.widget.ImageView;

public class IVThread extends Thread {
    ImageView ivPicture;
    MainActivity parent;
    public IVThread(ImageView ivp, MainActivity parent){
        ivPicture = ivp;
        this.parent = parent;
    }
    @Override
    public void run() {
        super.run();
        parent.OnImageFound(FlickrPhoto.LoadImageFromWebOperations("https://farm1.staticflickr.com/2/1418878_1e92283336_m.jpg",ivPicture));
    }
}
