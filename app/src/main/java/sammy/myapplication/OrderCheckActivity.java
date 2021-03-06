package sammy.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class OrderCheckActivity extends AppCompatActivity {
    String userID;
    String ORDERCHECK_USER_ID;
    private static final String URL ="http://140.134.26.71:58080/android-backend/webapi/orderItem/";
    private static final String ORDERCHECK_URL = "http://140.134.26.71:58080/android-backend/webapi/order/userId/";
    private static final int UPDATE_CHECK_LIST = 1;
    private OrderCheckAdapter orderCheckAdapter;
    private ArrayList<OrderChecklist> Checklist = new ArrayList<OrderChecklist>();
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_CHECK_LIST: {
                    updateCheckList();
                    break;
                }
            }
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_check);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ListView oreder_check_list = (ListView)this.findViewById(R.id.checklistView);
        orderCheckAdapter = new OrderCheckAdapter(this, new ArrayList<OrderChecklist>());
        oreder_check_list.setAdapter(orderCheckAdapter);
        getbundle();
        ORDERCHECK_USER_ID = ORDERCHECK_URL+userID;
        System.out.println(ORDERCHECK_USER_ID);
        new Thread(new Runnable() {
            @Override
            public void run() {
                getShopListFromServer();
            }}).start();

        oreder_check_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String checkurl = URL + String.valueOf(Checklist.get(position).getId());
                Intent intent = new Intent(OrderCheckActivity.this, OrderItemActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("status",Checklist.get(position).getStatus());
                bundle.putString("id",String.valueOf(Checklist.get(position).getId()));
                bundle.putString("checkurl", checkurl);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void updateCheckList() {
        orderCheckAdapter.clear();
        orderCheckAdapter.addAll(Checklist);
    }

    void getbundle() {
        Bundle bundle = this.getIntent().getExtras();
        userID = bundle.getString("userID");
    }

    void getShopListFromServer() {
        HttpURLConnection conn = null;
        try {
            // 建立連線
            URL url = new URL(ORDERCHECK_USER_ID);
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
                    OrderChecklist Ordlist = new OrderChecklist();
                    Ordlist.setId(((JSONObject) jsonObis.get(i)).getString("id"));
                    Ordlist.setStatus(((JSONObject) jsonObis.get(i)).getString("status"));
                    Ordlist.setOrderTime(((JSONObject) jsonObis.get(i)).getString("orderTime"));
                    Checklist.add(Ordlist);
                }
                Message m = new Message();
                m.what = UPDATE_CHECK_LIST;
                handler.sendMessage(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
