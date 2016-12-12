package sammy.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.util.List;


/**
 * Created by Lin on 2016/10/7.
 */
public class MealListAdapter extends ArrayAdapter<Meal> {
    private Context mContext;

    private List<Meal> mMealItemList;

    private int mResourceId;

    public MealListAdapter(Context context,int resource, List<Meal> objects) {
        super(context,resource ,objects);
        mContext = context;
        mResourceId = resource;
        mMealItemList = objects;
    }



    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

         Meal meal = mMealItemList.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.format_meunview, parent, false);
        }

        TextView tvShowShopName = (TextView)convertView.findViewById(R.id.textView7);
        TextView tvShowShopTel = (TextView)convertView.findViewById(R.id.textView3) ;
        TextView amount = (TextView)convertView.findViewById(R.id.amount);
        TextView mealname = (TextView) convertView.findViewById(R.id.mealname);
        TextView mealprice = (TextView) convertView.findViewById(R.id.mealprice);
        tvShowShopName.setText(meal.getShopName());
        tvShowShopTel.setText(meal.getshopTel());
        amount.setText("數量 "+String.valueOf(meal.getMealnumber()));
        mealname.setText(meal.getMealname());
        mealprice.setText("$ "+String.valueOf(meal.getMealprice()));


        return convertView;
    }
}