package com.example.datospersona;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    private TextView userCanvas;

    private String userList = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // retrieve component and update user list
        this.userCanvas = findViewById(R.id.peopleListTextView);
        this.userCanvas.setText(this.userList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {

            //retrieve
            String name = data.getStringExtra(Constants.INTENT_NAME_KEY);
            String surname = data.getStringExtra(Constants.INTENT_SURENAMES_KEY);
            String age = data.getStringExtra(Constants.INTENT_AGE_KEY);
            String phone = data.getStringExtra(Constants.INTENT_PHONE_KEY);
            boolean hasDrivingLicense = data.getBooleanExtra(Constants.INTENT_DRIVING_LICENSE_KEY, false);
            String englishLevel = data.getStringExtra(Constants.INTENT_ENGLISH_LEVEL_KEY);
            String date = data.getStringExtra(Constants.INTENT_DATE_KEY);

            // build new person string
            StringBuilder sb = new StringBuilder();
            sb.append("Nombre: ").append(name).append(" ").append(surname)
                    .append("\n").append("Edad: ").append(age)
                    .append(" ").append("PC: ").append(hasDrivingLicense ? "SI" : "NO")
                    .append(" ").append("Inglés: ").append(englishLevel)
                    .append(" ").append("Teléfono: ").append(phone)
                    .append(" ").append("INGRESO: ").append(date);

            // update user list
            this.userList = String.format("%s%s\n",this.userList,sb.toString());
            this.userCanvas.setText(this.userList);
        }
    }

    public void addUser(View view) {
        Intent intent = new Intent(this, LanzaActividad.class);
        startActivityForResult(intent, 1);
    }
}