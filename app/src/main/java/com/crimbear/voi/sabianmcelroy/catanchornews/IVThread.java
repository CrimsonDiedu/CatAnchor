package com.crimbear.voi.sabianmcelroy.catanchornews;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

public class IVThread extends Thread {
        public FlickrPhoto photoRef;
        public String path;
        InputStream is;
        MainActivity parent;
        public IVThread(MainActivity parent){
            this.parent = parent;
        }
    @Override
    public void run() {
        super.run();
        int i;
            try{
            if (photoRef != null) {
                is = photoRef.LoadImageFromWebOperations();
                parent.OnImageFound();
            } else if (path != null && path != "")
                is = FlickrPhoto.LoadStaticImageFromWebOperations(path);
            parent.OnImageFound();
        }
        catch(InterruptedException ie)
        {

        }
    }
}
