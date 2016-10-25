package com.example.excel16.sampleforhuggens;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by jerin on 24/10/16.
 */

public class slidescreenfragment extends Fragment {
    private String title;
    private int page;

    // newInstance constructor for creating fragment with arguments
    public static slidescreenfragment newInstance(int page) {
        slidescreenfragment fragmentFirst = new slidescreenfragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);

        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);

    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_slidescreen, container, false);
        TextView tvLabel = (TextView) view.findViewById(R.id.tvLabel);
        RelativeLayout back=(RelativeLayout) view.findViewById(R.id.back);

        switch(page){

            case 0: back.setBackgroundColor(Color.parseColor("#de8686"));
                tvLabel.setText("Sign in with google and facebook");
                break;
            case 1:back.setBackgroundColor(Color.parseColor("#759ded"));
                tvLabel.setText("weather details of cochin");
                break;
            case 2:back.setBackgroundColor(Color.parseColor("#62ec5d"));
                tvLabel.setText("weather details of 5 places users like");
                break;

        }
        return view;
    }
}
