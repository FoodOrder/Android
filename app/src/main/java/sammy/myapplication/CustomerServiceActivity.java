package sammy.myapplication;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
    private static final String ADDURL = "http://140.134.26.71:58080/android-backend/webapi/comment/register";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customerservice);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getbundle();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                                   send();
                Toast.makeText(CustomerServiceActivity.this, "服務訊息已送出", Toast.LENGTH_LONG).show();

                CustomerServiceActivity.this.finish();
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
                    EditText tvtype = (EditText)findViewById(R.id.type);
                    String TYPE = "";
                    TYPE = "\"type\":\""+tvtype.getText().toString()+"\"";
                    TextView tvcontent = (TextView)findViewById(R.id.content);
                    String CONTENT ="";
                    CONTENT =  "\"content\":\""+tvcontent.getText().toString()+"\"";

                    String jsonString = "{"+userID+","+TYPE+","+CONTENT+"}";

                    System.out.println(jsonString);

                    Log.v("Andy", jsonString);
                    DataOutputStream localDataOutputStream = new DataOutputStream(conn.getOutputStream());
                    localDataOutputStream.writeBytes(jsonString);
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
