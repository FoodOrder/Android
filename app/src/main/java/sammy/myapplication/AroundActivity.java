package sammy.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class AroundActivity extends AppCompatActivity {
    ListView aroundListView;
    Double compare = 0.0;
    private static final String SERVICE_URL = "http://140.134.26.71:58080/android-backend/webapi/shop/list";
    private static final double EARTH_RADIUS = 6378.137;
    private static final int UPDATE_SHOP_LIST = 1;
    private ArrayList<Shop> listShops = new ArrayList<Shop>();
    static final int MIN_TIME = 5000;// 位置更新條件：5000 毫秒
    static final float MIN_DIST = 5; // 位置更新條件：5 公尺
    LocationManager mgr;        // 定位總管
    private AroundAdapter aroundAdapter;
    LocationListener myLocListener;
    Double currentLat = 0.0, currentLong = 0.0;
    Location currentLocation;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_SHOP_LIST: {
                    updateShopList();
                    break;
                }
            }
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_around);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        aroundListView = (ListView) this.findViewById(R.id.aroundlistView);
        aroundAdapter = new AroundAdapter(this, new ArrayList<Shop>());
        aroundListView.setAdapter(aroundAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentLat.equals(compare)&&currentLong.equals(compare)){
                    Toast.makeText(AroundActivity.this, "請等待GPS載入完成", Toast.LENGTH_LONG).show();
                }else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            getShopListFromServer();
                        }
                    }).start();
                    Snackbar.make(view, "定位完成,如距離移動請再次點擊定位", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        mgr = (LocationManager) getSystemService(LOCATION_SERVICE);
        setMyLoc();
        System.out.println("Lat : " + String.valueOf(currentLat) + "\nLong : " + String.valueOf(currentLong));
    }

    private void updateShopList() {
        for (int i=0; i<listShops.size() ; i++){
            for(int j=0;i>j;j++){
                if(listShops.get(i).getDistance()<listShops.get(j).getDistance()){
                    Double temp;
                    String tempname;
                    String tempphone;
                    tempname = listShops.get(i).getName();
                    temp = listShops.get(i).getDistance();
                    tempphone = listShops.get(i).getTel();
                    listShops.get(i).setTel(listShops.get(j).getTel());
                    listShops.get(i).setName(listShops.get(j).getName());
                    listShops.get(i).setDistance(listShops.get(j).getDistance());
                    listShops.get(j).setDistance(temp);
                    listShops.get(j).setName(tempname);
                    listShops.get(j).setTel(tempphone);
                }
            }
        }
        aroundAdapter.clear();
        aroundAdapter.addAll(listShops);
        listShops.clear();
    }


    void setMyLoc() {
        Log.i("abc", "123");
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
                    TextView latlong = (TextView)findViewById(R.id.latlong);
                    latlong.setText("Lat : " + String.valueOf(currentLat) + "\tLong : " + String.valueOf(currentLong));
                    System.out.println("Lat : " + String.valueOf(currentLat) + "\nLong : " + String.valueOf(currentLong));
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
    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }
    public double GetDistance(double lat1, double lng1, double lat2, double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(
                Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 100);
        return (int)s * 10;
    }

    void getShopListFromServer() {


        HttpURLConnection conn = null;
        try {
            // 建立連線
            URL url = new URL(SERVICE_URL);
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
                    Shop shop = new Shop();
                    shop.setName(((JSONObject) jsonObis.get(i)).getString("shopName"));
                    shop.setTel(((JSONObject) jsonObis.get(i)).getString("phone"));
                    shop.setLatitude(Double.parseDouble(((JSONObject) jsonObis.get(i)).getString("latitude")));
                    shop.setLongitude(Double.parseDouble(((JSONObject) jsonObis.get(i)).getString("longitude")));
                    shop.setDistance(GetDistance(Double.parseDouble(((JSONObject) jsonObis.get(i)).getString("latitude")), Double.parseDouble(((JSONObject) jsonObis.get(i)).getString("longitude")), currentLat, currentLong));
                    System.out.println(shop.getDistance());
                    listShops.add(shop);
                }
                Message m = new Message();
                m.what = UPDATE_SHOP_LIST;
                handler.sendMessage(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
