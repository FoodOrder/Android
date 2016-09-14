package sammy.myapplication;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.util.Date;

/**
 * Created by Lin on 2016/9/13.
 */
public class Shop {

    private int id;

    private String name;

    private Drawable Img;

    private String imgURL;

    private Date openTime;

    private String tel;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getImg() {
        return Img;
    }

    public void setImg(Drawable img) {
        Img = img;
    }

    public String getImgURL() {return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public Date getOpenTime() {
        return openTime;
    }

    public void setOpenTime(Date openTime) {
        this.openTime = openTime;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
