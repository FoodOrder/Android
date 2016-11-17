package sammy.myapplication;

import java.io.Serializable;

/**
 * Created by Lin on 2016/10/17.
 */
public class Cart implements Serializable{

   private String foodid;

   private int foodamout;

    public String getFoodid() {
        return foodid;
    }

    public void setFoodid(String foodid) {
        this.foodid = foodid;
    }

    public int getFoodamout() {
        return foodamout;
    }

    public void setFoodamout(int foodamout) {
        this.foodamout = foodamout;
    }
}
