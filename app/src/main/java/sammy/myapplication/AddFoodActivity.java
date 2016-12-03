package sammy.myapplication;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AddFoodActivity extends AppCompatActivity {
    static String foodid;
    String orderid;
    String foodname;
    int foodprice;
    private EditText foodamt ;

    static String URL1;
    Drawable URL2;
    private Meal mItem;
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
//        System.out.println(foodamt.getText().toString());
        foodamt = (EditText)findViewById(R.id.foodamt);
        TextView foodname1 = (TextView)findViewById(R.id.foodname1);
        TextView foodprice1 = (TextView)findViewById(R.id.foodprice1);
        foodname1.setText(foodname);
        foodprice1.setText("$ " + String.valueOf(foodprice));
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(foodamt.getText().toString());
                Log.v("ANDY",foodamt.getText().toString());
              onBackPressed();

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
        ImageView foodpicture = (ImageView)findViewById(R.id.foodpic);
        foodpicture.setImageDrawable(URL2);
    }
    public void onBackPressed() {
        Intent intent = new Intent();
        Log.v("ANDY",foodamt.getText().toString());
        if ((foodamt.getText().toString()) != null){
            Log.v("ANDY",foodamt.getText().toString());
            System.out.println(foodamt.getText().toString());
            intent.putExtra("UpdateAmount", Integer.valueOf(foodamt.getText().toString()));
        }else {
            intent.putExtra("UpdateAmount", 0);
        }
        Log.v("food1", foodamt.getText().toString());
        setResult(MealListActivity.GET_AMOUNT, intent);
        this.finish();
    }

    void getbundle() {
        Intent intent = this.getIntent();
        mItem = (Meal)intent.getSerializableExtra("FoodItem");
        orderid = mItem.getMealid();
        foodname = mItem.getMealname();
        foodprice = mItem.getMealprice();
        Bundle bundle = this.getIntent().getExtras();
        URL1 = bundle.getString("picurl");
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
                    URL2=loadImageFromURL(URL1);
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
