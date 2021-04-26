package com.gandalfran.datospersonalist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gandalfran.datospersonalist.adapter.UnaPersonaAdapter;
import com.gandalfran.datospersonalist.bean.UnaPersona;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView userCanvas;
    private UnaPersonaAdapter adapter;
    private List<UnaPersona> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // create unapersona list and populate
        this.userList  = new ArrayList<>();
        this.populateTable();

        // retrieve component, set listeners and update user list
        this.userCanvas = findViewById(R.id.usersListView);
        this.adapter = new UnaPersonaAdapter(this, (ArrayList<UnaPersona>) this.userList);
        this.userCanvas.setAdapter(this.adapter);
        this.userCanvas.setOnItemClickListener(this.updateUser);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            //retrieve
            UnaPersona p = (UnaPersona) data.getSerializableExtra(Constants.PERSON_KEY);

            // update user list
            this.userList.add(p);
            this.adapter.notifyDataSetChanged();
        } else if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {
            //retrieve
            int position = data.getIntExtra(Constants.POSITION_KEY, 0);
            UnaPersona p = (UnaPersona) data.getSerializableExtra(Constants.PERSON_KEY);

            // update user list
            this.userList.set(position, p);
            this.adapter.notifyDataSetChanged();
        }
    }

    public void addUser(View view) {
        Intent intent = new Intent(this, LanzaActividad.class);
        intent.putExtra(Constants.IS_MODIFY, false);
        startActivityForResult(intent, 1);
    }

    private AdapterView.OnItemClickListener updateUser = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(MainActivity.this, LanzaActividad.class);

            // Add extra attributes
            intent.putExtra(Constants.IS_MODIFY, true);
            intent.putExtra(Constants.POSITION_KEY, position);
            intent.putExtra(Constants.PERSON_KEY, MainActivity.this.adapter.getItem(position));

            startActivityForResult(intent, 2);
        }
    };

    public void cancel(View view) {
        finish();
    }

    private void populateTable(){
        this.userList.add(new UnaPersona("Pepe", "Perez Margin", "10",
                "26/04/20", "111222333",
                "ALTO", false));
        this.userList.add(new UnaPersona("David", "Bisbal", "20",
                "26/03/20", "111222336",
                "BAJO", false));
        this.userList.add(new UnaPersona("Martinez", "Arias", "30",
                "26/02/21", "111222334",
                "MEDIO", true));
    }
}