package com.crimbear.voi.sabianmcelroy.catanchornews;

import android.content.Context;
import android.media.Image;
import android.widget.ImageView;

public class IVThread extends Thread {
    ImageView ivPicture;
    public FlickrPhoto photoRef;
    public String path;
    MainActivity parent;
    public IVThread(ImageView ivp, MainActivity parent){
        ivPicture = ivp;
        this.parent = parent;
    }
    @Override
    public void run() {
        super.run();
        if(photoRef != null)
            parent.OnImageFound(photoRef.LoadImageFromWebOperations());
        else
            parent.OnImageFound(FlickrPhoto.LoadStaticImageFromWebOperations(path));
    }
}
