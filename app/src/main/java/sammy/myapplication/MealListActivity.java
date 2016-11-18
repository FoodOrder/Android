package sammy.myapplication;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MealListActivity extends AppCompatActivity {
    static String URL = "http://140.134.26.71:58080/android-backend/webapi/menu/id/";
    static String Shopemail;
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

    AdapterView.OnItemClickListener onClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        }
    };
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater =  getLayoutInflater();

        final View view1 = inflater.inflate(R.layout.activity_meal_list, null);

        setContentView(view1);

        FloatingActionButton fab = (FloatingActionButton) view1.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MealListActivity.this, CartlistActivity.class);
                startActivity(intent);
                Snackbar.make(view, "前往購物車", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        getbundle();
        ListView mealListView = (ListView) this.findViewById(R.id.menulistView);
        MealListAdapter = new MealListAdapter(this, new ArrayList<Meal>());
        mealListView.setAdapter(MealListAdapter);


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        GetShopDetail();
                    }
                }).start();

        mealListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String fooddetail = URL+String.valueOf(listMenu.get(position).getMealid());
                Intent intent = new Intent(MealListActivity.this, AddFoodActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("fooddetail", fooddetail);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

            }

            private void updateMealList() {
                MealListAdapter.clear();
                MealListAdapter.addAll(listMenu);
            }


            void getbundle() {
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
                        for (int i = 0; i < jsonObis.length(); i++) {
                            Meal meal = new Meal();
                            meal.setMealid(((JSONObject) jsonObis.get(i)).getString("id"));
                            meal.setMealname(((JSONObject) jsonObis.get(i)).getString("menuName"));
                            meal.setMealprice(((JSONObject) jsonObis.get(i)).getInt("menuPrice"));
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
