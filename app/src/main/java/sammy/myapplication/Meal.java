package sammy.myapplication;

import android.graphics.drawable.Drawable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Lin on 2016/10/7.
 */
public class Meal implements Serializable{

    private String foodId;

    private String mealname;

    private String shopName;

    private Drawable shopPic;

    private String shopTel;

    private int mealprice;

    private int amount = 0;

    public String getMealid() {
        return foodId;
    }

    public void setMealid(String mealid) {
        this.foodId = mealid;
    }

    public String getMealname() {
        return mealname;
    }

    public void setMealname(String mealname) {
        this.mealname = mealname;
    }

    public int getMealprice() {
        return mealprice;
    }

    public void setMealprice(int mealprice) {
        this.mealprice = mealprice;
    }

    public int getMealnumber() {return amount; }

    public void setMealnumber(int mealnumber) {
        this.amount = mealnumber;
    }

    public void setshopPic(Drawable shopPic){
        this.shopPic = shopPic;
    }
    public Drawable getshopPic(){
        return shopPic;
    }

    public void setshopTel(String shopTel){
        this.shopTel = shopTel;
    }
    public String getshopTel(){
        return shopTel;
    }
    public void setShopName(String shopName){
        this.shopName = shopName;
    }
    public String getShopName(){
        return shopName;
    }



}



