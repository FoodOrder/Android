package sammy.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    public static final String KEY = "com.my.package.app";

    private ImageView GoToOrderButton;
    private ImageView SearchOrder;

    private ImageView ivSearchNearShop;
    private ImageView ivCustomerService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        GoToOrderButton =(ImageView) findViewById(R.id.buttonorder);
        GoToOrderButton.setImageResource(R.drawable.store);
        SearchOrder = (ImageView)findViewById(R.id.searchorder);
        SearchOrder.setImageResource(R.drawable.order);

        ivSearchNearShop = (ImageView)findViewById(R.id.searchNearShop);
        ivCustomerService = (ImageView)findViewById(R.id.service);
        ivSearchNearShop.setImageResource(R.drawable.nearshop);
        ivCustomerService.setImageResource(R.drawable.service);
    }
    //OrderActivity view change
     public void GotoOrderAct(View v) {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, OrderActivity.class);
        startActivity(intent);

    }
        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

            return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if(id == R.id.action_cart) {
            Intent intent = new Intent();
            intent.setClass(this, CartlistActivity.class);
            startActivity(intent);
        }

       if(id == R.id.action_account) {
           SharedPreferences spref = getApplication().getSharedPreferences(KEY, Context.MODE_PRIVATE);
           String URL= "http://140.134.26.71:58080/android-backend/webapi/user/email/";
           String email= URL+spref.getString("email", null);
           Intent intent = new Intent();
           Bundle bundle = new Bundle();
           bundle.putString("EMAIL", email);
           intent.putExtras(bundle);
           intent.setClass(this, AccountActivity.class);
           startActivity(intent);
        }

        if(id == R.id.action_signup) {
            Intent intent = new Intent();
            intent.setClass(this, SignUpActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
