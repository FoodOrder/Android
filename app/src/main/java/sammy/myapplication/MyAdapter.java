package sammy.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Lin on 2016/8/29.
 */
public class MyAdapter extends BaseAdapter {
    Context context;
    ArrayList<HashMap<String,Object>> data;

    public MyAdapter(Context context, ArrayList<HashMap<String, Object>> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    //用來計算資料的筆數
    public int getCount() {
        return data.size();
    }

    @Override
    //可以用來回傳 listview 一列中的某個物件
    public Object getItem(int position) {
        return null;
    }

    @Override
    //用來設定每一列 listview的id
    public long getItemId(int position) {
        return 0;
    }

    @Override
    //用來回傳每一列 listview 的樣式
    public View getView(int position, View convertView, ViewGroup parent) {
        MyTag myTag=new MyTag();
        //設定View的部份
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.listview,null);
            myTag.tv1=(TextView)convertView.findViewById(R.id.textView2);
            myTag.tv2=(TextView)convertView.findViewById(R.id.textView3);
            myTag.tv3=(TextView)convertView.findViewById(R.id.textView4);
            convertView.setTag(myTag);
        }else{
            myTag=(MyTag)convertView.getTag();
        }

        //設定資 料的部份
        myTag.tv1.setText(data.get(position).get("shopName").toString());
        myTag.tv2.setText(data.get(position).get("phone").toString());
        myTag.tv3.setText(data.get(position).get("ID").toString());
        return convertView;
    }

    class MyTag {
        TextView tv1,tv2,tv3;
    }
}