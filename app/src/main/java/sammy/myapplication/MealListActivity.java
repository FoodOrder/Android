package sammy.myapplication;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MealListActivity extends AppCompatActivity {
    static String Shopemail;
  //  static String URL = "http://140.134.26.71:58080/android-backend/webapi/menu/";
  //  static String ShopDetail_API_URL =URL+Shopemail;

    private MealListAdapter MealListAdapter;

    private ArrayList<Meal> listMenu = new ArrayList<Meal>();
    private static final int UPDATE_MEAL_LIST = 1;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_MEAL_LIST: {
                    updateMealList();
                    break;
                }
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_list);
        getbundle();
        ListView mealListView = (ListView) this.findViewById(R.id.menulistView);
        MealListAdapter = new MealListAdapter(this, new ArrayList<Meal>());
        mealListView.setAdapter(MealListAdapter);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i("098", Shopemail);
                GetShopDetail();
            }}).start();


    }

    private void updateMealList() {
        MealListAdapter.clear();
        MealListAdapter.addAll(listMenu);
    }


    void getbundle(){
        Bundle bundle = this.getIntent().getExtras();
        Shopemail = bundle.getString("shopemail");
    }

    void GetShopDetail() {
        HttpURLConnection conn = null;
        try {
            // 建立連線
            URL url = new URL(Shopemail);
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
                    Meal meal = new Meal();
                    meal.setMealid(((JSONObject) jsonObis.get(i)).getString("id"));
                    meal.setMealname(((JSONObject) jsonObis.get(i)).getString("menuName"));
                    meal.setMealprice(((JSONObject) jsonObis.get(i)).getInt("menuPrice"));
                    Log.i("789", meal.getMealname());
                    listMenu.add(meal);
                }
                Message m = new Message();
                m.what = UPDATE_MEAL_LIST;
                handler.sendMessage(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
