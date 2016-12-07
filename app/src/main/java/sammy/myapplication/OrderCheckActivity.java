package sammy.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class OrderCheckActivity extends AppCompatActivity {
    static String Useremail;
    String userID;
    String ORDERCHECK_USER_ID;
    private static final String ORDERCHECK_URL = "http://140.134.26.71:58080/android-backend/webapi/order/userId/";
    private static final int UPDATE_USER_DETAIL = 1;
    private static final int UPDATE_CHECK_LIST = 1;
    private OrderCheckAdapter orderCheckAdapter;
    private ArrayList<OrderChecklist> Checklist = new ArrayList<OrderChecklist>();
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_USER_DETAIL: {
                    updateUserDetail();
                    break;
                }
            }
        }
    };
    private Handler handler1 = new Handler() {
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
        getbundle();
        GetuserDetail();
        getShopListFromServer();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    private void updateCheckList() {
        orderCheckAdapter.clear();
        orderCheckAdapter.addAll(Checklist);
    }

    private void updateUserDetail() {
        ORDERCHECK_USER_ID = ORDERCHECK_URL+userID;
    }

    void getbundle() {
        Bundle bundle = this.getIntent().getExtras();
        Useremail = bundle.getString("EMAIL");
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
                    Ordlist.setRemark(((JSONObject) jsonObis.get(i)).getString("remark"));
                    Ordlist.setOrderTime(((JSONObject) jsonObis.get(i)).getString("orderTime"));
                    Checklist.add(Ordlist);
                }
                Message m = new Message();
                m.what = UPDATE_CHECK_LIST;
                handler1.sendMessage(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    void GetuserDetail() {
        HttpURLConnection conn = null;
        try {
            // 建立連線
            URL url = new URL(Useremail);
            System.out.println(Useremail);

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
            userID=new JSONObject(jsonString).getString("ID");
            Message m = new Message();
            m.what = UPDATE_USER_DETAIL;
            handler.sendMessage(m);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
