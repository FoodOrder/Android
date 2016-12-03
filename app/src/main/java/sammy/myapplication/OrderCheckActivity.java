package sammy.myapplication;

import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class OrderCheckActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ramdom);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


    }


   /* void getＣㄏㄜFromServer() {
        HttpURLConnection conn = null;
        try {
            // 建立連線
            URL url = new URL();
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
                    listShops.add(shop);
                }
                Message m = new Message();
                m.what = UPDATE_SHOP_LIST;
                handler.sendMessage(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}
