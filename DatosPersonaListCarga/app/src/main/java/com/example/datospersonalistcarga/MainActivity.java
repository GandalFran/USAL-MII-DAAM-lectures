package com.example.datospersonalistcarga;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.datospersonalistcarga.adapter.UnaPersonaAdapter;
import com.example.datospersonalistcarga.bean.UnaPersona;
import com.example.datospersonalistcarga.model.UnaPersonaStorage;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private ListView userCanvas;
    private UnaPersonaAdapter adapter;
    private List<UnaPersona> userList;

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_main);

        // create unapersona list
        this.userList  = new ArrayList<>();

        // retrieve component, set listeners and update user list
        this.userCanvas = findViewById(R.id.usersListView);
        this.progressBar = findViewById(R.id.progressBar);
        this.adapter = new UnaPersonaAdapter(this, (ArrayList<UnaPersona>) this.userList);
        this.userCanvas.setAdapter(this.adapter);
        this.userCanvas.setOnItemClickListener(this.updateUser);
        this.userCanvas.setOnItemLongClickListener(this.deleteUser);

        // if there is data stored retrieve
        if (UnaPersonaStorage.staticStorage != null){
            this.adapter.addAll(UnaPersonaStorage.staticStorage);
            this.adapter.notifyDataSetChanged();
        }else{
            UnaPersonaStorage.staticStorage = new ArrayList<>();
        }
    }

    @Override
    protected void onActivityResult(int code, int result, Intent data) {

        super.onActivityResult(code, result, data);

        if (code == 1 && result == Activity.RESULT_OK && data != null) {
            //retrieve
            UnaPersona p = (UnaPersona) data.getSerializableExtra(LanzaActividad.PERSON_KEY);

            // update user list
            this.userList.add(p);
            this.adapter.notifyDataSetChanged();

            // update static storage
            UnaPersonaStorage.staticStorage.add(p);
        } else if (code == 2 && result == Activity.RESULT_OK && data != null) {
            //retrieve
            int position = data.getIntExtra(LanzaActividad.POSITION_KEY, 0);
            UnaPersona p = (UnaPersona) data.getSerializableExtra(LanzaActividad.PERSON_KEY);

            // update user list
            this.userList.set(position, p);
            this.adapter.notifyDataSetChanged();

            // update static storage
            UnaPersonaStorage.staticStorage.set(position, p);
        }

    }

    public void throwFormulary(View view) {
        Intent intent = new Intent(this, LanzaActividad.class);
        intent.putExtra(LanzaActividad.IS_MODIFY, false);
        startActivityForResult(intent, 1);
    }

    private AdapterView.OnItemClickListener updateUser = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(MainActivity.this, LanzaActividad.class);

            // Add extra attributes
            intent.putExtra(LanzaActividad.IS_MODIFY, true);
            intent.putExtra(LanzaActividad.POSITION_KEY, position);
            intent.putExtra(LanzaActividad.PERSON_KEY, MainActivity.this.adapter.getItem(position));

            startActivityForResult(intent, 2);
        }
    };

    private AdapterView.OnItemLongClickListener deleteUser = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            UnaPersona user = MainActivity.this.adapter.getItem(position);

            new AlertDialog.Builder(MainActivity.this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(getResources().getString(R.string.activity_main_delete_dialog_title))
                    .setMessage(getResources().getString(R.string.activity_main_delete_dialog_description) + ' ' + user.getName() + ' ' + user.getSurname() + '?' )
                    .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MainActivity.this.userList.remove(user);
                            MainActivity.this.adapter.notifyDataSetChanged();
                            // update static storage
                            UnaPersonaStorage.staticStorage.remove(user);
                        }
                    })
                    .setNegativeButton(getResources().getString(R.string.no), null)
                    .show();
            return true;
        }
    };

    public void cancel(View view) {
        UnaPersonaStorage.staticStorage = null;
        finish();
    }

    public void loadUserData(View view){
        MainActivity.this.progressBar.setVisibility(View.VISIBLE);

        Thread th = new Thread(() -> {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 3000));
                    } catch (InterruptedException e) {
                    }
                    List<UnaPersona> users = new UnaPersonaStorage().load();

                    MainActivity.this.adapter.addAll(users);
                    MainActivity.this.userCanvas.setVisibility(View.VISIBLE);
                    MainActivity.this.progressBar.setVisibility(View.INVISIBLE);

                    // update static storage
                    UnaPersonaStorage.staticStorage.addAll(users);
                }
            });
        });
        th.start();
    }
}