package com.gandalfran.datospersonalist.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.gandalfran.datospersonalist.R;

import com.gandalfran.datospersonalist.bean.UnaPersona;

import java.util.ArrayList;
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

        // populate
        name.setText(String.format("%s %s", p.getName(), p.getSurname()));
        age.setText(p.getAge());
        date.setText(p.getDate());

        return rowView;
    }
}
