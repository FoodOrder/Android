package sammy.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class OrderItemActivity extends AppCompatActivity {
    private static final String update ="http://140.134.26.71:58080/android-backend/webapi/order/update";
    String URL;
    String Status;
    String id;
    String status = "3";
    String pass = "2";
    private Button btnchecked;
    private OrderItemAdapter orderItemAdapter;
    ArrayList<Meal> checklist = new ArrayList<Meal>();
    private static final int UPDATE_CHECK_LIST = 1;
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
        setContentView(R.layout.activity_order_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnchecked = (Button)findViewById(R.id.checkbt);
        btnchecked.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                System.out.println(Status);
                if(Status.equals(pass)) {
                    submitRegistration();
                    Toast.makeText(OrderItemActivity.this, "感謝使用", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent();
                    intent.setClass(OrderItemActivity.this, MainActivity.class);
                    startActivity(intent);
                }else{
                    Snackbar.make(v, "尚未外送無法選擇已送達", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });




        ListView itemListView = (ListView) this.findViewById(R.id.itemlistView);
        orderItemAdapter = new OrderItemAdapter(this, new ArrayList<Meal>());
        itemListView.setAdapter(orderItemAdapter);
        getbundle();
        System.out.println(URL);
        new Thread(new Runnable() {
            @Override
            public void run() {
                GetCheckDetail();
            }}).start();

    }

    private void updateCheckList() {
        orderItemAdapter.clear();
        orderItemAdapter.addAll(checklist);
    }

    void getbundle() {
        Bundle bundle = this.getIntent().getExtras();
        Status = bundle.getString("status");
        URL = bundle.getString("checkurl");
        id = bundle.getString("id");
    }

    void GetCheckDetail() {
        HttpURLConnection conn = null;
        try {
            // 建立連線
            java.net.URL url = new URL(URL);
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
                for (int i = 0; i < jsonObis.length(); i++) {
                    Meal meal=new Meal();
                    meal.setMealname(((JSONObject) jsonObis.get(i)).getString("foodName"));
                    meal.setMealnumber(((JSONObject) jsonObis.get(i)).getInt("amount"));
                    System.out.println(meal.getMealname());
                    System.out.println(meal.getMealnumber());
                    checklist.add(meal);
                }
                Message m = new Message();
                m.what = UPDATE_CHECK_LIST;
                handler.sendMessage(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void submitRegistration(){
        new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    String response = "";
                    URL url = new URL(update);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(15000);
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);

                    HashMap<String, String> postDataParams = new HashMap<>();

                    postDataParams.put("status",status);
                    postDataParams.put("id", id);
                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));

                    String test = "id="+id+"&"+"status="+status;
                    writer.write(test);
            //        writer.write(getPostDataString(postDataParams));
              System.out.println(test);
                    writer.flush();
                    writer.close();
                    os.close();
                    int responseCode=conn.getResponseCode();
                    if (responseCode == HttpsURLConnection.HTTP_OK) {
                        String line;
                        BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        while ((line=br.readLine()) != null) {
                            response+=line;
                        }
                    }
                    else {
                        response="";
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }
}
