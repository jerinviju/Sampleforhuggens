package com.example.excel16.sampleforhuggens;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.LruCache;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

public class Slidescreen extends AppCompatActivity implements  GoogleApiClient.OnConnectionFailedListener {
    FragmentPagerAdapter adapterViewPager;
    ImageView indicator1,indicator2,indicator3;
    TextView Skip;
    LoginButton loginButton;
    private CallbackManager callbackManager;
    private ImageView signInButton;
    private GoogleSignInOptions gso;
    private GoogleApiClient mGoogleApiClient;
    private int RC_SIGN_IN = 100;
    ImageView facebookhelper;
    String MY_PREFS_NAME="logindetails";
    static int cacheSize;
    public static LruCache<String, Bitmap> cache;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_slidescreen);
        ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager);

        setupfacebook();
        setupgooglelogin();

        indicator1=(ImageView) findViewById(R.id.indicator1);
        indicator2=(ImageView) findViewById(R.id.indicator2);
        indicator3=(ImageView) findViewById(R.id.indicator3);

        Skip=(TextView) findViewById(R.id.skip);

        Skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(Slidescreen.this,Mainpage.class);
                startActivity(intent);
                finish();
            }
        });
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        cacheSize = maxMemory / 8;

        //Initialize cache
        cache = new LruCache<String, Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };

        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);
        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        indicator1.setImageResource(R.drawable.indicator_circle_selected);
                        indicator2.setImageResource(R.drawable.indicator_circle_normal);
                        indicator3.setImageResource(R.drawable.indicator_circle_normal);

                        break;

                    case 1:
                        indicator1.setImageResource(R.drawable.indicator_circle_normal);
                        indicator2.setImageResource(R.drawable.indicator_circle_selected);
                        indicator3.setImageResource(R.drawable.indicator_circle_normal);
                        break;

                    case 2:
                        indicator1.setImageResource(R.drawable.indicator_circle_normal);
                        indicator2.setImageResource(R.drawable.indicator_circle_normal);
                        indicator3.setImageResource(R.drawable.indicator_circle_selected);
                        break;

                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 3;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return slidescreenfragment.newInstance(0);
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return slidescreenfragment.newInstance(1);
                case 2: // Fragment # 1 - This will show SecondFragment
                    return slidescreenfragment.newInstance(2);
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }

    }

    public void setupfacebook(){

        facebookhelper = (ImageView) findViewById(R.id.facebookhelper);

        callbackManager = CallbackManager.Factory.create();

        loginButton = (LoginButton) findViewById(R.id.login_button);

        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_friends"));
        facebookhelper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginButton.performClick();
            }
        });


        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {

                        Log.i("LoginActivity", response.toString());
                        Bundle bFacebookData = getFacebookData(object);
                        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putString("name", bFacebookData.getString("first_name")+" "+bFacebookData.getString("last_name"));
                        editor.putString("email", bFacebookData.getString("email"));
                        editor.putString("dpurl",bFacebookData.getString("profile_pic"));
                        editor.commit();
                        Intent intent=new Intent(Slidescreen.this,Mainpage.class);
                        startActivity(intent);

                    }
                });


                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, email");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {
                // App code
                Toast.makeText(getApplicationContext(), "on cancel", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();

            }
        });


    }

    private Bundle getFacebookData(JSONObject object) {
        Bundle bundle = new Bundle();
        try {

            String id = object.getString("id");
            try {
                URL profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?width=200&height=150");
                Log.i("profile_pic", profile_pic + "");
                bundle.putString("profile_pic", profile_pic.toString());

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }


            bundle.putString("idFacebook", id);
            if (object.has("first_name"))
                bundle.putString("first_name", object.getString("first_name"));
            if (object.has("last_name"))
                bundle.putString("last_name", object.getString("last_name"));
            if (object.has("email"))
                bundle.putString("email", object.getString("email"));


        } catch (Exception e) {

        }
        return bundle;
    }

    public void setupgooglelogin(){
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        signInButton = (ImageView) findViewById(R.id.sign_in_button);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this , this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
            editor.putString("name", acct.getDisplayName());
            editor.putString("email", acct.getEmail());
            editor.putString("dpurl",acct.getPhotoUrl().toString());
            editor.commit();
            Intent intent=new Intent(Slidescreen.this,Mainpage.class);
            startActivity(intent);


        } else {
            Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show();
        }
    }

}
