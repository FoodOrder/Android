package sammy.myapplication;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.view.View;
import android.widget.AdapterView;


public class OrderActivity extends AppCompatActivity {
    ListView lv;
    private static final String SERVICE_URL = "http://140.134.26.71:58080/android-backend/webapi/shop/list";
    private List<String> Shoplist = new ArrayList<String>();
    private static final int UPDATE_SHOP_LIST = 1;
    private ArrayAdapter<String> adt;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_SHOP_LIST: {
                    updateShopList();
                    break;
                }
            }
        }

    };


    AdapterView.OnItemClickListener onClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

      ListView shopListview = (ListView) this.findViewById(R.id.shoplistView);
        adt = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1, new ArrayList<String>());
        shopListview.setAdapter(adt);
        new Thread() {
            public void run() {
                getShopListFromServer();
            }
        }.start();

        shopListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s = Shoplist.get(position);
                Intent intent = new Intent(OrderActivity.this, ShopDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("SHOPNAME", s);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
       // findviews();
    }

    private void updateShopList() {
       adt.clear();
        adt.addAll(Shoplist);
    }


    void findviews(){
        lv=(ListView)findViewById(R.id.shoplistView);
        setListview3();
    }



    void setListview3(){
        ArrayList<HashMap<String,Object>> data=new ArrayList<>();
        for(int i=0;i<Shoplist.size();i++){
            HashMap<String,Object> items=new HashMap<>();
//            items.put("pics",pics[i%4]);
            items.put("shopName", Shoplist.get(i));
            //  items.put("phone",Shoplist.get(i));
        //    items.put("ID",Shoplist.get(i));

            data.add(items);
        }

        MyAdapter adt=new MyAdapter(this,data);
        lv.setAdapter(adt);

    }




    void getShopListFromServer() {
        HttpURLConnection conn = null;
        try {
            // 建立連線
            URL url = new URL(SERVICE_URL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.connect();
            // 讀取資料
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    conn.getInputStream(), "UTF-8"));
            String jsonString = reader.readLine();
            reader.close();
            // 解析 json
            JSONArray jsonObis = new JSONArray(jsonString);
            if (jsonObis.length() > 0) {
                Shoplist = new ArrayList<String>();
                for (int i = 0; i < jsonObis.length(); i++) {
                    String shopName = ((JSONObject) jsonObis.get(i)).getString("shopName");
                    Shoplist.add(shopName);
                }
                Message m = new Message();
                m.what = UPDATE_SHOP_LIST;
                handler.sendMessage(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } /*catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

     }

 }

