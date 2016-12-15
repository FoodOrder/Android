package sammy.myapplication;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class CustomerServiceActivity extends AppCompatActivity {
    String userID1;
    String TYPE1 ;
    TextView tvcontent ;
    private static final String ADDURL = "http://140.134.26.71:58080/android-backend/webapi/comment/register";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customerservice);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tvcontent = (TextView)findViewById(R.id.content);
        getbundle();
        Spinner spinner = (Spinner)findViewById(R.id.spinner);
        final String[] servecelist = {"修改使用者姓名","修改使用者密碼", "程式使用回饋", "舉報店家服務","其他"};
        ArrayAdapter<String> List = new ArrayAdapter<>(CustomerServiceActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                servecelist);
        spinner.setAdapter(List);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TYPE1 = servecelist[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override


            public void onClick(View view) {

                if (tvcontent.getText().toString().equals("")){
                    Snackbar.make(view, "請輸入內容", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }else{
                    send();
                    Toast.makeText(CustomerServiceActivity.this, "服務訊息已送出", Toast.LENGTH_LONG).show();
                    CustomerServiceActivity.this.finish();
                }
            }
        });

    }

    void getbundle() {
        Bundle bundle = this.getIntent().getExtras();
        userID1 = bundle.getString("userID");
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
                    String TYPE = "\"type\":\""+TYPE1+"\"";
                    tvcontent = (TextView)findViewById(R.id.content);
                    String CONTENT ="";
                    CONTENT =  "\"content\":\""+tvcontent.getText().toString()+"\"";

                    String jsonString = "{"+userID+","+TYPE+","+CONTENT+"}";

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
                                conn.getInputStream(),"utf-8"));
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
}
