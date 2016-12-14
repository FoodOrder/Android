package sammy.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    public static final String KEY = "com.my.package.app";
    String userID;
    private ImageView GoToOrderButton;
    private ImageView SearchOrder;
    private ImageView ivSearchNearShop;
    private ImageView ivCustomerService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        GoToOrderButton =(ImageView) findViewById(R.id.buttonorder);
        GoToOrderButton.setImageResource(R.drawable.store);
        SearchOrder = (ImageView)findViewById(R.id.searchorder);
        SearchOrder.setImageResource(R.drawable.order);

        ivSearchNearShop = (ImageView)findViewById(R.id.searchNearShop);
        ivCustomerService = (ImageView)findViewById(R.id.service);
        ivSearchNearShop.setImageResource(R.drawable.nearshop);
        ivCustomerService.setImageResource(R.drawable.service);

        SharedPreferences spref = getApplication().getSharedPreferences(KEY, Context.MODE_PRIVATE);
        String URL= "http://140.134.26.71:58080/android-backend/webapi/user/email/";
        final String email= URL+spref.getString("email", null);

        new Thread(new Runnable() {
            @Override
            public void run() {
                GetuserDetail(email);
            }}).start();

    }
    //OrderActivity view change
    public void GotoOrderAct(View v) {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, OrderActivity.class);
        startActivity(intent);

    }
    //CustomerServiceActivity view change
    public void GotoCustomerAct(View v) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("userID", userID);
        intent.putExtras(bundle);
        intent.setClass(MainActivity.this, CustomerServiceActivity.class);
        startActivity(intent);

    }
    //OrderCheckActivity view change
    public void GotoOrderCheckAct(View v) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("userID", userID);
        intent.putExtras(bundle);
        intent.setClass(MainActivity.this, OrderCheckActivity.class);
        startActivity(intent);
    }
    //AroundActivity view change
    public void GotoAroundAct(View v) {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, AroundActivity.class);
        startActivity(intent);

    }
        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

            return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            SharedPreferences spref = getApplication().getSharedPreferences(KEY, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = spref.edit();
            editor.clear();
            editor.commit();
            Intent intent = new Intent();
            intent.setClass(this, LoginActivity.class);
            startActivity(intent);
            MainActivity.this.finish();
            return true;
        }

       if(id == R.id.action_account) {
           SharedPreferences spref = getApplication().getSharedPreferences(KEY, Context.MODE_PRIVATE);
           String URL= "http://140.134.26.71:58080/android-backend/webapi/user/email/";
           String email= URL+spref.getString("email", null);
           Intent intent = new Intent();
           Bundle bundle = new Bundle();
           bundle.putString("EMAIL", email);
           intent.putExtras(bundle);
           intent.setClass(this, AccountActivity.class);
           startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
    void GetuserDetail(String email) {
        HttpURLConnection conn = null;
        try {
            // 建立連線
            URL url = new URL(email);
            System.out.println(email);

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
            SharedPreferences spref = getApplication().getSharedPreferences(KEY, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = spref.edit();
            editor.putString("userID", userID);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
