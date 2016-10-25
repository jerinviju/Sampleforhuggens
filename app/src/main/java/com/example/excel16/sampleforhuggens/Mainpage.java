package com.example.excel16.sampleforhuggens;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Mainpage extends AppCompatActivity {
    private String[] mNavigationDrawerItemTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    Toolbar toolbar;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;
    GPSTracker gps;
    ProgressDialog progressdialog;
    String url="http://api.openweathermap.org/data/2.5/weather";
    double latitude,longitude;
    ImageView irefresh;
    String url1="http://api.openweathermap.org/data/2.5/weather?q=Kochi&APPID=fde4299692df4647a79a8f9413e70f8b";
    TextView date,city,cold,degree,maxtemp,mintemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);
        mTitle = mDrawerTitle = getTitle();
        mNavigationDrawerItemTitles= getResources().getStringArray(R.array.navigation_drawer_items_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        progressdialog=new ProgressDialog(Mainpage.this);
        progressdialog.setMessage("Loading please wait");
        progressdialog.setCancelable(false);

        date=(TextView) findViewById(R.id.date);
        city=(TextView) findViewById(R.id.city);
        cold=(TextView) findViewById(R.id.cool);
        degree=(TextView) findViewById(R.id.temp);
        maxtemp=(TextView) findViewById(R.id.maxtemp);
        mintemp=(TextView) findViewById(R.id.mintemp);
     irefresh=(ImageView) findViewById(R.id.imagecloud) ;

//        irefresh=(ImageView) findViewById(R.id.refresh);
//        irefresh.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                progressdialog.show();
//                String tag_json_obj = "json_obj_req";
//                url=url+"?lat="+longitude+"&lon="+longitude+"&cnt="+"7"+"&APPID=fde4299692df4647a79a8f9413e70f8b";
//                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
//                        url, null,
//                        new Response.Listener<JSONObject>() {
//
//                            @Override
//                            public void onResponse(JSONObject response) {
//                                Toast.makeText(Mainpage.this, ""+latitude+" "+longitude, Toast.LENGTH_SHORT).show();
//                                progressdialog.dismiss();
//                            }
//                        }, new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                          Toast.makeText(Mainpage.this, ""+error, Toast.LENGTH_SHORT).show();
//                        progressdialog.dismiss();
//                    }
//                });
//
//
//
//
//// Adding request to request queue
//                appcontroller.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
//
//
//            }
//        });

        setupToolbar();
        String tag_json_obj = "json_obj_req";

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url1, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        SharedPreferences.Editor editor = getSharedPreferences("json", MODE_PRIVATE).edit();
                        editor.putString("jsonstring", response.toString());

                        editor.commit();
                        jsonparse();
                        progressdialog.dismiss();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //   Toast.makeText(Mainpage.this, ""+error, Toast.LENGTH_SHORT).show();
                progressdialog.dismiss();
            }
        });




// Adding request to request queue
        appcontroller.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);




        DataModel[] drawerItem = new DataModel[3];

        drawerItem[0] = new DataModel(R.drawable.drawer_icon, "Profile");
        drawerItem[1] = new DataModel(R.drawable.drawer_icon, "Selections");
        drawerItem[2] = new DataModel(R.drawable.drawer_icon, "Exit");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);

        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.list_view_item_row, drawerItem);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        setupDrawerToggle();
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(c.getTime());
        String sub=formattedDate.substring(3,5);
        ;
        switch(Integer.parseInt(sub)){
            case 01:
                   date.setText("Januvary "+formattedDate.substring(0,2));
                break;
            case 02:
                date.setText("February "+formattedDate.substring(0,2));
                break;
            case 03:
                date.setText("March "+formattedDate.substring(0,2));
                break;
            case 04:
                date.setText("April "+formattedDate.substring(0,2));
                break;
            case 05:
                date.setText("May "+formattedDate.substring(0,2));
                break;
            case 06:
                date.setText("June "+formattedDate.substring(0,2));
                break;
            case 07:
                date.setText("July "+formattedDate.substring(0,2));
                break;
            case 8:
                date.setText("August "+formattedDate.substring(0,2));
                break;
            case 9:
                date.setText("September "+formattedDate.substring(0,2));
                break;
            case 10:

                date.setText("October "+formattedDate.substring(0,2));
                break;
            case 11:
                date.setText("November "+formattedDate.substring(0,2));
                break;
            case 12:
                date.setText("December "+formattedDate.substring(0,2));
                break;
        }
        gps = new GPSTracker(Mainpage.this);
        if(gps.canGetLocation()){

           // Toast.makeText(this, ""+formattedDate, Toast.LENGTH_SHORT).show();
            progressdialog.show();
            latitude= gps.getLatitude();
            longitude = gps.getLongitude();

        }else
            gps.showSettingsAlert();




      //  else
       //     Toast.makeText(this, "Check your network", Toast.LENGTH_SHORT).show();

    }
    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

    }

    private void selectItem(int position) {

        Intent intent;

        switch (position) {
            case 0:
                intent= new Intent(Mainpage.this,Profile.class);
                startActivity(intent);
                break;
            case 1:
               intent =new Intent(Mainpage.this,Selections.class);
                startActivity(intent);
                break;
            case 2:
                 System.exit(0);
                break;

            default:
                break;
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    void setupToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    void setupDrawerToggle(){
        mDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(this,mDrawerLayout,toolbar,R.string.app_name, R.string.app_name);
        //This is necessary to change the icon of the Drawer Toggle upon state change.
        mDrawerToggle.syncState();
    }
    public boolean isNetworkAvailable(){

        ConnectivityManager connectivityManager
                = (ConnectivityManager) Mainpage.this.getSystemService(Mainpage.this.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void jsonparse(){
        SharedPreferences prefs = getSharedPreferences("json", MODE_PRIVATE);
        String  Name= prefs.getString("jsonstring", null);
        try{
            JSONObject jsonobject=new JSONObject(Name);
            city.setText(jsonobject.getString("name"));
            JSONArray contacts = jsonobject.getJSONArray("weather");
            for (int i = 0; i < contacts.length(); i++) {
                JSONObject c = contacts.getJSONObject(i);
                cold.setText(c.getString("description"));
                    if(c.getString("main").equals("Rain")){
                     irefresh.setBackgroundResource(R.drawable.rain1);
                }
                else if(c.getString("main").equals("Clear")){
                        irefresh.setBackgroundResource(R.drawable.clear);
                    }
                else
                        irefresh.setBackgroundResource(R.drawable.cloud);
            }
            JSONObject jobject=jsonobject.getJSONObject("main");
            degree.setText(jobject.getString("temp")+" K");
            maxtemp.setText("Maxtemp : "+jobject.getString("temp_max")+" K");
            mintemp.setText("Mintemp : "+jobject.getString("temp_min")+" K");

        }
        catch(Exception e){

        }

    }
}
