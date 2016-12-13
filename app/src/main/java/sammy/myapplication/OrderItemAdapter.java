package sammy.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Lin on 2016/12/13.
 */
public class OrderItemAdapter extends ArrayAdapter<Meal> {

    public OrderItemAdapter(Context context, List<Meal> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Meal meal = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.format_checkitem, parent, false);
        }

        TextView checkname = (TextView)convertView.findViewById(R.id.checkname);
        TextView checkamount = (TextView)convertView.findViewById(R.id.checkamount);
        checkamount.setText(String.valueOf(meal.getMealnumber()));
        checkname.setText(meal.getMealname());
        return convertView;
    }
}