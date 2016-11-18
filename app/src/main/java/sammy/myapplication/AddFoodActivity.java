package sammy.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;

public class AddFoodActivity extends AppCompatActivity {
    static String foodid;
    String orderid;
    String foodname;
    String foodnubmer;
    int foodnumber1;
    int foodprice;
    private EditText foodamt;
    private static final String URL1= "http://140.134.26.71:58080/android-backend/Menu_UploadDownloadFileServlet?id=1";
    Drawable URL2;
    public static final String KEY = "com.my.package.app";
    private static final int UPDATE_MEAL_LIST = 1;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_MEAL_LIST: {
                    updatefoodList();
                    break;
                }
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);;
        getbundle();
        foodamt = (EditText)findViewById(R.id.foodamt);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences spref = getApplication().getSharedPreferences(KEY, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = spref.edit();
                editor.putInt("foodid",Integer.valueOf(orderid));
                editor.putInt("foodprice", foodprice);
                foodnubmer = foodamt.getText().toString();
                foodnumber1 = Integer.valueOf(foodnubmer);
                editor.putInt("foodnumber",foodnumber1);
                editor.apply();
                Snackbar.make(view, "加入購物車", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                GetfoodDetail();
            }
        }).start();
    }

    private void updatefoodList() {
        TextView foodname1 = (TextView)findViewById(R.id.foodname1);
        TextView foodprice1 = (TextView)findViewById(R.id.foodprice1);
        ImageView foodpicture = (ImageView)findViewById(R.id.foodpic);
        foodname1.setText(foodname);
        foodprice1.setText("$ " + String.valueOf(foodprice));
        foodpicture.setImageDrawable(URL2);
    }

    void getbundle() {
        Bundle bundle = this.getIntent().getExtras();
        foodid = bundle.getString("fooddetail");
    }

    void GetfoodDetail() {
        HttpURLConnection conn = null;
        try {
            // 建立連線
            URL url = new URL(foodid);
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
                    orderid=new JSONObject(jsonString).getString("id");
                    foodname = new JSONObject(jsonString).getString("menuName");
                    foodprice = new JSONObject(jsonString).getInt("menuPrice");
            URL2=loadImageFromURL(URL1);
                   // foodnubmer = foodamt.getText().toString();
                Message m = new Message();
                m.what = UPDATE_MEAL_LIST;
                handler.sendMessage(m);
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
