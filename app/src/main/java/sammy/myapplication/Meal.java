package sammy.myapplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Lin on 2016/10/7.
 */
public class Meal implements Serializable{

    private String mealid;

    private String mealname;

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


    public JSONObject getJSONObject(){
        JSONObject obj = new JSONObject();
        try{

                obj.put("Mealname", mealid);
                obj.put("MealAmout",mealnumber);

            } catch (JSONException e1) {
            e1.printStackTrace();
        }
     return obj;
    }

}



