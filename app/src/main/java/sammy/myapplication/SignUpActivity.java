package sammy.myapplication;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class SignUpActivity extends AppCompatActivity {

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CHECK_SIGNUP: {
                    successornot();
                    break;
                }
            }
        }

    };

    public String getSORN() {
        return SORN;
    }

    public void setSORN(String SORN) {
        this.SORN = SORN;
    }

    private String SORN;

    private final String success="true";

    private static final int CHECK_SIGNUP = 1;

    private EditText etusername;

    private EditText etEmail;

    private EditText etPass;

    private Button btnSignUp;

    private static final String SIGNUP_API_URL = "http://140.134.26.71:58080/android-backend/webapi/user/register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initUIComponents();
    }

    private void initUIComponents() {

        etEmail = (EditText)findViewById(R.id.et_email);
        etPass = (EditText)findViewById(R.id.et_pass);
        etusername = (EditText)findViewById(R.id.et_username);

        btnSignUp = (Button) findViewById(R.id.btn_signup);

        btnSignUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                submitRegistration();
            }
        });

    }

    private void submitRegistration(){
        final String email = etEmail.getText().toString();
        final String pass = etPass.getText().toString();
        final String username = etusername.getText().toString();

        new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    String response = "";
                    URL url = new URL(SIGNUP_API_URL);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(15000);
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);


                    HashMap<String, String> postDataParams = new HashMap<>();
                    postDataParams.put("email", email);
                    postDataParams.put("password", pass);
                    postDataParams.put("userName", username);

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
                    Log.v(username, response);
                    Message m = new Message();
                    m.what = CHECK_SIGNUP;
                    handler.sendMessage(m);

                    System.out.println(response);

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
            Toast.makeText(SignUpActivity.this, "註冊成功", Toast.LENGTH_LONG).show();
            Intent intent = new Intent();
            intent.setClass(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
        }else{
            Toast.makeText(SignUpActivity.this,"帳號已經有人使用",Toast.LENGTH_LONG).show();
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
