package sammy.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lin on 2016/10/7.
 */
public class MealListAdapter extends ArrayAdapter<Meal> {
    private ArrayList<Cart> listCart = new ArrayList<Cart>();

    public MealListAdapter(Context context, List<Meal> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
         NumberPicker numPicker;
        final Meal meal = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.meunview, parent, false);
        }

        TextView mealname = (TextView) convertView.findViewById(R.id.mealname);
        TextView mealprice = (TextView) convertView.findViewById(R.id.mealprice);
        mealname.setText(meal.getMealname());
        mealprice.setText("$"+String.valueOf(meal.getMealprice()));

        numPicker=(NumberPicker) convertView.findViewById(R.id.numberPicker);
        numPicker.setMaxValue(20);
        numPicker.setMinValue(0);
        numPicker.setValue(0);
        numPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            public void onValueChange(NumberPicker view, int oldValue, int newValue) {
             /*   Cart cart = new Cart();
                cart.setFoodid(meal.getMealid());
                cart.setFoodamout(newValue);
                for (int i = 0; i < listCart.size(); i++) {
                    if (listCart.get(i).getFoodid() == meal.getMealid()) {
                        listCart.set(i, cart);
                    } else {
                        listCart.add(cart);
                    }
                }*/

                // tv.setText("pick number is " + String.valueOf(newValue));

            }
        });


        return convertView;
    }
}