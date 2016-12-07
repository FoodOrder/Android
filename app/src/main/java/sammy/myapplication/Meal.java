package sammy.myapplication;

import android.graphics.drawable.Drawable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Lin on 2016/10/7.
 */
public class Meal implements Serializable{

    private String mealid;

    private String mealname;

    private String shopName;

    private Drawable shopPic;

    private String shopTel;

    private int mealprice;

    private int mealnumber = 0;

    public String getMealid() {
        return mealid;
    }

    public void setMealid(String mealid) {
        this.mealid = mealid;
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

    public int getMealnumber() {return mealnumber; }

    public void setMealnumber(int mealnumber) {
        this.mealnumber = mealnumber;
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


    public JSONObject getJSONObject(){
        JSONObject obj = new JSONObject();
        try{

                obj.put("Mealname", mealid);
                obj.put("MealAmout",mealnumber);
            obj.put("shopName",shopName);
            obj.put("shopPic",shopPic);
            obj.put("shopTel",shopTel);

            } catch (JSONException e1) {
            e1.printStackTrace();
        }
     return obj;
    }

}



