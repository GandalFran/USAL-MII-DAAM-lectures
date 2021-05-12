package com.example.datospersonalistcarga.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.datospersonalistcarga.MainActivity;
import com.example.datospersonalistcarga.R;

import com.example.datospersonalistcarga.Utils;
import com.example.datospersonalistcarga.bean.UnaPersona;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UnaPersonaAdapter extends ArrayAdapter<UnaPersona> {

    private final List<UnaPersona> userList;

    public UnaPersonaAdapter(Context context, ArrayList<UnaPersona> userList) {
        super(context, 0, userList);
        this.userList = userList;
    }

    @Override
    public UnaPersona getItem(int position){
        return this.userList.get(position);
    }

    @Override
    public int getCount(){
        return this.userList.size();
    }

    @Override
    public View getView(int position, View rowView, ViewGroup parent) {

        UnaPersona p = this.getItem(position);

        // if view is null, retrieve corresponding row
        if (rowView == null) {
            rowView = LayoutInflater.from(this.getContext()).inflate(R.layout.listview_user, parent, false);
        }

        // retrieve
        TextView name = rowView.findViewById(R.id.name);
        TextView age = rowView.findViewById(R.id.age);
        TextView date = rowView.findViewById(R.id.date);
        TextView phone = rowView.findViewById(R.id.phone);
        TextView englishLevel = rowView.findViewById(R.id.englishLevel);
        TextView drivingLicense = rowView.findViewById(R.id.drivingLicense);

        // populate
        name.setText(String.format("%s %s", p.getName(), p.getSurname()));
        age.setText(p.getAge());
        date.setText(Utils.getDateString(p.getDate()));
        phone.setText(p.getPhone());
        englishLevel.setText(this.getEnglishLevelString(rowView,p.getEnglishLevel()) );
        drivingLicense.setText( p.isDrivingLicense() ? rowView.getResources().getString(R.string.yes):rowView.getResources().getString(R.string.no));

        return rowView;
    }

    private String getEnglishLevelString(View context, UnaPersona.EnglishLevel level){
        switch (level){
            case LOW: return context.getResources().getString(R.string.english_level_low);
            case MEDIUM: return context.getResources().getString(R.string.english_level_medium);
            case HIGH: return context.getResources().getString(R.string.english_level_high);
            default: return null;
        }
    }
}
