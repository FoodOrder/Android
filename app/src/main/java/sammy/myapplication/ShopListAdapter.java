package sammy.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

/**
 * Created by Lin on 2016/8/29.
 */
public class ShopListAdapter extends ArrayAdapter<Shop> {
    public ShopListAdapter(Context context, List<Shop> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Shop shop = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.format_shopview, parent, false);
        }
        ImageView ivshop =(ImageView)convertView.findViewById(R.id.shopview);
        TextView tvShopName = (TextView)convertView.findViewById(R.id.tv_shop_name);
        TextView tvPhone = (TextView)convertView.findViewById(R.id.tv_phone);
        tvShopName.setText(shop.getName());
        tvPhone.setText(shop.getTel());
        ivshop.setImageDrawable(shop.getImg());

        return convertView;
    }


}