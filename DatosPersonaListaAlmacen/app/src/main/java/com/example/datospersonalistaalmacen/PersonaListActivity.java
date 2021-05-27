package com.example.datospersonalistaalmacen;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.datospersonalistaalmacen.adapter.UnaPersonaAdapter;
import com.example.datospersonalistaalmacen.bean.UnaPersona;
import com.example.datospersonalistaalmacen.model.UnaPersonaStorage;

import java.util.ArrayList;

public class PersonaListActivity extends Activity {

    public static final String PERSONA_LIST_ACTIVITY_USER_LIST_STATUS = "PERSONA_LIST_ACTIVITY_USER_LIST_STATUS";

    private LinearLayout buttonsLayout;
    private ListView userCanvas;
    private ProgressBar progressBar;

    private UnaPersonaAdapter adapter;
    private UnaPersonaStorage storage;
    private SharedPreferences settings;

    public PersonaListActivity(){
        this.storage  = new UnaPersonaStorage(this);
    }

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_persona_list);

        this.settings = this.getSharedPreferences(SettingsActivity.SETTINGS_KEY, Context.MODE_PRIVATE);

        this.progressBar =  findViewById(R.id.progressBar);
        this.userCanvas = findViewById(R.id.usersListView);
        this.buttonsLayout = findViewById(R.id.buttonsLayout);

        // retrieve component, set listeners and update user list
        this.adapter = new UnaPersonaAdapter(this, (ArrayList<UnaPersona>) this.storage.getPersonaList());
        this.userCanvas.setAdapter(this.adapter);
        this.userCanvas.setOnItemClickListener(this.updateUser);
        this.userCanvas.setOnItemLongClickListener(this.deleteUser);

        // retrieve the instance status
        if (state != null) {
            Parcelable status = state.getParcelable(PERSONA_LIST_ACTIVITY_USER_LIST_STATUS);
            this.userCanvas.onRestoreInstanceState(status);
        }

        // create storage and populate
        this.storage  = new UnaPersonaStorage(this);
        this.populateTable();
    }

    @Override
    public void onRestoreInstanceState(Bundle savedState) {
        Parcelable state = (Parcelable) savedState.getSerializable(PERSONA_LIST_ACTIVITY_USER_LIST_STATUS);
        if(state == null)
            return;
        else
            this.userCanvas.onRestoreInstanceState(state);
    }

    @Override
    public void onSaveInstanceState(Bundle currentState) {
        currentState.putParcelable(PERSONA_LIST_ACTIVITY_USER_LIST_STATUS, this.userCanvas.onSaveInstanceState());
        super.onSaveInstanceState(currentState);
    }

    @Override
    protected void onActivityResult(int code, int result, Intent data) {

        super.onActivityResult(code, result, data);

        if (code == 1 && result == Activity.RESULT_OK && data != null) {
            //retrieve
            UnaPersona p = (UnaPersona) data.getSerializableExtra(EditPersonaActivity.PERSON_KEY);

            // update user list
            this.storage.add(p);
            this.adapter.notifyDataSetChanged();
        } else if (code == 2 && result == Activity.RESULT_OK && data != null) {
            //retrieve
            int position = data.getIntExtra(EditPersonaActivity.POSITION_KEY, 0);
            UnaPersona p = (UnaPersona) data.getSerializableExtra(EditPersonaActivity.PERSON_KEY);

            // update user list
            this.storage.update(p,position);
            this.adapter.notifyDataSetChanged();
        }
    }

    public void addUser(View view) {
        Intent intent = new Intent(this, EditPersonaActivity.class);
        intent.putExtra(EditPersonaActivity.IS_MODIFY, false);
        startActivityForResult(intent, 1);
    }

    private AdapterView.OnItemClickListener updateUser = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(PersonaListActivity.this, EditPersonaActivity.class);

            // Add extra attributes
            intent.putExtra(EditPersonaActivity.IS_MODIFY, true);
            intent.putExtra(EditPersonaActivity.POSITION_KEY, position);
            intent.putExtra(EditPersonaActivity.PERSON_KEY, PersonaListActivity.this.adapter.getItem(position));

            startActivityForResult(intent, 2);
        }
    };

    private AdapterView.OnItemLongClickListener deleteUser = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            UnaPersona user = PersonaListActivity.this.adapter.getItem(position);

            new AlertDialog.Builder(PersonaListActivity.this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(getResources().getString(R.string.activity_user_list_delete_dialog_title))
                    .setMessage(getResources().getString(R.string.activity_user_list_delete_dialog_description) + ' ' + user.getName() + ' ' + user.getSurname() + '?' )
                    .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            PersonaListActivity.this.adapter.remove(user);
                        }
                    })
                    .setNegativeButton(getResources().getString(R.string.no), null)
                    .show();
            return true;
        }
    };

    public void cancel(View view) {
        finish();
    }

    public void save(View view){
        UnaPersonaStorage.StorageType type = UnaPersonaStorage.StorageType.values()[PersonaListActivity.this.settings.getInt(SettingsActivity.STORAGE_TYPE_KEY, UnaPersonaStorage.StorageType.CONTENT_PROVIDER.ordinal())];
        ContextWrapper cw = new ContextWrapper(this.getApplicationContext());
        this.storage.store(cw, type);
        finish();
    }

    private void populateTable(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                String uri = PersonaListActivity.this.settings.getString(SettingsActivity.URL_KEY, "");
                PersonaListActivity.this.storage.load(uri);

                //try {
                //    Thread.sleep(4000);
                //} catch (InterruptedException e) {
                //    e.printStackTrace();
                //}

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        PersonaListActivity.this.progressBar.setVisibility(View.GONE);
                        PersonaListActivity.this.userCanvas.setVisibility(View.VISIBLE);
                        PersonaListActivity.this.buttonsLayout.setVisibility(View.VISIBLE);

                        PersonaListActivity.this.adapter = new UnaPersonaAdapter(PersonaListActivity.this, (ArrayList<UnaPersona>) PersonaListActivity.this.storage.getPersonaList());
                        PersonaListActivity.this.userCanvas.setAdapter(PersonaListActivity.this.adapter);
                        PersonaListActivity.this.userCanvas.setOnItemClickListener(PersonaListActivity.this.updateUser);
                        PersonaListActivity.this.userCanvas.setOnItemLongClickListener(PersonaListActivity.this.deleteUser);
                    }
                });
            }
        }).start();
    }
}