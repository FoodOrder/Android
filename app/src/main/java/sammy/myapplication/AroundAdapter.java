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
 * Created by Lin on 2016/12/11.
 */
public class AroundAdapter  extends ArrayAdapter<Shop> {
    public AroundAdapter(Context context, List<Shop> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Shop shop = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.format_aroundview, parent, false);
        }

        TextView tvShopName = (TextView)convertView.findViewById(R.id.ar_name);
        TextView tvPhone = (TextView)convertView.findViewById(R.id.ar_phone);
        TextView tvDistance = (TextView)convertView.findViewById(R.id.ar_distance);
        ImageView imageView = (ImageView)convertView.findViewById(R.id.aroundimgView);
        imageView.setImageDrawable(shop.getImg());
        tvShopName.setText(shop.getName());
        tvPhone.setText(shop.getTel());
        tvDistance.setText(shop.getDistance().toString()+"M");
        return convertView;
    }
}
