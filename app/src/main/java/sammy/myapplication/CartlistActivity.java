package sammy.myapplication;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class CartlistActivity extends AppCompatActivity {
    private static final String ADDURL="http://140.134.26.71:58080/android-backend/webapi/order/addOrder";
    ArrayList<Meal> Orderlist = new ArrayList<Meal>();
    data Orderdata =new data();
    ArrayList<item> Orderitem = new ArrayList<item>();
    Gson gson = new Gson();
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartlist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getbundle();

  /*      for (int i=0 ; i < Orderlist.size() ; i++){
            Orderitem.get(i).setFoodId(Orderlist.get(i).getMealid());
            Orderitem.get(i).setAmount(Orderlist.get(i).getMealnumber());
        }*/
        Orderdata.setMembers(Orderitem);
        gson.toJson(Orderdata);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "訂單已清除", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });






    }
    class item{
        String foodId;

        public String getFoodId() {
            return foodId;
        }

        public void setFoodId(String foodId) {
            this.foodId = foodId;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        int amount;
    }
    class data{
        private int userId = 10;

        public List<item> getMembers() {
            return members;
        }

        public void setMembers(List<item> members) {
            this.members = members;
        }

        @SerializedName("items")
        List<item> members = new ArrayList<item>();


    }

    void getbundle() {
        Intent intent = this.getIntent();
        Orderlist = (ArrayList<Meal>)intent.getSerializableExtra("FinalOrder");
        for (int i=0 ; i < Orderlist.size() ; i++){
            Orderitem.get(i).setFoodId(Orderlist.get(i).getMealid());
            Orderitem.get(i).setAmount(Orderlist.get(i).getMealnumber());
        }
    }
    void send(){
        final Gson fingson = gson;
    new Thread(new Runnable(){
        @Override
        public void run() {
            try {
                URL url = new URL(ADDURL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                JSONObject cred = new JSONObject();
                cred.put("sfd",fingson);
                cred.putOpt("sadas",fingson);
                DataOutputStream localDataOutputStream = new DataOutputStream(conn.getOutputStream());
                localDataOutputStream.writeBytes(cred.toString());
                localDataOutputStream.flush();
                localDataOutputStream.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }).start();
}




}


