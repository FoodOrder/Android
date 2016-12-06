package sammy.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AccountActivity extends AppCompatActivity {
    static String Useremail;
    String userID;
    String userName;
    String userEmail;
    String userPhone;
    String userPassword;
    private static final int UPDATE_USER_DETAIL = 1;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getbundle();
        new Thread(new Runnable() {
            @Override
            public void run() {
                GetuserDetail();
            }
        }).start();

    }

    private void updateUserDetail() {
        TextView userid = (TextView)findViewById(R.id.userid);
        TextView username = (TextView)findViewById(R.id.username);
        TextView useremail = (TextView)findViewById(R.id.useremail);
        TextView userpassword = (TextView)findViewById(R.id.userpassword);
        TextView userphone = (TextView)findViewById(R.id.userphone);

        userid.setText(userID);
        username.setText(userName);
        userphone.setText(userPhone);
        useremail.setText(userEmail);
        userpassword.setText(userPassword);
    }

    void getbundle() {
        Bundle bundle = this.getIntent().getExtras();
        Useremail = bundle.getString("EMAIL");
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
            userName = new JSONObject(jsonString).getString("userName");
            userEmail = new JSONObject(jsonString).getString("email");
            userPassword = new JSONObject(jsonString).getString("password");
            userPhone = new JSONObject(jsonString).getString("phone");
            System.out.println(userName);
            System.out.println(userID);
            System.out.println(userPhone);
            Message m = new Message();
            m.what = UPDATE_USER_DETAIL;
            handler.sendMessage(m);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
