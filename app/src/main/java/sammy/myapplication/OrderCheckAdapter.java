package sammy.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lin on 2016/12/8.
 */
public class OrderCheckAdapter extends ArrayAdapter<OrderChecklist> {

    public OrderCheckAdapter(Context context, List<OrderChecklist> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        OrderChecklist Ordlist = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.format_checkview, parent, false);
        }

        TextView tv_status = (TextView)convertView.findViewById(R.id.status);
        TextView tv_id = (TextView)convertView.findViewById(R.id.id);
        TextView tv_time = (TextView)convertView.findViewById(R.id.time);
        tv_id.setText(Ordlist.getId());
        switch (Ordlist.getStatus()){
            case  "0":
                tv_status.setText("待確認");
                break;
            case  "1":
                tv_status.setText("製作中");
                break;
            case  "2":
                tv_status.setText("外送中");
                break;
            case  "3":
                tv_status.setText("已送達");
                break;
            case  "4":
                tv_status.setText("拒絕此訂單");
                break;

        }
        tv_time.setText(Ordlist.getOrderTime());
        return convertView;
    }

}
