package sammy.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class LoginActivity extends AppCompatActivity {
    public static final String KEY = "com.my.package.app";
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CHECK_LOGIN: {
                    successornot();
                    break;
                }
            }
        }

    };

    private static final int CHECK_LOGIN = 1;

    public String getSORN() {
        return SORN;
    }

    public void setSORN(String SORN) {
        this.SORN = SORN;
    }

    private String SORN;

    private final String success="true";

    private EditText loginEmail;

    private EditText loginPass;

    private Button btnLogin;


    private static final String LOGIN_API_URL = "http://140.134.26.71:58080/android-backend/webapi/user/validate";

    public void GotoSignUpAct(View v) {
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initUIComponents();
    }
    private void initUIComponents() {

        loginEmail = (EditText)findViewById(R.id.login_email);
        loginPass = (EditText)findViewById(R.id.login_pass);
        btnLogin = (Button) findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                submitRegistration();
            }

        });


    }

    private void submitRegistration(){
        final String email = loginEmail.getText().toString();
        final String pass = loginPass.getText().toString();

        new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    String response = "";
                    URL url = new URL(LOGIN_API_URL);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(15000);
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);


                    HashMap<String, String> postDataParams = new HashMap<>();
                    postDataParams.put("email", email);
                    postDataParams.put("password", pass);

                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(getPostDataString(postDataParams));

                    writer.flush();
                    writer.close();
                    os.close();
                    int responseCode=conn.getResponseCode();
                    if (responseCode == HttpsURLConnection.HTTP_OK) {
                        String line;
                        BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        while ((line=br.readLine()) != null) {
                            response+=line;
                        }
                        setSORN(response);
                    }
                    else {
                        response="";
                    }
                    Log.v("sammy", response);
                    Message m = new Message();
                    m.what = CHECK_LOGIN;
                    handler.sendMessage(m);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void successornot(){
        if(getSORN().equalsIgnoreCase(success)){
            SharedPreferences spref = getApplication().getSharedPreferences(KEY, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = spref.edit();
            editor.putString("email",loginEmail.getText().toString());
            editor.commit();
            Toast.makeText(LoginActivity.this,"登入成功",Toast.LENGTH_LONG).show();
            Intent intent = new Intent();
            intent.setClass(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            LoginActivity.this.finish();
        }else{
            Toast.makeText(LoginActivity.this,"帳號或密碼有誤",Toast.LENGTH_LONG).show();
        }
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }


}
