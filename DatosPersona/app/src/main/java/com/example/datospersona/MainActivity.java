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
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_main);

        // retrieve status
        if (state != null) {
            this.userList = state.getString(Constants.MAIN_ACTIVITY_USER_LIST_STATUS);
        }

        // retrieve component and update user list
        this.userCanvas = findViewById(R.id.usersTextView);
        this.userCanvas.setText(this.userList);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedState) {
        String status = savedState.getString(Constants.MAIN_ACTIVITY_USER_LIST_STATUS);
        this.userCanvas.setText(status);
    }

    @Override
    public void onSaveInstanceState(Bundle currentState) {
        currentState.putString(Constants.MAIN_ACTIVITY_USER_LIST_STATUS, this.userCanvas.getText().toString());
        super.onSaveInstanceState(currentState);
    }

    @Override
    protected void onActivityResult(int code, int result, Intent data) {

        super.onActivityResult(code, result, data);

        if (code == 1 && result == Activity.RESULT_OK && data != null) {

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

    public void cancel(View view) {
        finish();
    }
}