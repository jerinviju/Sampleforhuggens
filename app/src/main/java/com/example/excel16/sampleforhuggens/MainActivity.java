package com.example.excel16.sampleforhuggens;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.MailTo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
      String Name,email,url1,MY_PREFS_NAME="logindetails";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                Name = prefs.getString("name", "");
                email = prefs.getString("email", "");
                url1 = prefs.getString("dpurl", "");
                if (Name.equals("") && email.equals("") && url1.equals("")) {
                    Intent intent=new Intent(MainActivity.this,Slidescreen.class);
                    startActivity(intent);
                    finish();
                }
                   else
                {
                    Intent intent=new Intent(MainActivity.this,Mainpage.class);
                    startActivity(intent);
                    finish();
                }

            }
        },2500);
    }
}
