package sammy.myapplication;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import android.view.View;
import android.widget.AdapterView;


public class OrderActivity extends AppCompatActivity {
    static String URL = "http://140.134.26.71:58080/android-backend/webapi/menu/email/";
    private static final String SERVICE_URL = "http://140.134.26.71:58080/android-backend/webapi/shop/list";
    private static final int UPDATE_SHOP_LIST = 1;
    private ShopListAdapter shopListAdapter;
    private ArrayList<Shop> listShops = new ArrayList<Shop>();

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


  /*  AdapterView.OnItemClickListener onClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        }
    };*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView shopListView = (ListView) this.findViewById(R.id.shoplistView);
        shopListAdapter = new ShopListAdapter(this, new ArrayList<Shop>());
        shopListView.setAdapter(shopListAdapter);

        new Thread(new Runnable() {
            @Override
            public void run() {
                getShopListFromServer();

        }}).start();


        shopListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String shopemail = URL+String.valueOf(listShops.get(position).getemail());
                Intent intent = new Intent(OrderActivity.this, MealListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("shopemail", shopemail);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    private void updateShopList() {
       shopListAdapter.clear();
        shopListAdapter.addAll(listShops);
    }
    //get shoplist from server.
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
                for(int i = 0; i < jsonObis.length(); i++) {
                    Shop shop = new Shop();
                    shop.setName(((JSONObject) jsonObis.get(i)).getString("shopName"));
                    shop.setTel(((JSONObject) jsonObis.get(i)).getString("phone"));
                    shop.setImgURL(((JSONObject) jsonObis.get(i)).getString("photo"));
                    shop.setemail(((JSONObject) jsonObis.get(i)).getString("email"));
                    shop.setImg(loadImageFromURL(shop.getImgURL()));
                    listShops.add(shop);
                }
                Message m = new Message();
                m.what = UPDATE_SHOP_LIST;
                handler.sendMessage(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
     }


    private Drawable loadImageFromURL(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable draw = Drawable.createFromStream(is, "src");
            return draw;
        } catch (Exception e) {
            return null;
        }
    }

}


