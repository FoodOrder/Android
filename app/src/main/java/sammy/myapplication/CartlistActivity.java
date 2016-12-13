package sammy.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.Entity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class CartlistActivity extends AppCompatActivity {
    private static final String ADDURL = "http://140.134.26.71:58080/android-backend/webapi/order/addOrder";
    ArrayList<Meal> Orderlist = new ArrayList<Meal>();
    ArrayList<Meal> Orderlist1 = new ArrayList<Meal>();
    public static final String KEY = "com.my.package.app";
    String item ="";
    String ADDRESS = null;
    String REMARK = null;
    String userID1;
    int id =1;
    private CartlistAdapter carlistadapter;

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
        SharedPreferences spref = getApplication().getSharedPreferences(KEY, Context.MODE_PRIVATE);
        userID1 = spref.getString("userID" , null);
        getbundle();
        setMyLoc();
        final ListView cartListView = (ListView) this.findViewById(R.id.cartlistView);
        carlistadapter = new CartlistAdapter(this,android.R.layout.activity_list_item, Orderlist1);
        cartListView.setAdapter(carlistadapter);
        mgr = (LocationManager) getSystemService(LOCATION_SERVICE);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                send();

                Toast.makeText(CartlistActivity.this, "訂單已送出", Toast.LENGTH_LONG).show();

                CartlistActivity.this.finish();
            }
        });


    }

    void getbundle() {
        Intent intent = this.getIntent();
        Orderlist = (ArrayList<Meal>) intent.getSerializableExtra("FinalOrder");
        for (int i = 0; i < Orderlist.size(); i++) {
            System.out.print(Orderlist.get(i).getMealid() + ",");
            System.out.print(Orderlist.get(i).getMealnumber() + "|");
        }
        for (int i = 0; i < Orderlist.size(); i++) {
            if (Orderlist.get(i).getMealnumber() != 0) {
                Orderlist1.add(Orderlist.get(i));
            }
        }
    }

    void send() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                try {
                    URL url = new URL(ADDURL);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setDoOutput(true);
                    conn.setRequestMethod("POST");
                    conn.setUseCaches(false);
                    conn.setConnectTimeout(10000);
                    conn.setReadTimeout(10000);
                    conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                    conn.connect();

                    String userID = "\"userId\":"+userID1;
                    String LOG = "\"longitude\":"+currentLong;
                    String LAT = "\"latitude\":"+currentLat;
                    EditText address = (EditText)findViewById(R.id.address);
                    EditText remark = (EditText)findViewById(R.id.remark);
                    ADDRESS = "\"address\":\""+address.getText().toString()+"\"";
                    REMARK =  "\"remark\":\""+remark.getText().toString()+"\"";
                    for (int i = 0; i < Orderlist1.size(); i++) {
                            if(i != Orderlist1.size()-1) {
                                item = item + "{\"foodId\":" + Orderlist1.get(i).getMealid() + ",\"amount\":" + Orderlist1.get(i).getMealnumber() + "},";
                            }else{
                                item = item + "{\"foodId\":" + Orderlist1.get(i).getMealid() + ",\"amount\":" + Orderlist1.get(i).getMealnumber() + "}";
                            }
                    }

                    String jsonString = "{\"items\":["+ item+"],"+userID+","+LOG+","+LAT+","+ADDRESS+","+REMARK+"}";

                    System.out.println(jsonString);

                    Log.v("Andy", jsonString);
                    DataOutputStream localDataOutputStream = new DataOutputStream(conn.getOutputStream());
                    byte[] bytes = jsonString.getBytes("UTF-8");
                    localDataOutputStream.write(bytes,0, bytes.length);
                    localDataOutputStream.flush();
                    localDataOutputStream.close();

                    int HttpResult =conn.getResponseCode();
                    if(HttpResult ==HttpURLConnection.HTTP_OK){
                        BufferedReader br = new BufferedReader(new InputStreamReader(
                                conn.getInputStream(),"UTF-8"));
                        String line = null;
                        StringBuffer sb = new StringBuffer();
                        while ((line = br.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        br.close();

                        System.out.println(""+sb.toString());

                    }else{
                        System.out.println(conn.getResponseMessage());
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finally {
                    if(conn != null) {
                        conn.disconnect();
                    }
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
                    TextView tvShow = (TextView)findViewById(R.id.textView10);
                     tvShow.setText( "緯度 : " + String.valueOf(currentLat) + "\t經度 : " + String.valueOf(currentLong));
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


