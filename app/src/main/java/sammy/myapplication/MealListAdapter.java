package sammy.myapplication;

import android.content.Context;
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

    public ArrayList<Cart> getCart() {
        return listCart;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final Meal meal = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.format_meunview, parent, false);
        }

        TextView mealname = (TextView) convertView.findViewById(R.id.mealname);
        TextView mealprice = (TextView) convertView.findViewById(R.id.mealprice);
        mealname.setText(meal.getMealname());
        mealprice.setText("$ "+String.valueOf(meal.getMealprice()));


        return convertView;
    }
}