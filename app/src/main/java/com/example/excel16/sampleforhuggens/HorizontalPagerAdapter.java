package com.example.excel16.sampleforhuggens;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.EditTextPreference;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by GIGAMOLE on 7/27/16.
 */
public class HorizontalPagerAdapter extends PagerAdapter {


    private Context mContext;
    private LayoutInflater mLayoutInflater;

    TextView date,city,cold,degree,maxtemp,mintemp;
    ImageView irefresh;
    EditText editText;
    Button go;
    RelativeLayout contentlayer,searchlayer;
    ProgressDialog progressdialog;
     View view;
    int position;

    String url1="http://api.openweathermap.org/data/2.5/weather?q=London&APPID=fde4299692df4647a79a8f9413e70f8b";
    public HorizontalPagerAdapter(final Context context ) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        progressdialog=new ProgressDialog(context);
        progressdialog.setMessage("Loading please wait");
        progressdialog.setCancelable(false);

    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public int getItemPosition( Object object) {
        return POSITION_NONE;
    }

    @Override
    public Object instantiateItem(ViewGroup container,  int positio) {


            view = mLayoutInflater.inflate(R.layout.item, container, false);
this.position=positio;
        city=(TextView) view.findViewById(R.id.city);
        cold=(TextView) view.findViewById(R.id.cool);
        degree=(TextView) view.findViewById(R.id.temp);
        maxtemp=(TextView) view.findViewById(R.id.maxtemp);
        mintemp=(TextView) view.findViewById(R.id.mintemp);
        irefresh=(ImageView) view.findViewById(R.id.imagecloud) ;
        editText=(EditText) view.findViewById(R.id.edittext);
        go=(Button) view.findViewById(R.id.search_button);
        contentlayer=(RelativeLayout) view.findViewById(R.id.content_frame);
        searchlayer=(RelativeLayout) view.findViewById(R.id.serachlayer);
        SharedPreferences prefs = mContext.getSharedPreferences(position+"", MODE_PRIVATE);
        if(prefs.getString("pos", "").equals(position+"")){
            contentlayer.setVisibility(View.VISIBLE);
            setview(position);
            searchlayer.setVisibility(View.GONE);
        }
        else{
            contentlayer.setVisibility(View.GONE);
            searchlayer.setVisibility(View.VISIBLE);
        }
        go.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                                if(!editText.getText().toString().equals("")){
                    Toast.makeText(mContext, "Please fill the edittext", Toast.LENGTH_SHORT).show();
                }
                else{
                    progressdialog.show();
                    String tag_json_obj = "json_obj_req";
                              Toast.makeText(mContext, ""+editText.getText().toString(), Toast.LENGTH_SHORT).show();

                    JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                            url1, null,
                            new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        Toast.makeText(mContext, ""+response, Toast.LENGTH_SHORT).show();
                                        progressdialog.dismiss();
                                        if (response.getString("cod").equals("502")){
                                            Toast.makeText(mContext, "Please choose a correct place", Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            SharedPreferences.Editor editor = mContext.getSharedPreferences(position+"", MODE_PRIVATE).edit();
                                            editor.putString("pos",position+"");
                                            editor.commit();

                                            jsonparse(response,position);
                                        }

                                    }
                                    catch(Exception e){

                                    }
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



                }
            }
        });

        container.addView(view);
        return view;
    }

    @Override
    public boolean isViewFromObject( View view,  Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem( ViewGroup container,  int position,  Object object) {
        container.removeView((View) object);
    }
    public void jsonparse(JSONObject jsonobject,int position){

        try{
            SharedPreferences.Editor editor = mContext.getSharedPreferences(position+"", MODE_PRIVATE).edit();
            editor.putString("cold",position+"");


            editor.putString("name",jsonobject.getString("name"));
            editor.commit();
            JSONArray contacts = jsonobject.getJSONArray("weather");
            for (int i = 0; i < contacts.length(); i++) {
                JSONObject c = contacts.getJSONObject(i);

                editor.putString("cold",c.getString("description"));
                editor.putString("image",c.getString("main"));

            }
            JSONObject jobject=jsonobject.getJSONObject("main");

            editor.putString("degree",jobject.getString("temp")+" K");

            editor.putString("maxtemp","Maxtemp : "+jobject.getString("temp_max")+" K");

            editor.putString("mintemp",    "Mintemp : "+jobject.getString("temp_min")+" K");
            editor.commit();
            setview(position);
        }
        catch(Exception e){

        }

    }
    public  void setview(int position){

        SharedPreferences prefs = mContext.getSharedPreferences(position+"", MODE_PRIVATE);
            city.setText(prefs.getString("name", ""));
        cold.setText(prefs.getString("cold", ""));
           degree.setText(prefs.getString("degree", ""));
        maxtemp.setText(prefs.getString("maxtemp", ""));
        mintemp.setText(prefs.getString("mintemp", ""));
        if(prefs.getString("mintemp", "").equals("Rain")){
            irefresh.setBackgroundResource(R.drawable.rain1);
        }
        else if(prefs.getString("mintemp", "").equals("Clear")){
            irefresh.setBackgroundResource(R.drawable.clear);
        }
        else
            irefresh.setBackgroundResource(R.drawable.cloud);
    }
}
