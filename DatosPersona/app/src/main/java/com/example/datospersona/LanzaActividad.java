package com.example.datospersona;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class LanzaActividad extends AppCompatActivity {

    private final String DATE_FORMAT = "%d/%d/%d";

    private TextView dateInput;
    private EditText nameInput;
    private EditText ageInput;
    private EditText phoneInput;
    private EditText surnameInput;
    private CheckBox drivingLicenseInput;
    private RadioGroup englishLevelInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lanza_actividad);

        // retrieve inputs
        this.ageInput = findViewById(R.id.ageInput);
        this.dateInput = findViewById(R.id.dateInput);
        this.nameInput = findViewById(R.id.nameInput);
        this.phoneInput = findViewById(R.id.phoneInput);
        this.surnameInput = findViewById(R.id.surnameInput);
        this.englishLevelInput = findViewById(R.id.englishlevelInput);
        this.drivingLicenseInput = findViewById(R.id.drivingLicesnseInput);

        // initialize date
        this.dateInput.setText(this.getFormattedDate());
    }

    public void changeDate(View view) {

        // get current date in calendar format
        Calendar c = Calendar.getInstance();

        // display and add event listener to update date in user. Also set the stored date for user as default
        DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                String date = String.format(DATE_FORMAT,day,month,year);
                dateInput.setText(date);
            }
        }, c.get(Calendar.YEAR),c.get(Calendar.DAY_OF_MONTH),c.get(Calendar.MONTH));
        dpd.show();
    }

    public void addUser(View view) {

        // retrieve user data
        String age = this.ageInput.getText().toString();
        String name = this.nameInput.getText().toString();
        String phone = this.phoneInput.getText().toString();
        String surname = this.surnameInput.getText().toString();
        boolean hasDrivingLicense = this.drivingLicenseInput.isChecked();
        int selectedEnglishLevelButtonId = this.englishLevelInput.getCheckedRadioButtonId();
        String englishLevel = ((RadioButton) findViewById(selectedEnglishLevelButtonId)).getText().toString();

        // check
        if(name.isEmpty()){
            name = "Unknown";
        }
        if(surname.isEmpty()){
            surname = "Unknown";
        }
        if (age.isEmpty()) {
            age = "Unknown";
        }
        if (phone.isEmpty()) {
            phone = "Unknown";
        }

        // return data to main activity
        // NOTE: it also clears activity stack with FLAG_ACTIVITY_CLEAR_TOP
        Intent intentResult = new Intent(this, MainActivity.class);
        intentResult.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intentResult.putExtra(Constants.INTENT_AGE_KEY, age);
        intentResult.putExtra(Constants.INTENT_NAME_KEY, name);
        intentResult.putExtra(Constants.INTENT_PHONE_KEY, phone);
        intentResult.putExtra(Constants.INTENT_SURENAMES_KEY, surname);
        intentResult.putExtra(Constants.INTENT_DATE_KEY, getFormattedDate());
        intentResult.putExtra(Constants.INTENT_ENGLISH_LEVEL_KEY, englishLevel);
        intentResult.putExtra(Constants.INTENT_DRIVING_LICENSE_KEY, hasDrivingLicense);
        setResult(Activity.RESULT_OK, intentResult);

        finish();
    }

    public void reset(View view) {
        this.nameInput.getText().clear();
        this.ageInput.getText().clear();
        this.phoneInput.getText().clear();
        this.englishLevelInput.clearCheck();
        this.surnameInput.getText().clear();
        this.dateInput.setText(this.getFormattedDate());
        this.englishLevelInput.check(R.id.lowLevelEnglish);
        if (this.drivingLicenseInput.isChecked()){
            this.drivingLicenseInput.toggle();
        }
    }

    public void cancel(View view) {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

    private String getFormattedDate() {
        Calendar c = Calendar.getInstance();
        return String.format(this.DATE_FORMAT,c.get(Calendar.DAY_OF_MONTH),c.get(Calendar.MONTH),c.get(Calendar.YEAR));
    }
}
