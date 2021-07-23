package com.sanslayshare.addfile;

import android.graphics.Bitmap;
import android.net.Uri;

public class listcons {

    String filename;
    Bitmap photo;
    String size;
    Uri ur;

   public listcons(String filename,Bitmap photo,String size,Uri ur)
    {
        this.filename= filename;
        this.photo= photo;
        this.size= size;
        this.ur= ur;

    }

    public String getfilename(){
        return filename;
    }

    public  String getsize()
    {

        return size;
    }
    public  Bitmap getphoto()
    {
        return photo;
    }

    public Uri geturi(){
       return ur;
    }

}
