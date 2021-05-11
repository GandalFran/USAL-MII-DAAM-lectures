package com.example.datospersonalistcarga;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.datospersonalistcarga.adapter.UnaPersonaAdapter;
import com.example.datospersonalistcarga.bean.UnaPersona;
import com.example.datospersonalistcarga.model.UnaPersonaStorage;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String MAIN_ACTIVITY_USER_LIST_STATUS = "MAIN_ACTIVITY_USER_LIST_STATUS";

    private ListView userCanvas;
    private UnaPersonaAdapter adapter;
    private List<UnaPersona> userList;

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_main);

        // create unapersona list and populate
        this.userList  = new ArrayList<>();
        this.populateTable();

        // retrieve component, set listeners and update user list
        this.userCanvas = findViewById(R.id.usersListView);
        this.adapter = new UnaPersonaAdapter(this, (ArrayList<UnaPersona>) this.userList);
        this.userCanvas.setAdapter(this.adapter);
        this.userCanvas.setOnItemClickListener(this.updateUser);
        this.userCanvas.setOnItemLongClickListener(this.deleteUser);

        // retrieve the instance status
        if (state != null) {
            Parcelable status = state.getParcelable(MAIN_ACTIVITY_USER_LIST_STATUS);
            this.userCanvas.onRestoreInstanceState(status);
        }

    }

    @Override
    public void onRestoreInstanceState(Bundle savedState) {
        Parcelable state = (Parcelable) savedState.getSerializable(MAIN_ACTIVITY_USER_LIST_STATUS);
        if(state == null)
            return;
        else
            this.userCanvas.onRestoreInstanceState(state);
    }

    @Override
    public void onSaveInstanceState(Bundle currentState) {
        currentState.putParcelable(MAIN_ACTIVITY_USER_LIST_STATUS, this.userCanvas.onSaveInstanceState());
        super.onSaveInstanceState(currentState);
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
        } else if (code == 2 && result == Activity.RESULT_OK && data != null) {
            //retrieve
            int position = data.getIntExtra(LanzaActividad.POSITION_KEY, 0);
            UnaPersona p = (UnaPersona) data.getSerializableExtra(LanzaActividad.PERSON_KEY);

            // update user list
            this.userList.set(position, p);
            this.adapter.notifyDataSetChanged();
        }
    }

    public void addUser(View view) {
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
                            MainActivity.this.adapter.remove(user);
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

    private void populateTable(){

        // TODO cambiar a cargar en segundo plano

        this.userList = new UnaPersonaStorage().load();
    }
}