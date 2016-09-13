package sammy.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Lin on 2016/8/29.
 */
public class MyAdapter extends ArrayAdapter<Shop> {

    public MyAdapter(Context context,  List<Shop> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Shop shop = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview, parent, false);
        }

        TextView tvShopName = (TextView)convertView.findViewById(R.id.tv_shop_name);
        TextView tvPhone = (TextView)convertView.findViewById(R.id.tv_phone);
        tvShopName.setText(shop.getName());
        tvPhone.setText(shop.getTel());

        return convertView;
    }
}