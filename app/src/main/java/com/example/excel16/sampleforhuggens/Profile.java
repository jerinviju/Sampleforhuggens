package com.example.excel16.sampleforhuggens;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.internal.CallbackManagerImpl;
import com.github.siyamed.shapeimageview.CircularImageView;

import java.net.URL;

public class Profile extends AppCompatActivity {
    String MY_PREFS_NAME="logindetails";
    String Name,email,url1="";
    CircularImageView imageview;
    TextView name,emailtext;
    LinearLayout login,notlogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        imageview = (CircularImageView) findViewById(R.id.dp);
        name = (TextView) findViewById(R.id.name);
        emailtext = (TextView) findViewById(R.id.email);
        login = (LinearLayout) findViewById(R.id.login);
        notlogin = (LinearLayout) findViewById(R.id.notlogin);
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        Name = prefs.getString("name", "");
        email = prefs.getString("email", "");
        url1 = prefs.getString("dpurl", "");
        if (Name.equals("") && email.equals("") && url1.equals("")) {
            notlogin.setVisibility(View.VISIBLE);
            login.setVisibility(View.GONE);
        }
        else {
            if (url1.equals("")) {

                url1 = "http://megaicons.net/static/img/icons_sizes/311/1406/256/mario-icon.png";
            }

            name.setText(Name);
            emailtext.setText(email);
            if (imageview != null) {
                Bitmap bm = Slidescreen.cache.get("" + Name);
                if (bm == null) {
                    imgCache img = new imgCache();
                    Log.v("Here10Here", "10");
                    bm = img.getImageFromInternal(Name, Profile.this);
                    if (bm != null) {
                        //addBitmapToMemoryCache(""+d.getImageUrl(), bm);
                        Slidescreen.cache.put("" + Name, bm);
                        Log.v("Here1Here", "1");
                        imageview.setImageBitmap(bm);
                    } else {
                        Log.v("Here2Here", "2");
                        img.saveToInternalMemory(Name, url1, Profile.this, imageview);
                        //img.saveToRealmDatabase(d.getUserName(), d.getImageUrl(), getContext(), image);
                    }
                } else {
                    Log.v("Here11Here", "11");
                    imageview.setImageBitmap(bm);
                }
            }


        }
    }
}
