package sammy.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CartlistActivity extends AppCompatActivity {
    private static final String ADDURL = "http://140.134.26.71:58080/android-backend/webapi/order/addOrder";
    ArrayList<Meal> Orderlist = new ArrayList<Meal>();
    data Orderdata = new data();
    ArrayList<item> Orderitem = new ArrayList<item>();
    Gson gson = new Gson();
    int id =1;

    static final int MIN_TIME = 5000;// 位置更新條件：5000 毫秒
    static final float MIN_DIST = 0; // 位置更新條件：5 公尺
    LocationManager mgr;        // 定位總管
    LocationListener myLocListener;
    Double currentLat = 0.0, currentLong = 0.0;
    Location currentLocation;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartlist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getbundle();
        setMyLoc();
        send();
        mgr = (LocationManager) getSystemService(LOCATION_SERVICE);
        /*for (int i=0 ; i < Orderlist.size() ; i++){
            Orderitem.get(i).setFoodId(Orderlist.get(i).getMealid());
            Orderitem.get(i).setAmount(Orderlist.get(i).getMealnumber());
        }*/
      //  Orderdata.setMembers(Orderitem);
     //   gson.toJson(Orderdata);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                send();
                Snackbar.make(view, "訂單已清除", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


    }

    class item {
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

    class data {
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

    public JSONObject getJSONObject(){
        JSONObject obj = new JSONObject();
        try{
            for (int i = 0; i < Orderlist.size(); i++) {
            //    if(Orderlist.get(i).getMealnumber()!=0){
                    obj.put("Mealname",Orderlist.get(i).getMealid());
                    obj.put("MealAmout",Orderlist.get(i).getMealnumber());
            //    }
                System.out.println(Orderlist.get(i).getMealid());
                System.out.println(Orderlist.get(i).getMealnumber());
            }

        }catch (JSONException e){
            System.out.println("Default" + e.getMessage());
        }
    return obj;
    }


    void getbundle() {
        Intent intent = this.getIntent();
        Orderlist = (ArrayList<Meal>) intent.getSerializableExtra("FinalOrder");
        for (int i = 0; i < Orderlist.size(); i++) {
            System.out.println(Orderlist.get(i).getMealid());
            System.out.println(Orderlist.get(i).getMealnumber());
        }
     /*   for (int i = 0; i < Orderlist.size(); i++) {
            Orderitem.get(i).setFoodId(Orderlist.get(i).getMealid());
            Orderitem.get(i).setAmount(Orderlist.get(i).getMealnumber());
        }*/
    }

    void send() {
        final Gson fingson = gson;
        new Thread(new Runnable() {
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
                    //JSONObject cred = new JSONObject();


                    HashMap<String, String> postDataParams = new HashMap<>();
                    postDataParams.put("Id", "10");
                    postDataParams.put("shopId", "1");

                    JSONObject ooo = new JSONObject();
                    ooo.put("userId",id);


                    JSONArray jsonArray = new JSONArray();
                    for(int i=0 ; i < Orderlist.size() ; i++){
                        jsonArray.put(Orderlist.get(i).getJSONObject());
                    }
                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(String.valueOf(postDataParams));

                    // Log.v("Andy", cred.toString());
                    DataOutputStream localDataOutputStream = new DataOutputStream(conn.getOutputStream());
                    localDataOutputStream.writeBytes(jsonArray.toString());
                    localDataOutputStream.writeBytes(ooo.toString());
                    System.out.println(ooo.toString());
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

    void setMyLoc() {
        myLocListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i("abc", "getLoc");
                currentLocation = location;
                currentLat = currentLocation.getLatitude();
                currentLong = currentLocation.getLongitude();

                Log.i("abc", currentLat.toString() + "   Lat");
                Log.i("abc", currentLong.toString() + "    Long");
                if (currentLocation != null) {
                    // tvShow.setText( "Lat : " + String.valueOf(currentLat) + "\nLong : " + String.valueOf(currentLong));
                } else {
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        String provider = LocationManager.NETWORK_PROVIDER;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mgr.requestLocationUpdates(provider, MIN_TIME, MIN_DIST, myLocListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mgr.removeUpdates(myLocListener);
    }
}


