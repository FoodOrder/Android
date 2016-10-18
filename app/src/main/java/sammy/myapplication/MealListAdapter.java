package sammy.myapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.NumberPicker;
import android.widget.TextView;
import java.util.List;

/**
 * Created by Lin on 2016/10/7.
 */
public class MealListAdapter extends ArrayAdapter<Meal> {

    public MealListAdapter(Context context, List<Meal> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
         NumberPicker numPicker;
        Meal meal = getItem(position);

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



        return convertView;
    }
}