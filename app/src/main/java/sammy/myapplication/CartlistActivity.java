package sammy.myapplication;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
  //  private ArrayList<item> foodlist = new ArrayList<item>();
 /*   public static final String KEY = "com.my.package.app";
    int foodid;
    int foodprice;
    int foodnumber;
    Gson gson1 = new Gson();
    Gson gson2 = new Gson();*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartlist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        System.out.print("kfofoijfoijcijfioqwjfoi");
        send();
        System.out.print("ookokpokopkk");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //  AsyncTaskRunner postReq = new AsyncTaskRunner();
                //   postReq.doInBackground();
                // postReq.execute("start");
                System.out.print("kfofoijfoijcijfioqwjfoi");
                send();
                System.out.print("ookokpokopkk");

                Snackbar.make(view, "訂單已送達 感謝使用", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


   /*     SharedPreferences spref = getApplication().getSharedPreferences(KEY, Context.MODE_PRIVATE);

        foodprice = spref.getInt("foodid", 0);

        System.out.println("foodid" + foodid);

        foodprice = spref.getInt("foodprice", 0);

            System.out.println("foodprice"+foodprice);

         foodnumber = spref.getInt("foodnumber", 0);

            System.out.println("foodnumber = " + foodnumber);

        item item1 = new item();

        item1.setAmount(foodnumber);
        item1.setFoodId(foodid);

        foodlist.add(item1);

        data data = new data();
        data.setMembers(foodlist);

        gson1.toJson(data);




    }
    class item{
        int foodId;

        public int getFoodId() {
            return foodId;
        }

        public void setFoodId(int foodId) {
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

*/
    }
    void send(){
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
                System.out.print("32403284239048918429038402");
                JSONObject cred = new JSONObject();
                cred.put("userId", "10");
                System.out.print("1111111111111111111111");
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



/*private class AsyncTaskRunner extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground(String... params) {
            try {
                URL object=new URL("http://140.134.26.71:58080/android-backend/webapi/order/addOrder");

                HttpURLConnection con = (HttpURLConnection) object.openConnection();
                con.setReadTimeout(15000);
                con.setConnectTimeout(15000);
                con.setDoOutput(true);
                con.setDoInput(true);
                //con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                con.setRequestMethod("POST");

                System.out.print("32403284239048918429038402");
                JSONObject cred = new JSONObject();
                cred.put("userId","10");
                System.out.print("1111111111111111111111");
                DataOutputStream localDataOutputStream = new DataOutputStream(con.getOutputStream());
                localDataOutputStream.writeBytes(cred.toString());
                localDataOutputStream.flush();
                localDataOutputStream.close();
            }
            catch (Exception e){
                Log.v("ErrorAPP",e.toString());
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }*/

}


